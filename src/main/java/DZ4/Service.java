package DZ4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


public class Service {

	private static final Logger LOGGER = Logger.getLogger(Service.class);

	
	static public List<String> readLinesFromFile(String filePath, Charset cs) {
		DOMConfigurator.configureAndWatch("log4j.xml", 1000);
		
		List<String> lines = new ArrayList();
		try {
			File inFile = new File(filePath);
			FileInputStream inStream = new FileInputStream(inFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, cs));
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			LOGGER.error("File " + filePath + " not found");
		}
		return lines;
	}

	public static void main(String[] args) throws IOException  {
		DOMConfigurator.configureAndWatch("log4j.xml", 1000);
		
		List<String> text;
		String str = null;
		String service = null;
		String res = null;
		Object obj = null;
		ArrayList<String> arrArg = null;
		ArrayList<Class> arrType = null;
		
		
		File outFile = new File("Output.txt");
		FileOutputStream outStream = new FileOutputStream(outFile);
		byte[] outBytes = null;
		
		text = readLinesFromFile("Input.txt", Charset.forName("CP1251"));
	
		for (int i = 0; i < text.size(); i++) {
			try {
				LOGGER.info("Begin read string №" + (i+1));
				StringTokenizer tokenizerStr = new StringTokenizer(text.get(i)," ");
				// System.out.println("Tokens=" + tokenizerStr.countTokens());;
				str = tokenizerStr.nextToken();
				//System.out.println("Class" + str);
				Class c = Class.forName(str);
				obj = c.newInstance();
				Class b = obj.getClass();
				service = tokenizerStr.nextToken();
				//System.out.println("service =" + service);
				// Method m = b.getMethod(service, paramTypes);
				arrArg = new ArrayList<String>();
				arrType = new ArrayList<Class>();
				while (tokenizerStr.hasMoreTokens()) {
					arrArg.add(tokenizerStr.nextToken());
					arrType.add(String.class);
				}
				Object[] argss = arrArg.toArray(new Object[arrArg.size()]);
				Class[] paramTypes = arrType.toArray(new Class[arrType.size()]);
				Method m = b.getMethod(service, paramTypes);
				LOGGER.info("End reading string №" + (i+1));
				String d = (String) m.invoke(obj, argss);
				//System.out.println("Call service " + d);
				res = d + "\n";
				outBytes = res.getBytes();
				outStream.write(outBytes);
				LOGGER.info("class=" + str + " method=" + service + " with parametrs=" + arrArg);
				LOGGER.debug("RESULT OF METHOD = " + d );
				
			} catch (ClassNotFoundException ex) {
				LOGGER.error("class= " + str + " not found");
			} catch (InstantiationException ex) {
				LOGGER.error("Cant create class " + obj + " object");
			} catch (IllegalAccessException ex) {
				LOGGER.error("Cant get access to class " + obj);
			} catch (SecurityException ex) {
				LOGGER.error("Cant get access to class " + obj);
			} catch (NoSuchElementException ex) {
				LOGGER.error("class= " + str + " not found");
			} catch (InvocationTargetException ex) {
				LOGGER.error("Cant invoke method " + service);
			} catch (NoSuchMethodException ex) {
				LOGGER.error("class=" + str + " method=" + service + " with parametrs=" + arrArg + " not found");
			}	catch (Exception ex) {
				LOGGER.error("Something bad happend");
			}
		}
	}
}
