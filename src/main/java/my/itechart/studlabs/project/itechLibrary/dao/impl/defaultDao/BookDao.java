package my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao;

import my.itechart.studlabs.project.itechLibrary.dao.DefaultDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.Book;
import my.itechart.studlabs.project.itechLibrary.model.factory.BookFactory;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao implements DefaultDao<Book>
{
    private static final Logger LOGGER = LogManager.getLogger(BookDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_BOOK_BY_ID_SQL = "select id, title_ru, title_origin, description, book_cost, day_cost, edition_year, page_count, registration_date from books where id = ?";
    private static final String FIND_ALL_BOOKS_SQL = "select id, title_ru, title_origin, description, book_cost, day_cost, edition_year, page_count, registration_date from books";
    private static final String FIND_BOOK_BY_TITLE_RU_SQL = "select id, title_ru, title_origin, description, book_cost, day_cost, edition_year, page_count, registration_date from books where title_ru = ?";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_BOOKS_SQL = "select count(id) as count from books";

    private static final String CREATE_BOOK_SQL = "insert into books(title_ru, title_origin, description, book_cost, day_cost, edition_year, page_count, registration_date) value (?,?,?,?,?,?,?,?)";
    private static final String UPDATE_BOOK_SQL = "update books set title_ru = ?, title_origin = ?, description = ?, book_cost = ?, day_cost = ?, edition_year = ?, page_count = ?, registration_date = ? where id = ?";
    private static final String DELETE_BOOK_SQL = "delete from books where id = ?";

    private static final String FIND_BOOKS_FOR_PAGE_WITH_SORTING_SQL = "select id, title_ru, title_origin, description, book_cost, day_cost, edition_year, page_count, registration_date from books order by ? limit ?";

    public BookDao() { }

    @Override
    public Optional<Book> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BOOK_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveBookFromResultSet(resultSet)); }
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
    public Optional<List<Book>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_BOOKS_SQL);
            return retrieveBooksFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all books: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_BOOKS_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of books: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<Book>> findBySortingAndPageNumber(String sortingColumn, int page)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BOOKS_FOR_PAGE_WITH_SORTING_SQL);
            statement.setString(1, sortingColumn);
            statement.setString(2, Integer.valueOf(20 * (page - 1)).toString() + ",20");
            resultSet = statement.executeQuery();
            return retrieveBooksFromResultSet(resultSet);
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

    public Optional<List<Book>> findByTitle(String title)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BOOK_BY_TITLE_RU_SQL);
            statement.setString(1, title);
            resultSet = statement.executeQuery();
            return retrieveBooksFromResultSet(resultSet);
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
    public Optional<Book> create(Book book)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(CREATE_BOOK_SQL);
            fillPreparedStatement(book, statement);
            statement.execute();
            long newId = statement.getGeneratedKeys().getLong("id");
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new book: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> update(Book book)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(UPDATE_BOOK_SQL);
            fillPreparedStatement(book, statement);
            statement.setLong(9, book.getId());
            statement.executeUpdate();
            return Optional.of(book);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update book with id = " + book.getId() + " and titleRu = \""
                    + book.getTitleRu() + "\" " + e.getLocalizedMessage());
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
            statement = conn.prepareStatement(DELETE_BOOK_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete book with id = " + id + ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<Book>> retrieveBooksFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) { books.add(retrieveBookFromResultSet(resultSet)); }
        return Optional.of(books);
    }

    private Book retrieveBookFromResultSet(ResultSet resultSet) throws SQLException
    {
        return BookFactory.getInstance().create(resultSet);
    }

    private void fillPreparedStatement(Book book, PreparedStatement preparedStatement) throws SQLException
    {
        preparedStatement.setString(1, book.getTitleRu());
        preparedStatement.setString(2, book.getTitleOrigin());
        preparedStatement.setString(3, book.getDescription());
        preparedStatement.setDouble(4, book.getBookCost());
        preparedStatement.setDouble(5, book.getDayCost());
        preparedStatement.setInt(6, book.getEditionYear());
        preparedStatement.setInt(7, book.getPageCount());
        preparedStatement.setDate(8, Date.valueOf(book.getRegistrationDate()));
    }
}
