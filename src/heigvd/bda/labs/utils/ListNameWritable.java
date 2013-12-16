package heigvd.bda.labs.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Writable;

/**
 * Very simple (and scholastic) implementation of a Writable associative array for String to Int.
 */
public class ListNameWritable implements Writable {
	
	private List<Name> names = new ArrayList<Name>();

	public void clear() {
		names.clear();
	}

	public String toString() {
		return names.toString();
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		clear();
		
		int len = in.readInt();
		for (int i = 0; i < len; i++) {
			Name name = new Name();
			int l = in.readInt();
			byte[] ba = new byte[l];
			in.readFully(ba);
			name.setFirstName(new String(ba));
			
			l = in.readInt();
			ba = new byte[l];
			in.readFully(ba);
			name.setLastName(new String(ba));
			
			names.add(name);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(getNames().size());
		for(Name name : names) {       
			out.writeInt(name.getFirstName().length());
			out.writeBytes(name.getFirstName());
			out.writeInt(name.getLastName().length());
			out.writeBytes(name.getLastName());
		}
	}
	
	public List<Name> getNames() {
		return names;
	}
	
	public void join(ListNameWritable listNames) {
		names.addAll(listNames.names);
	}
}
