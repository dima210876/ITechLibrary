package my.itechart.studlabs.project.itechLibrary.command.inputForms;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.dto.BorrowDto;
import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.service.BookService;
import my.itechart.studlabs.project.itechLibrary.service.BorrowService;
import my.itechart.studlabs.project.itechLibrary.service.ReaderService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BookServiceImpl;
import my.itechart.studlabs.project.itechLibrary.service.impl.BorrowServiceImpl;
import my.itechart.studlabs.project.itechLibrary.service.impl.ReaderServiceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum ShowBorrowCreationForm implements Command
{
    INSTANCE;

    private static final String READER_ID_PARAMETER_NAME = "readerId";
    private static final String READER_ATTRIBUTE_NAME = "reader";
    private static final String AVAILABLE_BOOKS_ATTRIBUTE_NAME = "availableBooks";

    private static final String EMPTY_ATTRIBUTE_VALUE = "null";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";

    private final BookService bookService = BookServiceImpl.getInstance();
    private final ReaderService readerService = ReaderServiceImpl.getInstance();
    private final BorrowService borrowService = BorrowServiceImpl.getInstance();

    private static final ResponseContext BORROW_CREATION_FORM_RESPONSE = new ResponseContext(UrlPatterns.CREATE_BORROW_ORDER, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String id = String.valueOf(request.getParameter(READER_ID_PARAMETER_NAME));
        final long readerId = (EMPTY_ATTRIBUTE_VALUE.equals(id) || id.isEmpty()) ? 0 : Integer.parseInt(id);
        ReaderDto readerDto = readerService.findById(readerId);
        if (readerDto != null)
        {
            List<BorrowDto> notReturnedBorrows = borrowService.findNotReturnedReaderBorrows(readerId);
            if (!notReturnedBorrows.isEmpty())
            {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, "У читателя есть не возвращённые им книги.");
            }
            else
            {
                request.resetParameter(ERROR_ATTRIBUTE_NAME);
                List<BookDto> availableBooks = bookService.findAll().stream()
                        .filter(bookDto -> bookDto.getAvailableCopyCount() > 0)
                        .sorted(Comparator.comparing(BookDto::getTitleRu))
                        .collect(Collectors.toList());
                request.setAttribute(READER_ATTRIBUTE_NAME, readerDto);
                request.setAttribute(AVAILABLE_BOOKS_ATTRIBUTE_NAME, availableBooks);
            }
        }
        else
        {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, "Читатель не найден.");
        }
        return BORROW_CREATION_FORM_RESPONSE;
    }
}
