package ru.voroby.schedule;

import lombok.extern.slf4j.Slf4j;
import ru.voroby.config.Config;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;

@Slf4j
@Singleton
@Startup
public class ScheduleOperations {

    @Inject
    @Config("hello.message")
    private String value;

    @Resource
    ManagedScheduledExecutorService sch;

    @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
    private void execute() {
        log.info(value);
    }

}
