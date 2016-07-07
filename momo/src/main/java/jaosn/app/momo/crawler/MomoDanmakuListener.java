package jaosn.app.momo.crawler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.IO.Options;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

/**
 * Hello world!
 *
 */
public class MomoDanmakuListener {
	private static Socket socket;

	public static void main(String[] args) throws URISyntaxException {
		HostnameVerifier myHostnameVerifier = new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				// TODO Auto-generated method stub
				return true;
			}

		};
		SSLContext mySSLContext = createSSLContext();
		// default settings for all sockets
		IO.setDefaultSSLContext(mySSLContext);
		IO.setDefaultHostnameVerifier(myHostnameVerifier);

		// set as an option
		Options opts = new IO.Options();
		opts.sslContext = mySSLContext;
		opts.hostnameVerifier = myHostnameVerifier;

		socket = IO.socket("https://web-ws.immomo.com", opts);
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("connected ");

			}

		}).on(Socket.EVENT_CONNECTING, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("connecting" + args);
			}
		}).on("client_ready", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("client_ready" + args);

			}

		}).on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("EVENT_RESPONSE_HEADERS" + args);
			}

		}).on("setmsg", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("EVENT_RESPONSE_HEADERS" + args[0]);
			}

		}).on("cmsg", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("cmsg" + args[0]);
			}

		}).on("needConfig", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("needConfig" + args);
				socket.emit("sconfig", "{\"cId\":\"56669065381604\",\"liveId\":\"1460046081626\"}");
			}

		}).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("connect error " + args);
			}

		}).on(Socket.EVENT_ERROR, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("error " + args[0]);
			}

		}).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("EVENT_MESSAGE " + args);
			}

		}).on("setmsg", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("setmsg " + args);
			}

		}).on(Socket.EVENT_PONG, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("pong " + args);
			}

		}).on(Socket.EVENT_PING, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("ping " + args);
			}

		}).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("* " + args);
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("disconnect " + args[0]);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
			  @Override
			  public void call(Object... args) {
			    Transport transport = (Transport)args[0];
			    transport.on(Transport.EVENT_OPEN, new Emitter.Listener() {
				      @Override
				      public void call(Object... args) {
				    	  System.out.println("event open " + args);
				        @SuppressWarnings("unchecked")
				        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
				        // modify request headers
				        headers.put("Cookie", Arrays.asList("foo=1;"));
				      }
				    });
			    
			    
			    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
			      @Override
			      public void call(Object... args) {
			    	  System.out.println("event request header " + args);
			        @SuppressWarnings("unchecked")
			        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
			        // modify request headers
			        headers.put("Cookie", Arrays.asList("foo=1;"));
			      }
			    });

			    transport.on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
			      @Override
			      public void call(Object... args) {
			        @SuppressWarnings("unchecked")
			        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
			        // access response headers
			        String cookie = headers.get("Set-Cookie").get(0);
			      }
			    });
			  }
			});
		socket.io().on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				System.out.println("EVENT_RESPONSE_HEADERS" + args);
			}

		});
		socket.connect();

	}

	private static SSLContext createSSLContext() {
		// TODO Auto-generated method stub
		final char[] JKS_PASSWORD = "XXXXX".toCharArray();
		final char[] KEY_PASSWORD = "XXXXX".toCharArray();
		try {
			/* Get the JKS contents */
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			try (final InputStream is = new FileInputStream(fullPathOfKeyStore())) {
				keyStore.load(is, JKS_PASSWORD);
			}
			final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, KEY_PASSWORD);
			final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);

			/*
			 * Creates a socket factory for HttpsURLConnection using JKS
			 * contents
			 */
			final SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
			// final SSLSocketFactory socketFactory = sc.getSocketFactory();
			// HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
			return sc;
		} catch (final GeneralSecurityException | IOException exc) {
			throw new RuntimeException(exc);
		}
	}

	private static String fullPathOfKeyStore() {
		// TODO Auto-generated method stub
		return "replace with you own keystore xxx.jks ";
	}
}
