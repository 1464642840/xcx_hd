package com.hxh.basic.project.utils.http;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.impl.cookie.NetscapeDraftSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SSLClient {

	private static PoolingHttpClientConnectionManager cm = null;
	private static Map<String, CloseableHttpClient> maps = new ConcurrentHashMap<String, CloseableHttpClient>();

	/**
	 * 
	 * @return
	 */

	public static CloseableHttpClient getHttpsClient() {

		PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
				.getDefault();

		CookieSpecProvider provider = new NetscapeDraftSpecProvider() {

			@Override
			public CookieSpec create(HttpContext context) {
				// TODO Auto-generated method stub
				return new DefaultCookieSpec();

			}
		};
		Registry<CookieSpecProvider> r = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.STANDARD_STRICT,
						new RFC6265CookieSpecProvider(publicSuffixMatcher))
				.register(CookieSpecs.NETSCAPE, provider).build();
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.setCookieSpec(CookieSpecs.NETSCAPE).build();
		Registry<ConnectionSocketFactory> regist = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", trustsAllHttpsCertificates()).build();
		cm = new PoolingHttpClientConnectionManager(regist);
		cm.setDefaultMaxPerRoute(2);
		cm.setMaxTotal(200);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(cm).setDefaultCookieSpecRegistry(r)
				.setRedirectStrategy(new LaxRedirectStrategy())
				.setRetryHandler(new HttpRequestRetryHandler() {

					@Override
					public boolean retryRequest(IOException arg0, int arg1,
                                                HttpContext arg2) {
						if (arg1 >= 3) {
							return false;
						}
						return true;

					}
				}).setDefaultRequestConfig(requestConfig).build();
		return httpclient;

	}

	/**
	 * 跳过证书验证类
	 * 
	 * @return
	 */

	public static SSLConnectionSocketFactory trustsAllHttpsCertificates() {

		SSLConnectionSocketFactory ssl = null;
		TrustManager[] trust = new TrustManager[1];
		TrustManager tm = new miTM();
		trust[0] = tm;
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLS");// sc =
												// SSLContext.getInstance("TLS")
			sc.init(null, trust, null);
			ssl = new SSLConnectionSocketFactory(sc,
					NoopHostnameVerifier.INSTANCE);
			// HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return ssl;
	}

	public static class miTM implements TrustManager, X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static CloseableHttpClient getHttpsClients(String s) {
		CloseableHttpClient clinet = getHttpsClient();
		maps.put(s, clinet);
		return clinet;

	}

	public CloseableHttpClient getClient(String s) {

		if (maps.containsKey(s)) {
			return maps.get(s);
		} else {
			return null;
		}

	}

	public void removeClient(String s) {

		if (maps.containsKey(s)) {

			maps.remove(s);

		}

	}

}
