package my.itechart.studlabs.project.itechLibrary.command.page;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.service.ReaderService;
import my.itechart.studlabs.project.itechLibrary.service.impl.ReaderServiceImpl;

import java.util.List;

public enum ShowReadersPage implements Command
{
    INSTANCE;

    private static final String READERS_ATTRIBUTE_NAME = "readers";
    private static final String PAGE_NUMBER_PARAMETER_NAME = "pageNumber";
    private static final String SORTING_PARAMETER_NAME = "sortingColumn";
    private static final String SORTING_ORDER_PARAMETER_NAME = "reversedOrder";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "countOfPages";
    private static final List<String> possibleSortingColumns = List.of("first_name", "last_name", "birth_date", "email", "address");

    private final ReaderService readerService = ReaderServiceImpl.getInstance();

    private static final ResponseContext READERS_PAGE_RESPONSE = new ResponseContext(UrlPatterns.READERS, false);

    public ShowReadersPage getInstance() { return INSTANCE; }

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String page = String.valueOf(request.getParameter(PAGE_NUMBER_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_NUMBER_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, readerService.getCountOfPages());
        String sortingColumn = String.valueOf(request.getParameter(SORTING_PARAMETER_NAME));
        List<ReaderDto> readerList;
        if (sortingColumn.equals("null") || !possibleSortingColumns.contains(sortingColumn))
        {
            request.setAttribute(SORTING_PARAMETER_NAME, "default");
            readerList = readerService.findBySortingAndPage("last_name", pageNumber);
        }
        else
        {
            String revOrder = String.valueOf(request.getParameter(SORTING_ORDER_PARAMETER_NAME));
            if (revOrder.equals("null")) { revOrder = "false"; }
            request.setAttribute(SORTING_PARAMETER_NAME, sortingColumn);
            request.setAttribute(SORTING_ORDER_PARAMETER_NAME, revOrder);
            if (revOrder.equals("false")) { readerList = readerService.findBySortingAndPage(sortingColumn, pageNumber); }
            else { readerList = readerService.findByReversedSortingAndPage(sortingColumn, pageNumber); }
        }
        request.setAttribute(READERS_ATTRIBUTE_NAME, readerList);
        return READERS_PAGE_RESPONSE;
    }
}
