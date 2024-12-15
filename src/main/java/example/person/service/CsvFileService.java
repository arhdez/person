package example.person.service;

import example.person.dto.AddressKafkaDto;
import example.person.dto.CsvPersonAddressDto;
import example.person.dto.PersonAddressDto;
import example.person.dto.PersonDto;
import example.person.jpa.PersonAddress;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CsvFileService {

    private final PersonAddressService personAddressService;
    private final PersonService personService;
    private final AddressService addressService;

    public void createFile(){

        List<PersonAddressDto> personAddresses = (List<PersonAddressDto>) personAddressService.findAll();
        List<CsvPersonAddressDto> personAddressList = createCsvPersonAddressDto(personAddresses);
        // Generate CSV file
        String filePath = "src\\main\\resources\\person_address.csv";
        writeCsvFile(filePath, personAddressList);

        System.out.println("CSV file created at: " + filePath);
    }

    private void writeCsvFile(String filePath, List<CsvPersonAddressDto> personAddressList) {
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Person ID", "Address ID", "First Name", "Last Name", "Email", "Date of Birth", "Address"))) {

            for (CsvPersonAddressDto dto : personAddressList) {
                csvPrinter.printRecord(
                        dto.getPersonId(),
                        dto.getAddressId(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getEmail(),
                        dto.getDateOfBirth(),
                        dto.getFullAddress()
                );
            }

            csvPrinter.flush(); // Ensure everything is written to the file
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions properly in production
        }
    }

    private List<CsvPersonAddressDto> createCsvPersonAddressDto(List<PersonAddressDto> personAddresses){
        List<CsvPersonAddressDto> resultCsvPersonAddressDto = new ArrayList<CsvPersonAddressDto>();
        PersonDto personDtoTemp;
        AddressKafkaDto addressKafkaDtoTemp;

       for (PersonAddressDto personAddress : personAddresses){
           CsvPersonAddressDto csvPersonAddressDtoTemp = new CsvPersonAddressDto();
           personDtoTemp = personService.findById(personAddress.getPersonId()).get();
           addressKafkaDtoTemp = addressService.findById(personAddress.getAddressId()).get();
           csvPersonAddressDtoTemp.setPersonId(personDtoTemp.getId());
           csvPersonAddressDtoTemp.setAddressId(addressKafkaDtoTemp.getAddressId());
           csvPersonAddressDtoTemp.setFirstName(personDtoTemp.getFirstName());
           csvPersonAddressDtoTemp.setLastName(personDtoTemp.getLastName());
           csvPersonAddressDtoTemp.setEmail(personDtoTemp.getEmail());
           csvPersonAddressDtoTemp.setDateOfBirth(personDtoTemp.getDateOfBirth());
           csvPersonAddressDtoTemp.setFullAddress(addressKafkaDtoTemp.getStreet() + addressKafkaDtoTemp.getCity() + addressKafkaDtoTemp.getState() + addressKafkaDtoTemp.getZipCode());
           resultCsvPersonAddressDto.add(csvPersonAddressDtoTemp);
       }
       return resultCsvPersonAddressDto;
    }

}
