package example.person.service;

import example.person.dto.AddressKafkaDto;
import example.person.dto.CsvPersonAddressDto;
import example.person.dto.PersonAddressDto;
import example.person.dto.PersonDto;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CsvFileService {

    private final PersonAddressService personAddressService;
    private final PersonService personService;
    private final AddressService addressService;

    public File createTempCSVFile() {
        List<PersonAddressDto> personAddresses = personAddressService.findAll();
        List<CsvPersonAddressDto> personAddressList = createCsvPersonAddressDto(personAddresses);

        File tempFile;
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        String fileName = "person_address_" + timeFileName() + ".csv";
        tempFile = new File(tempDir, fileName);

        writeCsvFile(tempFile, personAddressList);
        System.out.println("Temporary CSV file created at: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    private static String timeFileName() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return currentDate.format(formatter);
    }

    private void writeCsvFile(File file, List<CsvPersonAddressDto> personAddressList) {
        try (CSVPrinter csvPrinter = new CSVPrinter(
                new FileWriter(file),
                CSVFormat.DEFAULT.builder()
                        .setHeader("Person ID", "Address ID", "First Name", "Last Name", "Email", "Date of Birth", "Address")
                        .build())) {
            personAddressList.forEach(dto -> {
                try {
                    csvPrinter.printRecord(
                            dto.getPersonId(),
                            dto.getAddressId(),
                            dto.getFirstName(),
                            dto.getLastName(),
                            dto.getEmail(),
                            dto.getDateOfBirth()
                    );
                } catch (IOException e){
                    throw new RuntimeException("Error writing record: " + dto, e);
                }
            });
        }catch (IOException e){
            throw new RuntimeException("Error writing CSV file: " + file.getAbsolutePath(), e);
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
