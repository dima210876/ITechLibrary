package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.util.Objects;

public class BorrowBooksCopy
{
    private final Long id;
    private final Long borrowId;
    private final Long bookCopyId;
    private final Double bookCopyRating;

    public BorrowBooksCopy(Long id, Long borrowId, Long bookCopyId, Double bookCopyRating)
    {
        this.id = id;
        this.borrowId = borrowId;
        this.bookCopyId = bookCopyId;
        this.bookCopyRating = bookCopyRating;
    }

    public static class BorrowBooksCopyBuilder
    {
        private Long id;
        private Long borrowId;
        private Long bookCopyId;
        private Double bookCopyRating;

        public BorrowBooksCopyBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public BorrowBooksCopyBuilder borrowId(Long borrowId)
        {
            this.borrowId = borrowId;
            return this;
        }

        public BorrowBooksCopyBuilder bookCopyId(Long bookCopyId)
        {
            this.bookCopyId = bookCopyId;
            return this;
        }

        public BorrowBooksCopyBuilder bookCopyRating(Double bookCopyRating)
        {
            this.bookCopyRating = bookCopyRating;
            return this;
        }

        public BorrowBooksCopy build()
        {
            return new BorrowBooksCopy(this.id, this.borrowId, this.bookCopyId, this.bookCopyRating);
        }
    }

    public static BorrowBooksCopyBuilder builder() { return new BorrowBooksCopyBuilder(); }

    public Long getId() { return id; }

    public Long getBorrowId() { return borrowId; }

    public Long getBookCopyId() { return bookCopyId; }

    public Double getBookCopyRating() { return bookCopyRating; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowBooksCopy that = (BorrowBooksCopy) o;
        return Objects.equals(id, that.id) && Objects.equals(borrowId, that.borrowId) &&
                Objects.equals(bookCopyId, that.bookCopyId) && Objects.equals(bookCopyRating, that.bookCopyRating);
    }

    @Override
    public int hashCode() { return Objects.hash(id, borrowId, bookCopyId, bookCopyRating); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", borrowId: " + borrowId +
                ", bookCopyId: " + bookCopyId +
                ", bookCopyRating: " + bookCopyRating +
                '}';
    }
}
