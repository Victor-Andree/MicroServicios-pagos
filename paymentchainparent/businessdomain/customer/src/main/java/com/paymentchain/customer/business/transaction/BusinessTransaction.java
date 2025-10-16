package com.paymentchain.customer.business.transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exceptions.BusineesRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BusinessTransaction {

    @Autowired
    private  WebClient.Builder builder;

    @Autowired
    CustomerRepository customerRepository;


    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5800)
            .option(ChannelOption.SO_KEEPALIVE,true)
            .option(EpollChannelOption.TCP_KEEPIDLE,300)
            .option(EpollChannelOption.TCP_KEEPINTVL,60)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));

            });

    public Customer post(Customer input) throws BusineesRuleException,UnknownHostException {
        if (input.getProducts() != null){
            for (Iterator<CustomerProduct> it = input.getProducts().iterator(); it.hasNext(); ) {
                CustomerProduct dto = it.next();
                String productName = getProductName(dto.getProductId());
                if (productName.isBlank()) {
                    BusineesRuleException busineesRuleException = new BusineesRuleException("1025", "Error de validacion, producto con id" + dto.getProductId()+"no existe", HttpStatus.PRECONDITION_FAILED);
                    throw busineesRuleException;
                }else{
                    dto.setCustomer(input);
                }
            }
        }

        Customer save = customerRepository.save(input);
        return  save;
    }

    public Customer getByCode(@RequestParam(name = "code") String code) {
        Customer customer = customerRepository.findByCode(code);
        if (customer != null) {
            List<CustomerProduct> products = customer.getProducts();

            //for each product find it name
            products.forEach(x -> {
                try {
                    String productName = getProductName(x.getProductId());
                    x.setProductName(productName);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(BusineesRuleException.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
          /*  //find all transactions that belong this account number
            List<?> transactions = getTransactions(customer.getIban());
            customer.setTransactions(transactions);*/

        }
        return customer;
    }
    


    private String getProductName(long id) throws UnknownHostException {
        String name="";
        try{
            WebClient build = builder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-PRODUCT/product")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://BUSINESSDOMAIN-PRODUCT/product"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            name = block.get("name").asText();
        }catch(WebClientResponseException ex){
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return "";
            }else{
                throw new UnknownHostException(ex.getMessage());
            }
        }
        return name;
    }

    private List<?> getTransactions(String iban){
        WebClient build = builder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8082/transaction")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        List<?> transaction = build.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                        .path("/customer/transactions")
                        .queryParam("ibanAccount", iban)
                        .build())
                .retrieve().bodyToFlux(Object.class).collectList().block();
        return transaction;
    }

}
