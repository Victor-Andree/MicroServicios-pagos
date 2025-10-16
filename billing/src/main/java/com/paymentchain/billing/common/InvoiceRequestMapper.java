package com.paymentchain.billing.common;

import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.entities.Invoice;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceRequestMapper {

    @Mappings({
            @Mapping(source = "customer", target = "customerId")
    })
    Invoice InvoiceRequestToInvoice(InvoiceRequest source);

    List<Invoice> InvoiceRequestListToInvoiceList(List<InvoiceRequest> source);

    @InheritInverseConfiguration
    InvoiceRequest InvoiceToInvoiceRequest(Invoice source);

    @InheritInverseConfiguration
    List<InvoiceRequest> InvoiceListToInvoiceRequestList(List<Invoice> source);

}
