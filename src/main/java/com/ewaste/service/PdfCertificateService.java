package com.ewaste.service;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.EwasteItem;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PdfCertificateService {

    public byte[] generatePdf(Pickup pickup) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            Paragraph title = new Paragraph("CERTIFICATE OF E-WASTE RECYCLING", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            document.add(new Paragraph("Certificate No: " + UUID.randomUUID().toString(), textFont));
            document.add(new Paragraph("Date of Issue: " + LocalDate.now(), textFont));
            document.add(new Paragraph(" ", textFont));
            
            document.add(new Paragraph("This certifies that the electronic waste submitted by:", textFont));
            document.add(new Paragraph(pickup.getCitizen().getName() + " (" + pickup.getCitizen().getEmail() + ")", new Font(Font.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph(" ", textFont));
            
            document.add(new Paragraph("Has been securely recycled and processed in an environmentally friendly manner by:", textFont));
            document.add(new Paragraph(pickup.getRecycler().getName(), new Font(Font.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph(" ", textFont));

            document.add(new Paragraph("Items Recycled:", new Font(Font.HELVETICA, 12, Font.BOLD)));
            for (EwasteItem item : pickup.getItems()) {
                document.add(new Paragraph("- " + item.getItemType() + " (" + item.getWeight() + " kg)", textFont));
            }
            
            document.add(new Paragraph(" ", textFont));
            Paragraph footer = new Paragraph("Thank you for contributing to a sustainable future!", new Font(Font.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
