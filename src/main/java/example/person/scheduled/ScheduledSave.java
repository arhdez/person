package example.person.scheduled;

import example.person.service.CsvFileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduledSave {


    private final CsvFileService csvFileService;
    @Scheduled(fixedDelay = 5000)
    public void pintTask(){

        csvFileService.createFile();
        System.out.println("printing task");
    }
}
