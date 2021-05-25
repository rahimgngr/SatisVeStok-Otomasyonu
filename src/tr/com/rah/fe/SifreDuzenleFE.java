/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.com.rah.fe;

import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;
import tr.com.rah.dal.AccountDAL;
import tr.com.rah.dal.PersonelDAL;
import tr.com.rah.dal.YetkilerDAL;
import tr.com.rah.interfaces.FeInterfaces;
import tr.com.rah.types.AccountContract;
import tr.com.rah.types.PersonelContract;
import tr.com.rah.types.YetkilerContract;

/**
 *
 * @author rahimgng
 */
public class SifreDuzenleFE extends JDialog implements FeInterfaces {

    public SifreDuzenleFE() {
        initPencere();
    }

    @Override
    public void initPencere() {
        JPanel panel = initPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Şifre Düzenleme Sayfası"));

        add(panel);
        setTitle("Şifre Düzenle");
        pack();
        setModalityType(DEFAULT_MODALITY_TYPE);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel personelLabel = new JLabel("Personel:", JLabel.RIGHT);
        panel.add(personelLabel);
        JComboBox personelBox = new JComboBox(new PersonelDAL().GetAll().toArray());
        panel.add(personelBox);
        JLabel yetkiLabel = new JLabel("Yetki:", JLabel.RIGHT);
        panel.add(yetkiLabel);
        JComboBox yetkiBox = new JComboBox(new YetkilerDAL().GetAll().toArray());
        panel.add(yetkiBox);

        JLabel oldPassLabel = new JLabel("Eski Şifre:", JLabel.RIGHT);
        panel.add(oldPassLabel);
        JPasswordField oldPassField = new JPasswordField(10);
        panel.add(oldPassField);

        JLabel newPassLabel = new JLabel("Yeni Şifre:", JLabel.RIGHT);
        panel.add(newPassLabel);
        JPasswordField newPassField = new JPasswordField(10);
        panel.add(newPassField);
        JLabel newPassTekrarLabel = new JLabel("Yeni Şifre Tekrar:", JLabel.RIGHT);
        panel.add(newPassTekrarLabel);
        JPasswordField newPassTekrarField = new JPasswordField(10);
        panel.add(newPassTekrarField);

        JButton kaydetButton = new JButton("Kaydet");
        panel.add(kaydetButton);
        JButton iptalButton = new JButton("İptal");
        panel.add(iptalButton);

        kaydetButton.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> {
                AccountContract contract = new AccountContract();
                PersonelContract pContract = (PersonelContract) personelBox.getSelectedItem();
                YetkilerContract yContract = (YetkilerContract) yetkiBox.getSelectedItem();

                if ( (newPassField.getText() == null ? newPassTekrarField.getText() == null : newPassField.getText().equals(newPassTekrarField.getText()))) {

                    contract.setPersonelId(pContract.getId());
                    contract.setYetkiId(yContract.getId());
                    contract.setSifre(newPassField.getText());

                    JOptionPane.showMessageDialog(null, pContract.getAdiSoyadi() + " adlı kişinin şifresi başarıyla değiştirildi!");
                    JOptionPane.getRootFrame().dispose();
                    new AccountDAL().Update(contract);
                } else {
                    JOptionPane.showMessageDialog(null, "Şifreler Uyuşmuyor!");
                }

            });
        });
        return panel;
    }

    @Override
    public JMenuBar initBar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JTabbedPane initTabs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
