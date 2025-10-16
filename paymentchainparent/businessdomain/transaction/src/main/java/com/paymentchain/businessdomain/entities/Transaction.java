package com.paymentchain.businessdomain.entities;


import com.paymentchain.businessdomain.Enum.Channel;
import com.paymentchain.businessdomain.Enum.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String reference;

    private String accountIban;

    private LocalDateTime date;

    private Double amount;

    private double fee;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @PrePersist
    @PreUpdate
    public void applyBussinessRules() {
        if (amount == 0) {
            throw new IllegalArgumentException("El monto debe ser diferente de cero");
        }

        if (date == null) {
            date = LocalDateTime.now();
        }

        // Estado de la transacción según la fecha
        if (date.isAfter(LocalDateTime.now())) {
            status = Status.PENDIENTE;
        } else {
            status = Status.LIQUIDADA;
        }
    }






}
