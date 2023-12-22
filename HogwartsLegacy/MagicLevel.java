package HogwartsLegacy;

public enum MagicLevel {
	NOOB(50,"*"),
	ADEPT(100, "**"),
	STUDENT(200, "***"),
	EXPERT(500, "****"),
	MASTER(1000, "*****");
	
	private int MPvalue;
	private String rank;
	
	private MagicLevel(int MPvalue, String rank) {
		this.MPvalue = MPvalue;
		this.rank = rank;
	}
	
	public int toMana() {
		return MPvalue;
	}
	
	@Override
	public String toString() {
		return rank;
	}
}