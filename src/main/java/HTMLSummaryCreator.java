package blog.example_JMS;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamSource; 
import javax.xml.transform.stream.StreamResult; 

import org.xml.sax.SAXException;

public class HTMLSummaryCreator {
	private ConnectionFactory cf;
	private Topic t;
	int nHTML=1;
	String xmlContents;

	String xsdPath="src/main/files/cars.xsd";
	String xslPath = "src/main/files/cars.xsl";
	String HTMLPath = "src/main/files/html/file";

	public HTMLSummaryCreator() {

	}

	public void tryTopic() throws NamingException, JMSException, JAXBException {
		for (int numTries=0;numTries<5;numTries++) {
			try {
				this.cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
				this.t = InitialContext.doLookup("jms/topic/TopicCars");
				System.out.println("Connection successful");
				break;
			} catch (NamingException n) {
				try {
					System.out.println("Topic is down. Attempting to reconnect...");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// receives msg from WebCrawler
	public void receiveTopic() throws JMSException, JAXBException, NamingException {
		while (true) {
			tryTopic();

			try (JMSContext cntxt = this.cf.createContext("inesp", "123")) {
				cntxt.setClientID("HTMLSummaryCreator");
				JMSConsumer cons = cntxt.createDurableConsumer(t, "CarsKeeper");

				System.out.println("[HTMLS] Waiting for the XML message from Topic....");

				// gets the xmlContents
				TextMessage textMsg = (TextMessage) cons.receive();
				xmlContents = textMsg.getBody(String.class);

				if (validateXSD()) {
					System.out.println("The XSD is valid! Creating files...");
					createHTML(HTMLPath+nHTML+".html");
					nHTML++;
				}

			}
		}
	}

	public void createHTML (String outFilename) {

		try {
			// Create transformer factory
			TransformerFactory factory = TransformerFactory.newInstance();

			// Use the factory to create a template containing the xsl file
			Templates template = factory.newTemplates(new StreamSource(new FileInputStream(xslPath)));

			// Use the template to create a transformer
			Transformer xformer = template.newTransformer();

			// Prepare the input and output files

			InputStream stream = new ByteArrayInputStream(xmlContents.getBytes(StandardCharsets.UTF_8.name()));

			Source source = new StreamSource(stream);
			Result result = new StreamResult(new FileOutputStream(outFilename));

			// Apply the xsl file to the source file and write the result to the output file
			xformer.transform(source, result);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (TransformerConfigurationException e) {
			System.out.println("Error in the XSL File.");
		} catch (TransformerException e) {
			// An error occurred while applying the XSL file
			// Get location of error in input file
			SourceLocator locator = e.getLocator();
			int col = locator.getColumnNumber();
			int line = locator.getLineNumber();
			String publicId = locator.getPublicId();
			String systemId = locator.getSystemId();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean validateXSD(){
		try {
			SchemaFactory factory =
					SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = schema.newValidator();

			InputStream stream = new ByteArrayInputStream(xmlContents.getBytes(StandardCharsets.UTF_8.name()));
			validator.validate(new StreamSource(stream));
		} catch (IOException e){
			System.out.println("Exception: "+e.getMessage());
			return false;
		}catch(SAXException e1){
			System.out.println("SAX Exception: "+e1.getMessage());
			return false;
		}
		return true;
	}


	public static void main(String[] args) throws JMSException, JAXBException, NamingException {
		HTMLSummaryCreator hSC = new HTMLSummaryCreator();
		hSC.receiveTopic();
	}

}