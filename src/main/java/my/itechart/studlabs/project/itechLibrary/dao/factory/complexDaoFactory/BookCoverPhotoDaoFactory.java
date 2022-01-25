package my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory;

import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BookCoverPhotoDao;

public class BookCoverPhotoDaoFactory
{
    private static final BookCoverPhotoDaoFactory INSTANCE = new BookCoverPhotoDaoFactory();

    BookCoverPhotoDaoFactory() { }

    public static BookCoverPhotoDaoFactory getInstance() { return INSTANCE; }

    public BookCoverPhotoDao getDao() { return new BookCoverPhotoDao(); }
}
