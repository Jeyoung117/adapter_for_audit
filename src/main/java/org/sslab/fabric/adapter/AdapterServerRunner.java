package org.sslab.fabric.adapter;

import io.grpc.BindableService;
import org.corfudb.runtime.CorfuRuntime;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.sslab.fabric.MSP.EnrollAdmin;
import org.sslab.fabric.MSP.RegisterUser;
import org.sslab.fabric.chaincodeshim.contract.ContractRouter;
import org.sslab.fabric.corfu.CorfuAccess;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author capricorn116@postech.ac.kr
 *         created on 2021. 4. 08.
 */
public class AdapterServerRunner {

    static CorfuAccess corfu_access;
    static CorfuRuntime runtime;
    static ContractRouter cfc;
    static EnrollAdmin enrollAdmin;
    static RegisterUser registerUser;
    private static Logger logger = Logger.getLogger(AdapterServerRunner.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {
        corfu_access = new CorfuAccess();
        final int port = 54323;

        cfc = new ContractRouter(args);
        cfc.findAllContracts();
//        try {
//            enrollAdmin.EnrollAdm();
//            registerUser.RegistAdapterModule();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
//
//        Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
//
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, "Chaincode_Adapter1").networkConfig(networkConfigPath).discovery(true);
//        Gateway gateway = builder.connect();
//        Network network = gateway.getNetwork("mychannel");


        logger.info(cfc.getRoutingRegistry().toString());

        final BindableService adapterService = new AdapterModuleService(corfu_access, cfc);

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
