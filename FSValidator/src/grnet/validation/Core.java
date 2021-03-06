/**
 * 
 */
package grnet.validation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * @author vogias
 * 
 */
public class Core {

	/**
	 * @param args
	 */
	String reason;
	Boolean flag;
	Validator validator;

	public Core(String xsdPath) {
		reason = "";
		flag = true;

		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Schema schema;
		try {
			schema = factory.newSchema(new URL(xsdPath));
			validator = schema.newValidator();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("Malformed URL.");
			System.err.println("Please correct the input XSD URL");
			System.err.println("Exiting...");
			System.exit(-1);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean validateXMLSchema(File xml) {

		ValidationErrorHandler lenient = new ValidationErrorHandler();
		validator.setErrorHandler(lenient);
		try {
			validator.validate(new StreamSource(xml));

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Source XML File not found ");
			System.err.println("Please correct the input XML Folder location.");
			System.err.println("Exiting...");
			System.exit(-1);
		}

		String message = lenient.getMessage();
		setReason(message);

		if (message.contains("**Parsing Error**"))

			flag = false;

		else if (message.contains("**Fatal Error**"))

			flag = false;
		else if (message.contains("**Parsing Warning**"))
			flag = false;
		else
			flag = true;

		return flag;
	}
}
