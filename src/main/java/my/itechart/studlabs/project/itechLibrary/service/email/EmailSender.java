package my.itechart.studlabs.project.itechLibrary.service.email;

import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.dto.BorrowDto;
import my.itechart.studlabs.project.itechLibrary.service.BorrowService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BorrowServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EmailSender implements Job
{
    private static final Logger LOGGER = LogManager.getLogger(EmailSender.class);
    private static final String PROPS_FILE_NAME = "email.properties";
    private static final String USERNAME_PROPERTY = "email.username";
    private static final String PASSWORD_PROPERTY = "email.password";
    private static final String MAIL_FROM_PROPERTY = "mail.from";
    private static final String EMAIL_NOTIFICATION_SUBJECT_PROPERTY = "email.notification.subject";
    private static final String EMAIL_ALERT_SUBJECT_PROPERTY = "email.alert.subject";
    private static final String CURRENCY = "BYN";

    private final BorrowService borrowService = BorrowServiceImpl.getInstance();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        List<BorrowDto> borrowsToReturnToday = borrowService.findAll().stream()
                .filter(borrowDto -> LocalDate.now().isEqual(borrowDto.getReturnDate()))
                .filter(borrowDto -> "active".equals(borrowDto.getBorrowStatus()))
                .collect(Collectors.toList());
        borrowsToReturnToday.forEach(this::sendNotificationMail);

        if (!borrowService.updateBorrowStatus()) { throw new JobExecutionException("Can't update borrow statuses"); }
        List<BorrowDto> expiredBorrows = borrowService.findExpiredBorrows().stream()
                .filter(expiredBorrow ->
                        Duration.between(expiredBorrow.getReturnDate(), LocalDate.now()).toDays() >= 5)
                .collect(Collectors.toList());
        expiredBorrows.forEach(this::sendAlertMail);
    }

    private Optional<Properties> getEmailProperties()
    {
        final Properties props = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/" + PROPS_FILE_NAME))
        {
            props.load(inputStream);
            return Optional.of(props);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Email properties file not found: " + e.getLocalizedMessage());
        }
        catch (IOException e)
        {
            LOGGER.error("Error loading properties file: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    private void sendNotificationMail(BorrowDto borrowDto)
    {
        try
        {
            Message message = prepareMessage(borrowDto.getReader().getEmail().trim(), EMAIL_NOTIFICATION_SUBJECT_PROPERTY);
            List<BookDto> books = borrowDto.getRecords().stream()
                    .map(record -> record.getBookCopyDto().getBookDto())
                    .collect(Collectors.toList());
            StringBuilder textBuilder =
                    new StringBuilder("Your borrow for books with cost = %f expires today." +
                            " You need to return the following books to the library:\n");
            books.forEach(book -> textBuilder.append("- \"").append(book.getTitleRu()).append("\";\n"));
            textBuilder.deleteCharAt(textBuilder.length() - 1);
            textBuilder.append('.');
            message.setText(String.format(textBuilder.toString(), borrowDto.getCost()));
            Transport.send(message);
        }
        catch (NoSuchObjectException e)
        {
            LOGGER.error("Error getting email properties while trying to send email notification: " + e.getLocalizedMessage());
        }
        catch (MessagingException e)
        {
            LOGGER.error("Error while trying to send email notification: " + e.getLocalizedMessage());
        }
    }

    private void sendAlertMail(BorrowDto borrowDto)
    {
        try
        {
            Message message = prepareMessage(borrowDto.getReader().getEmail().trim(),EMAIL_ALERT_SUBJECT_PROPERTY);
            List<BookDto> books = borrowDto.getRecords().stream()
                    .map(record -> record.getBookCopyDto().getBookDto())
                    .collect(Collectors.toList());
            StringBuilder textBuilder =
                    new StringBuilder("Your borrow with cost = %f %s expired %d days ago, the amount of the current penalty is %f %s." +
                            " You need to return the following books to the library:\n");
            books.forEach(book -> textBuilder.append("- \"").append(book.getTitleRu()).append("\";\n"));
            textBuilder.deleteCharAt(textBuilder.length() - 1);
            textBuilder.append('.');
            double cost = borrowDto.getCost();
            long daysAfter = Duration.between(borrowDto.getReturnDate(), LocalDate.now()).toDays();
            double penalty = cost * daysAfter / 100;
            message.setText(String.format(textBuilder.toString(), cost, CURRENCY, daysAfter, penalty, CURRENCY));
            Transport.send(message);
        }
        catch (NoSuchObjectException e)
        {
            LOGGER.error("Error getting email properties while trying to send email alert: " + e.getLocalizedMessage());
        }
        catch (MessagingException e)
        {
            LOGGER.error("Error while trying to send email alert: " + e.getLocalizedMessage());
        }
    }

    private Message prepareMessage(String readerEmail, String subject) throws MessagingException, NoSuchObjectException
    {
        Properties props = getEmailProperties().orElseThrow(() -> new NoSuchObjectException("Email properties not found"));
        final String username = props.getProperty(USERNAME_PROPERTY);
        final String password = props.getProperty(PASSWORD_PROPERTY);
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(props.getProperty(MAIL_FROM_PROPERTY)));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(readerEmail));
        message.setSubject(props.getProperty(subject));
        return message;
    }
}
