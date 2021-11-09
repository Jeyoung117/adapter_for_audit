package org.sslab.fabric.chaincode.smallbank;

import com.owlike.genson.Genson;
import org.sslab.fabric.chaincodeshim.contract.Context;
import org.sslab.fabric.chaincodeshim.contract.ContractInterface;
import org.sslab.fabric.chaincodeshim.contract.annotation.*;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeException;
import org.sslab.fabric.chaincodeshim.shim.ChaincodeStub;

/**
 * Java implementation of the Fabric Car Contract described in the Writing Your
 * First Application tutorial
 */
@Contract(
        name = "smallbank",
        info = @Info(
                title = "SmallBank contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "F Carr",
                        url = "https://hyperledger.example.com")))
@Default
public final class smallbank implements ContractInterface {

    private final Genson genson = new Genson();

    private enum SmallBankErrors {
        ACCOUNT_NOT_FOUND,
        ACCOUNT_ALREADY_EXISTS,
        BALLANCE_INSUFFICIENT
    }


    /**
     * Creates a new account on the ledger.
     *
     * @param ctx the transaction context
     * @param customId the customId for the new account
     * @param status the status of the new account
     * @param balance the balance of the new account
     * @return the created Account
     */
    @Transaction()
    public Account createAccount(final Context ctx, final String customId, final String status, final String balance) {
        ChaincodeStub stub = ctx.getStub();

        String accountState = stub.getStringState(customId);

        int checking = Integer.parseInt(balance);
        if (!accountState.isEmpty()) {
            String errorMessage = String.format("Can not create duplicated account");
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SmallBankErrors.ACCOUNT_ALREADY_EXISTS.toString());
        }

        Account account = new Account(customId, status, checking, 0);
        accountState = genson.serialize(account);
        stub.putStringState(customId, accountState);

        return account;
    }

    /**
     * Send payment from source account to destination account
     *
     * @param ctx the transaction context
     * @param src the source account
     * @param dst the destination account
     * @param amount the amount
     * @return array of Cars found on the ledger
     */
    @Transaction()
    public Account sendPaymentNormal(final Context ctx, final String src, final String dst, final String amount) {
        ChaincodeStub stub = ctx.getStub();

        final String srcAccountId  = src;
        final String dstAccountId  = dst;
        int amountI = Integer.parseInt(amount);

        String srcAccountState = stub.getStringState(srcAccountId);
        String dstAccounStatet = stub.getStringState(dstAccountId);

        Account srcAccount = genson.deserialize(srcAccountState, Account.class);
        Account dstAccount = genson.deserialize(dstAccounStatet, Account.class);

        if(srcAccount.getBalance() < amountI) {
            String errorMessage = String.format("Insufficient funds in source checking account");
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SmallBankErrors.BALLANCE_INSUFFICIENT.toString());
        }

        int srcbalance = srcAccount.getBalance() - amountI;
        int dstbalance = dstAccount.getBalance() + amountI;

        Account newSrcAccount = new Account(srcAccount.getCustomId(), srcAccount.getStatus(), srcbalance , srcAccount.getCustodyBalance());
        Account newDstAccount = new Account(dstAccount.getCustomId(), dstAccount.getStatus(), dstbalance , srcAccount.getCustodyBalance());
        String newSrcAccountState = genson.serialize(newSrcAccount);
        String newDstAccountState = genson.serialize(newDstAccount);
        stub.putStringState(srcAccountId, newSrcAccountState);
        stub.putStringState(dstAccountId, newDstAccountState);

        return newSrcAccount;
    }

    @Transaction()
    public Account queryAccount(final Context ctx, final String customId) {
        ChaincodeStub stub = ctx.getStub();
        String accountState = stub.getStringState(customId);

        if (accountState.isEmpty()) {
            String errorMessage = String.format("Account %s does not exist", customId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SmallBankErrors.ACCOUNT_NOT_FOUND.toString());
        }

        Account account = genson.deserialize(accountState, Account.class);

        return account;
    }
}
