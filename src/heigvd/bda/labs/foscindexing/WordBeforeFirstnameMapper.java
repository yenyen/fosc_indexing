package heigvd.bda.labs.foscindexing;

import java.io.IOException;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import static heigvd.bda.labs.foscindexing.FoscIndexing.*;

/***
 * Mapper : emit all word of the text with value 1 if follow by a name or -1 if not
 * @author aurelien
 *
 */
public class WordBeforeFirstnameMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	Set<String> firstNames;
	IntWritable nextFirstName;
	IntWritable nextNotFirstName;
	Text outKey;
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		firstNames = FoscIndexing.readFile(context.getConfiguration(), firstNamePath);	
		nextFirstName = new IntWritable(1);
		nextNotFirstName = new IntWritable(-1);
		outKey = new Text();
		super.setup(context);
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
		throws java.io.IOException, InterruptedException {
		String[] tokens = FoscIndexing.words(value.toString());
		for (int i = 1; i < tokens.length-1; i++) {
			outKey.set(tokens[i - 1]);
			if(firstNames.contains(tokens[i].toUpperCase()) && tokens[i].matches(REG_NAME))
				context.write(outKey, nextFirstName);
			else
				context.write(outKey, nextNotFirstName);
		}
	}
}