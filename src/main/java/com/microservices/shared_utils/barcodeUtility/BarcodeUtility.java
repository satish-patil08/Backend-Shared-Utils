package com.microservices.shared_utils.barcodeUtility;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BarcodeUtility {
    public static ResponseEntity<byte[]> generateBarCode(String text, Integer height, Integer width, String barcodeNote) {
        try {
            InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("Quicksand-SemiBold.ttf");
            if (fontStream == null)
                throw new IOException("Font file not found in resources");

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 18);

            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.CODE_128, width, height);

            BufferedImage barcodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    barcodeImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : 0x00FFFFFF);

            int barcodePaddingTop = 15;  // Top padding before barcode starts
            int textPadding = 10;  // Padding around the barcode text

            ArrayList<String> wrappedWarningLines = new ArrayList<>();
            int warningNoteHeight = 0;

            Font noteFont = new Font("Serif", Font.BOLD, 13);
            if (barcodeNote != null && !barcodeNote.isEmpty()) {
                InputStream noteFontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("Quicksand-SemiBold.ttf");
                if (noteFontStream != null)
                    noteFont = Font.createFont(Font.TRUETYPE_FONT, noteFontStream).deriveFont(Font.PLAIN, 13);

                wrappedWarningLines = wrapText(barcodeNote, width, noteFont);
                warningNoteHeight = wrappedWarningLines.size() * (noteFont.getSize() + 2);
            }

            BufferedImage combinedImage = new BufferedImage(width, height + textPadding + barcodePaddingTop + 20 + warningNoteHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = combinedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(barcodeImage, 0, barcodePaddingTop, null);
            g2d.setFont(customFont);
            g2d.setColor(Color.BLACK);

            FontMetrics fontMetrics = g2d.getFontMetrics();
            int textYPosition = height + barcodePaddingTop + fontMetrics.getAscent();
            g2d.drawString(text, (width - fontMetrics.stringWidth(text)) / 2, textYPosition);

            int warningPaddingTop = 0;  // Adjustable padding between barcode value and warning note
            if (!wrappedWarningLines.isEmpty()) {
                int lineYPosition = textYPosition + warningPaddingTop;
                g2d.setFont(noteFont);
                for (String line : wrappedWarningLines) {
                    lineYPosition += g2d.getFontMetrics().getHeight();
                    g2d.drawString(line, (width - g2d.getFontMetrics().stringWidth(line)) / 2, lineYPosition);
                }
            }

            g2d.dispose();

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            ImageIO.write(combinedImage, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(pngData);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private static ArrayList<String> wrapText(String text, int maxWidth, Font font) {
        ArrayList<String> lines = new ArrayList<>();
        FontMetrics metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontMetrics(font);

        StringBuilder currentLine = new StringBuilder();
        for (String word : text.split(" ")) {
            if (metrics.stringWidth(currentLine + word) > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word).append(" ");
            } else {
                currentLine.append(word).append(" ");
            }
        }
        if (!currentLine.isEmpty())
            lines.add(currentLine.toString());
        return lines;
    }
}
