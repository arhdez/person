package example.person.scheduled;

import example.person.service.BucketSchedulerService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BucketScheduler {

    private final BucketSchedulerService bucketSchedulerService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        bucketSchedulerService.schedule();
    }
}
