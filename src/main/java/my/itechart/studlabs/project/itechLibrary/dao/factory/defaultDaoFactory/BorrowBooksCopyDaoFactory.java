package my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.BorrowBooksCopyDao;

public class BorrowBooksCopyDaoFactory
{
    private static final BorrowBooksCopyDaoFactory INSTANCE = new BorrowBooksCopyDaoFactory();

    BorrowBooksCopyDaoFactory() { }

    public static BorrowBooksCopyDaoFactory getInstance() { return INSTANCE; }

    public BorrowBooksCopyDao getDao() { return new BorrowBooksCopyDao(); }
}
