package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.util.Objects;

public class BorrowBooksCopyDamagePhoto
{
    private final Long id;
    private final Long borrowBooksCopyId;
    private final String damagePhotoPath;

    public BorrowBooksCopyDamagePhoto(Long id, Long borrowBooksCopyId, String damagePhotoPath)
    {
        this.id = id;
        this.borrowBooksCopyId = borrowBooksCopyId;
        this.damagePhotoPath = damagePhotoPath;
    }

    public static class BorrowBooksCopyDamagePhotoBuilder
    {
        private Long id;
        private Long borrowBooksCopyId;
        private String damagePhotoPath;

        public BorrowBooksCopyDamagePhotoBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public BorrowBooksCopyDamagePhotoBuilder borrowBooksCopyId(Long borrowBooksCopyId)
        {
            this.borrowBooksCopyId = borrowBooksCopyId;
            return this;
        }

        public BorrowBooksCopyDamagePhotoBuilder damagePhotoPath(String damagePhotoPath)
        {
            this.damagePhotoPath = damagePhotoPath;
            return this;
        }

        public BorrowBooksCopyDamagePhoto build()
        {
            return new BorrowBooksCopyDamagePhoto(this.id, this.borrowBooksCopyId, this.damagePhotoPath);
        }
    }

    public static BorrowBooksCopyDamagePhotoBuilder builder() { return new BorrowBooksCopyDamagePhotoBuilder(); }

    public Long getId() { return id; }

    public Long getBorrowBooksCopyId() { return borrowBooksCopyId; }

    public String getDamagePhotoPath() { return damagePhotoPath; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowBooksCopyDamagePhoto that = (BorrowBooksCopyDamagePhoto) o;
        return Objects.equals(id, that.id) && Objects.equals(borrowBooksCopyId, that.borrowBooksCopyId) &&
                Objects.equals(damagePhotoPath, that.damagePhotoPath);
    }

    @Override
    public int hashCode() { return Objects.hash(id, borrowBooksCopyId, damagePhotoPath); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", borrowBooksCopyId: " + borrowBooksCopyId +
                ", damagePhotoPath: '" + damagePhotoPath + '\'' +
                '}';
    }
}