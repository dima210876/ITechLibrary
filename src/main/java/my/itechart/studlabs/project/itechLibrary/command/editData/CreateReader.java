package my.itechart.studlabs.project.itechLibrary.command.editData;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowReaderCreationForm;
import my.itechart.studlabs.project.itechLibrary.command.page.ShowReadersPage;
import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.service.ReaderService;
import my.itechart.studlabs.project.itechLibrary.service.impl.ReaderServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum CreateReader implements Command
{
    INSTANCE;

    private static final String LAST_NAME_PARAMETER_NAME = "lastName";
    private static final String FIRST_NAME_PARAMETER_NAME = "firstName";
    private static final String MIDDLE_NAME_PARAMETER_NAME = "middleName";
    private static final String PASSPORT_NUMBER_PARAMETER_NAME = "passportNumber";
    private static final String BIRTH_DATE_PARAMETER_NAME = "birthDate";
    private static final String EMAIL_PARAMETER_NAME = "email";
    private static final String ADDRESS_PARAMETER_NAME = "address";

    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";

    private final ReaderService readerService = ReaderServiceImpl.getInstance();

    @Override
    public ResponseContext execute(RequestContext request)
    {
        String lastName = String.valueOf(request.getParameter(LAST_NAME_PARAMETER_NAME)).trim();
        String firstName = String.valueOf(request.getParameter(FIRST_NAME_PARAMETER_NAME)).trim();
        String middleName = String.valueOf(request.getParameter(MIDDLE_NAME_PARAMETER_NAME)).trim();
        String passportNumber = String.valueOf(request.getParameter(PASSPORT_NUMBER_PARAMETER_NAME)).trim();
        String birthDate = String.valueOf(request.getParameter(BIRTH_DATE_PARAMETER_NAME)).trim();
        String email = String.valueOf(request.getParameter(EMAIL_PARAMETER_NAME)).trim();
        String address = String.valueOf(request.getParameter(ADDRESS_PARAMETER_NAME)).trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ReaderDto reader = new ReaderDto(0L, firstName, lastName, middleName, passportNumber,
                LocalDate.parse(birthDate, formatter), email, address);
        ReaderDto createdReader = readerService.create(reader);
        if (createdReader == null)
        {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, "Сохранить читателя не удалось.");
            return ShowReaderCreationForm.INSTANCE.execute(request);
        }
        return ShowReadersPage.INSTANCE.execute(request);
    }
}
