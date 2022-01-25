package my.itechart.studlabs.project.itechLibrary.command.page;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.model.dto.BorrowDto;
import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.service.BorrowService;
import my.itechart.studlabs.project.itechLibrary.service.ReaderService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BorrowServiceImpl;
import my.itechart.studlabs.project.itechLibrary.service.impl.ReaderServiceImpl;

import java.util.List;

public enum ShowReaderPage implements Command
{
    INSTANCE;

    private static final String ID_PARAMETER_NAME = "readerId";
    private static final String READER_ATTRIBUTE_NAME = "reader";
    private static final String READER_NOT_RETURNED_BORROW_ATTRIBUTE_NAME = "notReturnedBorrow";

    private static final String EMPTY_ATTRIBUTE_VALUE = "null";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";

    private final ReaderService readerService = ReaderServiceImpl.getInstance();
    private final BorrowService borrowService = BorrowServiceImpl.getInstance();

    private static final ResponseContext READER_PAGE_RESPONSE = new ResponseContext(UrlPatterns.READER, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String id = String.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final long readerId = (EMPTY_ATTRIBUTE_VALUE.equals(id) || id.isEmpty()) ? 0 : Integer.parseInt(id);
        ReaderDto readerDto = readerService.findById(readerId);
        if (readerDto != null)
        {
            request.resetParameter(ERROR_ATTRIBUTE_NAME);
            request.setAttribute(READER_ATTRIBUTE_NAME, readerDto);
            List<BorrowDto> notReturnedBorrows = borrowService.findNotReturnedReaderBorrows(readerId);
            if (!notReturnedBorrows.isEmpty())
            {
                request.setAttribute(READER_NOT_RETURNED_BORROW_ATTRIBUTE_NAME, notReturnedBorrows.get(0));
            }
        }
        else
        {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, "Reader not found.");
        }
        return READER_PAGE_RESPONSE;
    }
}
