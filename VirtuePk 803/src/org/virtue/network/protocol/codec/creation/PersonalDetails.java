package org.virtue.network.protocol.codec.creation;

import java.util.Calendar;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 14, 2014
 */
public class PersonalDetails {

	private final Calendar dateOfBirth;
	private final int country;

	public PersonalDetails(Calendar dateOfBirth, int country) {
		this.dateOfBirth = dateOfBirth;
		this.country = country;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public int getCountry() {
		return country;
	}
	
}
