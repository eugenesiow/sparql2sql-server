package uk.ac.soton.ldanalytics.sparql2sql.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Server;

import uk.ac.soton.ldanalytics.sparql2sql.model.RdfTableMapping;

public class SparqlServer {

	public static void main(String[] args) {
		String configFile = "config.properties";
		if(args.length>0) {
			configFile = args[0];
		}
		
		Properties config = new Properties();
		if(!configFile.equals("")) {
			try {
				config.load(new FileInputStream(configFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		RdfTableMapping mapping = new RdfTableMapping();
		File folder = new File(config.getProperty("mappings"));
		for(File file:folder.listFiles()) {
			String tempFileName = file.getName();
			if(!tempFileName.startsWith(".") && tempFileName.endsWith(".nt"))
				mapping.loadMapping(file.getPath());
		}
		
        Server server = new Server(8080);
        server.setHandler(new SparqlQueryHandler(config, mapping));
 
        try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}

}
