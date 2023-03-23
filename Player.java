import java.lang.invoke.TypeDescriptor.OfMethod;
import java.util.Random;
import java.util.Scanner;

public class Player {
    // attrs
    private String name;
    private int swordLevel;
    private int shieldLevel;
    private int maxhp = 5;
    private int hp;
    private int keys;
    private boolean hasMap;
    private boolean hasCompass;
    private boolean[][] hasVisited;
    private Item[][] inventory = new Item[3][3];
    private int[] usableCount = new int[10];
    private Item[] equipInventory; // -> holds all the equipped items

    // constructors
    public Player(int swordLevel, int shieldLevel, int hp, boolean hasMap, boolean hasCompass) {
        this.swordLevel = swordLevel;
        this.shieldLevel = shieldLevel;
        this.hp = hp;
        this.hasMap = hasMap;
        this.hasCompass = hasCompass;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public int getSwordLevel() {
        return this.swordLevel;
    }

    public int getShieldLevel() {
        return this.shieldLevel;
    }

    public int getHp() {
        return this.hp;
    }

    public int getMaxhp() {
        return this.maxhp;
    }

    public int getKeys() {
        return this.keys;
    }

    public boolean getHasMap() {
        return this.hasMap;
    }

    public boolean getHasCompass() {
        return this.hasCompass;
    }

    public boolean[][] getVisited() {
        return this.hasVisited;
    }

    public Item[][] getInventory() {
        return this.inventory;
    }

    public Item[] getEquipInventory() {
        return this.equipInventory;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSwordLevel(int swordLevel) {
        this.swordLevel = swordLevel;
    }

    public void increaseSwordLevel() {
        this.swordLevel++;
    }

    public void setShieldLevel(int shieldLevel) {
        this.shieldLevel = shieldLevel;
    }

    public void increaseShieldLevel() {
        this.shieldLevel++;
    }

    public void setMaxhp(int maxhp) {
        this.maxhp = maxhp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public void addKeys(int num) {
        this.keys += num;
    }

    public void useKey() {
        this.keys--;
    }

    public void setHasMap(boolean hasMap) {
        this.hasMap = hasMap;
    }

    public void setHasCompass(boolean hasCompass) {
        this.hasCompass = hasCompass;
    }

    public void setVisited(boolean[][] hasVisited) {
        this.hasVisited = hasVisited;
    }

    public void setEquipInventory(Item[] equipInventory) {
        this.equipInventory = equipInventory;
    }

    /*
     * 
     * Methods
     * 
     */

    /*
     * To create the player info into a string
     */
    public String toString() {
        return "sword level: " + swordLevel + "\n" +
                "shield level: " + shieldLevel + "\n" +
                "max health: " + maxhp + "\n" +
                "health: " + hp + "\n" +
                "keys: " + keys + "\n" +
                "has hap? " + hasMap + "\n" +
                "has compass? " + hasCompass;
    }

    /*
     * To update the inventory if the item does not exist, if it does exist and its
     * usable type,
     * then increase the amount
     */
    public void updateInventory(Item itemIn, boolean increase, String type) {
        // 0 = weapon, 1 = sweapon, 2 = usable

        if (type.equals("weapon")) {
            // if type weapon, put in first row
            inventory[0][invNullIndex(0)] = itemIn;
        } else if (type.equals("sWeapon")) {
            // if type sweapon, put in second row
            inventory[1][invNullIndex(1)] = itemIn;
        } else if (type.equals("usable")) {
            // if type usable, put in third row
            // check if item exists
            boolean itemExists = itemExists(itemIn.getName(), 2);
            int itemIndex = -1;
            if (!itemExists) {
                // add item to inventory list if it doesn't exist
                itemIndex = invNullIndex(2);
                inventory[2][itemIndex] = itemIn;
            }
            // get index, increment the number in the other one
            usableCount[itemIndex]++;
        }
    }

    /*
     * To check the inventory and see if the item exists
     */
    public boolean itemExists(String name, int type) {
        boolean itemExists = false;
        for (int i = 0; i < inventory[type].length; i++) {
            if (inventory[type][i] == null) {
                continue;
            }
            if (inventory[type][i].getName().equals(name)) {
                itemExists = true;
                break;
            }
        }
        return itemExists;
    }

    /*
     * To get the item that can be used
     */
    public Item getUsable(String name) {
        Item toReturn = new Item("", "", 0);
        for (int i = 0; i < inventory[2].length; i++) {
            if (inventory[2][i].getName().equals(name)) {
                toReturn = inventory[2][i];
                break;
            }
        }
        return toReturn;
    }

    /*
     * To print all the equpiment that the player has
     */
    public void printEquip() {
        for (int i = 0; i < equipInventory.length; i++) {
            System.out.println(equipInventory[i]);
        }
    }

    // HELPER: print Item array
    public void printItemArr(Item[] arr) {
        String toPrint = "";
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                toPrint += ("[" + i + "]" + "  " +
                        arr[i].getName() + "  " +
                        arr[i].getType() + "  " +
                        arr[i].getPower()) + "\n";
            }
        }
        System.out.println(toPrint);
    }

    /*
     * To equip a weapon or a special weapon
     */
    public void updateEquip(int index, String type) {
        int typeNum = typePos(type);
        equipInventory[typeNum] = OnelineInventory(inventory)[index];
    }

    // HELPER: returns a row number based on item type
    public int typePos(String type) {
        switch (type) {
            case "weapon":
                return 0;
            case "sWeapon":
                return 1;
            case "usable":
                return 2;
            default:
                return -1;
        }
    }

    /*
     * Get the item equipped and use it in battle
     */
    public Item useEquip(int type) {
        return equipInventory[type];
    }

    /*
     * 
     */
    public void printInventory() {
        printItemArr(OnelineInventory(inventory));
    }

    /*
     * To update the visited attribute
     */
    public void updateVisited(int x, int y) {
        hasVisited[x][y] = true;
    }

    /*
     * to update Max health if health container is found
     */
    public void updateMaxHealth() {
        maxhp += 10;
    }

    /*
     * To heal, make sure that the Health Points <= maxHealth
     */
    public void heal() {
        if (maxhp - 10 < hp) {
            hp = maxhp;
        } else {
            hp += 10;
        }
    }

    /*
     * When an enemy is defeated, check for its dropped item.
     * If its an endgame item, it will return true
     */
    public boolean checkDrop(Enemy en) {
        if (en.getItem() == "endgame") {
            return true;
        }
        return false;
    }

    // HELPER: finds the first null cell in an inventory row
    private int invNullIndex(int type) {
        int nullIndex = -1;
        for (int i = 0; i < inventory[type].length; i++) {
            if (inventory[type][i] == null) {
                nullIndex = i;
                break;
            }
        }
        return nullIndex;
    }

    // HELPER: find item location
    public int[] itemLocation(String itemName) {
        for (int x = 0; x < inventory.length; x++) {
            for (int y = 0; y < inventory[0].length; y++) {
                if (inventory[x][y].getName().equals(itemName)) {
                    return new int[] { x, y };
                }
            }
        }
        return new int[] { -1, -1 };
    }

    // HELPER: get 1d inventory
    public Item[] OnelineInventory(Item[][] inventory) {
        Item[] OneInv = new Item[9];
        for (int i = 0; i < 3; i++) {
            OneInv[i] = inventory[0][i];
        }
        for (int i = 0; i < 3; i++) {
            OneInv[i + 3] = inventory[1][i];
        }
        for (int i = 0; i < 3; i++) {
            OneInv[i + 6] = inventory[2][i];
        }
        return OneInv;
    }
}
