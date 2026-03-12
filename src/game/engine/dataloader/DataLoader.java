package game.engine.dataloader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import game.engine.cards.*;
import game.engine.monsters.*;
import game.engine.cells.*;
import game.engine.Role;
public class DataLoader {
	public static final String CARDS_FILE_NAME = "cards.csv";
    public static final String CELLS_FILE_NAME = "cells.csv";
    public static final String MONSTERS_FILE_NAME = "monsters.csv";
    public static ArrayList<Card> readCards() throws IOException {

        ArrayList<Card> cards = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE_NAME));

        String line;

        while((line = br.readLine()) != null){

            String[] data = line.split(",");

            String type = data[0];
            String name = data[1];
            String description = data[2];
            int rarity = Integer.parseInt(data[3]);

            if(type.equals("SWAPPER")){
                cards.add(new SwapperCard(name, description, rarity));
            }

            else if(type.equals("SHIELD")){
                cards.add(new ShieldCard(name, description, rarity));
            }

            else if(type.equals("ENERGYSTEAL")){
                int energy = Integer.parseInt(data[4]);
                cards.add(new EnergyStealCard(name, description, rarity, energy));
            }

            else if(type.equals("STARTOVER")){
                boolean lucky = Boolean.parseBoolean(data[4]);
                cards.add(new StartOverCard(name, description, rarity, lucky));
            }

            else if(type.equals("CONFUSION")){
                int duration = Integer.parseInt(data[4]);
                cards.add(new ConfusionCard(name, description, rarity, duration));
            }
        }

        br.close();

        return cards;
    }
    public static ArrayList<Monster> readMonsters() throws IOException {

        ArrayList<Monster> monsters = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE_NAME));

        String line;

        while((line = br.readLine()) != null){

            String[] data = line.split(",");

            String type = data[0];
            String name = data[1];
            String description = data[2];
            Role role = Role.valueOf(data[3]);
            int energy = Integer.parseInt(data[4]);

            if(type.equals("DASHER")){
                monsters.add(new Dasher(name, description, role, energy));
            }

            else if(type.equals("DYNAMO")){
                monsters.add(new Dynamo(name, description, role, energy));
            }

            else if(type.equals("SCHEMER")){
                monsters.add(new Schemer(name, description, role, energy));
            }

            else if(type.equals("MULTITASKER")){
                monsters.add(new MultiTasker(name, description, role, energy));
            }
        }

        br.close();

        return monsters;
    }
    public static ArrayList<Cell> readCells() throws IOException { 
        ArrayList<Cell> cells = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(CELLS_FILE_NAME));

        String line;

        while((line = br.readLine()) != null){

            String[] data = line.split(",");

            // Door cells (3 columns)
            if(data.length == 3){

                String name = data[0];
                Role role = Role.valueOf(data[1]);
                int energy = Integer.parseInt(data[2]);

                cells.add(new DoorCell(name, role, energy));
            }

            // Transport cells (2 columns)
            else if(data.length == 2){

                String name = data[0];
                int effect = Integer.parseInt(data[1]);

                // Use the concrete subclass instead of TransportCell
                if(name.equalsIgnoreCase("ConveyorBelt")) {
                    cells.add(new ConveyorBelt(name, effect));
                } else if(name.equalsIgnoreCase("ContaminationSock")) {
                    cells.add(new ContaminationSock(name, effect));
                }
            }
        }

        br.close();

        return cells;
    }
    

}
