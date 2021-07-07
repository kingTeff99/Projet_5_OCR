package com.ocr.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.ArrayList;
import java.util.List;


@JsonFilter("mySpecificFilter")
public class FireStation {

	private String address;
	
	private String station;
	
	private List<Person> personToAdd = new ArrayList<>();



	public FireStation(String address, String station) {
		super();
		this.address = address;
		this.station = station;
	}

	public FireStation(){
	};
	
	public  void addPerson(Person person) {
		
		personToAdd.add(person);
		
	}

	public List<Person> getPersonToAdd() {
		
		return personToAdd;
		
	}
	
	public String getAddress() {
		
		return address;
		
	}
	public void setAddress(String address) {
		
		this.address = address;
		
	}
	public String getStation() {
		
		return station;
		
	}
	public void setStation(String station) {
		
		this.station = station;
		
	}
	public void setPersonToAdd(List<Person> personToAdd) {
		
		this.personToAdd = personToAdd;
		
	}

}
