package org.sslab.fabric.dtype;

import com.google.common.reflect.TypeToken;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class dtype {
    public enum TxType {
        BSPSentinelValue_BSPTX(55555, 55555),
        BSPSentinelValue_Auditor(55556, 55556),
        BSPSentinelValue_MCAuditTx(55557, 55557),
        BSPSentinelValue_Heartbeat(55558, 55558),
        BSPSentinelValue_NewView(55559, 55559),
        BSPSentinelValue_BetaDelivery(55560, 55560);
//        55555("BSPSentinelValue_BSPTX", TypeToken.of(String.class)),
//        55556("BSPSentinelValue_Auditor", TypeToken.of(int.class)),
//        55557("BSPSentinelValue_MCAuditTx", TypeToken.of(int.class)),
//        55558("BSPSentinelValue_Heartbeat", TypeToken.of(int.class)),
//        55559("BSPSentinelValue_NewView", TypeToken.of(int.class)),
//        55560("BSPSentinelValue_BetaDelivery", TypeToken.of(int.class));

        final int type;
        @Getter
        final int componentType;

        private TxType(int type, int componentType) {
            this.type = type;
            this.componentType = componentType;
        }

        public byte asByte() {
            return (byte) type;
        }

        public static Map<Byte, TxType> typeMap =
                Arrays.<TxType>stream(TxType.values())
                        .collect(Collectors.toMap(TxType::asByte,
                                Function.identity()));
    }
}
