package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage spikeImg;
    private Level currentLevel;
    private BufferedImage[] portalImgs;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImgs();
    }

    public void checkSpikesTouched(Player p) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    public void checkSpikesTouched(Enemy e) {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(e.getHitbox())) {
                e.hurt(e.getCurrentHealth()); // instant kill
                return;
            }
    }

    public boolean isSpikeAhead(Rectangle2D.Float hitbox, int walkDir) {
        float lookX = (walkDir == utilz.Constants.Directions.LEFT)
                ? hitbox.x - Game.TILES_SIZE
                : hitbox.x + hitbox.width + Game.TILES_SIZE;

        for (Spike s : currentLevel.getSpikes()) {
            if (s.getHitbox().x <= lookX + Game.TILES_SIZE &&
                    s.getHitbox().x + s.getHitbox().width >= lookX &&
                    Math.abs(s.getHitbox().y - (hitbox.y + hitbox.height)) < Game.TILES_SIZE)
                return true;
        }
        return false;
    }

    public void loadObjects(Level newLevel) {
        currentLevel = newLevel;
    }

    private void loadImgs() {
        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
        BufferedImage portalSheet = LoadSave.GetSpriteAtlas(LoadSave.PORTAL_ATLAS);
        if (portalSheet != null) {
            portalImgs = new BufferedImage[6];
            int frameW = portalSheet.getWidth() / 3;  // 3 columns
            int frameH = portalSheet.getHeight() / 2; // 2 rows
            int idx = 0;
            for (int row = 0; row < 2; row++)
                for (int col = 0; col < 3; col++)
                    portalImgs[idx++] = portalSheet.getSubimage(
                            col * frameW, row * frameH, frameW, frameH);
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawTraps(g, xLvlOffset);
        drawPortals(g, xLvlOffset);
    }

    public void updatePortals(boolean allEnemiesCleared) {
        for (Portal p : currentLevel.getPortals())
            p.update(allEnemiesCleared);
    }

    public boolean isPlayerAtOpenPortal(Rectangle2D.Float playerHitbox) {
        for (Portal p : currentLevel.getPortals())
            if (p.isUnlocked() && p.getHitbox().intersects(playerHitbox))
                return true;
        return false;
    }

    private void drawPortals(Graphics g, int xLvlOffset) {
        for (Portal p : currentLevel.getPortals()) {
            if (portalImgs == null) return;
            int frame = p.isUnlocked() ? p.getAniIndex() : 0; // frame 0 = closed
            g.drawImage(portalImgs[frame],
                    (int)(p.getHitbox().x - xLvlOffset) - PORTAL_DRAWOFFSET_X,
                    (int)(p.getHitbox().y)               - PORTAL_DRAWOFFSET_Y,
                    PORTAL_WIDTH, PORTAL_HEIGHT, null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes())
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    public void unlockPortalsForEmptyLevel() {
        for (Portal p : currentLevel.getPortals())
            p.forceUnlock();
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
    }
}