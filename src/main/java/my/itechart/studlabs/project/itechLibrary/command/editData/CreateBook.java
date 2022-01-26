package my.itechart.studlabs.project.itechLibrary.command.editData;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowBookCreationForm;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowReaderCreationForm;
import my.itechart.studlabs.project.itechLibrary.command.page.ShowMainPage;
import my.itechart.studlabs.project.itechLibrary.command.page.ShowReadersPage;
import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.AuthorDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.GenreDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.AuthorDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.GenreDao;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.Author;
import my.itechart.studlabs.project.itechLibrary.model.entity.Genre;
import my.itechart.studlabs.project.itechLibrary.service.BookService;
import my.itechart.studlabs.project.itechLibrary.service.impl.BookServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum CreateBook implements Command
{
    INSTANCE;

    private static final String TITLE_RU_PARAMETER_NAME = "titleRu";
    private static final String TITLE_ORIGIN_PARAMETER_NAME = "titleOrigin";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String BOOK_COST_PARAMETER_NAME = "bookCost";
    private static final String DAY_COST_PARAMETER_NAME = "dayCost";
    private static final String EDITION_YEAR_PARAMETER_NAME = "editionYear";
    private static final String PAGE_COUNT_PARAMETER_NAME = "pageCount";
    private static final String REGISTRATION_DATE_PARAMETER_NAME = "registrationDate";

    private static final String COPY_COUNT_PARAMETER_NAME = "copyCount";
    private static final String AUTHORS_PARAMETER_NAME = "bookAuthors";
    private static final String GENRES_PARAMETER_NAME = "bookGenres";

    private static final String COMMAND_PARAMETER_NAME = "command";

    private static final String SHOW_BOOKS_COMMAND_VALUE = "main";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String EMPTY_ATTRIBUTE_VALUE = "null";
    private static final String ERROR_ATTRIBUTE_VALUE = "Книгу сохранить не удалось.";

    private final BookService bookService = BookServiceImpl.getInstance();
    private final AuthorDao authorDao = AuthorDaoFactory.getInstance().getDao();
    private final GenreDao genreDao = GenreDaoFactory.getInstance().getDao();

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String titleRu = String.valueOf(request.getParameter(TITLE_RU_PARAMETER_NAME)).trim();
        String titleOrigin = String.valueOf(request.getParameter(TITLE_ORIGIN_PARAMETER_NAME)).trim();
        String description = String.valueOf(request.getParameter(DESCRIPTION_PARAMETER_NAME)).trim();
        String bCost = String.valueOf(request.getParameter(BOOK_COST_PARAMETER_NAME)).trim();
        Double bookCost = Double.parseDouble(bCost);
        String dCost = String.valueOf(request.getParameter(DAY_COST_PARAMETER_NAME)).trim();
        Double dayCost = Double.parseDouble(dCost);
        String editYear = String.valueOf(request.getParameter(EDITION_YEAR_PARAMETER_NAME)).trim();
        Integer editionYear = (editYear.equals(EMPTY_ATTRIBUTE_VALUE)) ? null : Integer.parseInt(editYear);
        String pgCount = String.valueOf(request.getParameter(PAGE_COUNT_PARAMETER_NAME)).trim();
        Integer pageCount = (pgCount.equals(EMPTY_ATTRIBUTE_VALUE)) ? null : Integer.parseInt(pgCount);
        String regDate = String.valueOf(request.getParameter(REGISTRATION_DATE_PARAMETER_NAME)).trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate registrationDate = LocalDate.parse(regDate, formatter);
        String cpCount = String.valueOf(request.getParameter(COPY_COUNT_PARAMETER_NAME)).trim();
        Integer copyCount = Integer.parseInt(cpCount);

        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();

        String[] bookAuthors = request.getParameterValues(AUTHORS_PARAMETER_NAME);
        String[] bookGenres = request.getParameterValues(GENRES_PARAMETER_NAME);
        for (String authorId: bookAuthors)
        {
            long id = Long.parseLong(authorId.trim());
            Optional<Author> author = authorDao.findById(id);
            author.ifPresent(authors::add);
        }
        for (String genreId: bookGenres)
        {
            long id = Long.parseLong(genreId.trim());
            Optional<Genre> genre = genreDao.findById(id);
            genre.ifPresent(genres::add);
        }

        BookDto bookDto = new BookDto(0L, titleRu, titleOrigin, description, bookCost, dayCost,
                editionYear, pageCount, registrationDate, authors, genres, new ArrayList<>(), copyCount, copyCount);

        BookDto createdBook = bookService.create(bookDto);
        if (createdBook == null)
        {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE);
            return ShowBookCreationForm.INSTANCE.execute(request);
        }
        request.setAttribute(COMMAND_PARAMETER_NAME, SHOW_BOOKS_COMMAND_VALUE);
        return ShowMainPage.INSTANCE.execute(request);
    }
}
