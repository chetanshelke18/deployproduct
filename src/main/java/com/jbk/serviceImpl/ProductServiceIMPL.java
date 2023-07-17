package com.jbk.serviceImpl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.coyote.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jbk.dao.ProductDao;
import com.jbk.entity.Product;
import com.jbk.service.ProductService;
import com.jbk.validation.ValidateObject;

@Service
public class ProductServiceIMPL implements ProductService  {

	@Autowired
	private ProductDao dao;

	String excludedRows ="";
	int totalRecordCount = 0;
	Map<String, Object> map = new HashMap<String, Object>();
	Map<String, String> validatedError = new HashMap<String, String>();
	Map<Integer, Map<String, String>> errorMap = new HashMap <Integer, Map<String, String>>();

	@Override
	public boolean saveProduct(Product product) {
		String productId = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
		product.setProductId(productId);
		boolean isAdded = dao.saveProduct(product);
		return isAdded;
	}

	@Override
	public Product getProductById(String productId) {

		return dao.getProductById(productId);
	}

	@Override
	public List<Product> getAllProducts() {

		return dao.getAllProducts();

	}

	@Override
	public boolean deleteProductById(String productId) {

		return dao.deleteProductById(productId);
	}

	@Override
	public boolean updateProduct(Product product) {

		return dao.updateProduct(product);
	}

	@Override
	public  List<Product> getMaxPriceProduct() {

		return dao.getMaxPriceProduct();
	}

	@Override
	public List<Product> sortProductById_ASC() {

		return dao.sortProductById_ASC();
	}

	@Override
	public List<Product> sortProductById_DESC() {

		return dao.sortProductById_DESC();
	}

	@Override
	public double getMaxPrice() {

		return dao.getMaxPrice();
	}

	@Override
	public double countSumOfProductPrice() {

		return dao.countSumOfProductPrice();
	}

	@Override
	public int getTotalCountOfProducts() {

		return dao.getTotalCountOfProducts();
	}

	public  List<Product> readExcel(String filepath) {
		Workbook workbook = null;
		FileInputStream fis= null;
		List<Product>list =new ArrayList<Product>();
		try {
			 fis = new FileInputStream(new File(filepath));
			workbook  = new XSSFWorkbook(fis);
			Sheet sheet=workbook.getSheetAt(1);
			totalRecordCount = sheet.getLastRowNum();
			Iterator<Row> rows = sheet.rowIterator();
			int rowCount = 0;;

			while (rows.hasNext()) {

				Row row = rows.next();
				if (rowCount == 0) {
					rowCount++;
					continue;
				}
				Product product = new Product();
				Thread.sleep(1);
				String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
				product.setProductId(id);
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					int column = cell.getColumnIndex();

					switch (column) {
					case 0: {
						product.setProductName(cell.getStringCellValue());
						break;
					}
					case 1: {
						product.setProductQTY((int) cell.getNumericCellValue());
						break;
					}
					case 2: {
						product.setProductPrice((int) cell.getNumericCellValue());
						break;
					}
					case 3: {
						product.setProductType(cell.getStringCellValue());
						break;
					}
					}
				}
				validatedError = ValidateObject.validateProduct(product);
				if (validatedError == null || validatedError.isEmpty()) {
					list.add(product);
				} else {
					int rowNum = row.getRowNum() + 1;
					// excludedRows = excludedRows + rowNum + ",";
					errorMap.put(rowNum, validatedError);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public String upploadSheet(MultipartFile myFile) {
		String path="src/main/resources";
		File file = new File(path);
		String msg=null;
		String absolutePath =file.getAbsolutePath();
		System.out.println(absolutePath);
		try {
			byte[] data=myFile.getBytes();
			FileOutputStream fos=new FileOutputStream(new File(absolutePath+File.separator+myFile.getOriginalFilename()));
			fos.write(data);

			List<Product>list=readExcel(absolutePath+File.separator+myFile.getOriginalFilename());
			msg=dao.uploadProducts(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	@Override
	public Map<String, Object> uploadSheet(CommonsMultipartFile file, HttpSession httpSession) {

		String path = httpSession.getServletContext().getRealPath("/");
		String fileName = file.getOriginalFilename();

		int[] arr;

		FileOutputStream fos = null;
		byte[] data = file.getBytes();
		try {
			System.out.println(path);
			fos = new FileOutputStream(new File(path + File.separator + fileName));
			fos.write(data);
			List<Product> list = readExcel(path + File.separator + fileName);
			arr = dao.uploadProductList(list);
			
			map.put("Total Record In Sheet", totalRecordCount);
			map.put("Uploaded Records In DB", arr[0]);
			map.put("Exists Records In DB", arr[1]);
			map.put("Total Excluded ", errorMap.size());
			map.put("Bad Record Row Number", errorMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public String exportToExcel(HttpSession session) {
		List<Product> allProduct = getAllProducts();
		
		String[] columns = { "NAME", "QTY", "PRICE", "TYPE" };
		try {
			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
			/*
			 * CreationHelper helps us create instances of various things like DataFormat,
			 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
			 */
			// Create a Sheet
			Sheet sheet = workbook.createSheet("product");
			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());
			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			// Create a Row
			Row headerRow = sheet.createRow(0);
			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}
			// Create Other rows and cells with products data
			int rowNum = 1;
			for (Product product : allProduct) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(product.getProductName());
				row.createCell(1).setCellValue(product.getProductQTY());
				row.createCell(2).setCellValue(product.getProductPrice());
				row.createCell(3).setCellValue(product.getProductType());
			}
			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			// Write the output to a file
			// path = session.getServletContext().getRealPath("/exported");
			String path = System.getProperty("user.home");
			String systemPath = path + "/Downloads/abc.xlsx";
			FileOutputStream outputStream = new FileOutputStream(new File(systemPath));
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return excludedRows;
	}
}

