package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Proj {
    @SerializedName("projId")
    @Expose
    private Integer projId;
    @SerializedName("projName")
    @Expose
    private String projName;
    @SerializedName("custId")
    @Expose
    private Integer custId;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("contactPerMob")
    @Expose
    private String contactPerMob;
    @SerializedName("contactPerName")
    @Expose
    private String contactPerName;
    @SerializedName("pincode")
    @Expose
    private Object pincode;
    @SerializedName("km")
    @Expose
    private Integer km;
    @SerializedName("address")
    @Expose
    private String address;

    public Integer getProjId() {
        return projId;
    }

    public void setProjId(Integer projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getContactPerMob() {
        return contactPerMob;
    }

    public void setContactPerMob(String contactPerMob) {
        this.contactPerMob = contactPerMob;
    }

    public String getContactPerName() {
        return contactPerName;
    }

    public void setContactPerName(String contactPerName) {
        this.contactPerName = contactPerName;
    }

    public Object getPincode() {
        return pincode;
    }

    public void setPincode(Object pincode) {
        this.pincode = pincode;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Proj{" +
                "projId=" + projId +
                ", projName='" + projName + '\'' +
                ", custId=" + custId +
                ", location='" + location + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", isUsed=" + isUsed +
                ", delStatus=" + delStatus +
                ", contactPerMob='" + contactPerMob + '\'' +
                ", contactPerName='" + contactPerName + '\'' +
                ", pincode=" + pincode +
                ", km=" + km +
                ", address='" + address + '\'' +
                '}';
    }
}
