package uk.ac.soton.ldanalytics.sparql2sql.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.ac.soton.ldanalytics.sparql2sql.model.RdfTableMapping;
import uk.ac.soton.ldanalytics.sparql2sql.model.SparqlOpVisitor;
import uk.ac.soton.ldanalytics.sparql2sql.util.ResultSetConverter;
import uk.ac.soton.ldanalytics.sparql2sql.util.SQLFormatter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpWalker;

public class SparqlQueryHandler implements Handler {
	private RdfTableMapping mapping = null;
	private Properties config = null;


	public SparqlQueryHandler(Properties config, RdfTableMapping mapping) {
		this.mapping = mapping;
		this.config = config;
	}

	public void addLifeCycleListener(Listener arg0) {
		// TODO Auto-generated method stub

	}

	public boolean isFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStarting() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStopped() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStopping() {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeLifeCycleListener(Listener arg0) {
		// TODO Auto-generated method stub

	}

	public void start() throws Exception {
		// TODO Auto-generated method stub

	}

	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public Server getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
		
		if(target.equals("/sparql")) {
			try {
				Class.forName(config.getProperty("driver"));
				
				String queryStr = null;
				if(request.getContentType().toLowerCase().equals("application/sparql-query")) {
					queryStr = IOUtils.toString(request.getInputStream()); 
				} else {
					queryStr = request.getParameter("query");
				}
				
				if(queryStr!=null) {
					
//					System.out.println(queryStr);
				
					Query query = QueryFactory.create(queryStr);
					Op op = Algebra.compile(query);
					
					SparqlOpVisitor v = new SparqlOpVisitor();
					v.useMapping(mapping);
					OpWalker.walk(op,v);
					String sql = v.getSQL();
					SQLFormatter formatter = new SQLFormatter();
//					System.out.println(formatter.format(sql));
					
					Connection conn = DriverManager.getConnection(config.getProperty("jdbc"), config.getProperty("username"), config.getProperty("password"));
					Statement stat = conn.createStatement();
					ResultSet rs = stat.executeQuery(sql);
					JSONArray rsJson = ResultSetConverter.convert(rs);
					rs.close();
					conn.close();
					
					JSONObject results = new JSONObject();
					JSONObject bindings = new JSONObject();
					bindings.put("bindings",rsJson);
					results.put("results",bindings);
					
					response.setContentType("application/sparql-results+json");
			        response.setStatus(HttpServletResponse.SC_OK);
					
					PrintWriter pw = response.getWriter();
					
					pw.write(results.toString());
				}
					
				baseRequest.setHandled(true);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setServer(Server arg0) {
		// TODO Auto-generated method stub

	}

}
