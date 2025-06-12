import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ContactAppGUI {
    private static AVLTree contactTree = new AVLTree();
    private static final String FILE_PATH = "contacts.txt";

    public static void main(String[] args) {
        contactTree.loadFromFile(FILE_PATH);

        JFrame frame = new JFrame("📒 Contact Book (AVL Tree)");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("📇 Contact Book");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));

        JButton addBtn = styledButton("➕ Add Contact");
        JButton searchBtn = styledButton("🔍 Search Contact");
        JButton deleteBtn = styledButton("❌ Delete Contact");
        JButton viewAllBtn = styledButton("📜 View All Contacts");

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(addBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(searchBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(deleteBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(viewAllBtn);

        frame.add(panel);
        frame.setVisible(true);

        addBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            Object[] fields = { "Name:", nameField, "Phone:", phoneField, "Email:", emailField };

            int option = JOptionPane.showConfirmDialog(frame, fields, "Add New Contact", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Name and Phone are required.");
                    return;
                }
                contactTree.insert(name, phone, email);
                JOptionPane.showMessageDialog(frame, "Contact added successfully!");
            }
        });

        searchBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter Name to Search:");
            if (name != null) {
                Contact contact = contactTree.search(name);
                if (contact != null) {
                    JOptionPane.showMessageDialog(frame, "Name: " + contact.name +
                            "\nPhone: " + contact.phone +
                            "\nEmail: " + contact.email);
                } else {
                    JOptionPane.showMessageDialog(frame, "Contact not found.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter Name to Delete:");
            if (name != null && !name.isEmpty()) {
                contactTree.delete(name);
                JOptionPane.showMessageDialog(frame, "Contact deleted (if existed).");
            }
        });

        viewAllBtn.addActionListener(e -> {
            ArrayList<Contact> contacts = new ArrayList<>();
            collectInOrder(contactTree.getRoot(), contacts);
            if (contacts.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No contacts available.");
            } else {
                StringBuilder builder = new StringBuilder();
                for (Contact c : contacts) {
                    builder.append("Name: ").append(c.name)
                            .append("\nPhone: ").append(c.phone)
                            .append("\nEmail: ").append(c.email)
                            .append("\n\n");
                }
                JTextArea area = new JTextArea(builder.toString());
                area.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(area);
                scrollPane.setPreferredSize(new Dimension(300, 200));
                JOptionPane.showMessageDialog(frame, scrollPane, "All Contacts", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                contactTree.saveToFile(FILE_PATH);
            }
        });
    }

    private static void collectInOrder(Contact node, ArrayList<Contact> list) {
        if (node != null) {
            collectInOrder(node.left, list);
            list.add(node);
            collectInOrder(node.right, list);
        }
    }

    private static JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(250, 40));
        button.setMaximumSize(new Dimension(250, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
