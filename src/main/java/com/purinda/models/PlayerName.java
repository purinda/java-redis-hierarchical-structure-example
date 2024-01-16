package com.purinda.models;

public class PlayerName {
    private int underageHierId;
    private String playerName;

    public PlayerName(int underageHierId, String playerName) {
        this.underageHierId = underageHierId;
        this.playerName = playerName;
    }

    public String toString() {
        return "PlayerName: [ID: " + underageHierId + ", PlayerName: " + playerName + "]";
    }
}
