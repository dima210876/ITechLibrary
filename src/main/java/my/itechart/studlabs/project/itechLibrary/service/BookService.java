package my.itechart.studlabs.project.itechLibrary.service;

import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;

import java.util.List;

public interface BookService extends DefaultService<BookDto>
{
    List<String> findBookPhotos(long bookId);

    List<BookDto> findByDefaultSortingAndPage(int page);

    List<BookDto> findBySortingAndPage(String sortingColumn, int page);

    List<BookDto> findByReversedSortingAndPage(String sortingColumn, int page);

    List<BookDto> findBySearchStringAndPage(String searchString, int page);

    boolean addBookPhoto(String photoPath, long bookId);

    void deleteById(long bookId);
}
