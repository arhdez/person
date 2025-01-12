package example.person.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BucketConfig {

    @Bean
    public Storage buildStorage() throws IOException {

        // Load credentials from the JSON key file
//        String credentialsPath = "src\\main\\java\\example\\person\\config\\turnkey-env-446305-i2-12cbf1c2f905.json"; // Update this path
        String credentialsPath = "/turnkey-env-446305-i2-12cbf1c2f905.json"; // Update this path

        InputStream inputStream = BucketConfig.class.getResourceAsStream(credentialsPath);

        if (inputStream == null) {
            throw new IOException("Credentials file not found");
        }
        // Instantiate the Storage client
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(inputStream))
                .build()
                .getService();
    }
}
