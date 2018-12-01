package cs603.mementoHw;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;
public class Game {
    Game(){
        killedNum = 0;
        monsters = new ArrayList<Character>();
        monsters.add(new Character(2, 1));
        monsters.add(new Character(4, 2));
        monsters.add(new Character(3, 2));
        monsters.add(new Character(3, 2));
        monsters.add(new Character(6, 3));
        //for convenience Attack success rate is 100%
        bs = () -> true;
        //it could be random as follows:
        /*
        bs = () ->{
            Random rd = new Random();
            return rd.nextBoolean();
        };
        */
    };
    private Hero hero;
    private int killedNum;
    private List<Character> monsters;
    private BooleanSupplier bs;
    public static int TOTAL_MONSTERS = 5;
    public static int INITIAL_HERO_HP = 5;
    public static int INITIAL_HERO_AP = 1;
    public static int LEVELONE_NUM = 2;
    public static int LEVELTWO_NUM = 3;
    public void setUpHero(String name){
        hero = new Hero(name, INITIAL_HERO_HP, INITIAL_HERO_AP);
    }
    public Hero getHero(){ return hero; }
    public boolean won(){ return killedNum == TOTAL_MONSTERS; }
    public void setAttackSucceeds(BooleanSupplier b){ bs = b; }

    //when hero die and restart, all the enemy also reset life
    public void resetMonster(){
        for (int i = 0; i < monsters.size(); ++i){
            monsters.get(i).resetLife();
        }
    }

    public boolean fightNextBattle(){
        Character attacker = hero;                  //hero attack first
        Character defender = getNextOpponent();
        Hero.HeroState state = hero.checkpoint();
        while(true){
            int heroHP = hero.getLife();
            int monsterHP = getNextOpponent().getLife();
            if (heroHP <= 0) {
                hero.restore(state);
                resetPosition();
                resetMonster();
                return false;
            }
            if (monsterHP <= 0) {
                setNextOpponent();          //set next monster to confront
                state = hero.checkpoint();  // hero win recorder state
                hero.heroUpgrade();         // hero win, upgrade
                return true;
            }
            if (bs.getAsBoolean()){         //whether attack succeed, default is 100%, and 50% in turn for test
                defender.damage(attacker.getAttack());
            }
            //swap attacker and defender
            Character temp = attacker;
            attacker = defender;
            defender = temp;
        }
    }

    public Character getNextOpponent(){
        return monsters.get(killedNum);
    }

    public void setNextOpponent(){
        ++killedNum;
    }

    public void resetPosition(){
        if (killedNum <= 1)                 //it means die in the first floor
            killedNum = 0;                  //start from the very beginning
        else                                //it means die in the second floor
            killedNum = LEVELONE_NUM;       //start from second floor's first monster
    }

    public static void main(String[] args) {
        System.out.println("Welcome to MiniFight");
        System.out.println("Please enter hero's name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        assert(name.isEmpty() == false);
        Game game = new Game();
        game.setUpHero(name);
        Hero hero = game.getHero();
        while(true){
            if (game.won()){
                System.out.println("Victory is yours!");
                return;
            }
            System.out.println("Please choose one:");
            System.out.println("0. Quit game");
            System.out.println("1. Fight next monster");
            int number = scanner.nextInt();
            if (number == 0){
                System.out.println("Thanks for playing");
                return;
            }
            else{
                if (game.fightNextBattle()){
                    System.out.println("Congratulations!");
                    System.out.print("Your new status: ");
                    System.out.println(hero.toString());
                }
                else{
                    System.out.println("You lost the battle. Game resets to the start of the level.");
                    System.out.print("Restored state: ");
                    System.out.println(hero.toString());
                }
            }
        }
    }
}
