package com.stitch.gateway.test;

import java.time.LocalDateTime;
public class PostTranOfficePayload {
    private long postTranId;
    private long postTranCustId;
    private long settledEntityId;
    private int batchNumber;
    private String sinkNodeName;
    private int tranPostilionOriginated;
    private String messageType;
    private String tranType;
    private long tranNumber;
    private String systemTraceAuditNumber;
    private String rspCodeReq;
    private String rspCodeRsp;
    private String authIdRsp;
    private String authType;
    private String acquiringInstitutionIdCode;
    private String messageReasonCode;
    private LocalDateTime datetimeTranGMT;
    private LocalDateTime dateTimeTranLocal;
    private LocalDateTime dateTimeReq;
    private LocalDateTime dateTimeRsp;
    private LocalDateTime realtimeBusinessDate;
    private LocalDateTime reconBusinessDate;
    private String fromAccountType;
    private String toAccountType;

    public long getPostTranId() {
        return postTranId;
    }

    public void setPostTranId(long postTranId) {
        this.postTranId = postTranId;
    }

    public long getPostTranCustId() {
        return postTranCustId;
    }

    public void setPostTranCustId(long postTranCustId) {
        this.postTranCustId = postTranCustId;
    }

    public long getSettledEntityId() {
        return settledEntityId;
    }

    public void setSettledEntityId(long settledEntityId) {
        this.settledEntityId = settledEntityId;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getSinkNodeName() {
        return sinkNodeName;
    }

    public void setSinkNodeName(String sinkNodeName) {
        this.sinkNodeName = sinkNodeName;
    }

    public int getTranPostilionOriginated() {
        return tranPostilionOriginated;
    }

    public void setTranPostilionOriginated(int tranPostilionOriginated) {
        this.tranPostilionOriginated = tranPostilionOriginated;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public long getTranNumber() {
        return tranNumber;
    }

    public void setTranNumber(long tranNumber) {
        this.tranNumber = tranNumber;
    }

    public String getSystemTraceAuditNumber() {
        return systemTraceAuditNumber;
    }

    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = systemTraceAuditNumber;
    }

    public String getRspCodeReq() {
        return rspCodeReq;
    }

    public void setRspCodeReq(String rspCodeReq) {
        this.rspCodeReq = rspCodeReq;
    }

    public String getRspCodeRsp() {
        return rspCodeRsp;
    }

    public void setRspCodeRsp(String rspCodeRsp) {
        this.rspCodeRsp = rspCodeRsp;
    }

    public String getAuthIdRsp() {
        return authIdRsp;
    }

    public void setAuthIdRsp(String authIdRsp) {
        this.authIdRsp = authIdRsp;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAcquiringInstitutionIdCode() {
        return acquiringInstitutionIdCode;
    }

    public void setAcquiringInstitutionIdCode(String acquiringInstitutionIdCode) {
        this.acquiringInstitutionIdCode = acquiringInstitutionIdCode;
    }

    public String getMessageReasonCode() {
        return messageReasonCode;
    }

    public void setMessageReasonCode(String messageReasonCode) {
        this.messageReasonCode = messageReasonCode;
    }

    public LocalDateTime getDatetimeTranGMT() {
        return datetimeTranGMT;
    }

    public void setDatetimeTranGMT(LocalDateTime datetimeTranGMT) {
        this.datetimeTranGMT = datetimeTranGMT;
    }

    public LocalDateTime getDateTimeTranLocal() {
        return dateTimeTranLocal;
    }

    public void setDateTimeTranLocal(LocalDateTime dateTimeTranLocal) {
        this.dateTimeTranLocal = dateTimeTranLocal;
    }

    public LocalDateTime getDateTimeReq() {
        return dateTimeReq;
    }

    public void setDateTimeReq(LocalDateTime dateTimeReq) {
        this.dateTimeReq = dateTimeReq;
    }

    public LocalDateTime getDateTimeRsp() {
        return dateTimeRsp;
    }

    public void setDateTimeRsp(LocalDateTime dateTimeRsp) {
        this.dateTimeRsp = dateTimeRsp;
    }

    public LocalDateTime getRealtimeBusinessDate() {
        return realtimeBusinessDate;
    }

    public void setRealtimeBusinessDate(LocalDateTime realtimeBusinessDate) {
        this.realtimeBusinessDate = realtimeBusinessDate;
    }

    public LocalDateTime getReconBusinessDate() {
        return reconBusinessDate;
    }

    public void setReconBusinessDate(LocalDateTime reconBusinessDate) {
        this.reconBusinessDate = reconBusinessDate;
    }

    public String getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(String fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }
}