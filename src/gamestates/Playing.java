package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.*;
import utilz.LoadSave;
import entities.NPC;

import java.util.ArrayList;

import ui.GameCompletedOverlay;
import audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder  = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage[] backgroundImgs;
    private int[] smallCloudPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean gameCompleted;
    private boolean playerDying;

    private ArrayList<NPC> npcs = new ArrayList<>();
    private NPC activeNPC = null;
    private DialogueOverlay dialogueOverlay;
    private boolean dialogueActive = false;
    private boolean allEnemiesCleared = false;

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImgs = new BufferedImage[]{
                LoadSave.GetSpriteAtlas("playing_bg_img1.png"),
                LoadSave.GetSpriteAtlas("playing_bg_img2.png")
        };

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        player.resetAll();
        game.getAudioPlayer().setLevelSong(levelManager.getLvlIndex());
        npcs.clear();
        initNPCs();
        xLvlOffset = 0;
        calcLvlOffset();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int)(64 * Game.SCALE), (int)(40 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);

        dialogueOverlay = new DialogueOverlay(this);
        initNPCs();
    }

    private void initNPCs() {
        for(Point p : levelManager.getCurrentLevel().getOldManSpawns()) {
            NPC npc = new NPC(p.x, p.y, "Old Man", new String[]{
                    "Welcome, traveler!",
                    "Beware of the enemies ahead.",
                    "Good luck on your journey!"
            });
            npc.loadLvlData(levelManager.getCurrentLevel().getLevelData());
            npcs.add(npc);
        }

        for(Point p : levelManager.getCurrentLevel().getMerchantSpawns()) {
            NPC npc = new NPC(p.x, p.y, "Merchant", new String[]{
                    "Welcome to my shop!",
                    "I have wares if you have coin."
            });
            npc.loadLvlData(levelManager.getCurrentLevel().getLevelData());
            npcs.add(npc);
        }
    }

    @Override
    public void update() {
        if(paused)
            pauseOverlay.update();
        else if(lvlCompleted)
            levelCompletedOverlay.update();
        else if(gameCompleted) {
            gameCompletedOverlay.update();
        }
        else if(gameOver)
            gameOverOverlay.update();
        else if(playerDying)
            player.update();
        else if(dialogueActive) {
            for(NPC npc : npcs)
                npc.update();
            dialogueOverlay.update(activeNPC);
        } else {
            levelManager.update();
            player.update();

            allEnemiesCleared = enemyManager.areAllEnemiesCleared();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            objectManager.updatePortals(allEnemiesCleared);
            objectManager.checkSpikesTouched(player);
            enemyManager.checkSpikesTouched(objectManager);

            checkCloseToBorder();
            for (NPC npc : npcs)
                npc.update();
        }

    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    private void checkCloseToBorder() {
        int playerX = (int)player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if(diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if(diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgs[levelManager.getLvlIndex()], 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        for(NPC npc : npcs)
            npc.draw(g, xLvlOffset);

        if(dialogueActive && activeNPC != null)
            dialogueOverlay.draw(g, activeNPC);

        if(paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if(gameOver)
            gameOverOverlay.draw(g);
        else if(lvlCompleted)
            levelCompletedOverlay.draw(g);
        else if(gameCompleted) {
            gameCompletedOverlay.draw(g);
        }
    }

    public void resetAll() {
        allEnemiesCleared = false;
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        gameCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
        dialogueActive = false;
        activeNPC = null;
        for(NPC npc : npcs)
            npc.endDialogue();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGameCompleted() {
        gameCompleted = true;
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver) {
            if(e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameOver)
            gameOverOverlay.mousePressed(e);
        else if(paused)
            pauseOverlay.mousePressed(e);
        else if(lvlCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if(gameCompleted)
            gameCompletedOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(gameOver)
            gameOverOverlay.mouseReleased(e);
        else if(paused)
            pauseOverlay.mouseReleased(e);
        else if(lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if(gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameOver) {
            gameOverOverlay.mouseMoved(e);
        }
        else if(paused) {
            pauseOverlay.mouseMoved(e);
        }
        else if(lvlCompleted) {
            levelCompletedOverlay.mouseMoved(e);
        }
        else if(gameCompleted) {
            gameCompletedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!gameOver && !gameCompleted && !lvlCompleted)
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_E:
                    if (objectManager.isPlayerAtOpenPortal(player.getHitbox()))
                        loadNextLevel();
                    else
                        handleNPCInteract();
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver && !gameCompleted && !lvlCompleted)
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }

    private void handleNPCInteract() {
        if(dialogueActive && activeNPC != null) {
            if(!dialogueOverlay.isTextComplete()) {
                dialogueOverlay.skipToEnd();
                return;
            }
            activeNPC.interact();
            if(!activeNPC.isDialogueActive()) {
                dialogueActive = false;
                activeNPC = null;
            }
        } else {
            for(NPC npc : npcs) {
                if(npc.isPlayerInRange(player.getHitbox())) {
                    activeNPC = npc;
                    activeNPC.interact();
                    dialogueActive = true;
                    break;
                }
            }
        }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        game.getAudioPlayer().lvlCompleted();
        if (levelManager.getLvlIndex() + 1 >= levelManager.getAmountOfLevels()) {
            levelManager.loadNextLevel(); // resets to 0 and goes to menu already
            resetAll();
            game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            return;
        }
        this.lvlCompleted = levelCompleted;
    }

    public void restartGame() {
        levelManager.resetLevelIndex();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        player.resetAll();
        resetAll();
        xLvlOffset = 0;
        calcLvlOffset();
        npcs.clear();
        initNPCs();
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() { return player; }
    public EnemyManager getEnemyManager() { return enemyManager; }
    public LevelManager getLevelManager() { return levelManager; }
    public ObjectManager getObjectManager() { return objectManager; }


}