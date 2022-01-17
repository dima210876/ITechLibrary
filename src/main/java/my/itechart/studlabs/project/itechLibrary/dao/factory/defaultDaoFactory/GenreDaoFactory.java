package my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.GenreDao;

public class GenreDaoFactory
{
    private static final GenreDaoFactory INSTANCE = new GenreDaoFactory();

    GenreDaoFactory() { }

    public static GenreDaoFactory getInstance() { return INSTANCE; }

    public GenreDao getDao() { return new GenreDao(); }
}
