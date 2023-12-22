package HogwartsLegacy;

import java.util.List;
import java.util.ArrayList;

public class Concoction extends Potion {
	private int health;  //may be any int value
	private int mana;    //may be any int value
	private List<Spell> spells; //must not be null but may be empty; Use ArrayList as concrete type
	//it is not allowed for health and mana to be both 0 and spells to be empty; The potion must at least have one effect
	  
	public Concoction(String name, int usages, int price, int weight, int health, int mana, List<Spell> spells) {
		if(spells==null)
			throw new IllegalArgumentException("Concoction spells==null");
		
		if(health == 0 && mana == 0 && spells.isEmpty())
			throw new IllegalArgumentException("Concoction health, mana or spells must have effect");
		
		super(name, usages, price, weight);
		this.health=health;
		this.mana=mana;
		this.spells=new ArrayList<>(spells);
		
	}
	
	public Concoction(String name, int usages, int price, int weight, int health, int mana) {
		if(spells==null)
			throw new IllegalArgumentException("Concoction spells==null");
		
		if(health == 0 && mana == 0 && spells.isEmpty())
			throw new IllegalArgumentException("Concoction health, mana or spells must have effect");
		
		super(name, usages, price, weight);
		this.health=health;
		this.mana=mana;
		this.spells= new ArrayLsit<>();
		
	}
	
	@Override
	public String additionalOutputString() { 
		StringBuilder output = new StringBuilder("");
		
		if(health != 0 || mana != 0 || !spells.isEmpty())
			output.append("; ");
		
		if (health != 0 ) {
	        output.append(health > 0 ? "+" : "-").append(Math.abs(health)).append(" HP");
	        
	        if (mana != 0)
	        	output.append("; ");
	    }

	    if (mana != 0) 
	        output.append(mana > 0 ? "+" : "-").append(Math.abs(mana)).append(" MP");
	    

	    if (!spells.isEmpty()) {
	        output.append("; cast [");
	        
	        for(int i = 0; i < spells.size(); i++ ) {
	        	if(i!=spells.size()-1) 
	        		output.append(spells.get(i).toString() + ", ");
	        	else
	        		output.append(spells.get(i).toString()+"]");
	        }
	    }

	    return output.toString();
	//returns "; '+/-''health' HP; '+/-''mana' MP; cast 'spells' ";
	//here '+/-' denotes the appropriate sign, spells will be a bracketed list of spells (Java default toString method for lists)
	//e.g. (total result of toString) "[My Brew; 2 g; 2 Knuts; 4 gulps; -5 HP; +10 MP; cast [[Confringo -20 health], [Diffindo -15 health]]]"
	//if health or mana is 0 or spells is empty, then the respective part(s) are suppressed e. g. "[Your Brew; 2 g; 1 Knut; 1 gulp; +5 MP]
	}

	@Override  
	public void useOn(MagicEffectRealization target) {
		if(target==null)
			throw new IllegalArgumentException("useOn target == null");
		
		if(tryUsage()) {
			if(health>0)
				target.heal(health);
			if(health<0)
				target.takeDamage(health);
			if(mana>0)
				target.enforceMagic(mana);
			if(mana<0)
				target.weakenMagic(mana);
			for(int i = 0; i < spells.size(); i++ )
				spells.get(i).cast(this, target);
		}
			
		
	//if usages>0 reduce usages by 1 (tryUsage method) and
	//change HP of target by health (call method heal(health) or takeDamage(health) depending on sign of health)
	//change MP of target by mana (call method enforceMagic(magic) or wakenMagic(magic) depending on sign of magic)
	//call cast Method for every spell in spells
	}
	
	//Concoction:useOn: Es stand: "change MP of target by mana (call method enforceMagic(magic) or wakenMagic(magic) depending on sign of magic)". 
	//Das Wort magic ist hier alle dreimal durch mana ersetzt worden, wakenMagic durch weakenMagic.
	
}