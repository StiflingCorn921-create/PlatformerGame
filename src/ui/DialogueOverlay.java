package ui;

import entities.NPC;
import gamestates.Playing;
import main.Game;
import java.awt.*;

public class DialogueOverlay {
    private Playing playing; // we'll fix this import below

    // Box dimensions
    private int boxX = (int)(20 * Game.SCALE);
    private int boxY = (int)(Game.GAME_HEIGHT - 100 * Game.SCALE);
    private int boxW = (int)(Game.GAME_WIDTH - 40 * Game.SCALE);
    private int boxH = (int)(80 * Game.SCALE);

    // Portrait placeholder dimensions
    private int portraitX = (int)(25 * Game.SCALE);
    private int portraitY = (int)(Game.GAME_HEIGHT - 95 * Game.SCALE);
    private int portraitSize = (int)(70 * Game.SCALE);

    // Text start position (after portrait)
    private int textX = (int)(110 * Game.SCALE);
    private int textY = (int)(Game.GAME_HEIGHT - 65 * Game.SCALE);

    private String displayedText = "";
    private int charIndex = 0;
    private int textTick = 0;
    private int textSpeed = 3; // lower = faster, higher = slower
    private String fullText = "";

    public DialogueOverlay(gamestates.Playing playing) {
        this.playing = playing;
    }

    public void update(NPC npc) {
        if (npc == null || !npc.isDialogueActive()) {
            return;
        }

        // If new line started, reset
        if (!fullText.equals(npc.getCurrentLine())) {
            fullText = npc.getCurrentLine();
            displayedText = "";
            charIndex = 0;
            textTick = 0;
        }

        // Reveal one character at a time
        if (charIndex < fullText.length()) {
            textTick++;
            if (textTick >= textSpeed) {
                textTick = 0;
                charIndex++;
                displayedText = fullText.substring(0, charIndex);
            }
        }
    }

    public void draw(Graphics g, NPC npc) {
        if (npc == null || !npc.isDialogueActive()) {
            return;
        }

        // Semi transparent background box
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        // Box border
        g.setColor(Color.WHITE);
        g.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        // Placeholder portrait box
        g.setColor(new Color(80, 80, 80, 220));
        g.fillRect(portraitX, portraitY, portraitSize, portraitSize);
        g.setColor(Color.WHITE);
        g.drawRect(portraitX, portraitY, portraitSize, portraitSize);

        // NPC name
        g.setFont(new Font("Arial", Font.BOLD, (int)(9 * Game.SCALE)));
        g.setColor(Color.YELLOW);
        g.drawString(npc.getName(), textX, textY - (int)(15 * Game.SCALE));

        // Dialogue text
        g.setFont(new Font("Arial", Font.PLAIN, (int)(7 * Game.SCALE)));
        g.setColor(Color.WHITE);
        g.drawString(displayedText, textX, textY);

        // Press E to continue hint
        g.setFont(new Font("Arial", Font.ITALIC, (int)(6 * Game.SCALE)));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("[E] Continue", boxX + boxW - (int)(60 * Game.SCALE),
                boxY + boxH - (int)(5 * Game.SCALE));
    }

    public boolean isTextComplete() {
        return charIndex >= fullText.length();
    }

    public void skipToEnd() {
        charIndex = fullText.length();
        displayedText = fullText;
    }
}