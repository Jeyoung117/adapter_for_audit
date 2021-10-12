package org.sslab.fabric.adapter;

import io.grpc.BindableService;
import org.corfudb.runtime.CorfuRuntime;
import org.sslab.fabric.corfu.Corfu_access;

import java.io.IOException;

/**
 * @author capricorn116@postech.ac.kr
 *         created on 2021. 4. 08.
 */
public class AdapterServerRunner {

    static CorfuRuntime runtime;
    static Corfu_access corfu_access;

    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
        return corfuRuntime;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        runtime = getRuntimeAndConnect("141.223.121.251:12011");
        corfu_access = new Corfu_access(runtime);
        final int port = 54323;

        final BindableService adapterService =  new AdapterModuleService(corfu_access);
        AdapterServer server = new AdapterServer(port, adapterService);

        server.start();
        server.blockUntilShutdown();
//        //server.awaitTermination();
//        Runtime.getRuntime().addShutdownHook(
//                new Thread(() -> server.shutdown())
//        );
    }
}
