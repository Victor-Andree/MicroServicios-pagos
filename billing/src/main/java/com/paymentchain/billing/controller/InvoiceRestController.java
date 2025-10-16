package com.paymentchain.billing.controller;

import com.paymentchain.billing.common.InvoiceRequestMapper;
import com.paymentchain.billing.common.InvoiceResponseMapper;
import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.billing.respository.InvoiceRepository;

import java.util.List;
import java.util.Optional;

@Tag(name = "Billing API", description = "This API serve all funcionality for managment invoices")
@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    @Autowired
    InvoiceRepository billingRepository;

    @Autowired
    InvoiceResponseMapper invoiceResponseMapper;

    @Autowired
    InvoiceRequestMapper invoiceRequestMapper;

    @Operation(description = "Return all invoices bundled into Response" , summary = "Return 204 if no data found")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exito")})
    @ApiResponse(responseCode = "500", description = "Internal Error")
    @GetMapping
    public List<InvoiceResponse> list(){
       List<Invoice> findAll =  billingRepository.findAll();
       return invoiceResponseMapper.InvoiceListToInvoiceResposeList(findAll);
    }

    @GetMapping("/{id}")
    public Invoice get(@PathVariable String id) {
        Optional<Invoice> findById = billingRepository.findById(Long.valueOf(id));
        return findById.get();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody InvoiceRequest input) {
        Invoice save = null;
        Optional<Invoice> findById = billingRepository.findById(Long.valueOf(id));
        Invoice get = findById.get();
        if(get != null){
            save = billingRepository.save(get);
        }
        return ResponseEntity.ok(save);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody InvoiceRequest input) {
        Invoice InvoiceRequestToInvoice = invoiceRequestMapper.InvoiceRequestToInvoice(input);
        Invoice save = billingRepository.save(InvoiceRequestToInvoice);
        InvoiceResponse InvoiceToInvoiceResponse = invoiceResponseMapper.InvoiceToInvoiceRespose(save);
        return ResponseEntity.ok(InvoiceToInvoiceResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Invoice save = null;
        Optional<Invoice> findById = billingRepository.findById(Long.valueOf(id));
        Invoice get = findById.get();
        if(get != null){
            billingRepository.delete(get);
        }
        return ResponseEntity.ok().build();
    }
    
}
