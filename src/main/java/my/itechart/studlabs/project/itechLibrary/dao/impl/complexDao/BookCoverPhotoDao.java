package my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao;

import my.itechart.studlabs.project.itechLibrary.dao.ComplexDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.BookCoverPhoto;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCoverPhotoDao implements ComplexDao<BookCoverPhoto>
{
    private static final Logger LOGGER = LogManager.getLogger(BookCoverPhotoDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_BOOK_COVER_PHOTO_BY_ID_SQL =
            "select id, book_id, cover_photo_path from book_cover_photos where id = ?";
    private static final String FIND_ALL_BOOK_COVER_PHOTOS_SQL = "select id, book_id, cover_photo_path from book_cover_photos";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_BOOK_COVER_PHOTOS_SQL = "select count(id) as count from book_cover_photos";

    private static final String CREATE_BOOK_COVER_PHOTO_SQL = "insert into book_cover_photos(book_id, cover_photo_path) value (?,?)";
    private static final String UPDATE_BOOK_COVER_PHOTO_SQL = "update book_cover_photos set cover_photo_path = ? where id = ?";
    private static final String DELETE_BOOK_COVER_PHOTO_SQL = "delete from book_cover_photos where id = ?";
    private static final String DELETE_BOOK_COVER_PHOTO_BY_BOOK_ID_SQL = "delete from book_cover_photos where book_id = ?";

    private static final String FIND_BOOK_COVER_PHOTOS_BY_BOOK_ID_SQL =
            "select id, book_id, cover_photo_path from book_cover_photos where book_id = ?";

    public BookCoverPhotoDao() { }

    @Override
    public Optional<BookCoverPhoto> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BOOK_COVER_PHOTO_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveBookCoverPhotoFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find book cover photo by id: " + e.getLocalizedMessage());
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
    public Optional<List<BookCoverPhoto>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_BOOK_COVER_PHOTOS_SQL);
            return retrieveBookCoverPhotosFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all book cover photos: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_BOOK_COVER_PHOTOS_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of book cover photos: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<BookCoverPhoto>> findByBookId(long bookId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BOOK_COVER_PHOTOS_BY_BOOK_ID_SQL);
            statement.setLong(1, bookId);
            resultSet = statement.executeQuery();
            return retrieveBookCoverPhotosFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find book cover photos by book id: " + e.getLocalizedMessage());
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
    public Optional<BookCoverPhoto> create(Connection conn, BookCoverPhoto bookCoverPhoto)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(CREATE_BOOK_COVER_PHOTO_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, bookCoverPhoto.getBookId());
            statement.setString(2, bookCoverPhoto.getCoverPhotoPath());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            long newId = resultSet.next() ? resultSet.getLong(1) : 0L;
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new book cover photo: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookCoverPhoto> update(Connection conn, BookCoverPhoto bookCoverPhoto)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(UPDATE_BOOK_COVER_PHOTO_SQL);
            statement.setString(1, bookCoverPhoto.getCoverPhotoPath());
            statement.setLong(2, bookCoverPhoto.getId());
            statement.executeUpdate();
            return findById(bookCoverPhoto.getId());
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update book cover photo with id = " + bookCoverPhoto.getId() +
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
            statement = conn.prepareStatement(DELETE_BOOK_COVER_PHOTO_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete book cover photo with id = " + id + ": "
                    + e.getLocalizedMessage());
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
            statement = conn.prepareStatement(DELETE_BOOK_COVER_PHOTO_BY_BOOK_ID_SQL);
            statement.setLong(1, bookId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete cover photos with book_id = " + bookId +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return false;
    }

    private Optional<List<BookCoverPhoto>> retrieveBookCoverPhotosFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<BookCoverPhoto> bookCoverPhotos = new ArrayList<>();
        while (resultSet.next()) { bookCoverPhotos.add(retrieveBookCoverPhotoFromResultSet(resultSet)); }
        return Optional.of(bookCoverPhotos);
    }

    private BookCoverPhoto retrieveBookCoverPhotoFromResultSet(ResultSet resultSet) throws SQLException
    {
        return BookCoverPhoto.builder()
                .id(resultSet.getLong("id"))
                .bookId(resultSet.getLong("book_id"))
                .coverPhotoPath(resultSet.getString("cover_photo_path"))
                .build();
    }
}
