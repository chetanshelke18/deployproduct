package com.jbk.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name= "deployproduct")
public class Product {

	@Id
	@Column(unique = true,nullable = false)
	private String productId;
	
	@NotNull(message = "Product Name Is Required")
	@Column(unique = true,nullable = false)
	private String productName;
	
	
	@Column(nullable = false)
	@Min(1)
	private int productQTY;
	
	@Column(nullable = false)
	@Min(1)
	private double productPrice;
	
	@NotNull(message = "ProductType is required")
	@Column(nullable = false)
	private String productType;

	public Product() {
		
	}

	public Product(String productId,  String productName,
			 int productQTY,  double productPrice,
			 String productType) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productQTY = productQTY;
		this.productPrice = productPrice;
		this.productType = productType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductQTY() {
		return productQTY;
	}

	public void setProductQTY(int productQTY) {
		this.productQTY = productQTY;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productQTY=" + productQTY
				+ ", productPrice=" + productPrice + ", productType=" + productType + "]";
	}

	
	}

	