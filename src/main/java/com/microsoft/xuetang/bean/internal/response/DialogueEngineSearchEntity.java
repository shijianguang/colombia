package com.microsoft.xuetang.bean.internal.response;

import com.microsoft.xuetang.internalrpc.response.BingPaperProfileResponse;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class DialogueEngineSearchEntity {
    private double logprob;
    private Long Id;
    private String Ty;
    private String Ti;
    private String L;
    private String Y;
    private String D;
    private String CC;
    private String ECC;
    private List<Long> RId;
    private String E;
    private List<String> W;
    private List<Map<String, Object>> AA;
    private List<Map<String, Object>> F;
    private Map<String, Object> J;
    private BingPaperProfileResponse paperProfile;

    public double getLogprob() {
        return logprob;
    }

    @JsonProperty("logprob")
    public void setLogprob(double logprob) {
        this.logprob = logprob;
    }

    public Long getId() {
        return Id;
    }

    @JsonProperty("Id")
    public void setId(Long id) {
        Id = id;
    }

    public String getTy() {
        return Ty;
    }

    @JsonProperty("Ty")
    public void setTy(String ty) {
        Ty = ty;
    }

    public String getTi() {
        return Ti;
    }

    @JsonProperty("Ti")
    public void setTi(String ti) {
        Ti = ti;
    }

    public String getL() {
        return L;
    }

    @JsonProperty("L")
    public void setL(String l) {
        L = l;
    }

    public String getY() {
        return Y;
    }

    @JsonProperty("Y")
    public void setY(String y) {
        Y = y;
    }

    public String getD() {
        return D;
    }

    @JsonProperty("D")
    public void setD(String d) {
        D = d;
    }

    public String getCC() {
        return CC;
    }

    @JsonProperty("CC")
    public void setCC(String CC) {
        this.CC = CC;
    }

    public String getECC() {
        return ECC;
    }

    @JsonProperty("ECC")
    public void setECC(String ECC) {
        this.ECC = ECC;
    }

    public List<Long> getRId() {
        return RId;
    }

    @JsonProperty("RId")
    public void setRId(List<Long> RId) {
        this.RId = RId;
    }

    public String getE() {
        return E;
    }

    @JsonProperty("E")
    public void setE(String e) {
        E = e;
    }

    public List<String> getW() {
        return W;
    }

    @JsonProperty("W")
    public void setW(List<String> w) {
        W = w;
    }

    public List<Map<String, Object>> getAA() {
        return AA;
    }

    @JsonProperty("AA")
    public void setAA(List<Map<String, Object>> AA) {
        this.AA = AA;
    }

    public List<Map<String, Object>> getF() {
        return F;
    }

    @JsonProperty("F")
    public void setF(List<Map<String, Object>> f) {
        F = f;
    }

    public Map<String, Object> getJ() {
        return J;
    }

    @JsonProperty("J")
    public void setJ(Map<String, Object> j) {
        J = j;
    }

    public BingPaperProfileResponse getPaperProfile() {
        return paperProfile;
    }

    public void setPaperProfile(BingPaperProfileResponse paperProfile) {
        this.paperProfile = paperProfile;
    }

}
