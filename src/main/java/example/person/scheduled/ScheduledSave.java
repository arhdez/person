package example.person.scheduled;

import example.person.service.CsvFileService;
import example.person.uploader.GoogleBucketUploader;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@AllArgsConstructor
public class ScheduledSave {


    private final CsvFileService csvFileService;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void pintTask(){

        String filePath = csvFileService.createFile();

        String bucketName = "your-bucket-name";
        String objectName = getFileName(filePath);
        GoogleBucketUploader.uploadFile(bucketName, filePath, objectName);

        /*mvn clean compile
        mvn exec:java -Dexec.mainClass=GoogleBucketUploader*/
    }

    private String getFileName(String filePath) {
        // Convert to Path object
        Path path = Paths.get(filePath);
        // Extract the file name (last part of the path)
        return path.getFileName().toString();
    }
}
