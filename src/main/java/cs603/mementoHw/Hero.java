package cs603.mementoHw;

public class Hero extends Character{
    private String name;
    private int bonusAttck;
    Hero(String name, int maxHP, int AP){
        super(maxHP, AP);
        this.name = name;
        bonusAttck = 1;
    }
    public void heroUpgrade(){
        ++maxHP;
        AP += bonusAttck;
    }
    public String toString(){
        return name + ": life/maxLife = " + currentHP + "/" + maxHP + ", attack = " + AP;
    }
    public class HeroState{
        private int maxHP;
        private int AP;

        HeroState(int hp, int ap) {
            maxHP = hp;
            AP = ap;
        }
        int getMaxHP(){ return maxHP;}
        int getAP(){ return AP;}
    }

    public HeroState checkpoint(){                      //recorder state
        return new HeroState(maxHP, AP);
    }

    public void restore(HeroState state){               //rest to the memento state
        this.currentHP = state.getMaxHP();
        this.maxHP = state.getMaxHP();
        this.AP = state.getAP();
    }
}
