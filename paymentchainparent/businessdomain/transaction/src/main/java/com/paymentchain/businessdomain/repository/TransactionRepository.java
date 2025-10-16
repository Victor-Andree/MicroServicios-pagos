package com.paymentchain.businessdomain.repository;

import com.paymentchain.businessdomain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

List<Transaction> findByAccountIban(String accountIban);

}
