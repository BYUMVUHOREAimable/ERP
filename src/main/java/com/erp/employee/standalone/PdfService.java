package com.erp.employee.standalone;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    public byte[] generatePdfTransactions(List<String> headers, List<List<String>> data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Transactions", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Create table
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);

            // Header row
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            for (String header : headers) {
                PdfPCell hCell = new PdfPCell(new Phrase(header, headFont));
                hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(hCell);
            }

            // Data rows
            for (List<String> rowData : data) {
                for (String cellData : rowData) {
                    PdfPCell cell = new PdfPCell(new Phrase(cellData));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
        return out.toByteArray();
    }
}
