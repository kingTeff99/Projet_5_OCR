package com.ocr.safetynet.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.model.MedicalRecord;
import com.ocr.safetynet.model.Person;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class FirestationServiceTest {

    @Mock
    DataTreatment dataTreatment;

    @InjectMocks
    FireStationService firestationService;
    @Mock
    MedicalRecordService medicalrecordService;
    @Mock
    PersonService personService;

    @Test
    public void getFireStationsReturnFirestationsTest(){
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	FireStation firestation2 = new FireStation("avenue de Marseille","5");
    	
        List<FireStation> ourListOfFirestations = new ArrayList<>();
        
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);

        when(dataTreatment.getFireStations()).thenReturn(ourListOfFirestations);

        //WHEN
        List<FireStation> result = firestationService.getFirestations();

        //THEN
        assertTrue(result.size() == 2);
        assertTrue(result.get(0).getAddress().equals("avenue de Paris"));
        assertTrue(result.get(1).getAddress().equals("avenue de Marseille"));
    }

    @Test
    public void getFireStationByAddressTest(){
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	FireStation firestation2 = new FireStation("avenue de Marseille","7");
    	
        List<FireStation> ourList = new ArrayList<>();
        
        ourList.add(firestation1);
        ourList.add(firestation2);

        when(dataTreatment.getFireStations()).thenReturn(ourList);

        //WHEN
        FireStation result = firestationService.getAllFireStation("avenue de Marseille");

        //THEN
        assertTrue(result.getAddress().equals("avenue de Marseille"));
        assertTrue(result.getStation().equals("7"));
    }

    @Test
    public void getFirestationByFirestationNumberTest(){
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	FireStation firestation2 = new FireStation("avenue de Marseille","7");
        List<FireStation> ourList = new ArrayList<>();
        ourList.add(firestation1);
        ourList.add(firestation2);

        when(dataTreatment.getFireStations()).thenReturn(ourList);

        //WHEN
        FireStation result = firestationService.getFireStationByNumber("2");

        //THEN
        assertTrue(result.getAddress().equals("avenue de Paris"));
        assertTrue(result.getStation().equals("2"));
    }

    @Test
    public void saveFirestationHaveToReturnFirestationTest(){
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	FireStation firestation2 = new FireStation("avenue de Marseille","5");
    	FireStation firestation3 = new FireStation("avenue de Brest","8");
    	
        List<FireStation> ourListOfFirestations = new ArrayList<>();
        
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);

        when(dataTreatment.getFireStations()).thenReturn(ourListOfFirestations);
        assertTrue(ourListOfFirestations.size()==2);

        //WHEN
        FireStation result = firestationService.saveFireStation(firestation3);

        //THEN
        assertTrue(ourListOfFirestations.size()==3);
        assertTrue(result.getStation().equals("8"));
        assertTrue(result.getAddress().equals("avenue de Brest"));
    }

    @Test
    public void deleteFireStationTest(){
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	FireStation firestation2 = new FireStation("avenue de Marseille","5");
    	FireStation firestation3 = new FireStation("avenue de Brest","8");
    	
        List<FireStation> ourListOfFirestations = new ArrayList<>();
        
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);
        ourListOfFirestations.add(firestation3);

        when(dataTreatment.getFireStations()).thenReturn(ourListOfFirestations);
        assertTrue(ourListOfFirestations.size()==3);

        //WHEN
        firestationService.deleteStation("avenue de Brest","8");

        //THEN
        assertTrue(ourListOfFirestations.size() == 2);
    }

    @Test
    public void sortPersonByAddressTest() throws JsonProcessingException {
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	
        Person person1 = new Person("Bob","Bobby","avenue de Paris","Paris"
                ,"75000","0123456789","bob@mail.com");
        
        Person person2 = new Person("Jack","Jacky","avenue de Paris","Paris"
                ,"75000","0123456788","jacky@mail.com");
        
        person1.setBirthdate("01/10/1999");
        person2.setBirthdate("01/10/1999");

        when(dataTreatment.getFireStations()).thenReturn(Arrays.asList(firestation1));
        when(dataTreatment.getPersons()).thenReturn(Arrays.asList(person1, person2));
        when(personService.ageCalculation(anyString())).thenReturn(20);
        when(medicalrecordService.getMedicalRecordByName(anyString(),anyString())).thenReturn(new MedicalRecord());
        when(medicalrecordService.getMedicalRecordByName(anyString(),anyString())).thenReturn(new MedicalRecord());

        JSONObject json = new JSONObject();
        json.put("persons",Arrays.asList(person1, person2));
        json.put("stations",firestation1.getStation());

        //WHEN
        JSONObject result = firestationService.sortPersonByAddress("avenue de Paris");

        //THEN
       assertTrue(result.toString().equals(json.toString()));
    }

    @Test
    public void testSortPersonByStation(){
    	
    	FireStation firestation1 = new FireStation("avenue de Paris", "2");
    	
        Person person1 = new Person("Bob","Bobby","avenue de Paris","Paris"
                ,"75000","0123456789","bob@mail.com");
        
        Person person2 = new Person("Jack","Jacky","avenue de Paris","Paris"
                ,"75000","0123456788","jacky@mail.com");
        
        person1.setBirthdate("01/10/1999");
        
        person2.setBirthdate("01/10/2015");

        when(dataTreatment.getFireStations()).thenReturn(Arrays.asList(firestation1));
        when(dataTreatment.getPersons()).thenReturn(Arrays.asList(person1, person2));
        when(personService.ageCalculation("01/10/1999")).thenReturn(21);
        when(personService.ageCalculation("01/10/2015")).thenReturn(5);

        JSONObject json = new JSONObject();
        json.put("persons",Arrays.asList(person1, person2));
        json.put("childrens",1);
        json.put("adults",1);

        //WHEN
        JSONObject result = firestationService.getPersonsByStation("2");

        //THEN
        assertTrue(result.toString().equals(json.toString()));
    }

    @Test
    public void testSortPersonByListOfStations(){
    	
        //GIVEN
    	FireStation firestation1 = new FireStation("avenue de Paris","2");
    	
    	FireStation firestation2 = new FireStation("avenue de Marseille","5");
    	
    	FireStation firestation3 = new FireStation("avenue de Brest","8");
    	
        Person person1 = new Person("Bob","Bobby","avenue de Paris","Paris"
                ,"75000","0123456789","bob@mail.com");
        
        Person person2 = new Person("Jack","Jacky","avenue de Paris","Paris"
                ,"75000","0123456788","jacky@mail.com");
        
        Person person3 = new Person("Bob","Bobby","avenue de Marseille","Paris"
                ,"75000","0123456789","bob@mail.com");
        
        Person person4 = new Person("Jack","Jacky","avenue de Brest","Paris"
                ,"75000","0123456788","jacky@mail.com");
        
        Person person5 = new Person("Bob","Bobby","avenue de Brest","Paris"
                ,"75000","0123456789","bob@mail.com");

        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();

        firestation1.setPersonToAdd(Arrays.asList(person1, person2));
        firestation2.setPersonToAdd(Arrays.asList(person3));
        firestation3.setPersonToAdd(Arrays.asList(person4, person5));

        when(dataTreatment.getFireStations()).thenReturn(Arrays.asList(firestation1, firestation2,firestation3));
        
        when(medicalrecordService.getMedicalRecordByName(anyString(),anyString())).thenReturn(new MedicalRecord());
        
        when(medicalrecordService.getMedicalRecordByName(anyString(),anyString())).thenReturn(new MedicalRecord());
        
        json.put("avenue de Paris",Arrays.asList(person1, person2));
        json2.put("2",json);
        json.put("avenue de Brest",Arrays.asList(person4, person5));
        json2.put("8",json);

        //WHEN
        JSONObject result = firestationService.getPersonByListOfStations(Arrays.asList("2", "8"));

        //THEN
        assertTrue(result.toString().equals(json2.toString()));

    }

}
