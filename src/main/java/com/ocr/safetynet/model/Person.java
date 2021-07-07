package com.ocr.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFilter("mySpecificFilter")
public class Person {

	private String firstName;
	
	private String lastName;
	
	private String address;
	
	private String city;
	
	private String zip;
	
	private String phone;
	
	private String email;

	private String birthdate;
	
	private Integer age;
	
	private List<String> medications = new ArrayList<>();
	
	private List<String> allergies = new ArrayList<>(); 

	public Person(String firstName,String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person(String firstName, String lastName, String address, String phone, String birthdate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.birthdate = birthdate;
	}

	public Person(){};

	public Person(String firstName, String lastName, String address, String city, String zip, String phone,
				  String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.phone = phone;
		this.email = email;
	}

	public Person(String lastName, String phone, Integer age, List<String> medications, List<String> allergies) {
		super();
		this.lastName = lastName;
		this.phone = phone;
		this.age = age;
		this.medications = medications;
		this.allergies = allergies;
	}

	
	public String getBirthdate() {
		
		return birthdate;
		
	}

	public void setBirthdate(String birthdate)  {
		
		this.birthdate = birthdate;
		
	}
	
	public Integer getAge() {
		
		return age;
		
	}

	public void setAge(Integer age) {
		
		this.age = age;
		
	}

	public String getFirstName() {
		
		return firstName;
		
	}
	public void setFirstName(String firstName) {
		
		this.firstName = firstName;
		
	}
	public String getLastName() {
		
		return lastName;
		
	}
	public void setLastName(String lastName) {
		
		this.lastName = lastName;
		
	}
	public String getAddress() {
		
		return address;
		
	}
	
	public void setAddress(String address) {
		
		this.address = address;
		
	}
	public String getCity() {
		
		return city;
		
	}
	public void setCity(String city) {
		
		this.city = city;
		
	}
	public String getZip() {
		
		return zip;
		
	}
	public void setZip(String zip) {
		
		this.zip = zip;
		
	}
	public String getPhone() {
		
		return phone;
		
	}
	
	public void setPhone(String phone) {
		
		this.phone = phone;
		
	}
	public String getEmail() {
		
		return email;
		
	}
	public void setEmail(String email) {
		
		this.email = email;
		
	}

	public List<String> getMedications() {
		
		return medications;
		
	}


	public List<String> getAllergies() {
		
		return allergies;
		
	}

	public void setMedications(List<String> medications) {
		
		this.medications = medications;
		
	}

	public void setAllergies(List<String> allergies) {
		
		this.allergies = allergies;
		
	}
}
