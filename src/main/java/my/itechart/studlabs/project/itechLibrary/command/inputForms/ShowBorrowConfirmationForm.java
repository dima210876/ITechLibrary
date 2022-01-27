package my.itechart.studlabs.project.itechLibrary.command.inputForms;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookCopyDto;
import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.service.BookCopyService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BookCopyServiceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public enum ShowBorrowConfirmationForm implements Command
{
    INSTANCE;

    private static final String READER_PARAMETER_NAME = "reader";
    private static final String SELECTED_BOOKS_IDS_PARAMETER_NAME = "selectedBooksIds";
    private static final String RETURN_DATE_PARAMETER_NAME = "returnDate";

    private static final String BOOK_COPIES_ATTRIBUTE_NAME = "bookCopies";
    private static final String FULL_COST_ATTRIBUTE_NAME = "fullCost";
    private static final String DISCOUNT_PERCENT_ATTRIBUTE_NAME = "discountPercent";
    private static final String COST_ATTRIBUTE_NAME = "cost";

    private static final String EMPTY_ATTRIBUTE_VALUE = "null";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";

    private final BookCopyService bookCopyService = BookCopyServiceImpl.getInstance();

    private static final ResponseContext BORROW_CONFIRMATION_FORM_RESPONSE = new ResponseContext(UrlPatterns.CONFIRM_BORROW_ORDER, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String reader = String.valueOf(request.getParameter(READER_PARAMETER_NAME));
        if (EMPTY_ATTRIBUTE_VALUE.equals(reader) || reader.isEmpty())
        {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, "Неверно указан читатель.");
            return ShowBorrowCreationForm.INSTANCE.execute(request);
        }
        ReaderDto readerDto = (ReaderDto) request.getParameter(READER_PARAMETER_NAME);
        if (readerDto != null)
        {
            request.resetParameter(ERROR_ATTRIBUTE_NAME);
            String[] selBooksIds = request.getParameterValues(SELECTED_BOOKS_IDS_PARAMETER_NAME);
            int countOfSelectedBooks = selBooksIds.length;
            if (countOfSelectedBooks < 1 || countOfSelectedBooks > 5)
            {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, "Допускается выбрать не более 5 книг.");
                return ShowBorrowCreationForm.INSTANCE.execute(request);
            }
            List<BookCopyDto> selectedBookCopies = new ArrayList<>();

            for (String selBookId: selBooksIds)
            {
                long selectedBookCopyId = Long.parseLong(selBookId.trim());
                List<BookCopyDto> availableBookCopies = bookCopyService.findAvailableBookCopiesByBookId(selectedBookCopyId);
                if (!availableBookCopies.isEmpty()) { selectedBookCopies.add(availableBookCopies.get(0)); }
                else
                {
                    request.setAttribute(ERROR_ATTRIBUTE_NAME, "Доступного экзепляра необходимой книги не нашлось.");
                    return ShowBorrowCreationForm.INSTANCE.execute(request);
                }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate returnDate = LocalDate.parse(String.valueOf(request.getParameter(RETURN_DATE_PARAMETER_NAME)), formatter);

            double fullCost = 0;
            for (BookCopyDto selectedBookCopy : selectedBookCopies)
            {
                fullCost += selectedBookCopy.getBookDto().getDayCost();
            }
            fullCost *= Duration.between(LocalDate.now(), returnDate).toDays();
            int discountPercent = switch (countOfSelectedBooks)
                    {
                        case 2, 3 -> 10;
                        case 4, 5 -> 20;
                        default -> 0;
                    };
            double cost = fullCost * (1 - 0.01 * discountPercent);
            request.setAttribute(BOOK_COPIES_ATTRIBUTE_NAME, selectedBookCopies);
            request.setAttribute(FULL_COST_ATTRIBUTE_NAME, fullCost);
            request.setAttribute(DISCOUNT_PERCENT_ATTRIBUTE_NAME, discountPercent);
            request.setAttribute(COST_ATTRIBUTE_NAME, cost);
        }
        else
        {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, "Читатель не найден.");
        }
        return BORROW_CONFIRMATION_FORM_RESPONSE;
    }
}
