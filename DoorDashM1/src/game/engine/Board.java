package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.*;

public class Board {
    private Cell[][] boardCells;
    private static ArrayList<Monster> stationedMonsters;
    private static ArrayList<Card> originalCards;
    public static ArrayList<Card> cards;

    public Board(ArrayList<Card> readCards) {
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        stationedMonsters = new ArrayList<Monster>();
        originalCards = readCards;
        cards = new ArrayList<Card>();
        setCardsByRarity();
        reloadCards();
    }

    public Cell[][] getBoardCells() {
        return boardCells;
    }

    public static ArrayList<Monster> getStationedMonsters() {
        return stationedMonsters;
    }

    public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
        Board.stationedMonsters = stationedMonsters;
    }

    public static ArrayList<Card> getOriginalCards() {
        return originalCards;
    }

    public static ArrayList<Card> getCards() {
        return cards;
    }

    public static void setCards(ArrayList<Card> cards) {
        Board.cards = cards;
    }

    private int[] indexToRowCol(int index) {
        int row = index / Constants.BOARD_COLS;
        int col = index % Constants.BOARD_COLS;
        if (row % 2 != 0) {
            col = (Constants.BOARD_COLS - 1) - col;
        }
        return new int[]{row, col};
    }

    private Cell getCell(int index) {
        int[] pos = indexToRowCol(index);
        return boardCells[pos[0]][pos[1]];
    }

    private void setCell(int index, Cell cell) {
        int[] pos = indexToRowCol(index);
        boardCells[pos[0]][pos[1]] = cell;
    }

    public void initializeBoard(ArrayList<Cell> specialCells) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (i % 2 == 0) {
                setCell(i, new Cell("Rest Cell"));
            } else {
                setCell(i, new DoorCell("Standard Door", Role.SCARER, 100));
            }
        }

        int doorIndex = 1;
        int conveyorIndex = 0;
        int sockIndex = 0;

        for (Cell cell : specialCells) {
            if (cell instanceof DoorCell) {
                setCell(doorIndex, cell);
                doorIndex += 2;
            } else if (cell instanceof ConveyorBelt) {
                setCell(Constants.CONVEYOR_CELL_INDICES[conveyorIndex], cell);
                conveyorIndex++;
            } else if (cell instanceof ContaminationSock) {
                setCell(Constants.SOCK_CELL_INDICES[sockIndex], cell);
                sockIndex++;
            }
        }

        for (int idx : Constants.CARD_CELL_INDICES) {
            setCell(idx, new CardCell("Card Cell"));
        }

        for (int i = 0; i < Constants.MONSTER_CELL_INDICES.length; i++) {
            int idx = Constants.MONSTER_CELL_INDICES[i];
            if (i < stationedMonsters.size()) {
                Monster m = stationedMonsters.get(i);
                m.setPosition(idx);
                setCell(idx, new MonsterCell("Monster Cell", m));
            }
        }
    }

    private void setCardsByRarity() {
        ArrayList<Card> expanded = new ArrayList<>();
        for (Card c : originalCards) {
            for (int i = 0; i < c.getRarity(); i++) {
                expanded.add(c);
            }
        }
        originalCards = expanded;
    }

    public static void reloadCards() {
        cards = new ArrayList<>(originalCards);
        Collections.shuffle(cards);
    }

    public static Card drawCard() {
        if (cards.isEmpty()) {
            reloadCards();
        }
        return cards.remove(0);
    }

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int oldPosition = currentMonster.getPosition();

        currentMonster.move(roll);

        if (currentMonster.getPosition() == opponentMonster.getPosition()) {
            currentMonster.setPosition(oldPosition);
            throw new InvalidMoveException("Destination occupied by opponent!");
        }

        getCell(currentMonster.getPosition()).onLand(currentMonster, opponentMonster);

        if (currentMonster.isConfused()) {
            currentMonster.decrementConfusion();
        }
        if (opponentMonster.isConfused()) {
            opponentMonster.decrementConfusion();
        }

        updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            getCell(i).setMonster(null);
        }
        getCell(player.getPosition()).setMonster(player);
        getCell(opponent.getPosition()).setMonster(opponent);
    }
}