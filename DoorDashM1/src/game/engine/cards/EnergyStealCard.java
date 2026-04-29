package game.engine.cards;
import game.engine.monsters.Monster;
import game.engine.interfaces.CanisterModifier;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	public void performAction(Monster player, Monster opponent) {
	    int stolen = Math.min(this.getEnergy(), opponent.getEnergy());
	    opponent.alterEnergy(-stolen);
	    player.alterEnergy(stolen);
	    modifyCanisterEnergy(player, stolen);
	    modifyCanisterEnergy(opponent, -stolen);
	}

	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
	    monster.alterEnergy(canisterValue);
	}
}
