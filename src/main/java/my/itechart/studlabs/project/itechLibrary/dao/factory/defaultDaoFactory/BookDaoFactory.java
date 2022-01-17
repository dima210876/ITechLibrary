package my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.BookDao;

public class BookDaoFactory
{
    private static final BookDaoFactory INSTANCE = new BookDaoFactory();

    BookDaoFactory() { }

    public static BookDaoFactory getInstance() { return INSTANCE; }

    public BookDao getDao() { return new BookDao(); }
}
