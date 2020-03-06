package com.ats.shivshambhoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PdfModel {
    @SerializedName("comp")
    @Expose
    private Comp comp;
    @SerializedName("cust")
    @Expose
    private Cust cust;
    @SerializedName("quotDetPrint")
    @Expose
    private List<QuotDetPrint> quotDetPrint = null;
    @SerializedName("docTermList")
    @Expose
    private List<DocTermList> docTermList = null;
    @SerializedName("payTerm")
    @Expose
    private PayTerm payTerm;
    @SerializedName("proj")
    @Expose
    private Proj proj;
    @SerializedName("bank")
    @Expose
    private Bank bank;

    private List<Bank> banks=null;
    private List<Comp> comps=null;

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public List<Comp> getComps() {
        return comps;
    }

    public void setComps(List<Comp> comps) {
        this.comps = comps;
    }

    public Comp getComp() {
        return comp;
    }

    public void setComp(Comp comp) {
        this.comp = comp;
    }

    public Cust getCust() {
        return cust;
    }

    public void setCust(Cust cust) {
        this.cust = cust;
    }

    public List<QuotDetPrint> getQuotDetPrint() {
        return quotDetPrint;
    }

    public void setQuotDetPrint(List<QuotDetPrint> quotDetPrint) {
        this.quotDetPrint = quotDetPrint;
    }

    public List<DocTermList> getDocTermList() {
        return docTermList;
    }

    public void setDocTermList(List<DocTermList> docTermList) {
        this.docTermList = docTermList;
    }

    public PayTerm getPayTerm() {
        return payTerm;
    }

    public void setPayTerm(PayTerm payTerm) {
        this.payTerm = payTerm;
    }

    public Proj getProj() {
        return proj;
    }

    public void setProj(Proj proj) {
        this.proj = proj;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "PdfModel{" +
                "comp=" + comp +
                ", cust=" + cust +
                ", quotDetPrint=" + quotDetPrint +
                ", docTermList=" + docTermList +
                ", payTerm=" + payTerm +
                ", proj=" + proj +
                ", bank=" + bank +
                ", banks=" + banks +
                ", comps=" + comps +
                '}';
    }
}
