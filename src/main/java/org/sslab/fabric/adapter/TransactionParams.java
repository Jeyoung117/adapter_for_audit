package org.sslab.fabric.adapter;

import org.hyperledger.fabric.protos.peer.ProposalPackage;

public class TransactionParams {
    public String txID;
    public String channelID;
    public String namespaceID;
    public ProposalPackage.SignedProposal signedProp;
    public ProposalPackage.Proposal proposal;
    public byte[] proposalHash;
//    HistoryQueryExecutor ledger.HistoryQueryExecutor
//    CollectionStore      privdata.CollectionStore
    boolean isInitTransaction;



    public TransactionParams(String txID, String channelID, String namespaceID, ProposalPackage.SignedProposal signedProp,
            ProposalPackage.Proposal proposal, byte[] proposalHash ) {
        this.txID = txID;
        this.channelID = channelID;
        this.namespaceID = namespaceID;
        this.proposal = proposal;
        this.signedProp = signedProp;
        this.proposalHash = proposalHash;
    }
}
