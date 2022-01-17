package my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.ReaderDao;

public class ReaderDaoFactory
{
    private static final ReaderDaoFactory INSTANCE = new ReaderDaoFactory();

    ReaderDaoFactory() { }

    public static ReaderDaoFactory getInstance() { return INSTANCE; }

    public ReaderDao getDao() { return new ReaderDao(); }
}
