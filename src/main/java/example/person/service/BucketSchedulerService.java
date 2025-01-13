package example.person.service;

import example.person.service.uploader.GoogleBucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BucketSchedulerService {

    private final CsvFileService csvFileService;
    private final GoogleBucketService googleBucketService;


    public void schedule() {
        File tempFile = csvFileService.createTempCSVFile();

        try (InputStream inputStream = new FileInputStream(tempFile)) {
            byte[] newCsvContent = inputStream.readAllBytes();
            String objectName = tempFile.getName();

            byte[] existingCsvContent = googleBucketService.getFile(objectName);

            if(!Arrays.equals(newCsvContent, existingCsvContent)) {
                googleBucketService.uploadFile(tempFile, objectName);
            } else {
                System.out.println("No change in CSV file. No need to upload.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Error handling file: " + tempFile.getAbsolutePath(),e);
        }finally {
            if(tempFile.exists() && !tempFile.delete()){
                System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
            }
        }
    }
}