package heigvd.bda.labs.foscindexing;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/***
 * Mapper : emit only FR article
 * @author aurelien
 *
 */
public class FilterFrArticleMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
	@Override
	public void map(LongWritable key, Text value, Context context)
		throws java.io.IOException, InterruptedException {
		if(value.toString().contains("LANG=\"FR\""))
			context.write(value, NullWritable.get());
	}
}
