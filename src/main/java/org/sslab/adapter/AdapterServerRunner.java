package org.sslab.adapter;

import io.grpc.BindableService;

import java.io.IOException;

/**
 * @author capricorn116@postech.ac.kr
 *         created on 2021. 4. 08.
 */
public class AdapterServerRunner {

    public static void main(String[] args) throws IOException, InterruptedException {

        final int port = 54323;
        final BindableService adapterService = (BindableService) new AdapterModuleService();

        AdapterServer server = new AdapterServer(port, adapterService);

        server.start();
        server.blockUntilShutdown();
//        //server.awaitTermination();
//        Runtime.getRuntime().addShutdownHook(
//                new Thread(() -> server.shutdown())
//        );
    }
}
