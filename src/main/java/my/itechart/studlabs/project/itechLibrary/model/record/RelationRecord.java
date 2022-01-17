package my.itechart.studlabs.project.itechLibrary.model.record;

import java.util.Objects;

public class RelationRecord
{
    private final Long id;
    private final Long firstId;
    private final Long secondId;

    public RelationRecord(Long id, Long firstId, Long secondId)
    {
        this.id = id;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    public static class RelationRecordBuilder
    {
        private Long id;
        private Long firstId;
        private Long secondId;

        public RelationRecordBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public RelationRecordBuilder firstId(Long firstId)
        {
            this.firstId = firstId;
            return this;
        }

        public RelationRecordBuilder secondId(Long secondId)
        {
            this.secondId = secondId;
            return this;
        }

        public RelationRecord build()
        {
            return new RelationRecord(this.id, this.firstId, this.secondId);
        }
    }

    public static RelationRecordBuilder builder() { return new RelationRecordBuilder(); }

    public Long getId() { return id; }

    public Long getFirstId() { return firstId; }

    public Long getSecondId() { return secondId; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationRecord that = (RelationRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(firstId, that.firstId) &&
                Objects.equals(secondId, that.secondId);
    }

    @Override
    public int hashCode() { return Objects.hash(id, firstId, secondId); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", firstId: " + firstId +
                ", secondId: " + secondId +
                '}';
    }
}
