//package com.stitch.repository;
//
//import com.stitch.model.entity.BillTransaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface BillerTransactionRepository extends JpaRepository<BillTransaction, Long> {
//    Optional<BillTransaction> findByOrderId(String orderId);
//
//    BillTransaction findByRequestId(String requestId);
//}
