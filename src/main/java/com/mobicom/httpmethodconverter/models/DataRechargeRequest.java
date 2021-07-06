package com.mobicom.httpmethodconverter.models;

/**
 *
 * @author uitumen.t
 */
public class DataRechargeRequest {

    private String isdn;
    private double price;
    private String bagts;

    public String getBagts() {
        return bagts;
    }

    public void setBagts(String bagts) {
        this.bagts = bagts;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    private String data;
    private String ruleId;

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setIsdn(String isdn) {
        isdn = trimIsdn(isdn);
        this.isdn = isdn;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    private String trimIsdn(String isdn) {
        isdn = isdn.trim();
        if (isdn.length() > 8 && isdn.substring(0, 3).equals("976")) {
            return isdn.substring(3, isdn.length());
        }
        return isdn;
    }
}
