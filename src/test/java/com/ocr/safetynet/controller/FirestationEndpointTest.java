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

import java.util.Arrays;

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
import com.ocr.safetynet.model.FireStation;
import com.ocr.safetynet.service.FireStationService;

@WebMvcTest(FirestationEndpoint.class)
@RunWith(SpringRunner.class)
public class FirestationEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FireStationService fireStationService;

    @Test
    public void getFireStationsTest() throws Exception {
    	
        //GIVEN
        FireStation firestation1 = new FireStation("avenue des champs elysees","2");
        
        FireStation firestation2 = new FireStation("rue du bourg","5");

        //WHEN
        when(fireStationService.getFirestations()).thenReturn(Arrays.asList(firestation1, firestation2));

        mockMvc.perform(get("/firestations")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].address", is("avenue des champs elysees")))
                .andExpect(jsonPath("$[0].station", is("2")))
                .andExpect(jsonPath("$[1].address", is("rue du bourg")))
                .andExpect(jsonPath("$[1].station", is("5")));

        verify(fireStationService,times(1)).getFirestations();
    }

    @Test
    public void createFireStationTest() throws Exception {
    	
        //GIVEN
        FireStation firestation;
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        String firestationJSON = "{\n" +
                "        \"address\": \"2 Rue de limete\",\n" +
                "        \"station\": \"2\"\n" +
                "    }" ;

        firestation = objectMapper.readValue(firestationJSON, FireStation.class);

        when(fireStationService.saveFireStation(Mockito.any(FireStation.class))).thenReturn(firestation);

        //THEN
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJSON))
                .andExpect(status().is(201));
    }

    @Test
    public void updatePersonsFirestationIfExistTest() throws Exception {
    	
        //GIVEN
        FireStation firestation = new FireStation("1 rue du kentucky","12");

        when(fireStationService.getAllFireStation("1 rue du kentucky")).thenReturn(firestation);

        String firestationJSON = "{\n" +
                "        \"address\": \"1 rue du kentucky\",\n" +
                "        \"station\": \"17\"\n" +
                "    }" ;

        //WHEN
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJSON))
                .andExpect(status().is(200));

        //THEN
        assertEquals(firestation.getAddress(),"1 rue du kentucky");
        assertEquals(firestation.getStation(),"17");
    }


    @Test
    public void deleteFirestationIfExistTest() throws Exception {
        String firestationJSON = "{\n" +
                "        \"address\": \"999 Rue New York\",\n" +
                "        \"station\": \"25\"\n" +
                "    }" ;

        doNothing().when(fireStationService).deleteStation("999 Rue New York","25");

        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firestationJSON))
                .andExpect(status().isOk());

        verify(fireStationService,times(1)).deleteStation("999 Rue New York","25");
    }

}
