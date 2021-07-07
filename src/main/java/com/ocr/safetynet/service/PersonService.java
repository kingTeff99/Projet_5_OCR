package com.ocr.safetynet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ocr.safetynet.model.Person;

@Service
public class PersonService {

    @Autowired
    private DataTreatment dataTreatment ;

    public List<Person> getPersons(){
    	
        return dataTreatment.getPersons();
        
    }

    public Person save(Person person) {
    	
    	dataTreatment.getPersons().add(person);
        
        return person;
        
    }

    public void deleteByName(String firstName,String lastName){
    	
    	dataTreatment.getPersons().removeIf(person -> (person.getFirstName().equals(firstName))
                && (person.getLastName().equals(lastName)));
        
    }

    public Person findPersonByName(String firstName,String lastName){
    	
        for (Person person : dataTreatment.getPersons()) {
        	
            if(person.getFirstName().equals(firstName) && person.getLastName().contentEquals(lastName))
                return person;
        }
        return null;
    }

    public int ageCalculation(String birthDate){
    	
    	LocalDate today = LocalDate.now();
		
		try {
			
		DateTimeFormatter formatter = DateTimeFormatter
				      .ofPattern("MM/dd/yyyy");
				    
		LocalDate parsedBirthDate = LocalDate.parse(birthDate, formatter);
				    
		return Period.between(parsedBirthDate, today).getYears();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	    return 0;
    }

    public JSONObject sortChildrenAndAdultByAddress(String address){
    	
        List<Person> personFamily =  dataTreatment.getPersons()
                .stream()
                .filter(c -> c.getAddress().equals(address))
                .collect(Collectors.toList());

        for (Person person : personFamily) {
        	
            person.setAge(ageCalculation(person.getBirthdate()));
            
        }
        
        List<Person> childrens = personFamily
                .stream()
                .filter(c -> c.getAge() < 18)
                .collect(Collectors.toList());
        
        List<Person> adult = personFamily
                .stream()
                .filter(c -> c.getAge() >= 18)
                .collect(Collectors.toList());
        
        JSONObject json = new JSONObject();
        
        json.put("childrens", childrens);
        
        json.put("adults", adult);
        
        JSONObject json2 = new JSONObject();
        
        json2.put(address, json);

        return json2;
    }

    public MappingJacksonValue filter(String[] tab, JSONObject json){
    	
        SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept(tab);
        
        FilterProvider listFilters = new SimpleFilterProvider().addFilter("mySpecificFilter", myFilter);
        
        MappingJacksonValue personFilters = new MappingJacksonValue(json);
        
        personFilters.setFilters(listFilters);
        
        return personFilters;
    }

}
