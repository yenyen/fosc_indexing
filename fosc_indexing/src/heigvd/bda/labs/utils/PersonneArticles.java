package heigvd.bda.labs.utils;


public class PersonneArticles extends AbstractArticles<Name>{

	@Override
	public Name createK() {
		return new Name();
	}
	
}