package socket_io_java_client;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SessionCookie {

	public String retrieveSessionId() throws IOException {
		HttpURLConnection con = null;
		try {
			con = sendHttpRequest();
			validateHttpResponse(con);
			return parseCookieFromResponse(con);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	private HttpURLConnection sendHttpRequest() throws MalformedURLException,
			IOException, ProtocolException {
		HttpURLConnection con;
		URL url = new URL(SocketIoSessionHeader.URL);
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		return con;
	}

	private void validateHttpResponse(HttpURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		if (responseCode != 204) {
			throw new RuntimeException("wrong http response code: "
					+ responseCode);
		}
	}

	private String parseCookieFromResponse(HttpURLConnection con) {
		String cookieHeaderField = con.getHeaderField("Set-Cookie");
		if (cookieHeaderField == null
				|| !cookieHeaderField.startsWith("JSESSIONID=")) {
			throw new RuntimeException("cookie not set in http response ");
		}
		return cookieHeaderField;
	}

}
