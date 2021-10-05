package org.sslab.adapter.chaincodeShim;


import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hyperledger.fabric.protos.peer.ChaincodeEventPackage.ChaincodeEvent;
import org.hyperledger.fabric.protos.peer.ProposalPackage.SignedProposal;
import org.hyperledger.fabric.shim.Chaincode.Response;
import org.hyperledger.fabric.shim.ledger.KeyValue;


public interface ChaincodeStub {
    /**

     *
     * @return a list of arguments (bytes arrays)
     */
    List<byte[]> getArgs();

    /**
     * Returns the arguments corresponding to the call to

     * @return a list of arguments cast to UTF-8 strings
     */
    List<String> getStringArgs();

    /**
     * A convenience method that returns the first argument of the chaincode
     * invocation for use as a function name.
     * <p>
     * The bytes of the first argument are decoded as a UTF-8 string.
     *
     * @return the function name
     */
    String getFunction();

    /**
     * A convenience method that returns all except the first argument of the
     * chaincode invocation for use as the parameters to the function returned by
     * #{@link ChaincodeStub#getFunction()}.
     * <p>
     * The bytes of the arguments are decoded as a UTF-8 strings and returned as a
     * list of string parameters.
     *
     * @return a list of parameters
     */
    List<String> getParameters();

    /**
     * Returns the transaction id for the current chaincode invocation request.
     * <p>
     * The transaction id uniquely identifies the transaction within the scope of
     * the channel.
     *
     * @return the transaction id
     */
    String getTxId();

    /**
     * Returns the channel id for the current proposal.
     * <p>
     * This would be the 'channel_id' of the transaction proposal except where the
     * chaincode is calling another on a different channel.
     *
     * @return the channel id
     */
    String getChannelId();

    /**
     * Locally calls the specified chaincode <code>invoke()</code> using the same
     * transaction context.
     * <p>
     * chaincode calling chaincode doesn't create a new transaction message.
     * <p>
     * If the called chaincode is on the same channel, it simply adds the called
     * chaincode read set and write set to the calling transaction.
     * <p>
     * If the called chaincode is on a different channel, only the Response is
     * returned to the calling chaincode; any <code>putState</code> calls from the
     * called chaincode will not have any effect on the ledger; that is, the called
     * chaincode on a different channel will not have its read set and write set
     * applied to the transaction. Only the calling chaincode's read set and write
     * set will be applied to the transaction. Effectively the called chaincode on a
     * different channel is a `Query`, which does not participate in state
     * validation checks in subsequent commit phase.
     * <p>
     * If `channel` is empty, the caller's channel is assumed.
     * <p>
     * Invoke another chaincode using the same transaction context.
     *
     * @param chaincodeName Name of chaincode to be invoked.
     * @param args          Arguments to pass on to the called chaincode.
     * @param channel       If not specified, the caller's channel is assumed.
     * @return {@link Response} object returned by called chaincode
     */
    Response invokeChaincode(String chaincodeName, List<byte[]> args, String channel);

    /**
     * Returns the value of the specified <code>key</code> from the ledger.
     * <p>
     * Note that getState doesn't read data from the writeset, which has not been
     * committed to the ledger. In other words, GetState doesn't consider data
     * modified by PutState that has not been committed.
     *
     * @param key name of the value
     * @return value the value read from the ledger
     */
    byte[] getState(String key);

    /**
     * Returns the value of the specified <code>key</code> from the ledger.
     * <p>
     * Note that getState doesn't read data from the writeset, which has not been
     * committed to the ledger. In other words, GetState doesn't consider data
     * modified by PutState that has not been committed.
     *
     * @param objectKey name of the value
     * @param objectKey name of the value
     * @param chaincodeName name of the value
     * @return value the value read from the ledger
     */
//    byte[] getState(final String objectKey, String channelId, String chaincodeName);

    /**
     * retrieves the key-level endorsement policy for <code>key</code>. Note that
     * this will introduce a read dependency on <code>key</code> in the
     * transaction's readset.
     *
     * @param key key to get key level endorsement
     * @return endorsement policy
     */
    byte[] getStateValidationParameter(String key);

    /**
     * Puts the specified <code>key</code> and <code>value</code> into the
     * transaction's writeset as a data-write proposal.
     * <p>
     * putState doesn't effect the ledger until the transaction is validated and
     * successfully committed. Simple keys must not be an empty string and must not
     * start with 0x00 character, in order to avoid range query collisions with
     * composite keys
     *
     * @param key   name of the value
     * @param value the value to write to the ledger
     */
    void putState(String key, byte[] value);

    /**
     * Sets the key-level endorsement policy for <code>key</code>.
     *
     * @param key   key to set key level endorsement
     * @param value endorsement policy
     */
    void setStateValidationParameter(String key, byte[] value);

    /**
     * Records the specified <code>key</code> to be deleted in the writeset of the
     * transaction proposal.
     * <p>
     * The <code>key</code> and its value will be deleted from the ledger when the
     * transaction is validated and successfully committed.
     *
     * @param key name of the value to be deleted
     */
    void delState(String key);

    /**
     * Returns all existing keys, and their values, that are lexicographically
     * between <code>startkey</code> (inclusive) and the <code>endKey</code>
     * (exclusive).

     *
     * @param startKey key as the start of the key range (inclusive)
     * @param endKey   key as the end of the key range (exclusive)
     * @return an {@link Iterable} of {@link KeyValue}
     */


    /**
     * Returns the value of the specified <code>key</code> from the specified
     * <code>collection</code>.
     * <p>
     * Note that {@link #getPrivateData(String, String)} doesn't read data from the
     * private writeset, which has not been committed to the
     * <code>collection</code>. In other words,
     * {@link #getPrivateData(String, String)} doesn't consider data modified by
     * {@link #putPrivateData(String, String, byte[])} * that has not been
     * committed.
     *
     * @param collection name of the collection
     * @param key        name of the value
     * @return value the value read from the collection
     */
    byte[] getPrivateData(String collection, String key);

    /**
     * @param collection name of the collection
     * @param key        name of the value
     * @return the private data hash
     */
    byte[] getPrivateDataHash(String collection, String key);

    /**
     * Retrieves the key-level endorsement policy for the private data specified by
     * <code>key</code>. Note that this introduces a read dependency on
     * <code>key</code> in the transaction's readset.
     *
     * @param collection name of the collection
     * @param key        key to get endorsement policy
     * @return Key Level endorsement as byte array
     */
    byte[] getPrivateDataValidationParameter(String collection, String key);

    /**
     * Puts the specified <code>key</code> and <code>value</code> into the
     * transaction's private writeset.
     * <p>
     * Note that only hash of the private writeset goes into the transaction
     * proposal response (which is sent to the client who issued the transaction)
     * and the actual private writeset gets temporarily stored in a transient store.
     * putPrivateData doesn't effect the <code>collection</code> until the
     * transaction is validated and successfully committed. Simple keys must not be
     * an empty string and must not start with null character (0x00), in order to
     * avoid range query collisions with composite keys, which internally get
     * prefixed with 0x00 as composite key namespace.
     *
     * @param collection name of the collection
     * @param key        name of the value
     * @param value      the value to write to the ledger
     */
    void putPrivateData(String collection, String key, byte[] value);

    /**
     * Sets the key-level endorsement policy for the private data specified by
     * <code>key</code>.
     *
     * @param collection name of the collection
     * @param key        key to set endorsement policy
     * @param value      endorsement policy
     */
    void setPrivateDataValidationParameter(String collection, String key, byte[] value);

    /**
     * Records the specified <code>key</code> to be deleted in the private writeset
     * of the transaction.
     * <p>
     * Note that only hash of the private writeset goes into the transaction
     * proposal response (which is sent to the client who issued the transaction)
     * and the actual private writeset gets temporarily stored in a transient store.
     * The <code>key</code> and its value will be deleted from the collection when
     * the transaction is validated and successfully committed.
     *
     * @param collection name of the collection
     * @param key        name of the value to be deleted
     */
    void delPrivateData(String collection, String key);


    /**
     * Invoke another chaincode using the same transaction context.
     * <p>
     * Same as {@link #invokeChaincode(String, List, String)} using channelId to
     * <code>null</code>
     *
     * @param chaincodeName Name of chaincode to be invoked.
     * @param args          Arguments to pass on to the called chaincode.
     * @return {@link Response} object returned by called chaincode
     */
    default Response invokeChaincode(final String chaincodeName, final List<byte[]> args) {
        return invokeChaincode(chaincodeName, args, null);
    }

    /**
     * Invoke another chaincode using the same transaction context.
     * <p>
     * This is a convenience version of
     * {@link #invokeChaincode(String, List, String)}. The string args will be
     * encoded into as UTF-8 bytes.
     *
     * @param chaincodeName Name of chaincode to be invoked.
     * @param args          Arguments to pass on to the called chaincode.
     * @param channel       If not specified, the caller's channel is assumed.
     * @return {@link Response} object returned by called chaincode
     */
    default Response invokeChaincodeWithStringArgs(final String chaincodeName, final List<String> args, final String channel) {
        return invokeChaincode(chaincodeName, args.stream().map(x -> x.getBytes(UTF_8)).collect(toList()), channel);
    }

    /**
     * Invoke another chaincode using the same transaction context.
     * <p>
     * This is a convenience version of {@link #invokeChaincode(String, List)}. The
     * string args will be encoded into as UTF-8 bytes.
     *
     * @param chaincodeName Name of chaincode to be invoked.
     * @param args          Arguments to pass on to the called chaincode.
     * @return {@link Response} object returned by called chaincode
     */
    default Response invokeChaincodeWithStringArgs(final String chaincodeName, final List<String> args) {
        return invokeChaincodeWithStringArgs(chaincodeName, args, null);
    }

    /**
     * Invoke another chaincode using the same transaction context.
     * <p>
     * This is a convenience version of {@link #invokeChaincode(String, List)}. The
     * string args will be encoded into as UTF-8 bytes.
     *
     * @param chaincodeName Name of chaincode to be invoked.
     * @param args          Arguments to pass on to the called chaincode.
     * @return {@link Response} object returned by called chaincode
     */
    default Response invokeChaincodeWithStringArgs(final String chaincodeName, final String... args) {
        return invokeChaincodeWithStringArgs(chaincodeName, Arrays.asList(args), null);
    }

    /**
     * Returns the byte array value specified by the key and decoded as a UTF-8
     * encoded string, from the ledger.
     * <p>
     * This is a convenience version of {@link #getState(String)}
     *
     * @param key name of the value
     * @return value the value read from the ledger
     */
    default String getStringState(final String key) {
        return new String(getState(key), UTF_8);
    }

    /**
     * Writes the specified value and key into the sidedb collection value converted
     * to byte array.
     *
     * @param collection collection name
     * @param key        name of the value
     * @param value      the value to write to the ledger
     */

    default void putPrivateData(final String collection, final String key, final String value) {
        putPrivateData(collection, key, value.getBytes(UTF_8));
    }

    /**
     * Returns the byte array value specified by the key and decoded as a UTF-8
     * encoded string, from the sidedb collection.
     *
     * @param collection collection name
     * @param key        name of the value
     * @return value the value read from the ledger
     */
    default String getPrivateDataUTF8(final String collection, final String key) {
        return new String(getPrivateData(collection, key), UTF_8);
    }

    /**
     * Writes the specified value and key into the ledger.
     *
     * @param key   name of the value
     * @param value the value to write to the ledger
     */
    default void putStringState(final String key, final String value) {

        putState(key, value.getBytes(UTF_8));
    }

    /**
     * Returns the CHAINCODE type event that will be posted to interested clients
     * when the chaincode's result is committed to the ledger.
     *
     * @return the chaincode event or null
     */
    ChaincodeEvent getEvent();

    /**
     * Returns the signed transaction proposal currently being executed.
     *
     * @return null if the current transaction is an internal call to a system
     *         chaincode.
     */
    SignedProposal getSignedProposal();

    /**
     * Returns the timestamp when the transaction was created.
     *
     * @return timestamp as specified in the transaction's channel header.
     */
    Instant getTxTimestamp();

    /**
     * Returns the identity of the agent (or user) submitting the transaction.
     *
     * @return the bytes of the creator field of the proposal's signature header.
     */
    byte[] getCreator();

    /**
     * Returns the transient map associated with the current transaction.
     *
     * @return map of transient field
     */
    Map<String, byte[]> getTransient();

    /**
     * Returns the transaction binding.
     *
     * @return binding between application data and proposal
     */
    byte[] getBinding();

    /**
     * Get the MSPID of the peer that started this chaincode.
     *
     * @return string MSPID
     */
    String getMspId();
}
