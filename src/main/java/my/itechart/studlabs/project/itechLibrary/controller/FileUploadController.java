package my.itechart.studlabs.project.itechLibrary.controller;

import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.WrappingRequestContext;
import my.itechart.studlabs.project.itechLibrary.command.page.ShowBookPage;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.service.BookService;
import my.itechart.studlabs.project.itechLibrary.service.file.FileReader;
import my.itechart.studlabs.project.itechLibrary.service.impl.BookServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "FileUploadController", value = "/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) //10MB
public class FileUploadController extends HttpServlet
{
    private static final String BOOK_ID_PARAMETER_NAME = "bookId";
    private static final String COVER_PARAMETER_NAME = "coverPhoto";
    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String COMMAND_COVER_PHOTO_PARAMETER_VALUE = "coverPhoto";
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_MESSAGE_ATTRIBUTE_VALUE = "Can't load photo";
    private final BookService bookService = BookServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if (req.getParameter(COMMAND_PARAMETER_NAME).equals(COMMAND_COVER_PHOTO_PARAMETER_VALUE))
        {
            Part filePart = req.getPart(COVER_PARAMETER_NAME);
            String packagePath = req.getSession().getServletContext().getRealPath("/") + "img/books/";
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String photoPath = FileReader.getInstance().read(packagePath, filePart, fileName);
            int bookId = Integer.parseInt(String.valueOf(req.getParameter(BOOK_ID_PARAMETER_NAME)));
            BookDto bookDto = bookService.findById(bookId);
            if (bookDto == null || !bookService.addBookPhoto(photoPath, bookId))
            {
                req.setAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, ERROR_MESSAGE_ATTRIBUTE_VALUE);
            }
        }
        else
        {
            req.setAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, ERROR_MESSAGE_ATTRIBUTE_VALUE);
        }
        final ResponseContext result = ShowBookPage.INSTANCE.execute(WrappingRequestContext.of(req));
        if (result.isRedirect()) { resp.sendRedirect(result.getPage()); }
        else
        {
            final RequestDispatcher dispatcher = req.getRequestDispatcher(result.getPage());
            dispatcher.forward(req, resp);
        }
    }
}
