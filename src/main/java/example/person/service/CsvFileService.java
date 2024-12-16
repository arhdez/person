package example.person.service;

import example.person.dto.AddressKafkaDto;
import example.person.dto.CsvPersonAddressDto;
import example.person.dto.PersonAddressDto;
import example.person.dto.PersonDto;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CsvFileService {

    private final PersonAddressService personAddressService;
    private final PersonService personService;
    private final AddressService addressService;

    public void createFile(){
        List<PersonAddressDto> personAddresses = personAddressService.findAll();
        List<CsvPersonAddressDto> personAddressList = createCsvPersonAddressDto(personAddresses);

        createdPath();
        String formattedTime = timeFileName();
        // Generate CSV file
        String filePath = "src\\main\\resources\\csv\\person_address"+formattedTime+".csv";
        writeCsvFile(filePath, personAddressList);

        System.out.println("CSV file created at: " + filePath);
    }

    private static void createdPath() {
        Path directoryPath = Paths.get("src\\main\\resources\\csv");
        try {
            // Check if the directory exists
            if (!Files.exists(directoryPath)) {
                // Create the directory
                Files.createDirectories(directoryPath);
                System.out.println("Directory created successfully: " + directoryPath.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    private static String timeFileName() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
        return currentTime.format(formatter);
    }

    private void writeCsvFile(String filePath, List<CsvPersonAddressDto> personAddressList) {
        try (CSVPrinter csvPrinter = new CSVPrinter(
                new FileWriter(filePath),
                CSVFormat.DEFAULT.withHeader("Person ID", "Address ID", "First Name", "Last Name", "Email", "Date of Birth", "Address"))) {

            // Use stream to simplify iteration
            personAddressList.forEach(dto -> {
                try {
                    csvPrinter.printRecord(
                            dto.getPersonId(),
                            dto.getAddressId(),
                            dto.getFirstName(),
                            dto.getLastName(),
                            dto.getEmail(),
                            dto.getDateOfBirth(),
                            dto.getFullAddress()
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Error writing record: " + dto, e);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file: " + filePath, e);
        }
    }

    private List<CsvPersonAddressDto> createCsvPersonAddressDto(List<PersonAddressDto> personAddresses) {
        return personAddresses.stream().map(personAddress -> {
            PersonDto personDto = personService.findById(personAddress.getPersonId()).orElseThrow(() ->
                    new IllegalArgumentException("Person not found with ID: " + personAddress.getPersonId()));
            AddressKafkaDto addressDto = addressService.findById(personAddress.getAddressId()).orElseThrow(() ->
                    new IllegalArgumentException("Address not found with ID: " + personAddress.getAddressId()));

            // Build CsvPersonAddressDto using constructor or setters
            return new CsvPersonAddressDto(
                    personDto.getId(),
                    addressDto.getAddressId(),
                    personDto.getFirstName(),
                    personDto.getLastName(),
                    personDto.getEmail(),
                    personDto.getDateOfBirth(),
                    String.join(", ", addressDto.getStreet(), addressDto.getCity(), addressDto.getState(), addressDto.getZipCode())
            );
        }).collect(Collectors.toList());
    }

}
