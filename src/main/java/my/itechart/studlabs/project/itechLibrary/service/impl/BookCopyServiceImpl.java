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
import my.itechart.studlabs.project.itechLibrary.model.dto.BookCopyDto;
import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.*;
import my.itechart.studlabs.project.itechLibrary.model.record.RelationRecord;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import my.itechart.studlabs.project.itechLibrary.service.BookCopyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookCopyServiceImpl implements BookCopyService
{
    private static final BookCopyServiceImpl INSTANCE = new BookCopyServiceImpl();
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(BookCopyServiceImpl.class);

    private final BookCopyDao bookCopyDao;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final BookAuthorDao bookAuthorDao;
    private final GenreDao genreDao;
    private final BookGenreDao bookGenreDao;
    private final BookCoverPhotoDao bookCoverPhotoDao;

    public BookCopyServiceImpl()
    {
        this.bookCopyDao = BookCopyDaoFactory.getInstance().getDao();
        this.bookDao = BookDaoFactory.getInstance().getDao();
        this.authorDao = AuthorDaoFactory.getInstance().getDao();
        this.bookAuthorDao = BookAuthorDaoFactory.getInstance().getDao();
        this.genreDao = GenreDaoFactory.getInstance().getDao();
        this.bookGenreDao = BookGenreDaoFactory.getInstance().getDao();
        this.bookCoverPhotoDao = BookCoverPhotoDaoFactory.getInstance().getDao();
    }

    public static BookCopyServiceImpl getInstance() { return INSTANCE; }

    @Override
    public List<BookCopyDto> findAll()
    {
        return checkOptionalList(
                bookCopyDao.findAll()
                        .map(bookCopies -> bookCopies.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public int getCountOfPages() { return bookCopyDao.getCountOfPages(); }

    @Override
    public BookCopyDto findById(long id)
    {
        return bookCopyDao.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public List<BookCopyDto> findExistingBookCopiesByBookId(long bookId)
    {
        return checkOptionalList(
                bookCopyDao.findExistingByBookId(bookId)
                        .map(bookCopies -> bookCopies.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookCopyDto> findAvailableBookCopiesByBookId(long bookId)
    {
        return checkOptionalList(
                bookCopyDao.findAvailableByBookId(bookId)
                        .map(bookCopies -> bookCopies.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BookCopyDto> createNewCopies(BookCopyDto bookCopyDto, int copyCount)
    {
        List<BookCopyDto> createdBookCopies = new ArrayList<>();
        List<Long> createdBookCopiesIds = new ArrayList<>();
        try
        {
            final Connection conn = POOL.retrieveConnection();
            conn.setAutoCommit(false);
            Savepoint savePoint = conn.setSavepoint("savepoint");
            try
            {
                if (bookDao.findById(bookCopyDto.getBookDto().getId()).isEmpty())
                {
                    throw new TransactionException("Book with id = " + bookCopyDto.getBookDto().getId() +
                            " doesn't exist");
                }

                BookCopy newCopy = BookCopy.builder()
                        .bookId(bookCopyDto.getBookDto().getId())
                        .copyStatus(bookCopyDto.getCopyStatus())
                        .copyState(bookCopyDto.getCopyState())
                        .build();

                for (int i = 0; i < copyCount; i++)
                {
                    long newCopyId = bookCopyDao.create(conn, newCopy);
                    if (newCopyId == 0L) { throw new TransactionException("Can't create a new book copy"); }
                    createdBookCopiesIds.add(newCopyId);
                }

                conn.commit();

                for (long bookCopyId : createdBookCopiesIds)
                {
                    BookCopyDto createdBookCopy = bookCopyDao.findById(bookCopyId).map(this::convertToDto).orElse(null);
                    createdBookCopies.add(createdBookCopy);
                }
            }
            catch (TransactionException e)
            {
                LOGGER.error("Exception while trying to create new book copies: " + e.getLocalizedMessage());
                conn.rollback(savePoint);
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
        return createdBookCopies;
    }

    @Override
    public boolean deleteBookCopyById(long bookCopyId)
    {
        if (!bookCopyDao.delete(bookCopyId))
        {
            LOGGER.error("Can't delete book copy with id = " + bookCopyId);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteBookCopiesByBookId(long bookId)
    {
        if (!bookCopyDao.deleteAllByBookId(bookId))
        {
            LOGGER.error("Can't delete all book copies with book_id = " + bookId);
            return false;
        }
        return true;
    }

    @Override
    public boolean changeBookCopyStatusById(long bookCopyId)
    {
        try
        {
            BookCopy bookCopy = bookCopyDao.findById(bookCopyId).orElse(null);
            if (bookCopy == null) { throw new TransactionException("Can't find book copy with id = " + bookCopyId); }
            BookCopy changedCopy = BookCopy.builder()
                    .id(bookCopy.getId())
                    .bookId(bookCopy.getBookId())
                    .copyStatus(
                            switch (bookCopy.getCopyStatus())
                                    {
                                        case "available" -> "borrowed";
                                        case "borrowed" -> "available";
                                        default -> "deleted";
                                    }
                    )
                    .copyState(bookCopy.getCopyState())
                    .build();
            if (update(convertToDto(changedCopy)) == null)
            {
                throw new TransactionException("Can't update status of book copy with id = " + bookCopyId);
            }
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to update book copy status: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean changeBookCopyStateById(long bookCopyId)
    {
        try
        {
            BookCopy bookCopy = bookCopyDao.findById(bookCopyId).orElse(null);
            if (bookCopy == null) { throw new TransactionException("Can't find book copy with id = " + bookCopyId); }
            BookCopy changedCopy = BookCopy.builder()
                    .id(bookCopy.getId())
                    .bookId(bookCopy.getBookId())
                    .copyStatus(bookCopy.getCopyStatus())
                    .copyState(
                            switch (bookCopy.getCopyState())
                                    {
                                        case "normal" -> "damaged";
                                        case "damaged" -> "repaired";
                                        default -> bookCopy.getCopyState();
                                    }
                    )
                    .build();
            if (update(convertToDto(changedCopy)) == null)
            {
                throw new TransactionException("Can't update state of book copy with id = " + bookCopyId);
            }
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to update book copy status: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public BookCopyDto create(BookCopyDto bookCopyDto)
    {
        BookCopyDto createdBookCopy = null;
        final Connection conn = POOL.retrieveConnection();
        try
        {
            if (bookDao.findById(bookCopyDto.getBookDto().getId()).isEmpty())
            {
                throw new TransactionException("Book with id = " + bookCopyDto.getBookDto().getId() +
                        " doesn't exist");
            }
            BookCopy newCopy = BookCopy.builder()
                    .bookId(bookCopyDto.getBookDto().getId())
                    .copyStatus(bookCopyDto.getCopyStatus())
                    .copyState(bookCopyDto.getCopyState())
                    .build();
            long createdBookCopyId = bookCopyDao.create(conn, newCopy);
            if (createdBookCopyId == 0L) { throw new TransactionException("Can't create a new book copy"); }
            createdBookCopy = bookCopyDao.findById(createdBookCopyId).map(this::convertToDto).orElse(null);
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to create a new book copy: " + e.getLocalizedMessage());
        }
        finally
        {
            POOL.returnConnection(conn);
        }
        return createdBookCopy;
    }

    @Override
    public BookCopyDto update(BookCopyDto bookCopyDto)
    {
        BookCopyDto updatedBookCopy = null;
        final Connection conn = POOL.retrieveConnection();
        try
        {
            BookCopy newCopy = BookCopy.builder()
                    .bookId(bookCopyDto.getBookDto().getId())
                    .copyStatus(bookCopyDto.getCopyStatus())
                    .copyState(bookCopyDto.getCopyState())
                    .build();
            if (!bookCopyDao.update(conn, newCopy)) { throw new TransactionException("Can't update a book copy"); }
            updatedBookCopy = bookCopyDao.findById(bookCopyDto.getId()).map(this::convertToDto).orElse(null);
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to update book copy with id = " + bookCopyDto.getId() +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            POOL.returnConnection(conn);
        }
        return updatedBookCopy;
    }

    private BookCopyDto convertToDto(BookCopy bookCopy)
    {
        BookDto bookDto = bookDao.findById(bookCopy.getBookId()).map(this::convertToDto).orElse(null);
        return new BookCopyDto(bookCopy.getId(), bookDto, bookCopy.getCopyStatus(), bookCopy.getCopyState());
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
