package Library.AI.RandomAI;

import java.util.ArrayList;

import Library.Player;
import Library.AI.AIInterface;

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
