package heigvd.bda.labs.utils;

import java.io.*;

import org.apache.hadoop.io.*;

/**
 * A Writable implementation that stores a pair of Text objects
 * 
 * Reference: Tom White's "Hadoop the definitive guide"
 */
public class Name {

	private String firstName;
	private String lastName;

	public Name() {
	}
	
	public Name(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	@Override
	public String toString() {
		return firstName + "\t" + lastName;
	}
}
