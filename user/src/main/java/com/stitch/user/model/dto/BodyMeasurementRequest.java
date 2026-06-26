package com.stitch.user.model.dto;

import lombok.Data;

@Data
public class BodyMeasurementRequest {
    private String tag;
    private double neck;
    private double shoulder;
    private double chest;
    private double tummy;
    private double hipWidth;
    private double neckToHipLength;
    private double shortSleeveAtBiceps;
    private double midSleeveAtElbow;
    private double longSleeveAtWrist;
    private double waist;
    private double thigh;
    private double knee;
    private double ankle;
    private double trouserLength;
}
