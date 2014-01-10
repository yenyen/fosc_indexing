package heigvd.bda.labs.utils;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.hadoop.io.*;

/**
 * A Writable implementation that stores a pair of Text objects
 * 
 * Reference: Tom White's "Hadoop the definitive guide"
 */
public class Name implements WritableComparable {

	private Set<String> tokens;

	public Name() {
		tokens = new LinkedHashSet<String>();
	}
	public Name(String s) {
		this();
		addName(s);
	}
	
	public void addName(String name) {
		tokens.add(name);
	}
	
	public Set<String> getNames() {
		return tokens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String n : tokens) {
			sb.append(n);
			sb.append(' ');
		}
		return sb.toString().trim();
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
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
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		tokens.clear();
		int lenNames = in.readInt();
		for(int j = 0; j < lenNames; j++) {
			int l = in.readInt();
			byte[] ba = new byte[l];
			in.readFully(ba);
			tokens.add(new String(ba));
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(tokens.size());
		for(String n : tokens) {
			out.writeInt(n.length());
			out.writeBytes(n);
		}
	}
	
	public void clear(){
		tokens.clear();
	}
	
	@Override
	public int compareTo(Object arg0) {
		Name name = (Name) arg0;
		if(equals(name)){
			return 0;
		} else {
			String[] otherNames = name.getNames().toArray(new String[0]);
			String[] thisNames = tokens.toArray(new String[0]);
			for(int i = 0; i < thisNames.length && i < otherNames.length; i++)
			{
				int value = thisNames[i].compareTo(otherNames[i]);
				if(value != 0)
					return value;
			}
			return thisNames.length - otherNames.length;
		}
	}
}
