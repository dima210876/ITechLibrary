package my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BorrowBooksCopyDamagePhotoDao;

public class BorrowBooksCopyDamagePhotoDaoFactory
{
    private static final BorrowBooksCopyDamagePhotoDaoFactory INSTANCE = new BorrowBooksCopyDamagePhotoDaoFactory();

    BorrowBooksCopyDamagePhotoDaoFactory() { }

    public static BorrowBooksCopyDamagePhotoDaoFactory getInstance() { return INSTANCE; }

    public BorrowBooksCopyDamagePhotoDao getDao() { return new BorrowBooksCopyDamagePhotoDao(); }
}
