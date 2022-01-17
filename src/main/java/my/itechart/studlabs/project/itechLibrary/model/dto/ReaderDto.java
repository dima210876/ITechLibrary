package my.itechart.studlabs.project.itechLibrary.model.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReaderDto
{
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String passportNumber;
    private final LocalDate birthDate;
    private final String email;
    private final String address;

    public ReaderDto(Long id, String firstName, String lastName, String middleName, String passportNumber,
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
        ReaderDto readerDto = (ReaderDto) o;
        return Objects.equals(id, readerDto.id) && Objects.equals(firstName, readerDto.firstName) &&
                Objects.equals(lastName, readerDto.lastName) && Objects.equals(middleName, readerDto.middleName) &&
                Objects.equals(passportNumber, readerDto.passportNumber) &&
                Objects.equals(birthDate, readerDto.birthDate) && Objects.equals(email, readerDto.email) &&
                Objects.equals(address, readerDto.address);
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
                ", birthDate: " + birthDate +
                ", email: '" + email + '\'' +
                ", address: '" + address + '\'' +
                '}';
    }
}
