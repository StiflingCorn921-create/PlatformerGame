package entities;

import main.Game;
import java.awt.geom.Rectangle2D;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

public class Zombie extends Enemy {

    private int attackBoxOffsetX;

    public Zombie(float x, float y) {
        super(x, y, ZOMBIE_WIDTH, ZOMBIE_HEIGHT, ZOMBIE);
        initHitbox(20, 28);
        initAttackBox(30, 30, (int)(Game.SCALE * 10));
        walkSpeed = Game.SCALE * 0.2f; // faster than crabby
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    protected void updateAttackBox() {
        if(walkDir == RIGHT)
            attackBox.x = hitbox.x + hitbox.width;
        else
            attackBox.x = hitbox.x - attackBox.width;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if(firstUpdate)
            firstUpdateCheck(lvlData);

        if(inAir) {
            updateInAir(lvlData);
        } else {
            switch(state) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if(isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex == 0)
                        attackChecked = false;
                    if(aniIndex == 2 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }


    public int getWalkDir() { return walkDir; }
}