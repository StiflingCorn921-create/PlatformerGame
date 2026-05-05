package utilz;

import entities.Crabby;
import entities.Zombie;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRABBY;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData){
        if(!IsSolid(x,y,lvlData)){
            if(!IsSolid(x+width, y+height, lvlData)){
                if(!IsSolid(x + width, y, lvlData)){
                    if(!IsSolid(x, y + height, lvlData)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData){
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;

        if(x < 0 || x >= maxWidth){
            return true;
        }
        if(y < 0 || y >= Game.GAME_HEIGHT){
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int)xIndex, (int)yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
        int value = lvlData[yTile][xTile];

        if(value >= 48 || value < 0 || value != 11){ //hitbox if condition conifgure if different
            return true;
        }else{
            return false;
        }
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);

        if(xSpeed > 0){
            //right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);

            return tileXPos + xOffset - 1;
        }else{
            //left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);

        if(airSpeed > 0){
            //falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);

            return tileYPos + yOffset - 1;
        }else{
            //jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        //check pixel below bottomleft and bottomright
        if(!IsSolid(hitbox.x, hitbox.y+ hitbox.height + 1, lvlData)){
            if(!IsSolid(hitbox.x + hitbox.width, hitbox.y+ hitbox.height + 1, lvlData)){
                return false;
            }
        }
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0){
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }else {
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i = 0; i < xEnd - xStart; i++){
            if(IsTileSolid(xStart + i, y, lvlData)){
                return false;
            }

            if(!IsTileSolid(xStart + i, y + 1, lvlData)){
                return false;
            }
        }

        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
        int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

        int secondXTile;
        if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
            secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
        else
            secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsSightClear_OLD(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        }
        else {
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for(int i = 0; i < img.getHeight(); i++) {
            for(int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));

                // Skip NPC pixels (R=0, G=0, B=200 or 255)
                if(color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() >= 200) {
                    lvlData[i][j] = 11;
                    continue;
                }
                // Skip spike pixels (R=0, G=0, B=4)
                if(color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 4) {
                    lvlData[i][j] = 11;
                    continue;
                }
                // Skip portal pixels (R=0, G=0, B=100)
                if(color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 100) {
                    lvlData[i][j] = 11;
                    continue;
                }
                // Skip crabby pixels (R=0, G=CRABBY, B=0)
                if(color.getRed() == 0 && color.getGreen() == CRABBY && color.getBlue() == 0) {
                    lvlData[i][j] = 11;
                    continue;
                }
                // Skip zombie pixels (R=0, G=1, B=0)
                if(color.getRed() == 0 && color.getGreen() == 1 && color.getBlue() == 0) {
                    lvlData[i][j] = 11;
                    continue;
                }
                // Skip player spawn pixel (G=100)
                if(color.getRed() == 0 && color.getGreen() == 100 && color.getBlue() == 0) {
                    lvlData[i][j] = 11;
                    continue;
                }

                int value = color.getRed();
                if(value >= 48) value = 0;
                lvlData[i][j] = value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        for(int i = 0; i < img.getHeight(); i++)
            for(int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                if(color.getRed() == 0 && color.getGreen() == CRABBY && color.getBlue() == 0)
                    list.add(new Crabby(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<Zombie> GetZombies(BufferedImage img) {
        ArrayList<Zombie> list = new ArrayList<>();
        for(int i = 0; i < img.getHeight(); i++)
            for(int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                // use green value 1 for zombie spawn
                if(color.getGreen() == 1 && color.getRed() == 0 && color.getBlue() == 0)
                    list.add(new Zombie(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<Point> GetNPCSpawns(BufferedImage img, int npcType) {
        ArrayList<Point> spawns = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                // Only count it if RED and GREEN are also exactly 0
                if (color.getBlue() == npcType &&
                        color.getRed() == 0 &&
                        color.getGreen() == 0)
                    spawns.add(new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return spawns;
    }

    public static Point GetPlayerSpawn(BufferedImage img){
        for(int i = 0; i < img.getHeight(); i++){
            for(int j = 0; j < img.getWidth(); j++){
                Color color = new Color(img.getRGB(j, i));
                int value =  color.getGreen();
                if(value == 100){
                    return new Point(j * Game.TILES_SIZE, i * Game.TILES_SIZE);
                }
            }
        }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }


}
