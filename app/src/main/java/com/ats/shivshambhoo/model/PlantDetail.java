package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlantDetail {
    @SerializedName("plantId")
    @Expose
    private Integer plantId;
    @SerializedName("plantName")
    @Expose
    private String plantName;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("plantAddress1")
    @Expose
    private String plantAddress1;
    @SerializedName("plantAddress2")
    @Expose
    private String plantAddress2;
    @SerializedName("plantContactNo1")
    @Expose
    private String plantContactNo1;
    @SerializedName("plantContactNo2")
    @Expose
    private String plantContactNo2;
    @SerializedName("plantFax1")
    @Expose
    private String plantFax1;
    @SerializedName("plantFax2")
    @Expose
    private String plantFax2;
    @SerializedName("plantHead")
    @Expose
    private Integer plantHead;
    @SerializedName("plantEmail1")
    @Expose
    private String plantEmail1;
    @SerializedName("plantEmail2")
    @Expose
    private String plantEmail2;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
    @SerializedName("exInt2")
    @Expose
    private Integer exInt2;
    @SerializedName("exInt3")
    @Expose
    private Integer exInt3;
    @SerializedName("exVar1")
    @Expose
    private String exVar1;
    @SerializedName("exVar2")
    @Expose
    private String exVar2;
    @SerializedName("exVar3")
    @Expose
    private String exVar3;
    @SerializedName("exDate1")
    @Expose
    private String exDate1;
    @SerializedName("exDate2")
    @Expose
    private String exDate2;
    @SerializedName("exBool1")
    @Expose
    private Integer exBool1;
    @SerializedName("exBool2")
    @Expose
    private Integer exBool2;
    @SerializedName("exBool3")
    @Expose
    private Integer exBool3;

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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getPlantAddress1() {
        return plantAddress1;
    }

    public void setPlantAddress1(String plantAddress1) {
        this.plantAddress1 = plantAddress1;
    }

    public String getPlantAddress2() {
        return plantAddress2;
    }

    public void setPlantAddress2(String plantAddress2) {
        this.plantAddress2 = plantAddress2;
    }

    public String getPlantContactNo1() {
        return plantContactNo1;
    }

    public void setPlantContactNo1(String plantContactNo1) {
        this.plantContactNo1 = plantContactNo1;
    }

    public String getPlantContactNo2() {
        return plantContactNo2;
    }

    public void setPlantContactNo2(String plantContactNo2) {
        this.plantContactNo2 = plantContactNo2;
    }

    public String getPlantFax1() {
        return plantFax1;
    }

    public void setPlantFax1(String plantFax1) {
        this.plantFax1 = plantFax1;
    }

    public String getPlantFax2() {
        return plantFax2;
    }

    public void setPlantFax2(String plantFax2) {
        this.plantFax2 = plantFax2;
    }

    public Integer getPlantHead() {
        return plantHead;
    }

    public void setPlantHead(Integer plantHead) {
        this.plantHead = plantHead;
    }

    public String getPlantEmail1() {
        return plantEmail1;
    }

    public void setPlantEmail1(String plantEmail1) {
        this.plantEmail1 = plantEmail1;
    }

    public String getPlantEmail2() {
        return plantEmail2;
    }

    public void setPlantEmail2(String plantEmail2) {
        this.plantEmail2 = plantEmail2;
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

    public Integer getExInt3() {
        return exInt3;
    }

    public void setExInt3(Integer exInt3) {
        this.exInt3 = exInt3;
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

    public String getExVar3() {
        return exVar3;
    }

    public void setExVar3(String exVar3) {
        this.exVar3 = exVar3;
    }

    public String getExDate1() {
        return exDate1;
    }

    public void setExDate1(String exDate1) {
        this.exDate1 = exDate1;
    }

    public String getExDate2() {
        return exDate2;
    }

    public void setExDate2(String exDate2) {
        this.exDate2 = exDate2;
    }

    public Integer getExBool1() {
        return exBool1;
    }

    public void setExBool1(Integer exBool1) {
        this.exBool1 = exBool1;
    }

    public Integer getExBool2() {
        return exBool2;
    }

    public void setExBool2(Integer exBool2) {
        this.exBool2 = exBool2;
    }

    public Integer getExBool3() {
        return exBool3;
    }

    public void setExBool3(Integer exBool3) {
        this.exBool3 = exBool3;
    }

    @Override
    public String toString() {
        return "PlantDetail{" +
                "plantId=" + plantId +
                ", plantName='" + plantName + '\'' +
                ", companyId=" + companyId +
                ", plantAddress1='" + plantAddress1 + '\'' +
                ", plantAddress2='" + plantAddress2 + '\'' +
                ", plantContactNo1='" + plantContactNo1 + '\'' +
                ", plantContactNo2='" + plantContactNo2 + '\'' +
                ", plantFax1='" + plantFax1 + '\'' +
                ", plantFax2='" + plantFax2 + '\'' +
                ", plantHead=" + plantHead +
                ", plantEmail1='" + plantEmail1 + '\'' +
                ", plantEmail2='" + plantEmail2 + '\'' +
                ", isUsed=" + isUsed +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", exDate1='" + exDate1 + '\'' +
                ", exDate2='" + exDate2 + '\'' +
                ", exBool1=" + exBool1 +
                ", exBool2=" + exBool2 +
                ", exBool3=" + exBool3 +
                '}';
    }
}
