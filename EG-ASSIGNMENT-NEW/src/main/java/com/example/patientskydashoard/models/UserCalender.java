package com.example.patientskydashoard.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "UserCalender")

public class UserCalender {
	
	@Id
	
	 private String calendar_id;
    private String username;
	   
		  
		  }
