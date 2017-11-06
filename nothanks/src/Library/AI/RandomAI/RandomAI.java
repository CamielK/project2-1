package Library.AI.RandomAI;

import Library.AI.AIInterface;

public class RandomAI implements AIInterface {

    @Override
    public boolean GetMove() {
        return Math.random() < 0.5;
    }

}
