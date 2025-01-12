package example.person.service;

import example.person.service.uploader.GoogleBucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BucketSchedulerService {

    private final CsvFileService csvFileService;
    private final GoogleBucketService googleBucketService;


    public void schedule() {
        String filePath = csvFileService.createFile();

        File f = new File(filePath);
        try (InputStream is = new FileInputStream(f)) {
            byte[] newCsv = is.readAllBytes();
            String objectName = getFileName(filePath);

            byte[] existentCsv = googleBucketService.getFile(objectName);

            if(!Arrays.equals(newCsv, existentCsv)) {
                googleBucketService.uploadFile(filePath, objectName);
            } else {
                System.out.println("No change in CSV file. No need to upload.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileName(String filePath) {
        // Convert to Path object
        Path path = Paths.get(filePath);
        // Extract the file name (last part of the path)
        return path.getFileName().toString();

    }
}