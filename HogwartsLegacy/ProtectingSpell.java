package HogwartsLegacy;

import java.util.Set;
import java.util.ArrayList; //selber hinzugefügt


public class ProtectingSpell extends Spell {
	private Set<AttackingSpell> attacks; //must not be null or empty; use HashSet as concrete type
	
	public ProtectingSpell(String name, int manaCost, MagicLevel levelNeeded,Set<AttackingSpell> attacks) {
		
		if (attacks == null || attacks.isEmpty()) {
            throw new IllegalArgumentException("ProtectingSpell: attacks must not be null or empty");
        }
		
		super(name, levelNeeded, attacks);
		this.attacks= new HashSet<>(attacks);
	}
	
	@Override
	public void doEffect(MagicEffectRealization target) {
		target.setProtection(attacks);
	//call setProtection method on target with attacks as parameter
	}

	@Override
	public String additionalOutputString() {
		StringBuilder sb = new StringBuilder();
	    sb.append("; protects against [");
	    
	    AttackingSpell[] attackArray = attacks.toArray(new AttackingSpell[0]);
	    for (int i = 0; i < attackArray.length; i++) {
	        sb.append(attackArray[i].toString());
	        if (i < attackArray.length - 1)
	            sb.append(", ");
	        else
	        	sb.append("]");

	    }

	    return sb.toString();


	//returns "; protects against 'listOfAttackSpells'" where 'listOfAttackSpells' is a bracketed list of all the attack spells (Java default toString method for sets)
	//e. g. "; protects against [[Confringo: 10 mana; -20 HP], [Bombarda: 20 mana; -50 % HP]]"
	}
}