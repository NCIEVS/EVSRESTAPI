package gov.nih.nci.evs.restapi.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelUtils {

    public ExcelUtils() {

    }

	public static void saveToFile(String outputfile, Vector v) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			if (v != null && v.size() > 0) {
				for (int i=0; i<v.size(); i++) {
					String t = (String) v.elementAt(i);
					pw.println(t);
				}
		    }
		} catch (Exception ex) {

		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

    private static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                Boolean bool_obj = cell.getBooleanCellValue();
                boolean bool = Boolean.valueOf(bool_obj);
                return "" + bool;

            case STRING:
                return (cell.getRichStringCellValue().getString());

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return ("" + cell.getDateCellValue());
                } else {
                    return ("" + cell.getNumericCellValue());
                }

            case FORMULA:
                return(cell.getCellFormula().toString());

            case BLANK:
                return "";

            default:
                return "";
        }
    }

	public static Vector readFile(String filename)
	{
		Vector v = new Vector();
		try {
			BufferedReader in = new BufferedReader(
			   new InputStreamReader(
						  new FileInputStream(filename), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				v.add(str);
			}
            in.close();
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return v;
	}

    public static Vector parseData(String line, char delimiter) {
		if(line == null) return null;
		Vector w = new Vector();
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			if (c == delimiter) {
				w.add(buf.toString());
				buf = new StringBuffer();
			} else {
				buf.append(c);
			}
		}
		w.add(buf.toString());
		return w;
	}

    public static Vector excel2Text(String excelfile, int sheetIndex) {
		return excel2Text(excelfile, sheetIndex, '\t');
	}

    public static String clone(String excelfile) {
		Workbook workbook = ExcelReader.openWorkbook(excelfile);
		ExcelWriter.write(workbook, "clone_" + excelfile);
		return "clone_" + excelfile;
	}

    public static Vector excel2Text(String excelfile, int sheetIndex, char delim) {
		int n = excelfile.lastIndexOf(".");
		String textfile = excelfile.substring(0, n) + ".txt";
		Vector w = new Vector();
		Workbook workbook = ExcelReader.openWorkbook(excelfile);
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		int numberOfRows = getNumberOfRows(sheet);
		for (int i=0; i<numberOfRows; i++) {
			Row row = sheet.getRow(i);
			StringBuffer buf = new StringBuffer();
			int numberOfCells = getNumberOfCells(row);
			for (int j=0; j<numberOfCells; j++) {
			    Cell cell = row.getCell(j);
			    String value = getCellValue(cell);
			    buf.append(value);
				if (j<numberOfCells-1) {
					buf.append(delim);
				}
			}
			w.add(buf.toString());
		}
		saveToFile(textfile, w);
		return w;
	}

    public static CellStyle getCellStype(String excelfile, int sheetNumber, int rowNumber) {
		try {
			Workbook workbook = ExcelReader.openWorkbook(excelfile);
			Sheet sheet = workbook.getSheetAt(sheetNumber);
			Row row = sheet.getRow(rowNumber);
			return row.getRowStyle();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public static int getNumberOfSheet(Workbook workbook) {
		return workbook.getNumberOfSheets();
	}

    public static int getNumberOfRows(Sheet sheet) {
		return sheet.getLastRowNum();
	}

    public static int getNumberOfRows(Workbook workbook, int sheetIndex) {
		return workbook.getSheetAt(sheetIndex).getLastRowNum();
	}

    public static int getNumberOfCells(Row row) {
		return row.getPhysicalNumberOfCells();
	}

    public static int getNumberOfCells(Workbook workbook, int sheetIndex, int rowIndex) {
		return getNumberOfCells(workbook.getSheetAt(sheetIndex).getRow(rowIndex));
	}

    public static void dumpMetadata(String excelfile) {
	    Workbook workbook = ExcelReader.openWorkbook(excelfile);
	    int numberOfSheets = workbook.getNumberOfSheets();
	    System.out.println("numberOfSheets: " + numberOfSheets);
	    for (int i=0; i<numberOfSheets; i++) {
		    Sheet sheet = workbook.getSheetAt(i);
		    String sheetName = sheet.getSheetName();
		    System.out.println("sheetName: " + sheetName);
		    int lastRowNum = getNumberOfRows(sheet);
		    int rowCount = lastRowNum + 1;
		    System.out.println("numberOfRows: " + rowCount);
		    for (int j=0; j<rowCount; j++) {
				Row row = sheet.getRow(j);
				short height = row.getHeight();
				int lastCellNum = row.getLastCellNum();
				System.out.println("numberOfCells: " + j + ": " +  lastCellNum + "; height: " + height);
			}
		}
	}

    public static void showHeadingCellStype(String excelfile) {
		int sheetNumber = 0;
		int rowNumber = 0;
	    CellStyle style = getCellStype(excelfile, 0, 0);
	    short al = style.getAlignment();
	    short bb = style.getBorderBottom();
	    short bl = style.getBorderLeft();

	    System.out.println("getAlignment: " + al);
	    System.out.println("getBorderBottom: " + bb);
	    System.out.println("getBorderLeft: " + bl);

        short fillForegroundColor = style.getFillForegroundColor();
        System.out.println("fillForegroundColor: " + fillForegroundColor);

        short fillBackgroundColor = style.getFillBackgroundColor();
        System.out.println("fillBackgroundColor: " + fillBackgroundColor);

        int fps = style.getFillPattern();
        System.out.println("getFillPattern: " + fps);
    }

    public static CellStyle createWrappedCellStyle(Sheet sheet, boolean wrapped) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		cellStyle.setWrapText(wrapped);
		return cellStyle;
	}

    public static CellStyle createCellStyle(Sheet sheet, int colorIndex, int patternIndex) {
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		if (colorIndex != -1) {
			IndexedColors indexedColors = IndexedColors.fromInt(colorIndex);
			cellStyle.setFillBackgroundColor(indexedColors.index);
		}
		if (patternIndex != -1) {
			FillPatternType fillPatternType = FillPatternType.forInt(patternIndex);
			cellStyle.setFillPattern(fillPatternType);
		}
		return cellStyle;
	}

	public static Sheet createRow(Sheet sheet, int rowNumber, Object[] objArr, int colorIndex, int patternIndex) {
		CellStyle cellStyle = createCellStyle(sheet, colorIndex, patternIndex);
		Row row = sheet.createRow(rowNumber);

		int cellnum = 0;
		for (Object obj : objArr) {
			Cell cell = row.createCell(cellnum++);
			cell.setCellStyle(cellStyle);
			if (obj instanceof String) {
				cell.setCellValue((String) obj);
			} else if (obj instanceof Integer) {
				cell.setCellValue((Integer) obj);
			}
		}
		return sheet;
	}

    public static void write(Workbook workbook, String excelfile) {
		try {
			FileOutputStream fileOut = new FileOutputStream(excelfile);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public static void generateTemplate(String excelfile, int sheetIndex) {
		String clonedfile = clone(excelfile);
		Workbook workbook = ExcelReader.openWorkbook(clonedfile);
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		int numberOfRows = getNumberOfRows(sheet);
		for (int i=1; i<=numberOfRows; i++) {
			Row row = sheet.getRow(i);
			int cellCount = getNumberOfCells(row);
			for (int j=0; j<cellCount; j++) {
				Cell cell = row.getCell(j);
				if (cell != null) {
					row.removeCell(row.getCell(j));
				}
			}
		}
		write(workbook, "template_" + excelfile);
	}


    public static Object[] toObjectArray(Vector v) {
		Object[] objArr = new Object[v.size()];
		for (int i=0; i<v.size(); i++) {
			objArr[i] = v.elementAt(i);
		}
		return objArr;
	}

    public static void write(String template_excelfile, String outputexcel, int sheetIndex, Vector data, char delim) {
		Workbook workbook = ExcelReader.openWorkbook(template_excelfile);
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		for (int i=1; i<data.size(); i++) {
			String line = (String) data.elementAt(i);
			Vector u = parseData(line, delim);
			sheet = createRow(sheet, i, toObjectArray(u), 0, 0);
		}
		write(workbook, outputexcel);
	}

	public static void main(String[] args) {
		String excelfile = args[0];
		////System.out.println("excelfile: " + excelfile);
		Vector data = excel2Text(excelfile, 0);
        generateTemplate(excelfile, 0);
        //write("template_" + excelfile, "v2_" + excelfile, 0, data, '\t');
	}
}
