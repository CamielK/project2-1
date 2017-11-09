package Library.AI;

public interface AIInterface {

    /**
     * Returns true if the player should pick a card and returns false if the player tosses a chip
     * @return boolean Pick card (true) or toss chip (false)
     */
    public boolean GetMove();
}
