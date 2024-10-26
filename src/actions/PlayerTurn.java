package actions;

public class PlayerTurn {
	private final String command = "getPlayerTurn";
	private int output;

	public PlayerTurn (int output) {
		this.output = output;
	}

	public String getCommand() {
		return command;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}
}
