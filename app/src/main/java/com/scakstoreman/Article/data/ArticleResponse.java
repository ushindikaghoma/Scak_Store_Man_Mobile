package com.scakstoreman.Article.data;

import com.google.gson.annotations.SerializedName;

public class ArticleResponse {
    @SerializedName("CodeArticle")
    String codeArticle;
    @SerializedName("DesegnationArticle")
    String designationArticle;
    @SerializedName("PrixAchat")
    double prixAchat;

    public ArticleResponse(String codeArticle, String designationArticle, double prixAchat) {
        this.codeArticle = codeArticle;
        this.designationArticle = designationArticle;
        this.prixAchat = prixAchat;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesignationArticle() {
        return designationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        this.designationArticle = designationArticle;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }
}
