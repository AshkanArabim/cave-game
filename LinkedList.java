public class LinkedList {
    // attributes
    public Node head;
    public int length = 0;
    public int enemies;

    // constructors
    public LinkedList() {
        this.head = null;
    }

    public LinkedList(Node head) {
        this.head = head;
    }

    public LinkedList(Node head, int length) {
        this.head = head;
        this.length = length;
    }

    /*
     * add node to list
     */
    public void add(Node node) {
        if (this.head == null) {
            this.head = node;
            this.length++;
        } else {
            Node temp = this.head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = node;
            node.next = null;
            this.length++;
        }
    }

    /*
     * delete first node and add to end
     * make use of add()
     */
    public void deleteAdd() {
        // remove first node
        Node temp = head;
        head = temp.next;

        // add to end
        add(temp);
    }

    /*
     * remove node from list
     */
    public void remove(Node node) {
        // set temp to the one before node
        Node temp = head;
        while (temp.next != node) {
            temp = temp.next;
        }
        // set temp's next to node's next
        temp.next = node.next;
    }

    /*
     * Print all the items in the list
     */
    public void printList() {
        Node temp = head;
        while (temp.next != null) {
            System.out.println(temp);
            temp = temp.next;
        }
        System.out.println(temp);
    }

    /*
     * Print all the enemies in the list
     */
    public void printEnemies() {
        Node temp = head;
        while (temp.next != null) {
            if (temp.enemy != null) {
                System.out.println(temp.enemy);
            }
            temp = temp.next;
        }
        if (temp.enemy != null) {
            System.out.println(temp.enemy);
        }
    }

    /*
     * Get the choosen enemy
     */
    public Node chooseEnemy(int index) {
        // TODO
        int iterator = 0;
        Node temp = head;
        while (iterator < index && temp.next != null) {
            if (temp.next.enemy != null) {
                iterator++;
            }
            temp = temp.next;
        }
        return temp;
    }

    /*
     * Check how many turns the player has, its done recursively
     */
    public int checkTurnsLeft(Node node) {
        // TODO
        int turns = 0;
        Node temp = head;
        while (temp.next != null) {
            if (temp.player != null) {
                turns++;
            }
            temp = temp.next;
        }
        if (temp.player != null) {
            turns++;
        }
        return turns;
    }
}
