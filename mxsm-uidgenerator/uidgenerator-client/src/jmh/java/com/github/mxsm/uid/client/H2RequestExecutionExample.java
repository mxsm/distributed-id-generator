package com.github.mxsm.uid.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncRequester;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.hc.core5.http2.impl.nio.bootstrap.H2RequesterBootstrap;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

/**
 * @author mxsm
 * @date 2022/5/4 22:18
 * @Since 1.0.0
 */
public class H2RequestExecutionExample {
    public static void main(final String[] args) throws Exception {

        // Create and start requester
        final H2Config h2Config = H2Config.custom()
            .setPushEnabled(false)
            .build();

        final HttpAsyncRequester requester = H2RequesterBootstrap.bootstrap()
            .setH2Config(h2Config)
            .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_2)
           // .setIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(50).build())
            .create();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("HTTP requester shutting down");
                requester.close(CloseMode.GRACEFUL);
            }
        });
        requester.start();

        final HttpHost target = new HttpHost("172.29.250.21",8080);
        final String[] requestUris = new String[] {"/api/v1/segment/uid/VHHpwr80O9HXVv27GKLsShDH119251376"};

        final CountDownLatch latch = new CountDownLatch(requestUris.length);
        for (final String requestUri: requestUris) {
            final Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(5));
            final AsyncClientEndpoint clientEndpoint = future.get();
            clientEndpoint.execute(
                AsyncRequestBuilder.get()
                    .setHttpHost(target)
                    .setPath(requestUri)
                    .build(),
                new BasicResponseConsumer<>(new StringAsyncEntityConsumer()),
                new FutureCallback<Message<HttpResponse, String>>() {

                    @Override
                    public void completed(final Message<HttpResponse, String> message) {
                        clientEndpoint.releaseAndReuse();
                        final HttpResponse response = message.getHead();
                        final String body = message.getBody();
                        System.out.println(requestUri + "->" + response.getCode());
                        System.out.println(body);
                        latch.countDown();
                    }

                    @Override
                    public void failed(final Exception ex) {
                        clientEndpoint.releaseAndDiscard();
                        System.out.println(requestUri + "->" + ex);
                        latch.countDown();
                    }

                    @Override
                    public void cancelled() {
                        clientEndpoint.releaseAndDiscard();
                        System.out.println(requestUri + " cancelled");
                        latch.countDown();
                    }

                });
        }

        latch.await();
        System.out.println("Shutting down I/O reactor");
        requester.initiateShutdown();
    }
}
