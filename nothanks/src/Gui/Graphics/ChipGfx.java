package Gui.Graphics;

import com.sun.javafx.scene.text.TextLayout;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class ChipGfx {

    private static ChipGfx spriteReader;

    private BufferedImage spriteSheet;

    private String path = "../Images/chip.png";
    private Integer textHeightOffset = 325;
    private Integer textWidthOffsetOneDigit = 200;
    private Integer textWidthOffsetTwoDigit = 140;

    private ChipGfx() {
        // Load spritesheet
        try {
            URL imgUrl = getClass().getResource(this.path);
            spriteSheet = ImageIO.read(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChipGfx getInstance() {
        if (spriteReader == null) {
            return (spriteReader = new ChipGfx());
        } else {
            return spriteReader;
        }
    }

    /**
     * Returns a chip graphic
     *
     * @param chips Number of chips to draw
     * @return BufferedImage
     */
    public BufferedImage getChipGfx(Integer chips) {
        // Init chip image
        BufferedImage image = getSourceSprite();
        Graphics2D g2d = image.createGraphics();

        // Draw number of chips on image
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 200));
        g2d.setColor(new Color(61, 50, 76));
        if (chips < 10) g2d.drawString(chips.toString(), textWidthOffsetOneDigit, textHeightOffset);
        else g2d.drawString(chips.toString(), textWidthOffsetTwoDigit, textHeightOffset);
        g2d.dispose();

        return image;
    }

    private BufferedImage getSourceSprite() {
        ColorModel cm = spriteSheet.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = spriteSheet.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
