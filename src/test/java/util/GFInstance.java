/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;

/**
 *
 * @author Michael Koppen
 */
public class GFInstance {

	private static final Logger LOG = Logger.getLogger(GFInstance.class.getName());

	public static Deployer deployer;
	public static GlassFish glassfish;
	public static ScatteredArchive archive;
	public static final String APP_NAME = "OwnSimpleCacheTest";

	public static void startInstance() throws Exception {

		GlassFishProperties glassfishProperties = new GlassFishProperties();
		File configFile = new File("src/test/resources/META-INF", "domain.xml");
		glassfishProperties.setConfigFileURI(configFile.toURI().toString());

		glassfish = GlassFishRuntime.bootstrap().newGlassFish(glassfishProperties);

		// Set the log levels. For example, set 'deployment' and 'server' log levels to FINEST 
		Logger.getLogger("").getHandlers()[0].setLevel(Level.WARNING);
		Logger.getLogger("javax.enterprise.system.tools.deployment").setLevel(Level.WARNING);
		Logger.getLogger("javax.enterprise.system").setLevel(Level.WARNING);

		glassfish.start();

		deployer = glassfish.getDeployer();
		archive = new ScatteredArchive(APP_NAME, ScatteredArchive.Type.WAR);
		archive.addClassPath(new File("target", "embedded-classes"));

		System.out.println(readFile(new File("target", "embedded-classes/META-INF/glassfish-resources.xml")));
		archive.addMetadata(new File("target", "embedded-classes/META-INF/glassfish-resources.xml"));
//		for (File jar : new File("target", "wlc-webapp/WEB-INF/lib/").listFiles()) {
//			archive.addClassPath(jar);
//		}
	}

	public static void stopInstance() throws Exception {
		glassfish.dispose();
	}

	private static String readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String content = "";
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			content = sb.toString();
		} finally {
			br.close();
		}
		return content;
	}

}
