package com.paymentchain.businessdomain.controller;


import com.paymentchain.businessdomain.entities.Transaction;
import com.paymentchain.businessdomain.exceptions.BussinesRuleException;
import com.paymentchain.businessdomain.repository.TransactionRepository;
import com.paymentchain.businessdomain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/id")
    public List<Transaction> list(){
        return transactionRepository.findAll();
    }

    // Obtener transacciones de un cliente por IBAN
    @GetMapping("/customer/transactions")
    public ResponseEntity<List<Transaction>> get(@RequestParam String ibanAccount) {
        List<Transaction> transactions = transactionService.getTransactionsByAccount(ibanAccount);

        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction input)
            throws BussinesRuleException, UnknownHostException {

        Transaction created = transactionService.createTransaction(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable long id, @RequestBody Transaction transaction) {
        Transaction find = transactionRepository.findById(id).get();
        if (find != null){
            find.setAmount(transaction.getAmount());
            find.setChannel(transaction.getChannel());
            find.setDate(transaction.getDate());
            find.setDescription(transaction.getDescription());
            find.setFee(transaction.getFee());
            find.setAccountIban(transaction.getAccountIban());
            find.setReference(transaction.getReference());
            find.setStatus(transaction.getStatus());
        }

        Transaction save = transactionRepository.save(transaction);
        return ResponseEntity.ok(save);
    }

    // Obtener saldo actual de la cuenta
    @GetMapping("/customer/balance")
    public ResponseEntity<Double> getBalance(@RequestParam String ibanAccount) {
        double balance = transactionService.getBalance(ibanAccount);
        return ResponseEntity.ok(balance);
    }






}
