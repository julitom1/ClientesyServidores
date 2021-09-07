package edu.eci.networking;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class URLExamples {

		public static void main(String [] args) {
			try {
				URL firstSite = new URL("https://campusvirtual.escuelaing.edu.co/moodle/mod/assign/view.php?id=34731");
				System.out.println("La URL es:" + firstSite.toString());
				System.out.println("El protocolo es:" + firstSite.getProtocol());
				System.out.println("El Authority es:" + firstSite.getAuthority());
				System.out.println("El Host es:" + firstSite.getHost());
				System.out.println("El Port es:" + firstSite.getPort());
				System.out.println("El Path es:" + firstSite.getPath());
				System.out.println("El Query es:" + firstSite.getQuery());
				System.out.println("El File es:" + firstSite.getFile());
				System.out.println("La ref es:" + firstSite.getRef());
				
			} catch (MalformedURLException e) {
				Logger.getLogger(URLExamples.class.getName()).log(Level.SEVERE,null,e);
			}
		}
}
