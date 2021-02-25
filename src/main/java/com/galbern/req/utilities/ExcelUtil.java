package com.galbern.req.utilities;

import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Requisition;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelUtil {
    public static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    @Autowired
    private FilesUtil filesUtil;

    public File findRequisitionFile(Requisition requisition) throws IOException {

//        String filePath = filesUtil.createFile(requisition);
        String filePath = filesUtil.createDirectoryFile(requisition);

        LOGGER.info("[file started] - {}", filePath);
        try {
            return writeObjects2ExcelFile(requisition.getItems(), filePath);
        }catch (IOException e){
            LOGGER.error("[EXCEL-CREATION-FAILURE] failed to generate excel for {} ",
                    new Gson().toJson(requisition).replaceAll("[\r\n]+",""));

            throw e;
        }
    }

    public File writeObjects2ExcelFile(List<Item> items, String filePath) throws IOException {
    String[] columns = {"ID", "REMARKS", "QTY", "PRICE", "AMOUNT"};

    Workbook workbook = new XSSFWorkbook();
//    workbook.createName().setNameName(filePath); //see how it works

    CreationHelper createHelper = workbook.getCreationHelper();

    Sheet sheet = workbook.createSheet("Items");

    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setColor(IndexedColors.BLUE.getIndex());

    CellStyle headerCellStyle = workbook.createCellStyle();
    headerCellStyle.setFont(headerFont);

    // Row for Header
    Row headerRow = sheet.createRow(0);

    // Header
    for (int col = 0; col < columns.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(columns[col]);
        cell.setCellStyle(headerCellStyle);
    }

    // CellStyle for Qty
    CellStyle amountCellStyle = workbook.createCellStyle();
        amountCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

    int rowIdx = 1;
    for (Item item : items) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(item.getId());
        row.createCell(1).setCellValue(item.getDescription());
        row.createCell(2).setCellValue(String.valueOf(item.getQuantity()));
        row.createCell(3).setCellValue(String.valueOf(item.getPrice()));

        Cell ageCell = row.createCell(3);
        ageCell.setCellValue(String.valueOf(item.getPrice().multiply(item.getQuantity())));
        ageCell.setCellStyle(amountCellStyle);
    }

    try {
        LOGGER.info("[starting workbook creation] - {}", workbook.getAllNames());

        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        LOGGER.info("[workbook created] - {}", workbook.getAllNames());
        fileOut.close();
        workbook.close();
        return new File(filePath);
    }catch (Exception e){
        LOGGER.error("[WORKBOOK-CREATION-FAILURE] - failed to create {}", filePath, e );
        throw e;
    }

    }
}