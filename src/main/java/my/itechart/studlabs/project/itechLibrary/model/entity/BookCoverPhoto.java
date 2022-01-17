package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.util.Objects;

public class BookCoverPhoto
{
    private final Long id;
    private final Long bookId;
    private final String coverPhotoPath;

    public BookCoverPhoto(Long id, Long bookId, String coverPhotoPath)
    {
        this.id = id;
        this.bookId = bookId;
        this.coverPhotoPath = coverPhotoPath;
    }

    public static class BookCoverPhotoBuilder
    {
        private Long id;
        private Long bookId;
        private String coverPhotoPath;

        public BookCoverPhotoBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public BookCoverPhotoBuilder bookId(Long bookId)
        {
            this.bookId = bookId;
            return this;
        }

        public BookCoverPhotoBuilder coverPhotoPath(String coverPhotoPath)
        {
            this.coverPhotoPath = coverPhotoPath;
            return this;
        }

        public BookCoverPhoto build()
        {
            return new BookCoverPhoto(this.id, this.bookId, this.coverPhotoPath);
        }
    }

    public static BookCoverPhotoBuilder builder() { return new BookCoverPhotoBuilder(); }

    public Long getId() { return id; }

    public Long getBookId() { return bookId; }

    public String getCoverPhotoPath() { return coverPhotoPath; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCoverPhoto that = (BookCoverPhoto) o;
        return Objects.equals(id, that.id) && Objects.equals(bookId, that.bookId) &&
                Objects.equals(coverPhotoPath, that.coverPhotoPath);
    }

    @Override
    public int hashCode() { return Objects.hash(id, bookId, coverPhotoPath); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", bookId: " + bookId +
                ", coverPhotoPath: '" + coverPhotoPath + '\'' +
                '}';
    }
}
