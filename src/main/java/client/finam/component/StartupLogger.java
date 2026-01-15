package client.finam.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupLogger {

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.description}")
    private String appDescription;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logStartupInformation();
    }

    private void logStartupInformation() {
        log.info("===================================================");
        log.info("Name: " + appName);
        log.info("Version:   " + appVersion);
        log.info("===================================================");
        log.info("Description: ");
        log.info(appDescription);
        log.info("===================================================");
    }
}