package blog.example_JMS;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import javax.jms.TextMessage;

public class CarsKeeper {
	private ConnectionFactory cf;
	private Destination d;
	private Topic t;
	private Cars cars = new Cars();
	private ListenTopic listenTopic = null;
	private ListenQueue listenQueue = null;
	private final Object lock = new Object();
	String xsdPath="src/main/files/cars.xsd";

	public CarsKeeper() throws NamingException {
		System.out.println("Starting CarsKeeper...");
		this.cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.t = InitialContext.doLookup("jms/topic/TopicCars");
		this.d = InitialContext.doLookup("jms/queue/QueueExample");

		this.listenTopic = new ListenTopic(this, cf, t, lock);
		this.listenQueue = new ListenQueue(this, cf, d, lock);
	}


	public class ListenTopic extends Thread {
		CarsKeeper keeper;
		Topic topic;
		ConnectionFactory cf;
		Object lock;

		ListenTopic(CarsKeeper cK, ConnectionFactory c, Topic t, Object lock) {
			this.keeper = cK;
			this.topic = t;
			this.cf = c;
			this.lock = lock;
			start();
		}

		// receives msg from WebCrawler
		public void run() {
			try (JMSContext cntxt = this.cf.createContext("inesp", "123")) {
				cntxt.setClientID("CarsKeeper");
				JMSConsumer cons = cntxt.createDurableConsumer(t, "CarsKeeper");

				while(true) {
					// gets the xmlContents
					System.out.println("Waiting for the XML message from Topic....");
					TextMessage textMsg = (TextMessage) cons.receive();
					String xmlContents = textMsg.getBody(String.class);

					if (validateXSD(xmlContents)) {
						JAXBContext jaxbContext = JAXBContext.newInstance(Cars.class);
						Unmarshaller unmarshaller = (Unmarshaller) jaxbContext.createUnmarshaller();

						StringReader reader = new StringReader(xmlContents);

						synchronized (lock) {
							cars = new Cars();
							cars = (Cars) unmarshaller.unmarshal(reader);
						}
					}
				}

			} catch (JMSException | JAXBException e) {
				e.printStackTrace();
			}

		}

		public boolean validateXSD(String xmlContents){
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

	}
	
	public class ListenQueue extends Thread {
		CarsKeeper keeper;
		Destination d;
		ConnectionFactory cf;
		Object lock;

		ListenQueue(CarsKeeper cK, ConnectionFactory c, Destination d, Object lock) {
			
			this.keeper = cK;
			this.d = d;
			this.cf = c;
			this.lock = lock;
			start();
		}

		public void run() {
			try (JMSContext cntxt = this.cf.createContext("inesp", "123")) {
				JMSConsumer cons = cntxt.createConsumer(d);
				JMSProducer prod = cntxt.createProducer();


				while(true) {
					System.out.println("Waiting for a message in Queue...");

					// receives msg from the Keeper Queue.
					TextMessage textMsg = (TextMessage) cons.receive();
					Destination tempQueue = textMsg.getJMSReplyTo();
					String msg = textMsg.getBody(String.class);
					
					String reply = checkQueryUser(msg);

					// sends reply to temporary queue
					prod.send( tempQueue, reply);
					System.out.println("Answered to "+tempQueue.toString());
				}

			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		public String checkQueryUser(String query) {


			String reply="";
			String[] parts = query.split("\\|");

			String preco=parts[0];
			String anunciante=parts[1];
			String marca=parts[2];
			String modelo=parts[3];
			String versao=parts[4];
			String combustivel=parts[5];
			String quilometros=parts[6];
			String potencia=parts[7];
			String cilindrada=parts[8];
			String cor=parts[9];
			String lotacao=parts[10];

			synchronized (lock) {
				// compares with the information obtained
				for (int i=0;i<cars.getCar().size();i++) {

					Cars.Car currentCar = cars.getCar().get(i);
					if (!preco.equals("N/A") && (Integer.parseInt(currentCar.preco) > (Integer.parseInt(preco)))) {
						continue;
					}

					if (!anunciante.equals("N/A") && !(currentCar.anunciante.toLowerCase().contains(anunciante.toLowerCase()))) {
						continue;
					}

					if (!marca.equals("N/A") && !(currentCar.marca.toLowerCase().contains(marca.toLowerCase()))) {
						continue;
					}

					if (!modelo.equals("N/A") && !(currentCar.modelo.toLowerCase().contains(modelo.toLowerCase()))) {
						continue;
					}

					if (!versao.equals("N/A") && !(currentCar.versao.toLowerCase().contains(versao.toLowerCase()))) {
						continue;
					}

					if (!combustivel.equals("N/A") && !(currentCar.combustivel.toLowerCase().contains(combustivel.toLowerCase()))) {
						continue;
					}

					if (!quilometros.equals("N/A") && (Double.parseDouble(currentCar.quilometros) < Double.parseDouble(quilometros))) {
						continue;
					}

					if (!potencia.equals("N/A") && (Double.parseDouble(currentCar.potencia) < Double.parseDouble(potencia)) ) {
						continue;
					}

					if (!cilindrada.equals("N/A") && (Double.parseDouble(currentCar.cilindrada) < Double.parseDouble(cilindrada))) {
						continue;
					}

					if (!cor.equals("N/A") && !(currentCar.cor.toLowerCase().contains(cor.toLowerCase()))) {
						continue;
					}

					if (!lotacao.equals("N/A") && (Integer.parseInt(currentCar.lotacao) != Integer.parseInt(lotacao))) {
						continue;
					}

					reply = reply + printCarInfo(currentCar);
					continue;
				}

			}

			if (reply.length()==0)
				reply = "No matches found... try again?";

			return reply;
		}

		public String printCarInfo(Cars.Car car) {
			String reply = "-------------------- \n";
			reply = reply + "Preco: " + car.preco + "\nAnunciante: "+ car.anunciante + "\nMarca: " + car.marca + "\n"
					+ "Modelo: " + car.modelo + "\nVersão: "+ car.versao + "\nCombustível: " + car.combustivel + "\n"
					+ "Quilómetros: " + car.quilometros + "\nPotência: "+ car.potencia + "\nCilindrada: " + car.cilindrada + "\n"
					+ "Cor: "+ car.cor + "Lotação: "+ car.lotacao + "\n"; 
			reply= reply + "--------------------\n";

			return reply;
		}
	}
	
	public static void main(String[] args) throws NamingException, JMSException, JAXBException {
		CarsKeeper c = new CarsKeeper();

	}

}