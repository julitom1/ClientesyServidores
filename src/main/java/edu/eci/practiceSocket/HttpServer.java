package edu.eci.practiceSocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class HttpServer {
	
	private static final HttpServer instance=new HttpServer();
	
	public static  HttpServer getInstance() {
		return instance;
	}
	private HttpServer() {
		
	}

	
	private void start() throws IOException, URISyntaxException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(35000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 35000.");
			System.exit(1);
		}
		boolean running=true;
		while (running){
			Socket clientSocket = null;
			try {
				System.out.println("Listo para recibir ...");
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			serveConnection(clientSocket);
		}
		serverSocket.close();
	}
	
	public String computeDefaultResponse() {
		return "HTTP/1.1 200 OK\r\n" 
				+ "Content-Type: text/html\r\n"
				+ "\r\n"
				+ "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"UTF-8\">"
				+ "<title>Title of the document</title>\n" + "</head>" + "<body>" + "My Web Site" 
				+ "<img src=\"https://pbs.twimg.com/media/EeMZ_gyXoAATpew.jpg\"> "
				+ "</body>"
				+ "</html>";
	}
	public String getResource(URI resourceURL){
		BufferedReader br = null;
		
		String uri=resourceURL.getPath().split("/")[1];
		
		File archivo = new File (uri);
		System.out.println(archivo.getPath() + "========================");
		String rta="HTTP/1.1 200 OK\r\n" 
 				+ "Content-Type: text/html\r\n"
				+ "\r\n";
		try {
			FileReader fr = new FileReader (archivo);
			br = new BufferedReader(fr);
	         // Lectura del fichero
	         String linea;
	         
	         while((linea=br.readLine())!=null)
	        	 rta=rta+linea;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(resourceURL.getPath());
		//return computeDefaultResponse();
		return rta;
	}
	public void serveConnection(Socket clientSocket) throws IOException, URISyntaxException {
		
		
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine;
		ArrayList<String> request=new ArrayList<String>();
		
		while ((inputLine = in.readLine()) != null) {
			System.out.println("Received: " + inputLine);
			request.add(inputLine);
			if (!in.ready()) {
				break;
			}
		}
		String uriStr="";
		try {
			uriStr=request.get(0).split(" ")[1];
			URI resourceURI = new URI(uriStr);
			outputLine = getResource(resourceURI);
			out.println(outputLine);
		}catch(Exception e) {}

		out.close();
		in.close();
		clientSocket.close();
		
	}
	public static void main(String[] args) throws IOException, URISyntaxException {
		HttpServer httpServer=getInstance();
		httpServer.start();
	}

}
