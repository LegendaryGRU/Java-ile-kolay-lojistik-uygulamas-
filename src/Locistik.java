import java.util.NoSuchElementException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Locistik
{
	private ListeDugumu front;
	private ListeDugumu rear;
	
	private int uzunluk;				// Kuyruğu ayrı bir sınıfta yapmak yerine içeri yerleştirdim
	private int sayac = 1;
	
	public class Kisi
	{
		private String isim;						// ListeDugumunden farklı olarak kisi sınıfı yaptım burada personllerin bilgileri tutulucak.
		private String soyIsim;
		private int mesafe;
		
		public Kisi(String isim, String soyIsim, int mesafe)
		{
			this.isim = isim;
			this.soyIsim = soyIsim;
			this.mesafe = mesafe;
		}
	}
	
	public class ListeDugumu
	{
		private int mesafeVerisi;
		private Kisi kisi;						
		private ListeDugumu sonraki;
		
		public ListeDugumu(int veri, Kisi kisi)
		{
			this.mesafeVerisi = veri;
			this.kisi = kisi;
		}
	}
	
	public Locistik()
	{
		front = null;
		rear = null;
		uzunluk = 0;
	}
	
	public boolean bosMu() 
	{
		return uzunluk == 0;
	}
	
	public void enqueue(int veri, Kisi kisi) 
	{
		ListeDugumu yeniDugum = new ListeDugumu(veri, kisi);
		
		if(bosMu()) 
		{
			front = yeniDugum;
		}
		else 
		{
			rear.sonraki = yeniDugum;
		}
		rear = yeniDugum;
		uzunluk++;
	}
	
	public void yazdir() 
	{
		if(bosMu()) 
		{
			return;
		}
		JFrame yazMenu = new JFrame();
		JTextArea area = new JTextArea();
		
		JButton geriDon = new JButton();
		geriDon.setText("Geri dön");			// kişileri yazdirmak için yeni bir pencere açıp JTextArea ile yazdırılır.
		geriDon.setBounds(300, 175, 100, 100);
		
		JLabel bgWal = new JLabel();
		ImageIcon imgg = new ImageIcon(Locistik.class.getResource("/bgWall.png"));
		bgWal.setIcon(imgg);
		bgWal.setBounds(0, 0, 500, 500);
		
		area.setLocation(0, 0);
		area.setVisible(true);
		area.setSize(200,500);
		
		ListeDugumu gecici = front;
		int index = 1;
		
		while(gecici != null) 
		{
			area.append(index + "-> " + gecici.kisi.isim + " , " + gecici.kisi.soyIsim + " => " + gecici.kisi.mesafe + "\n");
			gecici = gecici.sonraki;
			index++;
		}
		
		geriDon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 		// geriDon butonu ile yeni açılan pencere kapatılır.
			{
				yazMenu.setVisible(false);
			}
		});
		

		yazMenu.add(geriDon);
		yazMenu.add(area);
		yazMenu.add(bgWal);
		yazMenu.setLocation(550, 200);		
		yazMenu.setSize(500,500);
		yazMenu.setLayout(null);
		yazMenu.setVisible(true);
		
	}
	
	public ListeDugumu dequeue() 
	{
		if(bosMu()) 
		{
			throw new NoSuchElementException("Harekette araç yoktur.");
		}
		ListeDugumu gecici = front;
		
		front = front.sonraki;
		gecici.sonraki = null;
		uzunluk--;
		return gecici;
	}

	public void ekle(String adGetir, String soyadGetir, int mesafeGetir) 
	{
		String ad, soyad;
		int mesafe;
		
		ad = adGetir;
		soyad = soyadGetir;
		mesafe = mesafeGetir;
		
		Kisi kisi = new Kisi(ad, soyad, mesafe);
		
		int veriAl = mesafe;
		
		enqueue(veriAl, kisi);
		System.out.println("\n");			//her eklemeden sonra sıralama fonksiyonu çalştırılır.
		Sirala();
	}
	
	
	public void Sirala() 
	{
		ListeDugumu[] donusturDizi = new ListeDugumu[50]; 	// ListeDugumu tipinden bir dizi olusturuluyor.

		donusturDizi[0] = null;
		ListeDugumu gecici = front;

		int sayac = 1;							
		while (gecici != null) {
			donusturDizi[sayac] = gecici; 		// front' dan baslayıp rear'a kadar tüm dügümleri diziye ekler.
			sayac++;
			gecici = gecici.sonraki;
		}
		// --------------------------------------------------------------
		
		ListeDugumu geciciV2;
		
		for (int i = 1; i < sayac-1; i++) 			// Sonra Dizi'yi mesafe verilerine göre sıralarız
		{
		      int min = i;
		      for (int j = i + 1; j < sayac; j++) 
		      {
		    	  int onceki = donusturDizi[j].mesafeVerisi;
		    	  int sonraki = donusturDizi[min].mesafeVerisi;
		    			  
		    	  if ( onceki < sonraki) 
		    	  {
		    		  min = j;
		    	  } 
			  }
		      geciciV2 = donusturDizi[i];
		      donusturDizi[i] = donusturDizi[min];
		      donusturDizi[min] = geciciV2;
		}
		
		//-------------------------
		
		for(int j = 0; j < sayac-1; j++) 	//Sirasiz kuyruk'da eleman kalmıyana kadar silinir.
		{
			//System.out.println("sildi");
			dequeue();
		}
		
		for(int k = 0, j = 1; k < sayac - 1; k++, j++)  		//Bos kuyruga sirali dizideki tüm elemanları ekleriz.
		{
			enqueue(donusturDizi[j].mesafeVerisi, donusturDizi[j].kisi);
		}
		
		System.out.println("\nSiralandi.\n");
	}
	
	
	public static void main(String[] args) 
	{
		Locistik lcs = new Locistik();
	
		JFrame giris = new JFrame("--- Lojistik ---"); 	// pencere burada oluşturuluyor
	
		
		JLabel bgWal = new JLabel();
		ImageIcon imgg = new ImageIcon(Locistik.class.getResource("/bgWall.png"));
		bgWal.setIcon(imgg);
		bgWal.setBounds(0, 0, 500, 500);
	
		JLabel lcsLogo = new JLabel();
		ImageIcon logo = new ImageIcon(Locistik.class.getResource("/tirLogo.png"));
		lcsLogo.setIcon(logo);
		lcsLogo.setBounds(250, 150, 200, 150);
		
		
		JButton ekleButton = new JButton();
		JButton silButton = new JButton();
		JButton yazButton = new JButton();
		JButton yazdirMenuKapatma = new JButton();
		JButton cikis = new JButton();
		
		
		ekleButton.setText("Ekle");
		ekleButton.setBounds(80, 50, 100, 70);

		silButton.setText("Sefer bitti");
		silButton.setBounds(80, 150, 100, 70);		

		yazButton.setText("Yazdir");
		yazButton.setBounds(80, 250, 100, 70);

		cikis.setText("Çıkış");
		cikis.setBounds(80, 350, 100, 70);
		
		
		
		ekleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				//ekleme butonuna basıldığında  ekrana input almamıza yardım eden diyalog mesajları geliyor.

				while (true) {

					String ad = JOptionPane.showInputDialog(giris, "Adınızı giriniz");

					if (ad == null || ad.isEmpty()) {
						JOptionPane.showMessageDialog(giris, "Lütfen geçerli bir isim giriniz!", "Hata",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					String soyad = JOptionPane.showInputDialog(giris, "Soyadınızı giriniz");

					if (soyad == null || soyad.isEmpty()) {
						JOptionPane.showMessageDialog(giris, "Lütfen geçerli bir soyisim giriniz!", "Hata",			//Hata kontrolü burada yapılır.
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					int mesafe = 0;
					
					try {
						mesafe += Integer.parseInt(JOptionPane.showInputDialog(giris, "Sefer mesafesini giriniz"));	  
					} 
					catch (Exception e1) {
						JOptionPane.showMessageDialog(giris, "Lütfen geçerli bir sayi giriniz!", "Hata",	 
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					lcs.ekle(ad, soyad, mesafe);
					break;
				}
			}
		});
		
		silButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {			
				
				if(lcs.front == null) 
				{
					JOptionPane.showMessageDialog(giris, "Liste de kimse yok", "Hata",JOptionPane.ERROR_MESSAGE);	
				}
				else 
				{
					lcs.dequeue();
				}
			}
		});
		
		
		yazButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(lcs.front == null) 
				{
					JOptionPane.showMessageDialog(giris, "Henüz kimse eklenmdi", "Hata",JOptionPane.ERROR_MESSAGE);
				}
				else 
				{
					lcs.yazdir();
					
				}
			}
		});
		
		
		cikis.addActionListener(new ActionListener() {
 			@Override
			public void actionPerformed(ActionEvent e) 
			{
				giris.setVisible(false);
			}
		});


		giris.add(cikis);
		giris.add(yazButton);
		giris.add(ekleButton);					//pencereye üstte tanımladığımız butonları ekliyoruz.
		giris.add(silButton);
		giris.add(lcsLogo);
		giris.add(bgWal);
		
		giris.setLocation(550, 200);
		giris.setSize(500, 500);
		giris.setLayout(null);										//Pencerenin özellikleri burada oluşturuluyor
		giris.setVisible(true);
		giris.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
}

