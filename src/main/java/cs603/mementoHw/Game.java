package cs603.mementoHw;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.Scanner;
public class Game {
    Game(){
        monster = 0;
        Monsters = new ArrayList<Character>();
        Monsters.add(new Character(2, 1));
        Monsters.add(new Character(4, 2));
        Monsters.add(new Character(3, 2));
        Monsters.add(new Character(3, 2));
        Monsters.add(new Character(6, 3));
    };
    private Hero hero;
    private int monster;
    private boolean isWin;
    private List<Character> Monsters;
    private BooleanSupplier bs;
    public static int TOTAL_MONSTERS = 5;
    public void setUpHero(String name){
        hero = new Hero(name);
    }
    public Hero getHero(){
        return hero;
    }

    public boolean won(){
        return monster == 5;
    }

    public void setAttackSucceeds(BooleanSupplier b){
        bs = b;
    }

    public boolean isWinBattle(){
        int mlife = Monsters.get(monster).getLife();
        int hAp = hero.getAttack();
        int hlife = hero.getLife();
        int mAp = Monsters.get(monster).getAttack();
        int hCnt = mlife / hAp + (mlife % hAp == 0 ? 0 : 1);
        int mCnt = hlife / mAp + (hlife % mAp == 0 ? 0 : 1);
        if (hCnt > mCnt)
            return false;
        else {
            hero.damage(Monsters.get(monster).getAttack() * (hCnt - 1));
            return true;
        }
    }

    public boolean fightNextBattle(){
        Hero.HeroState saved = hero.checkpoint();
        if (isWinBattle()) {
            monster++;
            hero.getWinBonus();
            saved = hero.checkpoint();
            return true;
        }
        else{
            hero.restore(saved);
            if (monster <= 1)
                monster = 0;
            else
                monster = 2;
            return false;
        }
    }

    public Character getNextOpponent(){
        return Monsters.get(monster);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to MiniFight");
        System.out.println(" Please enter hero's name:");
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        game.setUpHero(name);
        Hero hero = game.getHero();
        while(true){
            if (game.won()){
                System.out.println("Victory is yours!");
                return;
            }
            System.out.println(" Please choose one:");
            System.out.println(" 0. Quit game");
            System.out.println(" 1. Fight next monster");
            int number = scanner.nextInt();
            if (number == 0){
                System.out.println("Thanks for playing");
                    return;
            }
            else{
                if (game.fightNextBattle()){
                    System.out.println(" Congratulations!");
                    System.out.print("Your new status: ");
                    System.out.println(hero.toString());
                }
                else{
                    System.out.println(" You lost the battle. Game resets to the start of the level.");
                    System.out.print("Restored state: ");
                    System.out.println(hero.toString());
                }
            }
        }
    }
}
