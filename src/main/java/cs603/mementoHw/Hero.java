package cs603.mementoHw;

public class Hero {
    private int currentHP;
    private int maxHP;
    private int AP;
    private String name;
    private static final int INITIAL_MAX_HP = 5;
    private static final int INITIAL_AP = 1;
    Hero(String name){
        this.name = name;
        maxHP = INITIAL_MAX_HP;
        currentHP = INITIAL_MAX_HP;
        AP = INITIAL_AP;
    }
    public int getLife(){
        return currentHP;
    }
    public int getMaxLife(){
        return maxHP;
    }
    public int getAttack(){
        return AP;
    }
    public void damage(int amount){
        currentHP -= amount;
    }
    public void getWinBonus(){
        maxHP++;
        AP++;
    }
    public String toString(){
        return name + ": life/maxLife = " + currentHP + "/" + maxHP + ", attack = " + AP;
    }
    public class HeroState{
        private int hp;
        private int ap;

        HeroState(int hp, int ap) {
            this.hp = hp;
            this.ap = ap;
        }
    }
    private HeroState state;

    public HeroState checkpoint(){
        return new HeroState(maxHP, AP);
    }

    public void restore(HeroState state){
        this.state = state;
        this.currentHP = this.maxHP;
    }

}
