package com.dibya.shoppingcart.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All details about the item.")
@Entity
public class Item {

	@Id
	@GeneratedValue
	private int itemId;
	private String itemName;
	private double price;
	private int quantity;
	
	
	protected Item() {
		
	}
	
	public Item(int i, String string, double k, int j) {
		super();
		this.itemId = i;
		this.itemName = string;
		this.price = k;
		this.quantity = j;
	}
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", itemName=" + itemName + ", quantity=" + quantity + ", price=" + price
				+ "]";
	}
	
	
}
