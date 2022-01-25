package my.itechart.studlabs.project.itechLibrary.command;

import my.itechart.studlabs.project.itechLibrary.command.editData.CreateBook;
import my.itechart.studlabs.project.itechLibrary.command.editData.CreateReader;
import my.itechart.studlabs.project.itechLibrary.command.inputForms.ShowBookCreationForm;
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
    DEFAULT(ShowMainPage.INSTANCE);

    //SEARCH(ShowBookSearchPage.INSTANCE),
    //FIND_BOOK(FindBook.INSTANCE);


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
