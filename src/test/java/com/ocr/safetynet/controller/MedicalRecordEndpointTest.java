package com.ocr.safetynet.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocr.safetynet.model.MedicalRecord;
import com.ocr.safetynet.service.MedicalRecordService;

@WebMvcTest(MedicalRecordEndpoint.class)
@RunWith(SpringRunner.class)
public class MedicalRecordEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MedicalRecordService medicalRecordService;

    @Test
    public void getMedicalRecordTest() throws Exception {
    	
        //GIVEN
        List<String> medications1 = new ArrayList<>();
        medications1.add("efferalgan");
        medications1.add("advil");
        
        List<String> medications2 = new ArrayList<>();
        medications2.add("dolirhume");
        medications2.add("advilEnfant");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("crabe");
        
        List<String> allergies2= new ArrayList<>();
        allergies2.add("pollen");
        allergies2.add("fromage");


        MedicalRecord medicalrecord1 = new MedicalRecord("Steve","Biko","08/07/1989",medications1,allergies1);
        MedicalRecord medicalrecord2 = new MedicalRecord("Sylvanus","Olympio","09/13/1987",medications2,allergies2);

        //WHEN
        when(medicalRecordService.getMedicalRecords()).thenReturn(Arrays.asList(medicalrecord1, medicalrecord2));

        mockMvc.perform(get("/medicalRecord")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Steve")))
                .andExpect(jsonPath("$[0].lastName", is("Biko")))
                .andExpect(jsonPath("$[0].birthdate", is("08/07/1989")))
                .andExpect(jsonPath("$[0].medications[0]", is("efferalgan")))
                .andExpect(jsonPath("$[0].medications[1]", is("advil")))
                .andExpect(jsonPath("$[0].allergies[0]", is("crabe")))

                .andExpect(jsonPath("$[1].firstName", is("Sylvanus")))
                .andExpect(jsonPath("$[1].lastName", is("Olympio")))
                .andExpect(jsonPath("$[1].birthdate", is("09/13/1987")))
                .andExpect(jsonPath("$[1].medications[0]", is("dolirhume")))
                .andExpect(jsonPath("$[1].medications[1]", is("advilEnfant")))
                .andExpect(jsonPath("$[1].allergies[0]", is("pollen")))
                .andExpect(jsonPath("$[1].allergies[1]", is("fromage")));

        verify(medicalRecordService,times(1)).getMedicalRecords();
    }

    @Test
    public void saveMedicalRecordTest() throws Exception {
    	
        /* GIVEN */
        MedicalRecord medicalrecord;
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        String medicalrecordJSON = "{\n" +
                "        \"firstName\": \"Steve\",\n" +
                "        \"lastName\": \"Biko\",\n" +
                "        \"birthdate\": \"08/07/1989\",\n" +
                "        \"medications\": [\n" +
                "            \"aznol:350mg\",\n" +
                "            \"hydrapermazol:100mg\"\n" +
                "        ],\n" +
                "        \"allergies\": [\n" +
                "            \"nillacilan\"\n" +
                "        ]\n" +
                "    }";

        medicalrecord = objectMapper.readValue(medicalrecordJSON, MedicalRecord.class);

        when(medicalRecordService.saveMedicalRecord(Mockito.any(MedicalRecord.class))).thenReturn(medicalrecord);

        /* THEN */
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicalrecordJSON))
                .andExpect(status().is(201));
    }

    @Test
    public void updatePersonsIfMedicalRecordExistTest() throws Exception {
    	
        //GIVEN
        List<String> medications = new ArrayList<>();
        medications.add("efferalgan");


        List<String> allergies = new ArrayList<>();
        
        allergies.add("fromage");
        allergies.add("pollen");

        MedicalRecord medicalRecord = new MedicalRecord("Steve","Biko","08/07/1989", medications, allergies);


        when(medicalRecordService.getMedicalRecordByNames("Steve","Biko")).thenReturn(medicalRecord);

        String medicalrecordJSON = "{\n" +
                "        \"firstName\": \"Steve\",\n" +
                "        \"lastName\": \"Biko\",\n" +
                "        \"birthdate\": \"01/10/1997\",\n" +
                "        \"medications\": [\n" +
                "            \"aznol:350mg\",\n" +
                "            \"hydrapermazol:100mg\"\n" +
                "        ],\n" +
                "        \"allergies\": [\n" +
                "            \"nillacilan\"\n" +
                "        ]\n" +
                "    }";

        //WHEN
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicalrecordJSON))
                .andExpect(status().is(200));

        //THEN
        assertEquals(medicalRecord.getMedications().size(),2);
        assertEquals(medicalRecord.getAllergies().size(),1);
    }

    @Test
    public void deleteIfFirestationExistTest() throws Exception {
    	
        String medicalrecordJSON = "{\n" +
                "        \"firstName\": \"Steve\",\n" +
                "        \"lastName\": \"Biko\"\n" +
                "    }" ;

        doNothing().when(medicalRecordService).deleteMedicalRecordByName("Steve","Biko");

        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(medicalrecordJSON))
                .andExpect(status().isOk());

        verify(medicalRecordService,times(1)).deleteMedicalRecordByName("Steve","Biko");
    }

}
