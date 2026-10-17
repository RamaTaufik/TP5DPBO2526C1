import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

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
    private String selectedId = null;
    private Database database;

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

    // Fungsi helper yang membantu melakukan escape character pada query SQL. Karakter yang di-escape adalah ' dan \, karena
    // kedua karakter tersebut dapat mengacaukan query juga pemrosesan String di Java
    public static String escChar(String str) {
        return str.replace("\\", "\\\\").replace("'", "\\'");
    }

    public PersonMenu() {
        database = new Database();

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
                selectedId = escChar(curId);
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

        try {
            ResultSet r_s = database.selectQuery("SELECT * FROM person");
            int i = 0;

            while(r_s.next()) {
                Object[] row = new Object[6];

                row[0] = i + 1;
                row[1] = r_s.getString("id");
                row[2] = r_s.getString("name");
                row[3] = r_s.getString("birth_y");
                row[4] = r_s.getString("category");
                row[5] = r_s.getString("level");
                temp.addRow(row);

                i++;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return temp;
    }

    public void insertData() {
        String e_msg = "";

        try {
            String id = escChar(idField.getText());
            if(id.isBlank()) {
                e_msg = "ID tidak boleh kosong!";
                throw new IllegalArgumentException();
            } else {
                try {
                    ResultSet r_s = database.selectQuery("SELECT * FROM person WHERE id = '" + id + "'");

                    if(r_s.next()) {
                        e_msg = "ID " + id + " sudah ada!";
                        throw new IllegalArgumentException();
                    }
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            String name = escChar(nameField.getText());
            if(name.isBlank()) {
                e_msg = "Nama tidak boleh kosong!";
                throw new IllegalArgumentException();
            }

            int birth_y = cstrtoint(birthYearField.getText().trim());
            if(birth_y < 0) {
                e_msg = "Tahun lahir harus berupa bilangan bulat positif!";
                throw new IllegalArgumentException();
            }

            String category = categoryComboBox.getSelectedItem().toString();
            if(categoryComboBox.getSelectedIndex() == 0) {
                e_msg = "Pilih kategori!";
                throw new IllegalArgumentException();
            }

            int level = levelSlider.getValue();

            String query = "INSERT INTO person VALUE('" + id + "', '" + name + "', " + birth_y + ", '" + category + "', " + level + ")";
            database.insertUpdateDeleteQuery(query);

            peopleTable.setModel(setTable());

            clearForm();

            System.out.println("ID " + selectedId + " inserted successfully");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, e_msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        String e_msg = "";

        try {
            String id = escChar(idField.getText());
            if(id.isBlank()) {
                e_msg = "ID tidak boleh kosong!";
                throw new IllegalArgumentException();
            } else if(!id.equals(selectedId)) {
                try {
                    ResultSet r_s = database.selectQuery("SELECT * FROM person WHERE id = '" + id + "'");

                    if(r_s.next()) {
                        e_msg = "ID sudah ada!";
                        throw new IllegalArgumentException();
                    }
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            String name = escChar(nameField.getText());
            if(name.isBlank()) {
                e_msg = "Nama tidak boleh kosong!";
                throw new IllegalArgumentException();
            }

            int birth_y = cstrtoint(birthYearField.getText().trim());
            if(birth_y < 0) {
                e_msg = "Tahun lahir harus berupa bilangan bulat positif!";
                throw new IllegalArgumentException();
            }

            String category = categoryComboBox.getSelectedItem().toString();
            if(categoryComboBox.getSelectedIndex() == 0) {
                e_msg = "Pilih kategori!";
                throw new IllegalArgumentException();
            }

            int level = levelSlider.getValue();

            String query = "UPDATE person SET id = '" + id + "', name = '" + name + "', birth_y = " + birth_y + ", category = '" + category + "', level = " + level + " WHERE id = '" + selectedId + "'";
            database.insertUpdateDeleteQuery(query);

            peopleTable.setModel(setTable());

            clearForm();

            System.out.println("ID \" + selectedId + \" updated successfully");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, e_msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        String query = "DELETE FROM person WHERE id = '" + selectedId + "'";
        database.insertUpdateDeleteQuery(query);
        peopleTable.setModel(setTable());

        clearForm();

        System.out.println("ID \" + selectedId + \" deleted successfully");
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
        selectedId = null;
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
