package org.sslab.fabric.adapter;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.*;
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
//        ExecutorService executor = new ThreadPoolExecutor(129, Integer.MAX_VALUE,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>(),
//                new ThreadFactoryBuilder()
//                        .setDaemon(true)
//                        .setNameFormat("Glowroot-IT-Harness-GRPC-Executor-%d")
//                        .build());

        ExecutorService executor = Executors.newFixedThreadPool(129);

        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(service)
                .executor(executor)
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
