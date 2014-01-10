package heigvd.bda.labs.foscindexing;

import heigvd.bda.labs.utils.EnterpriseArticles;
import heigvd.bda.labs.utils.Name;
import heigvd.bda.labs.utils.PersonneArticles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class FoscIndexing extends Configured implements Tool {
	
	private int numReducers;
	private Path inputPath;
	public static Path outputPathFilterFrArticle;
	public static Path outputPathBeforeFirstname;
	public static Path outputPathEntreprisePersonArticles;
	public static Path outputPathPersonEntrepriseArticles;
	public static Path firstNamePath;
	public static String REG_NAME = "[A-Z][a-z?]{2,}(\\-[A-Z][a-z?]{2,})?";
	public static int NB_TIMES_BEFORE_FIRSTNAME = 2;
	
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
		outputPathEntreprisePersonArticles = new Path(args[2] + "_2");
		outputPathPersonEntrepriseArticles = new Path(args[2] + "_3");
		firstNamePath = new Path(args[3]);
	}
	
	/**
	 * Utility to split a line of text in words.
	 * @param text what we want to split
	 * @return words in text as an Array of String
	 */
	public static String[] words(String text) {
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
        }

        return result;
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		if(runJobFilterFrArticle(conf) &&
				runJobBeforeFirstname(conf) &&
				runJobEntreprisePersonArticles(conf) &&
				runJobPersonEntrepriseArticles(conf))
				return 0;

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
	
	public boolean runJobEntreprisePersonArticles(Configuration conf) throws Exception {
		
		Job job = new Job(conf, "fosc_indexing");
		job.setJarByClass(FoscIndexing.class);

		job.setMapperClass(PersonneArticlesMapper.class);
		job.setReducerClass(PersonneArticlesReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(PersonneArticles.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(PersonneArticles.class);

		job.setCombinerClass(PersonneArticlesReducer.class);
		
		TextInputFormat.addInputPath(job,  outputPathFilterFrArticle);
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, outputPathEntreprisePersonArticles);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(numReducers);

		return job.waitForCompletion(true);
	}
	
	public boolean runJobPersonEntrepriseArticles(Configuration conf) throws Exception {
		
		Job job = new Job(conf, "fosc_indexing");
		job.setJarByClass(FoscIndexing.class);

		job.setMapperClass(EnterpriseArticlesMapper.class);

		job.setReducerClass(EnterpriseArticlesReducer.class);

		job.setMapOutputKeyClass(Name.class);
		job.setMapOutputValueClass(EnterpriseArticles.class);

		job.setOutputKeyClass(Name.class);
		job.setOutputValueClass(EnterpriseArticles.class);

		job.setCombinerClass(EnterpriseArticlesReducer.class);
		
		TextInputFormat.addInputPath(job,  outputPathEntreprisePersonArticles);
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, outputPathPersonEntrepriseArticles);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(numReducers);

		return job.waitForCompletion(true);
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new FoscIndexing(args), args);
		System.exit(res);
	}
}
