//package org.sslab.fabric.chaincode_support;
//
//import org.hyperledger.fabric.protos.peer.Chaincode;
//import org.hyperledger.fabric.protos.peer.ChaincodeShim;
//import org.sslab.fabric.adapter.TransactionParams;
//import org.sslab.fabric.chaincodeshim.contract.ContractRouter;
//
//import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.INIT;
//
//public class chaincodeSupport {
//    // Execute invokes chaincode and returns the original response.
//    public void Execute(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) {
////        ProposalResponsePackage.Response resp =
//                Invoke(txParams, chaincodeName, chaincodeInput);
//
//    // processChaincodeExecutionResult(txParams.txID); 추후 추가
////        return resp; //추후 return 값 변경
//    }
//
//
//    // invoke will invoke chaincode and return the message containing the response.
//    // The chaincode will be launched if it is not already running.
//    public void Invoke(TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) {
//        execute("test", INIT, txParams, chaincodeName, chaincodeInput);
//    }
//
//    public void processChaincodeExecutionResult(String ccName, String txid, ChaincodeShim.ChaincodeMessage resp) {
//        if(resp == null) {
//
//        }
//        if(resp.getChaincodeEvent() != null) {
////            resp.ch
//        }
//
//
//    }
//
//    public void execute(String ccid, ChaincodeShim.ChaincodeMessage.Type cctype, TransactionParams txParams, String chaincodeName, Chaincode.ChaincodeInput chaincodeInput) {
//        ChaincodeShim.ChaincodeMessage.Builder ccMsg = ChaincodeShim.ChaincodeMessage.newBuilder()
//                .setType(cctype)
//                .setChannelId(txParams.channelID)
//                .setTxid(txParams.txID)
//                .setPayload(chaincodeInput.toByteString());
//
//        System.out.println("Txid:" + ccMsg.getTxid());
//        System.out.println("Channelid:" + ccMsg.getChannelId());
//        System.out.println("getChaincodeEvent:" + ccMsg.getChaincodeEvent());
//
//        ContractRouter.main(new String[] {"-a", "127.0.0.1:7052", "-i", "testId"});
////        ChaincodeShim.ChaincodeMessage ccresp = ;
//
//    }
//
//
//}
//
