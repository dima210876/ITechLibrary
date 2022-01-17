package my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao;

import my.itechart.studlabs.project.itechLibrary.dao.DefaultDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.Genre;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDao implements DefaultDao<Genre>
{
    private static final Logger LOGGER = LogManager.getLogger(GenreDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_GENRE_BY_ID_SQL = "select id, genre_name from genres where id = ?";
    private static final String FIND_GENRE_BY_GENRE_NAME_SQL = "select id, genre_name from genres where genre_name = ?";
    private static final String FIND_ALL_GENRES_SQL = "select id, genre_name from genres";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_GENRES_SQL = "select count(id) as count from genres";

    private static final String CREATE_GENRE_SQL = "insert into genres(genre_name) value (?)";
    private static final String UPDATE_GENRE_SQL = "update genres set genre_name = ? where id = ?";
    private static final String DELETE_GENRE_SQL = "delete from genres where id = ?";

    public GenreDao() { }

    @Override
    public Optional<Genre> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_GENRE_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveGenreFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find genre by id: " + e.getLocalizedMessage());
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
    public Optional<List<Genre>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_GENRES_SQL);
            return retrieveGenresFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all genres: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_GENRES_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of genres: " + e.getLocalizedMessage());
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
    public Optional<Genre> create(Genre genre)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(CREATE_GENRE_SQL);
            statement.setString(1, genre.getGenreName());
            statement.execute();
            long newId = statement.getGeneratedKeys().getLong("id");
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new genre: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Genre> update(Genre genre)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(UPDATE_GENRE_SQL);
            statement.setString(1, genre.getGenreName());
            statement.setLong(2, genre.getId());
            statement.executeUpdate();
            return Optional.of(genre);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update genre with id = " + genre.getId() +
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
            statement = conn.prepareStatement(DELETE_GENRE_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete genre with id = " + id +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    public Optional<Genre> findByGenreName(String genreName)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_GENRE_BY_GENRE_NAME_SQL);
            statement.setString(1, genreName);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveGenreFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find genre by id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    private Optional<List<Genre>> retrieveGenresFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<Genre> genres = new ArrayList<>();
        while (resultSet.next()) { genres.add(retrieveGenreFromResultSet(resultSet)); }
        return Optional.of(genres);
    }

    private Genre retrieveGenreFromResultSet(ResultSet resultSet) throws SQLException
    {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .genreName(resultSet.getString("genre_name"))
                .build();
    }
}
