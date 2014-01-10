package heigvd.bda.labs.foscindexing;

import java.io.IOException;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import static heigvd.bda.labs.foscindexing.FoscIndexing.*;

/***
 * Mapper : emit each part of a sentence follow by a name with value 1
 * @author aurelien
 *
 */
public class SentenceBeforeFirstnameMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	Set<String> firstNames;
	IntWritable ONE;
	Text outKey;
	
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		firstNames = FoscIndexing.readFile(context.getConfiguration(), firstNamePath);	
		ONE = new IntWritable(1);
		outKey = new Text();
		super.setup(context);
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
		throws java.io.IOException, InterruptedException {
		String[] sentences = FoscIndexing.sentences(value.toString());
		for (int i = 1; i < sentences.length-1; i++) {
			StringBuilder sb =  new StringBuilder();
			String[] words = FoscIndexing.words(sentences[i]);
			for (int j = 0; j < words.length; j++) {
				if(words[j].matches(REG_NAME) && ((j + 1 < words.length &&
						firstNames.contains(words[j + 1].toUpperCase()) && 
						words[j + 1].matches(REG_NAME)) || firstNames.contains(words[j].toUpperCase())))
				{
					j++;
					outKey.set(sb.toString());
					context.write(outKey, ONE);
					sb = new StringBuilder();
				}
				else
				{
					sb.append(words[j]);
					sb.append(" ");
				}
			}
		}
	}
}
