package org.httpclient;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class lesson03 {

	public static void main(String[] args) throws Exception{
		HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet("http://www.apache.org/");

            // Execute HTTP request
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println("----------------------------------------");

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    instream.read();
                    // do something useful with the response
                } catch (IOException ex) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw ex;
                } catch (RuntimeException ex) {
                    // In case of an unexpected exception you may want to abort
                    // the HTTP request in order to shut down the underlying
                    // connection immediately.
                    httpget.abort();
                    throw ex;
                } finally {
                    // Closing the input stream will trigger connection release
                    try { instream.close(); } catch (Exception ignore) {}
                }
            }

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
	}
}
