package org.sslab.fabric.adapter;

import bsp_transaction.BspTransactionOuterClass;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.sslab.fabric.corfu.CorfuAccess;

import java.util.List;

public class TransactionParams {
    public String txID;
    public String channelID;
    public String chaincodeID;
    public String clientID;
    public BspTransactionOuterClass.Proposal signedProp;
    public BspTransactionOuterClass.ProposalPayload propPayload;
    public List<String> chaincodeArgs;
    public CorfuAccess corfu_access;
    boolean isInitTransaction;

    //for write transaction
    public TransactionParams(String txID, String chaincodeID, BspTransactionOuterClass.Proposal signedProp, BspTransactionOuterClass.ProposalPayload propPayload, List<String> chaincodeArgs, String channelID, CorfuAccess corfuAccess) {
        this.txID = txID;
        this.chaincodeID = chaincodeID;
        this.chaincodeArgs = chaincodeArgs;
        this.propPayload = propPayload;
        this.signedProp = signedProp;
        this.channelID = channelID;
        this.corfu_access = corfuAccess;
    }

    //for query transaction
    public TransactionParams(String txID, String channelID, String chaincodeID, List<String> chaincodeArgs, CorfuAccess corfuAccess) {
        this.txID = txID;
        this.channelID = channelID;
        this.chaincodeID = chaincodeID;
        this.chaincodeArgs = chaincodeArgs;
        this.corfu_access = corfuAccess;
    }
}
