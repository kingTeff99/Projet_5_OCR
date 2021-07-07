package com.ocr.safetynet.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ocr.safetynet.model.MedicalRecord;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class MedicalRecordServiceTest {

    @Mock
    DataTreatment dataTreatment;

    @InjectMocks
    MedicalRecordService medicalrecordService;

    @Test
    public void getMedicalrecordReturnMedicalrecordsTest(){
        //GIVEN
        List<MedicalRecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        MedicalRecord medicalrecord1 = new MedicalRecord("Louis","Funes","01/10/1997",medications1,allergies1);
        MedicalRecord medicalrecord2 = new MedicalRecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(dataTreatment.getMedicalRecords()).thenReturn(ourListOfMedicalrecord);

        //WHEN
        List<MedicalRecord> result = medicalrecordService.getMedicalRecords();

        //THEN
        assertTrue(result.size()==2);
        assertTrue(result.get(0).getFirstName().equals("Louis"));
        assertTrue(result.get(0).getLastName().equals("Funes"));
        assertTrue(result.get(1).getFirstName().equals("Franck"));
        assertTrue(result.get(1).getLastName().equals("Francky"));
    }

    @Test
    public void getMedicalrecordByNameTest(){
        //GIVEN
        List<MedicalRecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        MedicalRecord medicalrecord1 = new MedicalRecord("Louis","Funes","01/10/1997",medications1,allergies1);
        MedicalRecord medicalrecord2 = new MedicalRecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(dataTreatment.getMedicalRecords()).thenReturn(ourListOfMedicalrecord);

        //WHEN
        MedicalRecord result = medicalrecordService.getMedicalRecordByName("Louis","Funes");

        //THEN
        assertTrue(result.getMedications().get(0).equals("dodoli"));
        assertTrue(result.getMedications().get(1).equals("dodol"));
        assertTrue(result.getAllergies().get(0).equals("lait"));
    }

    @Test
    public void saveMedicalRecordReturnMedicalRecordTest(){
        //GIVEN
        List<MedicalRecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        MedicalRecord medicalrecord1 = new MedicalRecord("Louis","Funes","01/10/1997",medications1,allergies1);
        MedicalRecord medicalrecord2 = new MedicalRecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(dataTreatment.getMedicalRecords()).thenReturn(ourListOfMedicalrecord);

        MedicalRecord medicalrecordToSave = new MedicalRecord("Bill","Billy","14/02/1993",medications2,allergies1);

        //WHEN
        MedicalRecord result = medicalrecordService.saveMedicalRecord(medicalrecordToSave);

        //THEN
        assertTrue(result.getMedications().get(0).equals("dodoli500"));
        assertTrue(result.getMedications().get(1).equals("dodol1000"));
        assertTrue(result.getAllergies().get(0).equals("lait"));
    }

    @Test
    public void deleteMedicalRecordTest(){
        //GIVEN
        List<MedicalRecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        MedicalRecord medicalrecord1 = new MedicalRecord("Louis","Funes","01/10/1997",medications1,allergies1);
        MedicalRecord medicalrecord2 = new MedicalRecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(dataTreatment.getMedicalRecords()).thenReturn(ourListOfMedicalrecord);
        assertTrue(ourListOfMedicalrecord.size() == 2);

        //WHEN
        medicalrecordService.deleteMedicalRecordByName("Louis","Funes");

        //THEN
        assertTrue(ourListOfMedicalrecord.size() == 1);
    }

}
