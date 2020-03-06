package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bank {
    @SerializedName("bankDetId")
    @Expose
    private Integer bankDetId;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("bankIfsc")
    @Expose
    private String bankIfsc;
    @SerializedName("bankAddress")
    @Expose
    private String bankAddress;
    @SerializedName("accType")
    @Expose
    private Integer accType;
    @SerializedName("accNo")
    @Expose
    private String accNo;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
    @SerializedName("exInt2")
    @Expose
    private Integer exInt2;
    @SerializedName("exVar1")
    @Expose
    private String exVar1;
    @SerializedName("exVar2")
    @Expose
    private String exVar2;

    public Integer getBankDetId() {
        return bankDetId;
    }

    public void setBankDetId(Integer bankDetId) {
        this.bankDetId = bankDetId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public Integer getAccType() {
        return accType;
    }

    public void setAccType(Integer accType) {
        this.accType = accType;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExInt2() {
        return exInt2;
    }

    public void setExInt2(Integer exInt2) {
        this.exInt2 = exInt2;
    }

    public String getExVar1() {
        return exVar1;
    }

    public void setExVar1(String exVar1) {
        this.exVar1 = exVar1;
    }

    public String getExVar2() {
        return exVar2;
    }

    public void setExVar2(String exVar2) {
        this.exVar2 = exVar2;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankDetId=" + bankDetId +
                ", companyId=" + companyId +
                ", bankName='" + bankName + '\'' +
                ", bankIfsc='" + bankIfsc + '\'' +
                ", bankAddress='" + bankAddress + '\'' +
                ", accType=" + accType +
                ", accNo='" + accNo + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", delStatus=" + delStatus +
                ", isUsed=" + isUsed +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                '}';
    }
}
