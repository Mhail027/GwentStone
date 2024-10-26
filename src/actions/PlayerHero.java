package actions;

import cards.GameCard;
import cards.HeroCard;

public class PlayerHero {
	private final String command = "getPlayerHero";
	private int playerIdx;
	private HeroCard output;

	public PlayerHero (int playerIdx, HeroCard output) {
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

	public HeroCard getOutput() {
		return output;
	}

	public void setOutput(HeroCard output) {
		this.output = output;
	}
}
