package HogwartsLegacy;

public class HealthPotion extends Potion {
	private int health;  //must not be negative

	public HealthPotion(String name, int usages, int price, int weight, int health) {
		if(health<0)
			throw new IllegalArgumentException("Health muss not be negative");
		super(name, usages, price, weight);
		this.health=health;
	}
	@Override
	public String additionalOutputString() {
	return "; +"  health + " HP"; 
	//returns "; +'health' HP";
	//e.g. (total result of toString) "[Health Potion; 1 g; 1 Knut; 5 gulps; +10 health]"
	}

	@Override  
	public void useOn(MagicEffectRealization target) {
		if(target==null)
			throw new IllegalArgumentException("useOn target == null");
		
		if(tryUsage()) {
			target.heal(health);
		}
		
	//if usages>0 reduce usages by 1 (tryUsage method) and
	//increase HP of target by health (call method heal(health)
	}
}

/*HealthPotion::additionalOutputString/ManaPotion::additionalOutputString: 
 * in der Syntaxbeschreibung wird HP oder MP vorgeschrieben. Im darauffolgenden Beispiel stand aber 
 * health oder mana. Hier ist generell immer die Syntaxbeschreibung heranzuziehen. 
 * Die Spezifikation wurde entsprechend korrigiert.
 */