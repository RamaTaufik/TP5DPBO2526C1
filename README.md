> "Saya Rama Taufik Azkia dengan NIM 2508497 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin."

*— Janji*

# GARIS BESAR DESIGN
Aplikasi ini merupakan aplikasi pengelolaan data CRUD personil kombatan di suatu organisasi keamanan. Setiap personil memiliki kategori role masing-masing serta level yang menandakan tingkatan kemahiran mereka.

Aplikasi ini dibuat dengan memanfaatkan Java Swing, semacam *tool* yang memfasilitasi pembuatan GUI serupa Windows *desktop app*, ditulis dalam bahasa Java. Selain itu, aplikasi ini juga mengimplementasikan JDBC, yang memungkinkan koneksi *database* MySQL dalam Java (*cek [CATATAN](#catatan)*).

Terdapat 1 kelas, yaitu `person`, dengan atribut berikut: 

  1. `ID`, kode unik khusus yang menjadi pembeda utama antar personil;
  2. Nama (`name`), untuk nama personil;
  3. Tahun lahir (`birth_y`), untuk tahun lahir personil;
  4. Kategori (`category`), untuk kategori personil, didasarkan pada kemampuan kombat masing-masing. Ada 7: *Saber* (ahli bela diri), *Archer* (ahli panah), *Lancer* (ahli tombak), *Rider* (ahli tunggangan), *Caster* (ahli sihir), *Assassin* (ahli pembunuh), dan *Berserker* (*catch-all* untuk personil dengan kemampuan di luar kendali);
  5. `Level`, merujuk pada tingkat kemahiran tiap personil, mulai dari yang terrendah (level 1) hingga yang tertinggi (level 100).

Dari 5 atribut di atas, dalam *database*-nya sendiri semua dibuat NOT NULL tanpa DEFAULT, namun khusus `birth_y`, di-implementasikan DEFAULT(0) pada kode program ketika menambah/mengubah, sehingga dapat dikosongkan.

# ALUR
Saat pertama kali membuka aplikasi, pengguna akan disambut menu seperti berikut:

![Menu](docs/home.png)

Pada awalnya, tersedia 7 data *dummy*, satu untuk masing-masing kategori. Bagian input data dibuat fleksibel, dapat digunakan dalam fungsi tambah dan ubah data. Untuk melalukan perubahan data sendiri, pertama klik baris data yang ingin di ubah, lalu ubah data pada bagian input. Di saat yang bersamaan, akan juga muncul tombol *delete* yang jelas berfungsi untuk menghapus baris data yang sedang dipilih.

![Select row](docs/select_row.png)

Terakhir, ada tombol *cancel*. Tombol ini akan mengosongkan bagian input, juga membatalkan operasi perubahan data.

# DOKUMENTASI
1. Tambah data

| Keadaan | *screenshot* |
| --- | --- |
| Sebelum tambah data | ![Before create](docs/create_before.png) |
| Menambah data | ![Create](docs/create.png) |
| Sesudah tambah data | ![After create](docs/create_after.png) |
2. Ubah data

| Keadaan | *screenshot* |
| --- | --- |
| Sebelum ubah data | ![Before update](docs/update_before.png) |
| Mengubah data | ![Update](docs/update.png) |
| Sesudah ubah data | ![After update](docs/update_after.png) |
3. Hapus data

| Keadaan | *screenshot* |
| --- | --- |
| Sebelum hapus data | ![Before delete](docs/delete_before.png) |
| Menghapus data | ![Delete](docs/delete.png) |
| Sesudah hapus data | ![After delete](docs/delete_after.png) |
4. *Error handling*

| Keadaan | *screenshot* |
| --- | --- |
| ID duplikat saat tambah atau ubah data (kecuali ID tidak dirubah) | ![Duplicate ID](docs/duplicate_id.png) |
| Mengosongkan data wajib (ditandai '*' pada label) | ![Empty](docs/empty_required.png) |
| Memasukkan angka negatif pada input angka | ![Negative to positive integer](docs/neg_to_int.png) |
| Memasukkan angka desimal pada input angka | ![Decimal to positive integer](docs/dec_to_int.png) |
| Memasukkan bukan angka pada input angka | ![NaN to integer](docs/str_to_int.png) |

# CATATAN
Karena adanya implementasi *database* MySQL, maka perlu di aktifkan terlebih dulu SQL *server* seperti menggunakan Laragon atau XAMPP. Secara *default*, aplikasi ini diatur dengan MySQL yang di set pada *port* 3306, namun jika SQL berada di *port* lain atau ingin mengganti *user* yang digunakan, maka konfigurasi bisa diatur pada [kelas Database](PersonData/src/Database.java). Setelah MySQL aktif, jangan lupa untuk melakukan *import* pada *file* [db_people.sql](db_people.sql) terlebih dulu, dan pastikan belum ada *database* dengan nama `db_people` agar data tidak bercampur.

*All credits go to Kinoko Nasu, Type-moon, and the colorful book of human history & mythologies for all references made.*

<img src="docs/_watermark.gif" width="250px" align="right">