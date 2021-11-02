package org.sslab.fabric.adapter;

import io.grpc.BindableService;
import org.corfudb.runtime.CorfuRuntime;
import org.sslab.fabric.chaincodeshim.contract.ContractRouter;
import org.sslab.fabric.chaincodeshim.contract.metadata.MetadataBuilder;
import org.sslab.fabric.corfu.CorfuAccess;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author capricorn116@postech.ac.kr
 *         created on 2021. 4. 08.
 */
public class AdapterServerRunner {

    static CorfuAccess corfu_access;
    static CorfuRuntime runtime;
    static ContractRouter cfc;
    private static Logger logger = Logger.getLogger(AdapterServerRunner.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {
//        runtime = getRuntimeAndConnect("141.223.121.251:12011");
        runtime = getRuntimeAndConnect("141.223.121.139:12011");
        corfu_access = new CorfuAccess();
        final int port = 54323;

        cfc = new ContractRouter(args);
        cfc.findAllContracts();

        logger.info(cfc.getRoutingRegistry().toString());

        final BindableService adapterService = new AdapterModuleService(corfu_access, runtime, cfc);

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
