import java.io.*;

public class AVLTree {
    private Contact root;

    public Contact getRoot() {
        return root;
    }

    private int height(Contact N) {
        return (N == null) ? 0 : N.height;
    }

    private int getBalance(Contact N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }

    private Contact rightRotate(Contact y) {
        Contact x = y.left;
        Contact T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Contact leftRotate(Contact x) {
        Contact y = x.right;
        Contact T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public void insert(String name, String phone, String email) {
        root = insertRec(root, new Contact(name, phone, email));
    }

    private Contact insertRec(Contact node, Contact newNode) {
        if (node == null)
            return newNode;

        if (newNode.name.compareTo(node.name) < 0)
            node.left = insertRec(node.left, newNode);
        else if (newNode.name.compareTo(node.name) > 0)
            node.right = insertRec(node.right, newNode);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        if (balance > 1 && newNode.name.compareTo(node.left.name) < 0)
            return rightRotate(node);

        if (balance < -1 && newNode.name.compareTo(node.right.name) > 0)
            return leftRotate(node);

        if (balance > 1 && newNode.name.compareTo(node.left.name) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && newNode.name.compareTo(node.right.name) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void delete(String name) {
        root = deleteRec(root, name.toLowerCase());
    }

    private Contact deleteRec(Contact root, String name) {
        if (root == null)
            return root;

        if (name.compareTo(root.name) < 0)
            root.left = deleteRec(root.left, name);
        else if (name.compareTo(root.name) > 0)
            root.right = deleteRec(root.right, name);
        else {
            if ((root.left == null) || (root.right == null)) {
                Contact temp = (root.left != null) ? root.left : root.right;
                return temp;
            } else {
                Contact temp = minValueNode(root.right);
                root.name = temp.name;
                root.phone = temp.phone;
                root.email = temp.email;
                root.right = deleteRec(root.right, temp.name);
            }
        }

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Contact minValueNode(Contact node) {
        Contact current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public Contact search(String name) {
        return searchRec(root, name.toLowerCase());
    }

    private Contact searchRec(Contact node, String name) {
        if (node == null || node.name.equals(name))
            return node;

        if (name.compareTo(node.name) < 0)
            return searchRec(node.left, name);
        else
            return searchRec(node.right, name);
    }

    public boolean updateContact(String name, String newPhone, String newEmail) {
        Contact contact = search(name);
        if (contact != null) {
            contact.phone = newPhone;
            contact.email = newEmail;
            return true;
        }
        return false;
    }

    public void printInOrder() {
        printInOrderRec(root);
    }

    private void printInOrderRec(Contact node) {
        if (node != null) {
            printInOrderRec(node.left);
            System.out.println("Name: " + node.name + ", Phone: " + node.phone + ", Email: " + node.email);
            printInOrderRec(node.right);
        }
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            saveInOrderToFile(root, writer);
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    private void saveInOrderToFile(Contact node, BufferedWriter writer) throws IOException {
        if (node != null) {
            saveInOrderToFile(node.left, writer);
            writer.write(node.name + "," + node.phone + "," + node.email);
            writer.newLine();
            saveInOrderToFile(node.right, writer);
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3)
                    insert(parts[0], parts[1], parts[2]);
            }
        } catch (FileNotFoundException e) {
            // It's fine if file not exists
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }
    }
}
