package com.stitch.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VTPassCablePackageDto implements Serializable {

    private static final long serialVersionUID = 2234356L;

    private String response_description;
    private String code;
    private Content content;

    @Data
    public static class Content implements Serializable {
        private static final long serialVersionUID = 9434356L;
        private String ServiceName;
        @JsonProperty("Customer_Name")
        private String Customer_Name;
        @JsonProperty("Customer_Number")
        private String Customer_Number;
        private String serviceID;
        private String convinience_fee;
        private List<Variation> varations;
    }

    @Data
    public static class Variation implements Serializable {
        private static final long serialVersionUID = 6734356L;
        private String variation_code;
        private String name;
        private String variation_amount;
        private String fixedPrice;
    }
}
