import java.util.Objects;
import java.util.Scanner;
import java.awt.Color;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean testMode;
    private boolean gameOver;
    private boolean samuraiMode;
    private String mode;
    static OutputWindow window = new OutputWindow();
    public Color orange = new Color(240, 80, 20); // RGB!
    public Color purple = new Color(144, 26, 150); // RGB!
    public Color teal = new Color(26, 150, 119); // RGB!

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */

    public static void addText(String str) {
        window.clear();
        window.addTextToWindow("\n" + str, Color.black);
    }
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {

        window.addTextToWindow("Welcome to TREASURE HUNTER!", teal);
        window.addTextToWindow("\nGoing hunting for the big treasure, eh?", orange);
        window.addTextToWindow("\nWhat's your name, Hunter? ", Color.cyan);
        String name = SCANNER.nextLine().toLowerCase();
        window.addTextToWindow("\nSelect mode? (e/n/h): ", purple);
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("h")) {
            hardMode = true;
            hunter = new Hunter(name, 10, "h");
            mode = "h";
        } else if (hard.equals("test")) {
            hunter = new Hunter(name);
        } else if (hard.equals("e")) {
            easyMode = true;
            mode = "e";
            hunter = new Hunter(name, 20, "e");
        } else if (hard.equals("n")) {
            mode = "n";
            hunter = new Hunter(name, 10, "n");
        } else if (hard.equals("s")) {
            samuraiMode = true;
            hunter = new Hunter(name, 10, "s");
            mode = "s";
        }

    }

    public boolean getEasyMode() {
        return easyMode;
    }

    private void checkGameOver() {
        if (hunter.getHunterGold() < 0) {
            gameOver = true;
        } else {
            gameOver = false;
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.5;

            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode) {
            markdown = 0;
            toughness = .2;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, mode);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, mode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x") && !gameOver) {
            window.clear();
            window.addTextToWindow("\n" + currentTown.getLatestNews(), Color.blue);
            window.addTextToWindow("\n***", purple);
            if (hunter.getHunterGold() < 0) {
                gameOver = true;
                window.addTextToWindow("\nYou lose!", Color.red);
            } else if (hunter.getTreasures()[2] != null) {
                gameOver = true;
                window.addTextToWindow("\nCongratulations! You found the last of the hidden treasures!", teal);
                window.addTextToWindow("\nYou win!", Color.green);
            } else {
                window.addTextToWindow("\n" + hunter.toString(), Color.black);
                window.addTextToWindow("\n" + currentTown.toString(), Color.black);

                window.addTextToWindow("\n(B)uy something at the shop.", Color.black);
                window.addTextToWindow("\n(S)ell something at the shop.", Color.black);
                window.addTextToWindow("\n(M)ove on to a different town", Color.black);
                window.addTextToWindow("\n(L)ook for trouble!", Color.black);
                window.addTextToWindow("\n(H)unt for treasure!", Color.black);
                window.addTextToWindow("\n(D)ig for gold!", Color.black);
                window.addTextToWindow("\nGive up the hunt and e(X)it.\n", Color.black);

                window.addTextToWindow("\nWhat's your next move? ", Color.black);

                choice = SCANNER.nextLine().toLowerCase();
                processChoice(choice);
            }
        }



    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                addText(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("x")) {
            addText("Fare thee well, " + hunter.getHunterName() + "!");
        } else if (choice.equals("d")) {
            currentTown.digForGold();
        } else if (choice.equals("h")) {
            currentTown.lookForTreasure();
        } else {
            window.addTextToWindow("Yikes! That's an invalid option! Try again.", Color.red);
        }
    }

}