package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPoDetailList {

    @SerializedName("poDetailId")
    @Expose
    private Integer poDetailId;
    @SerializedName("poId")
    @Expose
    private Integer poId;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("poRate")
    @Expose
    private float poRate;
    @SerializedName("poQty")
    @Expose
    private float poQty;
    @SerializedName("poConsumeQty")
    @Expose
    private Integer poConsumeQty;
    @SerializedName("poRemainingQty")
    @Expose
    private float poRemainingQty;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("taxAmt")
    @Expose
    private Double taxAmt;
    @SerializedName("taxPer")
    @Expose
    private Integer taxPer;
    @SerializedName("taxableAmt")
    @Expose
    private float taxableAmt;
    @SerializedName("otherCharges")
    @Expose
    private float otherCharges;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("quDetailId")
    @Expose
    private Integer quDetailId;
    @SerializedName("varchar1")
    @Expose
    private String varchar1;
    @SerializedName("varchar2")
    @Expose
    private String varchar2;
    @SerializedName("extra1")
    @Expose
    private Integer extra1;
    @SerializedName("extra2")
    @Expose
    private Integer extra2;
    @SerializedName("itemName")
    @Expose
    private String itemName;

    public GetPoDetailList(Integer poDetailId, Integer poId, Integer itemId, Integer poRate, Integer poQty, Integer poConsumeQty, Integer poRemainingQty, Integer status, String remark, Double taxAmt, Integer taxPer, Integer taxableAmt, float otherCharges, Double total, Integer quDetailId, String varchar1, String varchar2, Integer extra1, Integer extra2, String itemName) {
        this.poDetailId = poDetailId;
        this.poId = poId;
        this.itemId = itemId;
        this.poRate = poRate;
        this.poQty = poQty;
        this.poConsumeQty = poConsumeQty;
        this.poRemainingQty = poRemainingQty;
        this.status = status;
        this.remark = remark;
        this.taxAmt = taxAmt;
        this.taxPer = taxPer;
        this.taxableAmt = taxableAmt;
        this.otherCharges = otherCharges;
        this.total = total;
        this.quDetailId = quDetailId;
        this.varchar1 = varchar1;
        this.varchar2 = varchar2;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.itemName = itemName;
    }

    public Integer getPoDetailId() {
        return poDetailId;
    }

    public void setPoDetailId(Integer poDetailId) {
        this.poDetailId = poDetailId;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public float getPoRate() {
        return poRate;
    }

    public void setPoRate(float poRate) {
        this.poRate = poRate;
    }

    public float getPoQty() {
        return poQty;
    }

    public void setPoQty(float poQty) {
        this.poQty = poQty;
    }

    public Integer getPoConsumeQty() {
        return poConsumeQty;
    }

    public void setPoConsumeQty(Integer poConsumeQty) {
        this.poConsumeQty = poConsumeQty;
    }

    public float getPoRemainingQty() {
        return poRemainingQty;
    }

    public void setPoRemainingQty(float poRemainingQty) {
        this.poRemainingQty = poRemainingQty;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public Integer getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(Integer taxPer) {
        this.taxPer = taxPer;
    }

    public float getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(float taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public float getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(float otherCharges) {
        this.otherCharges = otherCharges;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getQuDetailId() {
        return quDetailId;
    }

    public void setQuDetailId(Integer quDetailId) {
        this.quDetailId = quDetailId;
    }

    public String getVarchar1() {
        return varchar1;
    }

    public void setVarchar1(String varchar1) {
        this.varchar1 = varchar1;
    }

    public String getVarchar2() {
        return varchar2;
    }

    public void setVarchar2(String varchar2) {
        this.varchar2 = varchar2;
    }

    public Integer getExtra1() {
        return extra1;
    }

    public void setExtra1(Integer extra1) {
        this.extra1 = extra1;
    }

    public Integer getExtra2() {
        return extra2;
    }

    public void setExtra2(Integer extra2) {
        this.extra2 = extra2;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "GetPoDetailList{" +
                "poDetailId=" + poDetailId +
                ", poId=" + poId +
                ", itemId=" + itemId +
                ", poRate=" + poRate +
                ", poQty=" + poQty +
                ", poConsumeQty=" + poConsumeQty +
                ", poRemainingQty=" + poRemainingQty +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", taxAmt=" + taxAmt +
                ", taxPer=" + taxPer +
                ", taxableAmt=" + taxableAmt +
                ", otherCharges=" + otherCharges +
                ", total=" + total +
                ", quDetailId=" + quDetailId +
                ", varchar1='" + varchar1 + '\'' +
                ", varchar2='" + varchar2 + '\'' +
                ", extra1=" + extra1 +
                ", extra2=" + extra2 +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
