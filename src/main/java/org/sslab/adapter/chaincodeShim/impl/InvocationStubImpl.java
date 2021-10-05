package org.sslab.adapter.chaincodeShim.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.common.Common.ChannelHeader;
import org.hyperledger.fabric.protos.common.Common.Header;
import org.hyperledger.fabric.protos.common.Common.HeaderType;
import org.hyperledger.fabric.protos.common.Common.SignatureHeader;
import org.hyperledger.fabric.protos.corfu.CorfuChaincodeShim;
import org.hyperledger.fabric.protos.peer.Chaincode.ChaincodeInput;
import org.hyperledger.fabric.protos.peer.ChaincodeEventPackage.ChaincodeEvent;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.hyperledger.fabric.protos.peer.ProposalPackage.ChaincodeProposalPayload;
import org.hyperledger.fabric.protos.peer.ProposalPackage.Proposal;
import org.hyperledger.fabric.protos.peer.ProposalPackage.SignedProposal;
import org.hyperledger.fabric.shim.Chaincode.Response;
import org.sslab.adapter.Corfu_access;
import org.sslab.adapter.chaincodeShim.ChaincodeStub;
//import org.sslab.adapter.chaincode.fabcar.FabCar;
//import org.hyperledger.fabric.sdk.ProposalResponse;


/**
 * @author Jeyoung Hwang.capricorn116@postech.ac.kr
 *         created on 2021. 4. 10.
 */
public class InvocationStubImpl implements ChaincodeStub {
    private static final String UNSPECIFIED_START_KEY = new String(Character.toChars(0x000001));
    private static final String UNSPECIFIED_END_KEY = "";
    private static final Logger LOGGER = Logger.getLogger(InvocationStubImpl.class.getName());

    public static final String MAX_UNICODE_RUNE = "\udbff\udfff";
    private static final String CORE_PEER_LOCALMSPID = "CORE_PEER_LOCALMSPID";
    private final String channelId;
    private final String chaincodeId;
    private final String txId;
//    private final ChaincodeInvocationTask handler;
    private final List<ByteString> args;
    private final SignedProposal signedProposal;
    private final Instant txTimestamp;
    private final ByteString creator;
    private final Map<String, ByteString> transientMap;
//    private final byte[] binding;
    private ChaincodeEvent event;
    Corfu_access corfu_access = new Corfu_access();



    /**
     *
     * @param message
//     * @param handler
     * @throws InvalidProtocolBufferException
     */



    public InvocationStubImpl(final CorfuChaincodeShim.CorfuChaincodeMessage message)
            throws InvalidProtocolBufferException {
        this.channelId = message.getChannelId();
        this.txId = message.getTxid();
        this.signedProposal = message.getProposal();
        final Proposal proposal = Proposal.parseFrom(signedProposal.getProposalBytes());
        final Header header = Header.parseFrom(proposal.getHeader());
        final ChannelHeader channelHeader = ChannelHeader.parseFrom(header.getChannelHeader());
        final ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());
//        validateProposalType(channelHeader);
        final String chaincodeID =String.valueOf(chaincodeHdrExt.getChaincodeId());

        this.chaincodeId= chaincodeID;


//        this.handler = handler;
        final ChaincodeInput input = ChaincodeInput.parseFrom(message.getPayload());

        this.args = Collections.unmodifiableList(input.getArgsList());

        if (this.signedProposal == null || this.signedProposal.getProposalBytes().isEmpty()) {
            this.creator = null;
            this.txTimestamp = null;
            this.transientMap = Collections.emptyMap();
//            this.binding = null;
        } else {
            try {
//                final Proposal proposal = Proposal.parseFrom(signedProposal.getProposalBytes());
//                final Header header = Header.parseFrom(proposal.getHeader());
//                final ChannelHeader channelHeader = ChannelHeader.parseFrom(header.getChannelHeader());
//                final ProposalPackage.ChaincodeHeaderExtension chaincodeHdrExt = ProposalPackage.ChaincodeHeaderExtension.parseFrom(channelHeader.getExtension());
//                validateProposalType(channelHeader);
                final SignatureHeader signatureHeader = SignatureHeader.parseFrom(header.getSignatureHeader());
                final ChaincodeProposalPayload chaincodeProposalPayload = ChaincodeProposalPayload
                        .parseFrom(proposal.getPayload());
                final Timestamp timestamp = channelHeader.getTimestamp();


                this.txTimestamp = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
                this.creator = signatureHeader.getCreator();
                this.transientMap = chaincodeProposalPayload.getTransientMapMap();
//                this.binding = computeBinding(channelHeader, signatureHeader);

                System.out.println("전달받아서 생성한 this.chaincodeId:" + this.chaincodeId);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private byte[] computeBinding(final ChannelHeader channelHeader, final SignatureHeader signatureHeader)
            throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(signatureHeader.getNonce().asReadOnlyByteBuffer());
        messageDigest.update(this.creator.asReadOnlyByteBuffer());
        final ByteBuffer epochBytes = ByteBuffer.allocate(Long.BYTES).order(ByteOrder.LITTLE_ENDIAN)
                .putLong(channelHeader.getEpoch());
        epochBytes.flip();
        messageDigest.update(epochBytes);
        return messageDigest.digest();
    }

    private void validateProposalType(final ChannelHeader channelHeader) {
        switch (Common.HeaderType.forNumber(channelHeader.getType())) {
            case ENDORSER_TRANSACTION:
            case CONFIG:
                return;
            default:
                throw new RuntimeException(
                        String.format("Unexpected transaction type: %s", HeaderType.forNumber(channelHeader.getType())));
        }
    }

    @Override
    public List<byte[]> getArgs() {
        return null;
    }

    @Override
    public List<String> getStringArgs() {
        return null;
    }

    @Override
    public String getFunction() {
        return null;
    }

    @Override
    public List<String> getParameters() {
        return null;
    }

    @Override
    public String getTxId() {
        return null;
    }

    @Override
    public String getChannelId() {
        return null;
    }

    @Override
    public Response invokeChaincode(String chaincodeName, List<byte[]> args, String channel) {
        return null;
    }
    /**
     * Send the args back to the corfu server.
     *
     * Invoke method for processing string args (version 1)
     *
     *
     * @param objectKey The object key from the chaincode
     *
     *
     * @return ByteString to be parsed by the caller
     *
     */
    @Override
    public byte[] getState(final String key) {
        return corfu_access.getStringState(key, channelId, "fabcar");
//        return this.handler.invoke(objectKey, channelId, chaincodeName);
    }


    /**
     * Send the chaincode message back to the corfu server.
     *
     * Invoke method for processing corfuchaincodemsg (version 2)
     * @param key The object key from the chaincode
     * @return ByteString to be parsed by the caller
     *
     */
//    @Override
//    public byte[] getState(final String key) {
//        return this.handler.invoke(ChaincodeMessageFactory.newGetStateEventMessage(channelId, txId, "", key))
//                .toByteArray();
//    }


    @Override
    public byte[] getStateValidationParameter(String key) {
        return new byte[0];
    }

    @Override
    public void putState(String key, byte[] value) {
        corfu_access.putStringState(key, channelId, "fabcar", value);
    }

    @Override
    public void setStateValidationParameter(String key, byte[] value) {

    }

    @Override
    public void delState(String key) {

    }

    @Override
    public byte[] getPrivateData(String collection, String key) {
        return new byte[0];
    }

    @Override
    public byte[] getPrivateDataHash(String collection, String key) {
        return new byte[0];
    }

    @Override
    public byte[] getPrivateDataValidationParameter(String collection, String key) {
        return new byte[0];
    }

    @Override
    public void putPrivateData(String collection, String key, byte[] value) {

    }

    @Override
    public void setPrivateDataValidationParameter(String collection, String key, byte[] value) {

    }

    @Override
    public void delPrivateData(String collection, String key) {

    }

    @Override
    public ChaincodeEvent getEvent() {
        return null;
    }

    @Override
    public SignedProposal getSignedProposal() {
        return null;
    }

    @Override
    public Instant getTxTimestamp() {
        return null;
    }

    @Override
    public byte[] getCreator() {
        return new byte[0];
    }

    @Override
    public Map<String, byte[]> getTransient() {
        return null;
    }

    @Override
    public byte[] getBinding() {
        return new byte[0];
    }

    @Override
    public String getMspId() {
        return null;
    }
}