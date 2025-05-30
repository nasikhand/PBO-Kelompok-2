/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import model.PaketPerjalanan;
import model.Reservasi;
import model.RincianPaketPerjalanan;

import javax.swing.JOptionPane;
import java.io.File;
import java.util.List;

public class PdfGenerator {

    /**
     * Membuat file PDF Itinerary untuk sebuah reservasi paket perjalanan.
     * @param reservasi Objek Reservasi yang berisi info dasar.
     * @param paket Objek PaketPerjalanan untuk info tanggal.
     * @param daftarRincian List rincian destinasi yang akan ditampilkan di tabel.
     * @param destPath Path lengkap file tujuan untuk menyimpan PDF.
     */
    public static void createPaketItinerary(Reservasi reservasi, PaketPerjalanan paket, List<RincianPaketPerjalanan> daftarRincian, String destPath) {
        try {
            // Inisialisasi dokumen PDF
            PdfWriter writer = new PdfWriter(destPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Menambahkan Judul Utama
            Paragraph title = new Paragraph("RENCANA PERJALANAN (ITINERARY)")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            
            Paragraph subtitle = new Paragraph(reservasi.getKodeReservasi())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(subtitle);

            // Spasi
            document.add(new Paragraph("\n"));

            // Informasi Reservasi
            document.add(new Paragraph("Nama Paket: " + reservasi.getNamaTrip()).setFontSize(11));
            document.add(new Paragraph("Tanggal Perjalanan: " + paket.getTanggalMulai() + " s/d " + paket.getTanggalAkhir()).setFontSize(11));
            
            // Spasi lagi
            document.add(new Paragraph("\n\n"));
            
            // Membuat Tabel Rincian Destinasi
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 5, 2})); // 3 kolom dengan lebar relatif
            table.setWidth(UnitValue.createPercentValue(100)); // Lebar tabel 100%

            // Header Tabel
            table.addHeaderCell(new Cell().add(new Paragraph("No.").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Destinasi Kunjungan").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Estimasi Durasi").setBold()));

            // Isi Tabel
            int no = 1;
            for (RincianPaketPerjalanan rincian : daftarRincian) {
                table.addCell(String.valueOf(no++));
                table.addCell(rincian.getNamaDestinasi());
                table.addCell(rincian.getDurasiJam() != null ? rincian.getDurasiJam().toString() + " Jam" : "-");
            }
            document.add(table);

            // Catatan kaki
            document.add(new Paragraph("\n\n\n"));
            document.add(new Paragraph("Terima kasih telah memilih Sinar Jaya Travel!").setItalic().setFontSize(10));

            // Menutup dokumen
            document.close();

            JOptionPane.showMessageDialog(null, "File Itinerary PDF berhasil dibuat di:\n" + destPath, "Pembuatan PDF Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal membuat file PDF: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
}