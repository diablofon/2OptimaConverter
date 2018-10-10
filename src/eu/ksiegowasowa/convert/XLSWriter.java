package eu.ksiegowasowa.convert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import eu.ksiegowasowa.convert.pojo.Faktura;
import eu.ksiegowasowa.convert.pojo.Klient;

public class XLSWriter {
	
	private enum Kolumna {
		Kod(0), Nazwa(1), Nazwa2(2), Nazwa3(3), Telefon(4), Telefon2(5), TelefonSms(6), Fax(7), Ulica(8), NrDomu(9), NrLokalu(10),
		KodPocztowy(11), Poczta(12), Miasto(13), Kraj(14), Wojewodztwo(15), Powiat(16), Gmina(17), URL(18), Grupa(19), OsobaFizyczna(20),
		NIP(21), NIPKraj(22), Zezwolenie(23), Regon(24), Pesel(25), Email(26), BankRachunerNr(27), BankNazwa(28), Osoba(29), Opis(30),
		Rodzaj(31), PlatnikVAT(32), PodatnikVATCzynny(33), Eksport(34), LimityKredytu(35), Termin(36), FormaPlatnosci(37), Ceny(38),
		CenyNazwa(39), Upust(40), NieNaliczajOdsetek(41), MetodaKasowa(42), WindykacjaEMail(43), WindykacjaTelefonSMS(44),
		AlgorytmNettoBrutto(45), Waluta(46);
		
		private int nrKolumny;
		Kolumna(int nrKolumny) {
			this.nrKolumny = nrKolumny;
		}
		int getNrKolumny() {
			return this.nrKolumny;
		}
	}

	public XLSWriter() {
	};

	public void writeFile(List<Faktura> faktury, String fileName) throws ConvertException {
		
		List<String> clientName = new ArrayList<String>();
		try {

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Kontrahenci");  

            HSSFRow rowhead = sheet.createRow((short)0);
            for (Kolumna k: Kolumna.values()) {
                rowhead.createCell(k.getNrKolumny()).setCellValue(k.name());            	
            }
            
            int nextRowNumber = 1;
            for (Faktura f : faktury) {
            	Klient klient = f.klient;
            	if (!clientName.contains(klient.nazwa)) {
            		clientName.add(klient.nazwa);
                    HSSFRow row = sheet.createRow(nextRowNumber);
                    row.createCell(Kolumna.Kod.getNrKolumny()).setCellValue(klient.skroconaNazwa.substring(0, 20));
                    row.createCell(Kolumna.Nazwa.getNrKolumny()).setCellValue(klient.nazwa);
                    row.createCell(Kolumna.Ulica.getNrKolumny()).setCellValue(klient.ulica);
                    row.createCell(Kolumna.KodPocztowy.getNrKolumny()).setCellValue(klient.kodPocztowy);
                    row.createCell(Kolumna.Poczta.getNrKolumny()).setCellValue(klient.miasto);
                    row.createCell(Kolumna.Miasto.getNrKolumny()).setCellValue(klient.miasto);
                    row.createCell(Kolumna.Kraj.getNrKolumny()).setCellValue(klient.kraj);
                    row.createCell(Kolumna.NIP.getNrKolumny()).setCellValue(klient.nip);
                    row.createCell(Kolumna.Rodzaj.getNrKolumny()).setCellValue("O");
                    row.createCell(Kolumna.PlatnikVAT.getNrKolumny()).setCellValue(1);
                    row.createCell(Kolumna.PodatnikVATCzynny.getNrKolumny()).setCellValue(1);
                    row.createCell(Kolumna.FormaPlatnosci.getNrKolumny()).setCellValue("gotówka");
                    row.createCell(Kolumna.CenyNazwa.getNrKolumny()).setCellValue("domyślna");
                    row.createCell(Kolumna.Eksport.getNrKolumny()).setCellValue(0);
                    nextRowNumber++;
            	}
            }

            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Plik z kontrahentami gotowy, wyeksportowano " + (nextRowNumber-1) + "kontrahentów");

		} catch (IOException e) {
			throw new ConvertException(e.getMessage());
		}
	}
	
}
