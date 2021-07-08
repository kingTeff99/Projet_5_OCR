package com.ocr.safetynet.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ocr.safetynet.model.MedicalRecord;
import com.ocr.safetynet.service.MedicalRecordService;

@RestController
public class MedicalRecordEndpoint {

    private final Logger logger = LoggerFactory.getLogger(MedicalRecordEndpoint.class);

    @Autowired
    MedicalRecordService medicalRecordService;
    
    /**
     * GET MedicalRecord
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/medicalRecord")
    public MappingJacksonValue getMedicalRecord() throws Exception {
    	
        logger.info("HTTP GET request received at /medicalRecord URL");

        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept("");
        
        FilterProvider listFilters = new SimpleFilterProvider().addFilter("mySpecificFilter", myFilter);
        
        MappingJacksonValue medicalFiltres = new MappingJacksonValue( medicalRecordService.getMedicalRecords());
        
        medicalFiltres.setFilters(listFilters);

        return medicalFiltres;
    }


    /**
     * UPDATE Medical Record
     * @param medicalRecord
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/medicalRecord")
    public MappingJacksonValue updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws Exception {
        logger.info("HTTP PUT request received at /medicalRecord URL");

        MedicalRecord existingMedicalRecord = medicalRecordService
        		.getMedicalRecordByName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (existingMedicalRecord!=null) {
            existingMedicalRecord.setFirstName(medicalRecord.getFirstName());
            existingMedicalRecord.setLastName(medicalRecord.getLastName());
            existingMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            existingMedicalRecord.setMedications(medicalRecord.getMedications());
            existingMedicalRecord.setAllergies(medicalRecord.getAllergies());
        }
        return getMedicalRecord();
    }
    
    /**
     * SAVE Medical Record
     * @param medicalRecord
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/medicalRecord")
    public ResponseEntity<Void> saveMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws Exception {
    	
        logger.info("HTTP POST request received at /medicalRecord URL");
        
        MedicalRecord addedMedicalRecord = medicalRecordService.saveMedicalRecord(medicalRecord);

        if(addedMedicalRecord == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/personInfo")
                .queryParam("firstName",addedMedicalRecord.getFirstName())
                .queryParam("lastName",addedMedicalRecord.getLastName())
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * DELETE Medical Record
     * @param medicalRecord
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/medicalRecord")
    public MappingJacksonValue deleteMedicalrecord(@RequestBody MedicalRecord medicalRecord) throws Exception {
    	
        logger.info("HTTP DELETE request received at /medicalRecord URL");
        
        medicalRecordService.deleteMedicalRecordByName(medicalRecord.getFirstName(),medicalRecord.getLastName());

        return getMedicalRecord();
    }

}
