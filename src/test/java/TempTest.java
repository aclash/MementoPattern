package cs603.mementoHw;

import org.junit.Before;
import org.junit.Test;
import java.util.function.BooleanSupplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TempTest {
    private Game game;
    private Hero hero;

    // Initialize game.
    @Before
    public void initGame() {
        game = new Game();
        game.setUpHero("Jan");
        hero = game.getHero();
    }

    // Simple test if progress reverts to start, character's health reverts to original
    @Test
    public void toBeginning() {
        int heroLife = hero.getLife();
        int heroMaxLife = hero.getMaxLife();
        Hero.HeroState saved = hero.checkpoint();
        hero.damage(1);
        hero.restore(saved);
        assertEquals(heroLife, hero.getLife());
        assertEquals(heroMaxLife, hero.getMaxLife());
    }

    // Test if we initialize check for winning correctly
    @Test
    public void notYet() {
        assertFalse(game.won());
    }

    // Test whether attack succeeds
    @Test
    public void checkVictory() {
        game.setAttackSucceeds(new WinLoseAlternating());
        assertTrue(game.fightNextBattle());
    }

    // Test if first opponent is selected correctly
    @Test
    public void checkFirstOpponent() {
        Character next = game.getNextOpponent();
        assertEquals(2, next.getLife());
        assertEquals(1, next.getAttack());
    }

    // Test if other opponents are selected correctly
    @Test
    public void checkRemainingOpponents() {
        int [] expectedLife = {4, 3, 3, 6};
        int [] expectedAttack = {2, 2, 2, 3};
        // The assertion for fighting battles are just sanity checks
        // to make sure Hero is winning each battle
        for (int battleCt = 0; battleCt < expectedLife.length; battleCt++){
            game.setAttackSucceeds(new WinLoseAlternating());
            assertTrue(game.fightNextBattle());
            Character next = game.getNextOpponent();
            assertEquals(expectedLife[battleCt], next.getLife());
            assertEquals(expectedAttack[battleCt], next.getAttack());
        }
    }

    // More complex test to see if state is properly restored after
    // one victory and one loss (so to beginning of first level
    @Test
    public void winOneLoseOne() {
        int heroLife = hero.getLife();
        int heroAttack = hero.getAttack();
        game.setAttackSucceeds(new WinLoseAlternating());
        assertTrue(game.fightNextBattle());
        // WinLoseAlternating finished with a successful attack for
        // the first battle, so doing nothing means
        // effects will be opposite in next battle
        assertFalse(game.fightNextBattle());
        // after first victory, life and attack are incremented by 1
        assertEquals(heroLife+1, hero.getLife());
        assertEquals(heroAttack+1, hero.getAttack());
        // should reset to first opponent
        Character next = game.getNextOpponent();
        assertEquals(2, next.getLife());
        assertEquals(1, next.getAttack());

    }

    // Test if state is correctly restored to start of 2nd level
    // after getting through first level
    @Test
    public void restoreOnLevel2() {
        // beat all but one monsters on first and 2nd level
        // hero in the level2 means he beat level one's two monster and not all the level2, so it can be 2 or 3 or 4.
        int battlesWon = game.LEVELONE_NUM + game.LEVELTWO_NUM - 1;
        // compute expected stats
        int expectedLife = hero.getLife() + battlesWon;
        int expectedAttack = hero.getAttack() + battlesWon;
        for (int i = 0; i < battlesWon; i++) {
            game.setAttackSucceeds(new WinLoseAlternating());
            game.fightNextBattle();
        }
        game.fightNextBattle(); // lose next one
        // check that we reset to the first monster on the 2nd level
        Character next = game.getNextOpponent();
        assertEquals(3, next.getLife());
        assertEquals(2, next.getAttack());
        // and have the correct stats for the given number of victories
        // plus the bonus for beating the first level
        assertEquals(expectedLife, hero.getLife());
        assertEquals(expectedAttack, hero.getAttack());
    }

    // Test if overall victory is correctly determined after all
    // opponents are defeated
    @Test
    public void testWon() {
        for (int i = 0; i < Game.TOTAL_MONSTERS; i++) {
            game.setAttackSucceeds(new WinLoseAlternating());
            game.fightNextBattle();
        }
        assertTrue(game.won());
    }

    static class WinLoseAlternating implements BooleanSupplier {
        int counter;
        @Override
        public boolean getAsBoolean() {
            return 0 == counter++ % 2;
        }
    }
}