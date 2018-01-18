package Library.AI.RandomAI;

import Library.AI.AIInterface;
import Library.Player;

import java.util.ArrayList;

public class RandomAI implements AIInterface {

    @Override
    public boolean GetMove() {
   	//return true;
   	return Math.random() < 0.5;
    }

	@Override
	public void gameIsFinished(ArrayList<Player> winner) {
		// TODO Auto-generated method stub
		
	}

}
