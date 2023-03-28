public static String Enkrip(String kunci, String plaintext){
		String cipher="";
		int panjangPlain = plaintext.length();
		int panjangKunci = kunci.length();
		for (int i=0; i<panjangPlain; i++){
			int idxKunci = i % panjangKunci;
			
			int ASCIIEnkrip = ( CharToASCII(plaintext.charAt(i)) + CharToASCII(kunci.charAt(idxKunci)) ) % 256;
			//System.out.println(ASCIIToChar(ASCIIEnkrip));
			cipher = cipher+ASCIIToChar(ASCIIEnkrip);
		}
		return cipher;
	}
--------------------

public LogikaButtonow(){
		zapiszKod = new ZapiszKod();
		kompiluj = new Kompiluj();
		sciezki = new Sciezki();
		//sprawdzZadanie = new SprawdzZadanie();
	}
--------------------

@Override
	public void perbarui(float waktuDelta) {
		// TODO Auto-generated method stub
		Grafik grafik = permainan.getGrafik();
		ManajemenAset.grafikAktorBelakang = grafik.pixmapBaru(
				"gfx/aktor_belakang.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikAktorDepan = grafik.pixmapBaru(
				"gfx/aktor_depan.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikAktorKanan = grafik.pixmapBaru(
				"gfx/aktor_kanan.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikAktorKiri = grafik.pixmapBaru("gfx/aktor_kiri.png",
				PixmapFormat.ARGB4444);
		ManajemenAset.grafikHalangan = grafik.pixmapBaru("gfx/halangan.png",
				PixmapFormat.ARGB4444);
		ManajemenAset.grafikHalanganAtas = grafik.pixmapBaru(
				"gfx/halangan_atas.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikHalanganBawah = grafik.pixmapBaru(
				"gfx/halangan_bawah.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikHalanganTubuh = grafik.pixmapBaru(
				"gfx/halangan_tubuh.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikKunci = grafik.pixmapBaru("gfx/kunci.png",
				PixmapFormat.ARGB4444);
		ManajemenAset.grafikLatarBelakang = grafik.pixmapBaru(
				"gfx/latarbelakang.png", PixmapFormat.RGB565);
		ManajemenAset.grafikLBPermainan = grafik.pixmapBaru(
				"gfx/bgpermainan.png", PixmapFormat.RGB565);
		ManajemenAset.grafikLogo = grafik.pixmapBaru("gfx/logo.png",
				PixmapFormat.ARGB4444);
		ManajemenAset.grafikPintuTerbuka = grafik.pixmapBaru(
				"gfx/pintu_terbuka.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikPintuTertutup = grafik.pixmapBaru(
				"gfx/pintu_tertutup.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikTombol = grafik.pixmapBaru("gfx/tombol.png",
				PixmapFormat.ARGB4444);
		ManajemenAset.grafikTombolDitekan = grafik.pixmapBaru(
				"gfx/tombol_ditekan.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikTombolMulai = grafik.pixmapBaru(
				"gfx/tombolmulai.png", PixmapFormat.ARGB4444);
		ManajemenAset.grafikUbinSatu = grafik.pixmapBaru("gfx/ubin_satu.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinDua = grafik.pixmapBaru("gfx/ubin_dua.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinTiga = grafik.pixmapBaru("gfx/ubin_tiga.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinEmpat = grafik.pixmapBaru("gfx/ubin_empat.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinLima = grafik.pixmapBaru("gfx/ubin_lima.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinEnam = grafik.pixmapBaru("gfx/ubin_enam.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinTujuh = grafik.pixmapBaru("gfx/ubin_tujuh.png",
				PixmapFormat.RGB565);
		ManajemenAset.grafikUbinDelapan = grafik.pixmapBaru(
				"gfx/ubin_delapan.png", PixmapFormat.RGB565);
		ManajemenAset.grafikUbinSembilan = grafik.pixmapBaru(
				"gfx/ubin_sembilan.png", PixmapFormat.RGB565);
		ManajemenAset.grafikAngka = grafik.pixmapBaru("gfx/angka.png",
				PixmapFormat.ARGB4444);

		ManajemenAset.suaraMusuhDatang = permainan.getBunyi().suaraBaru(
				"sfx/monster-come.ogg");
		ManajemenAset.suaraPermainanSelesai = permainan.getBunyi().suaraBaru(
				"sfx/game-over.ogg");
		ManajemenAset.suaraTombol = permainan.getBunyi().suaraBaru(
				"sfx/sfxclick.ogg");

		permainan.setLayar(new LayarMainMenu(permainan));
	}
--------------------

