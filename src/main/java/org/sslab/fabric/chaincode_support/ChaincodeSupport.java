package org.sslab.fabric.chaincode_support;

import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.protos.peer.ChaincodeShim;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.sslab.fabric.adapter.TransactionParams;
import org.sslab.fabric.chaincodeshim.contract.ContractRouter;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;
import org.sslab.fabric.chaincodeshim.shim.impl.InvocationStubImpl;
import org.sslab.fabric.corfu.Rwset_builder;

import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.INIT;

public class ChaincodeSupport {
    // Execute invokes chaincode and returns the original response.
    public org.sslab.fabric.chaincodeshim.shim.Chaincode.Response Execute(ChaincodeStub stub, ContractRouter cfc) {
        org.sslab.fabric.chaincodeshim.shim.Chaincode.Response ccresp = Invoke(stub, cfc);

        Rwset_builder rwSet = ccresp.getRwset();

        // processChaincodeExecutionResult(txParams.txID); 추후 추가
        return ccresp;
    }


    // invoke will invoke chaincode and return the message containing the response.
    // The chaincode will be launched if it is not already running.
    public org.sslab.fabric.chaincodeshim.shim.Chaincode.Response Invoke(ChaincodeStub stub, ContractRouter cfc) {
        org.sslab.fabric.chaincodeshim.shim.Chaincode.Response ccresp  = cfc.invoke(stub);
        return ccresp;
    }

    public void processChaincodeExecutionResult(String ccName, String txid, ChaincodeShim.ChaincodeMessage resp) {
        if(resp == null) {

        }
        if(resp.getChaincodeEvent() != null) {
//            resp.ch
        }


    }

    public void execute(String ccid, ChaincodeShim.ChaincodeMessage.Type cctype, TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) {
        ChaincodeShim.ChaincodeMessage.Builder ccMsg = ChaincodeShim.ChaincodeMessage.newBuilder()
                .setType(cctype)
                .setChannelId(txParams.channelID)
                .setTxid(txParams.txID)
                .setPayload(chaincodeInput.toByteString());

        System.out.println("Txid:" + ccMsg.getTxid());
        System.out.println("Channelid:" + ccMsg.getChannelId());
        System.out.println("getChaincodeEvent:" + ccMsg.getChaincodeEvent());

        ContractRouter.main(new String[] {"-a", "127.0.0.1:7052", "-i", "testId"});
//        ChaincodeShim.ChaincodeMessage ccresp = ;

    }


}

