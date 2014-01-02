package heigvd.bda.labs.utils;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.hadoop.io.*;

/**
 * A Writable implementation that stores a pair of Text objects
 * 
 * Reference: Tom White's "Hadoop the definitive guide"
 */
public class Name {

	private Set<String> names;

	public Name() {
		names = new LinkedHashSet<String>();
	}
	
	public void addName(String name) {
		names.add(name);
	}
	
	public Set<String> getNames() {
		return names;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String n : names) {
			sb.append(n);
			sb.append(" ");
		}
		return sb.toString().trim();
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((names == null) ? 0 : names.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (names == null) {
			if (other.names != null)
				return false;
		} else if (!names.equals(other.names))
			return false;
		return true;
	}
	
	
}
