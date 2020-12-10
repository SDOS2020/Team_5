package org.emberon.winscan.domain.entity;

import java.util.Date;

public class Transaction {

    private String id;
    private String payerName;
    private String payeeName;
    private String payerUpiId;
    private String payeeUpiId;
    private long amount;
    private Date transactionDate;
    private transactionStatus currentStatus;

    public enum transactionStatus {
        SUCCESSFUL, CANCELLED, FAILED, PENDING;
    }

    public Transaction() {

    }

    public Transaction(String id, String payerName, String payeeName, String payerUpiId,
                       String payeeUpiId, long amount, Date transactionDate,
                       transactionStatus currentStatus) {
        this.id = id;
        this.payerName = payerName;
        this.payeeName = payeeName;
        this.payerUpiId = payerUpiId;
        this.payeeUpiId = payeeUpiId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.currentStatus = currentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayerUpiId() {
        return payerUpiId;
    }

    public void setPayerUpiId(String payerUpiId) {
        this.payerUpiId = payerUpiId;
    }

    public String getPayeeUpiId() {
        return payeeUpiId;
    }

    public void setPayeeUpiId(String payeeUpiId) {
        this.payeeUpiId = payeeUpiId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public transactionStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(transactionStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
