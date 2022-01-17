package my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao;

import my.itechart.studlabs.project.itechLibrary.dao.RelativeDao;
import my.itechart.studlabs.project.itechLibrary.model.record.RelationRecord;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class BookGenreDao implements RelativeDao
{
    private static final Logger LOGGER = LogManager.getLogger(BookGenreDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_RECORD_BY_ID_SQL =
            "select id, book_id, genre_id from book_genres where id = ?";
    private static final String FIND_RECORDS_BY_BOOK_ID_SQL =
            "select id, book_id, genre_id from book_genres where book_id = ?";
    private static final String FIND_RECORDS_BY_GENRE_ID_SQL =
            "select id, book_id, genre_id from book_genres where genre_id = ?";
    private static final String FIND_ALL_RECORDS_SQL = "select id, book_id, genre_id from book_genres";

    private static final String CREATE_RECORD_SQL = "insert into book_genres(book_id, genre_id) value (?,?)";
    private static final String DELETE_RECORD_SQL = "delete from book_genres where id = ?";
    private static final String DELETE_RECORD_BY_BOOK_ID_SQL = "delete from book_genres where book_id = ?";

    public BookGenreDao() { }

    @Override
    public Optional<RelationRecord> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_RECORD_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveRelationRecordFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find book-genre relation record by id = " + id +
                    ": " + e.getLocalizedMessage());
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
    public Optional<List<RelationRecord>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_RECORDS_SQL);
            return retrieveRelationRecordsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all book-genre relation records: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<List<RelationRecord>> findByBookId(long bookId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_RECORDS_BY_BOOK_ID_SQL);
            statement.setLong(1, bookId);
            resultSet = statement.executeQuery();
            return retrieveRelationRecordsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all book-genre relation records for bookId = " + bookId +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<List<RelationRecord>> findByGenreId(long genreId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_RECORDS_BY_GENRE_ID_SQL);
            statement.setLong(1, genreId);
            resultSet = statement.executeQuery();
            return retrieveRelationRecordsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all book-author relation records for genreId = " + genreId +
                    ": " + e.getLocalizedMessage());
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
    public Optional<RelationRecord> create(RelationRecord relationRecord)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(CREATE_RECORD_SQL);
            statement.setLong(1, relationRecord.getFirstId());
            statement.setLong(2, relationRecord.getSecondId());
            statement.execute();
            long newId = statement.getGeneratedKeys().getLong("id");
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new book-genre relation record: "
                    + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
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
            statement = conn.prepareStatement(DELETE_RECORD_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete book-genre relation record with id = " + id +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    public boolean deleteByBookId(long bookId)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(DELETE_RECORD_BY_BOOK_ID_SQL);
            statement.setLong(1, bookId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete book-genre relation records with book_id = " + bookId +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }
}
