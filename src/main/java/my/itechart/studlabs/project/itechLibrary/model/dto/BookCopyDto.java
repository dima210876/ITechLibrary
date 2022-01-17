package my.itechart.studlabs.project.itechLibrary.model.dto;

import my.itechart.studlabs.project.itechLibrary.model.entity.Book;

import java.util.Objects;

public class BookCopyDto
{
    private final Long id;
    private final BookDto bookDto;
    private final String copyStatus;
    private final String copyState;

    public BookCopyDto(Long id, BookDto bookDto, String copyStatus, String copyState)
    {
        this.id = id;
        this.bookDto = bookDto;
        this.copyStatus = copyStatus;
        this.copyState = copyState;
    }

    public Long getId() { return id; }

    public BookDto getBookDto() { return bookDto; }

    public String getCopyStatus() { return copyStatus; }

    public String getCopyState() { return copyState; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCopyDto that = (BookCopyDto) o;
        return Objects.equals(id, that.id) && Objects.equals(bookDto, that.bookDto) &&
                Objects.equals(copyStatus, that.copyStatus) && Objects.equals(copyState, that.copyState);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, bookDto, copyStatus, copyState);
    }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", bookDto: " + bookDto.toString() +
                ", copyStatus: '" + copyStatus + '\'' +
                ", copyState: '" + copyState + '\'' +
                '}';
    }
}
