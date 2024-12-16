package example.person.scheduled;

import example.person.service.CsvFileService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduledSave {


    private final CsvFileService csvFileService;
    @Scheduled(cron = "0 0/10 * * * ?")
    public void pintTask(){

        csvFileService.createFile();
        System.out.println("printing task");
    }
}
