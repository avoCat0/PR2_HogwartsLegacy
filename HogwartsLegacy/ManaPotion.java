package HogwartsLegacy;

public class ManaPotion extends Potion {
	private int mana; //must not be negative
	
	public ManaPotion(String name, int usages, int price, int weight, int mana){
		if(mana<0) 
			throw new IllegalArgumentException("mana must not be negative");
		super(name, usages, price, weight);
		this.mana=mana;
	}

	@Override
	public String additionalOutputString() {
		return "; +"+mana+" MP";
	//returns "; +'mana' MP";
	//e.g. (total result of toString) "[Mana Potion; 1 g; 2 Knuts; 1 gulp; +20 mana]"
	}

	@Override  
	public void useOn(MagicEffectRealization target) {
		if(target==null)
			throw new IllegalArgumentException("useOn target == null");
		
		if(tryUsage()) {
			target.enforceMagic(mana);
		}
	//if usages>0 reduce usages by 1 (tryUsage method) and
	//increase MP of target bei mana (call method enforceMagic(mana)
	}
}


/*HealthPotion::additionalOutputString/ManaPotion::additionalOutputString: 
 * in der Syntaxbeschreibung wird HP oder MP vorgeschrieben. Im darauffolgenden Beispiel stand aber 
 * health oder mana. Hier ist generell immer die Syntaxbeschreibung heranzuziehen. 
 * Die Spezifikation wurde entsprechend korrigiert.
 */