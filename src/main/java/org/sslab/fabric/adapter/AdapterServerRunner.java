package org.sslab.fabric.adapter;

import io.grpc.BindableService;
import org.corfudb.runtime.CorfuRuntime;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.impl.NetworkImpl;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.transaction.TransactionContext;
import org.sslab.fabric.MSP.EnrollAdmin;
import org.sslab.fabric.MSP.RegisterUser;
import org.sslab.fabric.MSP.Signer;
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
    static ContractRouter cfc;
    static EnrollAdmin enrollAdmin = new EnrollAdmin();
    static RegisterUser registerUser = new RegisterUser();
    static Signer signer;
    private static Logger logger = Logger.getLogger(AdapterServerRunner.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        corfu_access = new CorfuAccess();
        final int port = 54323;

        cfc = new ContractRouter(args);
        cfc.findAllContracts();
        try {
            enrollAdmin.EnrollAdm();
            registerUser.RegistAdapterModule();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        //fabric test network 경로
        Path networkConfigPath = Paths.get("..", "..", "hyperledger", "fabric-testnets", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.json");
//        Path networkConfigPath = Paths.get("..", "..",  "..", "edgechain", "bsp_210817_base", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.json");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "woochang2").networkConfig(networkConfigPath).discovery(true);
        Gateway gateway = builder.connect();
        NetworkImpl network = (NetworkImpl) gateway.getNetwork("mychannel");


        logger.info(cfc.getRoutingRegistry().toString());
        Channel channel = network.getChannel();

        signer = new Signer(channel, network.getGateway().getClient().getUserContext(), network.getGateway().getClient().getCryptoSuite());
        System.out.println("client의 getCryptoSuite: " + network.getGateway().getClient().getCryptoSuite());
        System.out.println("signer의 channelid: " + signer.getChannelID());
        System.out.println("signer의 signingIdentity: " + signer.signingIdentity);
        System.out.println("signer의 getidentity: " + signer.getIdentity());
        System.out.println("signer의 getidentity: " + signer.getSerializedIdentity());
        final BindableService adapterService = new AdapterModuleService(corfu_access, cfc, signer);

        AdapterServer server = new AdapterServer(port, adapterService);

        server.start();
        server.blockUntilShutdown();
        //server.awaitTermination();
    }

    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
        CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
        return corfuRuntime;
    }
}

