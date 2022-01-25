package my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao;

import my.itechart.studlabs.project.itechLibrary.dao.DefaultDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.Author;

import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao implements DefaultDao<Author>
{
    private static final Logger LOGGER = LogManager.getLogger(AuthorDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_AUTHOR_BY_ID_SQL = "select id, name, surname, photo_path from authors where id = ?";
    private static final String FIND_ALL_AUTHORS_SQL = "select id, name, surname, photo_path from authors";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_AUTHORS_SQL = "select count(id) as count from authors";

    private static final String CREATE_AUTHOR_SQL = "insert into authors(name, surname, photo_path) value (?,?,?)";
    private static final String UPDATE_AUTHOR_SQL = "update authors set name = ?, surname = ?, photo_path = ? where id = ?";
    private static final String DELETE_AUTHOR_SQL = "delete from authors where id = ?";

    public AuthorDao() { }

    @Override
    public Optional<Author> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_AUTHOR_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveAuthorFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find author by id: " + e.getLocalizedMessage());
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
    public Optional<List<Author>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_AUTHORS_SQL);
            return retrieveAuthorsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all authors: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_AUTHORS_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of authors: " + e.getLocalizedMessage());
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
    public Optional<Author> create(Author author)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(CREATE_AUTHOR_SQL, Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(author, statement);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            long newId = resultSet.next() ? resultSet.getLong(1) : 0L;
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new author: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> update(Author author)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(UPDATE_AUTHOR_SQL);
            fillPreparedStatement(author, statement);
            statement.setLong(4, author.getId());
            statement.executeUpdate();
            return findById(author.getId());
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update author with id = " + author.getId() +
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
            statement = conn.prepareStatement(DELETE_AUTHOR_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete author with id = " + id +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<Author>> retrieveAuthorsFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<Author> authors = new ArrayList<>();
        while (resultSet.next()) { authors.add(retrieveAuthorFromResultSet(resultSet)); }
        return Optional.of(authors);
    }

    private Author retrieveAuthorFromResultSet(ResultSet resultSet) throws SQLException
    {
        return Author.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .photoPath(resultSet.getString("photo_path"))
                .build();
    }

    private void fillPreparedStatement(Author author, PreparedStatement preparedStatement) throws SQLException
    {
        preparedStatement.setString(1, author.getName());
        preparedStatement.setString(2, author.getSurname());
        preparedStatement.setString(3, author.getPhotoPath());
    }
}
