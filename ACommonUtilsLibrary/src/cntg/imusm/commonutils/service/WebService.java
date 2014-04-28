package cntg.imusm.commonutils.service;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class WebService {
	
	/**
	 * createHttpClient (support https)
	 * @return
	 */
	public static DefaultHttpClient createHttpClient() {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", NoTrustSSLSocketFactory
				.getFactory(), 443));

		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(30));
		params.setParameter(ConnManagerPNames.TIMEOUT, 30000L);
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);

		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpConnectionParams.setConnectionTimeout(params, 50000);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);

		return new DefaultHttpClient(cm, params);
	}
}
