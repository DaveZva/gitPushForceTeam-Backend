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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PdfGenerationService {

    private static final float[] PRIMARY_COLOR = {0.008f, 0.482f, 1.0f};
    private static final float[] HEADER_BG = {0.94f, 0.94f, 0.94f};
    private static final float[] TEXT_DARK = {0.12f, 0.12f, 0.12f};
    private static final float[] TEXT_LIGHT = {0.4f, 0.4f, 0.4f};
    private static final float[] BORDER_COLOR = {0.9f, 0.9f, 0.9f};

    private final float pageMargin = 40;

    public byte[] createJudgingSheetsPdf(List<JudgingSheet> sheets) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDType0Font fontRegular = loadFont(document, "/fonts/DejaVuSans.ttf");
            PDType0Font fontBold = loadFont(document, "/fonts/DejaVuSans-Bold.ttf");

            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            float totalPageWidth = 842 - 2 * pageMargin;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float y = 530;

                if (!sheets.isEmpty()) {
                    Show show = sheets.get(0).getShow();
                    cs.beginText();
                    cs.setFont(fontBold, 24);
                    cs.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
                    cs.newLineAtOffset(pageMargin, y);
                    cs.showText("Nominations - " + show.getName());
                    cs.endText();

                    cs.beginText();
                    cs.setFont(fontRegular, 10);
                    cs.setNonStrokingColor(TEXT_LIGHT[0], TEXT_LIGHT[1], TEXT_LIGHT[2]);
                    cs.newLineAtOffset(pageMargin, y - 15);
                    cs.showText("FP – Poznań — " + sheets.get(0).getDay());
                    cs.endText();
                    y -= 60;
                }

                float classColWidth = 150;
                List<Judge> judges = sheets.stream()
                        .map(JudgingSheet::getJudge)
                        .distinct()
                        .collect(Collectors.toList());

                float judgeColWidth = (totalPageWidth - classColWidth) / Math.max(1, judges.size());
                float rowHeight = 45;

                drawRect(cs, HEADER_BG, pageMargin, y, classColWidth, rowHeight);
                drawText(cs, fontBold, 11, TEXT_DARK, pageMargin + 10, y + (rowHeight / 2) - 4, "Class");

                float currentX = pageMargin + classColWidth;
                for (Judge judge : judges) {
                    drawRect(cs, PRIMARY_COLOR, currentX, y, judgeColWidth, rowHeight);
                    String judgeDisplayName = judge.getLastName();
                    drawCenteredText(cs, fontBold, 10, new float[]{1, 1, 1}, currentX, y + (rowHeight / 2) - 4, judgeColWidth, judgeDisplayName);
                    currentX += judgeColWidth;
                }

                y -= rowHeight;

                var sheetsByClass = sheets.stream()
                        .collect(Collectors.groupingBy(s -> s.getCatEntry().getShowClass()));

                for (var entryClass : sheetsByClass.entrySet()) {
                    drawRect(cs, new float[]{0.98f, 0.98f, 0.98f}, pageMargin, y, classColWidth, rowHeight);
                    drawText(cs, fontBold, 10, TEXT_DARK, pageMargin + 10, y + (rowHeight / 2) - 4, String.valueOf(entryClass.getKey()));

                    currentX = pageMargin + classColWidth;
                    for (Judge judge : judges) {
                        cs.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
                        cs.addRect(currentX, y, judgeColWidth, rowHeight);
                        cs.stroke();

                        JudgingSheet cellSheet = entryClass.getValue().stream()
                                .filter(s -> s.getJudge().getId().equals(judge.getId()))
                                .findFirst().orElse(null);

                        if (cellSheet != null) {
                            drawCenteredText(cs, fontBold, 12, TEXT_DARK, currentX, y + (rowHeight / 2) + 2, judgeColWidth, String.valueOf(cellSheet.getCatalogNumber()));
                            drawCenteredText(cs, fontRegular, 8, TEXT_LIGHT, currentX, y + (rowHeight / 2) - 10, judgeColWidth, cellSheet.getCatEntry().getCat().getEmsCode());
                        } else {
                            drawCenteredText(cs, fontRegular, 10, TEXT_LIGHT, currentX, y + (rowHeight / 2) - 4, judgeColWidth, "–");
                        }
                        currentX += judgeColWidth;
                    }
                    y -= rowHeight;
                    if (y < 50) break;
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawRect(PDPageContentStream cs, float[] color, float x, float y, float w, float h) throws IOException {
        cs.setNonStrokingColor(color[0], color[1], color[2]);
        cs.addRect(x, y, w, h);
        cs.fill();
    }

    private void drawText(PDPageContentStream cs, PDType0Font font, float size, float[] color, float x, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(font, size);
        cs.setNonStrokingColor(color[0], color[1], color[2]);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    private void drawCenteredText(PDPageContentStream cs, PDType0Font font, float size, float[] color, float x, float y, float width, String text) throws IOException {
        float stringWidth = font.getStringWidth(text) * size / 1000f;
        float startX = x + (width - stringWidth) / 2;
        drawText(cs, font, size, color, startX, y, text);
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

    private String translateGender(String gender) {
        return switch (gender) {
            case "MALE" -> "Kocour / Male";
            case "FEMALE" -> "Kočka / Female";
            default -> gender;
        };
    }

    private String translateStatus(String status) {
        return switch (status) {
            case "PENDING" -> "Čeká na zpracování";
            case "CONFIRMED" -> "Potvrzeno";
            case "CANCELLED" -> "Zrušeno";
            default -> status;
        };
    }
}