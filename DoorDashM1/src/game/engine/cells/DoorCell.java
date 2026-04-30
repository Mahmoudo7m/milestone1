package game.engine.cells;

import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.Board;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
    private Role role;
    private int energy;
    private boolean activated;

    public DoorCell(String name, Role role, int energy) {
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }

    public Role getRole() {
        return role;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean isActivated) {
        this.activated = isActivated;
    }

    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        if (activated) return;
        boolean roleMatch = landingMonster.getRole() == this.role;
        int energyChange = roleMatch ? this.energy : -this.energy;
        boolean shieldBlocked = !roleMatch && landingMonster.isShielded();
        landingMonster.alterEnergy(energyChange);
        if (!shieldBlocked) {
            for (Monster m : Board.getStationedMonsters()) {
                if (m.getRole() == landingMonster.getRole()) {
                    m.alterEnergy(energyChange);
                }
            }
            this.activated = true;
        }
    }

    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        int effective = (monster.getRole() == this.role) ? canisterValue : -canisterValue;
        monster.alterEnergy(effective);
    }
}