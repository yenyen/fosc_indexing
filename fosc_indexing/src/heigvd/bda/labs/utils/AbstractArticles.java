package heigvd.bda.labs.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.hdfs.web.JsonUtil;
import org.apache.hadoop.io.Writable;

public abstract class AbstractArticles<K extends Writable> implements Writable {

	private Hashtable<K, Set<Long>> map = new Hashtable<K, Set<Long>>();

	public void add(K k, Long v) {
		Set<Long> l = null;
		if (map.containsKey(k))
			l = map.get(k);
		else
			map.put(k, l = new HashSet<Long>());
		l.add(v);
	}

	public void add(K k, Iterable<Long> args) {
		Set<Long> l = null;

		if (map.containsKey(k))
			l = map.get(k);
		else
			map.put(k, l = new HashSet<Long>());

		for (Long v : args)
			l.add(v);
	}

	public void clear() {
		map.clear();
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		clear();
		
		int c = in.readInt();
		Set<Long> l;
		
		for (int i = 0; i < c; i++) {
			K k = createK();
			k.readFields(in);
			int n = in.readInt();
			l = new HashSet<Long>();
			for (int j = 0; j < n; j++) {
				l.add(in.readLong());
			}
			map.put(k, l);
		}
	}

	public abstract K createK();

	@Override
	public void write(DataOutput out) throws IOException {

		Set<Entry<K, Set<Long>>> l = map.entrySet();
		out.writeInt(l.size());
		for (Entry<K, Set<Long>> o : l) {
			K k = o.getKey();
			k.write(out);
			Set<Long> v = o.getValue();
			out.writeInt(v.size());
			for (Long article : v)
				out.writeLong(article);
		}

	}

	public boolean haveKey() {
		return map.size() > 0;
	}

	public void join(AbstractArticles<K> keyArticles) {
		Set<Entry<K, Set<Long>>> items = keyArticles.map.entrySet();

		for (Entry<K, Set<Long>> entry : items) {
			add(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String toString() {

		//Lorent Hugo:1,2,4,5,6;George Micheal:13,24,45,56,67;
		
		StringBuilder sb=new StringBuilder();
		Set<Entry<K, Set<Long>>> items = map.entrySet();
		for (Entry<K, Set<Long>> entry : items) {
			K key = entry.getKey();
			sb.append(key.toString());
			sb.append(':');
			boolean isFirstOne = true;
			for(Long articleId:entry.getValue()){
				if(isFirstOne)
					isFirstOne = false;
				else
					sb.append(',');
				
				sb.append(articleId);
			}
			sb.append(';');
		}
		return sb.toString();
	}
}
