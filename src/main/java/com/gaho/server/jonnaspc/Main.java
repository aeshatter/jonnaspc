package com.gaho.server.jonnaspc;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import java.net.InetSocketAddress;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * Hello world!
 *
 */
@SuppressWarnings("restriction")
class RequestHandler implements HttpHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	public void handle(HttpExchange ex) throws IOException {
		logger.info("Request received. Method: {}, host: {}",ex.getRequestMethod(),ex.getRemoteAddress().getAddress().getHostAddress());
		InputStream is = ex.getRequestBody();
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, "UTF-8");
		logger.info("Request body: {} ", writer.toString().replaceAll("\\s", ""));
		int code = 200;
		
		if(ex.getRequestMethod().equals("POST")) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Command cmd = mapper.readValue(writer.toString(), Command.class);
				if(cmd.getToken() == null || !filter(cmd)) {
					logger.info("Authentication failed");
					code = 500;
				} else {
					try {
						CommandExecutor.execute(cmd);
					} catch (NoSuchCommandException e) {
						code = 500;
						logger.info(e.getMessage());
					}
				}
			} catch (IOException e) {
				logger.info("IOException of type {} caught. Message: {}", e.getClass().getSimpleName(), e.getMessage());	
			}
		}
		sendResponse(ex,code);	
	}
	
	private void sendResponse(HttpExchange ex, int status) throws IOException {
		String response = "Status" + status;
		ex.sendResponseHeaders(200,response.getBytes().length);
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	private static boolean filter(Filterable f) {
		return (f.getToken().equals("tindrasbajs"));
	}
}



public class Main 
{
	private final static Logger logger = LoggerFactory.getLogger(Main.class);
	
    @SuppressWarnings("restriction")
	public static void main( String[] args ) throws IOException
    {
    	HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    	logger.info("Starting http server...");
    	server.createContext("/", new RequestHandler());
    	server.setExecutor(null);
    	server.start();
    	logger.info("Server started.");
    }
}
