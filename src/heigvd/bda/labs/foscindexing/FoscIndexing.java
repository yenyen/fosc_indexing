package heigvd.bda.labs.foscindexing;

import heigvd.bda.labs.utils.ListNameWritable;
import heigvd.bda.labs.utils.Name;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;


public class FoscIndexing extends Configured implements Tool {
	
	private int numReducers;
	private Path inputPath;
	private static Path outputPathFilterFrArticle;
	private static Path outputPathBeforeFirstname;
	private static Path outputPathIndexEntreprise;
	private static Path firstNamePath;
	private static String REG_NAME = "[A-Z][a-z?][a-z?]+(\\-[A-Z][a-z?]+)?";
	
	
	/**
	 * OrderInversion Constructor.
	 * 
	 * @param args
	 */
	public FoscIndexing(String[] args) {
		if (args.length != 4) {
			System.out.println("Usage: OrderInversion <num_reducers> <input_path> <output_path> <firstname_path>");
			System.exit(0);
		}
		numReducers = Integer.parseInt(args[0]);
		inputPath = new Path(args[1]);
		outputPathFilterFrArticle = new Path(args[2] + "_0");
		outputPathBeforeFirstname = new Path(args[2] + "_1");
		outputPathIndexEntreprise = new Path(args[2] + "_2");
		firstNamePath = new Path(args[3]);
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
		//text = text.toLowerCase();
		text = text.replaceAll("[^a-zA-Z?\\-]+", " ");
		text = text.replaceAll("^\\s+", "");	
	    StringTokenizer st = new StringTokenizer(text);
	    ArrayList<String> result = new ArrayList<String>();
	    while (st.hasMoreTokens())
	    	result.add(st.nextToken());
	    return Arrays.copyOf(result.toArray(),result.size(),String[].class);
	}
	
	
	public static HashSet<String> readFile(Configuration conf, Path path) throws IOException
	{
	    //hardcoded or set it in the jobrunner class and retrieve via this key
	   // String location = conf.get("job.stopwords.path");
	   // if (location != null) {
		HashSet<String> result = new HashSet<String>();
        BufferedReader br = null;
        try {
            FileSystem fs = FileSystem.get(conf);
           // Path path = new Path(location);
            if (fs.exists(path)) {
            	FileStatus[] files = fs.listStatus(path);
            	for(FileStatus file : files)
            	{
	                FSDataInputStream fis = fs.open(file.getPath());
	                br = new BufferedReader(new InputStreamReader(fis));
	                String line = null;
	                while ((line = br.readLine()) != null && line.trim().length() > 0) {
	                	result.add(line);
	                }
            	}
            	System.out.println(result);
            }
            else
            {
            	throw new IOException("Directory not found");
            }
        }
        finally {
        	IOUtils.closeStream(br);
            //IOUtils.closeQuietly(br);
        }

        return result;
	}
	
	/*public static class PartitionerTextPair extends Partitioner<TextPair, IntWritable> {
		
		@Override
		public int getPartition(TextPair key, IntWritable value, int numPartitions) {
			int hash = key.getFirst().hashCode();
	        int partition = hash % numPartitions;
	        return partition;
		}
		conf.setNumReduceTasks(0)
	}*/
	
	public static class FilterFrArticleMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
			if(value.toString().contains("LANG=\"FR\""))
				context.write(value, NullWritable.get());
		}
	}
	
	
	public static class BeforeFirstnameMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		Set<String> firstNames;
		IntWritable nextFirstName;
		IntWritable nextNotFirstName;
		Text outKey;
		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			firstNames = FoscIndexing.readFile(context.getConfiguration(), firstNamePath);	
			System.out.println(firstNames);
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
		Set<String> firstNames;
		Set<String> wordsPreviousFirstNames;
		Text entreprise;
		ListNameWritable names;
		Set<String> linkNames;
		Set<String> linkMaybeNames;
		Set<String> linkNeverNames;
	

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			Configuration conf = context.getConfiguration();
			
			linkNames = new HashSet<String>();
			linkNames.add("dr");
			linkNames.add("dos");
			linkNames.add("di");
			linkNames.add("me");
			
			linkMaybeNames = new HashSet<String>();
			linkMaybeNames.add("von");
			linkMaybeNames.add("de");
			
			linkNeverNames = new HashSet<String>();
			linkNeverNames.add("st");
			linkNeverNames.add("saint");
			linkNeverNames.add("sainte");
			linkNeverNames.add("firma");
			linkNeverNames.add("soci?t?");
			linkNeverNames.add("le");
			linkNeverNames.add("la");
			
			firstNames = readFile(conf, firstNamePath);
			wordsPreviousFirstNames = readFile(conf, outputPathBeforeFirstname);
			
			System.out.println("------------------------\n" + firstNames);
			System.out.println("------------------------\n" + wordsPreviousFirstNames);
			
			entreprise = new Text();
			names = new ListNameWritable();
			
			super.setup(context);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
			String[] tokens = FoscIndexing.words(value.toString());
			boolean entrepriseFind = false;
			//entreprise.set(getEntrepriseName(value.toString()));
			for (int i = 1; i < tokens.length - 2; i++) {
				if(tokens[i - 1].equals("NAME") && !entrepriseFind)
				{
					entrepriseFind = true;
					String ent = "";
					while(!tokens[i + 1].equals("NAME"))
					{
						ent += " " + tokens[i];
						i++;
					}
					entreprise.set(ent);
				}
				else if((firstNames.contains(tokens[i].toUpperCase()) || wordsPreviousFirstNames.contains(tokens[i - 1])) 
						&& tokens[i].matches(REG_NAME) && !linkNeverNames.contains(tokens[i].toLowerCase()))
				{
					Name name = null;
					if(linkNames.contains(tokens[i + 1].toLowerCase()))
					{
						name = new Name();
						name.addName(tokens[i + 1]);
						name.addName(tokens[i + 2]);
					}
					else if(tokens[i + 1].matches(REG_NAME) && 
							!linkNeverNames.contains(tokens[i + 1].toLowerCase()) && 
							!linkMaybeNames.contains(tokens[i + 1].toLowerCase()))
					{
						name = new Name();
						name.addName(tokens[i + 1]);
						if(linkNames.contains(tokens[i + 1]))
								name.addName(tokens[i + 2]);
					}
					else if(tokens[i - 1].matches(REG_NAME) && !linkNeverNames.contains(tokens[i - 1].toLowerCase()))
					{
						name = new Name();
						if(linkNames.contains(tokens[i - 2].toLowerCase()) || linkMaybeNames.contains(tokens[i -2].toLowerCase()))
							name.addName(tokens[i - 2]);
						
						name.addName(tokens[i - 1]);
					}
					else if(linkMaybeNames.contains(tokens[i + 1].toLowerCase()) && tokens[i + 2].matches(REG_NAME))
					{
						name = new Name();
						name.addName(tokens[i + 1]);
						name.addName(tokens[i + 2]);
					}
					
					if(name != null)
					{
						name.addName(tokens[i]);
						names.getNames().add(name);
					}
				}
			}
		
			if(names.getNames().size() > 0)
			{
				context.write(entreprise, names);
				names.clear();
			}
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
		if(runJobFilterFrArticle(conf))
		{
			if(runJobBeforeFirstname(conf))
				if(runJobIndexEntrepriseName(conf))
					return 0;
		}
		return 1;
	}
	
	public boolean runJobFilterFrArticle(Configuration conf) throws Exception {
		Job job = new Job(conf, "fosc_indexing");
		job.setJarByClass(FoscIndexing.class);

		job.setMapperClass(FilterFrArticleMapper.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		TextInputFormat.addInputPath(job, inputPath);
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, outputPathFilterFrArticle);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(0);

		return job.waitForCompletion(true);
	}
	
	public boolean runJobBeforeFirstname(Configuration conf) throws Exception {
		
		Job job = new Job(conf, "fosc_indexing");
		job.setJarByClass(FoscIndexing.class);

		job.setMapperClass(BeforeFirstnameMapper.class);
		job.setReducerClass(BeforeFirstnameReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setCombinerClass(BeforeFirstnameCombiner.class);
		
		TextInputFormat.addInputPath(job, outputPathFilterFrArticle);
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, outputPathBeforeFirstname);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(numReducers);

		return job.waitForCompletion(true);
	}
	
	public boolean runJobIndexEntrepriseName(Configuration conf) throws Exception {
		
		Job job = new Job(conf, "fosc_indexing");
		job.setJarByClass(FoscIndexing.class);

		job.setMapperClass(IndexEntrepriseNameMapper.class);
		job.setReducerClass(IndexEntrepriseNameReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(ListNameWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(ListNameWritable.class);

		job.setCombinerClass(IndexEntrepriseNameReducer.class);
		
		TextInputFormat.addInputPath(job,  outputPathFilterFrArticle);
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, outputPathIndexEntreprise);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(numReducers);

		return job.waitForCompletion(true);
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new FoscIndexing(args), args);
		System.exit(res);
	}
}
