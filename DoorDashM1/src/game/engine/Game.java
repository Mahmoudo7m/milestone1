package game.engine;
import game.engine.exceptions.InvalidMoveException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
import game.engine.monsters.*;
import game.engine.Constants;
import game.engine.exceptions.OutOfEnergyException;
public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	

	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	private Monster getCurrentOpponent() {
	    return current == player ? opponent : player;
	}
	private int rollDice() {
	    return (int)(Math.random() * 6) + 1;
	}
	public void usePowerup() throws OutOfEnergyException {
	    if (current.getEnergy() < Constants.POWERUP_COST) {
	        throw new OutOfEnergyException();
	    }
	    current.setEnergy(current.getEnergy() - Constants.POWERUP_COST);
	    current.executePowerupEffect(getCurrentOpponent());
	}
	private void switchTurn() {
	    current = getCurrentOpponent();
	}
	public void playTurn() throws InvalidMoveException {
	    if (current.isFrozen()) {
	        current.setFrozen(false);
	        switchTurn();
	        return;
	    }
	    int roll = rollDice();
	    board.moveMonster(current, roll, getCurrentOpponent());
	    switchTurn();
	}
	private boolean checkWinCondition(Monster monster) {
	    return monster.getPosition() == Constants.WINNING_POSITION 
	        && monster.getEnergy() >= Constants.WINNING_ENERGY;
	}
	public Monster getWinner() {
	    if (checkWinCondition(player)) {
	        return player;
	    }
	    if (checkWinCondition(opponent)) {
	        return opponent;
	    }
	    return null;
	}
	public Game(Role playerRole) throws IOException {
	    this.board = new Board(DataLoader.readCards());
	    this.allMonsters = DataLoader.readMonsters();
	    this.player = selectRandomMonsterByRole(playerRole);
	    this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
	    this.current = player;
	    
	    ArrayList<Monster> stationedMonsters = new ArrayList<>(allMonsters);
	    stationedMonsters.remove(player);
	    stationedMonsters.remove(opponent);
	    board.setStationedMonsters(stationedMonsters);
	    board.initializeBoard(DataLoader.readCells());
	}
}