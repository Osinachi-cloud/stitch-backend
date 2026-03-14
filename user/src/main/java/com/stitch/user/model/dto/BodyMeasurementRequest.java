package com.stitch.user.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BodyMeasurementRequest {
    private String tag;
    private int neck;
    private int shoulder;
    private int chest;
    private int tummy;
    private int hipWidth;
    private int neckToHipLength;
    private int shortSleeveAtBiceps;
    private int midSleeveAtElbow;
    private int longSleeveAtWrist;
    private int waist;
    private int thigh;
    private int knee;
    private int ankle;
    private int trouserLength;

    @JsonProperty("isDefault")
    private boolean isDefault;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getThigh() {
        return thigh;
    }

    public void setThigh(int thigh) {
        this.thigh = thigh;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public int getTrouserLength() {
        return trouserLength;
    }

    public void setTrouserLength(int trouserLength) {
        this.trouserLength = trouserLength;
    }

    public int getAnkle() {
        return ankle;
    }

    public void setAnkle(int ankle) {
        this.ankle = ankle;
    }

    public int getKnee() {
        return knee;
    }

    public void setKnee(int knee) {
        this.knee = knee;
    }

    public int getLongSleeveAtWrist() {
        return longSleeveAtWrist;
    }

    public void setLongSleeveAtWrist(int longSleeveAtWrist) {
        this.longSleeveAtWrist = longSleeveAtWrist;
    }

    public int getMidSleeveAtElbow() {
        return midSleeveAtElbow;
    }

    public void setMidSleeveAtElbow(int midSleeveAtElbow) {
        this.midSleeveAtElbow = midSleeveAtElbow;
    }

    public int getShortSleeveAtBiceps() {
        return shortSleeveAtBiceps;
    }

    public void setShortSleeveAtBiceps(int shortSleeveAtBiceps) {
        this.shortSleeveAtBiceps = shortSleeveAtBiceps;
    }

    public int getNeckToHipLength() {
        return neckToHipLength;
    }

    public void setNeckToHipLength(int neckToHipLength) {
        this.neckToHipLength = neckToHipLength;
    }

    public int getHipWidth() {
        return hipWidth;
    }

    public void setHipWidth(int hipWidth) {
        this.hipWidth = hipWidth;
    }

    public int getTummy() {
        return tummy;
    }

    public void setTummy(int tummy) {
        this.tummy = tummy;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public int getShoulder() {
        return shoulder;
    }

    public void setShoulder(int shoulder) {
        this.shoulder = shoulder;
    }

    public int getNeck() {
        return neck;
    }

    public void setNeck(int neck) {
        this.neck = neck;
    }

    @Override
    public String toString() {
        return "BodyMeasurementRequest{" +
                "tag='" + tag + '\'' +
                ", neck=" + neck +
                ", shoulder=" + shoulder +
                ", chest=" + chest +
                ", tummy=" + tummy +
                ", hipWidth=" + hipWidth +
                ", neckToHipLength=" + neckToHipLength +
                ", shortSleeveAtBiceps=" + shortSleeveAtBiceps +
                ", midSleeveAtElbow=" + midSleeveAtElbow +
                ", longSleeveAtWrist=" + longSleeveAtWrist +
                ", waist=" + waist +
                ", thigh=" + thigh +
                ", knee=" + knee +
                ", ankle=" + ankle +
                ", trouserLength=" + trouserLength +
                ", isDefault=" + isDefault +
                '}';
    }
}
