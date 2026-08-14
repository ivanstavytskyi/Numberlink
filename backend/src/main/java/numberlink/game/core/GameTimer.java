package numberlink.game.core;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GameTimer {
    @Getter
    private final AtomicInteger seconds = new AtomicInteger(0);

    private ScheduledExecutorService scheduler;

    public void start() {
        seconds.set(0);
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            seconds.incrementAndGet();
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
