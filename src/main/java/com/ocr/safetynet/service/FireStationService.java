package com.ocr.safetynet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.model.Person;

@Service
public class FireStationService {

    @Autowired
    private DataTreatment dataTreatment;
    
    @Autowired
    PersonService personService;
    
    @Autowired
    MedicalRecordService medicalRecordService;

    public List<FireStation> getFirestations(){
    	
        return dataTreatment.getFireStations();
        
    }

    public FireStation getAllFireStation(String address) {
    	
        for (FireStation firestation : dataTreatment.getFireStations()) {
        	
            if (firestation.getAddress().equals(address)) {
            	
                return firestation;

            }
        }
        return null;
    }

    public FireStation getFireStationByNumber(String stationNumber) {
    	
        for (FireStation firestation : dataTreatment.getFireStations()) {
        	
            if (firestation.getStation().equals(stationNumber)) {
            	
                return firestation;

            }
        }
        return null;
    }

    public FireStation saveFireStation(FireStation firestation) {
    	
    	dataTreatment.getFireStations().add(firestation);
    	
        return firestation;
    }

    public void deleteStation(String address, String station) {
    	
    	dataTreatment.getFireStations().removeIf(firestation -> (firestation.getAddress().equals(address))
                && (firestation.getStation().equals(station)));
    }

    public JSONObject sortPersonByAddress(String address){
    	
        FireStation ourFirestation = getAllFireStation(address);
        List<Person> persons =  dataTreatment.getPersons()
                .stream()
                .filter(c -> c.getAddress().equals(address))
                .collect(Collectors.toList());

        for (Person person : dataTreatment.getPersons()) {
        	
            person.setAge(personService.ageCalculation(person.getBirthdate()));
            
            person.setAllergies(medicalRecordService.getMedicalRecordByName(person.getFirstName(),person.getLastName()).getAllergies());
            
            person.setMedications(medicalRecordService.getMedicalRecordByName(person.getFirstName(),person.getLastName()).getMedications());
            
        }
        
        JSONObject json = new JSONObject();
        
        json.put("persons",persons);
        
        json.put("stations",ourFirestation.getStation());

        return json;
    }
    
    public JSONObject sortPersonByStation(String stationNumber){
    	
        int numberOfChildren = 0;
        
        int numberOfAdults = 0;
        
        FireStation ourFirestation = getFireStationByNumber(stationNumber);
        
        List<Person> persons = dataTreatment.getPersons()
                .stream()
                .filter(fire -> fire.getAddress().equals(ourFirestation.getAddress()))
                .collect(Collectors.toList());
        
        for (Person person : persons) {
        	
            person.setAge(personService.ageCalculation(person.getBirthdate()));
            
        }
        
        for (Person person : persons) {
        	
            if(person.getAge()>=18) {
            	
                numberOfAdults++;

            } else {
            	
                numberOfChildren++;

            }
        }
        
        JSONObject json = new JSONObject();
        
        json.put("persons", persons);
        
        json.put("childrens", numberOfChildren);
        
        json.put("adults", numberOfAdults);

        return json;
    }

    public JSONObject sortPersonByListOfStations(List<String> listOfStations){
    	
        FireStation ourFirestation;
        
        JSONObject json = new JSONObject();
        
        JSONObject json2 = new JSONObject();
        
        for(int i = 0;i < listOfStations.size();i++){
        	
            ourFirestation = getFireStationByNumber(listOfStations.get(i));
            
            for (Person person : ourFirestation.getPersonToAdd()) {
            	
                person.setAge(personService.ageCalculation(person.getBirthdate()));
                
                person.setAllergies(medicalRecordService
                		.getMedicalRecordByName(person.getFirstName(),person.getLastName()).getAllergies());
                
                person.setMedications(medicalRecordService
                		.getMedicalRecordByName(person.getFirstName(),person.getLastName()).getMedications());
                
            }
            json.put(ourFirestation.getAddress(),ourFirestation.getPersonToAdd());
            
            json2.put(ourFirestation.getStation(),json);
        }
        
        return json2;
    }

    public MappingJacksonValue filter(String[] tab,JSONObject json){
    	
        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept(tab);
        
        FilterProvider listFilter = new SimpleFilterProvider().addFilter("mySpecificFilter", myFilter);
        
        MappingJacksonValue personFilters = new MappingJacksonValue(json);
        
        personFilters.setFilters(listFilter);
        
        return personFilters;
    }

}
