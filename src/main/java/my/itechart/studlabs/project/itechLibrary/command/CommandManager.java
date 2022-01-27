package my.itechart.studlabs.project.itechLibrary.command;

import my.itechart.studlabs.project.itechLibrary.command.editData.CreateBook;
import my.itechart.studlabs.project.itechLibrary.command.editData.CreateBorrow;
import my.itechart.studlabs.project.itechLibrary.command.editData.CreateReader;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowBookCreationForm;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowBorrowConfirmationForm;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowBorrowCreationForm;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowReaderCreationForm;
import my.itechart.studlabs.project.itechLibrary.command.page.*;
//import my.itechart.studlabs.project.itechLibrary.command.search.FindBook;

public enum CommandManager
{
    MAIN(ShowMainPage.INSTANCE),
    BOOK(ShowBookPage.INSTANCE),
    BOOK_CREATION_FORM(ShowBookCreationForm.INSTANCE),
    CREATE_BOOK(CreateBook.INSTANCE),
    READERS(ShowReadersPage.INSTANCE),
    READER(ShowReaderPage.INSTANCE),
    READER_CREATION_FORM(ShowReaderCreationForm.INSTANCE),
    CREATE_READER(CreateReader.INSTANCE),
    CREATE_BORROW_ORDER(ShowBorrowCreationForm.INSTANCE),
    CONFIRM_BORROW_ORDER(ShowBorrowConfirmationForm.INSTANCE),
    CREATE_BORROW(CreateBorrow.INSTANCE),
    DEFAULT(ShowMainPage.INSTANCE);

    private final Command command;

    CommandManager(Command command) { this.command = command; }

    public static Command of(String name)
    {
        for (CommandManager action : values())
        {
            if (action.name().equalsIgnoreCase(name)) { return action.command; }
        }
        return DEFAULT.command;
    }
}
