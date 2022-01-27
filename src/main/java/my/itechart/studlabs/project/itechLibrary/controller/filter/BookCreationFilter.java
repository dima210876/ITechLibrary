package my.itechart.studlabs.project.itechLibrary.controller.filter;

import my.itechart.studlabs.project.itechLibrary.command.WrappingRequestContext;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowBookCreationForm;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebFilter(filterName = "BookCreationFilter", urlPatterns = {"/controller"})
public class BookCreationFilter implements Filter
{
    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String CREATE_BOOK_COMMAND_VALUE = "create_book";
    private static final String SHOW_BOOK_CREATION_FORM_COMMAND_VALUE = "book_creation_form";

    private static final String TITLE_RU_PARAMETER_NAME = "titleRu";
    private static final String BOOK_COST_PARAMETER_NAME = "bookCost";
    private static final String DAY_COST_PARAMETER_NAME = "dayCost";
    private static final String EDITION_YEAR_PARAMETER_NAME = "editionYear";
    private static final String PAGE_COUNT_PARAMETER_NAME = "pageCount";
    private static final String REGISTRATION_DATE_PARAMETER_NAME = "registrationDate";

    private static final String AUTHORS_PARAMETER_NAME = "bookAuthors";
    private static final String GENRES_PARAMETER_NAME = "bookGenres";
    private static final String COPY_COUNT_PARAMETER_NAME = "copyCount";

    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_ATTRIBUTE_VALUE = "Введены некорректные данные!";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        final String command = String.valueOf(servletRequest.getParameter(COMMAND_PARAMETER_NAME));
        if (command.equals(CREATE_BOOK_COMMAND_VALUE))
        {
            final String titleRu = String.valueOf(servletRequest.getParameter(TITLE_RU_PARAMETER_NAME)).trim();
            final String bookCost = String.valueOf(servletRequest.getParameter(BOOK_COST_PARAMETER_NAME)).trim();
            final String dayCost = String.valueOf(servletRequest.getParameter(DAY_COST_PARAMETER_NAME)).trim();
            final String editionYear = String.valueOf(servletRequest.getParameter(EDITION_YEAR_PARAMETER_NAME)).trim();
            final String pageCount = String.valueOf(servletRequest.getParameter(PAGE_COUNT_PARAMETER_NAME)).trim();
            final String registrationDate = String.valueOf(servletRequest.getParameter(REGISTRATION_DATE_PARAMETER_NAME)).trim();
            final String copyCount = String.valueOf(servletRequest.getParameter(COPY_COUNT_PARAMETER_NAME)).trim();
            final String authors = String.valueOf(servletRequest.getParameter(AUTHORS_PARAMETER_NAME)).trim();
            final String genres = String.valueOf(servletRequest.getParameter(GENRES_PARAMETER_NAME)).trim();

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (titleRu.isEmpty() || titleRu.equals("null") ||
                    bookCost.isEmpty() || bookCost.equals("null") || (Double.parseDouble(bookCost) < 0) ||
                    dayCost.isEmpty() || dayCost.equals("null") || (Double.parseDouble(dayCost) < 0) ||
                    (!editionYear.isEmpty() && (Integer.parseInt(editionYear) < 1900 || Integer.parseInt(editionYear) > LocalDate.now().getYear())) ||
                    registrationDate.isEmpty() || registrationDate.equals("null") || LocalDate.parse(registrationDate, formatter).isAfter(LocalDate.now()) ||
                    copyCount.equals("null") || Integer.parseInt(copyCount) < 1 || Integer.parseInt(copyCount) > 100 ||
                    authors.isEmpty() || authors.equals("null") ||
                    genres.isEmpty() || genres.equals("null") ||
                    (!pageCount.isEmpty() && (Integer.parseInt(pageCount) < 1 || Integer.parseInt(pageCount) > 10000))
            )
            {
                final RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(ShowBookCreationForm.INSTANCE.execute(WrappingRequestContext.of((HttpServletRequest) servletRequest)).getPage());
                servletRequest.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE);
                servletRequest.setAttribute(COMMAND_PARAMETER_NAME, SHOW_BOOK_CREATION_FORM_COMMAND_VALUE);
                dispatcher.forward(servletRequest, servletResponse);
            }
            else { filterChain.doFilter(servletRequest, servletResponse); }
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() { }
}
