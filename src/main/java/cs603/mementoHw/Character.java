package cs603.mementoHw;

public class Character {
    private int currentHP;                      //current life point
    private static final int MAX_HP = 100;      //constant value of max hp
    private int maxHP;                          //current life point
    private int AP;                             //attack point
    Character(int hp, int ap){
        maxHP = hp;
        currentHP = hp;
        AP = ap;
    }
    public int getLife(){
        return currentHP;
    }
    public int getAttack(){
        return AP;
    }
    public void damage(int amount){
        currentHP -= amount;
    }
    public String toString(){
        return "Monster: " + "life = " + maxHP + ", attack = " + AP;
    }
}
