/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.aplat;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * DO NOT USE IN PRODUCTION!!!!
 * 
 * This class will simply trust everything that comes along.
 * 
 * @author frank
 *
 */
public class TrustAllX509TrustManager implements X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {
    }

}