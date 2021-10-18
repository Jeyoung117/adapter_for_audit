package org.sslab.fabric.adapter;

import io.grpc.BindableService;
import org.corfudb.runtime.CorfuRuntime;
import org.sslab.fabric.corfu.CorfuAccess;

import java.io.IOException;

/**
 * @author capricorn116@postech.ac.kr
 *         created on 2021. 4. 08.
 */
public class AdapterServerRunner {

//    static CorfuAccess corfu_access = new CorfuAccess();
//    static CorfuRuntime runtime;

    public static void main(String[] args) throws IOException, InterruptedException {
//        runtime = getRuntimeAndConnect("141.223.121.251:13011");
//        corfu_access = new CorfuAccess();
        final int port = 54323;
        final BindableService adapterService = new AdapterModuleService();

        AdapterServer server = new AdapterServer(port, adapterService);

        server.start();
        server.blockUntilShutdown();
//        //server.awaitTermination();
//        Runtime.getRuntime().addShutdownHook(
//                new Thread(() -> server.shutdown())
//        );
    }

    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
        return corfuRuntime;
    }
}
