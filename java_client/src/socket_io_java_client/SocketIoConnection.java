package socket_io_java_client;


import java.net.URISyntaxException;
import java.util.Map;

import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

public class SocketIoConnection {

	private static final String SOCKET_IO_CHANNEL_NAME = "data";
	
	private Socket socket;
	private String sessionId;

	public SocketIoConnection(String sessionId) throws URISyntaxException {
		this.sessionId = sessionId;
		this.socket = IO.socket(SocketIoSessionHeader.URL);
		setRequestHeaders();
		registerOnConnectListener();
		registerOnDataListener();
	}
	
	public void connect() {
		this.socket.connect();
	}

	private void setRequestHeaders() {
		this.socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Transport transport = (Transport) args[0];
				System.out.println( "Transport: " + transport.getClass().getSimpleName() );
				transport.on(Transport.EVENT_REQUEST_HEADERS,
						new Emitter.Listener() {
							@Override
							public void call(Object... args) {
								@SuppressWarnings("unchecked")
								Map<String, String> headers = (Map<String, String>) args[0];
								headers.put("Cookie", sessionId);
							}
						});
			}
		});
	}

	private void emitMessage(String message) {
		this.socket.emit(SOCKET_IO_CHANNEL_NAME, message);
	}
	
	private void registerOnDataListener() {
		this.socket.on(SOCKET_IO_CHANNEL_NAME, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				if (args[0] instanceof JSONObject) {
					JSONObject datagram = (JSONObject) args[0];
					System.out.println("receiving: " + datagram);
				}
				if (args[0] instanceof String) {
					System.out.println("receiving: " + args[0]);
				}
			}
		});
	}
	
	private void registerOnConnectListener() {
		this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
	        @Override
	        public void call(Object... args) {
	        	new Thread() {
	        		public void run() {
	        			for (int i=0; i<10; i++) {
	        				String message = "message_" + i;
	        				System.out.println("sending: " + message);
	        				emitMessage( message );
	        	    		try {
	        					Thread.sleep(1000);
	        				} catch (InterruptedException e) {
	        					e.printStackTrace();
	        				}
	        	    	}
	        		};
	        	}.start();
	        }
	    });
	}
}