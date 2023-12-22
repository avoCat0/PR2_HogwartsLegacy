package HogwartsLegacy;

public class HealingSpell extends Spell {
	private boolean type;
	private boolean percentage;
	private int amount; //has to be non negative; if percentage==true, amount must be in the interval [0,100]

	public HealingSpell(String name, int manaCost, MagicLevel levelNeeded, boolean type, boolean percentage, int amount) {
		if(amount < 0 || (percentage && (amount < 0 || amount > 100)))
			throw new IllegalArgumentException("Healing Spell: amount illegal argument");
		super(name,manaCost,levelNeeded);
		this.type=type;
		this.percentage=percentage;
		this.amount=amount;
	}
	
	@Override
	public void doEffect(MagicEffectRealization target) {
		  if (type) {
		        if (percentage) {
		            target.healPercent(amount);
		        } else {
		            target.heal(amount);
		        }
		    } 
		  else {
		        if (percentage) {
		            target.enforceMagicPercent(amount);
		        } else {
		            target.enforceMagic(amount);
		        }
		    }
	//use one of the functions heal, healPercent, enforceMagic or enforceMagicPercent according to the flags type and percentage
	}

	@Override
	public String additionalOutputString() {
		String output = "; +" + amount;
	    if (percentage) {
	        output += " %";
	    }
	    if (type) {
	        output += " HP";
	    } else {
	        output += " MP";
	    }
	    return output;
		
	//returns "; +amount 'percentage' 'HPorMP'", where 'percentage' is a '%'-sign if percentage is true, empty otherwise and HPorMP is HP if type is true, MP otherwise
	//e. g. "; +10 HP" or "; +50 % MP"
	}
}