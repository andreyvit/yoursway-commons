package com.yoursway.commons.excelexport.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import com.yoursway.commons.excelexport.Border;
import com.yoursway.commons.excelexport.Edge;
import com.yoursway.commons.excelexport.RGBColor;
import com.yoursway.commons.excelexport.Sheet;
import com.yoursway.commons.excelexport.SolidColorFill;
import com.yoursway.commons.excelexport.Workbook;

public class ExcelExportDemo {

	public static void main(String[] args) throws IOException {
		build("foo.xlsx");
		build("foo.zip");
	}

	private static void build(String fileName) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName);
		Workbook builder = new Workbook();

		SolidColorFill redFill = new SolidColorFill(new RGBColor(255, 0, 0));

		Sheet sheet = builder.addSheet("Fri 13.02");
		int row = 2;
		addTimeSlotsRow(sheet, row++);
		sheet.cell(row++, 1).text("Dina B");
		sheet.cell(row++, 1).text("Cormac");
		sheet.cell(row++, 1).text("Rabi");
		sheet.cell(row++, 1).text("Hatem");
		sheet.cell(row++, 1).text("Ricardo");
		sheet.cell(row++, 1).text("Jimmy Hendrix");
		row++; // empty
		sheet.cell(row++, 1).text("SPARE GUIDES").hspan(2);
		row++; // empty
		sheet.cell(row++, 1).text("OPEN REQUESTS").hspan(2);
		
		sheet.cell(3, 7).text("Flodemir Pavanel Ñ Oi Brazil").hspan(10);
		sheet.cell(4, 7).text("Juri Temant Ñ Telia Sonera").hspan(10).fill(redFill);
		sheet.cell(5, 7).text("Telenor Sweden Ñ Peter Ericsson").hspan(10);
		sheet.cell(6, 7).text("Laurent Delafosse Ñ Monaco Telecom").hspan(10);
		sheet.cell(7, 2).text("Maarit / Nigel Ñ Carphone Warehouse").hspan(10);
		sheet.cell(7, 13).text("Paloma Camronero Ñ Telefonica O2").hspan(10);
		
		sheet.range().startingAtColumn(2).columnWidth(6).center();
		sheet.range().reduceToRow(1).merge().center().text("This is a header");
		sheet.range().reduceToColumn(1).outerBorder(Border.THICK, Edge.RIGHT);
		sheet.range().startingAtColumn(2).startingAtRow(2).innerBorder(Border.THIN);

		Sheet second = builder.addSheet("Second");
		second.cell(2, 2).text("Hi there");

		builder.build(out);
		out.close();
	}

	private static void addTimeSlotsRow(Sheet sheet, int row) {
		int cell = 1;
		sheet.cell(row, cell++).text("Friday");
		for (int i = 0; i < 8; i++) {
			sheet.cell(row, cell++).text((9 + i) + ":00");
			sheet.cell(row, cell++).text((9 + i) + ":10");
			sheet.cell(row, cell++).text((9 + i) + ":20");
			sheet.cell(row, cell++).text((9 + i) + ":30");
			sheet.cell(row, cell++).text((9 + i) + ":40");
			sheet.cell(row, cell++).text((9 + i) + ":50");
		}
	}

}
