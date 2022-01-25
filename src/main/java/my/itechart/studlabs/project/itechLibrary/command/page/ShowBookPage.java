package my.itechart.studlabs.project.itechLibrary.command.page;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.Genre;
import my.itechart.studlabs.project.itechLibrary.service.BookService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BookServiceImpl;
import java.util.List;
import java.util.stream.Collectors;

public enum ShowBookPage implements Command
{
    INSTANCE;

    private static final String ID_PARAMETER_NAME = "bookId";
    private static final String BOOK_ATTRIBUTE_NAME = "book";
    private static final String BOOK_AUTHORS_ATTRIBUTE_NAME = "authors";
    private static final String BOOK_GENRES_ATTRIBUTE_NAME = "genres";
    private static final String BOOK_TOTAL_COUNT_ATTRIBUTE_NAME = "totalCopyCount";
    private static final String BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME = "availableCopyCount";
    private static final String BOOK_COVER_PHOTOS_ATTRIBUTE_NAME = "coverPhotos";

    private static final String EMPTY_ATTRIBUTE_VALUE = "null";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";

    private final BookService bookService = BookServiceImpl.getInstance();

    private static final ResponseContext BOOK_PAGE_RESPONSE = new ResponseContext(UrlPatterns.BOOK, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String id = String.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final long bookId = (EMPTY_ATTRIBUTE_VALUE.equals(id) || id.isEmpty()) ? 0 : Integer.parseInt(id);
        BookDto bookDto = bookService.findById(bookId);
        if (bookDto != null) { setRequestAttributes(request, bookDto); }
        return BOOK_PAGE_RESPONSE;
    }

    private void setRequestAttributes(RequestContext request, BookDto bookDto)
    {
        request.resetParameter(ERROR_ATTRIBUTE_NAME);
        request.setAttribute(BOOK_ATTRIBUTE_NAME, bookDto);
        request.setAttribute(BOOK_AUTHORS_ATTRIBUTE_NAME, parseList(bookDto.getAuthors().stream()
                .map(author -> author.getName() + " " + author.getSurname())
                .collect(Collectors.toList()))
        );
        request.setAttribute(BOOK_GENRES_ATTRIBUTE_NAME, parseList(bookDto.getGenres().stream()
                .map(Genre::getGenreName)
                .collect(Collectors.toList()))
        );
        request.setAttribute(BOOK_TOTAL_COUNT_ATTRIBUTE_NAME, bookDto.getTotalCopyCount());
        request.setAttribute(BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME, bookDto.getAvailableCopyCount());
        request.setAttribute(BOOK_COVER_PHOTOS_ATTRIBUTE_NAME, bookService.findBookPhotos(bookDto.getId()));
    }

    private String parseList(List<String> list) { return String.join(", ", list); }
}
