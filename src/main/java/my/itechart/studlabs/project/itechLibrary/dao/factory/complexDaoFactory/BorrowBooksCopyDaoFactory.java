package my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BorrowBooksCopyDao;

public class BorrowBooksCopyDaoFactory
{
    private static final BorrowBooksCopyDaoFactory INSTANCE = new BorrowBooksCopyDaoFactory();

    BorrowBooksCopyDaoFactory() { }

    public static BorrowBooksCopyDaoFactory getInstance() { return INSTANCE; }

    public BorrowBooksCopyDao getDao() { return new BorrowBooksCopyDao(); }
}
