package Library.AI.Gametree;

public class NothanksTree {

    private NothanksTree picked;    // game tree after picking the current card
    private NothanksTree tossed;    // game tree after tossing the current card
    private int value;              // Represents the value at this node

    public NothanksTree getPicked() {
        return picked;
    }

    public void setPicked(NothanksTree picked) {
        this.picked = picked;
    }

    public NothanksTree getTossed() {
        return tossed;
    }

    public void setTossed(NothanksTree tossed) {
        this.tossed = tossed;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
