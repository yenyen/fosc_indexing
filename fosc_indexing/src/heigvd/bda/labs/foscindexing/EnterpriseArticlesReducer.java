package heigvd.bda.labs.foscindexing;

import heigvd.bda.labs.utils.EnterpriseArticles;
import heigvd.bda.labs.utils.Name;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer: emit all names by enterprise
 * (used after IndexWordEntrepriseMapper and IndexSentenceEnterpriseMapper)
 * @author aurelien
 *
 */
public class EnterpriseArticlesReducer extends Reducer<Name, EnterpriseArticles, Name, EnterpriseArticles> {
	EnterpriseArticles result;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		result = new EnterpriseArticles();
		super.setup(context);
	}
	
	@Override
	public void reduce(Name key, Iterable<EnterpriseArticles> values, Context context) 
		throws IOException, InterruptedException {		
		result.clear();
		for(EnterpriseArticles value : values) {
			result.join(value);
		}
		context.write(key, result);
	}
}
