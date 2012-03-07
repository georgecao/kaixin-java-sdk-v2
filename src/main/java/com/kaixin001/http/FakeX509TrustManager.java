package com.kaixin001.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class FakeX509TrustManager implements X509TrustManager {
    private static final Logger LOG = LoggerFactory.getLogger(FakeX509TrustManager.class);
    private static final TrustManager[] TRUST_MANAGERS = new TrustManager[]{new FakeX509TrustManager()};
    private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return ACCEPTED_ISSUERS;
    }

    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, TRUST_MANAGERS, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error occurred", e);
        } catch (KeyManagementException e) {
            LOG.error("Error occurred", e);
        }
    }
}
