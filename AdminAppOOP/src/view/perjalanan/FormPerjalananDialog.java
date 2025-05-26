/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.perjalanan;

import controller.KotaController;
import controller.PerjalananController;
import model.Kota;
import model.Perjalanan;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FormPerjalananDialog extends JDialog {
    private JTextField namaPaketField, kuotaField, hargaField;
    private JComboBox<Kota> kotaCombo;
    private JComboBox<String> statusCombo;
    private JFormattedTextField tanggalMulaiField, tanggalAkhirField;
    private JLabel gambarPreview;
    private String gambarPath = null;
    private Runnable onSuccess;
    private int editId = -1;

    public FormPerjalananDialog(JFrame parent, Runnable onSuccess) {
        this(parent, onSuccess, -1);
    }

    public FormPerjalananDialog(JFrame parent, Runnable onSuccess, int editId) {
        super(parent, editId == -1 ? "Tambah Perjalanan" : "Edit Perjalanan", true);
        this.onSuccess = onSuccess;
        this.editId = editId;
        setSize(400, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        namaPaketField = new JTextField();
        kotaCombo = new JComboBox<>();
        tanggalMulaiField = new JFormattedTextField();
        tanggalAkhirField = new JFormattedTextField();
        kuotaField = new JTextField();
        hargaField = new JTextField();
        statusCombo = new JComboBox<>(new String[]{"tersedia", "penuh", "selesai"});

        gambarPreview = new JLabel("Belum ada gambar");
        gambarPreview.setHorizontalAlignment(SwingConstants.CENTER);
        JButton pilihGambarBtn = new JButton("Pilih Gambar");

        pilihGambarBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName().toLowerCase();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
                    gambarPath = selectedFile.getAbsolutePath();
                    gambarPreview.setText(selectedFile.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Format gambar tidak didukung. Gunakan .jpg, .jpeg, atau .png");
                }
            }
        });

        List<Kota> kotaList = KotaController.getAllKota();
        for (Kota k : kotaList) kotaCombo.addItem(k);

        form.add(new JLabel("Nama Paket")); form.add(namaPaketField);
        form.add(new JLabel("Kota")); form.add(kotaCombo);
        form.add(new JLabel("Tanggal Mulai")); form.add(tanggalMulaiField);
        form.add(new JLabel("Tanggal Akhir")); form.add(tanggalAkhirField);
        form.add(new JLabel("Kuota")); form.add(kuotaField);
        form.add(new JLabel("Harga")); form.add(hargaField);
        form.add(new JLabel("Status")); form.add(statusCombo);
        form.add(new JLabel("Gambar Paket")); form.add(pilihGambarBtn); form.add(gambarPreview);

        JButton simpanBtn = new JButton("Simpan");
        simpanBtn.setBackground(new Color(58, 130, 247));
        simpanBtn.setForeground(Color.WHITE);

        simpanBtn.addActionListener(e -> {
            try {
                Kota selectedKota = (Kota) kotaCombo.getSelectedItem();

                Perjalanan p = new Perjalanan();
                p.setNamaPaket(namaPaketField.getText());
                p.setKotaId(selectedKota.getId());
                p.setTanggalMulai(tanggalMulaiField.getText());
                p.setTanggalAkhir(tanggalAkhirField.getText());
                p.setKuota(Integer.parseInt(kuotaField.getText()));
                p.setHarga(Double.parseDouble(hargaField.getText()));
                p.setStatus((String) statusCombo.getSelectedItem());

                if (gambarPath != null) {
                    File src = new File(gambarPath);
                    String fileName = System.currentTimeMillis() + "_" + src.getName();
                    File destDir = new File("uploads");
                    if (!destDir.exists()) destDir.mkdirs();
                    File dest = new File(destDir, fileName);
                    Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    p.setGambar(fileName);
                }

                boolean success = (editId == -1) ? PerjalananController.insertPerjalanan(p)
                        : PerjalananController.updatePerjalanan(editId, p);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
                    if (onSuccess != null) onSuccess.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Input tidak valid: " + ex.getMessage());
            }
        });

        add(form, BorderLayout.CENTER);
        add(simpanBtn, BorderLayout.SOUTH);

        if (editId != -1) {
            Perjalanan data = PerjalananController.getById(editId);
            if (data != null) {
                namaPaketField.setText(data.getNamaPaket());
                tanggalMulaiField.setText(data.getTanggalMulai());
                tanggalAkhirField.setText(data.getTanggalAkhir());
                kuotaField.setText(String.valueOf(data.getKuota()));
                hargaField.setText(String.valueOf(data.getHarga()));
                statusCombo.setSelectedItem(data.getStatus());
                gambarPreview.setText(data.getGambar());
                for (int i = 0; i < kotaCombo.getItemCount(); i++) {
                    if (kotaCombo.getItemAt(i).getId() == data.getKotaId()) {
                        kotaCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        setVisible(true);
    }
}

