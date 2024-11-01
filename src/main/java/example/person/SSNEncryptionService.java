package example.person;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class SSNEncryptionService {
    private final AES256TextEncryptor textEncryptor;

    public SSNEncryptionService(@Value("${jasypt.encryptor.password}") String encryptionKey) {
        this.textEncryptor = new AES256TextEncryptor();
        this.textEncryptor.setPassword(encryptionKey);
    }

    public byte[] encrypt(String ssn) {
        String encryptedText = textEncryptor.encrypt(ssn);
        return encryptedText.getBytes(StandardCharsets.UTF_8);
    }

    public String decrypt(byte[] encryptedSsn) {
        String encryptedText = new String(encryptedSsn, StandardCharsets.UTF_8);
        return textEncryptor.decrypt(encryptedText);
    }
}
