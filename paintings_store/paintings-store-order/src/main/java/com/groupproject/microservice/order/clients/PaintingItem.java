package com.groupproject.microservice.order.clients;

import lombok.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

@EqualsAndHashCode
@Data
public class PaintingItem extends RepresentationModel {

	private String name;

	private double price;

	@JsonProperty("id")
	private long itemId;

	public PaintingItem() {
		super();
	}

	public PaintingItem(long id, String name, double price) {
		super();
		this.itemId = id;
		this.name = name;
		this.price = price;
	}

}
