package org.sslab.adapter;

import org.bouncycastle.util.CollectionStore;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.protos.peer.ProposalPackage;

public class TransactionParams {
    String txID;
    String channelID;
    String namespaceID;
    public ProposalPackage.SignedProposal signedProp;
    public ProposalPackage.Proposal proposal;
//    HistoryQueryExecutor ledger.HistoryQueryExecutor
//    CollectionStore      privdata.CollectionStore
    boolean isInitTransaction;



    public TransactionParams(String txID, String channelID, String namespaceID, ProposalPackage.SignedProposal signedProp,
            ProposalPackage.Proposal proposal ) {
        this.txID = txID;
        this.channelID = channelID;
        this.namespaceID = namespaceID;
        this.proposal = proposal;
        this.signedProp = signedProp;
    }
}
