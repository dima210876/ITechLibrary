package my.itechart.studlabs.project.itechLibrary.service.impl;

import my.itechart.studlabs.project.itechLibrary.dao.factory.complexDaoFactory.*;
import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.*;
import my.itechart.studlabs.project.itechLibrary.dao.factory.relativeDaoFactory.BookAuthorDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.factory.relativeDaoFactory.BookGenreDaoFactory;
import my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao.*;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.*;
import my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao.BookAuthorDao;
import my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao.BookGenreDao;
import my.itechart.studlabs.project.itechLibrary.error.TransactionException;
import my.itechart.studlabs.project.itechLibrary.model.dto.*;
import my.itechart.studlabs.project.itechLibrary.model.entity.*;
import my.itechart.studlabs.project.itechLibrary.model.factory.BorrowFactory;
import my.itechart.studlabs.project.itechLibrary.model.record.RelationRecord;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import my.itechart.studlabs.project.itechLibrary.service.BorrowService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BorrowServiceImpl implements BorrowService
{
    private static final BorrowServiceImpl INSTANCE = new BorrowServiceImpl();
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(BorrowServiceImpl.class);

    private final BorrowDao borrowDao;
    private final BorrowBooksCopyDao borrowBooksCopyDao;
    private final BorrowBooksCopyDamagePhotoDao borrowBooksCopyDamagePhotoDao;
    private final ReaderDao readerDao;
    private final BookCopyDao bookCopyDao;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final BookAuthorDao bookAuthorDao;
    private final GenreDao genreDao;
    private final BookGenreDao bookGenreDao;
    private final BookCoverPhotoDao bookCoverPhotoDao;

    public BorrowServiceImpl()
    {
        this.borrowDao = BorrowDaoFactory.getInstance().getDao();
        this.borrowBooksCopyDao = BorrowBooksCopyDaoFactory.getInstance().getDao();
        this.borrowBooksCopyDamagePhotoDao = BorrowBooksCopyDamagePhotoDaoFactory.getInstance().getDao();
        this.readerDao = ReaderDaoFactory.getInstance().getDao();
        this.bookCopyDao = BookCopyDaoFactory.getInstance().getDao();
        this.bookDao = BookDaoFactory.getInstance().getDao();
        this.authorDao = AuthorDaoFactory.getInstance().getDao();
        this.bookAuthorDao = BookAuthorDaoFactory.getInstance().getDao();
        this.genreDao = GenreDaoFactory.getInstance().getDao();
        this.bookGenreDao = BookGenreDaoFactory.getInstance().getDao();
        this.bookCoverPhotoDao = BookCoverPhotoDaoFactory.getInstance().getDao();
    }

    public static BorrowServiceImpl getInstance() { return INSTANCE; }

    @Override
    public List<BorrowDto> findAll()
    {
        return checkOptionalList(
                borrowDao.findAll()
                        .map(borrows -> borrows.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public int getCountOfPages() { return borrowDao.getCountOfPages(); }

    @Override
    public BorrowDto findById(long id)
    {
        return borrowDao.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public List<BorrowDto> findAllReaderBorrows(long readerId)
    {
        return checkOptionalList(
                borrowDao.findAllByReaderId(readerId)
                        .map(borrows -> borrows.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BorrowDto> findNotReturnedReaderBorrows(long readerId)
    {
        return checkOptionalList(
                borrowDao.findNotReturnedByReaderId(readerId)
                        .map(borrows -> borrows.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BorrowDto> findExpiredBorrows()
    {
        return checkOptionalList(
                borrowDao.findExpired()
                        .map(borrows -> borrows.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public boolean updateBorrowStatus() { return borrowDao.updateBorrowStatusToExpired(); }

    @Override
    public boolean returnBorrow(BorrowDto borrowDto)
    {
        try
        {
            final Connection conn = POOL.retrieveConnection();
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try
            {
                Borrow currentBorrow = borrowDao.findById(borrowDto.getId())
                        .orElseThrow(() -> new TransactionException("Can't find borrow with id = " + borrowDto.getId()));
                if (!Objects.equals(currentBorrow.getReaderId(), borrowDto.getReader().getId()))
                    throw new TransactionException("Readers don't match in borrow with id = " + borrowDto.getId());
                List<BorrowRecordDto> records = borrowDto.getRecords();
                for (BorrowRecordDto record: records)
                {
                    BorrowBooksCopy updBorrowBooksCopy = BorrowBooksCopy.builder()
                            .id(record.getId())
                            .borrowId(borrowDto.getId())
                            .bookCopyId(record.getBookCopyDto().getId())
                            .bookCopyRating(record.getBookCopyRating())
                            .build();
                    if (!borrowBooksCopyDao.update(conn, updBorrowBooksCopy))
                    {
                        throw new TransactionException("Can't update borrow books copies");
                    }

                    List<String> damagePhotos = record.getDamagePhotos();
                    for (String damagePhoto : damagePhotos)
                    {
                        BorrowBooksCopyDamagePhoto photo = BorrowBooksCopyDamagePhoto.builder()
                                .borrowBooksCopyId(record.getId())
                                .damagePhotoPath(damagePhoto)
                                .build();
                        if (borrowBooksCopyDamagePhotoDao.create(conn, photo) == 0L)
                        {
                            throw new TransactionException("Can't create borrow books copy damage photos");
                        }
                    }

                    BookCopy bookCopy = BookCopy.builder()
                            .id(record.getBookCopyDto().getId())
                            .bookId(record.getBookCopyDto().getBookDto().getId())
                            .copyStatus(record.getBookCopyDto().getCopyStatus())
                            .copyState(record.getBookCopyDto().getCopyState())
                            .build();
                    if (!bookCopyDao.update(conn, bookCopy))
                    {
                        throw new TransactionException("Can't update books copies");
                    }
                }

                if (!borrowDao.updateBorrowCostAndStatusToReturned(conn, currentBorrow.getId(), borrowDto.getCost()))
                {
                    throw new TransactionException("Can't update borrow cost and status to 'returned' in borrow with id = "
                            + borrowDto.getId());
                }
                conn.commit();
                return true;
            }
            catch (TransactionException e)
            {
                LOGGER.error("Exception while trying to return borrow with id = " + borrowDto.getId() +
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
        return false;
    }

    @Override
    public BorrowDto create(BorrowDto borrowDto)
    {
        BorrowDto createdBorrow = null;
        try
        {
            final Connection conn = POOL.retrieveConnection();
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try
            {
                if (readerDao.findById(borrowDto.getReader().getId()).isEmpty())
                {
                    throw new TransactionException("Reader with id = " + borrowDto.getReader().getId() +
                            " not found");
                }
                Borrow newBorrow = BorrowFactory.getInstance().create(borrowDto);
                long newBorrowId = borrowDao.create(conn, newBorrow);
                if (newBorrowId == 0L) { throw new TransactionException("Can't create a new borrow"); }
                conn.commit();

                List<BorrowRecordDto> records = borrowDto.getRecords();

                for(BorrowRecordDto record : records)
                {
                    BorrowBooksCopy newBorrowBooksCopy = BorrowBooksCopy.builder()
                            .borrowId(newBorrowId)
                            .bookCopyId(record.getBookCopyDto().getId())
                            .bookCopyRating(record.getBookCopyRating())
                            .build();
                    if (borrowBooksCopyDao.create(conn, newBorrowBooksCopy) == 0L)
                    {
                        throw new TransactionException("Can't create borrow books copies");
                    }

                    BookCopy bookCopy = BookCopy.builder()
                            .id(record.getBookCopyDto().getId())
                            .copyStatus(record.getBookCopyDto().getCopyStatus())
                            .copyState(record.getBookCopyDto().getCopyState())
                            .build();
                    if (!bookCopyDao.update(conn, bookCopy))
                    {
                        throw new TransactionException("Can't update books copies");
                    }
                }
                conn.commit();
                createdBorrow = borrowDao.findById(newBorrowId).map(this::convertToDto).orElse(null);
            }
            catch (TransactionException e)
            {
                LOGGER.error("Exception while trying to create borrow for reader with id = "
                        + borrowDto.getReader().getId() + ": " + e.getLocalizedMessage());
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
        return createdBorrow;
    }

    @Override
    public BorrowDto update(BorrowDto borrowDto)
    {
        BorrowDto updatedBorrow = null;
        final Connection conn = POOL.retrieveConnection();
        try
        {
            if (!borrowDao.update(conn, BorrowFactory.getInstance().create(borrowDto)))
            {
                throw new TransactionException("Can't update a borrow");
            }
            updatedBorrow = borrowDao.findById(borrowDto.getId()).map(this::convertToDto).orElse(null);
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to update reader with id = " + borrowDto.getId() +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            POOL.returnConnection(conn);
        }
        return updatedBorrow;
    }

    private BorrowDto convertToDto(Borrow borrow)
    {
         ReaderDto readerDto = readerDao.findById(borrow.getReaderId()).map(this::convertToDto).orElse(null);
         List<BorrowRecordDto> records = borrowBooksCopyDao.findByBorrowId(borrow.getId())
                 .map(borrowBooksCopies -> borrowBooksCopies.stream()
                         .map(this::convertToDto)
                         .collect(Collectors.toList())).orElse(new ArrayList<>());
         return new BorrowDto(borrow.getId(), readerDto, borrow.getBorrowDate(), borrow.getBorrowTimePeriodDays(),
                borrow.getReturnDate(), borrow.getBorrowStatus(), borrow.getDiscountPercent(),
                borrow.getCost(), records);
    }

    private BorrowRecordDto convertToDto(BorrowBooksCopy borrowBooksCopy)
    {
        BookCopyDto bookCopyDto = bookCopyDao.findById(borrowBooksCopy.getBookCopyId())
                .map(this::convertToDto).orElse(null);
        List<String> damagePhotos = borrowBooksCopyDamagePhotoDao.findByBorrowBooksCopyId(borrowBooksCopy.getId())
                .map(borrowBooksCopyDamagePhotos -> borrowBooksCopyDamagePhotos.stream()
                        .map(BorrowBooksCopyDamagePhoto::getDamagePhotoPath)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        return new BorrowRecordDto(borrowBooksCopy.getId(), bookCopyDto, borrowBooksCopy.getBookCopyRating(),
                damagePhotos);
    }

    private ReaderDto convertToDto(Reader reader)
    {
        return new ReaderDto(reader.getId(), reader.getFirstName(), reader.getLastName(), reader.getMiddleName(),
                reader.getPassportNumber(), reader.getBirthDate(), reader.getEmail(), reader.getAddress());
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
