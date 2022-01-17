package my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.AuthorDao;

public class AuthorDaoFactory
{
    private static final AuthorDaoFactory INSTANCE = new AuthorDaoFactory();

    AuthorDaoFactory() { }

    public static AuthorDaoFactory getInstance() { return INSTANCE; }

    public AuthorDao getDao() { return new AuthorDao(); }
}
