package com.fuelprice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mimit.carburanti")
public class MimitProperties {
    private String stationsUrl;
    private String pricesUrl;

    public String getStationsUrl() { return stationsUrl; }
    public void setStationsUrl(String stationsUrl) { this.stationsUrl = stationsUrl; }

    public String getPricesUrl() { return pricesUrl; }
    public void setPricesUrl(String pricesUrl) { this.pricesUrl = pricesUrl; }
}
