package cards;

import java.util.ArrayList;

public class Card {
	protected int mana;
	protected int health;
	protected String description;
	protected ArrayList<String> colors;
	protected String name;

	public Card() {
	}

	public Card(Card card) {
		mana = card.mana;
		health = card.health;
		description = card.description;
		colors = card.colors;
		name = card.name;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(final int mana) {
		this.mana = mana;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(final int health) {
		this.health = health;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public ArrayList<String> getColors() {
		return colors;
	}

	public void setColors(final ArrayList<String> colors) {
		this.colors = colors;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CardInput{"
				+  "mana="
				+ mana
				+ ", health="
				+ health
				+  ", description='"
				+ description
				+ '\''
				+ ", colors="
				+ colors
				+ ", name='"
				+  ""
				+ name
				+ '\''
				+ '}';
	}
}
