package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocTermList {
    @SerializedName("termDetailId")
    @Expose
    private Integer termDetailId;
    @SerializedName("termId")
    @Expose
    private Integer termId;
    @SerializedName("termDesc")
    @Expose
    private String termDesc;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("sortNo")
    @Expose
    private Integer sortNo;
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

    public Integer getTermDetailId() {
        return termDetailId;
    }

    public void setTermDetailId(Integer termDetailId) {
        this.termDetailId = termDetailId;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTermDesc() {
        return termDesc;
    }

    public void setTermDesc(String termDesc) {
        this.termDesc = termDesc;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
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
        return "DocTermList{" +
                "termDetailId=" + termDetailId +
                ", termId=" + termId +
                ", termDesc='" + termDesc + '\'' +
                ", delStatus=" + delStatus +
                ", sortNo=" + sortNo +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                '}';
    }
}
