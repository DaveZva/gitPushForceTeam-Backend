package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class PdfGenerationService {

    private static final float[] PRIMARY_COLOR = {0.008f, 0.482f, 1.0f};
    private static final float[] HEADER_BG = {0.94f, 0.94f, 0.94f};
    private static final float[] TEXT_DARK = {0.12f, 0.12f, 0.12f};
    private static final float[] TEXT_LIGHT = {0.4f, 0.4f, 0.4f};
    private static final float[] BORDER_COLOR = {0.85f, 0.85f, 0.85f};

    private final float pageMargin = 30;

    public byte[] createJudgingSheetsPdf(List<JudgingSheet> sheets) throws IOException {
        if (sheets.isEmpty()) {
            throw new IllegalArgumentException("No sheets to generate PDF");
        }

        Show show = sheets.get(0).getShow();
        Judge judge = sheets.get(0).getJudge();
        String day = sheets.get(0).getDay();

        try (PDDocument document = new PDDocument()) {
            PDType0Font fontRegular = loadFont(document, "/fonts/DejaVuSans.ttf");
            PDType0Font fontBold = loadFont(document, "/fonts/DejaVuSans-Bold.ttf");

            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            PDPageContentStream cs = new PDPageContentStream(document, page);
            float yPosition = pageHeight - pageMargin - 40;

            cs.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
            cs.addRect(0, yPosition + 10, pageWidth, 40);
            cs.fill();

            cs.beginText();
            cs.setFont(fontBold, 18);
            cs.setNonStrokingColor(1f, 1f, 1f);
            cs.newLineAtOffset(pageMargin, yPosition + 22);
            cs.showText(judge.getFirstName() + " " + judge.getLastName());
            cs.endText();

            cs.beginText();
            cs.setFont(fontRegular, 10);
            cs.newLineAtOffset(pageMargin, yPosition + 8);
            cs.showText(day);
            cs.endText();

            yPosition -= 50;

            float col1 = 35;
            float col2 = 80;
            float col3 = 35;
            float col4 = 50;
            float col5 = 65;
            float col6 = 40;
            float col7 = 40;
            float col8 = 40;
            float col9 = 40;
            float col10 = 40;
            float col11 = 40;
            float col12 = 40;
            float col13 = 40;
            float col14 = 100;

            String[] headers = {"No.", "EMS", "Sex", "Class", "Born", "Ad M", "Ad F", "Ne M", "Ne F", "11 M", "11 F", "12 M", "12 F", "Results"};
            float[] colWidths = {col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14};

            float rowHeight = 22;
            drawTableHeader(cs, fontBold, headers, colWidths, yPosition, rowHeight);
            yPosition -= rowHeight;

            for (JudgingSheet sheet : sheets) {
                if (yPosition < 60) {
                    cs.close();
                    page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                    yPosition = pageHeight - pageMargin;
                    drawTableHeader(cs, fontBold, headers, colWidths, yPosition, rowHeight);
                    yPosition -= rowHeight;
                }

                drawTableRow(cs, fontRegular, sheet, colWidths, yPosition, rowHeight);
                yPosition -= rowHeight;
            }

            cs.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawTableHeader(PDPageContentStream cs, PDType0Font font, String[] headers, float[] colWidths, float yPos, float rowHeight) throws IOException {
        cs.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
        float totalWidth = 0;
        for (float w : colWidths) totalWidth += w;
        cs.addRect(pageMargin, yPos, totalWidth, rowHeight);
        cs.fill();

        cs.setNonStrokingColor(1f, 1f, 1f);
        float xPos = pageMargin;

        for (int i = 0; i < headers.length; i++) {
            cs.beginText();
            cs.setFont(font, 9);
            cs.newLineAtOffset(xPos + 3, yPos + 7);
            cs.showText(headers[i]);
            cs.endText();
            xPos += colWidths[i];
        }
    }

    private void drawTableRow(PDPageContentStream cs, PDType0Font font, JudgingSheet sheet, float[] colWidths, float yPos, float rowHeight) throws IOException {
        Cat cat = sheet.getCatEntry().getCat();

        cs.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
        cs.setLineWidth(0.5f);

        float xPos = pageMargin;
        float totalWidth = 0;
        for (float w : colWidths) totalWidth += w;

        cs.addRect(pageMargin, yPos, totalWidth, rowHeight);
        cs.stroke();

        xPos = pageMargin;
        for (float width : colWidths) {
            cs.moveTo(xPos, yPos);
            cs.lineTo(xPos, yPos + rowHeight);
            cs.stroke();
            xPos += width;
        }

        xPos = pageMargin;
        String[] rowData = {
                String.valueOf(sheet.getCatalogNumber()),
                cat.getEmsCode() != null ? cat.getEmsCode() : "",
                cat.getGender() == Cat.Gender.MALE ? "0,1" : "1,0",
                sheet.getCatEntry().getShowClass() != null ? getClassCode(sheet.getCatEntry().getShowClass()) : "",
                cat.getBirthDate() != null ? cat.getBirthDate() : "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                sheet.getGrade() != null ? sheet.getGrade() : ""
        };

        cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        for (int i = 0; i < rowData.length; i++) {
            cs.beginText();
            cs.setFont(font, 8);
            cs.newLineAtOffset(xPos + 3, yPos + 7);
            cs.showText(rowData[i]);
            cs.endText();
            xPos += colWidths[i];
        }
    }

    private String getClassCode(RegistrationEntry.ShowClass showClass) {
        return showClass != null ? showClass.getFifeCode() : "";
    }

    public byte[] generateRegistrationPdf(Registration reg) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDType0Font fontRegular = loadFont(document, "/fonts/DejaVuSans.ttf");
            PDType0Font fontBold = loadFont(document, "/fonts/DejaVuSans-Bold.ttf");
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float currentY = 750;
                cs.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
                cs.addRect(0, 842 - 60, 595, 60);
                cs.fill();
                cs.setNonStrokingColor(1f, 1f, 1f);
                cs.beginText();
                cs.setFont(fontBold, 24);
                cs.newLineAtOffset(50, 802);
                cs.showText("CatShow");
                cs.endText();
                currentY -= 40;
                cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
                cs.beginText();
                cs.setFont(fontBold, 22);
                cs.newLineAtOffset(50, currentY);
                cs.showText("Potvrzení o přihlášení");
                cs.endText();
                currentY -= 60;
                cs.beginText();
                cs.setFont(fontBold, 14);
                cs.newLineAtOffset(50, currentY);
                cs.showText("Registrační číslo: " + reg.getRegistrationNumber());
                cs.endText();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private PDType0Font loadFont(PDDocument doc, String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new IOException("Font not found: " + path);
            return PDType0Font.load(doc, is);
        }
    }
}