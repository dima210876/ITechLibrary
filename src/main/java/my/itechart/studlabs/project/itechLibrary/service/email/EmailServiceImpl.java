package my.itechart.studlabs.project.itechLibrary.service.email;

import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class EmailServiceImpl implements EmailService
{
    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);
    private static final EmailService INSTANCE = new EmailServiceImpl();
    private static final String NAME_OF_JOB = "sendEmail";
    private static final String NAME_OF_GROUP = "emailGroup";
    private static final String NAME_OF_TRIGGER = "triggerStart";
    private static final int TIME_INTERVAL = 24;
    private Scheduler scheduler;

    private EmailServiceImpl() { }

    public static EmailService getInstance() { return INSTANCE; }

    public void startScheduler()
    {
        try
        {
            BasicConfigurator.configure();
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            JobDetail jobInstance = JobBuilder.newJob(EmailSender.class)
                    .withIdentity(NAME_OF_JOB, NAME_OF_GROUP)
                    .build();
            Trigger triggerNew = TriggerBuilder.newTrigger()
                    .withIdentity(NAME_OF_TRIGGER, NAME_OF_GROUP)
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(TIME_INTERVAL).repeatForever())
                    .build();
            scheduler.scheduleJob(jobInstance, triggerNew);
        }
        catch (SchedulerException e)
        {
            LOGGER.error("SchedulerException while trying to start schedule: " + e.getLocalizedMessage());
        }
    }

    public void shutdownScheduler()
    {
        try { scheduler.shutdown(); }
        catch (SchedulerException e)
        {
            LOGGER.error("SchedulerException while trying to shutdown schedule: " + e.getLocalizedMessage());
        }
    }
}
