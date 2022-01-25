package my.itechart.studlabs.project.itechLibrary.command.page;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;
import my.itechart.studlabs.project.itechLibrary.command.UrlPatterns;

public enum RedirectToIndexPage implements Command
{
    INSTANCE;

    private static final ResponseContext INDEX_PAGE_RESPONSE = new ResponseContext(UrlPatterns.INDEX, true);

    @Override
    public ResponseContext execute(RequestContext request) { return INDEX_PAGE_RESPONSE; }
}
