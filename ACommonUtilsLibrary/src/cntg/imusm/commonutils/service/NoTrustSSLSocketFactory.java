package cntg.imusm.commonutils.service;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


// for bypassing expired or invalid certificates
public class NoTrustSSLSocketFactory implements SocketFactory, LayeredSocketFactory {
	private SSLContext sslcontext = null;
	private static NoTrustSSLSocketFactory factory = new NoTrustSSLSocketFactory();

	private NoTrustSSLSocketFactory() {
		super();
	}

	public static NoTrustSSLSocketFactory getFactory() {
		return factory;
	}

	private SSLContext getSSLContext() throws IOException {
		if (this.sslcontext == null) {
			try {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, new TrustManager[] {
						new X509TrustManager() {
							public X509Certificate[] getAcceptedIssuers() { return null; }
							public void checkServerTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {}
							public void checkClientTrusted(X509Certificate[] chain, String authType)
									throws CertificateException {}
						}
				}, new SecureRandom());
				this.sslcontext = context;
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
		}
		return this.sslcontext;
	}

	public Socket connectSocket(Socket arg0, String arg1, int arg2, InetAddress arg3, int arg4, HttpParams arg5) throws IOException, UnknownHostException, ConnectTimeoutException {
		int connTimeout = HttpConnectionParams.getConnectionTimeout(arg5);
		int soTimeout = HttpConnectionParams.getSoTimeout(arg5);

		InetSocketAddress remoteAddress = new InetSocketAddress(arg1, arg2);
		SSLSocket sslsock = (SSLSocket) ((arg0 != null) ? arg0 : createSocket());

		if ((arg3 != null) || (arg4 > 0)) {
			if (arg4 < 0) {
				arg4 = 0;
			}
			InetSocketAddress isa = new InetSocketAddress(arg3, arg4);
			sslsock.bind(isa);
		}

		sslsock.connect(remoteAddress, connTimeout);
		sslsock.setSoTimeout(soTimeout);
		return sslsock;
	}

	public Socket createSocket() throws IOException {
		return getSSLContext().getSocketFactory().createSocket();
	}

	public boolean isSecure(Socket sock) throws IllegalArgumentException {
		return true;
	}

	public Socket createSocket(Socket arg0, String arg1, int arg2,
			boolean arg3) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}
}
