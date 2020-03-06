package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayTerm {
    @SerializedName("payTermId")
    @Expose
    private Integer payTermId;
    @SerializedName("payTerm")
    @Expose
    private String payTerm;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;

    public Integer getPayTermId() {
        return payTermId;
    }

    public void setPayTermId(Integer payTermId) {
        this.payTermId = payTermId;
    }

    public String getPayTerm() {
        return payTerm;
    }

    public void setPayTerm(String payTerm) {
        this.payTerm = payTerm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    public String toString() {
        return "PayTerm{" +
                "payTermId=" + payTermId +
                ", payTerm='" + payTerm + '\'' +
                ", date='" + date + '\'' +
                ", isUsed=" + isUsed +
                ", delStatus=" + delStatus +
                '}';
    }
}
