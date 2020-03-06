package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuotDetPrint {

    @SerializedName("quotDetailId")
    @Expose
    private Integer quotDetailId;
    @SerializedName("quotHeadId")
    @Expose
    private Integer quotHeadId;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("quotQty")
    @Expose
    private float quotQty;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("plantId")
    @Expose
    private Integer plantId;
    @SerializedName("plantName")
    @Expose
    private String plantName;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("taxPer")
    @Expose
    private Integer taxPer;
    @SerializedName("taxableValue")
    @Expose
    private Double taxableValue;
    @SerializedName("taxValue")
    @Expose
    private Double taxValue;
    @SerializedName("isTaxInc")
    @Expose
    private Integer isTaxInc;
    @SerializedName("uomName")
    @Expose
    private String uomName;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
    @SerializedName("quotUomId")
    @Expose
    private Integer quotUomId;
    @SerializedName("quotNo")
    @Expose
    private String quotNo;
    @SerializedName("quotDate")
    @Expose
    private String quotDate;

    public Integer getQuotDetailId() {
        return quotDetailId;
    }

    public void setQuotDetailId(Integer quotDetailId) {
        this.quotDetailId = quotDetailId;
    }

    public Integer getQuotHeadId() {
        return quotHeadId;
    }

    public void setQuotHeadId(Integer quotHeadId) {
        this.quotHeadId = quotHeadId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public float getQuotQty() {
        return quotQty;
    }

    public void setQuotQty(float quotQty) {
        this.quotQty = quotQty;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(Integer taxPer) {
        this.taxPer = taxPer;
    }

    public Double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(Double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public Double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(Double taxValue) {
        this.taxValue = taxValue;
    }

    public Integer getIsTaxInc() {
        return isTaxInc;
    }

    public void setIsTaxInc(Integer isTaxInc) {
        this.isTaxInc = isTaxInc;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getQuotUomId() {
        return quotUomId;
    }

    public void setQuotUomId(Integer quotUomId) {
        this.quotUomId = quotUomId;
    }

    public String getQuotNo() {
        return quotNo;
    }

    public void setQuotNo(String quotNo) {
        this.quotNo = quotNo;
    }

    public String getQuotDate() {
        return quotDate;
    }

    public void setQuotDate(String quotDate) {
        this.quotDate = quotDate;
    }

    @Override
    public String toString() {
        return "QuotDetPrint{" +
                "quotDetailId=" + quotDetailId +
                ", quotHeadId=" + quotHeadId +
                ", itemId=" + itemId +
                ", quotQty=" + quotQty +
                ", rate=" + rate +
                ", plantId=" + plantId +
                ", plantName='" + plantName + '\'' +
                ", total=" + total +
                ", taxPer=" + taxPer +
                ", taxableValue=" + taxableValue +
                ", taxValue=" + taxValue +
                ", isTaxInc=" + isTaxInc +
                ", uomName='" + uomName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", quotUomId=" + quotUomId +
                ", quotNo='" + quotNo + '\'' +
                ", quotDate='" + quotDate + '\'' +
                '}';
    }
}
