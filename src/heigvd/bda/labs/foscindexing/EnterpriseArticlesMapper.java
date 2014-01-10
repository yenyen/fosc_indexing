package heigvd.bda.labs.foscindexing;

import heigvd.bda.labs.utils.EnterpriseArticles;
import heigvd.bda.labs.utils.Name;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EnterpriseArticlesMapper extends Mapper<LongWritable, Text, Name, EnterpriseArticles> {
	EnterpriseArticles nameArticles;
	Name person;
	Text entreprise;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		person = new Name();
		nameArticles = new EnterpriseArticles();
		entreprise = new Text();
		
		super.setup(context);
	}

	//input "Enterprise\tPerson1 Names 1:1, 2, 3, 4, 5, 6;Person2 Names 2:1, 2, 3, 4, 5, 6;"
	//output "Person1 Names1\tEnterprise1:1, 2, 3, 4, 5, 6;Enterprise1:1, 2, 3, 4, 5, 6;"
	@Override
	public void map(LongWritable key, Text value, Context context)
		throws java.io.IOException, InterruptedException {
		String[] enterpiseInfo = value.toString().split("\t");
		entreprise.set(enterpiseInfo[0]);
		String[] entries = enterpiseInfo[1].split(";");
		
		for(String e : entries){
			String[] personInfo = e.split(":");
			String[] names = personInfo[0].split(" ");
			String[] articleIds = personInfo[1].split(",");

			for(String n : names)
				person.addName(n.trim());
			
			for(String articleId:articleIds)
				nameArticles.add(entreprise, Long.parseLong(articleId));

			context.write(person, nameArticles);
			
			person.clear();
			nameArticles.clear();			
		}
	}
}
