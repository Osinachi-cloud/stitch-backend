package com.stitch.model.dtos;

import lombok.Data;
import java.util.Date;
@Data
public class VTPassMultichoiceDTO {
    private String code;
    private Content content;

    @Data
    public static class Content {
        private String Customer_Name;
        private String Status;
        private Date DUE_DATE;
        private Long Customer_Number;
        private String Customer_Type;
        private String Current_Bouquet;
        private String Current_Bouquet_Code;
        private Integer Renewal_Amount;
    }
}
