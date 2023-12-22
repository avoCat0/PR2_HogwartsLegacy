package HogwartsLegacy;

public abstract class Potion extends MagicItem {
	
	public Potion(String name, int usages, int price, int weight) {
		super(name, usages, price, weight);
	}
	public void drink(Wizard drinker) {
		useOn(drinker);
	//delegates to method call useOn(drinker)
	}
	  
	@Override
	public String usageString() {
		
		if(usages == 1)
			return "gulp";
		
		else
			return "gulps";
	//returns "gulp" if usages is equal to 1, "gulps" otherwise
	}
}