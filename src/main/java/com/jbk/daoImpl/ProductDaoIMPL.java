package com.jbk.daoImpl;

import java.util.Collections;
import java.util.List;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.jbk.dao.ProductDao;
import com.jbk.entity.Product;

@Repository
public class ProductDaoIMPL implements ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean saveProduct(Product product) {
		Session session = null;
		Transaction transaction = null;
		boolean isAdded = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Product prd = session.get(Product.class, product.getProductId());
			if (prd == null) {
				session.save(product);
				transaction.commit();
				isAdded = true;
			}
		} 
		catch (PersistenceException e) {
			isAdded = false;
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return isAdded;
	}

	@Override
	public List<Product> getAllProducts() {
		List<Product> list = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Product.class);
			// criteria.addOrder(Order.asc("ProductId"));
			list = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Product getProductById(String productId) {
		Session session = null;
		Product product = null;
		try {
			session = sessionFactory.openSession();
			product = session.get(Product.class, productId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public boolean deleteProductById(String productId) {
		Session session = null;
		Transaction transaction = null; // save, update,delete
		boolean isDeleted = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Product product = session.get(Product.class, productId);
			if (product != null) {
				session.delete(product);
				transaction.commit();
				isDeleted = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDeleted;
	}

	@Override
	public boolean updateProduct(Product product) {
		Session session = null;
		Transaction transaction = null; // save, update,delete
		boolean isUpdated = false;
		try {
			session = sessionFactory.openSession();

			transaction = session.beginTransaction();
			Product prd = session.get(Product.class, product.getProductId());
			if (prd != null) {
				session.evict(prd);
				session.update(product);
				transaction.commit();
				isUpdated = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUpdated;
	}

	@Override
	public  int[] uploadProductList(List<Product> list) {
		Session session = null;
		Transaction transaction = null;
		int uploadedcount = 0;
		int existCount=0;
		int[]arr= new int[2];
		try {
			
			for (Product product : list) {
				boolean isAdded= saveProduct(product);
				if(isAdded) {
					uploadedcount=uploadedcount+1;
				}else {
					existCount=existCount+1;
				}
			}
			arr[0]=uploadedcount;
			arr[1]=existCount;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();

			}
		}

		return arr;
	}

	@Override
	public List<Product> getMaxPriceProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMaxPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Product> sortProductById_ASC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> sortProductById_DESC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double countSumOfProductPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCountOfProducts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String uploadProducts(List<Product> list) {
		// TODO Auto-generated method stub
		return null;
	}

}