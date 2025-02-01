package com.devilish.granja.services;

import com.devilish.granja.entities.Sale;
import com.devilish.granja.repository.SaleRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SalesReportService {

    private final SaleRepository saleRepository;

    public SalesReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public byte[] generateSalesReport() throws IOException {
        List<Sale> sales = saleRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório de Vendas");


        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(25);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("RELATÓRIO DE VENDA");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));


        Row dateRow = sheet.createRow(1);
        dateRow.setHeightInPoints(20);
        Cell dateCell = dateRow.createCell(5);
        dateCell.setCellValue("Gerado em: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dateCell.setCellStyle(dataStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));


        Row headerRow = sheet.createRow(2);
        headerRow.setHeightInPoints(20);
        String[] headers = {"Nome", "Status", "Cliente", "Tipo do Cliente", "Valor", "Data/Hora", "Vendedor"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }


        sheet.setAutoFilter(new CellRangeAddress(2, 2, 0, headers.length - 1));


        int rowNum = 3;
        for (Sale sale : sales) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sale.getDucks().get(0).getName());
            row.createCell(1).setCellValue(sale.getDucks().get(0).isSold() ? "Vendido" : "Disponível");
            row.createCell(2).setCellValue(sale.getClient().getName());
            row.createCell(3).setCellValue(sale.getClient().isDiscountEligible() ? "Com Desconto" : "Sem Desconto");
            row.createCell(4).setCellValue(sale.getTotalValue());
            row.createCell(5).setCellValue(sale.getDateSale().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            row.createCell(6).setCellValue(sale.getSeller().getName());


            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
