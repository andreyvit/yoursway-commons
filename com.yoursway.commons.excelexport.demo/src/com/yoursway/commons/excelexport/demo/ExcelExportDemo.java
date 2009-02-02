package com.yoursway.commons.excelexport.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import com.yoursway.commons.excelexport.Sheet;
import com.yoursway.commons.excelexport.Workbook;

public class ExcelExportDemo {

	public static void main(String[] args) throws IOException {
		build("foo.xlsx");
		build("foo.zip");
	}

	private static void build(String fileName) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName);
		Workbook builder = new Workbook();

		Sheet first = builder.addSheet("First");
		first.cell(1, 1).setText("Hello, world!").setHorizonalSpan(2);
		first.cell(1, 3).setText("222");
		first.cell(2, 1).setText("333");
		first.cell(3, 1).setText("444");
		
		Sheet second = builder.addSheet("Second");
		second.cell(2, 2).setText("Hi there");

		builder.build(out);
		out.close();
	}

}
