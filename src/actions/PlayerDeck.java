package actions;

import cards.GameCard;

import java.util.ArrayList;

public class PlayerDeck {
	private final String command = "getPlayerDeck";
	private int playerIdx;
	private ArrayList <GameCard> output;

	public PlayerDeck(int playerIdx, ArrayList<GameCard> output) {
		this.playerIdx = playerIdx;
		this.output = output;
	}

	public String getCommand() {
		return command;
	}

	public int getPlayerIdx() {
		return playerIdx;
	}

	public void setPlayerIdx(int playerIdx) {
		this.playerIdx = playerIdx;
	}

	public ArrayList<GameCard> getOutput() {
		return output;
	}

	public void setOutput(ArrayList<GameCard> output) {
		this.output = output;
	}
}
