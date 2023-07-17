package com.jbk.validation;


import java.util.HashMap;
import java.util.Map;

import com.jbk.entity.Product;

public class ValidateObject {
	public static Map<String, String> map=null;
	public static Map<String, String> validateProduct( Product product) {
		
		map=new HashMap<>();
		
		if (product.getProductName() == null || product.getProductName().equals("")) {
			
			map.put("productName", "ProductName is required");
		}
		if (product.getProductQTY() <= 0) {
			
			map.put("productQTY", "ProductQTY should be greater than 0");
		}
		if (product.getProductPrice() <= 0) {
			
			map.put("productPrice", "ProductPrice should be greater than 0");
		}
		if (product.getProductType() == null || product.getProductType().equals("")) {
			
			map.put("productType", "ProductType is required");
		} 

			return map;

		}
	

}
