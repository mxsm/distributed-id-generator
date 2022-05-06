package com.github.mxsm.uid.client;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpConnection;
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
import org.apache.hc.core5.http2.frame.RawFrame;
import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
import org.apache.hc.core5.http2.impl.nio.bootstrap.H2RequesterBootstrap;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

/**
 * @author mxsm
 * @date 2022/5/4 22:08
 * @Since 1.0.0
 */
public class H2MultiStreamExecutionExample {
    public static void main(final String[] args) throws Exception {

        // Create and start requester
        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
            .setSoTimeout(5, TimeUnit.SECONDS)
            .build();

        final H2Config h2Config = H2Config.custom()
            .setPushEnabled(false)
            .setMaxConcurrentStreams(100)
            .build();

        final HttpAsyncRequester requester = H2RequesterBootstrap.bootstrap()
            .setIOReactorConfig(ioReactorConfig)
            .setVersionPolicy(HttpVersionPolicy.NEGOTIATE)
            .setH2Config(h2Config)
            .setStreamListener(new H2StreamListener() {

                @Override
                public void onHeaderInput(final HttpConnection connection, final int streamId, final List<? extends Header> headers) {
                    for (int i = 0; i < headers.size(); i++) {
                        System.out.println(connection.getRemoteAddress() + " (" + streamId + ") << " + headers.get(i));
                    }
                }

                @Override
                public void onHeaderOutput(final HttpConnection connection, final int streamId, final List<? extends Header> headers) {
                    for (int i = 0; i < headers.size(); i++) {
                        System.out.println(connection.getRemoteAddress() + " (" + streamId + ") >> " + headers.get(i));
                    }
                }

                @Override
                public void onFrameInput(final HttpConnection connection, final int streamId, final RawFrame frame) {
                }

                @Override
                public void onFrameOutput(final HttpConnection connection, final int streamId, final RawFrame frame) {
                }

                @Override
                public void onInputFlowControl(final HttpConnection connection, final int streamId, final int delta, final int actualSize) {
                }

                @Override
                public void onOutputFlowControl(final HttpConnection connection, final int streamId, final int delta, final int actualSize) {
                }

            })
            .create();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("HTTP requester shutting down");
                requester.close(CloseMode.GRACEFUL);
            }
        });
        requester.start();

       // final HttpHost target = new HttpHost("nghttp2.org");
        final HttpHost target = new HttpHost("192.168.3.21:8080");
        //final String[] requestUris = new String[] {"/httpbin/ip", "/httpbin/user-agent", "/httpbin/headers"};
        final String[] requestUris = new String[] {"/api/v1/segment/uid/VHHpwr80O9HXVv27GKLsShDH119251376"};

        final Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(5));
        final AsyncClientEndpoint clientEndpoint = future.get();

        final CountDownLatch latch = new CountDownLatch(requestUris.length);
        for (final String requestUri: requestUris) {
            clientEndpoint.execute(
                AsyncRequestBuilder.get()
                    .setHttpHost(target)
                    .setPath(requestUri)
                    .build(),
                new BasicResponseConsumer<>(new StringAsyncEntityConsumer()),
                new FutureCallback<Message<HttpResponse, String>>() {

                    @Override
                    public void completed(final Message<HttpResponse, String> message) {
                        latch.countDown();
                        final HttpResponse response = message.getHead();
                        final String body = message.getBody();
                        System.out.println(requestUri + "->" + response.getCode());
                        System.out.println(body);
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        System.out.println(requestUri + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        System.out.println(requestUri + " cancelled");
                    }

                });
        }

        latch.await();

        // Manually release client endpoint when done !!!
        clientEndpoint.releaseAndDiscard();

        System.out.println("Shutting down I/O reactor");
        requester.initiateShutdown();
    }
}
