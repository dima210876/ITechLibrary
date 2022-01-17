package my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao;

import my.itechart.studlabs.project.itechLibrary.dao.DefaultDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.Reader;
import my.itechart.studlabs.project.itechLibrary.model.factory.ReaderFactory;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDao implements DefaultDao<Reader>
{
    private static final Logger LOGGER = LogManager.getLogger(ReaderDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_READER_BY_ID_SQL = "select id, first_name, last_name, middle_name, passport_number, birth_date, email, address from readers where id = ?";
    private static final String FIND_ALL_READERS_SQL = "select id, first_name, last_name, middle_name, passport_number, birth_date, email, address from readers";
    private static final String FIND_READER_BY_EMAIL_SQL = "select id, first_name, last_name, middle_name, passport_number, birth_date, email, address from readers where email = ?";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_READERS_SQL = "select count(id) as count from readers";

    private static final String CREATE_READER_SQL = "insert into readers(first_name, last_name, middle_name, passport_number, birth_date, email, address) value (?,?,?,?,?,?,?)";
    private static final String UPDATE_READER_SQL = "update readers set first_name = ?, last_name = ?, middle_name = ?, passport_number = ?, birth_date = ?, email = ?, address = ? where id = ?";
    private static final String DELETE_READER_SQL = "delete from readers where id = ?";

    private static final String FIND_READERS_FOR_PAGE_WITH_SORTING_SQL = "select id, first_name, last_name, middle_name, passport_number, birth_date, email, address from readers order by ? limit ?";

    public ReaderDao() { }

    @Override
    public Optional<Reader> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_READER_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveReaderFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find reader by id: " + e.getLocalizedMessage());
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
    public Optional<List<Reader>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_READERS_SQL);
            return retrieveReadersFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all readers: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_READERS_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of readers: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<Reader>> findBySortingAndPageNumber(String sortingColumn, int page)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_READERS_FOR_PAGE_WITH_SORTING_SQL);
            statement.setString(1, sortingColumn);
            statement.setString(2, Integer.valueOf(20 * (page - 1)).toString() + ",20");
            resultSet = statement.executeQuery();
            return retrieveReadersFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find books by sorting & page number: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<Reader> findByEmail(String email)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_READER_BY_EMAIL_SQL);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            return Optional.of(retrieveReaderFromResultSet(resultSet));
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find book by id: " + e.getLocalizedMessage());
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
    public Optional<Reader> create(Reader reader)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(CREATE_READER_SQL);
            fillPreparedStatement(reader, statement);
            statement.execute();
            long newId = statement.getGeneratedKeys().getLong("id");
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new reader: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Reader> update(Reader reader)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(UPDATE_READER_SQL);
            fillPreparedStatement(reader, statement);
            statement.setLong(8, reader.getId());
            statement.executeUpdate();
            return Optional.of(reader);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update reader with id = " + reader.getId() +
                    ": " + e.getLocalizedMessage());
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
            statement = conn.prepareStatement(DELETE_READER_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete reader with id = " + id +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<Reader>> retrieveReadersFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<Reader> readers = new ArrayList<>();
        while (resultSet.next()) { readers.add(retrieveReaderFromResultSet(resultSet)); }
        return Optional.of(readers);
    }

    private Reader retrieveReaderFromResultSet(ResultSet resultSet) throws SQLException
    {
        return ReaderFactory.getInstance().create(resultSet);
    }

    private void fillPreparedStatement(Reader reader, PreparedStatement preparedStatement) throws SQLException
    {
        preparedStatement.setString(1, reader.getFirstName());
        preparedStatement.setString(2, reader.getLastName());
        preparedStatement.setString(3, reader.getMiddleName());
        preparedStatement.setString(4, reader.getPassportNumber());
        preparedStatement.setDate(5, Date.valueOf(reader.getBirthDate()));
        preparedStatement.setString(6, reader.getEmail());
        preparedStatement.setString(7, reader.getAddress());
    }
}
