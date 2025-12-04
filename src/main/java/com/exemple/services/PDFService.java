package com.exemple.services;

import java.io.FileOutputStream;
import java.io.IOException;
import com.exemple.models.Employee;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFService {

    public static void generateEmployeePDF(Employee employee, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Titre
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Paragraph title = new Paragraph("Fiche Salarié", titleFont);
        title.setSpacingAfter(20);
        document.add(title);

        // Informations du salarié
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Tableau pour les informations
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        addTableRow(table, "Nom complet : ", 
                   employee.getFirstName() + " " + employee.getLastName(), 
                   labelFont, valueFont);
        addTableRow(table, "Téléphone fixe : ", 
                   employee.getPhone() != null ? employee.getPhone() : "N/A", 
                   labelFont, valueFont);
        addTableRow(table, "Téléphone portable : ", 
                   employee.getCell() != null ? employee.getCell() : "N/A", 
                   labelFont, valueFont);
        addTableRow(table, "Email : ", 
                   employee.getEmail(), 
                   labelFont, valueFont);
        addTableRow(table, "Service : ", 
                   employee.getService() != null ? employee.getService().getName() : "N/A", 
                   labelFont, valueFont);
        addTableRow(table, "Site : ", 
                   employee.getSite() != null ? employee.getSite().getCity() : "N/A", 
                   labelFont, valueFont);

        document.add(table);

        document.close();
    }

    private static void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setPadding(5);
        labelCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setPadding(5);
        valueCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(valueCell);
    }
}

