package heigvd.bda.labs.foscindexing;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


/***
 * Combiner: emit the sum of all values group by same key. 
 * (used after SentenceBeforeFirstNameMapper or WordBeforeFirstNameMapper)
 * @author aurelien
 *
 */
public class BeforeFirstnameCombiner extends Reducer<Text, IntWritable, Text, IntWritable> {
	IntWritable value;
	
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		value  = new IntWritable();
		
		super.setup(context);
	}
	
	
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) 
		throws IOException, InterruptedException {		
		int sum = 0;
		for (IntWritable value : values) {	
			sum += value.get();
		}
		value.set(sum);
		context.write(key, value);
	}
}
