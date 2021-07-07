package com.ocr.safetynet.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.model.Person;
import com.ocr.safetynet.service.FireStationService;
import com.ocr.safetynet.service.MedicalRecordService;
import com.ocr.safetynet.service.PersonService;

@RestController
public class SafetyController {

	private final Logger logger = LoggerFactory.getLogger(PersonEndpoint.class);

	@Autowired
	private FireStationService firestationService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private MedicalRecordService medicalrecordService;


	
	@GetMapping("/personInfo")
	public MappingJacksonValue displayPeople(@RequestParam(name="firstName", required = true)String firstName
			, @RequestParam(name="lastName", required = true)String lastName) throws Exception {


		List<Person> ourPersonList = new ArrayList<>();
		
		for(int i = 0; i < personService.getPersons().size(); i++) {
			
			if( personService.getPersons().get(i).getLastName().equals(lastName) )
				ourPersonList.add(personService.getPersons().get(i));
			
		}
		
		for (Person person : ourPersonList) {
			
			person.setAllergies(medicalrecordService
					.getMedicalRecordByName(person.getFirstName(),person.getLastName()).getAllergies());
			
			person.setMedications(medicalrecordService
					.getMedicalRecordByName(person.getFirstName(),person.getLastName()).getMedications());
			
		}

		SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.serializeAllExcept("firstName", "city", "zip", "phone", "birthdate");

		FilterProvider listFilters = new SimpleFilterProvider().addFilter("mySpecificFilter", myFilter);
		
		MappingJacksonValue personsFilters = new MappingJacksonValue(ourPersonList);
		
		personsFilters.setFilters(listFilters);

		return personsFilters;
	}


	@GetMapping("/firestation")
	public MappingJacksonValue displayPeopleByFireStationNumber(@RequestParam(name= "stationNumber", required = true)String number) throws Exception {
		
		String[] tab = {"city", "zip", "email", "birthdate", "medications", "allergies"};
		
		return firestationService.filter(tab, firestationService.sortPersonByStation(number));
	}

	
	@GetMapping("/childAlert")
	public MappingJacksonValue displayKids(@RequestParam(name= "address", required = true)String address) {
		
		String[] tab = {"address", "city", "zip", "email", "phone", "birthdate", "medications", "allergies"};
		
		return personService.filter(tab, personService.sortChildrenAndAdultByAddress(address));
	}

	@GetMapping("/flood/stations")
	public MappingJacksonValue displayPeopleByStationNumber(@RequestParam(name="stations", required = true)List<String> listOfStations) {
		
		String[] tab = {"address","city","zip","email","birthdate"};
		
		return firestationService.filter(tab,firestationService.sortPersonByListOfStations(listOfStations));
	}

	@GetMapping("/fire")
	public MappingJacksonValue displayPeopleByAddress(@RequestParam(name="address", required = true)String address) {
		
		String[] tab = {"firstName","address","city","zip","email","birthdate"};
		
		return firestationService.filter(tab,firestationService.sortPersonByAddress(address));
	}
	
	@GetMapping("/phoneAlert")
	public List<String> displayNumberByFirestation(@RequestParam(name="firestation", required = true)String firestation) {
		
		List<String> phoneNumber = new ArrayList<>();

		FireStation fireStation = firestationService.getFireStationByNumber(firestation);

		List<Person> localPerson = personService.getPersons()
				.stream()
				.filter(c -> c.getAddress().equals(fireStation.getAddress()))
				.collect(Collectors.toList());

		for (Person person : localPerson) {
			
			phoneNumber.add(person.getPhone());
			
		}
		return phoneNumber;
	}


	@GetMapping("/communityEmail")
	public List<String> getEmailOfCity(@RequestParam(name="city", required = true)String city) {
		
		List<String> emailCommunity = new ArrayList<>();
		
		List<Person> personFromCity = personService.getPersons()
				.stream()
				.filter(c -> c.getCity().equals(city))
				.collect(Collectors.toList());

		for (Person person : personFromCity) {
			
			emailCommunity.add(person.getEmail());
			
		}
		return emailCommunity;
	}

	
}