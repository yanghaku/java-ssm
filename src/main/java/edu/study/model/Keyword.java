package edu.study.model;

public class Keyword {

    private String keyword;

    private Double tfidf;

    public Keyword(String k,Double t){
        keyword = k;
        tfidf = t;
    }
    public Keyword(){}

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Double getTfidf() {
        return tfidf;
    }

    public void setTfidf(Double tfidf) {
        this.tfidf = tfidf;
    }

}
