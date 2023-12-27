package com.stitch.model.dtos;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class DataPlanContent implements Serializable {

    private static final long serialVersionUID = 8234356L;

    private String response_description;
    private Content content;

    @Data
    public static class Content implements Serializable {
        private static final long serialVersionUID = 8230356L;
        private String ServiceName;
        private String serviceID;
        private String convinience_fee;
        private List<Variation> varations;
    }

    @Data
    public static class Variation implements Serializable {
        private static final long serialVersionUID = 3234356L;
        private String variation_code;
        private String name;
        private String variation_amount;
        private String fixedPrice;
    }
}
