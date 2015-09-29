package com.logicanvas.games.boardgames.ludo.config;

/**
 * Created by amansoor on 19-06-2015.
 */
public class GameConfiguration {
    public static final float FULL_HEIGHT = 1.5f;
    public static final boolean DEBUG = true;

    public static final int[][] quadIndex = {{1,2,3,4},{4,1,2,3},{3,4,1,2},{2,3,4,1}};

//    public static final int PLAYER_TYPE_OFF = 0;
//    public static final int PLAYER_TYPE_AI_LEVEL1 = 1;
//    public static final int PLAYER_TYPE_HUMAN = 2;

    public enum GAME_STATE {START, WAITING_FOR_SPIN, SPIN_DIE, WAITING_FOR_INPUT, INPUT_RECD, PLAY_MOVE, PLAY_END, GAME_END};

    public static final String[] playerNames = {"Blue", "Red", "Green", "Yellow"};
//    public static final String[] moveTypeNames = {"Idle", "Open", "Move", "Move&Hit", "Reset", "Enter", "Home"};

    public static final int GAME_SOUNDS_TOKEN_MOVE = 0;
    public static final int GAME_SOUNDS_PLAYER_TOUCH = 1;
    public static final int GAME_SOUNDS_DICE_ROLL = 2;
    public static final int GAME_SOUNDS_INVALID_PICK = 3;

    public static final int NO_OF_PLAYERS = 4;
    public static final int NO_OF_TOKENS_PER_PLAYER = 4;
    public static final int TOKEN_SIZE = 75;

    public static final boolean REVERSE = false;
    public static final boolean FORWARD = true;

    public static final int TOKEN_STATE_UNOPEN = 1;
    public static final int TOKEN_STATE_OPEN = 2;
    public static final int TOKEN_STATE_IN_HOME_ROW = 3;
    public static final int TOKEN_STATE_HOME = 4;

    public static final int BLUE_PLAYER_ID = 0;
    public static final int RED_PLAYER_ID = 1;
    public static final int GREEN_PLAYER_ID = 2;
    public static final int YELLOW_PLAYER_ID = 3;

    public static final int MAX_TOKEN_MOVE = 57;

    public static final int BLUE_START_INDEX = 77;
    public static final int BLUE_OPEN_INDEX = 2;
    public static final int BLUE_HOME_ROW_START_INDEX = 52;

    public static final int RED_START_INDEX = 81;
    public static final int RED_OPEN_INDEX = 15;
    public static final int RED_HOME_ROW_START_INDEX = 59;

    public static final int YELLOW_START_INDEX = 85;
    public static final int YELLOW_OPEN_INDEX = 41;
    public static final int YELLOW_HOME_ROW_START_INDEX = 71;

    public static final int GREEN_START_INDEX = 89;
    public static final int GREEN_OPEN_INDEX = 28;
    public static final int GREEN_HOME_ROW_START_INDEX = 65;

    public static final int GAME_CYCLE_END_INDEX = 51;

    public static final int[] SAFE_SPOT_LIST = {2, 15, 28, 41};
    public static final int[] HOME_ROW_CHECK_INDEX_LIST = {0, 13, 26, 39};

    public static final int POINTS_IDLE_TOKEN = -50000;
    public static final int POINTS_TOKEN_HOME = 10;
    public static final int POINTS_OPEN_TOKEN = 10;
    public static final int POINTS_CAN_ENTER_HOME_ROW = 100000;
    public static final int POINTS_CAN_LAND_ON_SAFE_SPOT = 500;
    public static final int POINTS_CAN_DOUBLE_UP = 300;
    public static final int POINTS_OPPONENT_BEHIND_BEFORE_AND_AFTER_MOVE = 300;
    public static final int POINTS_OPPONENT_BEHIND_BEFORE_MOVE_AND_NOT_AFTER_MOVE = 300;
    public static final int POINTS_OPPONENT_AHEAD_BEFORE_AND_AFTER_MOVE = 200;
    public static final int POINTS_NO_OPPONENT_AHEAD_BEFORE_AND_OPPONENT_AHEAD_AFTER_MOVE = 200;
    public static final int POINTS_OPPONENT_AHEAD_BEFORE_MOVE_AND_HIT_AFTER_MOVE = 500;
    public static final int POINTS_CAN_OPEN_TOKEN = 50;
    public static final int POINTS_PLAYER_ALREADY_SAFE_SPOT = 200;


    public static final int[][] BOARD_POSITIONS = new int[][]{
            {3, 283},  // 0 - yellow right turn
            {3, 243},  // 1
            {43, 243}, // 2 -   blue start
            {83, 243},
            {123, 243},
            {163, 243},
            {203, 243},
            {243, 203}, // 7 - blue left turn
            {243, 163},
            {243, 123},
            {243, 83},  //10
            {243, 43},
            {243, 3},
            {283, 3}, // 13 - blue right turn
            {323, 3},
            {323, 43},  // 15 - red starts
            {323, 83},
            {323, 123},
            {323, 163},
            {323, 203},  //19
            {363, 243}, // 20 - red left turn
            {403, 243},
            {443, 243},
            {483, 243},
            {523, 243},
            {563, 243},   //25
            {563, 283},  // 26 - red right turn
            {563, 323},  //27
            {523, 323},  // 28 - green starts
            {483, 323},
            {443, 323},
            {403, 323},
            {363, 323}, //32
            {323, 363}, //33 - green left turn
            {323, 403},
            {323, 443},
            {323, 483},
            {323, 523},
            {323, 563}, //38
            {283, 563},   // 39 - green right turn
            {243, 563},  //40
            {243, 523},  // 41 - yellow starts
            {243, 483},
            {243, 443},
            {243, 403},
            {243, 363}, //45
            {203, 323},  // 46 - yellow left turn
            {163, 323},
            {123, 323},
            {83, 323},
            {43, 323},
            {3, 323},  // 51 - cycle end
            {43, 283},  // 52 - inside blue last row
            {83, 283},
            {123, 283},
            {163, 283},
            {203, 283},
            {243, 283},  // 57 - blue home
            {283, 283}, //58 - center home
            {283, 43}, // 59 - inside red last row
            {283, 83},
            {283, 123},
            {283, 163},
            {283, 203},
            {283, 243}, // 64 - red home
            {523, 283}, // 65 - inside green last row
            {483, 283},
            {443, 283},
            {403, 283},
            {363, 283},
            {323, 283}, // 70 - green home
            {283, 523}, // 71 - inside yellow last row
            {283, 483},
            {283, 443},
            {283, 403},
            {283, 363},
            {283, 323}, // 76 - yellow home
            {54, 104}, // 77 - blue start 1
            {104, 57}, // 78 - blue start 2
            {154, 104},  // 79 - blue start 3
            {104, 154},  // 80 - blue start 4
            {414, 104}, // 81 - red start 1
            {464, 57}, // 82 - red start 2
            {514, 104},  // 83 - red start 3
            {464, 154},  // 84 - red start 4
            {54, 464}, // 85 - yellow start 1
            {104, 417}, // 86 - yellow start 2
            {154, 464},  // 87 - yellow start 3
            {104, 514},  // 88 - yellow start 4
            {414, 464}, // 89 - green start 1
            {464, 417}, // 90 - green start 2
            {514, 464},  // 91 - green start 3
            {464, 514}  // 92 - green start 4
    };

}
