package com.paymentchain.businessdomain.service;

import com.paymentchain.businessdomain.entities.Transaction;
import com.paymentchain.businessdomain.exceptions.BussinesRuleException;
import com.paymentchain.businessdomain.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public Transaction createTransaction(Transaction input) throws BussinesRuleException {
        if (input.getAccountIban() == null || input.getAccountIban().isEmpty()) {
            throw new BussinesRuleException("TRANSACTION-001",
                    "El IBAN de la cuenta es requerido", HttpStatus.BAD_REQUEST);
        }

        if (input.getAmount() == null) {
            throw new BussinesRuleException("TRANSACTION-002",
                    "El monto de la transacción es requerido", HttpStatus.BAD_REQUEST);
        }

        double saldoActual = getBalance(input.getAccountIban());

        if (input.getAmount() < 0) {
            double fee = Math.abs(input.getAmount()) * 0.0098;
            double montoFinal = input.getAmount() - fee;

            if ((saldoActual + montoFinal) <= 0) {
                throw new BussinesRuleException("TRANSACTION-003",
                        "Fondos insuficientes para realizar el retiro", HttpStatus.BAD_REQUEST);
            }

            input.setFee(fee);
            input.setAmount(montoFinal);
        } else {
            input.setFee(0.0);
        }

        return transactionRepository.save(input);
    }

    @Override
    public Transaction updateTransaction(long id, Transaction transaction) throws BussinesRuleException {
        return transactionRepository.findById(id).map(existing -> {
            existing.setAmount(transaction.getAmount());
            existing.setChannel(transaction.getChannel());
            existing.setDate(transaction.getDate());
            existing.setDescription(transaction.getDescription());
            existing.setFee(transaction.getFee());
            existing.setAccountIban(transaction.getAccountIban());
            existing.setReference(transaction.getReference());
            existing.setStatus(transaction.getStatus());

            return transactionRepository.save(existing);
        }).orElseThrow(() -> new BussinesRuleException("TRANSACTION-004",
                "Transacción no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Transaction> getTransactionsByAccount(String ibanAccount) {
        return transactionRepository.findByAccountIban(ibanAccount);
    }

    @Override
    public double getBalance(String ibanAccount) {
        return transactionRepository.findByAccountIban(ibanAccount)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


}
