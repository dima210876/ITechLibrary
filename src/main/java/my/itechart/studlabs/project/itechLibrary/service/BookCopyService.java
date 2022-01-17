package my.itechart.studlabs.project.itechLibrary.service;

import my.itechart.studlabs.project.itechLibrary.model.dto.BookCopyDto;
import java.util.List;

public interface BookCopyService extends DefaultService<BookCopyDto>
{
    List<BookCopyDto> findExistingBookCopiesByBookId(long bookId);

    List<BookCopyDto> findAvailableBookCopiesByBookId(long bookId);

    List<BookCopyDto> createNewCopies(BookCopyDto bookCopyDto, int copyCount);

    boolean deleteBookCopyById(long bookCopyId);

    boolean deleteBookCopiesByBookId(long bookId);

    boolean changeBookCopyStatusById(long bookCopyId);

    boolean changeBookCopyStateById(long bookCopyId);
}
