package org.sslab.fabric.protoutil;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.bouncycastle.crypto.Signer;
import org.hyperledger.fabric.protos.common.Common;

public class TxUtils {
    CommonUtils commonUtils = new CommonUtils();

    public Common.Envelope CreateSignedEnvelopeWithTLSBindingWithTxID(Common.HeaderType txType, String channelID, Signer signer, Message dataMsg, int msgVersion, long seq, byte[] tlsCertHash, String txid) {
        Common.ChannelHeader payloadChannelHeader = commonUtils.makeChannelHeader(txType, msgVersion, channelID, seq, tlsCertHash, txid);
        Common.SignatureHeader payloadSignatureHeader;

        payloadSignatureHeader = commonUtils.makeSignatureHeader();
        ByteString data = dataMsg.toByteString();
        Common.Payload payload = Common.Payload.newBuilder()
                .setHeader(commonUtils.makePayloadHeader(payloadChannelHeader, payloadSignatureHeader))
                .setData(data)
                .build();

        ByteString paylBytes =  commonUtils.marshal(payload);

        Common.Envelope env = Common.Envelope.newBuilder()
                .setPayload(paylBytes)
                .setSignature()
                .build();
        return  env;

    }
}
