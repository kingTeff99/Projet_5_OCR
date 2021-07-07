package com.ocr.safetynet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocr.safetynet.model.MedicalRecord;

@Service
public class MedicalRecordService {

    @Autowired
    private DataTreatment dataTreatment;

    public List<MedicalRecord> getMedicalRecords(){
    	
        return dataTreatment.getMedicalRecords();
        
    }

    public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
    	
        for (MedicalRecord medicalrecord : dataTreatment.getMedicalRecords()) {
        	
            if(medicalrecord.getFirstName().equals(firstName) && medicalrecord.getLastName().equals(lastName)) {
            	
                return medicalrecord;

            }
        }
        return null;
    }

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
    	
    	dataTreatment.getMedicalRecords().add(medicalRecord);
    	
        return medicalRecord;
    }

    public void deleteMedicalRecordByName(String firstName, String lastName) {
    	
    	dataTreatment.getMedicalRecords().removeIf(person -> (person.getFirstName().equals(firstName))
                && (person.getLastName().equals(lastName)));
    }



}
