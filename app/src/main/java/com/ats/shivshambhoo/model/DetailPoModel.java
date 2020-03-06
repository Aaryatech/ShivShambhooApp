package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailPoModel {

    @SerializedName("poId")
    @Expose
    private Integer poId;
    @SerializedName("poNo")
    @Expose
    private String poNo;
    @SerializedName("poDate")
    @Expose
    private String poDate;
    @SerializedName("custId")
    @Expose
    private Integer custId;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("custProjectId")
    @Expose
    private Integer custProjectId;
    @SerializedName("poDocument")
    @Expose
    private String poDocument;
    @SerializedName("poDocument1")
    @Expose
    private String poDocument1;
    @SerializedName("quatationId")
    @Expose
    private Integer quatationId;
    @SerializedName("quatationNo")
    @Expose
    private String quatationNo;
    @SerializedName("poValidityDate")
    @Expose
    private String poValidityDate;
    @SerializedName("poTermId")
    @Expose
    private Integer poTermId;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("plantId")
    @Expose
    private Integer plantId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("extra1")
    @Expose
    private Integer extra1;
    @SerializedName("extra2")
    @Expose
    private Integer extra2;
    @SerializedName("bool1")
    @Expose
    private Integer bool1;
    @SerializedName("bool2")
    @Expose
    private Integer bool2;
    @SerializedName("varchar1")
    @Expose
    private String varchar1;
    @SerializedName("varchar2")
    @Expose
    private Object varchar2;
    @SerializedName("extraDate1")
    @Expose
    private Object extraDate1;
    @SerializedName("extraDate2")
    @Expose
    private Object extraDate2;
    @SerializedName("custName")
    @Expose
    private String custName;
    @SerializedName("plantName")
    @Expose
    private String plantName;
    @SerializedName("payTerm")
    @Expose
    private String payTerm;
    @SerializedName("projName")
    @Expose
    private String projName;
    @SerializedName("qutDate")
    @Expose
    private String qutDate;
    @SerializedName("getPoDetailList")
    @Expose
    private List<GetPoDetailList> getPoDetailList ;

    public DetailPoModel(Integer poId, String poNo, String poDate, Integer custId, String remark, Integer custProjectId, String poDocument, String poDocument1, Integer quatationId, String quatationNo, String poValidityDate, Integer poTermId, Integer delStatus, Integer plantId, Integer status, Integer extra1, Integer extra2, Integer bool1, Integer bool2, String varchar1, Object varchar2, Object extraDate1, Object extraDate2, String custName, String plantName, String payTerm, String projName, String qutDate, List<GetPoDetailList> getPoDetailList) {
        this.poId = poId;
        this.poNo = poNo;
        this.poDate = poDate;
        this.custId = custId;
        this.remark = remark;
        this.custProjectId = custProjectId;
        this.poDocument = poDocument;
        this.poDocument1 = poDocument1;
        this.quatationId = quatationId;
        this.quatationNo = quatationNo;
        this.poValidityDate = poValidityDate;
        this.poTermId = poTermId;
        this.delStatus = delStatus;
        this.plantId = plantId;
        this.status = status;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.bool1 = bool1;
        this.bool2 = bool2;
        this.varchar1 = varchar1;
        this.varchar2 = varchar2;
        this.extraDate1 = extraDate1;
        this.extraDate2 = extraDate2;
        this.custName = custName;
        this.plantName = plantName;
        this.payTerm = payTerm;
        this.projName = projName;
        this.qutDate = qutDate;
        this.getPoDetailList = getPoDetailList;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCustProjectId() {
        return custProjectId;
    }

    public void setCustProjectId(Integer custProjectId) {
        this.custProjectId = custProjectId;
    }

    public String getPoDocument() {
        return poDocument;
    }

    public void setPoDocument(String poDocument) {
        this.poDocument = poDocument;
    }

    public String getPoDocument1() {
        return poDocument1;
    }

    public void setPoDocument1(String poDocument1) {
        this.poDocument1 = poDocument1;
    }

    public Integer getQuatationId() {
        return quatationId;
    }

    public void setQuatationId(Integer quatationId) {
        this.quatationId = quatationId;
    }

    public String getQuatationNo() {
        return quatationNo;
    }

    public void setQuatationNo(String quatationNo) {
        this.quatationNo = quatationNo;
    }

    public String getPoValidityDate() {
        return poValidityDate;
    }

    public void setPoValidityDate(String poValidityDate) {
        this.poValidityDate = poValidityDate;
    }

    public Integer getPoTermId() {
        return poTermId;
    }

    public void setPoTermId(Integer poTermId) {
        this.poTermId = poTermId;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getBool1() {
        return bool1;
    }

    public void setBool1(Integer bool1) {
        this.bool1 = bool1;
    }

    public Integer getBool2() {
        return bool2;
    }

    public void setBool2(Integer bool2) {
        this.bool2 = bool2;
    }

    public String getVarchar1() {
        return varchar1;
    }

    public void setVarchar1(String varchar1) {
        this.varchar1 = varchar1;
    }

    public Object getVarchar2() {
        return varchar2;
    }

    public void setVarchar2(Object varchar2) {
        this.varchar2 = varchar2;
    }

    public Object getExtraDate1() {
        return extraDate1;
    }

    public void setExtraDate1(Object extraDate1) {
        this.extraDate1 = extraDate1;
    }

    public Object getExtraDate2() {
        return extraDate2;
    }

    public void setExtraDate2(Object extraDate2) {
        this.extraDate2 = extraDate2;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPayTerm() {
        return payTerm;
    }

    public void setPayTerm(String payTerm) {
        this.payTerm = payTerm;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getQutDate() {
        return qutDate;
    }

    public void setQutDate(String qutDate) {
        this.qutDate = qutDate;
    }

    public List<GetPoDetailList> getGetPoDetailList() {
        return getPoDetailList;
    }

    public void setGetPoDetailList(List<GetPoDetailList> getPoDetailList) {
        this.getPoDetailList = getPoDetailList;
    }


    @Override
    public String toString() {
        return "DetailPoModel{" +
                "poId=" + poId +
                ", poNo='" + poNo + '\'' +
                ", poDate='" + poDate + '\'' +
                ", custId=" + custId +
                ", remark='" + remark + '\'' +
                ", custProjectId=" + custProjectId +
                ", poDocument='" + poDocument + '\'' +
                ", poDocument1='" + poDocument1 + '\'' +
                ", quatationId=" + quatationId +
                ", quatationNo='" + quatationNo + '\'' +
                ", poValidityDate='" + poValidityDate + '\'' +
                ", poTermId=" + poTermId +
                ", delStatus=" + delStatus +
                ", plantId=" + plantId +
                ", status=" + status +
                ", extra1=" + extra1 +
                ", extra2=" + extra2 +
                ", bool1=" + bool1 +
                ", bool2=" + bool2 +
                ", varchar1='" + varchar1 + '\'' +
                ", varchar2=" + varchar2 +
                ", extraDate1=" + extraDate1 +
                ", extraDate2=" + extraDate2 +
                ", custName='" + custName + '\'' +
                ", plantName='" + plantName + '\'' +
                ", payTerm='" + payTerm + '\'' +
                ", projName='" + projName + '\'' +
                ", qutDate='" + qutDate + '\'' +
                ", getPoDetailList=" + getPoDetailList +
                '}';
    }
}
