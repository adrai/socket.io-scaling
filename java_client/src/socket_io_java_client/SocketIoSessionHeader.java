package socket_io_java_client;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class SocketIoSessionHeader {

	public static String URL;

	public static void main(String[] args) throws Exception {
		if (!validateProgammArguments(args))
			return;
		connect();
	}

	private static boolean validateProgammArguments(String[] args) {
		if (args.length<1) {
			System.err.println("to less arguments: use connection URL as program argument");
			return false;
		}
		URL = args[0];
		try {
			new URL(URL);
		} catch (MalformedURLException e) {
			System.err.println("connection URL \"" + URL + "\" is not valid");
			return false;
		}
		return true;
	}

	private static void connect() throws URISyntaxException, IOException {
		String sessionCookie = retrieveSessionCookie();
		SocketIoConnection socketIoConnection = new SocketIoConnection(sessionCookie);
		socketIoConnection.connect();
	}

	private static String retrieveSessionCookie() throws IOException {
		SessionCookie sessionCookie = new SessionCookie();
		return sessionCookie.retrieveSessionId();
	}

}
