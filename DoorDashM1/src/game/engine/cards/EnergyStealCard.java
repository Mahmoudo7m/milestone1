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
        boolean opponentShielded = opponent.isShielded();
        if (opponentShielded) {
            opponent.alterEnergy(-this.getEnergy());
        } else {
            int stolen = Math.min(this.getEnergy(), opponent.getEnergy());
            modifyCanisterEnergy(opponent, -stolen);
            modifyCanisterEnergy(player, stolen);
        }
    }

    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }
}