package my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao;

import my.itechart.studlabs.project.itechLibrary.dao.ComplexDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.BorrowBooksCopy;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowBooksCopyDao implements ComplexDao<BorrowBooksCopy>
{
    private static final Logger LOGGER = LogManager.getLogger(BorrowBooksCopyDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_BORROW_BOOKS_COPY_BY_ID_SQL =
            "select id, borrow_id, book_copy_id, book_copy_rating from borrow_books_copy where id = ?";
    private static final String FIND_ALL_BORROW_BOOKS_COPY_SQL =
            "select id, borrow_id, book_copy_id, book_copy_rating from borrow_books_copy";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_ALL_BORROW_BOOKS_COPY_SQL = "select count(id) as count from borrow_books_copy";

    private static final String CREATE_BORROW_BOOKS_COPY_SQL =
            "insert into borrow_books_copy(borrow_id, book_copy_id, book_copy_rating) value (?,?,?)";
    private static final String UPDATE_BORROW_BOOKS_COPY_SQL =
            "update borrow_books_copy set book_copy_rating = ? where id = ?";
    private static final String DELETE_BORROW_BOOKS_COPY_SQL = "delete from borrow_books_copy where id = ?";

    private static final String FIND_BORROW_BOOKS_COPY_BY_BORROW_ID_SQL =
            "select id, borrow_id, book_copy_id, book_copy_rating from borrow_books_copy where borrow_id = ?";
    private static final String FIND_BORROW_BOOKS_COPY_BY_BOOK_COPY_ID_SQL =
            "select id, borrow_id, book_copy_id, book_copy_rating from borrow_books_copy where book_copy_id = ?";

    public BorrowBooksCopyDao() { }

    @Override
    public Optional<BorrowBooksCopy> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROW_BOOKS_COPY_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveBorrowBookCopyFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find borrow_books_copy by id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<BorrowBooksCopy>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_BORROW_BOOKS_COPY_SQL);
            return retrieveBorrowBooksCopyFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all borrow_books_copy: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public int getCountOfPages()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(GET_COUNT_OF_ALL_BORROW_BOOKS_COPY_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of all borrow_books_copy: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<BorrowBooksCopy>> findByBorrowId(long borrowId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROW_BOOKS_COPY_BY_BORROW_ID_SQL);
            statement.setLong(1, borrowId);
            resultSet = statement.executeQuery();
            return retrieveBorrowBooksCopyFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find borrow_books_copy by borrow id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<List<BorrowBooksCopy>> findByBookCopyId(long bookCopyId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROW_BOOKS_COPY_BY_BOOK_COPY_ID_SQL);
            statement.setLong(1, bookCopyId);
            resultSet = statement.executeQuery();
            return retrieveBorrowBooksCopyFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find borrow_books_copy by book copy id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BorrowBooksCopy> create(Connection conn, BorrowBooksCopy borrowBooksCopy)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(CREATE_BORROW_BOOKS_COPY_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, borrowBooksCopy.getBorrowId());
            statement.setLong(2, borrowBooksCopy.getBookCopyId());
            statement.setDouble(3, borrowBooksCopy.getBookCopyRating());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            long newId = resultSet.next() ? resultSet.getLong(1) : 0L;
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new borrow_books_copy: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BorrowBooksCopy> update(Connection conn, BorrowBooksCopy borrowBooksCopy)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(UPDATE_BORROW_BOOKS_COPY_SQL);
            statement.setDouble(1, borrowBooksCopy.getBookCopyRating());
            statement.setLong(2,borrowBooksCopy.getId());
            statement.executeUpdate();
            return findById(borrowBooksCopy.getId());
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update borrow with id = " + borrowBooksCopy.getId() +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(long id)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(DELETE_BORROW_BOOKS_COPY_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete borrow_books_copy with id = " + id + ": "
                    + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<BorrowBooksCopy>> retrieveBorrowBooksCopyFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<BorrowBooksCopy> borrowBooksCopies = new ArrayList<>();
        while (resultSet.next()) { borrowBooksCopies.add(retrieveBorrowBookCopyFromResultSet(resultSet)); }
        return Optional.of(borrowBooksCopies);
    }

    private BorrowBooksCopy retrieveBorrowBookCopyFromResultSet(ResultSet resultSet) throws SQLException
    {
        return BorrowBooksCopy.builder()
                .borrowId(resultSet.getLong("borrow_id"))
                .bookCopyId(resultSet.getLong("book_copy_id"))
                .bookCopyRating(resultSet.getDouble("book_copy_rating"))
                .build();
    }
}
