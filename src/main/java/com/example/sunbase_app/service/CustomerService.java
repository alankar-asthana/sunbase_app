package com.example.sunbase_app.service;


import com.example.sunbase_app.models.CustomerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    //private String customerApiUrl;
    private final RestTemplate restTemplate;
    @Autowired
    public CustomerService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }
    public boolean createCustomer(String bearerToken, CustomerRequest customerRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create",
                HttpMethod.POST,
                entity,
                Void.class
        );
        return response.getStatusCode() == HttpStatus.CREATED;
    }
    public List<CustomerRequest> getCustomerList(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerRequest[]> response = restTemplate.exchange(
                "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list",
                HttpMethod.GET,
                entity,
                CustomerRequest[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return Arrays.asList(response.getBody());
        } else {
            // Handle errors or return an empty list
            return Collections.emptyList();
        }
    }
    public boolean deleteCustomer(String bearerToken, String uuid){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+bearerToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete&uuid=" + uuid,
                HttpMethod.POST,
                entity,
                Void.class
        );
        return response.getStatusCode() == HttpStatus.OK;
    }
    public boolean updateCustomer(String bearerToken, String uuid, CustomerRequest customerRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CustomerRequest> entity = new HttpEntity<>(customerRequest, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update&uuid=" + uuid,
                HttpMethod.POST,
                entity,
                Void.class
        );

        return response.getStatusCode() == HttpStatus.OK;
    }
}

//@Service
//public class CustomerService {
//
//    private final WebClient webClient;
//
//    @Value("${customer.api.url}")
//    private String customerApiUrl;
//
//    public CustomerService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl(customerApiUrl).build();
//    }
//
//    public Mono<Boolean> createCustomer(String bearerToken, CustomerRequest customerRequest) {
//        return webClient
//                .post()
//                .uri("/assignment.jsp?cmd=create")
//                .header("Authorization", "Bearer " + bearerToken)
//                .body(Mono.just(customerRequest), CustomerRequest.class)
//                .exchangeToMono(response -> {
//                    if (response.statusCode().isError()) {
//                        return Mono.just(true);
//                    } else {
//                        return Mono.just(false);
//                    }
//                });
//    }
//
//    public Mono<List<CustomerRequest>> getCustomerList(String bearerToken) {
//        return webClient
//                .get()
//                .uri("/assignment.jsp?cmd=get_customer_list")
//                .header("Authorization", "Bearer " + bearerToken)
//                .retrieve()
//                .bodyToFlux(CustomerRequest.class)
//                .collectList();
//    }
//
//    public Mono<Boolean> deleteCustomer(String bearerToken, String uuid) {
//        return webClient
//                .post()
//                .uri("/assignment.jsp?cmd=delete&uuid=" + uuid)
//                .header("Authorization", "Bearer " + bearerToken)
//                .exchangeToMono(response -> Mono.just(response.statusCode() == HttpStatus.OK));
//    }
//
//    public Mono<Boolean> updateCustomer(String bearerToken, String uuid, CustomerRequest customerRequest) {
//        return webClient
//                .post()
//                .uri("/assignment.jsp?cmd=update&uuid=" + uuid)
//                .header("Authorization", "Bearer " + bearerToken)
//                .body(Mono.just(customerRequest), CustomerRequest.class)
//                .exchangeToMono(response -> Mono.just(response.statusCode() == HttpStatus.OK));
//    }
//}

