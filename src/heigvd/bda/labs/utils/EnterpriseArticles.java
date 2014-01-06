package heigvd.bda.labs.utils;

import org.apache.hadoop.io.Text;


public class EnterpriseArticles extends AbstractArticles<Text>{

	@Override
	public Text createK() {
		return new Text();
	}
}
