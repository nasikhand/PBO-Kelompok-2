-- Menggunakan database yang relevan
USE travel_app;

-- Mengosongkan tabel dengan urutan yang benar untuk menghindari error foreign key
-- (Mulai dari tabel yang paling banyak memiliki ketergantungan)
DELETE FROM pembayaran;
DELETE FROM penumpang;
DELETE FROM itenerary;
DELETE FROM rincian_custom_trip;
DELETE FROM rincian_paket_perjalanan;
DELETE FROM reservasi;
DELETE FROM custom_trip;
DELETE FROM paket_perjalanan;
DELETE FROM destinasi;
DELETE FROM kota;
DELETE FROM user;
-- Tidak menghapus admin agar login utama tetap ada
-- DELETE FROM admin;

--
-- Isi tabel `admin` (jika kosong)
--
INSERT INTO `admin` (`id`, `nama_lengkap`, `email`, `password`, `created_at`, `updated_at`) VALUES
(1, 'Admin Utama', 'admin@travel.com', 'admin123', '2025-05-22 08:55:47', '2025-05-22 08:55:47')
ON DUPLICATE KEY UPDATE nama_lengkap = 'Admin Utama';

--
-- Isi tabel `user`
--
INSERT INTO `user` (`id`, `nama_lengkap`, `email`, `password`, `no_telepon`, `alamat`) VALUES
(1, 'Budi Santoso', 'budi.santoso@example.com', 'pass123', '081234567890', 'Jl. Merdeka No. 1, Jakarta'),
(2, 'Citra Lestari', 'citra.lestari@example.com', 'pass123', '081234567891', 'Jl. Sudirman No. 2, Bandung'),
(3, 'Agus Setiawan', 'agus.setiawan@example.com', 'pass123', '081234567892', 'Jl. Pahlawan No. 3, Surabaya'),
(4, 'Dewi Anggraini', 'dewi.anggraini@example.com', 'pass123', '081234567893', 'Jl. Gajah Mada No. 4, Yogyakarta'),
(5, 'Eko Prasetyo', 'eko.prasetyo@example.com', 'pass123', '081234567894', 'Jl. Diponegoro No. 5, Semarang'),
(6, 'Fitri Handayani', 'fitri.handayani@example.com', 'pass123', '081234567895', 'Jl. Imam Bonjol No. 6, Medan'),
(7, 'Gilang Ramadhan', 'gilang.ramadhan@example.com', 'pass123', '081234567896', 'Jl. Teuku Umar No. 7, Denpasar'),
(8, 'Hesti Purwanti', 'hesti.purwanti@example.com', 'pass123', '081234567897', 'Jl. A. Yani No. 8, Makassar'),
(9, 'Indra Kusuma', 'indra.kusuma@example.com', 'pass123', '081234567898', 'Jl. Gatot Subroto No. 9, Balikpapan'),
(10, 'Joko Susilo', 'joko.susilo@example.com', 'pass123', '081234567899', 'Jl. S. Parman No. 10, Palembang');

--
-- Isi tabel `kota`
--
INSERT INTO `kota` (`id`, `nama_kota`, `provinsi`) VALUES
(1, 'Denpasar', 'Bali'),
(2, 'Yogyakarta', 'DI Yogyakarta'),
(3, 'Labuan Bajo', 'Nusa Tenggara Timur'),
(4, 'Lombok', 'Nusa Tenggara Barat'),
(5, 'Bandung', 'Jawa Barat'),
(6, 'Malang', 'Jawa Timur'),
(7, 'Jakarta', 'DKI Jakarta'),
(8, 'Raja Ampat', 'Papua Barat'),
(9, 'Bukittinggi', 'Sumatera Barat'),
(10, 'Danau Toba', 'Sumatera Utara');

--
-- Isi tabel `destinasi`
--
INSERT INTO `destinasi` (`id`, `kota_id`, `nama_destinasi`, `deskripsi`, `harga`) VALUES
(1, 1, 'Pantai Kuta', 'Pantai ikonik dengan pemandangan matahari terbenam yang indah.', 15000.00),
(2, 1, 'Ubud Monkey Forest', 'Cagar alam dan kompleks candi Hindu dengan ratusan kera.', 80000.00),
(3, 2, 'Candi Borobudur', 'Candi Buddha terbesar di dunia, warisan UNESCO.', 250000.00),
(4, 2, 'Jalan Malioboro', 'Pusat perbelanjaan dan wisata kuliner khas Yogyakarta.', 0.00),
(5, 3, 'Pulau Padar', 'Pulau dengan pemandangan tiga teluk pantai yang menakjubkan.', 150000.00),
(6, 3, 'Pulau Komodo', 'Habitat asli hewan purba Komodo.', 200000.00),
(7, 4, 'Gili Trawangan', 'Pulau kecil yang populer untuk menyelam dan pesta.', 25000.00),
(8, 5, 'Kawah Putih', 'Danau kawah vulkanik dengan warna air yang bisa berubah.', 75000.00),
(9, 6, 'Bromo Tengger Semeru', 'Taman nasional dengan pemandangan gunung berapi yang spektakuler.', 320000.00),
(10, 8, 'Pianemo', 'Gugusan pulau karst yang sering disebut sebagai miniatur Raja Ampat.', 500000.00),
(11, 1, 'Tanah Lot', 'Pura laut yang ikonik di atas batu karang.', 60000.00),
(12, 2, 'Candi Prambanan', 'Kompleks candi Hindu terbesar di Indonesia.', 250000.00);

--
-- Isi tabel `paket_perjalanan`
--
INSERT INTO `paket_perjalanan` (`id`, `kota_id`, `nama_paket`, `tanggal_mulai`, `tanggal_akhir`, `kuota`, `harga`, `deskripsi`, `status`, `gambar`) VALUES
(1, 1, 'Jelajah Magis Bali 3H2M', '2025-06-10', '2025-06-12', 20, 2500000.00, 'Nikmati keindahan Kuta, Ubud, dan Tanah Lot dalam satu paket.', 'tersedia', '1685350001_bali.jpg'),
(2, 2, 'Pesona Sejarah Jogja 4H3M', '2025-06-15', '2025-06-18', 15, 3000000.00, 'Kunjungi Borobudur, Prambanan, dan pusat kota Jogja.', 'tersedia', '1685350002_jogja.jpg'),
(3, 3, 'Petualangan Komodo & Labuan Bajo', '2025-07-01', '2025-07-04', 10, 7500000.00, 'Sailing trip melihat Komodo dan keindahan pulau-pulau sekitarnya.', 'tersedia', '1685350003_komodo.jpg'),
(4, 4, 'Santai di Pantai Lombok', '2025-07-10', '2025-07-13', 25, 2800000.00, 'Menikmati keindahan Gili Trawangan dan pantai selatan Lombok.', 'penuh', '1685350004_lombok.jpg'),
(5, 5, 'Wisata Alam Bandung Selatan', '2025-08-05', '2025-08-07', 30, 1800000.00, 'Menikmati sejuknya Kawah Putih dan perkebunan teh Ciwidey.', 'tersedia', '1685350005_bandung.jpg'),
(6, 6, 'Sunrise di Bromo', '2025-08-17', '2025-08-18', 12, 1500000.00, 'Paket singkat untuk menyaksikan matahari terbit di Gunung Bromo.', 'tersedia', '1685350006_bromo.jpg'),
(7, 8, 'Surga Tersembunyi Raja Ampat', '2025-09-01', '2025-09-06', 8, 15000000.00, 'Paket eksklusif menyelam dan menjelajahi keindahan Raja Ampat.', 'tersedia', '1685350007_rajaampat.jpg'),
(8, 1, 'Bali Spiritual Healing', '2025-09-10', '2025-09-13', 10, 4500000.00, 'Paket yoga dan meditasi di Ubud.', 'selesai', '1685350008_ubud.jpg'),
(9, 2, 'Kuliner Malam Yogyakarta', '2025-05-20', '2025-05-22', 18, 1200000.00, 'Menjelajahi aneka kuliner otentik Yogyakarta di malam hari.', 'selesai', NULL),
(10, 9, 'Jelajah Ranah Minang', '2025-10-01', '2025-10-04', 20, 3500000.00, 'Mengunjungi Bukittinggi, Danau Maninjau, dan Istana Pagaruyung.', 'tersedia', NULL);

--
-- Isi tabel `rincian_paket_perjalanan`
--
INSERT INTO `rincian_paket_perjalanan` (`id`, `paket_perjalanan_id`, `destinasi_id`, `urutan_kunjungan`, `durasi_jam`) VALUES
(1, 1, 1, 1, 4), (2, 1, 2, 2, 5), (3, 1, 11, 3, 3),
(4, 2, 3, 1, 6), (5, 2, 12, 2, 5), (6, 2, 4, 3, 4),
(7, 3, 5, 2, 4), (8, 3, 6, 1, 6),
(9, 4, 7, 1, 8),
(10, 5, 8, 1, 5),
(11, 6, 9, 1, 8),
(12, 7, 10, 1, 7);

--
-- Isi tabel `custom_trip`
--
INSERT INTO `custom_trip` (`id`, `user_id`, `nama_trip`, `tanggal_mulai`, `tanggal_akhir`, `jumlah_peserta`, `status`, `total_harga`) VALUES
(1, 1, 'Trip Keluarga ke Bali', '2025-07-20', '2025-07-25', 4, 'dibayar', 8000000.00),
(2, 2, 'Backpacking Jogja Sendiri', '2025-08-01', '2025-08-05', 1, 'dipesan', 1500000.00),
(3, 3, 'Honeymoon di Lombok', '2025-09-15', '2025-09-20', 2, 'dipesan', 6000000.00),
(4, 4, 'Studi Tur Bandung', '2025-06-25', '2025-06-27', 30, 'selesai', 12000000.00),
(5, 5, 'Mendaki ke Semeru', '2025-10-10', '2025-10-13', 5, 'draft', 0.00),
(6, 7, 'Trip Fotografi di Bali', '2025-11-01', '2025-11-05', 3, 'dipesan', 5500000.00),
(7, 8, 'Wisata Kuliner Makassar', '2025-07-18', '2025-07-20', 2, 'dibayar', 2500000.00),
(8, 9, 'Company Outing ke Puncak', '2025-08-22', '2025-08-24', 25, 'dipesan', 20000000.00),
(9, 10, 'Jelajah Sejarah Palembang', '2025-09-05', '2025-09-07', 2, 'selesai', 2200000.00),
(10, 6, 'Liburan Akhir Tahun di Malang', '2025-12-23', '2025-12-26', 4, 'draft', 0.00);

--
-- Isi tabel `reservasi`
--
INSERT INTO `reservasi` (`id`, `trip_type`, `trip_id`, `kode_reservasi`, `tanggal_reservasi`, `status`) VALUES
(1, 'paket_perjalanan', 1, 'SJ-202505-0001', '2025-05-10', 'dibayar'),
(2, 'paket_perjalanan', 2, 'SJ-202505-0002', '2025-05-12', 'dipesan'),
(3, 'paket_perjalanan', 3, 'SJ-202505-0003', '2025-05-15', 'dipesan'),
(4, 'paket_perjalanan', 7, 'SJ-202505-0004', '2025-05-20', 'pending'),
(5, 'custom_trip', 1, 'SJ-202505-0005', '2025-05-21', 'dibayar'),
(6, 'custom_trip', 2, 'SJ-202505-0006', '2025-05-22', 'dipesan'),
(7, 'custom_trip', 3, 'SJ-202505-0007', '2025-05-25', 'dipesan'),
(8, 'paket_perjalanan', 4, 'SJ-202505-0008', '2025-04-10', 'selesai'),
(9, 'custom_trip', 4, 'SJ-202505-0009', '2025-04-15', 'selesai'),
(10, 'paket_perjalanan', 10, 'SJ-202505-0010', '2025-05-28', 'pending');

--
-- Isi tabel `penumpang`
--
INSERT INTO `penumpang` (`id`, `reservasi_id`, `nama_penumpang`, `jenis_kelamin`, `tanggal_lahir`, `nomor_telepon`, `email`) VALUES
(1, 1, 'Budi Santoso', 'pria', '1990-01-15', '081234567890', 'budi.santoso@example.com'),
(2, 1, 'Rina Santoso', 'wanita', '1992-03-20', '081234567890', 'budi.santoso@example.com'),
(3, 2, 'Citra Lestari', 'wanita', '1995-08-10', '081234567891', 'citra.lestari@example.com'),
(4, 3, 'Agus Setiawan', 'pria', '1988-11-05', '081234567892', 'agus.setiawan@example.com'),
(5, 3, 'Lina Setiawan', 'wanita', '1990-02-12', '081234567892', 'agus.setiawan@example.com'),
(6, 4, 'Gilang Ramadhan', 'pria', '1993-07-25', '081234567896', 'gilang.ramadhan@example.com'),
(7, 5, 'Budi Santoso', 'pria', '1990-01-15', '081234567890', 'budi.santoso@example.com'),
(8, 5, 'Anak Budi 1', 'pria', '2015-05-01', '081234567890', 'budi.santoso@example.com'),
(9, 5, 'Anak Budi 2', 'wanita', '2017-10-10', '081234567890', 'budi.santoso@example.com'),
(10, 6, 'Citra Lestari', 'wanita', '1995-08-10', '081234567891', 'citra.lestari@example.com'),
(11, 7, 'Hesti Purwanti', 'wanita', '1991-04-30', '081234567897', 'hesti.purwanti@example.com'),
(12, 7, 'Suami Hesti', 'pria', '1989-09-09', '081234567897', 'hesti.purwanti@example.com'),
(13, 10, 'Indra Kusuma', 'pria', '1994-06-18', '081234567898', 'indra.kusuma@example.com');

--
-- Isi tabel `pembayaran`
--
INSERT INTO `pembayaran` (`id`, `reservasi_id`, `metode_pembayaran`, `jumlah_pembayaran`, `tanggal_pembayaran`, `status_pembayaran`) VALUES
(1, 1, 'kartu kredit', 2500000.00, '2025-05-11', 'lunas'),
(2, 2, 'transfer', 3000000.00, '2025-05-13', 'lunas'),
(3, 3, 'transfer', 7500000.00, '2025-05-16', 'pending'),
(4, 4, 'kartu kredit', 15000000.00, '2025-05-21', 'pending'),
(5, 5, 'transfer', 8000000.00, '2025-05-22', 'lunas'),
(6, 6, 'cash', 1500000.00, '2025-05-23', 'lunas'),
(7, 7, 'kartu kredit', 6000000.00, '2025-05-26', 'pending'),
(8, 8, 'transfer', 2800000.00, '2025-04-11', 'lunas'),
(9, 9, 'kartu kredit', 12000000.00, '2025-04-16', 'lunas'),
(10, 10, 'transfer', 3500000.00, '2025-05-28', 'pending');