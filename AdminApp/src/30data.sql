USE travel_app;

-- Mengosongkan tabel dengan urutan yang benar untuk menghindari error foreign key
-- (Mulai dari tabel yang paling banyak memiliki ketergantungan ke tabel induk)
DELETE FROM pembayaran WHERE id > 0;
DELETE FROM penumpang WHERE id > 0;
DELETE FROM itenerary WHERE id > 0;
DELETE FROM rincian_custom_trip WHERE id > 0;
DELETE FROM rincian_paket_perjalanan WHERE id > 0;
DELETE FROM reservasi WHERE id > 0;
DELETE FROM custom_trip WHERE id > 0;
DELETE FROM paket_perjalanan WHERE id > 0 AND deleted_at IS NULL; -- Hanya hapus yang belum soft-delete jika sudah implementasi
DELETE FROM destinasi WHERE id > 0;
DELETE FROM kota WHERE id > 0;
DELETE FROM user WHERE id > 0; 
-- JANGAN HAPUS ADMIN UTAMA, cukup tambahkan admin baru jika perlu
-- DELETE FROM admin WHERE email != 'admin@travel.com';


-- Data Admin (Selain Admin Utama)
INSERT INTO `admin` (`nama_lengkap`, `email`, `password`) VALUES
('Admin Staff Satu', 'staff1@travel.com', 'staff123'), -- Nanti ganti dengan hash
('Admin Manager', 'manager@travel.com', 'manager123'); -- Nanti ganti dengan hash

-- Data User (minimal 30)
INSERT INTO `user` (`nama_lengkap`, `email`, `password`, `no_telepon`, `alamat`) VALUES
('Andi Wijaya', 'andi.wijaya@example.com', 'pass123', '081211110001', 'Jl. Kenanga No. 1, Jakarta'),
('Bunga Citra', 'bunga.citra@example.com', 'pass123', '081211110002', 'Jl. Mawar No. 2, Bandung'),
('Cahyo Purnomo', 'cahyo.purnomo@example.com', 'pass123', '081211110003', 'Jl. Melati No. 3, Surabaya'),
('Dian Lestari', 'dian.lestari@example.com', 'pass123', '081211110004', 'Jl. Anggrek No. 4, Yogyakarta'),
('Eko Sanjaya', 'eko.sanjaya@example.com', 'pass123', '081211110005', 'Jl. Kamboja No. 5, Semarang'),
('Fitri Amelia', 'fitri.amelia@example.com', 'pass123', '081211110006', 'Jl. Dahlia No. 6, Medan'),
('Gilang Perkasa', 'gilang.perkasa@example.com', 'pass123', '081211110007', 'Jl. Flamboyan No. 7, Denpasar'),
('Hesti Wulandari', 'hesti.wulandari@example.com', 'pass123', '081211110008', 'Jl. Cempaka No. 8, Makassar'),
('Indra Gunawan', 'indra.gunawan@example.com', 'pass123', '081211110009', 'Jl. Soka No. 9, Palembang'),
('Jasmine Putri', 'jasmine.putri@example.com', 'pass123', '081211110010', 'Jl. Tulip No. 10, Balikpapan'),
('Kurniawan Adi', 'kurniawan.adi@example.com', 'pass123', '081211110011', 'Jl. Bougenville No. 11, Manado'),
('Lina Marlina', 'lina.marlina@example.com', 'pass123', '081211110012', 'Jl. Edelweis No. 12, Jayapura'),
('Mario Santoso', 'mario.santoso@example.com', 'pass123', '081211110013', 'Jl. Aster No. 13, Pontianak'),
('Nina Handayani', 'nina.handayani@example.com', 'pass123', '081211110014', 'Jl. Lavender No. 14, Pekanbaru'),
('Oscar Pratama', 'oscar.pratama@example.com', 'pass123', '081211110015', 'Jl. Matahari No. 15, Padang'),
('Putri Ayu', 'putri.ayu@example.com', 'pass123', '081211110016', 'Jl. Teratai No. 16, Samarinda'),
('Rizky Febian', 'rizky.febian@example.com', 'pass123', '081211110017', 'Jl. Krisan No. 17, Banjarmasin'),
('Sari Dewi', 'sari.dewi@example.com', 'pass123', '081211110018', 'Jl. Lili No. 18, Mataram'),
('Toni Setiawan', 'toni.setiawan@example.com', 'pass123', '081211110019', 'Jl. Sakura No. 19, Kupang'),
('Umar Bakri', 'umar.bakri@example.com', 'pass123', '081211110020', 'Jl. Akasia No. 20, Ambon'),
('Vina Panduwinata', 'vina.panduwinata@example.com', 'pass123', '081211110021', 'Jl. Seruni No. 21, Ternate'),
('Wawan Gunarso', 'wawan.gunarso@example.com', 'pass123', '081211110022', 'Jl. Alamanda No. 22, Bengkulu'),
('Xena Warrior', 'xena.warrior@example.com', 'pass123', '081211110023', 'Jl. Anyelir No. 23, Palu'),
('Yudi Hartono', 'yudi.hartono@example.com', 'pass123', '081211110024', 'Jl. Bakung No. 24, Kendari'),
('Zahra Meidina', 'zahra.meidina@example.com', 'pass123', '081211110025', 'Jl. Begonia No. 25, Gorontalo'),
('Ahmad Subarjo', 'ahmad.subarjo@example.com', 'pass123', '081211110026', 'Jl. Cattleya No. 26, Manokwari'),
('Bella Swan', 'bella.swan@example.com', 'pass123', '081211110027', 'Jl. Dandelion No. 27, Mamuju'),
('Charlie Brown', 'charlie.brown@example.com', 'pass123', '081211110028', 'Jl. Gardenia No. 28, Sofifi'),
('Diana Prince', 'diana.prince@example.com', 'pass123', '081211110029', 'Jl. Hibiscus No. 29, Tanjung Pinang'),
('Edward Cullen', 'edward.cullen@example.com', 'pass123', '081211110030', 'Jl. Ixora No. 30, Pangkal Pinang');

-- Data Kota (minimal 30)
INSERT INTO `kota` (`nama_kota`, `provinsi`) VALUES
('Jakarta Pusat', 'DKI Jakarta'), ('Bandung', 'Jawa Barat'), ('Semarang', 'Jawa Tengah'), ('Yogyakarta', 'DI Yogyakarta'),
('Surabaya', 'Jawa Timur'), ('Denpasar', 'Bali'), ('Medan', 'Sumatera Utara'), ('Palembang', 'Sumatera Selatan'),
('Makassar', 'Sulawesi Selatan'), ('Banjarmasin', 'Kalimantan Selatan'), ('Balikpapan', 'Kalimantan Timur'), ('Manado', 'Sulawesi Utara'),
('Jayapura', 'Papua'), ('Padang', 'Sumatera Barat'), ('Pekanbaru', 'Riau'), ('Lampung', 'Lampung'),
('Pontianak', 'Kalimantan Barat'), ('Mataram', 'Nusa Tenggara Barat'), ('Kupang', 'Nusa Tenggara Timur'), ('Ambon', 'Maluku'),
('Bogor', 'Jawa Barat'), ('Malang', 'Jawa Timur'), ('Solo', 'Jawa Tengah'), ('Cirebon', 'Jawa Barat'),
('Depok', 'Jawa Barat'), ('Tangerang', 'Banten'), ('Bekasi', 'Jawa Barat'), ('Serang', 'Banten'),
('Samarinda', 'Kalimantan Timur'), ('Batam', 'Kepulauan Riau'), ('Jambi', 'Jambi');

-- Data Destinasi (minimal 30, sesuaikan kota_id dengan ID dari tabel kota di atas)
-- Asumsi ID Kota Jakarta Pusat = 1, Bandung = 2, ..., Jambi = 31
INSERT INTO `destinasi` (`kota_id`, `nama_destinasi`, `deskripsi`, `harga`, `gambar`) VALUES
(1, 'Monumen Nasional (Monas)', 'Landmark ikonik Jakarta dengan museum dan puncak observasi.', 20000, 'monas.jpg'),
(2, 'Kawah Putih Ciwidey', 'Danau kawah vulkanik dengan air berwarna putih kehijauan.', 75000, 'kawah_putih.jpg'),
(4, 'Candi Prambanan', 'Kompleks candi Hindu terbesar di Indonesia, warisan UNESCO.', 250000, 'prambanan.jpg'),
(5, 'Gunung Bromo', 'Gunung berapi aktif dengan pemandangan sunrise yang spektakuler.', 320000, 'bromo.jpg'),
(6, 'Pantai Kuta Bali', 'Pantai terkenal untuk berselancar dan menikmati matahari terbenam.', 15000, 'kuta.jpg'),
(1, 'Kota Tua Jakarta', 'Kawasan bersejarah dengan arsitektur kolonial Belanda.', 0, 'kota_tua_jakarta.jpg'),
(2, 'Trans Studio Bandung', 'Taman hiburan indoor terbesar di Indonesia.', 280000, 'trans_studio_bandung.jpg'),
(4, 'Jalan Malioboro', 'Pusat perbelanjaan, kuliner, dan kerajinan khas Yogyakarta.', 0, 'malioboro.jpg'),
(5, 'Taman Nasional Baluran', 'Savana ala Afrika di Jawa Timur, dikenal sebagai Africa van Java.', 150000, 'baluran.jpg'),
(6, 'Ubud Monkey Forest', 'Cagar alam dan kompleks candi dengan ratusan kera.', 80000, 'monkey_forest_ubud.jpg'),
(7, 'Danau Toba', 'Danau vulkanik terbesar di dunia dengan pulau Samosir di tengahnya.', 10000, 'danau_toba.jpg'),
(8, 'Jembatan Ampera', 'Jembatan ikonik di atas Sungai Musi, Palembang.', 0, 'ampera.jpg'),
(9, 'Pantai Losari', 'Waterfront populer di Makassar untuk menikmati kuliner dan matahari terbenam.', 0, 'losari.jpg'),
(10, 'Pasar Terapung Lok Baintan', 'Pasar tradisional di atas sungai, khas Banjarmasin.', 0, 'pasar_terapung.jpg'),
(11, 'Pantai Melawai', 'Tempat nongkrong populer di Balikpapan saat senja.', 0, 'melawai.jpg'),
(12, 'Taman Nasional Bunaken', 'Surga bawah laut terkenal untuk diving dan snorkeling.', 150000, 'bunaken.jpg'),
(13, 'Danau Sentani', 'Danau indah dengan pulau-pulau kecil dan budaya lokal yang kaya.', 20000, 'sentani.jpg'),
(14, 'Jam Gadang', 'Menara jam ikonik di Bukittinggi, Sumatera Barat.', 0, 'jam_gadang.jpg'),
(15, 'Istana Siak Sri Indrapura', 'Istana megah Kesultanan Siak di Riau.', 10000, 'istana_siak.jpg'),
(21, 'Kebun Raya Bogor', 'Salah satu kebun raya tertua dan terbesar di dunia.', 25000, 'kebun_raya_bogor.jpg'),
(22, 'Jatim Park 2 (Museum Satwa & Batu Secret Zoo)', 'Taman hiburan dan edukasi satwa modern di Batu, Malang.', 150000, 'jatim_park2.jpg'),
(3, 'Lawang Sewu', 'Bangunan bersejarah peninggalan Belanda dengan arsitektur unik.', 30000, 'lawang_sewu.jpg'),
(23, 'Keraton Kasunanan Surakarta', 'Istana resmi Kesunanan Surakarta Hadiningrat.', 15000, 'keraton_solo.jpg'),
(6, 'Pura Tanah Lot', 'Pura laut yang ikonik di atas batu karang besar.', 60000, 'tanah_lot.jpg'),
(4, 'Candi Borobudur (Magelang, dekat Jogja)', 'Candi Buddha terbesar di dunia, warisan UNESCO.', 250000, 'borobudur.jpg'),
(2, 'Floating Market Lembang', 'Pasar terapung modern dengan berbagai kuliner dan wahana.', 30000, 'floating_market_lembang.jpg'),
(5, 'Kawah Ijen', 'Kawah vulkanik dengan fenomena api biru dan danau asam.', 150000, 'ijen.jpg'),
(7, 'Istana Maimun', 'Istana Kesultanan Deli dengan arsitektur Melayu, Islam, dan Eropa.', 10000, 'istana_maimun.jpg'),
(9, 'Benteng Rotterdam', 'Benteng peninggalan Gowa-Tallo dan Belanda.', 10000, 'benteng_rotterdam.jpg'),
(18, 'Gili Trawangan (Lombok)', 'Pulau kecil populer untuk diving, snorkeling, dan suasana santai.', 25000, 'gili_trawangan.jpg'),
(19, 'Pulau Komodo (Labuan Bajo, dekat Kupang)', 'Habitat asli hewan purba Komodo.', 275000, 'pulau_komodo.jpg');


-- Data Paket Perjalanan (minimal 30)
-- Pastikan tanggal_mulai < tanggal_akhir, sesuaikan kota_id
-- Format tanggal YYYY-MM-DD
INSERT INTO `paket_perjalanan` (`kota_id`, `nama_paket`, `tanggal_mulai`, `tanggal_akhir`, `kuota`, `harga`, `deskripsi`, `status`, `gambar`) VALUES
(1, 'Jakarta City Tour 2H1M', '2025-07-01', '2025-07-02', 25, 1500000, 'Jelajahi ikon Jakarta: Monas, Kota Tua, dan pusat perbelanjaan.', 'tersedia', 'jkt_city.jpg'),
(2, 'Bandung Culinary & Shopping 3H2M', '2025-07-05', '2025-07-07', 20, 2200000, 'Nikmati kuliner lezat dan belanja di factory outlet Bandung.', 'tersedia', 'bdg_culinary.jpg'),
(4, 'Yogyakarta Heritage Trip 4H3M', '2025-07-10', '2025-07-13', 15, 3000000, 'Kunjungi Borobudur, Prambanan, Keraton, dan Malioboro.', 'penuh', 'jogja_heritage.jpg'),
(5, 'Bromo Sunrise Adventure 2D1N', '2025-07-15', '2025-07-16', 12, 1800000, 'Saksikan matahari terbit spektakuler di Gunung Bromo.', 'tersedia', 'bromo_sunrise.jpg'),
(6, 'Exotic Bali Escape 5H4M', '2025-07-20', '2025-07-24', 30, 4500000, 'Rasakan keindahan pantai, budaya, dan alam Bali.', 'tersedia', 'bali_escape.jpg'),
(7, 'Danau Toba & Samosir Explorer 3H2M', '2025-08-01', '2025-08-03', 18, 2800000, 'Jelajahi keindahan Danau Toba dan budaya Batak di Pulau Samosir.', 'tersedia', 'toba_explorer.jpg'),
(9, 'Makassar Cultural Journey 3H2M', '2025-08-05', '2025-08-07', 20, 2500000, 'Kunjungi Benteng Rotterdam, Pantai Losari, dan desa adat.', 'selesai', 'makassar_culture.jpg'),
(12, 'Bunaken Diving Paradise 4H3M', '2025-08-10', '2025-08-13', 10, 5500000, 'Paket menyelam di salah satu taman laut terindah di dunia.', 'tersedia', 'bunaken_diving.jpg'),
(18, 'Lombok & Gili Islands Getaway 5H4M', '2025-08-15', '2025-08-19', 22, 4800000, 'Nikmati pantai indah Lombok dan keseruan di Gili Trawangan.', 'tersedia', 'lombok_gili.jpg'),
(19, 'Komodo Dragon Adventure 3D2N', '2025-08-22', '2025-08-24', 10, 7500000, 'Bertemu Komodo langsung di habitat aslinya dan snorkeling.', 'tersedia', 'komodo_adv.jpg'),
(3, 'Semarang Old City & Heritage 2H1M', '2025-09-01', '2025-09-02', 20, 1600000, 'Jelajahi Kota Lama Semarang, Lawang Sewu, dan Klenteng Sam Poo Kong.', 'tersedia', 'semarang_heritage.jpg'),
(11, 'Derawan Islands Snorkeling Trip 4H3M', '2025-09-05', '2025-09-08', 15, 6000000, 'Snorkeling bersama ubur-ubur tidak menyengat dan penyu di Kepulauan Derawan.', 'tersedia', 'derawan_trip.jpg'),
(14, 'Minangkabau Cultural Immersion 3H2M', '2025-09-10', '2025-09-12', 18, 2700000, 'Rasakan budaya Minang di Bukittinggi, Danau Maninjau, dan Istana Pagaruyung.', 'penuh', 'minang_culture.jpg'),
(22, 'Malang & Batu Theme Parks Fun 3H2M', '2025-09-15', '2025-09-17', 25, 2300000, 'Keseruan di Jatim Park, Museum Angkut, dan BNS.', 'tersedia', 'malang_batu_fun.jpg'),
(21, 'Bogor Botanical & Safari Park 2D1N', '2025-09-20', '2025-09-21', 30, 1700000, 'Kunjungi Kebun Raya Bogor dan Taman Safari Indonesia.', 'tersedia', 'bogor_safari.jpg'),
(1, 'Jakarta Shopping Spree 3D2N', '2025-10-01', '2025-10-03', 15, 2000000, 'Belanja sepuasnya di mall-mall ternama Jakarta.', 'tersedia', NULL),
(2, 'Lembang Nature Retreat 2D1N', '2025-10-05', '2025-10-06', 20, 1500000, 'Menikmati sejuknya alam Lembang, Farmhouse, dan Floating Market.', 'tersedia', 'lembang_nature.jpg'),
(4, 'Solo Batik & Culinary Trail 3D2N', '2025-10-10', '2025-10-12', 18, 1900000, 'Belajar membatik dan mencicipi kuliner khas Solo.', 'tersedia', NULL),
(5, 'Surabaya Heroic Journey 2D1N', '2025-10-15', '2025-10-16', 22, 1600000, 'Mengunjungi Tugu Pahlawan, House of Sampoerna, dan Jembatan Suramadu.', 'tersedia', 'sby_heroic.jpg'),
(6, 'Nusa Penida & Lembongan Escape 3D2N', '2025-10-20', '2025-10-22', 12, 3500000, 'Jelajahi keindahan pantai dan spot foto Instagramable di Nusa Penida & Lembongan.', 'tersedia', 'nusa_penida.jpg'),
(8, 'Palembang Musi River Tour 2D1N', '2025-11-01', '2025-11-02', 20, 1700000, 'Menyusuri Sungai Musi, mengunjungi Pulau Kemaro dan Jembatan Ampera.', 'tersedia', NULL),
(10, 'Banjarmasin Floating Market & Culture 3D2N', '2025-11-05', '2025-11-07', 15, 2400000, 'Mengunjungi pasar terapung dan pusat kerajinan intan Martapura.', 'tersedia', 'bjm_culture.jpg'),
(13, 'Raja Ampat Unforgettable 5D4N', '2025-11-10', '2025-11-14', 8, 15000000, 'Pengalaman tak terlupakan di surga bawah laut Raja Ampat.', 'tersedia', 'raja_ampat_ultimate.jpg'),
(15, 'Pekanbaru Riau Wonders 3D2N', '2025-11-15', '2025-11-17', 18, 2600000, 'Mengunjungi Istana Siak, Masjid An-Nur, dan pusat budaya Melayu.', 'tersedia', NULL),
(16, 'Way Kambas Elephant Safari 2D1N', '2025-11-20', '2025-11-21', 20, 2000000, 'Melihat gajah Sumatera dari dekat di Taman Nasional Way Kambas.', 'tersedia', 'way_kambas.jpg'),
(20, 'Ambon Spice Islands Historical Tour 3D2N', '2025-12-01', '2025-12-03', 15, 3000000, 'Jelajahi sejarah jalur rempah di Ambon dan sekitarnya.', 'tersedia', 'ambon_spice.jpg'),
(24, 'Cirebon Heritage & Culinary 2D1N', '2025-12-05', '2025-12-06', 20, 1500000, 'Kunjungi Keraton Kasepuhan, Gua Sunyaragi, dan nikmati kuliner khas Cirebon.', 'tersedia', NULL),
(25, 'Thousand Islands Getaway (Jakarta) 2D1N', '2025-12-10', '2025-12-11', 25, 1200000, 'Liburan singkat ke salah satu pulau di Kepulauan Seribu.', 'penuh', 'thousand_islands.jpg'),
(30, 'Batam Singapore Quick Hop 3D2N', '2025-12-15', '2025-12-17', 18, 3500000, 'Nikmati Batam dan menyeberang untuk city tour singkat di Singapura.', 'tersedia', NULL),
(31, 'Jambi Ancient Kingdom Trail 3D2N', '2025-12-20', '2025-12-22', 15, 2800000, 'Jelajahi situs Candi Muaro Jambi dan sejarah Kerajaan Sriwijaya.', 'selesai', NULL);

-- Data Rincian Paket Perjalanan (beberapa untuk setiap paket)
-- Asumsi ID Paket Jakarta City Tour = 1, Bandung Culinary = 2, dst.
INSERT INTO `rincian_paket_perjalanan` (`paket_perjalanan_id`, `destinasi_id`, `urutan_kunjungan`, `durasi_jam`) VALUES
(1, 1, 1, 4), (1, 6, 2, 4), -- Jakarta
(2, 2, 1, 5), (2, 7, 2, 3), -- Bandung
(3, 3, 1, 6), (3, 4, 2, 4), (3, 25, 3, 5), -- Yogyakarta
(4, 5, 1, 8), -- Bromo
(5, 24, 1, 4), (5, 10, 2, 5), (5, 6, 3, 4); -- Bali

-- Data Custom Trip (minimal 30, sesuaikan user_id)
INSERT INTO `custom_trip` (`user_id`, `nama_trip`, `tanggal_mulai`, `tanggal_akhir`, `jumlah_peserta`, `status`, `total_harga`, `catatan_user`) VALUES
(1, 'Keluarga Andi ke Jogja', '2025-07-10', '2025-07-14', 4, 'dibayar', 5000000, 'Sertakan guide ramah anak.'),
(2, 'Bunga Solo Trip Bandung', '2025-07-15', '2025-07-18', 1, 'dipesan', 1800000, 'Fokus kuliner dan tempat Instagramable.'),
(3, 'Cahyo Family Bali Fun', '2025-07-20', '2025-07-25', 5, 'draft', 0, 'Ingin banyak aktivitas air.'),
(4, 'Dian & Teman ke Lombok', '2025-07-25', '2025-07-29', 3, 'dibayar', 6000000, 'Prioritaskan snorkeling.'),
(5, 'Eko Adventure Bromo Ijen', '2025-08-01', '2025-08-04', 2, 'selesai', 4000000, 'Ingin lihat blue fire.'),
(6, 'Fitri Honeymoon Ubud', '2025-08-05', '2025-08-09', 2, 'dipesan', 7000000, 'Villa private pool, yoga class.'),
(7, 'Gilang Fotografi Trip Raja Ampat', '2025-08-10', '2025-08-17', 1, 'dibayar', 20000000, 'Sewa kapal dan guide lokal untuk spot terbaik.'),
(8, 'Hesti Wisata Kuliner Palembang', '2025-08-18', '2025-08-20', 2, 'draft', 0, 'Cari semua jenis pempek.'),
(9, 'Indra Gathering Kantor Jakarta', '2025-08-22', '2025-08-24', 30, 'dipesan', 25000000, 'Outbound dan gala dinner.'),
(10, 'Jasmine Historical Tour Semarang', '2025-08-25', '2025-08-27', 2, 'dibayar', 2500000, 'Kunjungi semua museum dan Kota Lama.'),
(11, 'Kurniawan Derawan Diving', '2025-09-01', '2025-09-05', 4, 'selesai', 24000000, 'Sertakan whale shark point.'),
(12, 'Lina & Mario Prewedding Bali', '2025-09-06', '2025-09-10', 2, 'dipesan', 10000000, 'Spot foto sunrise dan sunset.'),
(13, 'Mario Family Goes To Malang', '2025-09-12', '2025-09-15', 4, 'draft', 0, 'Jatim Park wajib.'),
(14, 'Nina & Friends Bunaken Trip', '2025-09-16', '2025-09-20', 5, 'dibayar', 27500000, 'Semua peralatan diving disediakan.'),
(15, 'Oscar Solo Backpacking Flores', '2025-09-22', '2025-09-30', 1, 'dipesan', 8000000, ' overland, penginapan budget.'),
(16, 'Putri & Geng ke Bandung', '2025-10-01', '2025-10-04', 6, 'dibayar', 9000000, 'Wisata alam dan kafe hits.'),
(17, 'Rizky Jelajah Kalimantan', '2025-10-05', '2025-10-12', 1, 'draft', 0, 'Orangutan dan suku Dayak.'),
(18, 'Sari Meditation Retreat Ubud', '2025-10-14', '2025-10-18', 1, 'dipesan', 6500000, 'Tempat yang tenang dan private.'),
(19, 'Toni & Vina Anniversary Trip Jogja', '2025-10-20', '2025-10-23', 2, 'dibayar', 4500000, 'Romantic dinner include.'),
(20, 'Umar Jelajah Sejarah Jakarta', '2025-10-25', '2025-10-27', 1, 'selesai', 1200000, 'Semua museum sejarah.'),
(21, 'Vina & Wawan Explore Toba', '2025-11-01', '2025-11-04', 2, 'dipesan', 5000000, 'Penginapan view danau.'),
(22, 'Wawan Family Vacation Lombok', '2025-11-06', '2025-11-11', 4, 'draft', 0, 'Pantai ramah anak.'),
(23, 'Xena Adventure Komodo', '2025-11-13', '2025-11-16', 1, 'dibayar', 7800000, 'Liveaboard standar.'),
(24, 'Yudi Culinary Makassar', '2025-11-18', '2025-11-20', 2, 'dipesan', 3000000, 'Coto, Konro, Pallubasa.'),
(25, 'Zahra Shopping Trip Bandung', '2025-11-22', '2025-11-24', 3, 'dibayar', 4000000, 'Kunjungi semua FO terkenal.'),
(26, 'Ahmad & Tim ke Bromo', '2025-11-26', '2025-11-27', 10, 'selesai', 15000000, 'Include jeep dan penginapan.'),
(27, 'Bella Solo Healing Bali', '2025-12-01', '2025-12-07', 1, 'dipesan', 8000000, 'Yoga, spa, tempat sunyi.'),
(28, 'Charlie & Snoopy ke Kebun Raya', '2025-12-09', '2025-12-09', 2, 'dibayar', 500000, 'Piknik dan foto-foto.'),
(29, 'Diana Wonder Woman ke Borobudur', '2025-12-11', '2025-12-12', 1, 'draft', 0, 'Sunrise Borobudur.'),
(30, 'Edward & Bella Honeymoon ke Gili', '2025-12-14', '2025-12-18', 2, 'dipesan', 9000000, 'Private bungalow, candle light dinner.');


-- Data Rincian Custom Trip (beberapa untuk setiap custom trip)
-- Asumsi ID Custom Trip Keluarga Andi ke Jogja = 1, Bunga Solo Trip Bandung = 2, dst.
-- Asumsi ID Destinasi Candi Prambanan = 3, Malioboro = 4, Borobudur = 25 (dari data destinasi di atas)
-- Asumsi ID Kawah Putih = 2, Trans Studio Bandung = 7
INSERT INTO `rincian_custom_trip` (`custom_trip_id`, `destinasi_id`, `tanggal_kunjungan`) VALUES
(1, 3, '2025-07-11'), (1, 4, '2025-07-12'), (1, 25, '2025-07-13'),
(2, 2, '2025-07-16'), (2, 7, '2025-07-17');

-- Data Reservasi (minimal 30, campur antara paket dan custom trip)
-- Asumsi kode_reservasi unik
INSERT INTO `reservasi` (`trip_type`, `trip_id`, `kode_reservasi`, `tanggal_reservasi`, `status`) VALUES
('paket_perjalanan', 1, 'SJ-20250601-001', '2025-06-01', 'dibayar'),
('paket_perjalanan', 2, 'SJ-20250602-002', '2025-06-02', 'dipesan'),
('custom_trip', 1, 'SJ-20250603-003', '2025-06-03', 'dibayar'),
('paket_perjalanan', 3, 'SJ-20250604-004', '2025-06-04', 'pending'),
('custom_trip', 2, 'SJ-20250605-005', '2025-06-05', 'dipesan'),
('paket_perjalanan', 4, 'SJ-20250606-006', '2025-06-06', 'selesai'),
('paket_perjalanan', 5, 'SJ-20250607-007', '2025-06-07', 'dibayar'),
('custom_trip', 4, 'SJ-20250608-008', '2025-06-08', 'dibayar'),
('paket_perjalanan', 6, 'SJ-20250609-009', '2025-06-09', 'dipesan'),
('custom_trip', 5, 'SJ-20250610-010', '2025-06-10', 'selesai'),
('paket_perjalanan', 7, 'SJ-20250611-011', '2025-06-11', 'pending'),
('custom_trip', 6, 'SJ-20250612-012', '2025-06-12', 'dipesan'),
('paket_perjalanan', 8, 'SJ-20250613-013', '2025-06-13', 'dibayar'),
('custom_trip', 7, 'SJ-20250614-014', '2025-06-14', 'dibayar'),
('paket_perjalanan', 9, 'SJ-20250615-015', '2025-06-15', 'selesai'),
('custom_trip', 9, 'SJ-20250616-016', '2025-06-16', 'dipesan'),
('paket_perjalanan', 10, 'SJ-20250617-017', '2025-06-17', 'dibayar'),
('custom_trip', 10, 'SJ-20250618-018', '2025-06-18', 'dibayar'),
('paket_perjalanan', 11, 'SJ-20250619-019', '2025-06-19', 'pending'),
('custom_trip', 11, 'SJ-20250620-020', '2025-06-20', 'selesai'),
('paket_perjalanan', 12, 'SJ-20250621-021', '2025-06-21', 'dipesan'),
('custom_trip', 12, 'SJ-20250622-022', '2025-06-22', 'dipesan'),
('paket_perjalanan', 13, 'SJ-20250623-023', '2025-06-23', 'dibayar'),
('custom_trip', 14, 'SJ-20250624-024', '2025-06-24', 'dibayar'),
('paket_perjalanan', 14, 'SJ-20250625-025', '2025-06-25', 'penuh'), -- Dari paket perjalanan
('custom_trip', 15, 'SJ-20250626-026', '2025-06-26', 'dipesan'),
('paket_perjalanan', 15, 'SJ-20250627-027', '2025-06-27', 'tersedia'), -- Dari paket perjalanan
('custom_trip', 16, 'SJ-20250628-028', '2025-06-28', 'dibayar'),
('custom_trip', 18, 'SJ-20250629-029', '2025-06-29', 'dipesan'),
('custom_trip', 19, 'SJ-20250630-030', '2025-06-30', 'dibayar');

-- Data Penumpang (beberapa untuk setiap reservasi)
INSERT INTO `penumpang` (`reservasi_id`, `nama_penumpang`, `jenis_kelamin`, `tanggal_lahir`, `nomor_telepon`, `email`) VALUES
(1, 'Andi Wijaya', 'pria', '1980-01-01', '081211110001', 'andi.wijaya@example.com'),
(1, 'Istri Andi', 'wanita', '1982-02-02', '081211110001', 'andi.wijaya@example.com'),
(3, 'Andi Wijaya', 'pria', '1980-01-01', '081211110001', 'andi.wijaya@example.com'),
(3, 'Anak Andi 1', 'pria', '2010-03-03', '081211110001', 'andi.wijaya@example.com'),
(3, 'Anak Andi 2', 'wanita', '2012-04-04', '081211110001', 'andi.wijaya@example.com'),
(2, 'Bunga Citra', 'wanita', '1995-05-05', '081211110002', 'bunga.citra@example.com'),
(5, 'Bunga Citra', 'wanita', '1995-05-05', '081211110002', 'bunga.citra@example.com');

-- Data Pembayaran (untuk beberapa reservasi)
INSERT INTO `pembayaran` (`reservasi_id`, `metode_pembayaran`, `jumlah_pembayaran`, `tanggal_pembayaran`, `status_pembayaran`) VALUES
(1, 'kartu kredit', 1500000, '2025-06-01', 'lunas'),
(3, 'transfer', 5000000, '2025-06-03', 'lunas'),
(4, 'transfer', 3000000, '2025-06-04', 'pending'),
(7, 'kartu kredit', 4500000, '2025-06-07', 'lunas'),
(8, 'transfer', 6000000, '2025-06-08', 'lunas');

-- Data Itenerary (beberapa contoh, biasanya digenerate oleh aplikasi)
-- Untuk paket perjalanan Jakarta City Tour (ID Reservasi = 1, ID Paket = 1)
INSERT INTO `itenerary` (`trip_type`, `trip_id`, `destinasi_id`, `urutan_kunjungan`, `tanggal_kunjungan`, `durasi_jam`, `waktu_mulai`, `waktu_selesai`, `catatan`) VALUES
('paket_perjalanan', 1, 1, 1, '2025-07-01', 4, '09:00:00', '13:00:00', 'Kumpul di lobi hotel jam 08.30'),
('paket_perjalanan', 1, 6, 2, '2025-07-01', 4, '14:00:00', '18:00:00', 'Makan malam bebas di area Kota Tua.');
-- Untuk custom trip Keluarga Andi ke Jogja (ID Reservasi = 3, ID Custom Trip = 1)
INSERT INTO `itenerary` (`trip_type`, `trip_id`, `destinasi_id`, `urutan_kunjungan`, `tanggal_kunjungan`, `durasi_jam`, `waktu_mulai`, `waktu_selesai`, `catatan`) VALUES
('custom_trip', 1, 3, 1, '2025-07-11', 6, '10:00:00', '16:00:00', 'Bawa topi dan air minum.'),
('custom_trip', 1, 4, 2, '2025-07-12', 4, '18:00:00', '22:00:00', 'Belanja oleh-oleh dan kuliner malam.');