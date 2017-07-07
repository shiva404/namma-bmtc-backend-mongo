package com.intuit.types;

/***
 *
 */
public class ThanksCount {

    private String refToken;
    private Integer thanksCount;

    public ThanksCount() {
    }

    public ThanksCount(String refToken, Integer thanksCount) {

        this.refToken = refToken;
        this.thanksCount = thanksCount;
    }

    public String getRefToken() {
        return refToken;
    }

    public void setRefToken(String refToken) {
        this.refToken = refToken;
    }

    public Integer getThanksCount() {
        return thanksCount;
    }

    public void setThanksCount(Integer thanksCount) {
        this.thanksCount = thanksCount;
    }
}
