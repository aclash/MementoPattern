package cs603.mementoHw;

public class Character {
    protected int currentHP;                      //current life point
    protected int maxHP;                          //max life point
    protected int AP;                             //attack point
    Character(int hp, int ap){
        maxHP = hp;
        currentHP = hp;
        AP = ap;
    }
    public int getLife(){ return currentHP; }
    public int getMaxLife(){ return maxHP; }
    public int getAttack(){ return AP; }
    public void damage(int amount){
        currentHP -= amount;
    }
    public void resetLife(){ currentHP = maxHP;}
    public String toString(){ return "Next monster: " + "life = " + maxHP + ", attack = " + AP; }
}
