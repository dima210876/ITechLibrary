package my.itechart.studlabs.project.itechLibrary.service.impl;

import my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory.BookCopyDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory.BookCoverPhotoDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory.BookDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.*;
import my.itechart.studlabs.project.itechLibrary.dao.factory.relativeDaoFactory.BookAuthorDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.relativeDaoFactory.BookGenreDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BookCopyDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BookCoverPhotoDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.BookDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.*;
import my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao.BookAuthorDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao.BookGenreDao;
import my.itechart.studlabs.project.itechLibrary.error.TransactionException;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.*;
import my.itechart.studlabs.project.itechLibrary.model.factory.BookFactory;
import my.itechart.studlabs.project.itechLibrary.model.record.RelationRecord;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import my.itechart.studlabs.project.itechLibrary.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService
{
    private static final BookServiceImpl INSTANCE = new BookServiceImpl();
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(BookServiceImpl.class);
    private static final long PAGE_SIZE = 20;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final BookAuthorDao bookAuthorDao;
    private final GenreDao genreDao;
    private final BookGenreDao bookGenreDao;
    private final BookCoverPhotoDao bookCoverPhotoDao;
    private final BookCopyDao bookCopyDao;

    private BookServiceImpl()
    {
        bookDao = BookDaoFactory.getInstance().getDao();
        authorDao = AuthorDaoFactory.getInstance().getDao();
        bookAuthorDao = BookAuthorDaoFactory.getInstance().getDao();
        genreDao = GenreDaoFactory.getInstance().getDao();
        bookGenreDao = BookGenreDaoFactory.getInstance().getDao();
        bookCoverPhotoDao = BookCoverPhotoDaoFactory.getInstance().getDao();
        bookCopyDao = BookCopyDaoFactory.getInstance().getDao();
    }

    public static BookServiceImpl getInstance() { return INSTANCE; }

    @Override
    public List<BookDto> findAll()
    {
        return checkOptionalList(
                bookDao.findAll()
                        .map(books -> books.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookDto> findBySortingAndPage(String sortingColumn, int page)
    {
        return checkOptionalList(
                bookDao.findBySortingAndPageNumber(sortingColumn, page)
                        .map(books -> books.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookDto> findByReversedSortingAndPage(String sortingColumn, int page)
    {
        return checkOptionalList(
                bookDao.findByReversedSortingAndPageNumber(sortingColumn, page)
                        .map(books -> books.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookDto> findByDefaultSortingAndPage(int page)
    {
        return checkOptionalList(
                bookDao.findAll()
                        .map(books -> books.stream()
                                .map(this::convertToDto)
                                .sorted((b1, b2) ->
                                        {
                                            Integer avCopyCount1 = b1.getAvailableCopyCount();
                                            Integer avCopyCount2 = b2.getAvailableCopyCount();
                                            if (!avCopyCount1.equals(avCopyCount2))
                                            {
                                                return avCopyCount1.compareTo(avCopyCount2);
                                            }
                                            else
                                            {
                                                return b1.getTitleRu().compareTo(b2.getTitleRu());
                                            }
                                        }
                                        )
                                .skip(PAGE_SIZE * (page - 1))
                                .limit(PAGE_SIZE)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookDto> findBySearchStringAndPage(String searchString, int page)
    {
        return findAll().stream()
                .filter(book -> searchString.equals("")
                        || book.getTitleRu().toLowerCase().contains(searchString.toLowerCase())
                        || book.getDescription().toLowerCase().contains(searchString.toLowerCase()))
                .sorted(Comparator.comparing(BookDto::getId))
                .skip(PAGE_SIZE * (page - 1))
                .limit(PAGE_SIZE)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findById(long id)
    {
        return bookDao.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public BookDto create(BookDto bookDto)
    {
        BookDto createdBook = null;
        try
        {
            final Connection conn = POOL.retrieveConnection();
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try
            {
                Book newBook = BookFactory.getInstance().create(bookDto);
                long bookId = bookDao.create(conn, newBook);
                if (bookId == 0L)
                {
                    throw new TransactionException("Can't create a new book");
                }
                conn.commit();

                List<Long> authorIds = bookDto.getAuthors().stream()
                        .map(Author::getId).collect(Collectors.toList());

                for (Long authorId: authorIds)
                {
                    RelationRecord record = RelationRecord.builder()
                            .firstId(bookId)
                            .secondId(authorId)
                            .build();
                    if (bookAuthorDao.create(conn, record) == 0L)
                    {
                        throw new TransactionException("Can't create book authors: " +
                                "exception while trying to insert new book-author relations");
                    }
                }

                List<Long> genreIds = bookDto.getGenres().stream()
                        .map(Genre::getId).collect(Collectors.toList());

                for (Long genreId: genreIds)
                {
                    RelationRecord record = RelationRecord.builder()
                            .firstId(bookId)
                            .secondId(genreId)
                            .build();
                    if (bookGenreDao.create(conn, record) == 0L)
                    {
                        throw new TransactionException("Can't create book genres: " +
                                "exception while trying to insert new book-genre relations");
                    }
                }

                List<String> photoPaths = bookDto.getCoverPhotos();

                for (String photoPath: photoPaths)
                {
                    BookCoverPhoto newBookCoverPhoto = BookCoverPhoto.builder()
                            .bookId(bookId)
                            .coverPhotoPath(photoPath)
                            .build();
                    if (bookCoverPhotoDao.create(conn, newBookCoverPhoto) == 0L)
                    {
                        throw new TransactionException("Can't update book cover photos: " +
                                "exception while trying to insert new book cover photos");
                    }
                }

                BookCopy newBookCopy = BookCopy.builder()
                        .bookId(bookId)
                        .copyStatus("available")
                        .copyState("normal")
                        .build();

                for (int i = 0; i < bookDto.getTotalCopyCount(); i++)
                {
                    if (bookCopyDao.create(conn, newBookCopy) == 0L)
                    {
                        throw new TransactionException("Can't create new book copies: " +
                                "exception while trying to insert new book copies");
                    }
                }

                conn.commit();
                createdBook = bookDao.findById(bookId).map(this::convertToDto).orElse(null);
            }
            catch (TransactionException e)
            {
                LOGGER.error("Exception while trying to create a new book: " + e.getLocalizedMessage());
                conn.rollback(savepoint);
            }
            finally
            {
                conn.setAutoCommit(true);
                POOL.returnConnection(conn);
            }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
        }
        return createdBook;
    }

    @Override
    public BookDto update(BookDto bookDto)
    {
        BookDto updatedBook = null;
        AtomicBoolean transactionExceptionWithAuthors = new AtomicBoolean(false);
        AtomicBoolean transactionExceptionWithGenres = new AtomicBoolean(false);
        AtomicBoolean transactionExceptionWithPhotos = new AtomicBoolean(false);
        try
        {
            final Connection conn = POOL.retrieveConnection();
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try
            {
                BookDto curBook = bookDao.findById(bookDto.getId()).map(this::convertToDto).orElse(null);
                if (curBook == null)
                {
                    throw new TransactionException("Book doesn't exist");
                }
                if (!bookDao.update(conn, BookFactory.getInstance().create(bookDto)))
                {
                    throw new TransactionException("Can't update book info");
                }

                List<Long> authorIdsOld = curBook.getAuthors().stream()
                        .map(Author::getId).collect(Collectors.toList());
                List<Long> authorIdsNew = bookDto.getAuthors().stream()
                        .map(Author::getId).collect(Collectors.toList());
                if (!(authorIdsNew.containsAll(authorIdsOld) && authorIdsOld.containsAll(authorIdsNew)))
                {
                    if (!bookAuthorDao.deleteByBookId(conn, bookDto.getId()))
                    {
                        throw new TransactionException("Can't update book authors: " +
                                "exception while trying to delete old book-author relations");
                    }
                    authorIdsNew.forEach(newAuthorId ->
                    {
                        RelationRecord record = RelationRecord.builder()
                                .firstId(bookDto.getId())
                                .secondId(newAuthorId)
                                .build();
                        if (bookAuthorDao.create(conn, record) == 0L)
                        {
                            transactionExceptionWithAuthors.set(true);
                        }
                    });
                }
                if (transactionExceptionWithAuthors.get())
                {
                    throw new TransactionException("Can't update book authors: " +
                            "exception while trying to insert new book-author relations");
                }

                List<Long> genreIdsOld = curBook.getGenres().stream()
                        .map(Genre::getId).collect(Collectors.toList());
                List<Long> genreIdsNew = bookDto.getGenres().stream()
                        .map(Genre::getId).collect(Collectors.toList());
                if (!(genreIdsNew.containsAll(genreIdsOld) && genreIdsOld.containsAll(genreIdsNew)))
                {
                    if (!bookGenreDao.deleteByBookId(conn, bookDto.getId()))
                    {
                        throw new TransactionException("Can't update book genres: " +
                                "exception while trying to delete old book-genre relations");
                    }
                    genreIdsNew.forEach(newGenreId ->
                    {
                        RelationRecord record = RelationRecord.builder()
                                .firstId(bookDto.getId())
                                .secondId(newGenreId)
                                .build();
                        if (bookGenreDao.create(conn, record) == 0L)
                        {
                            transactionExceptionWithGenres.set(true);
                        }
                    });
                }
                if (transactionExceptionWithGenres.get())
                {
                    throw new TransactionException("Can't update book genres: " +
                            "exception while trying to insert new book-genre relations");
                }

                List<String> photoPathsOld = curBook.getCoverPhotos();
                List<String> photoPathsNew = bookDto.getCoverPhotos();
                if (!(photoPathsNew.containsAll(photoPathsOld) && photoPathsOld.containsAll(photoPathsNew)))
                {
                    if (!bookCoverPhotoDao.deleteByBookId(conn, bookDto.getId()))
                    {
                        throw new TransactionException("Can't update book cover photos: " +
                                "exception while trying to delete old book cover photos");
                    }
                    photoPathsNew.forEach(newPhotoPath ->
                    {
                        BookCoverPhoto newBookCoverPhoto = BookCoverPhoto.builder()
                                .bookId(bookDto.getId())
                                .coverPhotoPath(newPhotoPath)
                                .build();
                        if (bookCoverPhotoDao.create(conn, newBookCoverPhoto) == 0L)
                        {
                            transactionExceptionWithPhotos.set(true);
                        }
                    });
                }
                if (transactionExceptionWithPhotos.get())
                {
                    throw new TransactionException("Can't update book cover photos: " +
                            "exception while trying to insert new book cover photos");
                }
                conn.commit();
                updatedBook = bookDao.findById(bookDto.getId()).map(this::convertToDto).orElse(null);
            }
            catch (TransactionException e)
            {
                LOGGER.error("Exception while trying to update book with id = " + bookDto.getId() +
                        ": " + e.getLocalizedMessage());
                conn.rollback(savepoint);
            }
            finally
            {
                conn.setAutoCommit(true);
                POOL.returnConnection(conn);
            }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
        }
        return updatedBook;
    }

    @Override
    public int getCountOfPages() { return bookDao.getCountOfPages(); }

    @Override
    public void deleteById(long bookId)
    {
        if (!bookDao.delete(bookId)) { LOGGER.error("Can't delete book with id = " + bookId); }
    }

    @Override
    public List<String> findBookPhotos(long bookId)
    {
        return bookCoverPhotoDao.findByBookId(bookId)
                .map(photos -> photos.stream()
                        .map(BookCoverPhoto::getCoverPhotoPath)
                        .collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    @Override
    public boolean addBookPhoto(String photoPath, long bookId)
    {
        final Connection conn = POOL.retrieveConnection();
        long createdPhotoId = bookCoverPhotoDao.create(conn, BookCoverPhoto.builder()
                        .bookId(bookId)
                        .coverPhotoPath(photoPath)
                        .build());
        POOL.returnConnection(conn);
        return (createdPhotoId > 0);
    }

    private BookDto convertToDto(Book book)
    {
        Optional<List<RelationRecord>> optionalAuthorRecords = bookAuthorDao.findByBookId(book.getId());
        List<RelationRecord> authorRecords = optionalAuthorRecords.orElseGet(ArrayList::new);
        List<Author> authors = new ArrayList<>();
        authorRecords.forEach(record ->
        {
            Optional<Author> author = authorDao.findById(record.getSecondId());
            author.ifPresent(authors::add);
        });

        Optional<List<RelationRecord>> optionalGenreRecords = bookGenreDao.findByBookId(book.getId());
        List<RelationRecord> genreRecords = optionalGenreRecords.orElseGet(ArrayList::new);
        List<Genre> genres = new ArrayList<>();
        genreRecords.forEach(record ->
        {
            Optional<Genre> genre = genreDao.findById(record.getSecondId());
            genre.ifPresent(genres::add);
        });

        Optional<List<BookCoverPhoto>> optionalPhotos = bookCoverPhotoDao.findByBookId(book.getId());
        List<BookCoverPhoto> bookCoverPhotos = optionalPhotos.orElseGet(ArrayList::new);
        List<String> coverPhotos = bookCoverPhotos.stream()
                .map(BookCoverPhoto::getCoverPhotoPath)
                .collect(Collectors.toList());

        Integer totalCopyCount = bookCopyDao.getExistingCountByBookId(book.getId());
        Integer availableCopyCount = bookCopyDao.getAvailableCountByBookId(book.getId());

        return new BookDto(book.getId(), book.getTitleRu(), book.getTitleOrigin(), book.getDescription(),
                book.getBookCost(), book.getDayCost(), book.getEditionYear(), book.getPageCount(),
                book.getRegistrationDate(), authors, genres, coverPhotos, totalCopyCount, availableCopyCount);
    }
}
