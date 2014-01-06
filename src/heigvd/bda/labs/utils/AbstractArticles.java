package heigvd.bda.labs.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.hdfs.web.JsonUtil;
import org.apache.hadoop.io.Writable;

public abstract class AbstractArticles<K extends Writable> implements Writable {

	private Hashtable<K, List<Long>> map = new Hashtable<K, List<Long>>();

	public void add(K k, Long v) {
		List<Long> l = null;
		if (map.contains(k))
			l = map.get(k);
		else
			map.put(k, l = new ArrayList<Long>());
		l.add(v);
	}

	public void add(K k, Iterable<Long> args) {
		List<Long> l = null;
		if (map.contains(k))
			l = map.get(k);
		else
			map.put(k, l = new ArrayList<Long>());

		for (Long v : args)
			l.add(v);
	}

	public void clear() {
		map.clear();
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		int i = 0, c = in.readInt();
		int j = 0, n = 0;
		ArrayList<Long> l;
		for (i = 0; i < c; i++) {
			K k = createK();
			k.readFields(in);
			n = in.readInt();
			l = new ArrayList<Long>();
			for (j = 0; j < n; j++) {
				l.add(in.readLong());
			}
			map.put(k, l);
		}
	}

	public abstract K createK();

	@Override
	public void write(DataOutput out) throws IOException {

		Set<Entry<K, List<Long>>> l = map.entrySet();
		out.writeInt(l.size());
		for (Entry<K, List<Long>> o : l) {
			K k = o.getKey();
			k.write(out);
			List<Long> v = o.getValue();
			out.writeInt(v.size());
			for (int i = 0; i < v.size(); i++)
				out.writeLong(v.get(i));
		}

	}

	public boolean haveKey() {
		return map.size() > 0;
	}

	public void join(AbstractArticles<K> keyArticles) {
		Set<Entry<K, List<Long>>> items = keyArticles.map.entrySet();

		for (Entry<K, List<Long>> entry : items) {
			K key = entry.getKey();
			add(key, entry.getValue());
		}
	}

	@Override
	public String toString() {

		//Lorent Hugo:1,2,4,5,6;George Micheal:13,24,45,56,67;
		
		StringBuilder sb=new StringBuilder();
		Set<Entry<K, List<Long>>> items = map.entrySet();
		for (Entry<K, List<Long>> entry : items) {
			K key = entry.getKey();
			sb.append(key.toString());
			sb.append(':');
			boolean isFirstOnde=true;
			for(Long articleId:entry.getValue()){
				sb.append(articleId);
				if(isFirstOnde==false)
					sb.append(',');
				sb.append(articleId);
				
				isFirstOnde=false;
			}
			sb.append(';');
		}
		return sb.toString();
	}
}
