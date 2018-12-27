package com.company;

import javax.swing.*;

import static com.company.Game.start;

/**
 * Blackjack by Albert Li, made in December of 2017 as a high school project. I learned programming for the first time in
 * fall of 2017, and this project was made after a few months of learning.
 *
 * The following is a standard Blackjack game.
 * See https://en.wikipedia.org/wiki/Blackjack for rules.
 *
 * In this version, the Dealer will stand on 17, but the Dealer will ALWAYS win ties. A "blackjack" will pay the player 3 to 2.
 * The dealing shoe contains 8 decks of standard playing cards.
 * Player will start at $100 and can choose to bet whichever amount he/she pleases. If the user decides to leave, or if
 * the player runs out of money, the game will end.
 * Good luck!
 */
public class Main {

    public static int playerBalance = 100;
    public static int bet = 0;
    private static int handsPlayed = 0;
    public static int wins = 0;
    public static int losses = 0;
    static Game game = new Game();

    /**
     * The main method is the game menu, listing the cumulative wins and losses of the player.
     * @param args
     */

    public static void main(String[] args) {
        String[] options = new String[]{"Bet Money", "Cancel and Leave"};
        while (true) {
            int response = JOptionPane.showOptionDialog(null, "Choose to play!\nYou have played: " +
                    handsPlayed + " hands.\nWins: " + wins + "\nLosses: " + losses,
                    "BlackJack - Albert Li",// assign every option as an integer
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);//text box, title, using regular option, using a plain message.
            if (response == 0) {
                handsPlayed++;
                startGame();

            } else {
                JOptionPane.showMessageDialog(null, "Thanks for Playing!");
                System.exit(0);
            }
        }
    }

    /**
     * Takes user input for how much he/she wishes to bet.
     */
    public static void startGame() {
        boolean temp = true;
        while(temp) {
            String input = JOptionPane.showInputDialog("How much money do you want to bet?" + "\nYou currently have $"
            + playerBalance);
            try {
                bet = Integer.parseInt(input);
                if (bet < 0 || bet > playerBalance) {
                    throw new Exception();
                }
                temp = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "You must input a valid integer!" +
                        "Do not bet more than what you currently have!");
            }
            if (!temp) {
                start();
                if (game.runGame()) {
                    wins++;
                    playerBalance += bet;
                    JOptionPane.showMessageDialog(null, "You won $" + bet +
                            "\nYou now have $" + playerBalance);
                } else {
                    losses++;
                    playerBalance -= bet;
                    JOptionPane.showMessageDialog(null, "You lost $" + bet +
                            "\nYou now have $" + playerBalance);
                }
                if (playerBalance <= 0) {
                    JOptionPane.showMessageDialog(null, "You're out of money!\nHands Played: " +
                            handsPlayed + "\nWins: " + wins + "\nLosses: " + losses);
                    System.exit(0);
                }
            }
        }
    }
}
