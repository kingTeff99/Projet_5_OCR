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
import com.ocr.safetynet.model.Person;
import com.ocr.safetynet.service.PersonService;



@WebMvcTest(PersonEndpoint.class)
@RunWith(SpringRunner.class)
public class PersonEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonService personService;


    @Test
    public void getPersonsTest() throws Exception {

        //GIVEN
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
        ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        //WHEN
        when(personService.getPersons()).thenReturn(Arrays.asList(person1, person2));

        //THEN
        mockMvc.perform(get("/person")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Bob")))
                .andExpect(jsonPath("$[0].lastName", is("Bobby")))
                .andExpect(jsonPath("$[0].address", is("avenue des Bob")))
                .andExpect(jsonPath("$[0].city", is("Paris")))
                .andExpect(jsonPath("$[0].zip", is("75000")))
                .andExpect(jsonPath("$[0].phone", is("0123456789")))
                .andExpect(jsonPath("$[0].email", is("bob@mail.com")))
                .andExpect(jsonPath("$[1].firstName", is("Jack")))
                .andExpect(jsonPath("$[1].lastName", is("Jacky")))
                .andExpect(jsonPath("$[1].address", is("avenue des Jack")))
                .andExpect(jsonPath("$[1].city", is("Paris")))
                .andExpect(jsonPath("$[1].zip", is("75000")))
                .andExpect(jsonPath("$[1].phone", is("0123456788")))
                .andExpect(jsonPath("$[1].email", is("jacky@mail.com")));

        verify(personService,times(1)).getPersons();
    }

    @Test
    public void createPersonsTest() throws Exception {
        //GIVEN
        Person person;
        ObjectMapper mapper = new ObjectMapper();
        String personJSON = " {\n" +
                "        \"firstName\": \"Louis\",\n" +
                "        \"lastName\": \"Funes\",\n" +
                "        \"address\": \"9999 Culver St\",\n" +
                "        \"city\": \"Culver\",\n" +
                "        \"zip\": \"97451\",\n" +
                "        \"phone\": \"841-874-6512\",\n" +
                "        \"email\": \"louis@email.com\"\n" +
                "    }";
        person = mapper.readValue(personJSON, Person.class);

        when(personService.save(Mockito.any(Person.class))).thenReturn(person);

        //WHEN & THEN
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(personJSON))
                .andExpect(status().is(201));
    }


    @Test
    public void updatePersonsTest() throws Exception {
        //GIVEN
        Person person = new Person("Louis","Funes","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");

        when(personService.findPersonByName("Louis","Funes")).thenReturn(person);

        String personJSON = " {\n" +
                "        \"firstName\": \"Louis\",\n" +
                "        \"lastName\": \"Funes\",\n" +
                "        \"address\": \"9999 Culver St\",\n" +
                "        \"city\": \"PARIS\",\n" +
                "        \"zip\": \"75000\",\n" +
                "        \"phone\": \"0123456789\",\n" +
                "        \"email\": \"louis@email.com\"\n" +
                "    }";

        //WHEN
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(personJSON))
                .andExpect(status().is(200));

        //THEN
        assertEquals(person.getCity(),"PARIS");
        assertEquals(person.getAddress(),"9999 Culver St");
        assertEquals(person.getZip(),"75000");
        assertEquals(person.getPhone(),"0123456789");
        assertEquals(person.getEmail(),"louis@email.com");
    }

    @Test
    public void deleteWhenPersonExistTest() throws Exception {
        String personJSON = " {\n" +
                "        \"firstName\": \"Louis\",\n" +
                "        \"lastName\": \"Funes\"\n" +
                "    }";

        doNothing().when(personService).deleteByName("Louis","Funes");

        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(personJSON))
                .andExpect(status().isOk());

        verify(personService,times(1)).deleteByName("Louis","Funes");
    }

}
