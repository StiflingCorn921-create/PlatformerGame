package entities;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();
    private BufferedImage[][] zombieArr;
    private ArrayList<Zombie> zombies = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
        zombies  = level.getZombies();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;

        for(Crabby c : crabbies)
            if(c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }

        for(Zombie z : zombies)
            if(z.isActive()) {
                z.update(lvlData, player);
                isAnyActive = true;
            }

        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawZombies(g, xLvlOffset);
    }

    private void drawZombies(Graphics g, int xLvlOffset) {
        for(Zombie z : zombies) {
            if(z.isActive()) {
                int state = z.getState();
                int frame = z.getAniIndex();
                if(zombieArr[state] != null && frame < zombieArr[state].length) {
                    BufferedImage currentFrame = zombieArr[state][frame];

                    int drawWidth  = ZOMBIE_WIDTH;
                    int drawHeight = ZOMBIE_HEIGHT;
                    int flipX = z.getWalkDir() == LEFT ? drawWidth : 0;
                    int flipW = z.getWalkDir() == LEFT ? -1 : 1;

                    g.drawImage(currentFrame,
                            (int)z.getHitbox().x - xLvlOffset - ZOMBIE_DRAWOFFSET_X + flipX,
                            (int)z.getHitbox().y - ZOMBIE_DRAWOFFSET_Y,
                            drawWidth * flipW,
                            drawHeight,
                            null);
                }
                z.drawHitbox(g, xLvlOffset);
                z.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for(Crabby c : crabbies) {
            if(c.isActive()) {
                int state = c.getState();
                int frame = c.getAniIndex();
                // bounds check before drawing
                if(frame < crabbyArr[state].length) {
                    g.drawImage(crabbyArr[state][frame],
                            (int)c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
                            (int)c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
                            CRABBY_WIDTH * c.flipW(),
                            CRABBY_HEIGHT,
                            null);
                }
            }
        }
    }

    //                c.drawHitbox(g, xLvlOffset);
//                c.drawAttackBox(g, xLvlOffset);

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for(Crabby c : crabbies)
            if(c.isActive() && c.getState() != DEAD && c.getState() != HIT)
                if(attackBox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
                }

        for(Zombie z : zombies)
            if(z.isActive() && z.getState() != DEAD && z.getState() != HIT)
                if(attackBox.intersects(z.getHitbox())) {
                    z.hurt(10);
                    return;
                }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);

        for(int j = 0; j < crabbyArr.length; j++){
            for(int i = 0; i < crabbyArr[j].length; i++){
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }

        zombieArr = new BufferedImage[5][];
        zombieArr[IDLE]   = loadZombieAction(LoadSave.ZOMBIE_WALK,   6,  56);
        zombieArr[RUNNING]= loadZombieAction(LoadSave.ZOMBIE_WALK,   6,  56);
        zombieArr[ATTACK] = loadZombieAction(LoadSave.ZOMBIE_ATTACK, 5,  56);
        zombieArr[HIT]    = loadZombieAction(LoadSave.ZOMBIE_HIT,    3,  56);
        zombieArr[DEAD]   = loadZombieAction(LoadSave.ZOMBIE_DEATH,  11, 56);
    }

    private BufferedImage[] loadZombieAction(String path, int frameCount, int height) {
        BufferedImage sheet = LoadSave.GetSpriteAtlas(path);
        if(sheet == null) {
            System.out.println("Failed to load: " + path);
            return new BufferedImage[frameCount];
        }
        int frameWidth = sheet.getWidth() / frameCount;
        BufferedImage[] frames = new BufferedImage[frameCount];
        for(int i = 0; i < frameCount; i++)
            frames[i] = sheet.getSubimage(i * frameWidth, 0, frameWidth, height);
        return frames;
    }

    public void resetAllEnemies() {
        for(Crabby c : crabbies){
            c.resetEnemy();
        }
        for(Zombie z : zombies){
            z.resetEnemy();
        }
    }
}
