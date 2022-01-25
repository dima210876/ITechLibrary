package my.itechart.studlabs.project.itechLibrary.command;

public interface Command
{
    ResponseContext execute(RequestContext request);
}
