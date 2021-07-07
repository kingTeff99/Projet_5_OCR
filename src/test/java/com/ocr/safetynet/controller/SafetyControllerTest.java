package com.ocr.safetynet.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.model.MedicalRecord;
import com.ocr.safetynet.model.Person;
import com.ocr.safetynet.service.FireStationService;
import com.ocr.safetynet.service.MedicalRecordService;
import com.ocr.safetynet.service.PersonService;

@WebMvcTest(SafetyController.class)
@RunWith(SpringRunner.class)
public class SafetyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FireStationService firestationService;
    
    @MockBean
    private MedicalRecordService medicalrecordService;
    
    @MockBean
    private PersonService personService;


    @Test
    public void getPersonsIfExistTest() throws Exception {
    	
        //GIVEN
        Person person1 = new Person("John","David","avenue des champs elysees","Paris"
                ,"75000","0123456789","jdd@mail.com");
        
        Person person2 = new Person("Laurent","Patrick","avenue des champs elysees","Paris"
                ,"75000","0123456788","lpatrick@mail.com");

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");

        MedicalRecord medicalrecord1 = new MedicalRecord("Peu","importe","01/10/1997", medications1, allergies1);

        when(medicalrecordService.getMedicalRecordByName(anyString(),anyString())).thenReturn(medicalrecord1);
        
        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/personInfo")
                .contentType(APPLICATION_JSON)
                .param("firstName","Laurent")
                .param("lastName","Patrick"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName",is("Patrick")))
                .andExpect(jsonPath("$[0].address",is("avenue des champs elysees")))
                .andExpect(jsonPath("$[0].email",is("lpatrick@mail.com")));
    }

    @Test
    public void getPersonsIfNotExistTest() throws Exception {
    	
        //GIVEN
    	Person person1 = new Person("John","David","avenue des champs elysees","Paris"
                ,"75000","0123456789","jdd@mail.com");
        
        Person person2 = new Person("Laurent","Patrick","avenue des champs elysees","Paris"
                ,"75000","0123456788","lpatrick@mail.com");

        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/personInfo")
                .contentType(APPLICATION_JSON)
                .param("firstName","Inconnu")
                .param("lastName","Inconnu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    public void getStationNumberByFireStationTest() throws Exception {
    	
    	Person person1 = new Person("John","David","avenue des champs elysees","Paris"
                ,"75001","418996352","jdd@mail.com");
        
        Person person2 = new Person("Laurent","Patrick","avenue des champs elysees","Paris"
                ,"75001","418438253","lpatrick@mail.com");
        
        Person person3 = new Person("Patrice","Lumumba","avenue limete","Kinshasa"
                ,"99000","4182615887","plumumba@mail.com");
        
        FireStation firestation = new FireStation("avenue des champs elysees","14");

        when(firestationService.getFireStationByNumber("14")).thenReturn(firestation);
        
        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2, person3));

        mockMvc.perform(get("/phoneAlert")
                .contentType(APPLICATION_JSON)
                .param("firestation","14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0]",is("418996352")))
                .andExpect(jsonPath("$[1]",is("418438253")));
    }

    @Test
    public void getEmailsByCityTest() throws Exception {
    	
    	Person person1 = new Person("John","David","avenue rocheteau","Lyon"
                ,"75001","0123456789","jdd@mail.com");
        
        Person person2 = new Person("Laurent","Patrick","avenue des champs elysees","Paris"
                ,"75001","0123456788","lpatrick@mail.com");
        
        Person person3 = new Person("Patrice","Lumumba","avenue limete","Kinshasa"
                ,"99000","4182615887","plumumba@mail.com");

        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2,person3));

        mockMvc.perform(get("/communityEmail")
                .contentType(APPLICATION_JSON)
                .param("city","Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0]",is("lpatrick@mail.com")));
    }


}
