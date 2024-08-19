package com.fujitsu.delivery_fee_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationDTO {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "wmocode")
    private String wmocode;

    @JacksonXmlProperty(localName = "airtemperature")
    private String airtemperature;

    @JacksonXmlProperty(localName = "windspeed")
    private String windspeed;

    @JacksonXmlProperty(localName = "phenomenon")
    private String phenomenon;
}