package entities;

import main.Game;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;

public class NPC extends Entity {
    private String name;
    private String[] dialogueLines;
    private int dialogueIndex = 0;
    private boolean dialogueActive = false;
    private float interactRange = Game.TILES_SIZE * 2;
    private int[][] lvlData;
    private float fallSpeed = 0;
    private float gravity = 0.04f * Game.SCALE;
    private boolean inAir = true;

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public NPC(float x, float y, String name, String[] dialogueLines) {
        super(x, y, (int)(32 * Game.SCALE), (int)(32 * Game.SCALE));
        this.name = name;
        this.dialogueLines = dialogueLines;
        initHitbox((int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update() {
        if(lvlData == null) return;
        if(inAir) {
            if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed,
                    hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
                fallSpeed = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        // Placeholder: draw a colored rectangle as NPC
        g.setColor(Color.ORANGE);
        g.fillRect(
                (int)(hitbox.x - xLvlOffset),
                (int)(hitbox.y),
                (int)(hitbox.width),
                (int)(hitbox.height)
        );
        // Draw name above NPC
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, (int)(8 * Game.SCALE)));
        g.drawString(name, (int)(hitbox.x - xLvlOffset), (int)(hitbox.y - 5));
    }

    public boolean isPlayerInRange(Rectangle2D.Float playerHitbox) {
        float dist = Math.abs(playerHitbox.x - hitbox.x);
        return dist <= interactRange;
    }

    public void interact() {
        if (!dialogueActive) {
            dialogueActive = true;
            dialogueIndex = 0;
        } else {
            dialogueIndex++;
            if (dialogueIndex >= dialogueLines.length) {
                dialogueActive = false;
                dialogueIndex = 0;
            }
        }
    }

    public void endDialogue() {
        dialogueActive = false;
        dialogueIndex = 0;
    }

    public boolean isTextFinished(String displayedText) {
        return displayedText.equals(getCurrentLine());
    }

    public boolean isDialogueActive() { return dialogueActive; }
    public String getCurrentLine() { return dialogueLines[dialogueIndex]; }
    public String getName() { return name; }
}