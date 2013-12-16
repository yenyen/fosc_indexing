package heigvd.bda.labs.foscindexing;

import heigvd.bda.labs.utils.ListNameWritable;
import heigvd.bda.labs.utils.Name;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;


public class FoscIndexing extends Configured implements Tool {
	
	private int numReducers;
	private Path inputPath;
	private Path outputPath;
	
	
	/**
	 * OrderInversion Constructor.
	 * 
	 * @param args
	 */
	public FoscIndexing(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: OrderInversion <num_reducers> <input_path> <output_path>");
			System.exit(0);
		}
		numReducers = Integer.parseInt(args[0]);
		inputPath = new Path(args[1]);
		outputPath = new Path(args[2]);
	}
	
	/**
	 * Utility to split a line of text in words.
	 * The text is first transformed to lowercase, all non-alphanumeric characters are removed.
	 * All spaces are removed and the text is tokenized using Java StringTokenizer.
	 *  
	 * @param text what we want to split
	 * @return words in text as an Array of String
	 */
	public static String[] words(String text) {
		text = text.toLowerCase();
		text = text.replaceAll("[^a-z]+", " ");
		text = text.replaceAll("^\\s+", "");	
	    StringTokenizer st = new StringTokenizer(text);
	    ArrayList<String> result = new ArrayList<String>();
	    while (st.hasMoreTokens())
	    	result.add(st.nextToken());
	    return Arrays.copyOf(result.toArray(),result.size(),String[].class);
	}	
	
	/*public static class PartitionerTextPair extends Partitioner<TextPair, IntWritable> {
		
		@Override
		public int getPartition(TextPair key, IntWritable value, int numPartitions) {
			int hash = key.getFirst().hashCode();
	        int partition = hash % numPartitions;
	        return partition;
		}
	}*/
	
	public static class BeforeFirstnameMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		List<String> firstNames;
		IntWritable nextFirstName;
		IntWritable nextNotFirstName;
		Text outKey;
		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			firstNames = new ArrayList<String>();
			//READ FILE HERE			
			nextFirstName = new IntWritable(1);
			nextNotFirstName = new IntWritable(-1);

			super.setup(context);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
			String[] tokens = FoscIndexing.words(value.toString());
			
			for (int i = 1; i < tokens.length-1; i++) {
				outKey.set(tokens[i - 1]);
				if(firstNames.contains(tokens[i]))
					context.write(outKey, nextFirstName);
				else
					context.write(outKey, nextNotFirstName);
			}
		}
	}
	
	public static class BeforeFirstnameCombiner extends Reducer<Text, IntWritable, Text, IntWritable> {
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

	public static class BeforeFirstnameReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
			throws IOException, InterruptedException {		
			int sum = 0;
			for (IntWritable value : values) {	
				sum += value.get();
			}
			
			if(sum > 0)
				context.write(key, NullWritable.get());
		}
	}
	
	
	public static class IndexEntrepriseNameMapper extends Mapper<LongWritable, Text, Text, ListNameWritable> {
		List<String> firstNames;
		List<String> wordsPreviousFirstNames;
		Text entreprise;
		ListNameWritable names;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			firstNames = new ArrayList<String>();
			wordsPreviousFirstNames = new ArrayList<String>();
			//READ FILE HERE			
			entreprise = new Text();
			names = new ListNameWritable();
			
			super.setup(context);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
			String[] tokens = FoscIndexing.words(value.toString());
			entreprise.set(getEntrepriseName(value.toString()));
			
			for (int i = 1; i < tokens.length - 2; i++) {
				if(firstNames.contains(tokens[i]) || wordsPreviousFirstNames.contains(tokens[i - 1]))
					names.getNames().add(new Name(tokens[i], tokens[i + 1]));
			}
		
			if(names.getNames().size() > 0)
			{
				context.write(entreprise, names);
				names.clear();
			}
		}

		private String getEntrepriseName(String string) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	public static class IndexEntrepriseNameReducer extends Reducer<Text, ListNameWritable, Text, ListNameWritable> {
		ListNameWritable result;
		
		@Override
		protected void setup(Context context) {
			result = new ListNameWritable();
		}
		
		@Override
		public void reduce(Text key, Iterable<ListNameWritable> values, Context context) 
			throws IOException, InterruptedException {		
			result.clear();
			for(ListNameWritable value : values) {
				result.join(value);
			}
			context.write(key, result);
		}
	}
	
	

	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();
		Job job = new Job(conf, "Order Inversion");

		job.setJarByClass(FoscIndexing.class);

		job.setMapperClass(BeforeFirstnameMapper.class);
		job.setReducerClass(BeforeFirstnameReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setCombinerClass(BeforeFirstnameCombiner.class);
		
		TextInputFormat.addInputPath(job, inputPath);
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, outputPath);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(numReducers);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new FoscIndexing(args), args);
		System.exit(res);
	}
}
