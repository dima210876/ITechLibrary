package my.itechart.studlabs.project.itechLibrary.listener;

import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import my.itechart.studlabs.project.itechLibrary.service.email.EmailService;
import my.itechart.studlabs.project.itechLibrary.service.email.EmailServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener
{
    private final EmailService emailService = EmailServiceImpl.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        ConnectionPool.getInstance().init();
        emailService.startScheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        ConnectionPool.getInstance().destroy();
        emailService.shutdownScheduler();
    }
}
