package my.itechart.studlabs.project.itechLibrary.dao.impl.relativeDao;

import my.itechart.studlabs.project.itechLibrary.dao.RelativeDao;
import my.itechart.studlabs.project.itechLibrary.model.record.RelationRecord;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookAuthorDao implements RelativeDao
{
    private static final Logger LOGGER = LogManager.getLogger(BookAuthorDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_RECORD_BY_ID_SQL =
            "select id, book_id, author_id from book_authors where id = ?";
    private static final String FIND_RECORDS_BY_BOOK_ID_SQL =
            "select id, book_id, author_id from book_authors where book_id = ?";
    private static final String FIND_RECORDS_BY_AUTHOR_ID_SQL =
            "select id, book_id, author_id from book_authors where author_id = ?";
    private static final String FIND_ALL_RECORDS_SQL = "select id, book_id, author_id from book_authors";

    private static final String CREATE_RECORD_SQL = "insert into book_authors(book_id, author_id) value (?,?)";
    private static final String DELETE_RECORD_SQL = "delete from book_authors where id = ?";
    private static final String DELETE_RECORD_BY_BOOK_ID_SQL = "delete from book_authors where book_id = ?";

    public BookAuthorDao() { }

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
            LOGGER.error("SQLException while trying to find book-author relation record by id = " + id +
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
            LOGGER.error("SQLException while trying to find all book-author relation records: " + e.getLocalizedMessage());
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
            LOGGER.error("SQLException while trying to find all book-author relation records for bookId = " + bookId +
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

    public Optional<List<RelationRecord>> findByAuthorId(long authorId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_RECORDS_BY_AUTHOR_ID_SQL);
            statement.setLong(1, authorId);
            resultSet = statement.executeQuery();
            return retrieveRelationRecordsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all book-author relation records for authorId = " + authorId +
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
    public long create(Connection conn, RelationRecord relationRecord)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(CREATE_RECORD_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, relationRecord.getFirstId());
            statement.setLong(2, relationRecord.getSecondId());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getLong(1) : 0L; //id of created relation record, otherwise 0
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new book-author relation record: "
                    + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return 0L;
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
            LOGGER.error("SQLException while trying to delete book-author relation record with id = " + id +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    public boolean deleteByBookId(Connection conn, long bookId)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(DELETE_RECORD_BY_BOOK_ID_SQL);
            statement.setLong(1, bookId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete book-author relation records with book_id = " + bookId +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return false;
    }

    private Optional<List<RelationRecord>> retrieveRelationRecordsFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<RelationRecord> relationRecords = new ArrayList<>();
        while (resultSet.next()) { relationRecords.add(retrieveRelationRecordFromResultSet(resultSet)); }
        return Optional.of(relationRecords);
    }

    private RelationRecord retrieveRelationRecordFromResultSet(ResultSet resultSet) throws SQLException
    {
        return RelationRecord.builder()
                .id(resultSet.getLong("id"))
                .firstId(resultSet.getLong("book_id"))
                .secondId(resultSet.getLong("author_id"))
                .build();
    }
}
