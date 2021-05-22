package bank.bankproject.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InfoLogger {
    public void log(String message) {
        log.info(message);
    }
}
