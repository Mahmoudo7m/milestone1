package game.engine.cells;
import game.engine.Constants;
import game.engine.monsters.Monster;
import game.engine.interfaces.CanisterModifier;

public class ContaminationSock extends TransportCell implements CanisterModifier {

	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}
	public void onLand(Monster landingMonster, Monster opponentMonster) {
	    super.onLand(landingMonster, opponentMonster);
	    transport(landingMonster);
	    landingMonster.alterEnergy(-Constants.SLIP_PENALTY);
	}

	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
	    monster.alterEnergy(canisterValue);
	}
	

}

