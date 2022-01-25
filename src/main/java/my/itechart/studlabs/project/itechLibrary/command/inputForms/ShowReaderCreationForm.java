package my.itechart.studlabs.project.itechLibrary.command.inputForms;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;

public enum ShowReaderCreationForm implements Command
{
    INSTANCE;

    private static final ResponseContext READER_CREATION_FORM_RESPONSE = new ResponseContext(UrlPatterns.CREATE_READER, false);

    @Override
    public ResponseContext execute(RequestContext request)
    {
        return READER_CREATION_FORM_RESPONSE;
    }
}
