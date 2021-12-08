package com.groupproject.microservice.order.logic;

import com.groupproject.microservice.order.clients.CatalogClient;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERTABLE")
class PaintingOrder {

	@Id
	@GeneratedValue
	private long id;

	private long customerId;

	@OneToMany(cascade = CascadeType.ALL)
	private List<PaintingOrderLine> paintingOrderLine;

	public PaintingOrder() {
		super();
		paintingOrderLine = new ArrayList<PaintingOrderLine>();
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public List<PaintingOrderLine> getOrderLine() {
		return paintingOrderLine;
	}

	public PaintingOrder(long customerId) {
		super();
		this.customerId = customerId;
		this.paintingOrderLine = new ArrayList<PaintingOrderLine>();
	}

	public void setOrderLine(List<PaintingOrderLine> paintingOrderLine) {
		this.paintingOrderLine = paintingOrderLine;
	}

	public void addLine(int count, long itemId) {
		this.paintingOrderLine.add(new PaintingOrderLine(count, itemId));
	}

	public int getNumberOfLines() {
		return paintingOrderLine.size();
	}

	public double totalPrice(CatalogClient itemClient) {
		return paintingOrderLine.stream()
				.map((ol) -> ol.getCount() * itemClient.price(ol.getItemId()))
				.reduce(0.0, (d1, d2) -> d1 + d2);
	}

	public void setCustomer(long customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);

	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
