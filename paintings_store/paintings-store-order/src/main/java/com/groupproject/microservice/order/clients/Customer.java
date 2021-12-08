package com.groupproject.microservice.order.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class Customer extends RepresentationModel {

    private String surname;

    private String firstname;

    private String email;

    private String address;

    private String city;

    @JsonProperty("id")
    private long customerId;

    public Customer(long id, String firstname, String surname, String email,
                    String address, String city) {
        super();
        this.customerId = id;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.city = city;
    }

}
