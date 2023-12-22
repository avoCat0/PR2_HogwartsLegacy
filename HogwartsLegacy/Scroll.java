package HogwartsLegacy;

public class Scroll extends MagicItem {
	private Spell spell;
	
	public Scroll(String name, int usages, int price, int weight, Spell spell) {
		if(spell == null)
			throw new IllegalArgumentException("Scroll - spell illegal");
		
		super(name, usages, price, weight);
		this.spell=spell;
	}
	  
	@Override
	public String additionalOutputString() {
		return "; casts " + spell.toString();
		
	//returns "; casts 'spell'"
	//e.g. (total result of toString) "[Scroll of doom; 1 g; 100 Knuts; 5 usages; casts [Bombarda: 20 mana; -50 % HP]]"
	}

	@Override  
	public void useOn(MagicEffectRealization target) {
		if(target==null)
			throw new IllegalArgumentException("useOn target == null");
		
		if(tryUsage()) {
			spell.cast(this, target);
		}
			
	//if usages>0 reduce usages by 1 (tryUsage method) and
	//cast the spell using this as magic source and parameter target as target 
	}
}