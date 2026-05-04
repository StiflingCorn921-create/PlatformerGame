package levels;

import entities.Crabby;
import entities.Zombie;
import main.Game;
import objects.Spike;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.SPIKE;
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


    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calculateLvlOffsets();
        calcPlayerSpawn();
        oldManSpawns   = GetNPCSpawns(img, 255);
        merchantSpawns = GetNPCSpawns(img, 200);
        zombies = GetZombies(img);
    }

    private void loadObjects(int blueValue, int x, int y){
        switch(blueValue){
            case SPIKE:
                spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
                break;
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
}
