package levels;

import entities.Crabby;
import entities.Zombie;
import main.Game;
import objects.Portal;
import objects.Spike;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.PORTAL;
import static utilz.Constants.ObjectConstants.SPIKE;
import static utilz.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Zombie> zombies;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;
    private ArrayList<Point> oldManSpawns;
    private ArrayList<Point> merchantSpawns;
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<Portal> portals = new ArrayList<>();

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createObjects();
        calculateLvlOffsets();
        calcPlayerSpawn();
        oldManSpawns   = GetNPCSpawns(img, 255);
        merchantSpawns = GetNPCSpawns(img, 200);
        zombies = GetZombies(img);
    }

    private void createObjects() {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == SPIKE)
                    spikes.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
                else if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == PORTAL)
                    portals.add(new Portal(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calculateLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    public void resetEnemies() {
        crabs  = GetCrabs(img);
        zombies = GetZombies(img);
        spikes = new ArrayList<>();
        portals = new ArrayList<>();
        createObjects();
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLevelData(){
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }

    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }

    public Point getPlayerSpawn(){
        return playerSpawn;
    }

    public ArrayList<Point> getOldManSpawns()   { return oldManSpawns; }

    public ArrayList<Point> getMerchantSpawns() { return merchantSpawns; }

    public ArrayList<Zombie> getZombies() { return zombies; }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Portal> getPortals() { return portals; }
}