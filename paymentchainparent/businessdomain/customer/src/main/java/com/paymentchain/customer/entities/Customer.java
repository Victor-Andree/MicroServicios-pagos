/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.entities;

import jakarta.persistence.*;


import lombok.Data;

import java.util.List;

@Data
@Entity
public class Customer {
   
   @GeneratedValue(strategy = GenerationType.AUTO)  
   @Id
   private long id;
   private String code;
   private String names;
   private String surname;
   private String phone;
   private String address;
   private String iban;

   @OneToMany (fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<CustomerProduct> products;

   @Transient
   private List<?> transactions;



}
