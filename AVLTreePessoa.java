import java.util.ArrayList;
import java.util.List;

class Node<T> {
    T data;
    int height;
    Node<T> left;
    Node<T> right;
    Node<T> parent;

    public Node(T data) {
        this.data = data;
        this.height = 1;
    }
}

public class AVLTreePessoa<T extends Comparable<T>> {
    Node<T> root;  // The root node of the tree

    public int getHeight(Node<T> node) {
        if (node == null) {
            return 0;
        }

        return node.height;
    }

    private void setHeight(Node<T> node) {
        if (node == null) {
            return;
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    public Node<T> getRoot() {
        return this.root;
    }

    private int getBalanceFactor(Node<T> node) {
        if (node == null) {
            return 0;
        }

        // return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
        return getHeight(node.left) - getHeight(node.right);
    }

    public int getTotalNodes(Node<T> node) {
        if (node == null) {
            return 0;
        }

        int left = getTotalNodes(node.left);
        int right = getTotalNodes(node.right);
        
        return 1 + left + right;
    }

    public Node<T> maximum(Node<T> node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }


    public Node<T> minimum(Node<T> node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Just testing
    public Node<T> minimumRec(Node<T> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node;
        } else {
            return minimumRec(node.left);
        }
    }

    public Node<T> successor(Node<T> node) {
        if (node == null) {
            return null;
        }

        if (node.right != null) {
            return minimum(node.right);
        }

        // Case node has no right child
        Node<T> y = node.parent; // get the parent of the node
        // while it has a parent and the current node is child on the right
        while (y != null && node == y.right) {
            // go up the tree
            node = y;
            y = y.parent;
        }
        return y;
    }

    public Node<T> predecessor(Node<T> node) {
        if (node == null) {
            return null;
        }

        if (node.left != null) {
            return maximum(node.left);
        }

        Node<T> y = node.parent;
        while (y != null && node == node.left) {
            node = y;
            y = y.parent;
        }
        return y;
    }

    public String nodeDataToString(Node<T> node) {
        return node == null ? "" : node.data.toString();
    }

    public boolean nodeIsNull(Node<T> node) {
        return node == null ? true : false;
    }

    public boolean treeIsEmpty(AVLTreePessoa<T> tree) {
        return tree.getRoot() == null ? true : false;
    }

    public boolean nodesEquals(Node<T> node1, Node<T> node2) {
        return node1 == node2 ? true : false;
    }

    /* These fn returns the data in an Optional[data] format, use the nodeDataToString instead */
    /*
    public Optional<T> getNodeData(Node<T> node) {
        return node == null ? Optional.empty() : Optional.of(node.data);
    }

    public Optional<T> getRootData() {
        if (this.root == null) {
            return Optional.empty();
        }
        return Optional.of(this.root.data);
    }

    public Optional<T> getMaximumData(Node<T> node) {
        if (node == null) {
            return Optional.empty();
        }
        Node<T> max = maximum(node);
        return Optional.of(max.data);
    }

    public Optional<T> getMinimumData(Node<T> node) {
        if (node == null) {
            return Optional.empty();
        }
        Node<T> min = minimum(node);
        return Optional.of(min.data);
    }

    public Optional<T> getSuccessorData(Node<T> node) {
        if (node == null) {
            return Optional.empty();
        }
        Node<T> successor = successor(node);
        if (successor == null) {     // Case tree has no right nodes, it will return the parent in
            return Optional.empty(); // the called function, if the parent is null a null pointer
        } else {                     // exception will occur without this if
            return Optional.of(successor.data);
        }
    }

    public Optional<T> getPredecessorData(Node<T> node) {
        if (node == null) {
            return Optional.empty();
        }
        Node<T> predecessor = predecessor(node);
        if (predecessor == null) {   // Case tree has no left nodes, it will return the parent in
            return Optional.empty(); // the called function, if the parent is null a null pointer
        } else {                     // exception will occur without this if
            return Optional.of(predecessor.data); 
        }
    }
    */

    public void printTree(Node<T> node) {
        printTreeRec(node, 0);
    }

    private void printTreeRec(Node<T> node, int level) {
        if (node != null) {
            // Print spaces proportional to the level
            for (int i = 0; i < level; i++) {
                System.out.print("    ");
            }

            // Print node data, its height, balance factor, and parent
            System.out.println(
                node.data + " Height: " + getHeight(node) +
                " Balance Factor: " + getBalanceFactor(node) +
                " Parent: " + ((node.parent == null) ? "null" : node.parent.data)
            );

            int nextLevel = level + 1;

            // Recursively print left and right subtrees, with increased level
            if (node.left != null) {
                printTreeRec(node.left, nextLevel);
            } else { // If node.left is null, print a _ in the place
                for (int i = 0; i < nextLevel; i++) {
                    System.out.print("    ");
                }
                System.out.println("_");
            }

            if (node.right != null) {
                printTreeRec(node.right, nextLevel);
            } else { // If node.right is  is null, print a _ in the place
                for (int i = 0; i < nextLevel; i++) {
                    System.out.print("    ");
                }
                System.out.println("_");
            }
        }
    }

    public boolean search(T data) {
        return iterSearch(root, data);
    }

    private boolean iterSearch(Node<T> node, T data) {
        while (node != null) {
            if (data.compareTo(node.data) == 0) {
                return true;
            } else if (data.compareTo(node.data) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    private boolean searchRec(Node<T> node, T data) {
        if (node == null) {
            return false;
        }

        if (node.data.compareTo(data) == 0) {
            return true;
        }

        /*
        if (searchRec(node.left, data) == false) {
            return searchRec(node.right, data);
        } else {
            return true;
        }
        */
        return searchRec(node.left, data) || searchRec(node.right, data);
    }

    // Search fn returning the node to use in other functions in middle of tree, like find the 
    // minimum of a sub-tree.
    public Node<T> searchNode(T data) {
        return iterSearchNode(root, data);
    }

    private Node<T> searchNodeRec(Node<T> node, T data) {
        if (node == null) {
            return null;
        }

        if (data.compareTo(node.data) == 0) {
            return node;
        }

        if (data.compareTo(node.data) < 0) {
            return searchNodeRec(node.left, data);
        } else {
            return searchNodeRec(node.right, data);
        }
    }

    private Node<T> iterSearchNode(Node<T> node, T data) {
        while (node != null) {
            if (data.compareTo(node.data) == 0) {
                return node;
            } else if (data.compareTo(node.data) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }

    public void insert(T data) {
        root = insertRec(root, data, null);
        //insertIter(data);
    }

    private Node<T> insertRec(Node<T> node, T data, Node<T> parent) {
        // If the tree/subtree is empty, create a new node
        if (node == null) {
            Node<T> newNode = new Node<>(data);
            newNode.parent = parent;
            return newNode;
        }

        if (search(data)) {
            System.out.println("*** Duplicates not allowed: " + data + ". ***");
            return node;
        }

        int cmp = data.compareTo(node.data);

        if (cmp < 0) {
            // If the data is less than the node's data, go to the left subtree
            node.left = insertRec(node.left, data, node); // node is now the parent of the newnode
        } else if (cmp > 0) {                             // as we go down the tree
            // If the data is greater than the node's data, go to the right subtree
            node.right = insertRec(node.right, data, node);
        }

        setHeight(node);
        return rebalance(node);
    }

    private void insertIter(T data) {
        if (root == null) {
            root = new Node<>(data);
            return;
        }

        Node<T> node = root;
        Node<T> p = null;

        while (node != null) {
            p = node;
            int cmp = data.compareTo(node.data);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return;
            }
        }

        Node<T> newNode = new Node<>(data);
        newNode.parent = p;

        if (data.compareTo(p.data) < 0) {
            p.left = newNode;
        } else {
            p.right = newNode;
        }

        node = newNode;
        p = newNode.parent;

        while (p != null) {
            setHeight(p);
            p = rebalance(p);

            node = p;
            p = node.parent;
        }

        while (root.parent != null) {
            root = root.parent;
        }

        /*
        TREE-INSERT(T, Z)
        1 y = NIL
        2 x = T.root
        3 while x != NIL
        4   y = x
        5   if Z.key < x.key
        6       x = x.left
        7   else x = x.right
        8 Z.p = y
        9 if y == NIL
       10   T.root = Z  // tree T was empty
       11 elseif Z.key < y.key
       12   y.left = Z
       13 else y.right = Z
       */
    }

    public void remove(T data) {
        root = removeRec(root, data);
    }

    // Copy
    private Node<T> removeRec(Node<T> node, T data) {
        if (!search(data)) {
            System.out.println("*** Data not found. ***");
            return node;
        }

        if (node == null) {
            return node;
        }

        int cmp = data.compareTo(node.data);

        if (cmp < 0) { // Traverse left in the tree
            node.left = removeRec(node.left, data);
        } else if (cmp > 0) { // Traverse right in the tree
            node.right = removeRec(node.right, data);
        } else { // Node found
            if (node.left == null || node.right == null) { // Node with only one or no child
                Node<T> temp = (node.left != null) ? node.left : node.right;

                if (temp == null) { // No child case
                    node = null;
                } else { // One child case
                    temp.parent = node.parent; // Update parent 
                    node = temp; // Node to be "deleted" will be equal to either right or left
                }

            } else { // Two children deletion
                Node<T> temp = minimum(node.right); // The successor to right of the deleted node
                node.data = temp.data; // Assign the data in the temp to the node to be "deleted"
                node.right = removeRec(node.right, temp.data); // remove the successor of the node
            }
        }

        setHeight(node);
        return rebalance(node);
    }
    
    // Remove an entire sub-tree
    public void massRemove(T data) {
        root = massRemoveRec(root, data);
    }
    public Node<T> massRemoveRec(Node<T> node, T data) {
        if (!search(data)) {
            System.out.println("*** Data not found. ***");
            return node;
        }

        if (node == null) {
            return node;
        }

        int cmp = data.compareTo(node.data);

        if (cmp < 0) {
            node.left = massRemoveRec(node.left, data);
        } else if (cmp > 0) {
            node.right = massRemoveRec(node.right, data);
        } else { // Node found
            //node = null; // Remove as if they are leaf
            return null; // returned null is assigned to left or right reference of the parent.
        }
        
        setHeight(node);
        return rebalance(node);
    }

    public void preorder() {
        preorderRec(root);
        System.out.println("");
    }

    private void preorderRec(Node<T> node) {
        if (node != null) {
            System.out.print(node.data + " "); // Visit node
            preorderRec(node.left);            // Visit left subtree
            preorderRec(node.right);           // Visit right subtree
        }
    }

    public void postorder() {
        postorderRec(root);
        System.out.println("");
    }

    private void postorderRec(Node<T> node) {
        if (node != null) {
            postorderRec(node.left);           // Visit left subtree
            postorderRec(node.right);          // Visit right subtree
            System.out.print(node.data + " "); // Visit node
        }
    }

    public void inorder() {
        inorderRec(root);
        System.out.println("");
    }

    private void inorderRec(Node<T> node) {
        if (node != null) {
            inorderRec(node.left);             // Visit left subtree
            System.out.print(node.data + " "); // Visit node
            inorderRec(node.right);            // Visit right subtree
        }
    }

    // Will return a subtree that matches with a given prefix, or null if tree is not of type str
    public Node<String> prefixMatch(String data, Class<T> type) {
        if (!String.class.isAssignableFrom(type)) {
            return null;
        }
        return prefixMatch((Node<String>) root, data);
    }

    public Node<String> prefixMatch(Node<String> node, String data) {
        if (node == null) {
            return null;
        }
        String nodeData = (String) node.data; // Explicit cast of node.data to a String
        int lengthOfData = data.length();
        String nodeDataSubstring = node.data.substring(0, lengthOfData);
        if (nodeData.startsWith(data)) {
            return node;
        }

        if (data.compareTo(nodeDataSubstring) < 0) {
            return prefixMatch(node.left, data);
        } else {
            return prefixMatch(node.right, data);
        }
    }

    public List<String> fuzzySearch(String data, int maxDistance, Class<T> type) {
        if (!String.class.isAssignableFrom(type)) {
            return null;
        }
        
        List<String> result = new ArrayList<>();
        fuzzySearch((Node<String>) root, data, maxDistance, result);
        return result;
    }

    private void fuzzySearch(Node<String> node, String data, int maxDistance, List<String> result) {
        if (node == null) {
            return;
        }
        
        int distance = levenshteinDistance(data, node.data);
        if (distance <= maxDistance) {
            result.add(node.data);
        }
        
        fuzzySearch(node.left, data, maxDistance, result);
        fuzzySearch(node.right, data, maxDistance, result);
    }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j; // j insertions
                } else if (j == 0) {
                    dp[i][j] = i; // i deletions
                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // No cost if characters are same
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], // Substitution
                                   Math.min(dp[i - 1][j],     // Deletion
                                            dp[i][j - 1]));   // Insertion
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    private Node<T> rebalance(Node<T> node) {
        int balanceFactor = getBalanceFactor(node);

        // Balancing tree
        // Left Left Case
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            //System.out.println("*** Performing right rotation on " + node.data + ". ***");
            return rightRotation(node);
        }

        // Right Right Case
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            //System.out.println("*** Performing left rotation on " + node.data + ". ***");
            return leftRotation(node);
        }

        // Left Right Case
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            //System.out.println(
            //    "*** Performing left-right rotation on " + node.left.data +
            //    " and " + node.data + ". ***"
            //);
            node.left = leftRotation(node.left);
            return rightRotation(node);
        }

        // Right Left Case
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            //System.out.println(
            //    "*** Performing right-left rotation on " + node.right.data +
            //    " and " + node.data + ". ***"
            //);
            node.right = rightRotation(node.right);
            return leftRotation(node);
        }
        return node;
    }

    /*
                4                          2
               / \                        / \
              2   6                      1   4
             / \               =>       /   / \
            1   3                     -1   3   6
           /
         -1
    4 = node                   | 2 = leftOfNode
    6 = node.right (unchanged) | 4 = leftOfNode.right (node)
    2 = node.left (leftOfNode) | 1 = leftOfNode.left
    1 = leftOfNode.left        | 3 = node.left
    3 = leftOfNode.right       | 6 = node.right
    */
    private Node<T> rightRotation(Node<T> node) { // 4 where imbalance happens
        Node<T> leftOfNode = node.left; // 2 the new root of subtree
        Node<T> rightOfLeftNode = leftOfNode.right; // 3 right of the left of node

        leftOfNode.right = node; // 4 goes to the left of 2
        node.left = rightOfLeftNode; // 3 goes to the left of 4

        // Update parents
        if (rightOfLeftNode != null) {
            rightOfLeftNode.parent = node; // 3 is now parent of 4
        }
        leftOfNode.parent = node.parent; // the parent of 4 is now the parent of 2
        node.parent = leftOfNode; // 2 is now parent of 4

        setHeight(node);
        setHeight(leftOfNode);

        return leftOfNode;
    }

    /*
                4                          6
               / \                        / \
              2   6                      4   8
                 / \           =>       / \   \
                5   8                  2   5   9
                     \
                      9
    4 = node                     | 6 = rightOfNode
    6 = node.right (rightOfNode) | 4 = leftOfNode.right (node)
    2 = node.left (unchanged)    | 8 = leftOfNode.left
    5 = rightOfNode.left         | 2 = node.left
    8 = rightOfNode.right        | 5 = node.right
    */
    private Node<T> leftRotation(Node<T> node) {
        Node<T> rightOfNode = node.right;
        Node<T> leftOfRightNode = rightOfNode.left;

        rightOfNode.left = node;
        node.right = leftOfRightNode;

        // Update parents
        if (leftOfRightNode != null) {
            leftOfRightNode.parent = node;
        }
        rightOfNode.parent = node.parent;
        node.parent = rightOfNode;

        setHeight(node);
        setHeight(rightOfNode);

        return rightOfNode;
    }

    // not used    
    private void transplant(Node<T> toBeTransplanted, Node<T> newRoot) {
        if (toBeTransplanted.parent == null) {
            this.root = newRoot;
        } else if (toBeTransplanted == toBeTransplanted.parent.left) {
            toBeTransplanted.parent.left = newRoot;
        } else {
            toBeTransplanted.parent.right = newRoot;
        }

        if (newRoot != null) {
            newRoot.parent = toBeTransplanted.parent;
        }
    }
}