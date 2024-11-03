package example.person.service;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SSNEncryptionService {
    private final AES256TextEncryptor textEncryptor;

    public SSNEncryptionService(@Value("${jasypt.encryptor.password}") String encryptionKey) {
        this.textEncryptor = new AES256TextEncryptor();
        this.textEncryptor.setPassword(encryptionKey);
    }

    public byte[] encrypt(String ssn) {
        // Encrypt the SSN
        String encryptedSsn = textEncryptor.encrypt(ssn);

        // Convert the encrypted string to bytes using Base64 encoding
        return Base64.getEncoder().encode(encryptedSsn.getBytes());
    }

    public String decrypt(byte[] encryptedSsnBytes) {
        // Convert bytes back to Base64 string
        String encryptedSsn = new String(Base64.getDecoder().decode(encryptedSsnBytes));

        // Decrypt the SSN
        return textEncryptor.decrypt(encryptedSsn);
    }
}
