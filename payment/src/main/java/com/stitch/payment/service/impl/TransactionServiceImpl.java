package com.stitch.payment.service.impl;

import com.stitch.commons.model.dto.PaginatedResponse;
import com.stitch.commons.util.RandomIdGenerator;
import com.stitch.payment.exception.PaymentException;
import com.stitch.payment.model.dto.*;
import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.enums.TransactionType;
import com.stitch.payment.repository.TransactionRepository;
import com.stitch.payment.repository.projection.TransactionView;
import com.stitch.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;

    public PaginatedResponse<List<TransactionResponse>> fetchAllCustomerTransactions(String customerId,
                                                                                     TransactionHistoryRequest request) {
        Page<Transaction> transactions = transactionRepository
                .findAllByCustomerId(customerId, PageRequest.of(request.getPage(), request.getSize()));

        PaginatedResponse<List<TransactionResponse>> response = new PaginatedResponse<>();
        response.setPage(request.getPage());
        response.setSize(request.getSize());
        response.setTotal(transactions.getTotalPages());
        response.setData(transactions.stream().map(this::mapToTransactionResponse)
                .collect(Collectors.toList()));
        return response;
    }


    @Override
    public PaginatedResponse<List<TransactionDto>> fetchCustomerTransactions(String customerId, TransactionHistoryRequest request) {
        Page<TransactionView> transactionView = transactionRepository.findForCustomerId(customerId, PageRequest.of(request.getPage(), request.getSize()));

        List<TransactionDto> transactionDtos = transactionView.getContent().stream().map(this::convertTransactionViewToDto).collect(Collectors.toList());
        PaginatedResponse<List<TransactionDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(transactionView.getNumber());
        paginatedResponse.setSize(transactionView.getSize());
        paginatedResponse.setTotal(transactionView.getTotalPages());
        paginatedResponse.setData(transactionDtos);
        return paginatedResponse;
    }

    private TransactionDto convertTransactionViewToDto(TransactionView transaction) {
        TransactionDto.TransactionDtoBuilder transactionDtoBuilder = TransactionDto.builder()
                .id(transaction.getId().intValue())
                .transactionType(transaction.getTransactionType().getDescription())
                .transactionId(transaction.getTransactionId())
                .timestamp(String.valueOf(transaction.getDateCreated().toEpochMilli()))
                .amount(transaction.getAmount().toPlainString())
                .currency(transaction.getCurrency())
                .sourceAmount(transaction.getSrcAmount() != null ? transaction.getSrcAmount().toPlainString() : (transaction.getCurrency() != null && transaction.getCurrency().equals(transaction.getSrcCurrency()) ? transaction.getAmount().toPlainString() : ""))
                .sourceCurrency(transaction.getSrcCurrency() != null ? transaction.getSrcCurrency() : "")
                .status(transaction.getStatus().getDescription())
                .description(transaction.getDescription())
                .orderId(transaction.getOrderId())
                .fullName(String.format("%s %s", transaction.getFirstName(), transaction.getLastName()))
                .transactionPaymentOption(new TransactionDto.TransactionPaymentOption(transaction.getTransactionType().equals(TransactionType.PAY_BILL) ? "Debit" : "Credit", transaction.getPaymentMode(), ""));

        if (transaction.getTransactionType().equals(TransactionType.FUND_WALLET)) {
            transactionDtoBuilder.description("Credit transaction to wallet");
        } else if (transaction.getTransactionType().equals(TransactionType.PAY_BILL)) {
            transactionDtoBuilder.description(String.format("Payment for %s", transaction.getProductCategory().toLowerCase()));
        } else if (transaction.getTransactionType().equals(TransactionType.SCHEDULED_BILL)) {
            transactionDtoBuilder.description(String.format("Scheduled Payment for %s", transaction.getProductCategory().toLowerCase()));
        }

        TransactionDto.UtilityReceipt.UtilityReceiptBuilder utilityReceiptBuilder = TransactionDto.UtilityReceipt.builder()
                .paidOn(new TransactionDto.PaidOn("Paid On", String.valueOf(transaction.getDateCreated().toEpochMilli())))
                .transactionType(new TransactionDto.TransactionType("Transaction Type", transaction.getProductCategory()))
                .billProvider(new TransactionDto.BillProvider("Bill Provider", transaction.getProductProvider() != null ? transaction.getProductProvider().toUpperCase() : ""))
                .amount(new TransactionDto.Amount("Amount", transaction.getAmount().toPlainString()))
                .transactionId(new TransactionDto.TransactionId("Transaction ID", transaction.getTransactionId()));
        if ("AIRTIME".equals(transaction.getProductCategory()) || "DATA".equals(transaction.getProductCategory())) {
            utilityReceiptBuilder.customerUtilityId(new TransactionDto.CustomerUtilityId("Phone Number", transaction.getBillersCode()));
        } else if ("ELECTRICITY".equals(transaction.getProductCategory())) {
            utilityReceiptBuilder.customerUtilityId(new TransactionDto.CustomerUtilityId("Meter Number", transaction.getBillersCode()))
                    .customerUtilityName(new TransactionDto.CustomerUtilityName("Meter Name", transaction.getCustomerName()))
                    .address(new TransactionDto.Address("Address", transaction.getCustomerAddress()))
                    .meterToken(new TransactionDto.MeterToken("Meter Token", transaction.getToken()))
                    .meterUnits(new TransactionDto.MeterUnits("Meter Units", transaction.getUnits()));
        } else if ("CABLE-TV".equals(transaction.getProductCategory())) {
            utilityReceiptBuilder.customerUtilityId(new TransactionDto.CustomerUtilityId("Smartcard Number", transaction.getBillersCode()));
        }
        TransactionDto.UtilityReceipt utilityReceipt = utilityReceiptBuilder.build();
        transactionDtoBuilder.utilityReceipt(utilityReceipt);
        return transactionDtoBuilder.build();
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setTransactionId(transaction.getTransactionId());
        response.setOrderId(transaction.getOrderId());
        response.setCustomerId(transaction.getCustomerId());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency().name());
        response.setPaymentMode(transaction.getPaymentMode().name());
        response.setSrcCurrency(transaction.getSrcCurrency().name());
        response.setDestCurrency(transaction.getDestCurrency().name());
        response.setFee(transaction.getFee());
        response.setProductCategory(transaction.getProductCategory());
        response.setNarration(transaction.getNarration());
        response.setDescription(transaction.getDescription());
        response.setStatus(transaction.getStatus().name());
        response.setDateCreated(transaction.getDateCreated().toEpochMilli());
        response.setLastUpdated(transaction.getLastUpdated().toEpochMilli());
        return response;
    }

    @Override
    public Transaction createTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = Transaction.builder()
                .transactionId(generateTransactionId())
                .amount(transactionRequest.getAmount())
                .currency(transactionRequest.getCurrency())
                .orderId(transactionRequest.getOrderId())
                .transactionType(transactionRequest.getTransactionType())
                .paymentMode(transactionRequest.getPaymentMode())
                .customerId(transactionRequest.getCustomerId())
                .walletId(transactionRequest.getWalletId())
                .walletTransactionId(transactionRequest.getWalletTransactionId())
                .paymentCardId(transactionRequest.getCardId())
                .productCategory(transactionRequest.getProductCategory())
                .srcCurrency(transactionRequest.getSrcCurrency())
                .srcAmount(transactionRequest.getSrcAmount())
                .destCurrency(transactionRequest.getDestCurrency())
                .destAmount(transactionRequest.getDestAmount())
                .exchangeRate(transactionRequest.getExchangeRate())
                .narration(transactionRequest.getNarration())
                .description(transactionRequest.getDescription())
                .fee(transactionRequest.getFee())
                .status(transactionRequest.getStatus() == null ? TransactionStatus.P : transactionRequest.getStatus())
                .build();
        return transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public Transaction updateTransactionStatus(String transactionId, TransactionStatus status) {

        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentException("Payment transaction Id not found: " + transactionId));

        transaction.setStatus(status);
        return transactionRepository.saveAndFlush(transaction);

    }

    @Override
    public Transaction updateTransactionStatus(Long id, TransactionStatus status) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new PaymentException("Payment transaction Id not found: " + id));

        transaction.setStatus(status);
        return transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.saveAndFlush(transaction);
    }

    public String generateTransactionId() {
        return RandomIdGenerator.generateNumberIdWithDateTime(19);
    }


    @Override
    public TransactionDetailsDto fetchPaymentDetails(String transactionId, String customerId) {
        TransactionView transactionView = transactionRepository.findOrderDetails(transactionId, customerId)
            .orElseThrow(() -> new PaymentException("Payment transaction not found"));

        TransactionDetailsDto detailsDto = new TransactionDetailsDto();
        detailsDto.setCustomerId(customerId);
        detailsDto.setTransactionId(transactionId);
        detailsDto.setCreatedOn(transactionView.getDateCreated());
        detailsDto.setPhoneNumber(transactionView.getPhoneNumber());
        detailsDto.setTransactionType(transactionView.getTransactionType());
        detailsDto.setAmount(transactionView.getAmount() != null ? transactionView.getAmount().toString() : null);
        detailsDto.setCurrency(transactionView.getCurrency() != null ? transactionView.getCurrency() : null);

        detailsDto.setSourceAmount(transactionView.getSrcAmount() != null ? transactionView.getSrcAmount().toPlainString() : null);
        detailsDto.setSourceCurrency(transactionView.getSrcCurrency() != null ? transactionView.getSrcCurrency() : null);
        detailsDto.setProductCategoryName(transactionView.getProductCategory());
        detailsDto.setProductProvider(transactionView.getProductProvider());
        detailsDto.setProductPackage(transactionView.getProductPackage());
        detailsDto.setBillersCode(transactionView.getBillersCode());
        detailsDto.setVariationCode(transactionView.getVariationCode());
        detailsDto.setCustomerName(transactionView.getCustomerName());
        detailsDto.setCustomerAddress(transactionView.getCustomerAddress());
        detailsDto.setToken(transactionView.getToken());
        detailsDto.setUnits(transactionView.getUnits());

        return detailsDto;
    }

    @Override
    public Transaction retrieveTransaction(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentException("Payment transaction not found for transactionId: "+transactionId));
    }

    @Override
    public Transaction retrieveTransactionByOrderId(String orderId) {
        return transactionRepository.findByOrderId(orderId)
            .orElseThrow(() -> new PaymentException("Payment transaction not found for orderId: "+orderId));
    }

    @Override
    public PaginatedResponse<List<TransactionDto>> fetchAllTransactions(TransactionHistoryRequest request) {
        Page<TransactionView> transactionView = transactionRepository.findAllTransactions(PageRequest.of(request.getPage(), request.getSize()));
        List<TransactionDto> transactionDtos = transactionView.getContent().stream().map(this::convertTransactionViewToDto).collect(Collectors.toList());
        PaginatedResponse<List<TransactionDto>> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setPage(transactionView.getNumber());
        paginatedResponse.setSize(transactionView.getSize());
        paginatedResponse.setTotal(transactionView.getTotalPages());
        paginatedResponse.setData(transactionDtos);
        return paginatedResponse;
    }
}
