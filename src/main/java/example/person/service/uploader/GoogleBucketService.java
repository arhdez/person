package example.person.service.uploader;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class GoogleBucketService {
    private final Storage storage;
    private static final String BUCKET_NAME = "personaddressapi";


    public void uploadFile(String filePath, String objectName) {
        try {

            // Get the file as a byte array
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);

            // Define the blob ID (bucket name and object name)
            BlobId blobId = BlobId.of(BUCKET_NAME, objectName);

            // Create a blob with metadata
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("text/csv") // Adjust content type based on your file
                    .build();

            // Upload the file
            storage.create(blobInfo, fileBytes);

            // Print success message
            System.out.println("File uploaded to bucket: " + BUCKET_NAME + "/" + objectName);
        } catch (Exception e) {
            System.err.println("Error uploading file to bucket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public byte[] getFile(String objectName) {
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, objectName));
        if (blob == null){
            return new byte[0];
        }
            return blob.getContent();
    }


}
