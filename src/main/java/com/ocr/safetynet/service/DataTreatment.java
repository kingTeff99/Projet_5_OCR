package com.ocr.safetynet.service;


import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.model.MedicalRecord;
import com.ocr.safetynet.model.Person;

@org.springframework.stereotype.Service
public class DataTreatment {

	private final Logger logger = LoggerFactory.getLogger(DataTreatment.class);

	private JSONObject jsonObj;

	private List<Person> persons = new ArrayList<>();
	
	private List<FireStation> firestations = new ArrayList<>();
	
	private List<MedicalRecord> medicalrecords = new ArrayList<>();


	public void setPersons(List<Person> persons) {
		
		this.persons = persons;
		
	}

	public List<FireStation> getFireStations() {
		
		return firestations;
		
	}

	public void setFireStations(List<FireStation> firestations) {
		
		this.firestations = firestations;
		
	}

	public void setMedicalRecords(List<MedicalRecord> medicalrecords) {
		
		this.medicalrecords = medicalrecords;
		
	}

	@PostConstruct
	public void init() throws Exception {
		
		parsing();
		
	}

	public DataTreatment() throws Exception {
		
		jsonObj = loadFile();
		
	}

	public List<Person> getPersons() {
		
		return persons;
		
	}
	
	public List<MedicalRecord> getMedicalRecords() {
		
		return medicalrecords;
		
	}
	
	public void parseJsonToPersonObject() {
		
		JSONArray arrayPerson = (JSONArray) jsonObj.get("persons");
		
		for(int i = 0; i < arrayPerson.size(); i++) {
			
			JSONObject jsonObj = (JSONObject)arrayPerson.get(i);
			
			persons.add(new Person((String )jsonObj.get("firstName"), (String )jsonObj.get("lastName"),
					(String )jsonObj.get("address"), (String )jsonObj.get("city"),
					(String )jsonObj.get("zip"), (String )jsonObj.get("phone")
					, (String )jsonObj.get("email")));
		}
	}

	public void parseJsonToFireStationObject() {
		
		JSONArray arrayFireStation = (JSONArray) jsonObj.get("firestations");
		
		for(int i = 0;i < arrayFireStation.size(); i++) {	
			
			JSONObject jsonObj = (JSONObject)arrayFireStation.get(i);
			
			firestations.add(new FireStation((String )jsonObj.get("address"), (String )jsonObj.get("station")));
			
			String address = (String) jsonObj.get("address");
			
			for(int j = 0;j < persons.size(); j++) {
				
				if(address.equals(persons.get(j).getAddress())) {
					
					firestations.get(i)
					.addPerson(new Person(persons.get(j).getFirstName(),persons.get(j).getLastName()
							,persons.get(j).getAddress(),persons.get(j).getPhone(),persons.get(j).getBirthdate()));
				}
			}
		}	
	}

	public void parseJsonToMedicalRecordObject() throws ParseException {
		
		JSONArray arrayMedicalRecord = (JSONArray) jsonObj.get("medicalrecords");
		
		for(int i = 0; i < arrayMedicalRecord.size(); i++) {	
			
			JSONObject jsonObj = (JSONObject)arrayMedicalRecord.get(i);
			
			medicalrecords.add(new MedicalRecord((String)jsonObj.get("firstName"), (String)jsonObj.get("lastName")
					,(String)jsonObj.get("birthdate")));

			persons.stream()
					.filter(p -> p.getFirstName().equals((String )jsonObj.get("firstName")) 
							&& p.getLastName().equals((String )jsonObj.get("lastName")))
					.findAny().get().setBirthdate((String )jsonObj.get("birthdate"));
			
			JSONArray arrays = (JSONArray) jsonObj.get("medications");
			
			for(int j = 0; j < arrays.size(); j++) {
				
				medicalrecords.get(i).addMedications((String)arrays.get(j));
				
			}
			
			JSONArray arrays2 = (JSONArray) jsonObj.get("allergies");
			
			for(int j = 0; j < arrays2.size(); j++) {
				
				medicalrecords.get(i).addAllergies((String)arrays2.get(j));
				
			}
		}		
		
	}

	public void parsing() throws ParseException {
		
		parseJsonToPersonObject();
		
		parseJsonToMedicalRecordObject();
		
		parseJsonToFireStationObject();

	}

	public JSONObject loadFile() throws Exception {
		
		String filepath = "src/data.json";
		
		JSONParser parser = new JSONParser();
		
		try {
			
			Object obj = parser.parse(new FileReader(filepath));
			
			logger.info("Importation of JSON File was made successfully");
			
			return (JSONObject)obj;
			
		} catch (IOException e) {
			
			logger.error("Error in importation");
		}
		return null;
	}


}
