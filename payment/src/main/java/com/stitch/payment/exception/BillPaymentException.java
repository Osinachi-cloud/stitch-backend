//package com.stitch.payment.exception;
//
//
//import com.stitch.commons.exception.StitchException;
//import com.stitch.exception.BillingException;
//
//public class BillPaymentException extends StitchException {
//
//    private String requestId;
//
//    public BillPaymentException() {
//    }
//
//    public BillPaymentException(String message) {
//        super(message);
//    }
//
//    public BillPaymentException(String message, Throwable cause) {
//        super(message, cause);
//        if (cause instanceof BillingException) {
//            BillingException exception = (BillingException)cause;
//            this.requestId = exception.getRequestId();
//        }
//    }
//
//    public BillPaymentException(Throwable cause) {
//        super(cause);
//    }
//
//    public BillPaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//        super(message, cause, enableSuppression, writableStackTrace);
//    }
//
//    public String getRequestId() {
//        return requestId;
//    }
//}
