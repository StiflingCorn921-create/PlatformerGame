package levels;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private BufferedImage[][] levelSprites;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more levels! Game Completed!");
            Gamestate.state = Gamestate.MENU;
            return;
        }

        Level newLevel = levels.get(lvlIndex);
        newLevel.resetEnemies(); // fresh enemy objects
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    public void resetLevelIndex() {
        lvlIndex = 0;
        Level newLevel = levels.get(lvlIndex);
        newLevel.resetEnemies(); // fresh enemy objects here too
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevels){
            levels.add(new Level(img));
        }
    }

    private void importOutsideSprites() {
        String[] atlases = {LoadSave.LEVEL_ATLAS, LoadSave.LEVEL_ATLAS_2};
        levelSprites = new BufferedImage[atlases.length][48];

        for(int k = 0; k < atlases.length; k++){
            BufferedImage img = LoadSave.GetSpriteAtlas(atlases[k]);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 12; j++){
                    int index = i * 12 + j;
                    levelSprites[k][index] = img.getSubimage(j * 32, i * 32, 32, 32);
                }
            }
        }
    }

    public void draw(Graphics g, int lvlOffset){
        for(int i = 0; i < Game.TILES_IN_HEIGHT; i++){
            for(int j = 0; j < levels.get(lvlIndex).getLevelData()[0].length; j++){
                int index = levels.get(lvlIndex).getSpriteIndex(j, i);
                g.drawImage(levelSprites[lvlIndex][index], Game.TILES_SIZE * j - lvlOffset, Game.TILES_SIZE * i, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update(){

    }

    public void setLevelIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }

    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }

    public int getLvlIndex(){
        return lvlIndex;
    }

}
