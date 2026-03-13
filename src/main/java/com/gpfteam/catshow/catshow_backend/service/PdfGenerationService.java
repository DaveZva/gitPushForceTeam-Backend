package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.*;
import com.gpfteam.catshow.catshow_backend.model.enums.ShowClass;
import jakarta.annotation.PostConstruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfGenerationService {

    private static final float[] PRIMARY_COLOR = {0.008f, 0.482f, 1.0f};
    private static final float[] CUSTOM_BLUE = {0.008f, 0.482f, 1.0f}; // #027BFF
    private static final float[] ROW_ALT_COLOR = {0.96f, 0.98f, 1.0f};
    private static final float[] FOOTER_LINE_COLOR = {0.80f, 0.90f, 1.0f};
    private static final float[] HEADER_BG = {0.96f, 0.96f, 0.96f};
    private static final float[] TEXT_DARK = {0.1f, 0.1f, 0.1f};
    private static final float[] BORDER_COLOR = {0.80f, 0.80f, 0.80f};
    private static final float[] TEXT_GRAY = {0.4f, 0.4f, 0.4f};

    private final float pageMargin = 40;
    private final Map<String, byte[]> logoCache = new HashMap<>();

    @PostConstruct
    public void preloadLogos() {
        String[] logoPaths = {"/logos/logo(1).png", "/logos/logo(2).png", "/logos/logo(3).png", "/logos/logo(4).png"};
        for (String path : logoPaths) {
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    logoCache.put(path, is.readAllBytes());
                }
            } catch (Exception ignored) {}
        }
    }

    public byte[] createJudgingSheetsPdf(List<JudgingSheet> sheets) throws IOException {
        if (sheets.isEmpty()) throw new IllegalArgumentException("No sheets to generate PDF");

        Judge judge = sheets.get(0).getJudge();
        String day = sheets.get(0).getDay();

        String showName = "";
        String showDate = "";
        if (sheets.get(0).getCatEntry() != null && sheets.get(0).getCatEntry().getRegistration() != null) {
            Show show = sheets.get(0).getCatEntry().getRegistration().getShow();
            if (show != null) {
                showName = show.getName();
                if (show.getStartDate() != null) {
                    showDate = show.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
            }
        }

        try (PDDocument document = new PDDocument()) {
            PDType0Font fontRegular = loadFont(document, "/fonts/DejaVuSans.ttf");
            PDType0Font fontBold = loadFont(document, "/fonts/DejaVuSans-Bold.ttf");

            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            PDPageContentStream cs = new PDPageContentStream(document, page);
            float yPosition = pageHeight - pageMargin - 65;

            drawCustomHeader(document, cs, fontBold, judge, day, showName, showDate, pageWidth, yPosition);

            // Posunuto výš pro zarovnání nahoru
            yPosition -= 70;
            float[] colWidths = {35, 45, 55, 35, 50, 65, 35, 35, 35, 35, 35, 35, 35, 35, 100};
            String[] headers = {"No.", "EMS", "", "Sex", "Class", "Born", "Ad M", "Ad F", "Ne M", "Ne F", "11 M", "11 F", "12 M", "12 F", "Results"};
            float rowHeight = 22;

            float totalTableWidth = 0;
            for (float w : colWidths) totalTableWidth += w;
            float tableStartX = (pageWidth - totalTableWidth) / 2;

            drawCustomTableHeader(cs, fontBold, headers, colWidths, yPosition, rowHeight, tableStartX);
            yPosition -= rowHeight;

            int rowIdx = 0;
            for (JudgingSheet sheet : sheets) {
                if (yPosition < 110) {
                    cs.close();
                    page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                    yPosition = pageHeight - pageMargin - 65;
                    drawCustomHeader(document, cs, fontBold, judge, day, showName, showDate, pageWidth, yPosition);
                    // Posunuto výš pro zarovnání nahoru
                    yPosition -= 70;
                    drawCustomTableHeader(cs, fontBold, headers, colWidths, yPosition, rowHeight, tableStartX);
                    yPosition -= rowHeight;
                }
                drawCustomTableRow(cs, fontRegular, fontBold, sheet, colWidths, yPosition, rowHeight, rowIdx % 2 != 0, tableStartX);
                yPosition -= rowHeight;
                rowIdx++;
            }

            cs.close();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawCustomHeader(PDDocument doc, PDPageContentStream cs, PDType0Font fontBold, Judge judge, String day, String showName, String showDate, float pageWidth, float y) throws IOException {
        // Nastaveni barvy pozadi na svetlou modrou (#E3F1FF)
        cs.setNonStrokingColor(0.89f, 0.945f, 1.0f);
        cs.addRect(0, y, pageWidth, 105);
        cs.fill();

        // Nastaveni log s vetsim odstupem a mezerami
        String[] logoPaths = {"/logos/logo(1).png", "/logos/logo(2).png", "/logos/logo(3).png", "/logos/logo(4).png"};
        float logoSize = 55;
        float logoSpacing = 25;
        float edgeMargin = 60;
        float logoY = y + 25;

        // Leva dvojice log
        for (int i = 0; i < 2; i++) {
            byte[] logoBytes = logoCache.get(logoPaths[i]);
            if (logoBytes != null) {
                PDImageXObject img = PDImageXObject.createFromByteArray(doc, logoBytes, logoPaths[i]);
                cs.drawImage(img, edgeMargin + (i * (logoSize + logoSpacing)), logoY, logoSize, logoSize);
            }
        }

        // Prava dvojice log
        for (int i = 2; i < 4; i++) {
            byte[] logoBytes = logoCache.get(logoPaths[i]);
            if (logoBytes != null) {
                PDImageXObject img = PDImageXObject.createFromByteArray(doc, logoBytes, logoPaths[i]);
                float xPos = pageWidth - edgeMargin - logoSize - ((3 - i) * (logoSize + logoSpacing));
                cs.drawImage(img, xPos, logoY, logoSize, logoSize);
            }
        }

        String fullName = judge.getFirstName().toUpperCase() + " " + judge.getLastName().toUpperCase();
        float nameFontSize = 20;
        float nameSpacing = -1.5f;
        float nameWidth = (fontBold.getStringWidth(fullName) / 1000 * nameFontSize) + (fullName.length() * nameSpacing);
        float nameX = (pageWidth - nameWidth) / 2;

        cs.beginText();
        cs.setFont(fontBold, nameFontSize);
        // Nastaveni barvy jmena na ČERNOU (TEXT_DARK)
        cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        cs.setCharacterSpacing(nameSpacing);
        cs.newLineAtOffset(nameX, y + 75);
        cs.showText(fullName);
        cs.endText();
        cs.setCharacterSpacing(0);

        String infoText = (showName != null ? showName.toUpperCase() : "") + " | " + (showDate != null ? showDate : "");
        float infoFontSize = 10;
        float infoWidth = fontBold.getStringWidth(infoText) / 1000 * infoFontSize;
        float infoX = (pageWidth - infoWidth) / 2;

        cs.beginText();
        cs.setFont(fontBold, infoFontSize);
        // Nastaveni barvy info o vystave na ČERNOU (TEXT_DARK)
        cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        cs.newLineAtOffset(infoX, y + 55);
        cs.showText(infoText);
        cs.endText();

        float rectWidth = 120;
        float rectHeight = 22;
        float rectX = (pageWidth - rectWidth) / 2;
        float rectY = y + 17;

        // Pozadi tlacitka nechame syte modre (#027BFF)
        cs.setNonStrokingColor(CUSTOM_BLUE[0], CUSTOM_BLUE[1], CUSTOM_BLUE[2]);
        drawRoundedRect(cs, rectX, rectY, rectWidth, rectHeight, 10);

        String dayText = day.toUpperCase();
        float dayFontSize = 10;
        float daySpacing = -1.25f;
        float dayWidth = (fontBold.getStringWidth(dayText) / 1000 * dayFontSize) + (dayText.length() * daySpacing);
        float textX = rectX + (rectWidth - dayWidth) / 2;

        cs.beginText();
        cs.setFont(fontBold, dayFontSize);
        // Text uvnitr tlacitka nechame bily
        cs.setNonStrokingColor(1f, 1f, 1f);
        cs.setCharacterSpacing(daySpacing);
        cs.newLineAtOffset(textX, rectY + 7);
        cs.showText(dayText);
        cs.endText();
        cs.setCharacterSpacing(0);
    }

    private void drawRoundedRect(PDPageContentStream cs, float x, float y, float width, float height, float r) throws IOException {
        float k = 0.552284749831f;
        cs.moveTo(x + r, y);
        cs.lineTo(x + width - r, y);
        cs.curveTo(x + width - r + k * r, y, x + width, y + r - k * r, x + width, y + r);
        cs.lineTo(x + width, y + height - r);
        cs.curveTo(x + width, y + height - r + k * r, x + width - r + k * r, y + height, x + width - r, y + height);
        cs.lineTo(x + r, y + height);
        cs.curveTo(x + r - k * r, y + height, x, y + height - r + k * r, x, y + height - r);
        cs.lineTo(x, y + r);
        cs.curveTo(x, y + r - k * r, x + r - k * r, y, x + r, y);
        cs.fill();
    }

    private void drawCustomTableHeader(PDPageContentStream cs, PDType0Font font, String[] headers, float[] colWidths, float yPos, float rowHeight, float startX) throws IOException {
        float totalWidth = 0;
        for (float w : colWidths) totalWidth += w;

        cs.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
        cs.addRect(startX, yPos, totalWidth, rowHeight);
        cs.fill();

        cs.setStrokingColor(1f, 1f, 1f);
        cs.setLineWidth(0.5f);

        float xPos = startX;
        for (int i = 0; i < headers.length; i++) {
            if (i == 2) {
                xPos += colWidths[i];
                continue;
            }

            cs.moveTo(xPos, yPos);
            cs.lineTo(xPos, yPos + rowHeight);
            cs.stroke();

            cs.beginText();
            cs.setFont(font, 8);
            cs.setNonStrokingColor(1f, 1f, 1f);

            float labelX = xPos + 3;
            if (i == 1) {
                float emsCombinedWidth = colWidths[1] + colWidths[2];
                float textWidth = font.getStringWidth("EMS") / 1000 * 8;
                labelX = xPos + (emsCombinedWidth - textWidth) / 2;
            }

            cs.newLineAtOffset(labelX, yPos + 7);
            cs.showText(headers[i]);
            cs.endText();
            xPos += colWidths[i];
        }
        cs.moveTo(xPos, yPos);
        cs.lineTo(xPos, yPos + rowHeight);
        cs.stroke();
    }

    private void drawCustomTableRow(PDPageContentStream cs, PDType0Font font, PDType0Font fontBold, JudgingSheet sheet, float[] colWidths, float yPos, float rowHeight, boolean isAlt, float startX) throws IOException {
        float totalWidth = 0; for (float w : colWidths) totalWidth += w;

        if (isAlt) {
            cs.setNonStrokingColor(ROW_ALT_COLOR[0], ROW_ALT_COLOR[1], ROW_ALT_COLOR[2]);
            cs.addRect(startX, yPos, totalWidth, rowHeight);
            cs.fill();
        }

        cs.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
        cs.setLineWidth(0.5f);
        cs.addRect(startX, yPos, totalWidth, rowHeight);
        cs.stroke();

        float xPos = startX;
        for (float width : colWidths) {
            cs.moveTo(xPos, yPos);
            cs.lineTo(xPos, yPos + rowHeight);
            cs.stroke();
            xPos += width;
        }

        Cat cat = sheet.getCatEntry().getCat();
        RegistrationEntry entry = sheet.getCatEntry();
        String classCode = getClassCode(entry.getShowClass());
        boolean isMale = cat.getGender() == Cat.Gender.MALE;
        boolean isNeuter = entry.isNeutered() || Arrays.asList("2", "4", "6", "8", "10").contains(classCode);

        String adM="", adF="", neM="", neF="", c11M="", c11F="", c12M="", c12F="";
        if ("11".equals(classCode)) { if (isMale) c11M = "X"; else c11F = "X"; }
        else if ("12".equals(classCode)) { if (isMale) c12M = "X"; else c12F = "X"; }
        else {
            if (isNeuter) { if (isMale) neM = "X"; else neF = "X"; }
            else { if (isMale) adM = "X"; else adF = "X"; }
        }

        String fullEms = cat.getEmsCode() != null ? cat.getEmsCode() : "";
        String emsBase = "";
        String emsSuffix = "";
        if (fullEms.contains(" ")) {
            int firstSpace = fullEms.indexOf(" ");
            emsBase = fullEms.substring(0, firstSpace);
            emsSuffix = fullEms.substring(firstSpace + 1);
        } else {
            emsBase = fullEms;
        }

        xPos = startX;
        String[] rowData = {
                String.valueOf(sheet.getCatalogNumber()),
                emsBase,
                emsSuffix,
                isMale ? "1,0" : "0,1",
                classCode,
                cat.getBirthDate() != null ? cat.getBirthDate() : "",
                adM, adF, neM, neF, c11M, c11F, c12M, c12F,
                sheet.getGrade() != null ? sheet.getGrade() : ""
        };

        for (int i = 0; i < rowData.length; i++) {
            String text = rowData[i];
            boolean isMark = (i >= 6 && i <= 13 && "X".equals(text));
            // Cerna barva pro celou tabulku (TEXT_DARK)
            cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
            cs.beginText();
            cs.setFont(i == 0 || isMark ? fontBold : font, 7);
            float xOffset = isMark ? xPos + (colWidths[i] / 2) - 3 : xPos + 4;
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
                cs.showText((cat.getTitleBefore() != null ? cat.getTitleBefore() : "") + " " + cat.getCatName() + " " + (cat.getTitleAfter() != null ? cat.getTitleAfter() : ""));
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
                drawLabelValue(cs, fontBold, fontRegular, "Otec:", cat.getFatherName(), pageMargin + 10, innerY);
                innerY -= 15;
                drawLabelValue(cs, fontBold, fontRegular, "Matka:", cat.getMotherName(), pageMargin + 10, innerY);
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

    private String getClassCode(ShowClass showClass) { return showClass != null ? showClass.getFifeCode() : ""; }

    private PDType0Font loadFont(PDDocument doc, String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new IOException("Font not found: " + path);
            return PDType0Font.load(doc, is);
        }
    }
}