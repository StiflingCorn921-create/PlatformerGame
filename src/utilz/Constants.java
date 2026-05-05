package utilz;

import main.Game;

public class Constants {
    public static final float GRAVITY = 0.04f * Game.SCALE;
    public static final int ANI_SPEED = 25;

    public static class EnemyConstants {
        public static final int CRABBY = 22;
        public static final int ZOMBIE = 1;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int)(CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int)(CRABBY_HEIGHT_DEFAULT * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_X = (int)(26 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int)(9 * Game.SCALE);

        public static final int ZOMBIE_WIDTH_DEFAULT = 40;
        public static final int ZOMBIE_HEIGHT_DEFAULT = 40;
        public static final int ZOMBIE_WIDTH = (int)(ZOMBIE_WIDTH_DEFAULT * Game.SCALE);
        public static final int ZOMBIE_HEIGHT = (int)(ZOMBIE_HEIGHT_DEFAULT * Game.SCALE);
        public static final int ZOMBIE_DRAWOFFSET_X = (int)(5 * Game.SCALE);
        public static final int ZOMBIE_DRAWOFFSET_Y = (int)(8 * Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch(enemy_state) {
                case IDLE:
                case RUNNING:
                    if(enemy_type == ZOMBIE) return 6;
                    return 9;
                case ATTACK:
                    if(enemy_type == ZOMBIE) return 5;
                    return 7;
                case HIT:
                    if(enemy_type == ZOMBIE) return 3;
                    return 4;
                case DEAD:
                    if(enemy_type == ZOMBIE) return 11;
                    return 5;
            }
            return 0;
        }

        public static int GetMaxHealth(int enemy_type) {
            switch(enemy_type) {
                case CRABBY: return 20;
                case ZOMBIE: return 30;
                default: return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch(enemy_type) {
                case CRABBY: return 10;
                case ZOMBIE: return 15;
                default: return 0;
            }
        }
    }

    public static class ObjectConstants {
        public static final int SPIKE = 4;
        public static final int PORTAL = 100;

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int PORTAL_WIDTH_DEFAULT  = 32;
        public static final int PORTAL_HEIGHT_DEFAULT = 32;
        public static final int PORTAL_WIDTH  = (int)(PORTAL_WIDTH_DEFAULT  * Game.SCALE);
        public static final int PORTAL_HEIGHT = (int)(PORTAL_HEIGHT_DEFAULT * Game.SCALE);
        public static final int PORTAL_DRAWOFFSET_X = 0;
        public static final int PORTAL_DRAWOFFSET_Y = 0;

        public static int GetSpriteAmount(int objType) {
            return switch (objType) {
                case SPIKE  -> 1;
                case PORTAL -> 6;
                default -> 1;
            };
        }
    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;

        public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int)(BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int)(SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;

        public static int GetSpriteAmount(int player_action) {
            switch(player_action) {
                case IDLE:    return 14;
                case RUNNING: return 8;
                case JUMP:    return 7;
                case ATTACK:  return 3;
                case FALLING:
                default:      return 1;
            }
        }
    }

}