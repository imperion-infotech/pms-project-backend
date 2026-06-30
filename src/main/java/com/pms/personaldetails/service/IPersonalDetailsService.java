/**
 * 
 */
package com.pms.personaldetails.service;

import java.util.List;

import com.pms.personaldetails.PersonalDetails;

/**
 * 
 */
public interface IPersonalDetailsService {
	
	 public List<PersonalDetails> getAll();
	 public PersonalDetails getById(Long id);
	 public PersonalDetails create(PersonalDetails details);
	 public PersonalDetails update(Long id, PersonalDetails details);
//	 public boolean delete(Long id);
	 public Boolean deletePersonalDetails(Long id);
	 public List<PersonalDetails> search(String firstName,String lastName, String email, String phone, String city,String nationality);
	
	

}
