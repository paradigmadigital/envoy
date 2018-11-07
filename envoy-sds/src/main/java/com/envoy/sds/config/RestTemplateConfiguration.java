package com.envoy.sds.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * For now we are going to skip certificate validation, but you should check certificate
 * 
 * @author jarodriguez
 *
 */
@Component
public class RestTemplateConfiguration {

	private static final TrustStrategy INSECURE_TRUST_STRATEGY = new TrustStrategy() {
		public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {return true;}
	}; 
	
	@Bean
	public RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		
		SSLContextBuilder contextBuilder = new SSLContextBuilder();
	    contextBuilder.loadTrustMaterial(null, INSECURE_TRUST_STRATEGY);
	    
	    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(contextBuilder.build());
	    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    requestFactory.setHttpClient(httpClient);
	    
	    return new RestTemplate(requestFactory);
	}	
}
