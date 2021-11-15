package org.sslab.fabric.protoutil;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.identity.SigningIdentity;
import org.sslab.fabric.MSP.Signer;

public class CommonUtils {
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
//                .setTlsCertHash(ByteString.copyFrom(tlsCertHash))
                .setTxId(txID)
                .build();

    }

    public Common.SignatureHeader makeSignatureHeader(Signer signer) {

        return Common.SignatureHeader.newBuilder()
                .setCreator(signer.getIdentity().toByteString())
                .setNonce(signer.getNonce()) //signer가 static이면 값 안 변하는 거 아닌지?
                .build();
    }

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
