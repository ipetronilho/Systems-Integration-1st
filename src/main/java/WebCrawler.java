package blog.example_JMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.xml.parsers.DocumentBuilder;


import java.io.File;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
/**
 * Hello world!
 *
 */
public class WebCrawler 
{

	Cars cars = new Cars();

	Document HTMLdocument;
	String stringURL = "https://www.standvirtual.com/destaques/";
	String appendURL = "?page=";

	ArrayList <Cars.Car> car = new ArrayList<>();

	
	private String XMLPath = "src/main/files/car.xml";
	File XMLFile = new File(XMLPath);

	Elements car_labels;
	Elements car_values;

	private ConnectionFactory cf;
	private Destination d;

	public WebCrawler() throws InterruptedException, IOException, JAXBException {
		while(true) {
			if (!XMLFile.exists())
				crawl();
			

			for (int numTries=0;numTries<5;numTries++) {
				try {
					// tries to connect to Topic
					this.cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
					this.d = InitialContext.doLookup("jms/topic/TopicCars");

					publishTopic();
					break;
				} catch (NamingException e) {
					System.out.println("Can't connect to Topic.");
					if (!XMLFile.exists()) {
						// first try and unsuccessful: creates XML file
						createXMLFile();
						cars = new Cars();
					}
					Thread.sleep(10000);
				}
			}
			Thread.sleep(10000);
		}
	}

	// transforms "1 250 cm3" into "1250" for example
	public String checkQuality(String text) {
		String[] parts = text.split(" ");
		String result = "";
		for (int i=0;i<parts.length;i++) {
			if (parts[i].matches("[0-9]+") && parts[i].length()>0) {
				result = result + parts[i];
			}
		}
		return result;
	}
	

	public int getNumberPages() {
		Elements els = HTMLdocument.select(".page");
		int nPages=-1;
		// for each car
		System.out.println("Fetching info on cars...");
		for (Element el: els) {
			try {
				nPages = Integer.parseInt(el.text());
			} catch(NumberFormatException ne) {

			}
		}
		return nPages;
	}

	public void crawl() throws IOException {
		System.out.println("Fetching HTML document...");
		
		HTMLdocument = Jsoup.connect(stringURL).get();

		int numberPages = getNumberPages();

		for (int i=1;i<numberPages+1;i++) {
			String URL = stringURL+appendURL+i;
			System.out.println("Am scanning "+URL);
			HTMLdocument = Jsoup.connect(URL).get();


			Elements els = HTMLdocument.select(".details");
			Elements links = els.select("a[href]"); 
			
			try {
				
				System.out.println("Fetching info on cars...");
				for (Element link : links) {

					Document docCars = Jsoup.connect(link.attr("href")).get();

					// info das qualidades (Marca, Cor) e dos valores (Volvo, amarelo)
					Elements car_labels = docCars.select(".offer-params__label");
					Elements car_values = docCars.select(".offer-params__value");
					Elements price = docCars.select(".offer-price__number");

					getInfoCar(car_labels, car_values, price.text());

				}
			} catch (IOException e) {

				e.printStackTrace();
			}

			System.out.println("Done!");
		}

	} 
	
	public Cars.Car initialize(Cars.Car oneCar) {
		oneCar.setPreco("");
		oneCar.setAnunciante("");
		oneCar.setMarca("");
		oneCar.setModelo("");
		oneCar.setVersao("");
		oneCar.setCombustivel("");
		oneCar.setQuilometros("");
		oneCar.setPotencia("");
		oneCar.setCilindrada("");
		oneCar.setCor("");
		oneCar.setLotacao("");
		return oneCar;
	}


	public void getInfoCar(Elements car_labels, Elements car_values, String price) {
		int i;

		Cars.Car oneCar = new Cars.Car();
		oneCar = initialize(oneCar);
		oneCar.setPreco(checkQuality(price));

		for (i=0;i<car_labels.size();i++) {
			if (car_labels.get(i).text().compareTo("Anunciante")==0) {
				oneCar.setAnunciante(car_values.get(i).text());
			}
			else if (car_labels.get(i).text().compareTo("Marca")==0) {
				oneCar.setMarca(car_values.get(i).text());
			}
			else if (car_labels.get(i).text().compareTo("Modelo")==0) {
				oneCar.setModelo(car_values.get(i).text());
			}
			else if (car_labels.get(i).text().compareTo("Versão")==0) {
				oneCar.setVersao(car_values.get(i).text());
			}
			else if (car_labels.get(i).text().compareTo("Combustível")==0) {
				oneCar.setCombustivel(car_values.get(i).text());
			}
			else if (car_labels.get(i).text().compareTo("Quilómetros")==0) { 
				oneCar.setQuilometros(checkQuality(car_values.get(i).text()));
			}
			else if (car_labels.get(i).text().compareTo("Potência")==0) {
				oneCar.setPotencia(checkQuality(car_values.get(i).text()));
			}
			else if (car_labels.get(i).text().compareTo("Cilindrada")==0) {
				oneCar.setCilindrada(checkQuality(car_values.get(i).text()));
			}
			else if (car_labels.get(i).text().compareTo("Cor")==0) {
				oneCar.setCor(car_values.get(i).text());
			}
			else if (car_labels.get(i).text().compareTo("Lotação")==0) {
				oneCar.setLotacao(checkQuality(car_values.get(i).text()));
			}
		}

		cars.getCar().add(oneCar);
	}

	
	 
	// create XML file with the contents it is going to publish
	public void createXMLFile() throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Cars.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		jaxbMarshaller.marshal(cars, new File(XMLPath));
	}

	// sends message containing the XML document to the JMS topic
	public void publishTopic() throws NamingException, JAXBException, IOException, InterruptedException {

		System.out.println("Sending msg to Topic...");

		JMSContext cntx = this.cf.createContext("inesp", "123");

		JAXBContext jaxbContext = JAXBContext.newInstance(Cars.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		StringWriter sW = new StringWriter();
		if (XMLFile.exists()) {
			FileReader fr = new FileReader(XMLPath);
			BufferedReader br = new BufferedReader(fr);

			String currentLine="";
			String xmlString="";

			while ((currentLine = br.readLine()) != null) {
				System.out.println(currentLine);
				xmlString = xmlString + currentLine;
			}

			br.close();
			fr.close();

			// send msg with the file
			TextMessage textMsg = cntx.createTextMessage(xmlString);
			JMSProducer prod = cntx.createProducer();
			prod.send(d, textMsg);

			XMLFile.delete();
		}

		else {
			jaxbMarshaller.marshal(cars, sW);
			TextMessage textMsg = cntx.createTextMessage(sW.toString());
			JMSProducer prod = cntx.createProducer();
			prod.send(d, textMsg);
		}

		System.out.println("Done!");


	}

	public static void main(String[] args) throws JAXBException, NamingException, IOException, InterruptedException{
		WebCrawler wb = new WebCrawler();
	}
}
