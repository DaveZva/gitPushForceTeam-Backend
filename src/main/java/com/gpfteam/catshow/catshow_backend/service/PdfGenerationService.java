package com.gpfteam.catshow.catshow_backend.service;

import com.gpfteam.catshow.catshow_backend.model.Breeder;
import com.gpfteam.catshow.catshow_backend.model.Cat;
import com.gpfteam.catshow.catshow_backend.model.Owner;
import com.gpfteam.catshow.catshow_backend.model.Registration;
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
import java.util.Objects;

@Service
public class PdfGenerationService {

    // Barvy
    private static final float[] PRIMARY_COLOR = {0.2f, 0.4f, 0.7f}; // Modrá
    private static final float[] ACCENT_COLOR = {0.95f, 0.6f, 0.2f}; // Oranžová
    private static final float[] LIGHT_BG = {0.97f, 0.97f, 0.98f}; // Světle šedá
    private static final float[] BORDER_COLOR = {0.85f, 0.85f, 0.87f}; // Světle šedá bordura
    private static final float[] TEXT_DARK = {0.2f, 0.2f, 0.2f}; // Tmavě šedá
    private static final float[] TEXT_LIGHT = {0.5f, 0.5f, 0.5f}; // Světle šedá text

    private PDType0Font fontRegular;
    private PDType0Font fontBold;
    private PDPageContentStream contentStream;
    private PDDocument document;
    private float yPosition;
    private final float pageTop = 750;
    private final float pageMargin = 50;
    private final float pageWidth = 595 - 2 * pageMargin;
    private final float pageHeight = 842;

    public byte[] generateRegistrationPdf(Registration reg) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            this.document = doc;
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            loadFonts(document);
            contentStream = new PDPageContentStream(document, page);
            yPosition = pageTop;

            // Hlavička s barevným páskem
            drawHeader();

            // Hlavní nadpis
            addMainTitle("Potvrzení o přihlášení");
            addSubtitle("Výstava koček");
            yPosition -= 15;

            // Info box s číslem registrace
            drawInfoBox(reg);
            yPosition -= 20;

            // Sekce s detaily
            addModernSection("Informace o registraci", PRIMARY_COLOR);
            addInfoRow("Číslo přihlášky", reg.getRegistrationNumber(), true);
            addInfoRow("Výstava", reg.getShow().getName(), false);
            addInfoRow("Datum odeslání", reg.getCreatedAt().format(DateTimeFormatter.ofPattern("d. M. yyyy HH:mm")), false);
            addInfoRow("Stav", translateStatus(reg.getStatus().name()), false);
            addInfoRow("Dny účasti", reg.getDays().toUpperCase(), false);
            yPosition -= 25;

            // Majitel
            addModernSection("Majitel", PRIMARY_COLOR);
            Owner owner = reg.getOwner();
            addInfoRow("Jméno a příjmení", owner.getFirstName() + " " + owner.getLastName(), true);
            addInfoRow("Adresa", owner.getAddress() + ", " + owner.getZip() + " " + owner.getCity(), false);
            addInfoRow("E-mail", owner.getEmail(), false);
            addInfoRow("Telefon", owner.getPhone(), false);

            String org = owner.getOwnerLocalOrganization() != null && !owner.getOwnerLocalOrganization().isEmpty()
                    ? owner.getOwnerLocalOrganization() : "—";
            String member = owner.getOwnerMembershipNumber() != null && !owner.getOwnerMembershipNumber().isEmpty()
                    ? owner.getOwnerMembershipNumber() : "—";
            addInfoRow("Organizace", org, false);
            addInfoRow("Členské číslo", member, false);
            yPosition -= 25;

            // Chovatel (pokud se liší)
            Breeder breeder = reg.getBreeder();
            if (breeder != null && !Objects.equals(breeder.getEmail(), owner.getEmail())) {
                addModernSection("Chovatel", PRIMARY_COLOR);
                addInfoRow("Jméno a příjmení", breeder.getFirstName() + " " + breeder.getLastName(), true);
                addInfoRow("E-mail", breeder.getEmail(), false);
                yPosition -= 25;
            }

            // Kočky
            addModernSection("Registrované kočky (" + reg.getCats().size() + ")", ACCENT_COLOR);
            yPosition -= 5;

            int catCount = 0;
            for (Cat cat : reg.getCats()) {
                if (catCount > 0) {
                    yPosition -= 10;
                }
                drawCatCard(cat, catCount + 1);
                catCount++;
            }

            // Zápatí
            drawFooter();

            contentStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void loadFonts(PDDocument document) throws IOException {
        try (InputStream fontStreamRegular = getClass().getResourceAsStream("/fonts/DejaVuSans.ttf");
             InputStream fontStreamBold = getClass().getResourceAsStream("/fonts/DejaVuSans-Bold.ttf")) {

            if (fontStreamRegular == null || fontStreamBold == null) {
                throw new IOException("Soubory s fonty (DejaVuSans.ttf, DejaVuSans-Bold.ttf) nebyly nalezeny v resources/fonts/");
            }
            fontRegular = PDType0Font.load(document, fontStreamRegular);
            fontBold = PDType0Font.load(document, fontStreamBold);
        }
    }

    private void drawHeader() throws IOException {
        // Horní barevný pás
        contentStream.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
        contentStream.addRect(0, pageHeight - 60, 595, 60);
        contentStream.fill();

        // Logo text / název aplikace
        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.beginText();
        contentStream.setFont(fontBold, 24);
        contentStream.newLineAtOffset(pageMargin, pageHeight - 40);
        contentStream.showText("CatShow");
        contentStream.endText();

        // Dekorativní akcent
        contentStream.setNonStrokingColor(ACCENT_COLOR[0], ACCENT_COLOR[1], ACCENT_COLOR[2]);
        contentStream.addRect(pageMargin, pageHeight - 48, 80, 3);
        contentStream.fill();

        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        yPosition = pageTop - 40;
    }

    private void addMainTitle(String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(fontBold, 22);
        contentStream.newLineAtOffset(pageMargin, yPosition);
        contentStream.showText(text);
        contentStream.endText();
        yPosition -= 28;
    }

    private void addSubtitle(String text) throws IOException {
        contentStream.setNonStrokingColor(TEXT_LIGHT[0], TEXT_LIGHT[1], TEXT_LIGHT[2]);
        contentStream.beginText();
        contentStream.setFont(fontRegular, 12);
        contentStream.newLineAtOffset(pageMargin, yPosition);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        yPosition -= 20;
    }

    private void drawInfoBox(Registration reg) throws IOException {
        float boxHeight = 50;
        float boxY = yPosition - boxHeight;

        // Pozadí boxu
        contentStream.setNonStrokingColor(LIGHT_BG[0], LIGHT_BG[1], LIGHT_BG[2]);
        contentStream.addRect(pageMargin, boxY, pageWidth, boxHeight);
        contentStream.fill();

        // Bordura
        contentStream.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
        contentStream.setLineWidth(1f);
        contentStream.addRect(pageMargin, boxY, pageWidth, boxHeight);
        contentStream.stroke();

        // Levá strana - ikona a číslo
        contentStream.setNonStrokingColor(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
        contentStream.addRect(pageMargin, boxY, 4, boxHeight);
        contentStream.fill();

        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        contentStream.beginText();
        contentStream.setFont(fontBold, 11);
        contentStream.newLineAtOffset(pageMargin + 15, boxY + 30);
        contentStream.showText("Registrační číslo");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(fontBold, 16);
        contentStream.newLineAtOffset(pageMargin + 15, boxY + 12);
        contentStream.showText(reg.getRegistrationNumber());
        contentStream.endText();

        // Pravá strana - datum
        String dateStr = reg.getCreatedAt().format(DateTimeFormatter.ofPattern("d. M. yyyy"));
        contentStream.setNonStrokingColor(TEXT_LIGHT[0], TEXT_LIGHT[1], TEXT_LIGHT[2]);
        contentStream.beginText();
        contentStream.setFont(fontRegular, 10);
        float dateWidth = (fontRegular.getStringWidth(dateStr) / 1000f) * 10;
        contentStream.newLineAtOffset(pageMargin + pageWidth - dateWidth - 15, boxY + 25);
        contentStream.showText(dateStr);
        contentStream.endText();

        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        yPosition = boxY - 10;
    }

    private void addModernSection(String title, float[] color) throws IOException {
        // Barevná bordura vlevo
        contentStream.setNonStrokingColor(color[0], color[1], color[2]);
        contentStream.addRect(pageMargin, yPosition - 18, 3, 20);
        contentStream.fill();

        // Nadpis sekce
        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        contentStream.beginText();
        contentStream.setFont(fontBold, 14);
        contentStream.newLineAtOffset(pageMargin + 10, yPosition);
        contentStream.showText(title);
        contentStream.endText();
        yPosition -= 25;
    }

    private void addInfoRow(String label, String value, boolean isFirst) throws IOException {
        if (!isFirst) {
            yPosition -= 14;
        }

        // Label
        contentStream.setNonStrokingColor(TEXT_LIGHT[0], TEXT_LIGHT[1], TEXT_LIGHT[2]);
        contentStream.beginText();
        contentStream.setFont(fontRegular, 9);
        contentStream.newLineAtOffset(pageMargin + 10, yPosition);
        contentStream.showText(label.toUpperCase());
        contentStream.endText();

        // Value
        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        contentStream.beginText();
        contentStream.setFont(fontRegular, 11);
        contentStream.newLineAtOffset(pageMargin + 10, yPosition - 12);
        contentStream.showText(Objects.requireNonNullElse(value, "—"));
        contentStream.endText();

        yPosition -= 12;
    }

    private void drawCatCard(Cat cat, int number) throws IOException {
        float cardHeight = 140;
        float cardY = yPosition - cardHeight;

        // Kontrola prostoru na stránce
        if (cardY < 100) {
            // Nedostatek místa, přidáme novou stránku
            contentStream.close();
            PDPage newPage = new PDPage(PDRectangle.A4);
            document.addPage(newPage);
            contentStream = new PDPageContentStream(document, newPage);
            yPosition = pageTop;
            cardY = yPosition - cardHeight;
        }

        // Pozadí karty
        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.addRect(pageMargin, cardY, pageWidth, cardHeight);
        contentStream.fill();

        // Bordura
        contentStream.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
        contentStream.setLineWidth(1f);
        contentStream.addRect(pageMargin, cardY, pageWidth, cardHeight);
        contentStream.stroke();

        // Číslo kočky
        contentStream.setNonStrokingColor(ACCENT_COLOR[0], ACCENT_COLOR[1], ACCENT_COLOR[2]);
        contentStream.beginText();
        contentStream.setFont(fontBold, 11);
        contentStream.newLineAtOffset(pageMargin + 10, cardY + cardHeight - 18);
        contentStream.showText("KOČKA #" + number);
        contentStream.endText();

        // Jméno kočky
        String catName = (cat.getTitleBefore() != null && !cat.getTitleBefore().isEmpty() ? cat.getTitleBefore() + " " : "") +
                cat.getCatName() +
                (cat.getTitleAfter() != null && !cat.getTitleAfter().isEmpty() ? " " + cat.getTitleAfter() : "");

        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        contentStream.beginText();
        contentStream.setFont(fontBold, 14);
        contentStream.newLineAtOffset(pageMargin + 10, cardY + cardHeight - 36);
        contentStream.showText(catName.trim());
        contentStream.endText();

        // Tenká oddělovací čára
        contentStream.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(pageMargin + 10, cardY + cardHeight - 45);
        contentStream.lineTo(pageMargin + pageWidth - 10, cardY + cardHeight - 45);
        contentStream.stroke();

        float detailY = cardY + cardHeight - 65;
        float col1X = pageMargin + 10;
        float col2X = pageMargin + pageWidth / 2 + 5;

        // Levý sloupec
        addCardDetail("EMS kód", cat.getEmsCode(), col1X, detailY);
        addCardDetail("Datum narození", cat.getBirthDate(), col1X, detailY - 25);
        addCardDetail("Třída", translateClass(cat.getShowClass().name()), col1X, detailY - 50);
        addCardDetail("Matka", cat.getMotherName() != null && !cat.getMotherName().isEmpty() ? cat.getMotherName() : "—", col1X, detailY - 75);

        // Pravý sloupec
        addCardDetail("Pohlaví", translateGender(cat.getGender().name()), col2X, detailY);
        addCardDetail("Kastrovaná", translateNeutered(cat.getNeutered().name()), col2X, detailY - 25);
        addCardDetail("Typ klece", translateCageType(cat.getCageType().name()), col2X, detailY - 50);
        addCardDetail("Otec", cat.getFatherName() != null && !cat.getFatherName().isEmpty() ? cat.getFatherName() : "—", col2X, detailY - 75);

        yPosition = cardY - 15;
    }

    private void addCardDetail(String label, String value, float x, float y) throws IOException {
        contentStream.setNonStrokingColor(TEXT_LIGHT[0], TEXT_LIGHT[1], TEXT_LIGHT[2]);
        contentStream.beginText();
        contentStream.setFont(fontRegular, 8);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label.toUpperCase());
        contentStream.endText();

        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
        contentStream.beginText();
        contentStream.setFont(fontRegular, 10);
        contentStream.newLineAtOffset(x, y - 12);
        contentStream.showText(value);
        contentStream.endText();
    }

    private void drawFooter() throws IOException {
        float footerY = 40;

        contentStream.setStrokingColor(BORDER_COLOR[0], BORDER_COLOR[1], BORDER_COLOR[2]);
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(pageMargin, footerY + 20);
        contentStream.lineTo(pageMargin + pageWidth, footerY + 20);
        contentStream.stroke();

        contentStream.setNonStrokingColor(TEXT_LIGHT[0], TEXT_LIGHT[1], TEXT_LIGHT[2]);
        String footerText = "Dokument vygenerován automaticky systémem CatShow";
        float textWidth = (fontRegular.getStringWidth(footerText) / 1000f) * 9;
        contentStream.beginText();
        contentStream.setFont(fontRegular, 9);
        contentStream.newLineAtOffset((595 - textWidth) / 2, footerY);
        contentStream.showText(footerText);
        contentStream.endText();

        contentStream.setNonStrokingColor(TEXT_DARK[0], TEXT_DARK[1], TEXT_DARK[2]);
    }

    private String translateStatus(String status) {
        return switch (status) {
            case "PENDING" -> "Čeká na zpracování";
            case "CONFIRMED" -> "Potvrzeno";
            case "CANCELLED" -> "Zrušeno";
            default -> status;
        };
    }

    private String translateGender(String gender) {
        return switch (gender) {
            case "MALE" -> "Kocour";
            case "FEMALE" -> "Kočka";
            default -> gender;
        };
    }

    private String translateNeutered(String neutered) {
        return switch (neutered) {
            case "YES" -> "Ano";
            case "NO" -> "Ne";
            default -> neutered;
        };
    }

    private String translateClass(String showClass) {
        // Přidejte podle vašich skutečných tříd
        return showClass;
    }

    private String translateCageType(String cageType) {
        return switch (cageType) {
            case "SINGLE" -> "Jednoduchá";
            case "DOUBLE" -> "Dvojitá";
            default -> cageType;
        };
    }
}