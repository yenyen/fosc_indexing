package heigvd.bda.labs.foscindexing;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import static heigvd.bda.labs.foscindexing.FoscIndexing.*;

/***
 * Reducer: sum all values group by key and emit if the sum bigger than "NB_TIMES_BEFORE_FIRSTNAME"
 * (used after BeforFirstnameCombiner)
 * @author aurelien
 *
 */
public class BeforeFirstnameReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) 
		throws IOException, InterruptedException {		
		int sum = 0;
		for (IntWritable value : values) {	
			sum += value.get();
		}
		
		if(sum > NB_TIMES_BEFORE_FIRSTNAME)
			context.write(key, NullWritable.get());
	}
}