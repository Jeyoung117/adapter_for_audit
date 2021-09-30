package org.sslab.adapter.chaincodeShim.contract;
import java.io.IOException;
import java.security.cert.CertificateException;

import org.hyperledger.fabric.contract.ClientIdentity;
import org.sslab.adapter.chaincodeShim.CorfuChaincodeStub;
import org.json.JSONException;

/**
 *
 * This context is available to all 'transaction functions' and provides the
 * transaction context. It also provides access to the APIs for the world state
 * using {@link #getStub()}
 * <p>
 * Applications can implement their own versions if they wish to add
 * functionality. All subclasses MUST implement a constructor, for example
 *
 * <pre>
 * {@code
 *
 * public MyContext extends Context {
 *
 *     public MyContext(ChaincodeStub stub) {
 *        super(stub);
 *     }
 * }
 *
 *}
 * </pre>
 *
 */
public class Context {
    /**
     *
     */
    protected CorfuChaincodeStub stub;

    /**
     *
     */
    protected ClientIdentity clientIdentity;

    /**
     * Creates new client identity and sets it as a property of the stub.
     *
     * @param stub Instance of the {@link CorfuChaincodeStub} to use
     */
    public Context(final CorfuChaincodeStub stub) {
        this.stub = stub;
//        try {
//            this.clientIdentity = new ClientIdentity(stub);
//        } catch (CertificateException | JSONException | IOException e) {
//            throw new ContractRuntimeException("Could not create new client identity", e);
//        }
    }

    /**
     *
     * @return ChaincodeStub instance to use
     */
    public CorfuChaincodeStub getStub() {
        return this.stub;
    }

    /**
     *
     * @return ClientIdentity object to use
     */
    public ClientIdentity getClientIdentity() {
        return this.clientIdentity;
    }
}