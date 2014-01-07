package heigvd.bda.labs.foscindexing;

import static heigvd.bda.labs.foscindexing.FoscIndexing.REG_NAME;
import static heigvd.bda.labs.foscindexing.FoscIndexing.firstNamePath;
import static heigvd.bda.labs.foscindexing.FoscIndexing.outputPathBeforeFirstname;
import static heigvd.bda.labs.foscindexing.FoscIndexing.readFile;
import heigvd.bda.labs.utils.Name;
import heigvd.bda.labs.utils.PersonneArticles;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PersonneArticlesMapper extends Mapper<LongWritable, Text, Text, PersonneArticles> {
	static final Name SPECIAL_NAME=new Name("\0");
	Set<String> firstNames;
	Set<String> sentencesPreviousFirstNames;
	Text entreprise;
	PersonneArticles nameArticles;
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
		nameArticles = new PersonneArticles();
		
		super.setup(context);
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
		throws java.io.IOException, InterruptedException {
		String[] sentences = FoscIndexing.sentences(value.toString());
		boolean entrepriseFound = false;
		StringBuilder sb = new StringBuilder();
		Long articleId=Long.parseLong(sentences[0].trim());
		
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
							nameArticles.add(name, articleId);
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
		
	
		if(nameArticles.haveKey()==false)
			nameArticles.add(SPECIAL_NAME, articleId);
		context.write(entreprise, nameArticles);
		nameArticles.clear();
	}
}
