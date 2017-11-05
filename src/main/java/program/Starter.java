package program;

import javax.servlet.ServletContextEvent;

import indexes.indexer;
import searcher.Application;
/**
 * 
 * @author Simone-Erba
 *
 *A ServletContextListener that start the indexer and the graph updater classes
 */
public class Starter implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		//Application a=new Application();
		//a.start();
		//indexer i=new indexer();
		//i.start();
	}
}
