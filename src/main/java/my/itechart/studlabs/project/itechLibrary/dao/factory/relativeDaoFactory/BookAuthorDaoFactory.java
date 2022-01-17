package my.itechart.studlabs.project.itechLibrary.dao.factory.relativeDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao.BookAuthorDao;

public class BookAuthorDaoFactory
{
    private static final BookAuthorDaoFactory INSTANCE = new BookAuthorDaoFactory();

    BookAuthorDaoFactory() { }

    public static BookAuthorDaoFactory getInstance() { return INSTANCE; }

    public BookAuthorDao getDao() { return new BookAuthorDao(); }
}
