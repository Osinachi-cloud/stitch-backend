package com.stitch.gateway.test;
import java.sql.Timestamp;
import java.util.Date;


public class Transaction {

    private String pan;
    private String stan;
    private String retrievalReferenceNr;
    private char messageType;
    private float amount;
    private String uniqueKey;
    private String terminalId;
    private String retrievingInstitutionId;
    private String extendedTransactionType;
    private char respCode;
    private String respMessage;
    private String merchantId;
    private String merchantName;
    private String bin;
    private String issuer;
    private String acquirer;
    private String fromAccount;
    private String toAccount;
    private Timestamp transactionDate;
    private String transactionCategoryLabel;
    private String cardType;
    private float surcharge;
    private String bankDetails;
    private char transactionType;
    private Date settlementDate;

    // Getters and Setters
    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getRetrievalReferenceNr() {
        return retrievalReferenceNr;
    }

    public void setRetrievalReferenceNr(String retrievalReferenceNr) {
        this.retrievalReferenceNr = retrievalReferenceNr;
    }

    public char getMessageType() {
        return messageType;
    }

    public void setMessageType(char messageType) {
        this.messageType = messageType;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getRetrievingInstitutionId() {
        return retrievingInstitutionId;
    }

    public void setRetrievingInstitutionId(String retrievingInstitutionId) {
        this.retrievingInstitutionId = retrievingInstitutionId;
    }

    public String getExtendedTransactionType() {
        return extendedTransactionType;
    }

    public void setExtendedTransactionType(String extendedTransactionType) {
        this.extendedTransactionType = extendedTransactionType;
    }

    public char getRespCode() {
        return respCode;
    }

    public void setRespCode(char respCode) {
        this.respCode = respCode;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCategoryLabel() {
        return transactionCategoryLabel;
    }

    public void setTransactionCategoryLabel(String transactionCategoryLabel) {
        this.transactionCategoryLabel = transactionCategoryLabel;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public float getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(float surcharge) {
        this.surcharge = surcharge;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public char getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(char transactionType) {
        this.transactionType = transactionType;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "pan='" + pan + '\'' +
                ", stan='" + stan + '\'' +
                ", retrievalReferenceNr='" + retrievalReferenceNr + '\'' +
                ", messageType=" + messageType +
                ", amount=" + amount +
                ", uniqueKey='" + uniqueKey + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", retrievingInstitutionId='" + retrievingInstitutionId + '\'' +
                ", extendedTransactionType='" + extendedTransactionType + '\'' +
                ", respCode=" + respCode +
                ", respMessage='" + respMessage + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", bin='" + bin + '\'' +
                ", issuer='" + issuer + '\'' +
                ", acquirer='" + acquirer + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", transactionDate=" + transactionDate +
                ", transactionCategoryLabel='" + transactionCategoryLabel + '\'' +
                ", cardType='" + cardType + '\'' +
                ", surcharge=" + surcharge +
                ", bankDetails='" + bankDetails + '\'' +
                ", transactionType=" + transactionType +
                ", settlementDate=" + settlementDate +
                '}';
    }
}
