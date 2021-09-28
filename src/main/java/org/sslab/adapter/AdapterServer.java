package org.sslab.adapter;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author homo.efficio@gmail.com
 *         created on 2021. 4. 08.
 */
public class AdapterServer {

    private final Logger logger = Logger.getLogger(AdapterServer.class.getName());

    private final int port;
    private final Server server;

    public AdapterServer(int port, BindableService service) throws IOException {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        logger.info("gRPC Server Listening on port " + port);
        this.server.awaitTermination();
    }

    public void shutdown() {
        System.err.println("gRPC 서버 종료..");
        server.shutdown();
        System.err.println("gRPC 서버 종료 완료");
    }

    public void blockUntilShutdown() throws InterruptedException {
        this.server.awaitTermination();
    }
}
