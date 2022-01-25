package my.itechart.studlabs.project.itechLibrary.command;

import javax.servlet.http.HttpSession;

public interface RequestContext
{
    void setAttribute(String name, Object obj);

    void resetParameter(String name);

    void removeSessionAttribute(String name);

    HttpSession getSession();

    Object getParameter(String name);

    String[] getParameterValues(String name);

    void invalidateSession();

    void setSessionAttribute(String name, Object value);

    Object getSessionAttribute(String name);

    String getHeader(String referer);

    String getCookieValue(String name);
}
