package game.engine.monsters;

import game.engine.Role;
import game.engine.Constants;
import game.engine.Board;
public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	public void setEnergy(int energy) {
	    int change = energy - getEnergy();
	    if (change != 0) {
	        super.setEnergy(getEnergy() + change + Constants.SCHEMER_STEAL);
	    } else {
	        super.setEnergy(energy);
	    }
	}

	private int stealEnergyFrom(Monster target) {
	    int stolen = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
	    target.setEnergy(target.getEnergy() - stolen);
	    return stolen;
	}

	public void executePowerupEffect(Monster opponentMonster) {
	    int total = stealEnergyFrom(opponentMonster);
	    for (Monster m : Board.getStationedMonsters()) {
	        total += stealEnergyFrom(m);
	    }
	    this.setEnergy(this.getEnergy() + total);
	}
	
}
