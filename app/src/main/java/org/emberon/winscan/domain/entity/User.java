package org.emberon.winscan.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private List<Transaction> transactions = new ArrayList<>();

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
