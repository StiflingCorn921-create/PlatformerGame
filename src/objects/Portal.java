package objects;

import main.Game;
import static utilz.Constants.ObjectConstants.*;

public class Portal extends GameObject {
    private boolean unlocked = false;

    public Portal(int x, int y) {
        super(x, y, PORTAL);
        // hitbox is the interaction zone, slightly larger than visual
        initHitbox(16, 32);
        // center the hitbox on the tile
        hitbox.x -= (int)(8 * Game.SCALE);
    }

    public void update(boolean allEnemiesCleared) {
        if (allEnemiesCleared && !unlocked) {
            unlocked = true;
            doAnimation = true;
        }
        if (doAnimation)
            updateAnimationTick();
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= utilz.Constants.ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(PORTAL))
                aniIndex = 0;
        }
    }

    public boolean isUnlocked() { return unlocked; }

    // Areas with no enemies (village, merchant) portal starts open
    public void forceUnlock() {
        unlocked = true;
        doAnimation = true;
    }
}