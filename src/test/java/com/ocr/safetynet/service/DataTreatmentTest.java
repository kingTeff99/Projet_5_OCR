package com.ocr.safetynet.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.model.MedicalRecord;
import com.ocr.safetynet.model.Person;


public class DataTreatmentTest {

    private DataTreatment dataTreatment;

    @Test
    public void  parseJsonToPersonObjectTest() throws Exception {
    	
        //GIVEN
    	dataTreatment = new DataTreatment();
    	
        Person person1 = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        Person person2 = new Person("Jacob","Boyd","1509 Culver St","Culver","97451","841-874-6513","drk@email.com");

        //WHEN
        dataTreatment.parseJsonToPersonObject();

        //THEN
        assertTrue(dataTreatment.getPersons().get(0).getFirstName().equals(person1.getFirstName()));
        assertTrue(dataTreatment.getPersons().get(1).getFirstName().equals(person2.getFirstName()));
    }

    @Test
    public void parseJsonToFirestationObjectTest() throws Exception {
    	
        //GIVEN
    	dataTreatment = new DataTreatment();
        FireStation firestation1 = new FireStation("1509 Culver St","3");
        FireStation firestation2 = new FireStation("29 15th St","2");

        //WHEN
        dataTreatment.parseJsonToFireStationObject();

        //THEN
        assertTrue(dataTreatment.getFireStations().get(0).getAddress().equals(firestation1.getAddress()));
        assertTrue(dataTreatment.getFireStations().get(1).getAddress().equals(firestation2.getAddress()));
        assertTrue(dataTreatment.getFireStations().get(0).getStation().equals(firestation1.getStation()));
        assertTrue(dataTreatment.getFireStations().get(1).getStation().equals(firestation2.getStation()));

    }

    @Test
    public void parseJsonToMedicalrecordObjectTest() throws Exception {
    	
        //GIVEN
    	dataTreatment = new DataTreatment();
    	
        List<String> medications1 = new ArrayList<>();
        medications1.add("aznol:350mg");
        medications1.add("hydrapermazol:100mg");
        
        List<String> medications2 = new ArrayList<>();
        medications2.add("pharmacol:5000mg");
        medications2.add("terazine:10mg");
        medications2.add("noznazol:250mg");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("nillacilan");
        List<String> allergies2= new ArrayList<>();


        MedicalRecord medicalrecord1 = new MedicalRecord("John", "Boyd", "03/06/1984", medications1, allergies1);
        MedicalRecord medicalrecord2 = new MedicalRecord("Jacob", "Boyd", "03/06/1989", medications2, allergies2);

        //WHEN
        dataTreatment.parseJsonToPersonObject();
        dataTreatment.parseJsonToMedicalRecordObject();

        //THEN
        assertTrue(dataTreatment.getMedicalRecords().get(0).getFirstName().equals(medicalrecord1.getFirstName()));
        
    }

    @Test
    public void parsingTest() throws Exception {
    	
        //GIVEN
    	dataTreatment = new DataTreatment();
    	
        Person person1 = new Person("John","Boyd","1509 Culver St","Culver","97451","841-874-6512","jaboyd@email.com");
        
        FireStation firestation1 = new FireStation("1509 Culver St","3");

        List<String> medications1 = new ArrayList<>();
        medications1.add("aznol:350mg");
        medications1.add("hydrapermazol:100mg");
        
        List<String> allergies1 = new ArrayList<>();
        allergies1.add("nillacilan");
        
        MedicalRecord medicalrecord1 = new MedicalRecord("John","Boyd","03/06/1984",medications1,allergies1);

        //WHEN
        dataTreatment.parsing();

        //THEN
        assertTrue(dataTreatment.getPersons().get(0).getFirstName().equals(person1.getFirstName()));
        assertTrue(dataTreatment.getFireStations().get(0).getStation().equals(firestation1.getStation()));
        assertTrue(dataTreatment.getMedicalRecords().get(0).getBirthdate().equals(medicalrecord1.getBirthdate()));
        
    }

}
