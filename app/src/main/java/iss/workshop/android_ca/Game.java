package iss.workshop.android_ca;

public class Game {

    private String player1_name;
    private String player2_name;
    private long player1_time;
    private long player2_time;
    private int player1_score;
    private int player2_score;
    private int gameMode; // 0 for multi, 1 for single

    // Empty Constructor
    public Game (){}

    public String getPlayer1_name() {
        return player1_name;
    }

    public void setPlayer1_name(String player1_name) {
        this.player1_name = player1_name;
    }

    public String getPlayer2_name() {
        return player2_name;
    }

    public void setPlayer2_name(String player2_name) {
        this.player2_name = player2_name;
    }

    public long getPlayer1_time() {
        return player1_time;
    }

    public void setPlayer1_time(long player1_time) {
        this.player1_time = player1_time;
    }

    public long getPlayer2_time() {
        return player2_time;
    }

    public void setPlayer2_time(long player2_time) {
        this.player2_time = player2_time;
    }

    public int getPlayer1_score() {
        return player1_score;
    }

    public void setPlayer1_score(int player1_score) {
        this.player1_score = player1_score;
    }

    public int getPlayer2_score() {
        return player2_score;
    }

    public void setPlayer2_score(int player2_score) {
        this.player2_score = player2_score;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }
}

