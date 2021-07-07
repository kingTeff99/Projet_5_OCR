package com.ocr.safetynet.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.ocr.safetynet.model.Person;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {


    @Mock
    DataTreatment dataTreatment;

    @InjectMocks
    PersonService personService;


    @Test
    public void getPersonsShouldReturnAllPersonsTest(){
    	
        //GIVEN
        List<Person> ourListOfPerson = new ArrayList<>();
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        ourListOfPerson.add(person1);
        ourListOfPerson.add(person2);

        when(dataTreatment.getPersons()).thenReturn(ourListOfPerson);

        //WHEN
        List<Person> result = personService.getPersons();

        //THEN
        assertTrue(result.size() == 2);
        assertTrue(result.get(0).getFirstName().equals("Bob"));
        assertTrue(result.get(0).getLastName().equals("Bobby"));
        assertTrue(result.get(1).getFirstName().equals("Jack"));
        assertTrue(result.get(1).getLastName().equals("Jacky"));
    }

    @Test
    public void savePersonReturnThePersonToSave(){
        //GIVEN
        List<Person> ourListOfPerson = new ArrayList<>();
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        ourListOfPerson.add(person1);
        ourListOfPerson.add(person2);

        when(dataTreatment.getPersons()).thenReturn(ourListOfPerson);

        Person personTosave = new Person("Louis","Funes","avenue des Louis","Brest"
                ,"60000","0110133","lou@mail.com");

        //WHEN
        Person result = personService.save(personTosave);

        //THEN
        assertTrue(ourListOfPerson.size()==3);
        assertTrue(result.getFirstName().equals("Louis"));
        assertTrue(result.getLastName().equals("Funes"));
    }

    @Test
    public void deletePersonTest(){
        //GIVEN
        List<Person> ourListOfPerson = new ArrayList<>();
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        ourListOfPerson.add(person1);
        ourListOfPerson.add(person2);

        when(dataTreatment.getPersons()).thenReturn(ourListOfPerson);
        assertTrue(ourListOfPerson.size()==2);
        //WHEN
        personService.deleteByName("Bob","Bobby");

        //THEN
        assertTrue(ourListOfPerson.size()==1);
    }

    @Test
    public void getPersonByNameShouldReturnTheGoodPersonTest(){
        List<Person> ourListOfPerson = new ArrayList<>();
        Person person1 = new Person("Bob","Bobby","avenue des Bob","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");

        ourListOfPerson.add(person1);
        ourListOfPerson.add(person2);

        when(dataTreatment.getPersons()).thenReturn(ourListOfPerson);

        //WHEN
        Person result = personService.findPersonByName("Jack","Jacky");

        //THEN
        assertTrue(result.getFirstName().equals("Jack"));
        assertTrue(result.getLastName().equals("Jacky"));
        assertTrue(result.getPhone().equals("0123456788"));
    }

    @Test
    public void calculateAgeFromBirthdateToReturnAgeTest(){
    	
        assertTrue(personService.ageCalculation("01/10/2000") == 21);
        assertTrue(personService.ageCalculation("01/10/1997") == 24);
    }

    @Test
    public void getChildrenAndAdultByAddressTest(){
    	
        Person person1 = new Person("Bob","Bobby","avenue des Jack","Paris"
                ,"75000","0123456789","bob@mail.com");
        Person person2 = new Person("Jack","Jacky","avenue des Jack","Paris"
                ,"75000","0123456788","jacky@mail.com");
        Person person3 = new Person("Jack","Jacky","avenue des autres","Paris"
                ,"75000","0123456788","jacky@mail.com");

        person1.setBirthdate("01/10/1997");
        person2.setBirthdate("01/10/1993");
        person3.setBirthdate("01/10/2005");

        when(dataTreatment.getPersons()).thenReturn(Arrays.asList(person1, person2, person3));

        //WHEN
        JSONObject json = personService.sortChildrenAndAdultByAddress("avenue des Jack");

        //THEN
        assertTrue(json.containsKey("avenue des Jack"));
    }

}
