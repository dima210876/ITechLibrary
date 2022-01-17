package my.itechart.studlabs.project.itechLibrary.model.dto;

import my.itechart.studlabs.project.itechLibrary.model.entity.BookCopy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BorrowRecordDto
{
    private final Long id;
    private final BookCopyDto bookCopyDto;
    private final Double bookCopyRating;
    private final List<String> damagePhotos;

    public BorrowRecordDto(Long id, BookCopyDto bookCopyDto, Double bookCopyRating, List<String> damagePhotos)
    {
        this.id = id;
        this.bookCopyDto = bookCopyDto;
        this.bookCopyRating = bookCopyRating;
        this.damagePhotos = damagePhotos;
    }

    public Long getId() { return id; }

    public BookCopyDto getBookCopyDto() { return bookCopyDto; }

    public Double getBookCopyRating() { return bookCopyRating; }

    public List<String> getDamagePhotos() { return damagePhotos; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRecordDto that = (BorrowRecordDto) o;
        return Objects.equals(id, that.id) && Objects.equals(bookCopyDto, that.bookCopyDto) &&
                Objects.equals(bookCopyRating, that.bookCopyRating) && Objects.equals(damagePhotos, that.damagePhotos);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, bookCopyDto, bookCopyRating, damagePhotos);
    }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", bookCopyDto: " + bookCopyDto.toString() +
                ", bookCopyRating: " + bookCopyRating +
                ", damagePhotos: " + Arrays.toString(damagePhotos.toArray()) +
                '}';
    }
}
