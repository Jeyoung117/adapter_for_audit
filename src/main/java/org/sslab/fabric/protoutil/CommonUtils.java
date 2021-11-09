package org.sslab.fabric.protoutil;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.identity.SigningIdentity;

public class CommonUtils {
    private SigningIdentity signingIdentity;
    public Common.ChannelHeader makeChannelHeader(Common.HeaderType headerType, int version, String chainID ,long epoch, byte[] tlsCertHash, String txID) {
        return Common.ChannelHeader.newBuilder()
                .setType(headerType.getNumber())
                .setVersion(version)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(System.currentTimeMillis())
                        .setNanos(0)
                        .build())
                .setChannelId(chainID)
                .setEpoch(epoch)
                .setTlsCertHash(ByteString.copyFrom(tlsCertHash))
                .setTxId(txID)
                .build();

    }

//    public Common.SignatureHeader makeSignatureHeader(SigningIdentity signingIdentity) {
//        Identities.SerializedIdentity creator = signingIdentity.createSerializedIdentity();
//        Common.SignatureHeader.newBuilder()
//                .setCreator(creator)
//                .setNonce(0)
//                .build();
//    }

    public ByteString marshal(Message pb) {
        ByteString data = pb.toByteString();
        return data;
    }

    public Common.Header makePayloadHeader(Common.ChannelHeader ch, Common.SignatureHeader sh) {
        return Common.Header.newBuilder()
                .setChannelHeader(marshal(ch))
                .setSignatureHeader(marshal(sh))
                .build();

    }
}
