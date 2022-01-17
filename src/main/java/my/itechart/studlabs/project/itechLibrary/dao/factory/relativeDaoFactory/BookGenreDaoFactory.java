package my.itechart.studlabs.project.itechLibrary.dao.factory.relativeDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao.BookGenreDao;

public class BookGenreDaoFactory
{
    private static final BookGenreDaoFactory INSTANCE = new BookGenreDaoFactory();

    BookGenreDaoFactory() { }

    public static BookGenreDaoFactory getInstance() { return INSTANCE; }

    public BookGenreDao getDao() { return new BookGenreDao(); }
}
