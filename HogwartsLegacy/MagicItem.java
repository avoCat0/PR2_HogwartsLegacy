package HogwartsLegacy;

public abstract class MagicItem implements Tradeable, MagicEffectRealization, MagicSource {
	private String name; //must not be null or empty
	private int usages; //number of usages remaining; must not be negative
	private int price;  //must not be negative
	private int weight; //must not be negative
	
	public MagicItem(String name, int usages, int price, int weight) {
		if(name == null || name == "")
			throw new IllegalArgumentException("MagicItem name illegal");
		if(usages < 0 || price < 0 || weight < 0)
			throw new IllegalArgumentException("MagicItem usages, price or weight < 0");
		
		this.name=name;
		this.usages=usages;
		this.price=price;
		this.weight=weight;
		
	}

	public int getUsages() {
		return usages;
    //returns value of usages (for access from deriving classes)
	}

	public boolean tryUsage() {
		if(usages > 0) {
			usages--;
			return true;
		}
		
		else
			return false;
    //if usages > 0 reduce usage by 1 and return true, otherwise return false
	}

	public String usageString() {
		if(usages == 1)
			return "use";
		else
			return "uses";
		
    //returns "use" if usages is equal to 1, "uses" otherwise
	}

	public String additionalOutputString() {
		return "";
		
    //returns empty string. Is overridden in deriving classes as needed
	}
	
	String currencyString = "Knut";

	@Override 
	public String toString() {
		return  "[" + name + "; " + weight + " g; " + price + " " + currencyString + "; " + usages + " " + usageString() + additionalOutputString() + "]";
    //formats this object according to "['name'; 'weight' g; 'price' 'currencyString'; 'usages' 'usageString''additionalOutputString']"
    //'currencyString' is "Knut" if price is 1, "Knuts" otherwise
    //e.g. (when additionalOutput() returns an empty string) "[Accio Scroll; 1 g; 1 Knut; 5 uses]" or "[Alohomora Scroll; 1 g; 10 Knuts; 1 use]"
	}

	//Tradeable Interface:
	@Override
	public int getPrice() {
		return price;
    //returns price of the object
	}

	@Override    
	public int getWeight() {
		return weight;
    //returns weight of the object
	}
	  
	//MagicSource Interface:
	@Override
	public boolean provideMana(MagicLevel levelNeeded, int amount) {
		
		if (levelNeeded == null || 0 > amount)
			throw new IllegalArgumentException("provideMana: levelNeeded == null or amount is negativ");
	
	    //levelNeeded==null or negative manaAmount must throw IllegalArgumentException; 
		//returns true if the object has at least the required level and can provide enough mana, false otherwise.
		
	    //a typical implementation will check if the objects level is high enough, returning false if not. 
		//Otherwise it reduces the object's MP by manaAmount. There may be exceptions though, 
		//like objects with infinite mana or supporting all levels  for example
		
		return true;
    //always returns true; no Exceptions needed
	}

	//MagicEffectRealization Interface:
	@Override
	public void takeDamagePercent(int percentage) {
		if(percentage<0 || percentage > 100)
			throw new IllegalArgumentException("takenDamagePercent - 0 < percentage < 100");
		usages= usages*(1-percentage/100);
	//reduce usages to usages*(1-percentage/100.)
	}
	
}