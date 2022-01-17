package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.util.Objects;

public class BookCopy
{
    private final Long id;
    private final Long bookId;
    private final String copyStatus;
    private final String copyState;

    public BookCopy(Long id, Long bookId, String copyStatus, String copyState)
    {
        this.id = id;
        this.bookId = bookId;
        this.copyStatus = copyStatus;
        this.copyState = copyState;
    }

    public static class BookCopyBuilder
    {
        private Long id;
        private Long bookId;
        private String copyStatus;
        private String copyState;

        public BookCopyBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public BookCopyBuilder bookId(Long bookId)
        {
            this.bookId = bookId;
            return this;
        }

        public BookCopyBuilder copyStatus(String copyStatus)
        {
            this.copyStatus = copyStatus;
            return this;
        }

        public BookCopyBuilder copyState(String copyState)
        {
            this.copyState = copyState;
            return this;
        }

        public BookCopy build()
        {
            return new BookCopy(this.id, this.bookId, this.copyStatus, this.copyState);
        }
    }

    public static BookCopyBuilder builder() { return new BookCopyBuilder(); }

    public Long getId() { return id; }

    public Long getBookId() { return bookId; }

    public String getCopyStatus() { return copyStatus; }

    public String getCopyState() { return copyState; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCopy bookCopy = (BookCopy) o;
        return Objects.equals(id, bookCopy.id) && Objects.equals(bookId, bookCopy.bookId) &&
                Objects.equals(copyStatus, bookCopy.copyStatus) && Objects.equals(copyState, bookCopy.copyState);
    }

    @Override
    public int hashCode() { return Objects.hash(id, bookId, copyStatus, copyState); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", bookId: " + bookId +
                ", copyStatus: '" + copyStatus + '\'' +
                ", copyState: '" + copyState + '\'' +
                '}';
    }
}
