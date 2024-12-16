package example.person.uploader;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GoogleBucketUploader {

    public static void uploadFile(String bucketName, String filePath, String objectName){
        try {
            // Instantiate the Storage client
            Storage storage = StorageOptions.getDefaultInstance().getService();

            // Get the file as a byte array
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);

            // Define the blob ID (bucket name and object name)
            BlobId blobId = BlobId.of(bucketName, objectName);

            // Create a blob with metadata
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("text/csv")
                    .build();

            //Upload the file
            storage.create(blobInfo, fileBytes);
            //Permissions
            //storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            System.out.println("File uploaded to bucket: " + bucketName + "/" + objectName);
        } catch (Exception e){
            System.err.println("Error uploading file to bucket: " + e.getMessage());
            e.printStackTrace();
        }
    }
}