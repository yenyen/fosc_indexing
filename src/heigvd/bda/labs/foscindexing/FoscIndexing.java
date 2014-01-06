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
	public static Path outputPathFilterFrArticle;
	public static Path outputPathBeforeFirstname;
	public static Path outputPathIndexEntreprise;
	public static Path firstNamePath;
	public static String REG_NAME = "[A-Z][a-z?]{2,}(\\-[A-Z][a-z?]{2,})?";
	private static int NB_TIMES_BEFORE_FIRSTNAME = 2;
	
	/**
	 * FoscIndexing Constructor.
	 * 
	 * @param args
	 */
	public FoscIndexing(String[] args) {
		if (args.length != 4) {
			System.out.println("Usage: FoscIndexing <num_reducers> <input_path> <output_path> <firstname_path>");
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
	
	
	/**
	 * Utility to split a line of text in sentences.
	 * @param text what we want to split
	 * @return sentences in text as an Array of String
	 */
	public static String[] sentences(String text) {
		String[] array = text.split("[!.<>]");
		return array;
	}
	
	/**
	 * Read all files from a directory on the hadoop file system
	 * @param confiruation hadoop
	 * @param path of the directory
	 * @return HashSet of each line of all the files
	 * @throws IOException
	 */
	public static HashSet<String> readFile(Configuration conf, Path path) throws IOException
	{
		HashSet<String> result = new HashSet<String>();
        BufferedReader br = null;
        try {
            FileSystem fs = FileSystem.get(conf);
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

	/***
	 * Mapper : emit only FR article
	 * @author aurelien
	 *
	 */
	public static class FilterFrArticleMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
			if(value.toString().contains("LANG=\"FR\""))
				context.write(value, NullWritable.get());
		}
	}
	/***
	 * Mapper : emit each part of a sentence follow by a name with value 1
	 * @author aurelien
	 *
	 */
	public static class SentenceBeforeFirstnameMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
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
	
	/***
	 * Mapper : emit all word of the text with value 1 if follow by a name or -1 if not
	 * @author aurelien
	 *
	 */
	public static class WordBeforeFirstnameMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
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
	
	/***
	 * Combiner: emit the sum of all values group by same key. 
	 * (used after SentenceBeforeFirstNameMapper or WordBeforeFirstNameMapper)
	 * @author aurelien
	 *
	 */
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
	
	/***
	 * Reducer: sum all values group by key and emit if the sum bigger than "NB_TIMES_BEFORE_FIRSTNAME"
	 * (used after BeforFirstnameCombiner)
	 * @author aurelien
	 *
	 */
	public static class BeforeFirstnameReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
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
	
	/***
	 * Mapper: emit entreprises with names (using SentenceFirstName output and FirstNames file)
	 * @author aurelien
	 *
	 */
	public static class SentenceIndexEntrepriseNameMapper extends Mapper<LongWritable, Text, Text, ListNameWritable> {
		Set<String> firstNames;
		Set<String> sentencesPreviousFirstNames;
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
			sentencesPreviousFirstNames = readFile(conf, outputPathBeforeFirstname);
			
			entreprise = new Text();
			names = new ListNameWritable();
			
			super.setup(context);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
			String[] sentences = FoscIndexing.sentences(value.toString());
			boolean entrepriseFound = false;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < sentences.length; i++) {
				if(!entrepriseFound && i > 0 && i < sentences.length - 1 && sentences[i - 1].equals("NAME") && sentences[i + 1].equals("/NAME"))
				{
					entreprise.set(sentences[i]);
					entrepriseFound = true;
				}
				else
				{
					String[] tokens = FoscIndexing.words(sentences[i]);
					for (int j = 1; j < tokens.length - 2; j++) {
						if((firstNames.contains(tokens[j].toUpperCase()) || sentencesPreviousFirstNames.contains(sb.toString())) 
								&& tokens[j].matches(REG_NAME) && !linkNeverNames.contains(tokens[j].toLowerCase()))
						{
							Name name = null;
							if(linkNames.contains(tokens[j + 1].toLowerCase()))
							{
								name = new Name();
								name.addName(tokens[j + 1]);
								name.addName(tokens[j + 2]);
							}
							else if(tokens[j + 1].matches(REG_NAME) && 
									!linkNeverNames.contains(tokens[j + 1].toLowerCase()) && 
									!linkMaybeNames.contains(tokens[j + 1].toLowerCase()))
							{
								name = new Name();
								name.addName(tokens[j + 1]);
								if(linkNames.contains(tokens[j + 1]))
									name.addName(tokens[j + 2]);
							}
							else if(tokens[j - 1].matches(REG_NAME) && !linkNeverNames.contains(tokens[j - 1].toLowerCase()))
							{
								name = new Name();
								if(j > 1 && 
										(linkNames.contains(tokens[j - 2].toLowerCase()) || linkMaybeNames.contains(tokens[j - 2].toLowerCase())))
									name.addName(tokens[j - 2]);
								
								name.addName(tokens[j - 1]);
							}
							else if(linkMaybeNames.contains(tokens[j + 1].toLowerCase()) && tokens[j + 2].matches(REG_NAME))
							{
								name = new Name();
								name.addName(tokens[j + 1]);
								name.addName(tokens[j + 2]);
							}
							
							if(name != null)
							{
								name.addName(tokens[j]);
								names.getNames().add(name);
								sb = new StringBuilder();
							}
							else
							{
								sb.append(tokens[j]);
								sb.append(" ");
							}
						}
						else
						{
							sb.append(tokens[j]);
							sb.append(" ");
						}
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
	
	/***
	 * Index emit entreprises with names (using WordBeforeFirstName output and FirstNames file)
	 * @author aurelien
	 *
	 */
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
				//ATTENTION NE FONCTIONNE PAS car <CANTON.NAME> est détecté comme <NAME>)
				if(tokens[i - 1].equals("NAME") && !entrepriseFind)
				{
					entrepriseFind = true;
					String ent = "";
					while(!tokens[i + 1].equals("NAME"))
					{
						ent += tokens[i] + " ";
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
	
	/**
	 * Reducer: emit all names by enterprise
	 * (used after IndexWordEntrepriseMapper and IndexSentenceEnterpriseMapper)
	 * @author aurelien
	 *
	 */
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

		job.setMapperClass(SentenceBeforeFirstnameMapper.class);
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

		job.setMapperClass(SentenceIndexEntrepriseNameMapper.class);
		/*or
		job.setMapperClass(IndexEntrepriseNameMapper.class);
		*/

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
