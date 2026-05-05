package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites1.png";
    public static final String LEVEL_ATLAS_2 = "outside_sprites2.png";
    public static final String LEVEL_ATLAS_3 = "outside_sprites3.png";

    public static final String MENU_BUTTONS = "button_atlas.png";
//    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";

    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";

    public static final String COMPLETED_IMG = "completed_sprite.png";

    public static final String PLAYING_BG_IMG = "playing_bg_img1.png";
//    public static final String BIG_CLOUDS = "big_clouds.png";
//    public static final String SMALL_CLOUDS = "small_clouds.png";

    public static final String CRABBY_SPRITE = "crabby_sprite.png";

    public static final String ZOMBIE_WALK   = "sprites/Zombie/Zombie_Walk.png";
    public static final String ZOMBIE_ATTACK = "sprites/Zombie/Zombie_Attack.png";
    public static final String ZOMBIE_HIT    = "sprites/Zombie/Zombie_Hit.png";

    public static final String GAME_COMPLETED = "game_completed.png";
    public static final String OPTIONS_MENU = "options_background.png";
    public static final String DEATH_SCREEN = "death_screen.png";

    public static final String ZOMBIE_DEATH  = "sprites/Zombie/Zombie_Death.png";

    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String PORTAL_ATLAS = "portal.png";

    public static final String STATUS_BAR = "health_power_bar.png";

    public static final String CHARACTER_SELECT_BG = "playing_bg_img1.png";

    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                is.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return img;
    }

    public static BufferedImage GetBrawlerSprite(String action){
        String path = "/sprites/Brawler/" + action + "_Brawler.png";
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(path);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/lvls");
        if(url == null) return new BufferedImage[0];

        File folder = null;
        try {
            folder = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new BufferedImage[0];
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if(files == null || files.length == 0) return new BufferedImage[0];

        List<File> levelFiles = new ArrayList<>();
        for(File f : files){
            if(f.getName().matches("\\d+\\.png")){
                levelFiles.add(f);
            }
        }

        levelFiles.sort(Comparator.comparingInt(f -> Integer.parseInt(f.getName().replace(".png", ""))));

        BufferedImage[] imgs = new BufferedImage[levelFiles.size()];
        for(int i = 0; i < levelFiles.size(); i++){
            try {
                imgs[i] = ImageIO.read(levelFiles.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }




}
