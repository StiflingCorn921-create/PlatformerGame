package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.UP;
import static utilz.HelpMethods.CanMoveHere;

import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed = 0f;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;
    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;
    protected float currentPushSpeed;

    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }



    protected void applyKnockback(int direction, float force, float speed) {
        this.pushBackDir = direction;
        this.currentPushSpeed = speed;
        this.pushDrawOffset = 0;
        this.pushBackOffsetDir = UP;
        this.inAir = true;
        this.airSpeed = force;
    }

    protected void updateKnockback(int[][] lvlData) {
        // horizontal slide
        float xSpeed = (pushBackDir == LEFT) ? -currentPushSpeed : currentPushSpeed;
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;

        // vertical arc
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            if (airSpeed > 0) {
                inAir = false;
                airSpeed = 0;
            } else {
                airSpeed = 0.5f * Game.SCALE;
            }
        }
        updatePushBackDrawOffset();
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        // For debugging hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE),(int)(height * Game.SCALE));
    }

    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if(pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if(pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        } else {
            pushDrawOffset += speed;
            if(pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if(pushBackDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if(CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }

    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }


//    protected void updateHitbox(){
//        hitbox.x = (int)x;
//        hitbox.y = (int)y;
//    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getState(){
        return state;
    }

    public int getAniIndex(){
        return aniIndex;
    }

    public int getCurrentHealth() { return currentHealth; }
}

