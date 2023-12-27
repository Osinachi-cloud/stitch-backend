package com.stitch.payment.repository;

import com.stitch.currency.model.enums.Currency;
import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.enums.TransactionType;
import com.stitch.payment.repository.projection.TransactionView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    Page<Transaction> findAllByCustomerId(String customerId, Pageable pageable);

    @Query(value = "select t.id as id, t.date_created as dateCreated, t.transaction_id as transactionId,  t.payment_mode as paymentMode, t.amount as amount, " +
            "t.currency as currency, t.src_amount as srcAmount, t.src_currency as srcCurrency, t.description as description, t.narration as narration,  t.status as status, t.transaction_type as transactionType, " +
            "t.product_category as productCategory,  o.product_package as productPackage, o.product_provider as productProvider, b.phone_number as phoneNumber, " +
            "b.variation_code as variationCode, b.billers_code as billersCode, b.customer_name as customerName, b.customer_address as customerAddress, b.token as token, b.units as units, t.order_id as orderId from transaction t left join product_order o on t.order_id = o.order_id \n" +
            "left join bill_transaction b on t.order_id = b.order_id where t.customer_id = ?1 order by t.date_created desc", nativeQuery = true)
    Page<TransactionView> findForCustomerId(String customerId, Pageable pageable);

    @Query(value = "select t.id as id, t.date_created as dateCreated, t.transaction_id as transactionId,  t.payment_mode as paymentMode, t.amount as amount, " +
        "t.currency as currency, t.src_amount as srcAmount, t.src_currency as srcCurrency, t.description as description, t.narration as narration,  t.status as status, t.transaction_type as transactionType,t.order_id as orderId, " +
        "t.product_category as productCategory,  o.product_package as productPackage, o.product_provider as productProvider, b.phone_number as phoneNumber, " +
        "b.variation_code as variationCode, b.billers_code as billersCode, b.customer_name as customerName, b.customer_address as customerAddress, b.token as token, b.units as units, c.first_name as firstName, c.last_name as lastName" +
        " from transaction t left join product_order o on t.order_id = o.order_id \n" +
        "left join bill_transaction b on t.order_id = b.order_id left join customer c on t.customer_id = c.customer_id  order by t.date_created desc", nativeQuery = true)
    Page<TransactionView> findAllTransactions(Pageable pageable);

    Optional<Transaction> findByTransactionId(String transactionId);

    Optional<Transaction> findByOrderId(String orderId);

    @Query(value = "select t.id as id, t.date_created as dateCreated, t.transaction_id as transactionId,  t.payment_mode as paymentMode, t.amount as amount, " +
        "t.currency as currency, t.src_amount as srcAmount, t.src_currency as srcCurrency, t.description as description, t.narration as narration,  t.status as status, t.transaction_type as transactionType, " +
        "t.product_category as productCategory,  o.product_package as productPackage, o.product_provider as productProvider, b.phone_number as phoneNumber, " +
        "b.variation_code as variationCode, b.billers_code as billersCode, b.customer_name as customerName, b.customer_address as customerAddress, b.token as token, b.units as units from transaction t left join product_order o on t.order_id = o.order_id \n" +
        "left join bill_transaction b on t.order_id = b.order_id where t.transaction_id = ?1 and t.customer_id = ?2", nativeQuery = true)
    Optional<TransactionView> findOrderDetails(String transactionId, String customerId);


    List<Transaction> findAllByCustomerIdAndTransactionTypeAndStatusInAndSrcCurrencyAndDateCreatedBetween(String customerId, TransactionType transactionType,
                                                                                                          List<TransactionStatus> statuses, Currency currency, Instant start, Instant end);

    List<Transaction> findAllByCustomerIdAndTransactionTypeAndStatusInAndSrcCurrencyAndProductCategoryNotInIgnoreCaseAndDateCreatedBetween(String customerId, TransactionType transactionType,
                                                                                                          List<TransactionStatus> statuses, Currency currency, List<String> productCategory, Instant start, Instant end);
}
