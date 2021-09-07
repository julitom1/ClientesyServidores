package edu.eci.practiceSocket;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class HttpServer {
	
	private static final HashMap<String,String> content=new HashMap<String,String>();
	private static final HttpServer instance=new HttpServer();
	
	public static  HttpServer getInstance() {
		return instance;
	}
	private HttpServer() {
		content.put("css","text/css");
		content.put("js","application/javascript");
		content.put("jpeg","image/jpeg");
		content.put("jpg","image/jpeg");
		content.put("doc","application/msword");
		content.put("gif","image/gif");
		content.put("htm","text/html");
		content.put("html","text/html");
		content.put("json","application/json");
		content.put("pdf","application/pdf");
		content.put("png","image/png");
	}

	
	public void start() throws IOException, URISyntaxException {
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
	
	
	public String getResource(URI resourceURL){
		
		BufferedReader br = null;
		String uri=resourceURL.getPath().split("/")[1];
		//String uri = Paths.get("public_html" + resourceURL.getPath());
		File archivo = new File (uri);
		
		String rta=httpOk("html");
		
		try {
			FileReader fr = new FileReader (archivo);
			br = new BufferedReader(fr);
	         // Lectura del fichero
	         String linea;
	         
	         while((linea=br.readLine())!=null)
	        	 rta=rta+linea;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			rta+="ERRORrrrrr";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			rta+="ERRORssssss";
		}
		System.out.println(resourceURL.getPath());
		//return computeDefaultResponse();
		
		return rta;
	}
	public void getResourceImage(String path,String extension,OutputStream outStream) {
		System.out.println(" ======================================= 6t");
		BufferedImage image;
		try {
			
			image = ImageIO.read(new File(path+"."+extension));
			ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
	 		DataOutputStream dataOutputStream= new DataOutputStream(outStream); 
			ImageIO.write(image,extension, byteArrayOutputStream);
			dataOutputStream.writeBytes(path);
			dataOutputStream.write(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void serveConnection(Socket clientSocket) throws IOException, URISyntaxException {
		
		OutputStream outStream=clientSocket.getOutputStream();
		PrintWriter out = new PrintWriter(outStream, true);
		InputStream inputStream=clientSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
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
			
			String uri=resourceURI.getPath().split("/")[1];
			String[] ur=uri.split(".");
			System.out.println(uri + "=============================================================");
			getResourceImage(ur[0],ur[1],outStream);
			
			//out.println(rta);
		}catch(Exception e) {
		}

		out.close();
		in.close();
		clientSocket.close();
		
	}
	public String httpOk(String key) {
		return "HTTP/1.1 200 OK \r\n" 
 				+ "Content-Type: "+ content.get(key) + "\r\n"
				+ "\r\n";
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
	public static void main(String[] args) throws IOException, URISyntaxException {
		HttpServer httpServer=getInstance();
		httpServer.start();
	}

}
