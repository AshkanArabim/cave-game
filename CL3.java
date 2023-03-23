/* ASHKAN ARABI-MIANROODI
[CS1101 - FA22] Comprehensive Lab 2
This work is to be done individually. It is not 
permitted to share, reproduce, or alter any part of 
this
assignment for any purpose. Students are not 
permitted from sharing code, uploading this
assignment online in any form, or 
viewing/receiving/modifying code written from 
anyone else. This
assignment is part of an academic course at The 
University of Texas at El Paso and a grade will be
assigned for the work produced individually by 
the student. *

import java.io.File;
import java.util.Scanner;
import java.util.Random;

public class CL3 {
    // execution code to copy: javac .\CL3.java ; java CL3

    /*
     * Main method
     * In here you will gather all necessary assets and start the game with those
     * Assets.
     */
    public static void main(String[] args) {
        String[][] dungeon = loadDungeon();

        Enemy[] enemies = readEnemies();
        int[] playerCurr = dungeonEntrance(dungeon);
        Item[] items = getItems();
        Player player = createPlayer("Player");
        player.setVisited(new boolean[dungeon.length][dungeon[0].length]);

        // DEBUG
        // for (int i = 0; i < items.length; i++) {
        // System.out.println(items[i]); // --> the whole thing is a null???
        // }
        // enter();

        gameStart(player, dungeon, playerCurr, enemies, items);
    }

    /*
     * 
     * Loading Methods and file reading methods
     * 
     */

    /*
     * To load the dungeon
     */
    public static String[][] loadDungeon() {
        String[][] dungeon = new String[22][26];

        try {
            String dungeonPath = "./Assets/dungeon.csv";
            File dungeonFile = new File(dungeonPath);
            Scanner dungeonReader = new Scanner(dungeonFile);

            // populate first row with -1s
            for (int i = 0; i < 26; i++) {
                dungeon[0][i] = "-1";
            }

            // read from file
            for (int i = 1; i < 21; i++) {
                // pupulate first and last cell with -1
                dungeon[i][0] = "-1";
                dungeon[i][25] = "-1";

                String line = dungeonReader.nextLine();
                Scanner lineReader = new Scanner(line);
                lineReader.useDelimiter(",");

                for (int j = 1; j < 25; j++) {
                    String cell = lineReader.next();
                    dungeon[i][j] = cell;
                }
            }

            // populate last row with -1s
            for (int i = 0; i < 26; i++) {
                dungeon[21][i] = "-1";
            }

            // DEBUG: just print out the dungeon.
            // for (int i = 0; i < dungeon.length; i++) {
            // for (int j = 0; j < dungeon[0].length; j++) {
            // System.out.print(dungeon[i][j] + " ");
            // }
            // System.out.println("\n");
            // }
            // scan();

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }

        return dungeon;
    }

    /*
     * To locate the entrance of a dungeon
     */
    public static int[] dungeonEntrance(String[][] dungeon) {
        int[] coords = new int[2];
        for (int i = 0; i < dungeon.length; i++) {
            for (int j = 0; j < dungeon[0].length; j++) {
                if (dungeon[i][j].equals("e")) {
                    coords[0] = i; // vertical location
                    coords[1] = j; // horizantal location
                    return coords;
                }
            }
        }
        return coords;
    }

    /*
     * Read and get enemies types from a txt file
     * text file order
     * name,health,attack power, item drop
     */
    public static Enemy[] readEnemies() {
        String enemyPath = "./Assets/Enemy.txt";
        int enemyLines = numLines(enemyPath);
        Enemy[] enList = new Enemy[enemyLines];

        try {
            File enemyFile = new File(enemyPath);
            Scanner enemyReader = new Scanner(enemyFile);
            for (int i = 0; i < enemyLines; i++) {
                String line = enemyReader.nextLine();
                Scanner lineReader = new Scanner(line);
                enList[i] = new Enemy(
                        lineReader.next(),
                        lineReader.nextInt(),
                        lineReader.nextInt(),
                        lineReader.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return enList;
    }

    /*
     * Read and get items from a txt file
     * text file order
     * name,type,amount -> if amount is 0 then its infinite
     */
    public static Item[] getItems() {
        String itemsPath = "./Assets/Items.txt";
        int itemLines = numLines(itemsPath);
        Item[] items = new Item[itemLines];

        try {
            File itemsFile = new File(itemsPath);

            Scanner itemScanner = new Scanner(itemsFile);

            for (int i = 0; i < itemLines; i++) {
                String line = itemScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter("-");
                items[i] = new Item(
                        lineScanner.next(),
                        lineScanner.next(),
                        lineScanner.nextInt());
            }
        } catch (Exception e) {
            System.out.println("ITS HERE!!!!!!!");
            e.printStackTrace();
        }
        return items;
    }

    /*
     * This method will be used to create the player
     * text file order
     * sword level, shield level, hasMap, hasCompass
     */
    public static Player createPlayer(String playerName) {
        String playerPath = "./Assets/Player.txt";

        try {
            File playerFile = new File(playerPath);
            Scanner ps = new Scanner(playerFile);
            ps.useDelimiter(",");
            return new Player(
                    ps.nextInt(),
                    ps.nextInt(),
                    ps.nextInt(),
                    ps.nextBoolean(),
                    ps.nextBoolean());
        } catch (Exception e) {
            e.printStackTrace();
            // just to please the compiler
            return new Player(0, 0, 0, false, false);
        }
    }

    /*
     * Method to get the number of lines of a txt or csv file
     */
    public static int numLines(String fileName) {
        int size = 0;
        try {
            File users = new File(fileName);
            Scanner userScanner = new Scanner(users);

            while (userScanner.hasNextLine()) {
                size++;
                userScanner.nextLine();
            }
            userScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /*
     * Method to get an enemy, depending on the area that the player is
     * is the rate of the enemy that can spawn, unless its a boss.
     */
    public static Enemy getEnemy(Enemy[] enemies, boolean boss, int maxLv) {
        // return a random one
        if (boss) {
            Enemy selected = enemies[4];
            enemies[4] = enemies[5];
            enemies[5] = enemies[6];
            enemies[6] = null;
            return selected;
        } else {
            return enemies[random(maxLv)];
        }
    }

    /*
     * 
     * Main Game methods
     * 
     */

    /*
     * Method that will run the main game
     */
    public static void gameStart(Player player, String[][] dungeon, int[] playerCurr, Enemy[] enemies, Item[] items) {
        String move = "";
        int enemyRate = 0;
        int enemyThreshold = 10; // supposed to be easy
        // LATER ON: ADD DIFFICULTY: MODIFIES THE ENEMY GENERATION THRESHOLD

        clearScreen();
        while (true) {
            // Check for special rooms, check for possible paths and check for the enemy
            // rate and go into battle

            if (player.getHp() == 0) {
                gameOver(false);
                enter();
                break;
            }

            clearScreen();

            // check for special rooms
            if (checkRoom(dungeon, playerCurr)) {
                checkSpecial(move, playerCurr, dungeon, player, items, enemies);
            } else {
                enemyRate++;
            }

            clearScreen();

            // check paths around player
            boolean[] around = checkAround(dungeon, playerCurr);

            // check enemy rate, if high, battle
            if (enemyRate >= enemyThreshold) {
                battleSystem(player, false, playerCurr, enemies, dungeon);
            }

            // UI
            // clearScreen();
            System.out.println("Enemy Rate " + enemyRate + " / " + enemyThreshold);
            printDivision();
            dungeonTraverse(player, dungeon, playerCurr);
            printDivision();

            // Check if player has compass and print it out
            if (player.getHasCompass()) {
                printCompass(checkAround(dungeon, playerCurr));
            }
            System.out.println("playerCurr is: " + playerCurr[0] + "," + playerCurr[1]);

            // Comment out if you want to see the player current location
            System.out.println("Hp: " + player.getHp());
            System.out.println("Keys: " + player.getKeys());
            System.out.println("Type \"help\" to see controls");
            System.out.print(">");
            move = scan().split(" ")[0];

            if (isInList(move, new String[] { "w", "a", "s", "d" })) {
                System.out.println("moving...");
                System.out.println("coordinates: " + playerCurr[0] + " " + playerCurr[1]);
                movePlayer(move, playerCurr, around);
            } else if (move.equals("i")) {
                clearScreen();
                inventory(player, move);
                enter();
            } else if (move.equals("c")) {
                clearScreen();
                System.out.println(player.toString());
                enter();
            } else if (move.equals("q")) {
                // check if health stuff available
                healSystem();
                enter();
            } else if (move.equals("e")) {
                return;
            } else if (move.equals("help")) {
                clearScreen();
                System.out.println(
                        "Conrols:\r\n\r\n   help: display this menu\r\n\r\n   w: move up\r\n   a: move left\r\n   s: move down\r\n   d: move right\r\n\r\n   i: show player inventory\r\n   c: check status\r\n   q: heal\r\n   e: exit\r\n");
                enter();
            } else {
                invalid();
                enter();
            }
        }
    }

    // HELPER: checks if player has the stuff needed to be healed. If they do, heals them
    public static void healSystem() {
        // check if player has the stuff needed for healing
        // check inventory??

        // if they do, heal
        // if they don't, don't heal
        // print a message for each 
    }

    // HELPER: check if player can heal themselves
    public static void hasHearts()

    /*
     * If player is defeated, then game ends else it will print a congratulations
     * message and game ends.
     */
    public static void gameOver(boolean end) {
        clearScreen();
        System.out.println("GAME OVER!");
        if (end) {
            System.out.println("Congrats ma boy! You are now a certified pro gamer.");
        } else {
            System.out.println("lol such a loser :>");
        }
    }

    /*
     * if map is found, it will show all the rooms of the dungeon,
     * hint: make sure to update the visitedRooms array
     * on the player attributes
     */

    public static void mapFound(String[][] dungeon, Player player) {
        // set has map to true
        player.setHasMap(true);
        // update player has visited to show all the possible paths
        for (int x = 0; x < dungeon.length; x++) {
            for (int y = 0; y < dungeon.length; y++) {
                if (!dungeon[x][y].equals("-1")) {
                    player.updateVisited(x, y);
                }
            }
        }
    }

    /*
     * Method that will traverse the dungeon
     * when the player has enter the room, it will update the visited array
     * to create a path of all the visited rooms
     */
    public static void dungeonTraverse(Player player, String[][] d, int[] curr) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                if (d[i][j].equals("-1")) {
                    System.out.print("   ");
                } else if (curr[0] == i && curr[1] == j) {
                    System.out.print("." + "  ");
                    player.updateVisited(i, j);
                } else if (player.getVisited()[i][j] == true) {
                    System.out.print(d[i][j] + "  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }

    /*
     * If player inputs "i" it will print the inventory, else if "ei" it will print
     * current equiped items and let them
     * equip items if available (If the item is in the inventory)
     */
    public static void inventory(Player player, String invType) {
        // functino is MERGED: inventory does both i and ei
        // show all of inventory
        System.out.println("----- INVENTORY -----");
        player.printInventory();

        // ask if they wanna equip anything
        System.out.print("Which one would you like to equip? (index) ");

        int index;
        try {
            index = Integer.parseInt(scan());
        } catch (NumberFormatException e) {
            System.out.println("Invalid response. That's not a number.");
            return;
        }

        Item[] itemArray = player.OnelineInventory(player.getInventory());
        // if index out of range, tell em, then quit
        if (index >= itemArray.length) {
            invalid();
        } else {
            Item selected = itemArray[index];
            player.updateEquip(index, selected.getType());
        }
    }

    /*
     * Method to check if the room has a special event
     * Rooms
     * -t -> treasure, make use of openTreasure method
     * -m -> map, update the character map attribute and make use of the mapFound
     * method
     * -c -> compass, update the character compass attribute, when back on the main
     * menu make sure to use printCompass if the attribute is true
     * -k -> key door, only available if the character has keys to open it, else it
     * will not let the character through
     * -b -> boss, it will call the battleSystem method and make the boss input
     * true, it will spawn a different enemy in battle.
     * -f -> fairy, it will let the character upgrade either shield or sword
     */
    public static void checkSpecial(String direction, int[] curr, String[][] dungeon, Player player, Item[] items,
            Enemy[] enemies) {
        clearScreen();
        // save room type
        String roomType = dungeon[curr[0]][curr[1]];
        // choose a type --> using switch
        switch (roomType) {
            case "t": {
                // treasure
                // check if treasure has notbeen discovered before
                System.out.println("----- TREASURE CHEST -----\n");
                openTreasure(items, dungeon, player);
                eliminateRoom(dungeon, curr);
                break;
            }
            case "m": {
                // map
                System.out.println("----- MAP -----\n");
                System.out.println("You have found a map! Now you can see all the rooms of the dungeon.\n");
                player.setHasMap(true);
                mapFound(dungeon, player);
                eliminateRoom(dungeon, curr);
                break;
            }
            case "c": {
                // compass
                System.out.println("----- COMPASS -----\n");
                System.out.println(
                        "You have found a compass! This allows you to see the walls and openings around you.\n");
                player.setHasCompass(true);
                eliminateRoom(dungeon, curr);
                break;
            }
            case "k": {
                // key door
                // uses one key from the player's inventory
                System.out.println("----- LOCKED DOOR -----\n");
                if (player.getKeys() == 0) {
                    System.out.println("YOU DON'T HAVE A KEY TO OPEN THE DOOR!\n");
                } else {
                    System.out.println("Used one key to open.");
                    player.useKey();
                    // eliminate the unlocked door
                    eliminateRoom(dungeon, curr);
                }
                break;
            }
            case "b": {
                System.out.println("----- BOSS -----\n");
                System.out.println("You have found a boss!\n");
                // room is eliminated inside the battle system only if player defea
                battleSystem(player, true, curr, enemies, dungeon);
                break;
            }
            case "f": {
                // fairy
                System.out.println("----- FAIRY -----\n");
                System.out.println(
                        "Wow, you found a fairy! The fairy can give you one of these two items. Choose wisely. \n");
                fairyRoom(player);
                // eliminate the current room
                eliminateRoom(dungeon, curr);
                break;
            }
        }

        // update visited to show that player has seen this room before
        player.updateVisited(curr[0], curr[1]);
        // always move player back after a special event
        moveBack(direction, curr);

        // only keep the screen if there's a special event going on
        if (isInList(roomType, new String[] { "t", "m", "c", "k", "b", "f" })) {
            enter();
        }
    }

    // HELPER: moves player to the previous location
    public static void moveBack(String direction, int[] curr) {
        // where to go based on each possible move
        switch (direction) {
            case "w": {
                changeCoords("s", curr);
                break;
            }
            case "a": {
                changeCoords("d", curr);
                break;
            }
            case "s": {
                changeCoords("w", curr);
                break;
            }
            case "d": {
                changeCoords("a", curr);
                break;
            }
        }
    }

    // HELPER: handle fairy functions
    public static void fairyRoom(Player player) {
        System.out.println("You found a fairy!");
        System.out.println("What do you want to upgrade?");
        System.out.println("1) sword");
        System.out.println("2) shield");
        String response = scan();
        if (response.equals("1")) {
            player.increaseSwordLevel();
            System.out.println("Your Sword was upgraded.");
        } else if (response.equals("2")) {
            player.increaseShieldLevel();
            System.out.println("Your shield was upgraded.");
        } else {
            System.out.println("that ain't a valid option.");
            System.out.println("just take the damn sword upgrade and leave");
            player.increaseSwordLevel();
        }
    }

    // HELPER: eliminate current room
    // used to make special events only appear once
    public static void eliminateRoom(String[][] dungeon, int[] curr) {
        dungeon[curr[0]][curr[1]] = "1";
    }

    /*
     * Method to check that the room is a special room, it will not increase the
     * enemy encounter if moved to
     * these rooms
     */
    public static boolean checkRoom(String[][] dungeon, int[] curr) {
        // check if room is anything in the allowed list
        String currentRoomType = dungeon[curr[0]][curr[1]];
        String[] normalRooms = { "1", "2", "3" };
        if (!inArray(currentRoomType, normalRooms)) {
            return true;
        }
        return false;
    }

    /*
     * Method that will be called once the player has found a treasure chest, it
     * will give random items and keys
     */
    public static void openTreasure(Item[] items, String[][] dungeon, Player player) {
        // generate random number form 0 to items length
        int randomIndex = random(items.length);

        // if the index is empty (item was used in another chest), generate another
        // random value
        Item randomItem = null;
        do {
            randomItem = items[randomIndex];
        } while (randomItem == null);

        System.out.println("You have found a " + randomItem.getName() + " and a key!");
        System.out.println();
        System.out.println("Name: " + randomItem.getName());
        System.out.println("Type: " + randomItem.getType());
        System.out.println("Power: " + randomItem.getPower());
        System.out.println();

        player.updateInventory(randomItem, false, randomItem.getType());
        // give the player one key
        player.addKeys(1);

        // LATER ON: add a screen to show what they got.
    }

    // FIRST AXIS COORDINATE IS VERTICAL

    /*
     * It will check around the area of the player and see if there is a path, if
     * there is then the player can move otherwise it won't
     */
    public static boolean[] checkAround(String[][] dungeon, int[] playerCurr) {
        boolean[] canMove = new boolean[4];
        int[][] newIndices = new int[][] {
                { playerCurr[0] - 1, playerCurr[1] }, // w
                { playerCurr[0], playerCurr[1] - 1 }, // a
                { playerCurr[0] + 1, playerCurr[1] }, // s
                { playerCurr[0], playerCurr[1] + 1 } // d
        };

        System.out.println("inside the move method");

        for (int i = 0; i < 4; i++) {
            int x = newIndices[i][0];
            int y = newIndices[i][1];
            // if it isn't -1
            canMove[i] = !dungeon[x][y].equals("-1");
        }

        return canMove;
    }

    /*
     * The main battle system after an enemy is encountered
     */
    public static int battleSystem(Player player, boolean boss, int[] curr, Enemy[] enemies, String[][] dungeon) {
        // TODO
        while (true) {

            // this will check whether the player of the enemies have won
            if (true) {
                break;
            }

            printDivision();
            // print the turns

            // print the enemy index
            printDivision();

            // print hp
            System.out.println("HP: " + player.getHp());

            // print the controls
            System.out.print(
                    "Choose an option:\r\n\r\n   q) Attack\r\n   w) Use Special Weapon\r\n   e) Use Weaopn\r\n   d) Defend\r\n   r) Heal\r\n   s) Escape\r\n\r\n> ");

            // get the input
            String response = scan();

            // check input against options
            if (response.equals("q")) {
                // attack
            } else if (response.equals("w")) {
                // use special weapon
            } else if (response.equals("e")) {
                // use weapon
            } else if (response.equals("d")) {
                // defend
            } else if (response.equals("r")) {
                // heal
            } else if (response.equals("s")) {
                // escape
            } else {
                invalid();
            }
            enter();
        }
        return 0;
    }

    /*
     *
     * For UI purposes
     * 
     */

    /*
     * To print a division in the menu
     */
    public static void printDivision() {
        String line = "";
        for (int i = 0; i < 100; i++) {
            line += "-";
        }
        System.out.println(line);
    }

    /*
     * To clear the console screen, if giving issues just comment or dont call the
     * method
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void enter() {
        System.out.println("Press Enter to continue...");
        scan();
    }

    /*
     * To get a random value, used mainly on random encounter rate
     */
    public static int getRandomVal() {
        Random rand = new Random();
        return rand.nextInt(5, 16);
    }

    /*
     * When the item compass is found, it will print the compass and the paths that
     * the user can move to
     */
    public static void printCompass(boolean[] isPath) {
        if (isPath[0] == true) {
            System.out.print("  !\n");
        } else {
            System.out.println(" ");
        }
        System.out.println("  N");

        if (isPath[1] && isPath[3]) {
            System.out.print("!W E!\n");
        } else if (!isPath[1] && !isPath[3]) {
            System.out.print(" W E \n");
        } else {
            String lr = (isPath[1] && !isPath[3]) ? "!W E " : " W E!";
            System.out.println(lr);
        }
        System.out.println("  S");
        if (isPath[2] == true) {
            System.out.print("  !\n");
        } else {
            System.out.println(" ");
        }
    }

    // HELPER: custom random function
    // generates a random integer from 0 to upper bound
    // much more useful than the original random function
    public static int random(int upperBound) {
        Random r = new Random();
        return (int) (r.nextDouble() * upperBound);
    }

    // HELPER: check if a string is in a string array
    public static boolean inArray(String s, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    // HELPER: easy way to get input;
    public static String scan() {
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    // HELPER: show error when response is invalid
    public static void invalid() {
        System.out.println("Invalid response!");
    }

    // HELPER: moves the player only if around conditions are met
    public static void movePlayer(String direction, int[] curr, boolean[] around) {
        String[] possible = { "w", "a", "s", "d" };
        String[] validList = new String[4];

        // DEBUG
        System.out.println("coordinates: " + curr[0] + " " + curr[1]);

        // check which inputs are valid
        for (int i = 0; i < around.length; i++) {
            // DEBUG
            System.out.println(around[i]);

            if (around[i]) {
                validList[i] = possible[i];
            }
        }

        // only run the rest if input is valid
        if (isInList(direction, validList)) {
            changeCoords(direction, curr);
        }
    }

    // HELPER: handles the actual moving of the player
    public static void changeCoords(String direction, int[] curr) {
        switch (direction) {
            case "w": {
                curr[0] = curr[0] - 1;
                break;
            }
            case "a": {
                curr[1] = curr[1] - 1;
                break;
            }
            case "s": {
                curr[0] = curr[0] + 1;
                break;
            }
            case "d": {
                curr[1] = curr[1] + 1;
                break;
            }
        }
    }

    // HELPER: searchs for a string value in an array
    public static boolean isInList(String x, String[] arr) {
        boolean result = false;
        for (String element : arr) {
            if (element == null) {
                continue;
            }
            if (element.equals(x)) {
                result = true;
            }
        }
        return result;
    }
}