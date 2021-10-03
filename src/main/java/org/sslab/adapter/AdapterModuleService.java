package org.sslab.adapter;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.corfudb.protocols.wireprotocol.Token;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.stream.IStreamView;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.hyperledger.fabric.protos.corfu.CorfuConnectGrpc;
import org.hyperledger.fabric.protos.peer.*;
import org.sslab.adapter.chaincode.fabcar.Car;
import org.sslab.adapter.chaincode.fabcar.FabCar;
import org.sslab.adapter.chaincodeShim.contract.Context;
import org.sslab.adapter.chaincodeShim.impl.ChaincodeMessageFactory;
import org.sslab.adapter.chaincodeShim.impl.InvocationStubImpl;
//import org.sslab.adapter.chaincode.fabcar.FabCar;
//import org.hyperledger.fabric.sdk.ProposalResponse;


import java.util.*;
import java.util.logging.Logger;


/**
 * @author Jeyoung Hwang.capricorn116@postech.ac.kr
 *         created on 2021. 4. 10.
 */
public class AdapterModuleService extends CorfuConnectGrpc.CorfuConnectImplBase{
    Map<UUID, CorfuRuntime> runtimes;
    Map<UUID, IStreamView> streamViews;
    Map<String, Long> lastReadAddrs;
    //tokenMap key: fabric txID, value: access token
    Map<String, Token> tokenMap;

//    private ProposalPackage.Proposal proposal;


    public AdapterModuleService() {
        streamViews = new HashMap<UUID, IStreamView>();
        runtimes = new HashMap<UUID, CorfuRuntime>();
//        runtime = new CorfuRuntime(runtimeAddr[0]).connect();
        lastReadAddrs = new HashMap<String, Long>();
        System.out.println("Init AdapterModuleService");
        tokenMap = new HashMap<String, Token>();
    }


    private static CorfuRuntime getRuntimeAndConnect(String configurationString) {
            CorfuRuntime corfuRuntime = new CorfuRuntime(configurationString).connect();
            return corfuRuntime;
        }
    CorfuRuntime runtime =  getRuntimeAndConnect("141.223.121.251:12011");
    AddressSpaceView addressSpaceView = runtime.getAddressSpaceView();

        private final Logger logger = Logger.getLogger(AdapterModuleService.class.getName());


    //commitTransaction version 3
    //Receive proposal response from peer

    @SneakyThrows
    @Override
    public void processProposal(ProposalPackage.SignedProposal signedProposal, StreamObserver<ProposalResponsePackage.ProposalResponse> responseObserver) {
        UnpackedProposal up =  unpackProposal(signedProposal);
        String result = processProposalSuccessfullyOrError(up);

        System.out.println("[interface] {processProposal} Corfu runtime is finished");
    }

    @SneakyThrows
    public UnpackedProposal unpackProposal(ProposalPackage.SignedProposal signedProposal) {
        ProposalPackage.Proposal proposal;
        Common.Header header;
        Common.ChannelHeader channelHeader;
        Common.SignatureHeader signatureHeader;
        ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt;
        ProposalPackage.ChaincodeProposalPayload payload;
        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec;

//        byte[] sp = signedProposal.getProposalBytes().toByteArray();
        proposal = ProposalPackage.Proposal.parseFrom(signedProposal.getProposalBytes());

        header = Common.Header.parseFrom(proposal.getHeader());

        channelHeader = Common.ChannelHeader.parseFrom(header.getChannelHeader());

        signatureHeader = Common.SignatureHeader.parseFrom(header.getSignatureHeader());

        chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());

        payload = ProposalPackage.ChaincodeProposalPayload.parseFrom(proposal.getPayload());

        chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.parseFrom(payload.getInput());

        UnpackedProposal unpackedProposal = new UnpackedProposal(chaincodeHdrExt.getChaincodeId().getName().toString(), channelHeader, chaincodeInvocationSpec.getChaincodeSpec().getInput(),
                proposal, signatureHeader, signedProposal);
        return unpackedProposal;
    }

    public String processProposalSuccessfullyOrError(UnpackedProposal up) throws InvalidProtocolBufferException {
        TransactionParams txParams =  new TransactionParams(
                up.channelHeader.getTxId(),
                up.channelHeader.getChannelId(),
                up.chaincodeName,
                up.signedProp,
                up.proposal
                );
        executeProposal(txParams, up.chaincodeName, up.input);
    return null;

    }

    public void executeProposal(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) throws InvalidProtocolBufferException {

//        CorfuChaincodeShim.CorfuChaincodeMessage message= new CorfuChaincodeShim.CorfuChaincodeMessage();
        FabCar fabcar = new FabCar();
        System.out.println(chaincodeInput);
        System.out.println(chaincodeInput.toString());


        InvocationStubImpl invocationStub = new InvocationStubImpl(ChaincodeMessageFactory.newGetStateEventMessage(txParams.channelID, txParams.txID, "", "CAR9"));
        Context context = new Context(invocationStub);
//        Car car = fabcar.queryCar(context, chaincodeInput.getArgs(1).toString());
        Car car1 = fabcar.queryCar(context, "CAR9");
        System.out.println(car1);
        fabcar.changeCarOwner(context,"CAR9", "newowner1");
        Car car2 = fabcar.queryCar(context, "CAR9");
        System.out.println(car2);


    }
//    public void executeProposal(String channelID, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) {
//        FabCar fabcar = new FabCar();
//        fabcar.queryCar(chaincodeInput.toString());


}






