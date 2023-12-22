package HogwartsLegacy;

import java.util.HashSet;

public class AttackingSpell extends Spell {
	private boolean type;
	private boolean percentage;
	private int amount; //has to be non negative; if percentage==true, amount must be in the interval [0,100]
	
	public AttackingSpell(String name, int manaCost, MagicLevel levelNeeded, boolean type, boolean percentage, int amount) {
		if(amount < 0 || (percentage && (amount < 0 || amount > 100)))
			throw new IllegalArgumentException("Attacking Spell: amount illegal argument");
		super(name,manaCost,levelNeeded);
		this.type=type;
		this.percentage=percentage;
		this.amount=amount;
	}
	

	@Override
	public void doEffect(MagicEffectRealization target) {
		Set<AttackingSpell> attacks = new HashSet<>();
	    attacks.add(this);
		
		if (target.isProtected(this)) {
	        target.removeProtection(attacks);
	    } else {
	        if (type) {
	            if (percentage) {
	                target.takeDamagePercent(amount);
	            } else {
	                target.takeDamage(amount);
	            }
	        } else {
	            if (percentage) {
	                target.weakenMagicPercent(amount);
	            } else {
	                target.weakenMagic(amount);
	            }
	        }
	    }
	//if the target is protected against this spell (isProtected), then protection against exactly this spell is removed (removeProtection
	//otherwise use one of the functions takeDamage, takeDamagePercent, weakenMagic or weakenMagicPercent on target according to the flags type and percentage
	}

	@Override
	public String additionalOutputString() {
		String output = "; -" + amount;
	    if (percentage) {
	        output += " %";
	    }
	    if (type) {
	        output += " HP";
	    } else {
	        output += " MP";
	    }
	    return output;
	//returns "; -amount 'percentage' 'HPorMP'", where 'percentage' is a '%'-sign if percentage is true, empty otherwise and HPorMP is HP if type is true, MP otherwise
	//e. g. "; -10 MP" or "; -50 % HP"
	}
}