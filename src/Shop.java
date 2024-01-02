import javax.swing.*;
import java.util.Scanner;
import java.awt.Color;

/**
 * The Shop class controls the cost of the items in the Treasure Hunt game. <p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method. <p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Shop {
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int BOOTS_COST = 8;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;
    private static final int SHOVEL_COST = 8;
    private static final int SWORD_COST = 0;

    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private double markdown;
    private Hunter customer;
    private boolean secretMode = false;


    /**
     * The Shop constructor takes in a markdown value and leaves customer null until one enters the shop.
     *
     * @param markdown Percentage of markdown for selling items in decimal format.
     */
    public Shop(double markdown, String secretMode) {
        this.markdown = markdown;
        customer = null; // is set in the enter method
        if (secretMode.equals("s")) {
            this.secretMode = true;
        }
    }

    /**
     * Method for entering the shop.
     *
     * @param hunter the Hunter entering the shop
     * @param buyOrSell String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell) {
        customer = hunter;

        if (buyOrSell.equals("b")) {
            String str = "Welcome to the shop! We have the finest wares in town.";
            str += "\nCurrently we have the following items:";
            str += "\n" + inventory();
            str += "\nWhat're you lookin' to buy? ";
            TreasureHunter.addText(str);
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, true);
            if (cost == 0 && customer.hasItemInKit("sword")) {
                TreasureHunter.addText("We ain't got none of those.");
            } else {
                TreasureHunter.addText("It'll cost you " + cost + " gold. Buy it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();

                if (option.equals("y")) {
                    buyItem(item);
                }
            }
        } else {
            String str = "What're you lookin' to sell? ";
            str += "You currently have the following items: " + customer.getInventory();
            TreasureHunter.addText(str);
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, false);
            if (cost == 0) {
                TreasureHunter.addText("We don't want none of those.");
            } else {
                TreasureHunter.addText("It'll get you " + cost + " gold. Sell it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();

                if (option.equals("y")) {
                    sellItem(item);
                }
            }
        }
    }

    /**
     * A method that returns a string showing the items available in the shop
     * (all shops sell the same items).
     *
     * @return the string representing the shop's items available for purchase and their prices.
     */
    public String inventory() {
        String str = "Water: " + WATER_COST + " gold\n";
        str += "Rope: " + ROPE_COST + " gold\n";
        str += "Machete: " + MACHETE_COST + " gold\n";
        str += "Boots: " + BOOTS_COST + " gold\n";
        str += "Horse: " + HORSE_COST + " gold\n";
        str += "Boat: " + BOAT_COST + " gold\n";
        str += "Shovel: " + SHOVEL_COST + " gold\n";
        if (secretMode) {
            str += "Sword: " + SWORD_COST + " gold\n";
        }
        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     *
     * @param item The item being bought.
     */
    public void buyItem(String item) {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem)) {
            TreasureHunter.addText("Ye' got yerself a " + item + ". Come again soon.");
        } else if (!customer.hasItemInKit("sword")){
            TreasureHunter.addText("Hmm, either you don't have enough gold or you've already got one of those!");
        } else {
            customer.buyItem(item, 0);
            TreasureHunter.addText("Just take anything ye want! I don't want any trouble...");
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     *
     * @param item The item being sold.
     */
    public void sellItem(String item) {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice)) {
            TreasureHunter.addText("Pleasure doin' business with you.");
        } else {
            TreasureHunter.addText("Stop stringin' me along!");
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     *
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying) {
        if (isBuying) {
            return getCostOfItem(item);
        } else {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item) {
        if (item.equals("water")) {
            return WATER_COST;
        } else if (item.equals("rope")) {
            return ROPE_COST;
        } else if (item.equals("machete")) {
            return MACHETE_COST;
        } else if (item.equals("horse")) {
            return HORSE_COST;
        } else if (item.equals("boat")) {
            return BOAT_COST;
        } else if (item.equals("boots")) {
            return BOOTS_COST;
        } else if (item.equals("shovel")) {
            return SHOVEL_COST;
        } else if (item.equals("sword")) {
            return SWORD_COST;
        } else {
            return 0;
        }
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item) {
        int cost = (int) (getCostOfItem(item) * markdown);
        return cost;
    }
}