package Gui.Graphics;

import Library.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class CardGfx {

    private static CardGfx spriteReader;

    private BufferedImage spriteSheet;

    private String path = "../Images/cards.png";
    private Integer heightOffset = 17;
    private Integer widthOffset = 17;
    private Integer cardHeight = 254;
    private Integer cardWidth = 182;

    private CardGfx() {
        // Load spritesheet
        try {
            URL imgUrl = getClass().getResource(this.path);
            spriteSheet = ImageIO.read(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CardGfx getInstance() {
        if (spriteReader == null) {
            return (spriteReader = new CardGfx());
        } else {
            return spriteReader;
        }
    }

    /**
     * Returns an image of all the cards in the given arraylist
     *
     * @param cards Sorted list of cards owned by player
     * @return BufferedImage
     */
    public BufferedImage renderPlayerDeck(ArrayList<Card> cards, Integer chips) {
        if (cards.size() == 0 && chips == null) return null;

        // Find stacks of concurrent cards
        int maxHeight = 1;
        ArrayList<ArrayList> stacks = new ArrayList<ArrayList>();
        ArrayList<Integer> stack = new ArrayList<Integer>();
        for (int x = 0; x < cards.size(); x++) {
            if (stack.size() == 0) {
                stack.add(cards.get(x).getNumber());
            } else if (cards.get(x).getNumber() - stack.get(stack.size()-1) > 1) {
                stacks.add(stack);
                stack = new ArrayList<>();
                stack.add(cards.get(x).getNumber());
            } else {
                stack.add(cards.get(x).getNumber());
            }
            if (stack.size() > maxHeight) maxHeight = stack.size();
        }
        if (stack.size() != 0) stacks.add(stack);

        // Init image
        int deckWidth = stacks.size() * (cardWidth+widthOffset);
        int deckHeight = cardHeight + maxHeight*(heightOffset*2);
        BufferedImage chipImg = null;
        if (chips != null) {
            chipImg = ChipGfx.getInstance().getChipGfx(chips);
            deckWidth += chipImg.getWidth()/3;
        }
        BufferedImage image = new BufferedImage(deckWidth, deckHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw stacks on player deck
        for (int x = 0; x < stacks.size(); x++) {
            ArrayList<Integer> substack = stacks.get(x);
            Collections.reverse(substack);
            for (int y = 0; y < substack.size(); y++) {
                g2d.drawImage(getSourceSprite(substack.get(y)), x*(widthOffset+cardWidth), y*(heightOffset*2), null);
            }
        }

        // draw optional chips
        if (chips != null) {
            g2d.drawImage(chipImg, deckWidth-(chipImg.getWidth()/3), 0, chipImg.getWidth()/3, chipImg.getHeight()/3, null);
        }

        g2d.dispose();
        return image;
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
