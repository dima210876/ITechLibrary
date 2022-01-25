package my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BorrowDao;

public class BorrowDaoFactory
{
    private static final BorrowDaoFactory INSTANCE = new BorrowDaoFactory();

    BorrowDaoFactory() { }

    public static BorrowDaoFactory getInstance() { return INSTANCE; }

    public BorrowDao getDao() { return new BorrowDao(); }
}
