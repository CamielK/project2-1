package Helper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CardSpriteReader {

    private static CardSpriteReader spriteReader;

    private BufferedImage spriteSheet;

    private String path = "../Gui/Images/cards.png";
    private Integer heightOffset = 17;
    private Integer widthOffset = 17;
    private Integer cardHeight = 254;
    private Integer cardWidth = 182;

    private CardSpriteReader() {
        // Load spritesheet
        try {
            URL imgUrl = getClass().getResource(this.path);
            spriteSheet = ImageIO.read(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CardSpriteReader getInstance() {
        if (spriteReader == null) {
            return (spriteReader = new CardSpriteReader());
        } else {
            return spriteReader;
        }
    }

    /**
     * Returns a transparently padded image containing the card sprite
     *
     * @param cardNum Number of card to be returned
     * @return BufferedImage
     */
    public BufferedImage getFlippedCard(Integer cardNum) {
        // Init empty transparent image
        BufferedImage image = new BufferedImage(cardWidth+widthOffset+cardWidth, cardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw sprite on image
        g2d.drawImage(getSourceSprite(cardNum), cardWidth+widthOffset, 0, cardWidth, cardHeight, null);
        g2d.dispose();

        return image;
    }

    /**
     * Finds and returns the given card number from the spritesheet
     *
     * @param cardNum Number of card to be returend (3 >= cardNum <= 35)
     * @return BufferedImage
     */
    private BufferedImage getSourceSprite(Integer cardNum) {
        if (cardNum < 3 || cardNum > 35) return null;

        // Find sprite coordinates
        int spriteX = widthOffset;
        if (cardNum%10 != 0) spriteX = (cardNum%10) * (widthOffset+cardWidth+widthOffset) - cardWidth-widthOffset;
        else if (cardNum%10 == 0) spriteX = 1961;

        int spriteY = heightOffset;
        if (cardNum > 10 && cardNum <= 20) spriteY = heightOffset*3 + cardHeight;
        else if (cardNum > 20 && cardNum <= 30) spriteY = heightOffset*5 + cardHeight*2;
        else if (cardNum > 30) spriteY = heightOffset*7 + cardHeight*3;

        // Cut sprite
        BufferedImage image = null;
        image = spriteSheet.getSubimage(spriteX, spriteY, cardWidth, cardHeight);

        return image;
    }

}
