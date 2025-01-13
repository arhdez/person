package example.person.service.uploader;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class GoogleBucketService {
    private final Storage storage;
    private static final String BUCKET_NAME = "personaddressapi";


    public void uploadFile(@NonNull File file, String objectName) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("text/csv")
                    .build();
            storage.create(blobInfo, fileBytes);
            System.out.println("File uploaded to bucket: " + BUCKET_NAME + "/" + objectName);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to bucket", e);
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
