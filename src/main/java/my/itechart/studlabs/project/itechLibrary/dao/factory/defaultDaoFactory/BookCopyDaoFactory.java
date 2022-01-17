package my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.BookCopyDao;

public class BookCopyDaoFactory
{
    private static final BookCopyDaoFactory INSTANCE = new BookCopyDaoFactory();

    BookCopyDaoFactory() { }

    public static BookCopyDaoFactory getInstance() { return INSTANCE; }

    public BookCopyDao getDao() { return new BookCopyDao(); }
}
