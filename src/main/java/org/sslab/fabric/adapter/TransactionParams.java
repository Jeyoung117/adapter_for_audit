package org.sslab.fabric.adapter;

import bsp_transaction.BspTransactionOuterClass;
import org.hyperledger.fabric.protos.peer.ProposalPackage;

import java.util.List;

public class TransactionParams {
    public String txID;
    public String channelID;
    public String namespaceID;
    public String clientID;
    public BspTransactionOuterClass.Proposal signedProp;
    public BspTransactionOuterClass.ProposalPayload propPayload;
    List<String> chaincodeArgs;
    boolean isInitTransaction;



    public TransactionParams(String txID, String namespaceID, BspTransactionOuterClass.Proposal signedProp, BspTransactionOuterClass.ProposalPayload propPayload, List<String> chaincodeArgs) {
        this.txID = txID;
//        this.channelID = channelID;
        this.namespaceID = namespaceID;
        this.chaincodeArgs = chaincodeArgs;
        this.propPayload = propPayload;
        this.signedProp = signedProp;
//        this.proposalHash = proposalHash;
    }
}
