package com.paymentchain.businessdomain.service;

import com.paymentchain.businessdomain.entities.Transaction;
import com.paymentchain.businessdomain.exceptions.BussinesRuleException;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction input) throws BussinesRuleException;
    Transaction updateTransaction(long id, Transaction transaction) throws BussinesRuleException;
    List<Transaction> getTransactionsByAccount(String ibanAccount);
    double getBalance(String ibanAccount);
}
