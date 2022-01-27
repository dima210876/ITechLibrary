package my.itechart.studlabs.project.itechLibrary.controller.filter;

import my.itechart.studlabs.project.itechLibrary.command.WrappingRequestContext;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowReaderCreationForm;
import my.itechart.studlabs.project.itechLibrary.service.ReaderService;
import my.itechart.studlabs.project.itechLibrary.service.impl.ReaderServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@WebFilter(filterName = "ReaderCreationFilter", urlPatterns = {"/controller"})
public class ReaderCreationFilter implements Filter
{
    private final ReaderService readerService = ReaderServiceImpl.getInstance();

    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String CREATE_READER_COMMAND_VALUE = "create_reader";

    private static final String LAST_NAME_PARAMETER_NAME = "lastName";
    private static final String FIRST_NAME_PARAMETER_NAME = "firstName";
    private static final String PASSPORT_NUMBER_PARAMETER_NAME = "passportNumber";
    private static final String BIRTH_DATE_PARAMETER_NAME = "birthDate";
    private static final String EMAIL_PARAMETER_NAME = "email";

    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_ATTRIBUTE_VALUE = "Введены некорректные данные!" +
            " Электронная почта и номер паспорта (при указании) должны быть уникальными, а дата рождения - допустимой.";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        final String command = String.valueOf(servletRequest.getParameter(COMMAND_PARAMETER_NAME));
        if (command.equals(CREATE_READER_COMMAND_VALUE))
        {
            final String lastName = String.valueOf(servletRequest.getParameter(LAST_NAME_PARAMETER_NAME)).trim();
            final String firstName = String.valueOf(servletRequest.getParameter(FIRST_NAME_PARAMETER_NAME)).trim();
            final String passportNumber = String.valueOf(servletRequest.getParameter(PASSPORT_NUMBER_PARAMETER_NAME)).trim();
            final String birthDay = String.valueOf(servletRequest.getParameter(BIRTH_DATE_PARAMETER_NAME)).trim();
            final String email = String.valueOf(servletRequest.getParameter(EMAIL_PARAMETER_NAME)).trim();

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (lastName.isEmpty() || lastName.equals("null") ||
                    firstName.isEmpty() || firstName.equals("null") ||
                    (!passportNumber.isEmpty() && !checkPassportNumberUnique(passportNumber)) ||
                    birthDay.isEmpty() || birthDay.equals("null") ||
                    LocalDate.parse(birthDay, formatter).isBefore(LocalDate.of(1900,1,1)) || LocalDate.parse(birthDay, formatter).isAfter(LocalDate.now()) ||
                    email.isEmpty() || email.equals("null") ||
                    !checkEmailUnique(email))
            {
                final RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(ShowReaderCreationForm.INSTANCE.execute(WrappingRequestContext.of((HttpServletRequest) servletRequest)).getPage());
                servletRequest.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE);
                servletRequest.setAttribute(COMMAND_PARAMETER_NAME, command);
                dispatcher.forward(servletRequest, servletResponse);
            }
            else { filterChain.doFilter(servletRequest, servletResponse); }
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() { }

    private boolean checkPassportNumberUnique(String passportNumber)
    {
        return readerService.findAll().stream()
                .filter(readerDto -> Objects.equals(readerDto.getPassportNumber(), passportNumber))
                .count() == 0;
    }

    private boolean checkEmailUnique(String email)
    {
        return readerService.findAll().stream()
                .filter(readerDto -> Objects.equals(readerDto.getEmail(), email))
                .count() == 0;
    }
}
