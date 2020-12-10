package org.emberon.winscan.domain.entity;

public class Rewards {
    private String company;
    private int value;
    private rewardStatus currentStatus;

    public Rewards() {

    }

    public Rewards(String company, int value, rewardStatus currentStatus) {
        this.company = company;
        this.value = value;
        this.currentStatus = currentStatus;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public rewardStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(rewardStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public enum rewardStatus {
        CLAIMED, UNCLAIMED
    }
}
