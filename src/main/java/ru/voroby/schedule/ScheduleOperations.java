package ru.voroby.schedule;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

@Slf4j
@Singleton
@Startup
public class ScheduleOperations {

    @Resource
    ManagedScheduledExecutorService sch;

    @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
    private void execute() {
        log.info("Hello from Application server!");
    }

}
