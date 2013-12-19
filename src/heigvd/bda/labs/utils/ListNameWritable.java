package heigvd.bda.labs.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.io.Writable;

/**
 * Very simple (and scholastic) implementation of a Writable associative array for String to Int.
 */
public class ListNameWritable implements Writable {
	
	private HashSet<Name> names = new HashSet<Name>();

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
			int lenNames = in.readInt();
			Name name = new Name();
			for(int j = 0; j < lenNames; j++) {
				int l = in.readInt();
				byte[] ba = new byte[l];
				in.readFully(ba);
				name.addName(new String(ba));
			}
			names.add(name);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(getNames().size());
		for(Name name : names) {   
			out.writeInt(name.getNames().size());
			for(String n : name.getNames()) {
				out.writeInt(n.length());
				out.writeBytes(n);
			}
		}
	}
	
	public HashSet<Name> getNames() {
		return names;
	}
	
	public void join(ListNameWritable listNames) {
		names.addAll(listNames.names);
	}
}
