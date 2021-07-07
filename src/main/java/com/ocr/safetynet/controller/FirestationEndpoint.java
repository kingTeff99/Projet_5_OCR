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
import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.service.FireStationService;

@RestController
public class FirestationEndpoint {

    private final Logger logger = LoggerFactory.getLogger(FirestationEndpoint.class);

    @Autowired
    FireStationService firestationService;

    @GetMapping(value = "/firestations")
    public MappingJacksonValue displayFireStation() throws Exception {
    	
        logger.info("HTTP GET request received at /firestations URL");
        
        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept("personToSave");
        
        FilterProvider listFilters = new SimpleFilterProvider().addFilter("mySpecificFilter", myFilter);
        
        MappingJacksonValue firestationFilters = new MappingJacksonValue( firestationService.getFirestations());
        
        firestationFilters.setFilters(listFilters);

        return firestationFilters;
    }



    @PutMapping(value = "/firestation")
    public MappingJacksonValue updateFirestation(@RequestBody FireStation fireStation) throws Exception {
    	
        logger.info("HTTP PUT request received at /firestation URL");
        
        FireStation existingFirestation = firestationService.getAllFireStation(fireStation.getAddress());

        if (existingFirestation!=null) {
        	
            existingFirestation.setAddress(fireStation.getAddress());
            
            existingFirestation.setStation(fireStation.getStation());
        }
        return displayFireStation();
    }

    @PostMapping(value = "/firestation")
    public ResponseEntity<Void> saveFirestation(@RequestBody FireStation firestation) throws Exception {
    	
        logger.info("HTTP POST request received at /firestation URL");
        
        FireStation addedFirestation = firestationService.saveFireStation(firestation);

        if(addedFirestation == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/firestation")
                .build()
                .toUri();


        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/firestation")
    public MappingJacksonValue deleteFirestation(@RequestBody FireStation firestation) throws Exception {
    	
        logger.info("HTTP DELETE request received at /firestation URL");
        
        firestationService.deleteStation(firestation.getAddress(),firestation.getStation());
        
        return displayFireStation();
    }

}
