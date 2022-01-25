//package my.itechart.studlabs.project.itechLibrary.command.search;
//
//import my.itechart.studlabs.project.itechLibrary.command.Command;
//import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
//import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
//import my.itechart.studlabs.project.itechLibrary.command.page.ShowBookSearchPage;
//import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
//import my.itechart.studlabs.project.itechLibrary.service.BookService;
//import my.itechart.studlabs.project.itechLibrary.service.impl.BookServiceImpl;
//import java.util.List;
//
//public enum FindBook implements Command
//{
//    INSTANCE;
//
//    private static final String BOOKS_PARAMETER_NAME = "books";
//    private static final String SEARCH_STRING_PARAMETER_NAME = "searchString";
//    private static final String PAGE_NUMBER_PARAMETER_NAME = "pageNumber";
//    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "countOfPages";
//    private static final int PAGE_SIZE = 20;
//
//    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
//    private static final String ERROR_ATTRIBUTE_VALUE = "No books found for this request.";
//    private final BookService bookService = BookServiceImpl.getInstance();
//
//    @Override
//    public ResponseContext execute(RequestContext request)
//    {
//        final String searchString = String.valueOf(request.getParameter(SEARCH_STRING_PARAMETER_NAME)).trim();
//        String page = String.valueOf(request.getParameter(PAGE_NUMBER_PARAMETER_NAME));
//        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
//
//        List<BookDto> books = bookService.findBySearchStringAndPage(searchString, pageNumber);
//        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, (books.size() / PAGE_SIZE + 1));
//
//        if (books.isEmpty()) { request.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE); }
//        else { request.setAttribute(BOOKS_PARAMETER_NAME, books); }
//        return ShowBookSearchPage.INSTANCE.execute(request);
//    }
//}
