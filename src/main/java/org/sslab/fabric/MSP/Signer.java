package org.sslab.fabric.MSP;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.helper.Config;
import org.hyperledger.fabric.sdk.helper.Utils;
import org.hyperledger.fabric.sdk.identity.IdentityFactory;
import org.hyperledger.fabric.sdk.identity.SigningIdentity;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.transaction.ProtoUtils;
import org.hyperledger.fabric.sdk.transaction.TransactionContext;

public class Signer {
    private static final Config config = Config.getConfig();
    //    private static final Log logger = LogFactory.getLog(TransactionContext.class);
    //TODO right now the server does not care need to figure out
    private final ByteString nonce = ByteString.copyFrom(Utils.generateNonce());
    private final CryptoSuite cryptoPrimitives;
    private final User user;
    private final Channel channel;
    private final String txID;
    private final Identities.SerializedIdentity identity;
    Timestamp currentTimeStamp = null;
    private boolean verify = true;
    //private List<String> attrs;
    private long proposalWaitTime = config.getProposalWaitTime();
    public SigningIdentity signingIdentity;
    private final String toString;

    public Signer(Channel channel, User user, CryptoSuite cryptoPrimitives) {

        this.user = user;
        this.channel = channel;
        //TODO clean up when public classes are interfaces.
        this.verify = !"".equals(channel.getName());  //if name is not blank not system channel and need verify.

        //  this.txID = transactionID;
        this.cryptoPrimitives = cryptoPrimitives;

        // Get the signing identity from the user
        this.signingIdentity = IdentityFactory.getSigningIdentity(cryptoPrimitives, user);

        // Serialize signingIdentity
        this.identity = signingIdentity.createSerializedIdentity();

        ByteString no = getNonce();

        ByteString comp = no.concat(identity.toByteString());

        byte[] txh = cryptoPrimitives.hash(comp.toByteArray());

        //    txID = Hex.encodeHexString(txh);
        txID = new String(Utils.toHexString(txh));
        toString = "TransactionContext{ txID: " + txID + ", mspid: " + user.getMspId() + ", user: " + user.getName() + "}";

    }
    public CryptoSuite getCryptoPrimitives() {
        return cryptoPrimitives;
    }

    public Identities.SerializedIdentity getIdentity() {

        return identity;

    }

    public long getEpoch() {
        return 0;
    }

    /**
     * Get the user with which this transaction context is associated.
     *
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * Get the attribute names associated with this transaction context.
     *
     * @return the attributes.
     */
    //public List<String> getAttrs() {
    //    return this.attrs;
    //}

    /**
     * Set the attributes for this transaction context.
     *
     * @param attrs the attributes.
     */
    //public void setAttrs(List<String> attrs) {
    //    this.attrs = attrs;
    //}

    /**
     * Get the channel with which this transaction context is associated.
     *
     * @return The channel
     */
    public Channel getChannel() {
        return this.channel;
    }

    /**
     * Gets the timeout for a single proposal request to endorser in milliseconds.
     *
     * @return the timeout for a single proposal request to endorser in milliseconds
     */
    public long getProposalWaitTime() {
        return proposalWaitTime;
    }

    /**
     * Sets the timeout for a single proposal request to endorser in milliseconds.
     *
     * @param proposalWaitTime the timeout for a single proposal request to endorser in milliseconds
     */
    public void setProposalWaitTime(long proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    public Timestamp getFabricTimestamp() {
        if (currentTimeStamp == null) {

            currentTimeStamp = ProtoUtils.getCurrentFabricTimestamp();
        }
        return currentTimeStamp;
    }

    public ByteString getNonce() {

        return nonce;

    }

    public void verify(boolean verify) {
        this.verify = verify;
    }

    public boolean getVerify() {
        return verify;
    }

    public String getChannelID() {
        return getChannel().getName();
    }

    public String getTxID() {
        return txID;
    }

    public byte[] sign(byte[] b) throws CryptoException, InvalidArgumentException {
        return signingIdentity.sign(b);
    }

    public ByteString signByteString(byte[] b) throws CryptoException, InvalidArgumentException {
        return ByteString.copyFrom(sign(b));
    }

    public ByteString signByteStrings(ByteString... bs) throws CryptoException, InvalidArgumentException {
        if (bs == null) {
            return null;
        }
        if (bs.length == 0) {
            return null;
        }
        if (bs.length == 1 && bs[0] == null) {
            return null;
        }

        ByteString f = bs[0];
        for (int i = 1; i < bs.length; ++i) {
            f = f.concat(bs[i]);

        }
        return ByteString.copyFrom(sign(f.toByteArray()));
    }

    public ByteString[] signByteStrings(User[] users, ByteString... bs) throws CryptoException, InvalidArgumentException {
        if (bs == null) {
            return null;
        }
        if (bs.length == 0) {
            return null;
        }
        if (bs.length == 1 && bs[0] == null) {
            return null;
        }

        ByteString f = bs[0];
        for (int i = 1; i < bs.length; ++i) {
            f = f.concat(bs[i]);
        }

        final byte[] signbytes = f.toByteArray();

        ByteString[] ret = new ByteString[users.length];

        int i = -1;
        for (User user : users) {
            // Get the signing identity from the user
            SigningIdentity signingIdentity = IdentityFactory.getSigningIdentity(cryptoPrimitives, user);

            // generate signature
            ret[++i] = ByteString.copyFrom(signingIdentity.sign(signbytes));
        }
        return ret;
    }

    @Override
    public String toString() {
        return toString;
    }

    public TransactionContext retryTransactionSameContext() {
        return new TransactionContext(channel, user, cryptoPrimitives);
    }

    public Identities.SerializedIdentity getSerializedIdentity() {
        return identity;
    }
}
