package game.engine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import game.engine.dataloader.DataLoader;
import game.engine.monsters.Monster;
import game.engine.Role;
import game.engine.cards.Card;
public class Game {
	private final Board board;
    private final ArrayList<Monster> allMonsters;
    private final Monster player;
    private final Monster opponent;
    private Monster current;
    public Game(Role playerRole) throws IOException {
        
        board = new Board(DataLoader.readCards());

        
        allMonsters = DataLoader.readMonsters();

        
        player = selectRandomMonsterByRole(playerRole);

        
        Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
        opponent = selectRandomMonsterByRole(opponentRole);

        
        current = player;
    }
    private Monster selectRandomMonsterByRole(Role role) {
        ArrayList<Monster> candidates = new ArrayList<>();
        for (Monster m : allMonsters) {
            if (m.getRole() == role) {
                candidates.add(m);
            }
        }

        if (candidates.isEmpty()) {
            return null; 
        }

        Random rand = new Random();
        return candidates.get(rand.nextInt(candidates.size()));
    }
    public Monster getCurrent() {
        return current;
    }

    public void setCurrent(Monster current) {
        this.current = current;
    }
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


}
