package blog.example_JMS;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CarsRequester {
	private ConnectionFactory cf;
	private Destination keeperQueue;
	private TemporaryQueue tempQueue;

	String queryUser;

	public CarsRequester() throws NamingException {
		this.cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
		this.keeperQueue = InitialContext.doLookup("jms/queue/QueueExample");
	}

	// sends query to Keeper (in KeeperQueue)
	public void sendKeeperQueue() throws JMSException {

		try (JMSContext cntx = this.cf.createContext("inesp", "123")) {
			
			JMSProducer prod = cntx.createProducer();

			while (true) {

				// sends TextMessage to KeeperQueue 
				tempQueue = cntx.createTemporaryQueue();
				TextMessage textMsg = cntx.createTextMessage(getQueryUser());
				textMsg.setJMSReplyTo(tempQueue);
				prod.send(keeperQueue, textMsg);

				System.out.println("Just sent message, expect in "+tempQueue.toString());

				JMSConsumer cons = cntx.createConsumer(tempQueue);
				String msg = cons.receiveBody(String.class);
				System.out.println("Just received "+msg);
			}
		}

	}

	public String gQU() {
		//return "50000000|N/A|audi|a3|1.2|Gasolina|91047|105|1197|azul|4|";
		return "50000000|a|a|a|a|a|0|0|0|a|N/A|";
	}

	public String getQueryUser() {
		//String preco, anunciante, marca, modelo, versao, combustivel, quilometros, potencia, cilindrada, cor, lotacao = null;

		String[] query = new String[11];

		Scanner sc = new Scanner(System.in);
		System.out.println("Preço?");
		query[0] = sc.nextLine();
		System.out.println("Anunciante?");
		query[1] = sc.nextLine();
		System.out.println("Marca?");
		query[2] = sc.nextLine();
		System.out.println("Modelo?");
		query[3] = sc.nextLine();
		System.out.println("Versão?");
		query[4] = sc.nextLine();
		System.out.println("Combustivel?");
		query[5] = sc.nextLine();
		System.out.println("Quilómetros");
		query[6] = sc.nextLine();
		System.out.println("Potência?");
		query[7] = sc.nextLine();
		System.out.println("Cilindrada?");
		query[8] = sc.nextLine();
		System.out.println("Cor?");
		query[9] = sc.nextLine();
		System.out.println("Lotação?");
		query[10] = sc.nextLine();

		String msg= "";
		for (int i=0;i<query.length;i++) {
			// todos os campos vazios ficam a "N/A"
			if (query[i].length()==0) {
				query[i] = "N/A";
			}
			msg = msg + query[i] + "|";
		}

		return msg;
	}

	public static void main(String[] args) throws NamingException, JMSException {
		CarsRequester cr = new CarsRequester();
		cr.sendKeeperQueue();
	}

}