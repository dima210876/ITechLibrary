package my.itechart.studlabs.project.itechLibrary.command.inputForms;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;
import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.AuthorDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.GenreDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.AuthorDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.GenreDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.Author;
import my.itechart.studlabs.project.itechLibrary.model.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public enum ShowBookCreationForm implements Command
{
    INSTANCE;

    private static final String AUTHORS_ATTRIBUTE_NAME = "authors";
    private static final String GENRES_ATTRIBUTE_NAME = "genres";

    private final AuthorDao authorDao = AuthorDaoFactory.getInstance().getDao();
    private final GenreDao genreDao = GenreDaoFactory.getInstance().getDao();

    private static final ResponseContext BOOK_CREATION_FORM_RESPONSE = new ResponseContext(UrlPatterns.CREATE_BOOK, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        List<Author> authors = authorDao.findAll().orElse(new ArrayList<>());
        List<Genre> genres = genreDao.findAll().orElse(new ArrayList<>());
        request.setAttribute(AUTHORS_ATTRIBUTE_NAME, authors);
        request.setAttribute(GENRES_ATTRIBUTE_NAME, genres);
        return BOOK_CREATION_FORM_RESPONSE;
    }
}
