/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.com.rah.fe;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import tr.com.rah.dal.MusteriDAL;
import tr.com.rah.dal.SatisDAL;
import tr.com.rah.dal.StokDAL;
import tr.com.rah.dal.UrunlerDAL;
import tr.com.rah.interfaces.FeInterfaces;
import tr.com.rah.types.MusteriContract;
import tr.com.rah.types.PersonelContract;
import tr.com.rah.types.SatisContract;
import tr.com.rah.types.StokContract;
import tr.com.rah.types.UrunlerContract;
import tr.com.rah.utilities.MenulerCom;

/**
 *
 * @author rahimgng
 */
public class AnaPencereFE extends JFrame implements FeInterfaces {

    public AnaPencereFE() {
        initPencere();
    }

    @Override
    public final void initPencere() {
        JPanel panel = initPanel();
        JMenuBar bar = initBar();

        add(panel);
        setJMenuBar(bar);
        setTitle("Satış ve Stok Programı");
        pack();
        setSize(600, 250);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public JPanel initPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane pane = initTabs();
        panel.add(pane, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public JMenuBar initBar() {
        JMenuBar bar = MenulerCom.initBar();
        return bar;
    }

    @Override
    public JTabbedPane initTabs() {
        JTabbedPane pane = new JTabbedPane();
        ImageIcon icon = new ImageIcon("icons/stock.png");

        JPanel stokPanel = new JPanel(new BorderLayout());
        JPanel satisPanel = new JPanel(new BorderLayout());

        //stok tab
        JPanel stokSolPanel = new JPanel(new BorderLayout());
        JPanel stokSolUstPanel = new JPanel(new GridLayout(5, 2));
        JPanel stokSolAltPanel = new JPanel();

        stokSolPanel.setBorder(BorderFactory.createTitledBorder("Stok İşlemleri"));
        Object[] stokKolonlar = {"Id", "Ürün Adı", "Personel Adı", "Adet", "Toplam", "Tarih"};
        DefaultTableModel model = new DefaultTableModel(stokKolonlar, 0);
        JTable table = new JTable(model);
        JScrollPane stokTablePane = new JScrollPane(table);

        new StokDAL().GetAllStok().forEach((contract) -> {
            model.addRow(contract.getVeriler());
        });

        JLabel stokUrunAdiLabel = new JLabel("Ürün Adı:", JLabel.RIGHT);
        stokSolUstPanel.add(stokUrunAdiLabel);
        JComboBox stokUrunAdiBox = new JComboBox(new UrunlerDAL().GetAll().toArray());
        stokSolUstPanel.add(stokUrunAdiBox);
        JLabel stokAdetLabel = new JLabel("Adet:", JLabel.RIGHT);
        stokSolUstPanel.add(stokAdetLabel);
        JTextField stokAdetField = new JTextField(10);
        stokSolUstPanel.add(stokAdetField);
        JLabel stokTarihiLabel = new JLabel("Tarih:", JLabel.RIGHT);
        stokSolUstPanel.add(stokTarihiLabel);
        JDateChooser stokTarihi = new JDateChooser();
        stokSolUstPanel.add(stokTarihi);

        JButton stokEkleButton = new JButton("Stok Ekle");
        stokSolUstPanel.add(stokEkleButton);
        JButton stokYenileButton = new JButton("Yenile");
        stokSolUstPanel.add(stokYenileButton);

        JButton stoktTotalButton = new JButton("Stok Toplam Ürün");
        stokSolUstPanel.add(stoktTotalButton);

        //stok total action
        stoktTotalButton.addActionListener((ActionEvent e) -> {
            int satir = model.getRowCount();
            for (int i = 0; i < satir; i++) {
                model.removeRow(0);
            }
            new StokDAL().GetTotalStok().forEach((total) -> {
                model.addRow(total.getVeriler());
            });
        });

        //stok yenile action
        stokYenileButton.addActionListener((ActionEvent e) -> {
            int satir = model.getRowCount();
            for (int i = 0; i < satir; i++) {
                model.removeRow(0);
            }
            new StokDAL().GetAllStok().forEach((compContract) -> {
                model.addRow(compContract.getVeriler());
            });
        });
        
        //stok ekle action
        stokEkleButton.addActionListener((ActionEvent e) -> {
            StokContract contract = new StokContract();
            UrunlerContract uContract = (UrunlerContract) stokUrunAdiBox.getSelectedItem();
            PersonelContract pContract = (PersonelContract) LoginFE.emailBox.getSelectedItem();

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String date = format.format(stokTarihi.getDate());

            contract.setPersonelId(pContract.getId());
            contract.setUrunId(uContract.getId());
            contract.setTarih(date);
            contract.setAdet(Integer.parseInt(stokAdetField.getText()));

            new StokDAL().Insert(contract);

            JOptionPane.showMessageDialog(null, uContract.getAdi() + " ürünü eklendi!");

            //aynı anda sağ tarafa ekleme
            int satir = model.getRowCount();
            for (int i = 0; i < satir; i++) {
                model.removeRow(0);
            }
            new StokDAL().GetAllStok().forEach((compContract) -> {
                model.addRow(compContract.getVeriler());
            });

        });

        //satış tab
        JPanel satisSagPanel = new JPanel(new BorderLayout());
        JPanel satisSagUstPanel = new JPanel(new GridLayout(6, 2));
        JPanel satisSagAltPanel = new JPanel();

        satisSagPanel.setBorder(BorderFactory.createTitledBorder("Satış İşlemleri"));
        Object[] satisKolonlar = {"Id", "Personel Adı", "Müşteri Adı", "Ürün Adı", "Adeti", "Tarihi"};
        DefaultTableModel satisModel = new DefaultTableModel(satisKolonlar, 0);
        JTable satisTable = new JTable(satisModel);
        JScrollPane satisTablePane = new JScrollPane(satisTable);

        JLabel musteriLabel = new JLabel("Müşteri Adı:", JLabel.RIGHT);
        satisSagUstPanel.add(musteriLabel);
        JComboBox musteriAdiBox = new JComboBox(new MusteriDAL().GetAll().toArray());
        satisSagUstPanel.add(musteriAdiBox);
        JLabel satisUrunAdiLabel = new JLabel("Ürün Adı:", JLabel.RIGHT);
        satisSagUstPanel.add(satisUrunAdiLabel);
        JComboBox satisUrunAdiBox = new JComboBox(new UrunlerDAL().GetAll().toArray());
        satisSagUstPanel.add(satisUrunAdiBox);
        JLabel satisAdetLabel = new JLabel("Adet:", JLabel.RIGHT);
        satisSagUstPanel.add(satisAdetLabel);
        JTextField satisAdetField = new JTextField(10);
        satisSagUstPanel.add(satisAdetField);
        JLabel satisTarihiLabel = new JLabel("Tarih:", JLabel.RIGHT);
        satisSagUstPanel.add(satisTarihiLabel);
        JDateChooser satisTarihi = new JDateChooser();
        satisSagUstPanel.add(satisTarihi);

        JButton satisEkleButton = new JButton("Satış Yap");
        satisSagUstPanel.add(satisEkleButton);
        JButton satisYenileButton = new JButton("Yenile");
        satisSagUstPanel.add(satisYenileButton);
        
        //sağda göstermek için
        new SatisDAL().GetAllSatis().forEach((compContract) -> {
                satisModel.addRow(compContract.getVeriler());
        });

        //satis ekle action
        satisEkleButton.addActionListener((ActionEvent e) -> {
            PersonelContract pContract = (PersonelContract) LoginFE.emailBox.getSelectedItem();
            UrunlerContract uContract = (UrunlerContract) satisUrunAdiBox.getSelectedItem();
            MusteriContract mContract = (MusteriContract) musteriAdiBox.getSelectedItem();
            SatisContract contract = new SatisContract();

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String date = format.format(satisTarihi.getDate());

            contract.setMusteriId(mContract.getId());
            contract.setPersonelId(pContract.getId());
            contract.setUrunId(uContract.getId());
            contract.setAdet(Integer.parseInt(satisAdetField.getText()));
            contract.setTarih(date);

            new SatisDAL().Insert(contract);
            
            //aynı anda sağa ekleme
            int satir = satisModel.getRowCount();
            for (int i = 0; i < satir; i++) {
                satisModel.removeRow(0);
            }
            new SatisDAL().GetAllSatis().forEach((compContract) -> {
                satisModel.addRow(compContract.getVeriler());
            });

            //stoktan eksiltme işlemi
            StokContract stokContract = new StokContract();
            stokContract.setPersonelId(pContract.getId());
            stokContract.setUrunId(uContract.getId());
            stokContract.setAdet(-Integer.parseInt(satisAdetField.getText()));
            stokContract.setTarih(date);

            new StokDAL().Insert(stokContract);
            JOptionPane.showMessageDialog(null, mContract.getAdiSoyadi() + " adlı müşteriye satış işlemi gerçekleştirildi ve" + uContract.getAdi() + " adlı ürün stok içerisinden" + contract.getAdet() + " adet eksiltildi!");

        });

        //satis yenile action
        satisYenileButton.addActionListener((ActionEvent e) -> {
            int satir = satisModel.getRowCount();
            for (int i = 0; i < satir; i++) {
                satisModel.removeRow(0);
            }
            new SatisDAL().GetAllSatis().forEach((compContract) -> {
                satisModel.addRow(compContract.getVeriler());
            });
        });

        //panel işlemleri
        stokPanel.add(stokSolPanel, BorderLayout.WEST);
        stokPanel.add(stokTablePane, BorderLayout.CENTER);

        satisPanel.add(satisSagPanel, BorderLayout.WEST);
        satisPanel.add(satisTablePane, BorderLayout.CENTER);

        stokSolPanel.add(stokSolUstPanel, BorderLayout.NORTH);
        stokSolPanel.add(stokSolAltPanel, BorderLayout.SOUTH);

        satisSagPanel.add(satisSagUstPanel, BorderLayout.NORTH);
        satisSagPanel.add(satisSagAltPanel, BorderLayout.SOUTH);

        pane.addTab("Stoklar ", icon, stokPanel, "does nothing");
        pane.addTab("Satışlar ", icon, satisPanel, "does nothing");
        return pane;
    }

}
