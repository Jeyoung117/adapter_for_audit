package org.sslab.fabric.protoutil;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.sslab.fabric.MSP.Signer;

public class TxUtils {
    CommonUtils commonUtils = new CommonUtils();

    public Common.Envelope CreateSignedEnvelopeWithTLSBindingWithTxID(Common.HeaderType txType, String channelID, Signer signer,
                                                                      Message dataMsg, int msgVersion, long seq, byte[] tlsCertHash, String txid)
            throws InvalidArgumentException, CryptoException {
        Common.ChannelHeader payloadChannelHeader = commonUtils.makeChannelHeader(txType, msgVersion, channelID, seq, tlsCertHash, txid);
        Common.SignatureHeader payloadSignatureHeader;

        payloadSignatureHeader = commonUtils.makeSignatureHeader(signer);
        ByteString data = dataMsg.toByteString();

        Common.Payload payload = Common.Payload.newBuilder()
                .setHeader(commonUtils.makePayloadHeader(payloadChannelHeader, payloadSignatureHeader))
                .setData(data)
                .build();

        ByteString paylBytes =  commonUtils.marshal(payload);

        Common.Envelope env = Common.Envelope.newBuilder()
                .setPayload(paylBytes)
                .setSignature(signer.signByteString(payload.toByteArray()))
                .build();

        return  env;
    }
}
