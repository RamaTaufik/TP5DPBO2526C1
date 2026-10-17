import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PersonMenu {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel idFieldLabel;
    private JTextField idField;
    private JLabel nameFieldLabel;
    private JTextField nameField;
    private JLabel birthYearFieldLabel;
    private JTextField birthYearField;
    private JLabel categoryComboBoxLabel;
    private JComboBox categoryComboBox;
    private JLabel levelSliderLabel;
    private JSlider levelSlider;
    private JTable peopleTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JLabel levelSliderCurLabel;

    private int selectedIndex = -1;
    private ArrayList<Person> listPeople;

    // Fungsi helper statis untuk merubah string menjadi integer sekaligus memastikan input hanya merupakan bilangan
    // bulat positif
    public static int cstrtoint(String str) {
        int num = 0;

        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) < '0' || str.charAt(i) > '9') {
                // Karena semua atribut integer dipastikan hanya bilangan bulat positif, maka hanya digit berupa angka 0 sampai 9
                // yang akan diterima, sehingga fungsi ini juga sekaligus menjadi filter input
                return -1;
            } else {
                num = (num * 10) + (str.charAt(i) - '0');
            }
        }

        return num;
    }

    public PersonMenu() {
        listPeople = new ArrayList<>();

        populateList();

        peopleTable.setModel(setTable());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setText("Combatant List");

        String[] categories = {"---", "Saber", "Lancer", "Archer", "Rider", "Caster", "Assassin", "Berserker"};
        categoryComboBox.setModel(new DefaultComboBoxModel<>(categories));
        levelSlider.setMinimum(1);
        levelSlider.setMaximum(100);
        levelSlider.setValue(1);
        levelSliderCurLabel.setText("1");

        deleteButton.setVisible(false);

        levelSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(String.valueOf(levelSlider.getValue()) != levelSliderCurLabel.getText()) {
                    levelSliderCurLabel.setText(String.valueOf(levelSlider.getValue()));
                }
            }
        });
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Yakin hapus?") == 0) {
                    deleteData();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();

                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curName = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curBirthY = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curCategory = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                int curLevel = Integer.parseInt(peopleTable.getModel().getValueAt(selectedIndex, 5).toString());

                idField.setText(curId);
                nameField.setText(curName);
                birthYearField.setText(curBirthY);
                categoryComboBox.setSelectedItem(curCategory);
                levelSlider.setValue(curLevel);

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Tahun Lahir", "Kategori", "Level"};
        DefaultTableModel temp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for(int i = 0; i < listPeople.size(); i++) {
            Object[] row = {
                    i + 1,
                    listPeople.get(i).getId(),
                    listPeople.get(i).getName(),
                    listPeople.get(i).getBirthYear(),
                    listPeople.get(i).getCategory(),
                    listPeople.get(i).getLevel()
            };
            temp.addRow(row);
        }

        return temp;
    }

    public void insertData() {
        String e_msg = "";

        try {
            String id = idField.getText();
            if(id.isBlank()) {
                e_msg = "ID tidak boleh kosong!";
                throw new IllegalArgumentException();
            }

            String name = nameField.getText();
            if(name.isBlank()) {
                e_msg = "Nama tidak boleh kosong!";
                throw new IllegalArgumentException();
            }

            int birth_y = cstrtoint(birthYearField.getText().trim());
            if(birth_y == -1) {
                e_msg = "Tahun lahir harus berupa bilangan bulat positif!";
                throw new IllegalArgumentException();
            }

            String category = categoryComboBox.getSelectedItem().toString();

            int level = levelSlider.getValue();

            listPeople.add(new Person(id, name, birth_y, category, level));
            peopleTable.setModel(setTable());

            clearForm();

            System.out.println("Inserted successfully");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, e_msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        String e_msg = "";

        try {
            String id = idField.getText();
            if(id.isBlank()) {
                e_msg = "ID tidak boleh kosong!";
                throw new IllegalArgumentException();
            }

            String name = nameField.getText();
            if(name.isBlank()) {
                e_msg = "Nama tidak boleh kosong!";
                throw new IllegalArgumentException();
            }

            int birth_y = cstrtoint(birthYearField.getText().trim());
            if(birth_y == -1) {
                e_msg = "Tahun lahir harus berupa bilangan bulat positif!";
                throw new IllegalArgumentException();
            }

            String category = categoryComboBox.getSelectedItem().toString();

            int level = levelSlider.getValue();

            listPeople.get(selectedIndex).setId(id);
            listPeople.get(selectedIndex).setName(name);
            listPeople.get(selectedIndex).setBirthYear(birth_y);
            listPeople.get(selectedIndex).setCategory(category);
            listPeople.get(selectedIndex).setLevel(level);
            peopleTable.setModel(setTable());

            clearForm();

            System.out.println("Updated successfully");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, e_msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        listPeople.remove(selectedIndex);
        peopleTable.setModel(setTable());

        clearForm();

        System.out.println("Deleted successfully");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        birthYearField.setText("");
        categoryComboBox.setSelectedIndex(0);
        levelSlider.setValue(1);

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    public void populateList() {
        listPeople.add(new Person("SBR-001", "Lord Logres", 480, "Saber", 90));
        listPeople.add(new Person("ARC-001", "Super Orion", 1200, "Archer", 89));
        listPeople.add(new Person("LNC-001", "Romulus-Quirinus", 753, "Lancer", 75));
        listPeople.add(new Person("RDR-001", "Noah", 450, "Rider", 80));
        listPeople.add(new Person("CST-001", "King Solomon", 970, "Caster", 90));
        listPeople.add(new Person("ASS-001", "Hassan-i Sabbah", 1050, "Assassin", 90));
        listPeople.add(new Person("BRK-001", "Hercules", 2010, "Berserker", 80));
    }

    static void main(String[] args) {
        JFrame frame = new JFrame("Chaldea Database");
        PersonMenu personMenuInstance = new PersonMenu();

        frame.setContentPane(personMenuInstance.mainPanel);

        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
