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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfGenerationService {

    private static final float[] PRIMARY_COLOR = {0.008f, 0.482f, 1.0f};
    private static final float[] HEADER_BG = {0.96f, 0.96f, 0.96f};
    private static final float[] TEXT_DARK = {0.1f, 0.1f, 0.1f};
    private static final float[] TEXT_GRAY = {0.4f, 0.4f, 0.4f};
    private static final float[] BORDER_COLOR = {0.85f, 0.85f, 0.85f};

    private final float pageMargin = 40;

    public byte[] createJudgingSheetsPdf(List<JudgingSheet> sheets) throws IOException {
        if (sheets.isEmpty()) {
            throw new IllegalArgumentException("No sheets to generate PDF");
        }

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

            float[] colWidths = {35, 80, 35, 50, 65, 40, 40, 40, 40, 40, 40, 40, 40, 100};
            String[] headers = {"No.", "EMS", "Sex", "Class", "Born", "Ad M", "Ad F", "Ne M", "Ne F", "11 M", "11 F", "12 M", "12 F", "Results"};

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
                drawTableRow(cs, fontRegular, fontBold, sheet, colWidths, yPosition, rowHeight);
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

    private void drawTableRow(PDPageContentStream cs, PDType0Font font, PDType0Font fontBold, JudgingSheet sheet, float[] colWidths, float yPos, float rowHeight) throws IOException {
        Cat cat = sheet.getCatEntry().getCat();
        RegistrationEntry entry = sheet.getCatEntry();

        String adM = "", adF = "", neM = "", neF = "", c11M = "", c11F = "", c12M = "", c12F = "";
        String classCode = getClassCode(entry.getShowClass());
        boolean isMale = cat.getGender() == Cat.Gender.MALE;
        boolean isNeuter = entry.isNeutered();

        List<String> neuterClasses = Arrays.asList("2", "4", "6", "8", "10");
        if (neuterClasses.contains(classCode)) isNeuter = true;

        List<String> adultClasses = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "13a", "13b", "13c", "14", "15", "16", "17");

        if ("11".equals(classCode)) {
            if (isMale) c11M = "X"; else c11F = "X";
        } else if ("12".equals(classCode)) {
            if (isMale) c12M = "X"; else c12F = "X";
        } else if (adultClasses.contains(classCode)) {
            if (isNeuter) {
                if (isMale) neM = "X"; else neF = "X";
            } else {
                if (isMale) adM = "X"; else adF = "X";
            }
        }

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
                isMale ? "1,0" : "0,1",
                classCode,
                cat.getBirthDate() != null ? cat.getBirthDate() : "",
                adM, adF, neM, neF, c11M, c11F, c12M, c12F,
                sheet.getGrade() != null ? sheet.getGrade() : ""
        };

        cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        for (int i = 0; i < rowData.length; i++) {
            String text = rowData[i];
            boolean isMark = (i >= 5 && i <= 12);
            cs.beginText();
            cs.setFont(isMark && "X".equals(text) ? fontBold : font, 8);
            float xOffset = isMark && "X".equals(text) ? xPos + (colWidths[i] / 2) - 3 : xPos + 3;
            cs.newLineAtOffset(xOffset, yPos + 7);
            cs.showText(text);
            cs.endText();
            xPos += colWidths[i];
        }
    }

    public byte[] generateRegistrationPdf(Registration reg) throws IOException {
        Show show = reg.getShow();
        Owner owner = reg.getOwner();

        try (PDDocument document = new PDDocument()) {
            PDType0Font fontRegular = loadFont(document, "/fonts/DejaVuSans.ttf");
            PDType0Font fontBold = loadFont(document, "/fonts/DejaVuSans-Bold.ttf");

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(document, page);
            float y = 780;

            cs.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
            cs.addRect(0, 842 - 80, 595, 80);
            cs.fill();

            cs.beginText();
            cs.setFont(fontBold, 24);
            cs.setNonStrokingColor(1f, 1f, 1f);
            cs.newLineAtOffset(pageMargin, 842 - 50);
            cs.showText("Potvrzení o registraci");
            cs.endText();

            y -= 40;
            drawSectionHeader(cs, fontBold, "Informace o výstavě", y);
            y -= 25;
            drawLabelValue(cs, fontBold, fontRegular, "Název výstavy:", show.getName(), pageMargin, y);
            y -= 15;
            drawLabelValue(cs, fontBold, fontRegular, "Místo konání:", show.getVenueName() + ", " + show.getVenueCity(), pageMargin, y);
            y -= 15;
            drawLabelValue(cs, fontBold, fontRegular, "Datum:", show.getStartDate() + " - " + show.getEndDate(), pageMargin, y);
            y -= 15;
            drawLabelValue(cs, fontBold, fontRegular, "Pořadatel:", show.getOrganizerName() + " (" + show.getOrganizerContactEmail() + ")", pageMargin, y);

            y -= 40;
            drawSectionHeader(cs, fontBold, "Informace o vystavovateli (Majitel)", y);
            y -= 25;
            drawLabelValue(cs, fontBold, fontRegular, "Jméno a příjmení:", owner.getFirstName() + " " + owner.getLastName(), pageMargin, y);
            y -= 15;
            drawLabelValue(cs, fontBold, fontRegular, "E-mail:", owner.getEmail(), pageMargin, y);
            y -= 15;
            drawLabelValue(cs, fontBold, fontRegular, "Registrační číslo přihlášky:", reg.getRegistrationNumber(), pageMargin, y);

            y -= 40;
            drawSectionHeader(cs, fontBold, "Přihlášené kočky", y);
            y -= 20;

            for (RegistrationEntry entry : reg.getEntries()) {
                if (y < 200) {
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                    y = 780;
                }

                Cat cat = entry.getCat();
                cs.setNonStrokingColor(HEADER_BG[0], HEADER_BG[1], HEADER_BG[2]);
                cs.addRect(pageMargin, y - 100, 595 - (2 * pageMargin), 110);
                cs.fill();
                cs.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
                cs.addRect(pageMargin, y - 100, 595 - (2 * pageMargin), 110);
                cs.stroke();

                float innerY = y - 20;
                cs.beginText();
                cs.setFont(fontBold, 12);
                cs.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
                cs.newLineAtOffset(pageMargin + 10, innerY);
                cs.showText(getString(cat.getTitleBefore()) + " " + cat.getCatName() + " " + getString(cat.getTitleAfter()));
                cs.endText();

                innerY -= 20;
                drawLabelValue(cs, fontBold, fontRegular, "EMS Kód:", cat.getEmsCode(), pageMargin + 10, innerY);
                drawLabelValue(cs, fontBold, fontRegular, "Pohlaví:", cat.getGender().name(), pageMargin + 200, innerY);
                drawLabelValue(cs, fontBold, fontRegular, "Narození:", cat.getBirthDate(), pageMargin + 350, innerY);

                innerY -= 15;
                drawLabelValue(cs, fontBold, fontRegular, "Třída:", entry.getShowClass() != null ? entry.getShowClass().name().replace("_", " ") + " (" + entry.getShowClass().getFifeCode() + ")" : "-", pageMargin + 10, innerY);
                drawLabelValue(cs, fontBold, fontRegular, "Číslo PP:", cat.getPedigreeNumber(), pageMargin + 200, innerY);
                drawLabelValue(cs, fontBold, fontRegular, "Čip:", cat.getChipNumber(), pageMargin + 350, innerY);

                innerY -= 15;
                drawLabelValue(cs, fontBold, fontRegular, "Otec:", getString(cat.getFatherName()), pageMargin + 10, innerY);
                innerY -= 15;
                drawLabelValue(cs, fontBold, fontRegular, "Matka:", getString(cat.getMotherName()), pageMargin + 10, innerY);

                y -= 130;
            }

            y -= 20;
            cs.setStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
            cs.setLineWidth(1);
            cs.moveTo(pageMargin, y);
            cs.lineTo(595 - pageMargin, y);
            cs.stroke();

            y -= 20;
            cs.beginText();
            cs.setFont(fontRegular, 8);
            cs.setNonStrokingColor(TEXT_GRAY[0], TEXT_GRAY[1], TEXT_GRAY[2]);
            cs.newLineAtOffset(pageMargin, y);
            cs.showText("Odesláním přihlášky vystavovatel potvrzuje, že souhlasí s výstavními pravidly FIFe a propozicemi výstavy.");
            cs.newLineAtOffset(0, -10);
            cs.showText("Vystavovatel uděluje souhlas se zpracováním osobních údajů pro účely výstavy.");
            cs.endText();

            cs.close();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawSectionHeader(PDPageContentStream cs, PDType0Font font, String title, float y) throws IOException {
        cs.setNonStrokingColor(HEADER_BG[0], HEADER_BG[1], HEADER_BG[2]);
        cs.addRect(pageMargin, y - 5, 595 - (2 * pageMargin), 20);
        cs.fill();
        cs.beginText();
        cs.setFont(font, 11);
        cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        cs.newLineAtOffset(pageMargin + 5, y);
        cs.showText(title);
        cs.endText();
    }

    private void drawLabelValue(PDPageContentStream cs, PDType0Font fontBold, PDType0Font fontRegular, String label, String value, float x, float y) throws IOException {
        cs.beginText();
        cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        cs.setFont(fontBold, 9);
        cs.newLineAtOffset(x, y);
        cs.showText(label);
        cs.setFont(fontRegular, 9);
        cs.showText(" " + (value != null ? value : ""));
        cs.endText();
    }

    private String getString(String val) {
        return val != null ? val : "";
    }

    private String getClassCode(RegistrationEntry.ShowClass showClass) {
        return showClass != null ? showClass.getFifeCode() : "";
    }

    private PDType0Font loadFont(PDDocument doc, String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new IOException("Font not found: " + path);
            return PDType0Font.load(doc, is);
        }
    }
}