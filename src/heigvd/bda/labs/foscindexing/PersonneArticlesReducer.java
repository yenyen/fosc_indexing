package heigvd.bda.labs.foscindexing;

import heigvd.bda.labs.utils.PersonneArticles;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer: emit all names by enterprise
 * (used after IndexWordEntrepriseMapper and IndexSentenceEnterpriseMapper)
 * @author aurelien
 *
 */
public class PersonneArticlesReducer extends Reducer<Text, PersonneArticles, Text, PersonneArticles> {
	PersonneArticles result;
	int i = 0;
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		result = new PersonneArticles();
		super.setup(context);
	}
	
	@Override
	public void reduce(Text key, Iterable<PersonneArticles> values, Context context) 
		throws IOException, InterruptedException {		
		result.clear();	
		for(PersonneArticles value : values) {
			result.join(value);
		}
		context.write(key, result);
	}
}
