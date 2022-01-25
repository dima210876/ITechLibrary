package my.itechart.studlabs.project.itechLibrary.command.page;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.service.BookService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BookServiceImpl;

import java.util.List;

public enum ShowMainPage implements Command
{
    INSTANCE;

    private static final String BOOK_LIST_PARAMETER_NAME = "bookList";
    private static final String PAGE_NUMBER_PARAMETER_NAME = "pageNumber";
    private static final String SORTING_PARAMETER_NAME = "sortingColumn";
    private static final String SORTING_ORDER_PARAMETER_NAME = "reversedOrder";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "countOfPages";
    private static final List<String> possibleSortingColumns = List.of("title_ru", "edition_year", "book_cost", "day_cost");
    private final BookService bookService = BookServiceImpl.getInstance();

    private static final ResponseContext MAIN_PAGE_RESPONSE = new ResponseContext(UrlPatterns.MAIN, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String page = String.valueOf(request.getParameter(PAGE_NUMBER_PARAMETER_NAME));
        String sortingColumn = String.valueOf(request.getParameter(SORTING_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_NUMBER_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, bookService.getCountOfPages());
        List<BookDto> bookList;
        if (sortingColumn.equals("null") || !possibleSortingColumns.contains(sortingColumn))
        {
            request.setAttribute(SORTING_PARAMETER_NAME, "default");
            bookList = bookService.findByDefaultSortingAndPage(pageNumber);
        }
        else
        {
            request.setAttribute(SORTING_PARAMETER_NAME, sortingColumn);
            String revOrder = String.valueOf(request.getParameter(SORTING_ORDER_PARAMETER_NAME));
            if (revOrder.equals("null")) { revOrder = "false"; }
            request.setAttribute(SORTING_ORDER_PARAMETER_NAME, revOrder);
            if (revOrder.equals("false")) { bookList = bookService.findBySortingAndPage(sortingColumn, pageNumber); }
            else { bookList = bookService.findByReversedSortingAndPage(sortingColumn, pageNumber); }
        }
        request.setAttribute(BOOK_LIST_PARAMETER_NAME, bookList);
        return MAIN_PAGE_RESPONSE;
    }
}
