package com.groupproject.microservice.order.clients;

import lombok.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

@EqualsAndHashCode
@Data
public class Item extends RepresentationModel {

	private String name;

	private double price;

	@JsonProperty("id")
	private long itemId;

	public Item() {
		super();
	}

	public Item(long id, String name, double price) {
		super();
		this.itemId = id;
		this.name = name;
		this.price = price;
	}

}
