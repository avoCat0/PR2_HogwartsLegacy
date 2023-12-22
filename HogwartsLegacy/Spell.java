package HogwartsLegacy;

public abstract class Spell {
	private String name;  //must not be null or empty
	private int manaCost; //must not be negative
	private MagicLevel levelNeeded; //must not be null
	
	public Spell(String name, int manaCost, MagicLevel levelNeeded) {
		if(name == null || name == "" || manaCost < 0 || levelNeeded == null)
			throw new IllegalArgumentException("Spell: name, manaCost or levelNeeded - illegal");
		this.name=name;
		this.manaCost=manaCost;
		this.levelNeeded=levelNeeded;
	}
	
	public MagicLevel getLevel() {
		return levelNeeded;
	}
	
	public int getManaCost() {
		return manaCost;
	}
	
	
	public void cast(MagicSource source, MagicEffectRealization target) {
		if(source.provideMana(this.levelNeeded, this.manaCost))
			doEffect(target);
		
	//ensure necessary magic level and get necessary energy by calling provideMana on source (this will typically reduce MP in source)
	//if provideMana fails (returns false) cast is canceled
	//otherwise the abstract method doEffect is called
	}
	  
	public abstract void doEffect(MagicEffectRealization target);
	//the actual effect of the spell on target must be implemented by the subclasses
	  
	public String additionalOutputString() {
		return "";
	//returns ""; is overridden in deriving classes when needed
	}
	  
	@Override
	public String toString() {
		return "[" + name + "(" + levelNeeded.toString() + "): " + manaCost + " mana"+ additionalOutputString() + "]";
	//return output in format "['name'('levelNeeded'): 'manaCost' mana'additionalOutputString']"; where 'levelNeeded' is displayed as asterisks (see MagicLevel.toString)
	//e.g. (full Output containing additionalOutputString) [Episkey(*): 5 mana; +20 HP]
	}
}