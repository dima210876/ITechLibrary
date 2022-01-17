package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reader
{
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String passportNumber;
    private final LocalDate birthDate;
    private final String email;
    private final String address;

    public Reader(Long id, String firstName, String lastName, String middleName, String passportNumber,
                  LocalDate birthDate, String email, String address)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.passportNumber = passportNumber;
        this.birthDate = birthDate;
        this.email = email;
        this.address = address;
    }

    public static class ReaderBuilder
    {
        private Long id;
        private String firstName;
        private String lastName;
        private String middleName;
        private String passportNumber;
        private LocalDate birthDate;
        private String email;
        private String address;

        public ReaderBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public ReaderBuilder firstName(String firstName)
        {
            this.firstName = firstName;
            return this;
        }

        public ReaderBuilder lastName(String lastName)
        {
            this.lastName = lastName;
            return this;
        }

        public ReaderBuilder middleName(String middleName)
        {
            this.middleName = middleName;
            return this;
        }

        public ReaderBuilder passportNumber(String passportNumber)
        {
            this.passportNumber = passportNumber;
            return this;
        }

        public ReaderBuilder birthDate(LocalDate birthDate)
        {
            this.birthDate = birthDate;
            return this;
        }

        public ReaderBuilder email(String email)
        {
            this.email = email;
            return this;
        }

        public ReaderBuilder address(String address)
        {
            this.address = address;
            return this;
        }

        public Reader build()
        {
            return new Reader(
                    this.id,
                    this.firstName,
                    this.lastName,
                    this.middleName,
                    this.passportNumber,
                    this.birthDate,
                    this.email,
                    this.address
            );
        }
    }

    public static ReaderBuilder builder() { return new ReaderBuilder(); }

    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getMiddleName() { return middleName; }

    public String getPassportNumber() { return passportNumber; }

    public LocalDate getBirthDate() { return birthDate; }

    public String getEmail() { return email; }

    public String getAddress() { return address; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return Objects.equals(id, reader.id) && Objects.equals(firstName, reader.firstName) &&
                Objects.equals(lastName, reader.lastName) && Objects.equals(middleName, reader.middleName) &&
                Objects.equals(passportNumber, reader.passportNumber) && Objects.equals(birthDate, reader.birthDate) &&
                Objects.equals(email, reader.email) && Objects.equals(address, reader.address);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, firstName, lastName, middleName, passportNumber, birthDate, email, address);
    }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", middleName: '" + middleName + '\'' +
                ", passportNumber: '" + passportNumber + '\'' +
                ", birthDate: " + birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", email: '" + email + '\'' +
                ", address: '" + address + '\'' +
                '}';
    }
}
