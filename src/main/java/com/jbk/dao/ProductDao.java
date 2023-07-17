package com.jbk.dao;

import java.util.List;
import com.jbk.entity.Product;

public interface ProductDao {
	public boolean saveProduct(Product product);
	public Product getProductById(String productId);
	public List<Product> getAllProducts();
	public boolean deleteProductById(String productId);
	public int[] uploadProductList(List<Product> list);
	public List<Product> getMaxPriceProduct();
	public double getMaxPrice();
	public List<Product> sortProductById_ASC();
	public List<Product> sortProductById_DESC();
	public double countSumOfProductPrice();
	public int getTotalCountOfProducts();
	public String uploadProducts(List<Product>list);
	public boolean updateProduct(Product product);
	

}
