package com.intuit.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 *
 */
public class AsyncClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncClient.class);

    public static void main(final String[] args) throws Exception {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            HttpGet request = new HttpGet("http://www.apache.org/");
            Future<HttpResponse> future = httpclient.execute(request, null);
            final HttpGet request2 = new HttpGet("http://www.apache.org/");
            final CountDownLatch latch1 = new CountDownLatch(1);
            httpclient.execute(request2, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response2) {
                    latch1.countDown();
                    LOGGER.info(request2.getRequestLine() + "->" + response2.getStatusLine());
                }
                public void failed(final Exception ex) {
                    latch1.countDown();
                    LOGGER.info(request2.getRequestLine() + "->" + ex);
                }
                public void cancelled() {
                    latch1.countDown();
                    LOGGER.info(request2.getRequestLine() + " cancelled");
                }
            });
            latch1.await();
        } finally {
            httpclient.close();
        }
        LOGGER.info("Done");
    }

}
