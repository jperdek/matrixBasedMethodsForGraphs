package graphConstruction;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;


public class GraphCreator implements AutoCloseable {

	protected final Driver driver;
	 
	public GraphCreator(String uri, String user, String password) {
		this.driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
	}
	
	@Override
	public void close() throws Exception {
		driver.close();
	}
	
}
