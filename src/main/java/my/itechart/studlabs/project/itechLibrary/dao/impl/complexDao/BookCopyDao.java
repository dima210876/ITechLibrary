package my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao;

import my.itechart.studlabs.project.itechLibrary.dao.ComplexDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.BookCopy;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCopyDao implements ComplexDao<BookCopy>
{
    private static final Logger LOGGER = LogManager.getLogger(BookCopyDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_BOOK_COPY_BY_ID_SQL = "select id, book_id, copy_status, copy_state from book_copies where id = ?";
    private static final String FIND_ALL_BOOK_COPIES_SQL = "select id, book_id, copy_status, copy_state from book_copies";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_ALL_BOOK_COPIES_SQL = "select count(id) as count from book_copies";

    private static final String CREATE_BOOK_COPY_SQL = "insert into book_copies(book_id, copy_status, copy_state) value (?,?,?)";
    private static final String UPDATE_BOOK_COPY_SQL = "update book_copies set copy_status = ?, copy_state = ? where id = ?";
    private static final String DELETE_BOOK_COPY_SQL = "update book_copies set copy_status = 'deleted' where id = ?";
    private static final String DELETE_BOOK_COPIES_BY_BOOK_ID_SQL = "update book_copies set copy_status = 'deleted' where book_id = ?";

    private static final String FIND_EXISTING_BOOK_COPIES_BY_BOOK_ID_SQL =
            "select id, book_id, copy_status, copy_state from book_copies where book_id = ? and copy_status != 'deleted'";
    private static final String FIND_AVAILABLE_BOOK_COPIES_BY_BOOK_ID_SQL =
            "select id, book_id, copy_status, copy_state from book_copies where book_id = ? and copy_status = 'available'";
    private static final String GET_COUNT_OF_EXISTING_BOOK_COPIES_BY_BOOK_ID_SQL =
            "select count(id) as count from book_copies where book_id = ? and copy_status != 'deleted'";
    private static final String GET_COUNT_OF_AVAILABLE_BOOK_COPIES_BY_BOOK_ID_SQL =
            "select count(id) as count from book_copies where book_id = ? and copy_status = 'available'";

    public BookCopyDao() { }

    @Override
    public Optional<BookCopy> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BOOK_COPY_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveBookCopyFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find book copy by id: " + e.getLocalizedMessage());
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
    public Optional<List<BookCopy>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_BOOK_COPIES_SQL);
            return retrieveBookCopiesFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all book copies: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_ALL_BOOK_COPIES_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of book copies: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<BookCopy>> findExistingByBookId(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_EXISTING_BOOK_COPIES_BY_BOOK_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            return retrieveBookCopiesFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find existing book copies by book id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<List<BookCopy>> findAvailableByBookId(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_AVAILABLE_BOOK_COPIES_BY_BOOK_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            return retrieveBookCopiesFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find available book copies by book id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public int getExistingCountByBookId(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(GET_COUNT_OF_EXISTING_BOOK_COPIES_BY_BOOK_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of existing book copies by book id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public int getAvailableCountByBookId(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(GET_COUNT_OF_AVAILABLE_BOOK_COPIES_BY_BOOK_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of available book copies by book id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    @Override
    public long create(Connection conn, BookCopy bookCopy)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(CREATE_BOOK_COPY_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, bookCopy.getBookId());
            statement.setString(2, bookCopy.getCopyStatus());
            statement.setString(3, bookCopy.getCopyState());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getLong(1) : 0L; //id of created book copy, otherwise 0
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new book copy: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return 0L;
    }

    @Override
    public boolean update(Connection conn, BookCopy bookCopy)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(UPDATE_BOOK_COPY_SQL);
            statement.setString(1, bookCopy.getCopyStatus());
            statement.setString(2, bookCopy.getCopyState());
            statement.setLong(3, bookCopy.getId());
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update book copy with id = " + bookCopy.getId() +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return false;
    }

    @Override
    public boolean delete(long id)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(DELETE_BOOK_COPY_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete book copy with id = " + id +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    public boolean deleteAllByBookId(long bookId)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(DELETE_BOOK_COPIES_BY_BOOK_ID_SQL);
            statement.setLong(1, bookId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete all book copies with book_id = " + bookId +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<BookCopy>> retrieveBookCopiesFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<BookCopy> bookCopies = new ArrayList<>();
        while (resultSet.next()) { bookCopies.add(retrieveBookCopyFromResultSet(resultSet)); }
        return Optional.of(bookCopies);
    }

    private BookCopy retrieveBookCopyFromResultSet(ResultSet resultSet) throws SQLException
    {
        return BookCopy.builder()
                .id(resultSet.getLong("id"))
                .bookId(resultSet.getLong("book_id"))
                .copyStatus(resultSet.getString("copy_status"))
                .copyState(resultSet.getString("copy_state"))
                .build();
    }
}
