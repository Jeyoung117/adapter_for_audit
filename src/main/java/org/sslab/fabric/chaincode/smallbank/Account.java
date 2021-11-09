package org.sslab.fabric.chaincode.smallbank;

import java.util.Objects;

import org.sslab.fabric.chaincodeshim.contract.annotation.DataType;
import org.sslab.fabric.chaincodeshim.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Account {

    @Property()
    private final String customId;

    @Property()
    private final String status;

    @Property()
    private final int balance;

    @Property()
    private final int custodyBalance;

    public String getCustomId() {
        return customId;
    }

    public String getStatus() {
        return status;
    }

//    public String getRegion() {
//        return region;
//    }

    public int getBalance() {
        return balance;
    }

    public int getCustodyBalance() {
        return custodyBalance;
    }

//    public Account(@JsonProperty("customId") final String customId, @JsonProperty("status") final String status,
//               @JsonProperty("region") final String region, @JsonProperty("balance") final int balance, @JsonProperty("custodyBalance") final int custodyBalance) {
//        this.customId = customId;
//        this.status = status;
//        this.region = region;
//        this.balance = balance;
//        this.custodyBalance = custodyBalance;
//    }

    public Account(@JsonProperty("customId") final String customId, @JsonProperty("status") final String status,
                   @JsonProperty("balance") final int balance, @JsonProperty("custodyBalance") final int custodyBalance) {
        this.customId = customId;
        this.status = status;
//        this.region = region;
        this.balance = balance;
        this.custodyBalance = custodyBalance;
    }

//    @Override
//    public boolean equals(final Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if ((obj == null) || (getClass() != obj.getClass())) {
//            return false;
//        }
//
//        Account other = (Account) obj;
//
//        return Objects.deepEquals(new String[] {getCustomId(), getStatus(), getBalance() getCustodyBalance()},
//                new String[] {other.getCustomId(), other.getStatus(), other.getBalance(), other.getCustodyBalance()});
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomId(), getStatus(), getBalance(), getCustodyBalance());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [customId=" + customId + ", status="
                + status + ", balance=" + balance + ", custodyBalance=" + custodyBalance + "]";
    }
}