package HogwartsLegacy;

import java.util.Set;
import java.util.HashSet; //selber hinzugefügt
import java.util.Random;
import java.util.ArrayList;


public class Wizard implements MagicSource, Trader, MagicEffectRealization {
	private String name; //not null not empty 
	private MagicLevel level; //not null
	private int basicHP; //not negative
	private int HP; //not negative; defaults to basicHP
	private int basicMP; //not less than the manapoints associated with the magic level
	private int MP; //not negative; defaults to basicMP
	private int money; //not negative
	private Set<Spell> knownSpells; //not null, may be empty; use HashSet for instantiation
	private Set<AttackingSpell> protectedFrom; //not null, may be empty; use HashSet for instantiation
	private int carryingCapacity; //not negative
	private Set<Tradeable> inventory; //not null, may be empty, use HashSet for instantiation, total weight of inventory may never exceed carryingCapacity

	public Wizard(String name, MagicLevel level, int basicHP, int HP,  int basicMP, int MP, int money, Set<Spell> knownSpells, Set<AttackingSpell> protectedFrom, int carryingCapacity, Set<Tradeable> inventory) {
		
		if (name == null || level == null || knownSpells == null || protectedFrom == null || inventory == null)
			throw new IllegalArgumentException("name, level, knownSpells, protectedFrom or inventory null");
		
		if(name == "")
			throw new IllegalArgumentException("name is invalid");
		
		if(basicHP < 0 || HP < 0 || basicMP < 0 || MP < 0 || money < 0 || carryingCapacity < 0)
			throw new IllegalArgumentException("basicHP, HP, basicMP, MP, money or carryingCapacity < 0");
		
		if(basicMP < level.toMana())
			throw new IllegalArgumentException("basicMP < level.toMana()");
		
		if(inventory.size() < carryingCapacity)
			throw new IllegalArgumentException("inventory size < carryingCapacity");
		
		this.name = name;
		this.level = level;
		this.basicHP = basicHP;
		this.HP = HP;
		this.basicMP = basicMP;
		this.MP = MP;
		this.money = money;
		this.knownSpells = new HashSet<>(knownSpells);
		this.protectedFrom = new HashSet<>(protectedFrom);
		this.carryingCapacity = carryingCapacity;
		this.inventory = new HashSet<>(inventory);
		
	}
	
public Wizard(String name, MagicLevel level, int basicHP, int basicMP, int money, int carryingCapacity) {
		
		if (name == null || level == null)
			throw new IllegalArgumentException("name or level null");
		
		if(name == "")
			throw new IllegalArgumentException("name is invalid");
		
		if(basicHP < 0 || basicMP < 0 || money < 0 || carryingCapacity < 0)
			throw new IllegalArgumentException("basicHP, basicMP, money or carryingCapacity< 0");
		
		if(basicMP < level.toMana())
			throw new IllegalArgumentException("basicMP < level.toMana()");
		
		
		this.name = name;
		this.level = level;
		this.basicHP = basicHP;
		this.HP = basicHP;
		this.basicMP = basicMP;
		this.MP = basicMP;
		this.money = money;
		this.knownSpells = new HashSet<>();
		this.protectedFrom = new HashSet<>();
		this.carryingCapacity = carryingCapacity;
		this.inventory = new HashSet<>();
		
	}
	
	public boolean isDead() {
	//return true, if HP is 0, false otherwise
		if(HP == 0)
			return true;
		else
			return false;
	}  
	  
	private int inventoryTotalWeight() {
		int w = 0;
		for(Tradeable t : inventory)
			w+=t.getWeiht();
		return w;
	//calculates and returns the total weight of all the items in the inventory
	}
	  
	public boolean learn(Spell s) {
		if(s==null)
			throw new IllegalArgumentException("spell s parameter learn == null");
		if(this.isDead())
			return false;
		
		knownSpells.add(s);
		return true;
		
	//if spell is null, IllegalArgumentException has to be thrown
	//if wizard is dead (isDead) no action can be taken and false is returned
	//add spell to the set of knownSpells
	//returns true if insertion was successful, false otherwise
	}
	  
	public boolean forget(Spell s) {
		if(s==null)
			throw new IllegalArgumentException("spell s parameter forget == null");
		if(this.isDead() || knownSpells.contains(s))
			return false;
		else
			return knownSpells.remove(s);
		
	//if spell is null, IllegalArgumentException has to be thrown
	//if wizard is dead (isDead) no action can be taken and false is returned
	//remove spell from the set of knownSpells
	//returns true if removal was successful, false otherwise
	}
	
	public boolean castSpell(Spell s, MagicEffectRealization target) {
		if(target == null || s == null)
			throw new IllegalArgumentException("castSpell - s or target == null");
		if(this.isDead())
			return false;
		if(!knownSpells.contains(s))
			return false;
		if(castPossible(s.getLevel(), s.getManaCost())) {
			s.cast(this,target);
			return true;
		}
		
		return false;
	//if s or target is null, IllegalArgumentException has to be thrown
	//if wizard is dead (isDead) no action can be taken and false is returned
	//if wizard does not know the spell, false is returned
	//call cast on s whith this as source and parameter target as target
	//return true if cast was called
	}
	
	public boolean castPossible(MagicLevel levelNeeded, int manaAmount) {
		if (levelNeeded == null || 0 > manaAmount)
			throw new IllegalArgumentException("provideMana: levelNeeded == null or manaAmount is negativ");
		
		if (levelNeeded.ordinal() > level.ordinal() || MP < manaAmount)
			return false;
		
		if(isDead())
			return false;
		
		if(MP<manaAmount)
			return false;
		
		return true;
	}
	
	public MagicLevel getLevel() {
		return this.level;
	}
	
	public int getMana() {
		return this.MP;
	}
	  
	public boolean castRandomSpell(MagicEffectRealization target) {
		if(knownSpells.isEmpty())
			return false;
		
		Random r = new Random();
	    int index = r.nextInt(knownSpells.size());
	    Spell randomSpell = knownSpells.get(index);
	    
	    return castSpell(randomSpell, target);
	//if this object's knownSpells is empty, return false
	//otherwise choose a random spell from knownSpells and delegate to castSpell(Spell, MagicEffectRealization)
	}
	  
	public boolean useItem(Tradeable item, MagicEffectRealization target) {
		if(item == null || target == null)
			throw new IllegalArgumentException("useItem - item or target == null");
		
		if(this.isDead()) 
			return false;
		
		if(!this.possesses(item))
			return false;
		if(item.getUsages>0) {
			item.useOn(target);
			return true; 
		}
		
		
		return false;
	//if item or target is null, IllegalArgumentException has to be thrown
	//if wizard is dead (isDead) no action can be taken and false is returned
	//if wizard does not possess the item, false is returned
	//call useOn on the item with parameter target as target
	//return true if useOn was called
	}
	
	

	public boolean useRandomItem(MagicEffectRealization target) {
		if(inventory.isEmpty())
			throw new IllegalArgumentException("useRandomItem - inventory empty");
		
		Random r = new Random();
	    int index = r.nextInt(inventory.size());
	    MagicItem randomItem = inventory.get(index);
	    return this.useItem(randomItem, target);
	//if this object's inventory is empty, return false
	//otherwise choose a random item from inventory and delegate to useItem(MagicItem, MagicEffectRealization)
	}
	  
	public boolean sellItem(Tradeable item, Trader target) {
		if(item==null || target == null)
			throw new IllegalArgumentExcption("sellItem - item or target == null");
		if(isDead())
			return false;
		return item.purchase(this,target);
		
	//if item or target is null, IllegalArgumentException has to be thrown
	//if wizard is dead (isDead) no action can be taken and false is returned
	//call purchase on the item with this as seller and target as buyer
	//return true if purchase was called
	}
	//Wizard::useRandomItem/Wizard::sellRandomItem: Bei der Beschreibung der Delegation an die Methoden 
	//useItem/sellItem hat der erste Parameter den Typ Tradeable (es stand ursprünglich MagicItem)
		

	public boolean sellRandomItem(Trader target) {
		if(infentory.isEmpty())
			return false;
		
		Random r = new Random();
	    int index = r.nextInt(inventory.size());
	    MagicItem randomItem = inventory.get(index);
	    return this.sellItem(randomItem, target);
	//if this object's inventory is empty, return false
	//otherwise choose a random item from inventory and delegate to sellItem(MagicItem, MagicEffectRealization)
	}

	@Override
	public String toString() {
	    String output = "[" + this.name + "(" + this.level.toString() + "): " + this.HP + "/" + this.basicHP + " " + this.MP + "/" + this.basicMP + " " + this.money + "Knuts; knows [";

	    Spell[] knownSpellArray = knownSpells.toArray(new Spell[0]);
	    for (int i = 0; i < knownSpellArray.length; i++) {
	        output = output.concat(knownSpellArray[i].toString());
	        if (i < knownSpellArray.length - 1)
	            output = output.concat(", ");
	        else
	            output = output.concat("]; carries [");
	    }

	    Tradeable[] inventoryArray = inventory.toArray(new Tradeable[0]);
	    for (int i = 0; i < inventoryArray.length; i++) {
	        output = output.concat(inventoryArray[i].toString());
	        if (i < inventoryArray.length - 1)
	            output = output.concat(", ");
	        else
	            output = output.concat("]]");
	    }

	    return output;
			
	//returns a string in the format "['name'('level'): 'HP'/'basicHP' 'MP'/'basicMP'; 'money' 'KnutOrKnuts'; 
	// knows 'knownSpells'; carries 'inventory'}"; where 'level' is the asterisks representation of the level 
	// (see MagicLevel.toString) and 'knownSpells' and 'inventory' use the default toString method of Java Set; 
	// 'KnutOrKnuts' is Knut if 'money' is 1, Knuts otherwise
	
	//e.g. [Ignatius(**): 70/100 100/150; 72 Knuts; knows [[Episkey(*): 5 mana; +20 HP], [Confringo: 10 mana; -20 HP]]; carries []]
	}
	
	
	//MagicSource Interface
	@Override
	public boolean provideMana(MagicLevel levelNeeded, int manaAmount) {
	
		if (levelNeeded == null || 0 > manaAmount)
			throw new IllegalArgumentException("provideMana: levelNeeded == null or manaAmount is negativ");
		
		if (levelNeeded.ordinal() > level.ordinal() || MP < manaAmount)
			return false;
		
		if(isDead())
			return false;
		
		if(MP<manaAmount)
			return false;
		
		if(MP != Integer.MAX_VALUE)	
			MP -= manaAmount;
		
		return true;
		
		//Richtig umgesetzt?? denke ich muss das neu schreiben und zwar den einen Teil in MagicSource????
		
	    //levelNeeded==null or negative manaAmount must throw IllegalArgumentException; 
		//returns true if the object has at least the required level and can provide enough mana, false otherwise.
		
	    //a typical implementation will check if the objects level is high enough, returning false if not. 
		//Otherwise it reduces the object's MP by manaAmount. There may be exceptions though, 
		//like objects with infinite mana or supporting all levels  for example
		
		
	//if wizard is dead (isDead) no action can be taken and false is returned
	//check if level is at least levelNeeded, return false
	//if MP<manaAmount return false
	//subtract manaAmount from MP and return true
	}

	//Trader Interface
	@Override
	public boolean canSteal() {
		if(!isDead())
			return true;
		else
			return false;
			
			
		
	}
	// returns true , if this object ’s HP are not 0 ( alive wizard )
	//per default immer false
	
	//Im Override in der Klasse Wizard ist entsprechend true zu retournieren, 
	//wenn das Wizard Objekt noch lebt, false sonst. Der Test am Beginn der steal Methode lautet 
	//dann canSteal statt !isDead().
	
	@Override
	public boolean canLoot() {
		if(!isDead())
			return true;
		else
			return false;
			
			
	}
	
	// returns true , if this object ’s HP are not 0 ( alive wizard )
	//per default immer false
	
	//Im Override in der Klasse Wizard ist entsprechend true zu retournieren, 
	//wenn das Wizard Objekt noch lebt, false sonst. Der Test am Beginn der steal Methode lautet 
	//dann canSteal statt !isDead().
	
	
	@Override
	public boolean possesses(Tradeable item) {
		return inventory.contains(item);
	//return true if the item is in the inventory, false otherwise
	}

	@Override
	public boolean canAfford(int amount) {
		if(money>=amount)
			return true;
		else
			return false;
	//return true if money>=amount, false otherwise
	}

	@Override
	public boolean hasCapacity(int weight) {
		if(inventoryTotalWeight()+weight <=carryingCapacity)
			return true;
		else
			return false;
	//return true if inventoryTotalWeight+weight<=carryingCapacity, false otherwise
	}

	@Override
	public boolean pay(int amount) {
		if(isDead())
			return false;
		else
			if(money >= amount) {
				money-=amount;
				return true;
			}
			else
				return false;
		
				
	//if wizard is dead (isDead) no action can be taken and false is returned
	//if this owns enough money deduct amount from money and return true, return false otherwise
	}
	    
	@Override
	public boolean earn(int amount) {
		if(isDead)
			return false;
		else {
			money+=amount;
			return true;
		}
	//if wizard is dead (isDead) no action can be taken and false is returned
	//add amount to this object's money and return true
	}
	    
	@Override
	public boolean addToInventory(Tradeable item) {
		if(possesses(item))
			return false;
		if(inventoryTotalWeight()+item.getWeight() >carryingCapacity)
			return false;
		return inventory.add(item);
	//add item to inventory if carryingCapacity is sufficient
	//returns true if item is successfully added, false otherwise (carrying capacity exceeded or item is already in the inventory)
	}

	@Override
	public boolean removeFromInventory(Tradeable item) {
		return inventory.remove(item);
	//remove item from inventory
	//returns true if item is successfully removed, false otherwise (item not in the inventory)
	}
	
	public int getCC() {
		return carryingCapacity;
	}
	
	@Override
	public boolean steal(Trader thief) {
		if (thief == null) {
	        throw new IllegalArgumentException("thief == null");
	    }

	    if (!thief.canSteal()) {
	        return false;
	    }

	    if (this.inventory.isEmpty()) {
	        return false;
	    }

	    Random random = new Random();
	    int index = random.nextInt(this.inventory.size());
	    Tradeable item = this.inventory.get(index);

	    this.removeFromInventory(item);
	    
	    return thief.addToInventory(item);
			// if thief is null , IllegalArgumentException has to be thrown
			// if thief cannot steal ( canSteal returns false ), no action can be
			//taken and false is returned
			// returns false , if the object ’s inventory is empty
			// otherwise transfers a random item from the this object ’s inventory
			//into the thief ’s inventory ;
			// if the thief ’s inventory has not enough capacity , the object just
			//vanishes and false is returned
			// returns true if , theft was successful

	//if thief is null, IllegalARgumentException has to be thrown
	//if thief is dead (isDead) no action can be taken and false is returned
	//returns false if the object's inventory is empty
	//otherwise transfers a random item from the this object's inventory into the thief's inventory;
	//if the thief's inventory has not enough capacity the object just vanishes and false is returned
	//returns true if theft was successful
	}
	
	/*Wizard::steal: hier stand, dass geprüft werden soll, ob thief tot ist. Die entsprechende Methode ist aber 
	 * im Trader Interface gar nicht vorhanden. Es wurden zwei neue Methoden im Trader Interface definiert: 
	 * canSteal und canLoot, die defaultmäßig immer false retournieren. Im Override in der Klasse Wizard 
	 * ist entsprechend true zu retournieren, wenn das Wizard Objekt noch lebt, false sonst. 
	 * Der Test am Beginn der steal Methode lautet dann canSteal statt !isDead().
	 */

	@Override
	public boolean isLootable() {
		return isDead();
	//returns true if this object's HP are 0 (dead wizard)
	}
	  
	@Override
	public boolean loot(Trader looter) {
		if(looter==null)
			throw new IllegalArgumentException("looter==null");
		if(isDead())
			return false;
		if(isLootable()) {
			boolean itemsTransferred = false;
	        for (Tradeable item : this.inventory) {
	            if (looter.addToInventory(item)) {
	                itemsTransferred = true;
	            }
	        }
	        this.inventory.clear();
	        return itemsTransferred;
		}
			
	//if looter is dead (isDead) no action can be taken and false is returned
	//if the this object can be looted (isLootable), transfer all the items in the object's inventory into the looter's inventory; 
	//items that don't fit in the looter's inventory because auf the weight limitation just vanish
	//returns true if at least one item was successfully transferred, false otherwise
	//im Fall looter==null eine Exception zu werfen ist.
	}
	  
	//MagicEffectRealization Interface
	@Override
	public void takeDamage(int amount) {
		if(amount < 0)
			throw new IllegalArgumentException("amount < 0");
		
		if(HP< amount)
			HP=0;
		else
			HP-=amount;
	//reduce the object's HP by amount ensuring however that HP does not become negative.
	}
	    
	@Override
	public void takeDamagePercent(int percentage) {
		if(percentage<0 || percentage > 100)
			throw new IllegalArgumentException("takenDamagePercent - 0 < percentage < 100");
		double damage = (double) percentage / 100 * this.basicHP;
	    int damageInt = (int) damage;
	    this.HP -= damageInt;
	    
	    if (this.HP < 0) {
	        this.HP = 0;
	    }
	//reduce the object's HP by the percentage given of the object's basic HP value ensuring however, that HP does not become negative. Do calculations in double truncating to int only for the assignment
	}
	    
	@Override
	public void weakenMagic(int amount) {
		if(amount < 0)
			throw new IllegalArgumentException("amount < 0");
		
		if(amount<MP)
			MP-=amount;
		else
			MP=0;
	//reduce the object's MP by amount ensuring however that MP does not become negative.
	}
	  
	@Override
	public void weakenMagicPercent(int percentage) {
		if(percentage<0 || percentage > 100)
			throw new IllegalArgumentException("weakenMagicPercent - 0 < percentage < 100");
		
		double reduction = (double) percentage / 100 * this.basicMP;
	    int reductionInt = (int) reduction;
	    this.MP -= reductionInt;
	    
	    if (this.MP < 0) {
	        this.MP = 0;
	    }
	//reduce the object's MP by the percentage given of the object's basic MP value ensuring however, that MP does not become negative. Do calculations in double truncating to int only for the assignment
	}
	  
	@Override
	public void heal(int amount) {
		if(amount<0)
			throw new IllegalArgumentException("amount < 0");
		HP+=amount; //größer als basicHP????
	//increase the object's HP by the amount given.
	}
	    
	@Override
	public void healPercent(int percentage) {
		if(percentage<0 || percentage > 100)
			throw new IllegalArgumentException("healPercent - 0 < percentage < 100");
		
		double increase = (double) percentage / 100 * this.basicHP;
	    int increaseInt = (int) increase;
	    this.HP += increaseInt;
	    
	    if (this.HP > this.basicHP) {
	        this.HP = this.basicHP;
	    }
	//increase the object's HP by the percentage given of the object's basic HP. Do calculations in double truncating to int only for the assignment
	}

	@Override
	public void enforceMagic(int amount) {
		if(amount<0)
			throw new IllegalArgumentException("amount < 0");
		MP+=amount; //größer als basicMP???
	//increase the object's MP by the amount given.
	}
	  
	@Override
	public void enforceMagicPercent(int percentage) {
		if(percentage<0 || percentage > 100)
			throw new IllegalArgumentException("enforceMagicPercent - 0 < percentage < 100");
		double increase = (double) percentage / 100 * this.basicMP;
	    int increaseInt = (int) increase;
	    this.MP += increaseInt;
	    
	    if (this.MP > this.basicMP) {
	        this.MP = this.basicMP;
	    }
	//increase the object's MP by the percentage given of the object's basic MP. Do calculations in double truncating to int only for the assignment
	}
	    
	@Override
	public boolean isProtected(Spell s) {
		if(s==null)
			throw new IllegalArgumentException("isProtected - s==null");
		return protectedFrom.contains(s);
	//return true if s is contained in instance variable protectedFrom
	}
	    
	@Override
	public void setProtection(Set<AttackingSpell> attacks) {
		if(attacks==null)
			throw new IllegalArgumentException("setProtected - attacks == null");
		protectedFrom.addAll(attacks);
	//add all spells from attacks to instance variable protectedFrom
	}

	@Override
	public void removeProtection(Set<AttackingSpell> attacks) {
		if(attacks==null)
			throw new IllegalArgumentException("removeProtection - attacks == null");
		protectedFrom.removeAll(attacks);
	}
	//remove all spells from attacks from instance variable protectedFrom
}