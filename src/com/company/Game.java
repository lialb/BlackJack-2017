package com.company;

import javax.swing.*;
import java.util.ArrayList;

import static com.company.CardDeck.createDeck;

/**
 * This class runs the game of BlackJack
 */
public class Game extends Main {
    /**
     * playerString is the literal representation of the cards for the player (e.g. "Ace of hearts")
     */
    ArrayList<String> playerString = new ArrayList<>();
    /**
     * dealerString is the literal representation of the cards for the dealer (same as playerString)
     */
    ArrayList<String> dealerString = new ArrayList<>();
    /**
     * playerInt is the value of the cards for the player (e.g. a queen would be worth 10)
     */
    ArrayList<Integer> playerInt = new ArrayList<>();
    /**
     * dealerInt is the same as playerInt but for the dealer
     */
    ArrayList<Integer> dealerInt = new ArrayList<>();
    /**
     * hit dictates if the player decides to hit on the very first action
     */
    private boolean hit = false;
    /**
     * split dictates if the player decides to split on the first action
     */
    public boolean split = false;

    static CardDeck card = new CardDeck();
    /**
     * starts game
     */
    public static void start() {
        createDeck();
    }

    /**
     * This method will deal the cards to the player and dealer, recognize a blackjack, and prompts player for actions
     * @return true if the player wins or false if the player loses
     */
    public boolean runGame() {
        playerString.clear();
        playerInt.clear();
        dealerString.clear();
        dealerInt.clear();
        hit = false;
        split = false;
        dealCards();
        if (checkBlackjack(playerInt, playerString)) {
            JOptionPane.showMessageDialog(null, "You have a blackJack! \n" + playerString);
            bet *= 1.5;
            wins++;
            return true;
        } else if (checkBlackjack(dealerInt, dealerString)) {
            JOptionPane.showMessageDialog(null, "Dealer has a blackJack!" +
                    "Dealer's cards: " + dealerString);
            losses++;
            return false;
        }

        String[] options = new String[]{"Hit", "Stand", "Double Down", "Split"};
        while(true) {
            int response = JOptionPane.showOptionDialog(null, "Your cards: " +
                            playerString + "\nYour Total: " +
                            sum(playerInt) +
                            "\nDealer's Cards: " + dealerString.get(0) + " and ???",
                    "BlackJack - Albert Li",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (response == 0) {
                hit = true;
                return hitOrStand();
            } else if (response == 1) {
                return hitOrStand();
            } else if (response == 2) {
                if (playerBalance - 2 * bet < 0) {
                    JOptionPane.showMessageDialog(null,
                            "You do not have enough money to double down!");
                } else {
                    bet *= 2;
                    return doubleDown();
                }
            } else if (response == 3) {
                String[] arr1 = playerString.get(0).split(" ");
                String[] arr2 = playerString.get(1).split(" ");
                if (arr1[0].equals(arr2[0])) {
                    if (bet * 2 > playerBalance) {
                        JOptionPane.showMessageDialog(null,
                                "You do not have enough money to split!");
                    } else {
                        bet *= 2;
                        JOptionPane.showMessageDialog(null,
                                "You have split!\nYour bet is now: " + bet);
                        return split(playerString.get(0), playerString.get(1));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You cannot split!");
                }
            }
        }
    }

    private boolean doubleDown() {
        playerInt.add(card.dealInt());
        playerString.add(card.dealString());
        ace(playerInt);
        JOptionPane.showMessageDialog(null, "You double down!\nYour bet is now: "+ bet);
        JOptionPane.showMessageDialog(null, "You are dealt: "+
                playerString.get(playerString.size() - 1) +
                "\nYour total: " + sum(playerInt));
        if(checkIfBust(playerInt)) {
            return false;
        }
        return compareSum(dealerHit(), playerInt);
    }

    private boolean hitOrStand() {
        if (!hit) {
            ace(playerInt);
            JOptionPane.showMessageDialog(null, "You stand!\nYour cards: " + playerString +
                    "\nYour total: " + sum(playerInt));
            return compareSum(dealerHit(), playerInt);
        } else {
            playerInt.add(card.dealInt());
            playerString.add(card.dealString());
            ace(playerInt);
            JOptionPane.showMessageDialog(null, "You were dealt: " + playerString.get(2));
            if (checkIfBust(playerInt)) {
                return false;
            }
            String[] options = new String[]{"Hit", "Stand"};
            while(true) {
                int response = JOptionPane.showOptionDialog(null,
                        "Your cards: " + playerString +
                                "\nYour Total: " + sum(playerInt) +
                                "\nDealer's Cards: " + dealerString.get(0) + " and ???",
                        "BlackJack - Albert Li",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (response == 0) {
                    playerString.add(card.dealString());
                    playerInt.add(card.dealInt());
                    ace(playerInt);
                    JOptionPane.showMessageDialog(null, "Your were dealt: " +
                            playerString.get(playerString.size() - 1));
                    if (checkIfBust(playerInt)) {
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "You stand!\nYour Cards: " + playerString +
                            "\nYour total: " + sum(playerInt) +
                            "\nDealer's Cards: " + dealerString + "\nDealer's total: " + sum(dealerInt));

                    return compareSum(dealerHit(), playerInt);
                }
            }
        }
    }

    /**
     * This method calculates the value of the Ace to make sure it doesn't bust the player unnecessarily.
     * @param list determines the value of the Ace inside of the ArrayList
     */
    private void ace(ArrayList<Integer> list) {
        if (list.contains(1)) {
            while (sum(list) <= 11) {
                if (list.contains(1)) {
                    if (!(sum(list) + 10 > 21)) {
                        list.set(list.indexOf(1), 11);
                    }
                }
            }
        }
        if (list.contains(11)) {
            while (sum(list) > 21) {
                if (list.contains(11)) {
                    list.set(list.indexOf(11), 1);
                }
            }
        }
    }

    /**
     * See title of method
     * @param list any ArrayList to check if the person has gone over 21
     * @return true if the person busts and vice versa
     */
    private boolean checkIfBust(ArrayList<Integer> list) {
        if (sum(list) > 21) {
            JOptionPane.showMessageDialog(null, "You have busted!\nYour Cards: " +
                    playerString + "\nYour total: " + sum(list));
            return true;
        }
        return false;
    }
    private boolean compareSum(int dealerSum, ArrayList<Integer> list) {
        JOptionPane.showMessageDialog(null,"Your cards: " + playerString +
                "\nYour total: " + sum(playerInt) + "\nDealer Cards: " + dealerString +
                "\nDealer's total: " + sum(dealerInt));
        if (dealerSum >= sum(list)) {
            return false;
        }
        return true;
    }
    private int dealerHit() {
        ace(dealerInt);
        JOptionPane.showMessageDialog(null, "Dealer's Cards: " + dealerString);
        while (sum(dealerInt) < 17) {
            if (sum(dealerInt) >= sum(playerInt)) {
                break;
            }
            dealerString.add(card.dealString());
            dealerInt.add(card.dealInt());
            ace(dealerInt);
            JOptionPane.showMessageDialog(null, "Dealer was dealt: " + dealerString.get(dealerString.size() - 1) +
                    "\nDealer's total: " + sum(dealerInt));

            if (sum(dealerInt) > 21) {
                JOptionPane.showMessageDialog(null, "Dealer has busted! Dealer's total: " +
                        sum(dealerInt));
                return 0;
            }
        }
        return (sum(dealerInt));
    }
    private void dealCards() {
        card.createDeck();
        card.shuffle();
        playerString.add(card.dealString());
        playerString.add(card.dealString());
        playerInt.add(card.dealInt());
        playerInt.add(card.dealInt());
        ace(playerInt);

        dealerString.add(card.dealString());
        dealerString.add(card.dealString());
        dealerInt.add(card.dealInt());
        dealerInt.add(card.dealInt());
        ace(dealerInt);
    }
    private boolean checkBlackjack(ArrayList<Integer> intList, ArrayList<String> stringList) {
        if (intList.contains(11) && intList.contains(10)) {
            return true;
        }
        if (intList.contains(1) && intList.contains(10)) {
            return true;
        }
        return false;
    }
    private int sum(ArrayList<Integer> intList) {
        int total = 0;
        for (int i = 0; i < intList.size(); i++) {
            total += intList.get(i);
        }
        return total;
    }
    /**
     * This method will run if the player decides to split. It gives another hand to the player and plays normally.
     * @param card1 the first card of the original hand
     * @param card2 the second card of the original hand
     * @return true if the player wins both, false if the player loses one or both.
     */
    private boolean split(String card1, String card2) {
        boolean hand1 = true, hand2 = true, hand1Ended = false, hand2Ended = false;
        ArrayList<String> secondString = new ArrayList<>();
        ArrayList<Integer> secondInt = new ArrayList<>();
        secondString.add(card2);
        secondInt.add(playerInt.get(1));
        playerString.remove(1);
        playerInt.remove(1);

        playerString.add(card.dealString());
        playerInt.add(card.dealInt());
        ace(playerInt);
        secondString.add(card.dealString());
        secondInt.add(card.dealInt());
        ace(secondInt);
        JOptionPane.showMessageDialog(null, "Your first hand: " + playerString +
                "\nFirst hand total: " + sum(playerInt) + "\n\nYour second hand: " + secondString +
                "\nSecond hand total: " + sum(secondInt) + "\nDealer hand: " + dealerString.get(0) + " and ????");

        String[] options = new String[]{"Hit", "Stand"};
        while (!hand1Ended) {
            int response = JOptionPane.showOptionDialog(null, "Your first hand: " + playerString + "\nYour Total: " + sum(playerInt) +
                            "\nDealer's Cards: " + dealerString.get(0) + " and ???",
                    "BlackJack - Albert Li",// assign every option as an integer
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (response == 0) {
                playerString.add(card.dealString());
                playerInt.add(card.dealInt());
                ace(playerInt);
                if (checkIfBust(playerInt)) {
                    hand1Ended = true;
                    hand1 = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Your first hand stands");
                hand1Ended = true;
            }
        }
        while (!hand2Ended) {
            int response = JOptionPane.showOptionDialog(null, "Your second hand: " + secondString +
                            "\nYour Total: " + sum(secondInt) +
                            "\nDealer's Cards: " + dealerString.get(0) + " and ???",
                    "BlackJack - Albert Li",// assign every option as an integer
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (response == 0) {
                secondString.add(card.dealString());
                secondInt.add(card.dealInt());
                ace(secondInt);
                if (checkIfBust(secondInt)) {
                    hand2Ended = true;
                    hand2 = false;
                }
            } else {
                JOptionPane.showMessageDialog(null,"Your second hand stands.");
                hand2Ended = true;
            }
        }
        if (!hand1 && !hand2) {
            JOptionPane.showMessageDialog(null, "Both hands lost!");
            return false;
        }
        int dealerTotal = dealerHit();
        if (hand1 && compareSum(dealerHit(), playerInt)) {
            JOptionPane.showMessageDialog(null, "Your first hand won!\nFirst hand" +
                    playerString + "\nFirst hand total: " + sum(playerInt) + "\n\nDealer hand: " + dealerString +
                    "\nDealer total: " + sum(dealerInt));
        } else {
            hand1 = false;
            JOptionPane.showMessageDialog(null, "Your first hand lost!\nFirst hand" +
                    playerString + "\nFirst hand total: " + sum(playerInt) + "\n\nDealer hand: " + dealerString +
                    "\nDealer total: " + sum(dealerInt));
        }
        if (hand2 && compareSum(dealerHit(), playerInt)) {
            JOptionPane.showMessageDialog(null, "Your second hand won!\nSecond hand" +
                    secondString + "\nSecond hand total: " + sum(secondInt) + "\n\nDealer hand: " + dealerString +
                    "\nDealer total: " + sum(dealerInt));
        } else {
            hand2 = false;
            JOptionPane.showMessageDialog(null, "Your second hand lost!\nSecond hand" +
                    secondString + "\nSecond hand total: " + sum(secondInt) + "\n\nDealer hand: " + dealerString +
                    "\nDealer total: " + sum(dealerInt));
        }
        if (hand1 && hand2) {
            JOptionPane.showMessageDialog(null, "Both hands won!\nYou won: $" + bet);
            return true;
        }
        if (hand1 || hand2) {
            bet /= 2;
            JOptionPane.showMessageDialog(null, "Only one hand won.\nYou lose: $" + bet);
            return false;
        }
        if (!hand1 && !hand2) {
            JOptionPane.showMessageDialog(null,"You lost both hands.\nYou lost: $" + bet);
            return false;
        }
        return false;
    }
}
