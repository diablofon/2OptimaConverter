package eu.ksiegowasowa.convert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import eu.ksiegowasowa.convert.pojo.Faktura;
import eu.ksiegowasowa.convert.pojo.Faktura.TypFaktury;
import eu.ksiegowasowa.convert.pojo.Pozycja;

public class EppReader {

	private static final String INFO = "[INFO]";
	private static final String NAGLOWEK = "[NAGLOWEK]";
	private static final String ZAWARTOSC = "[ZAWARTOSC]";
	private static final String TOKEN = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
	private static final String FAKTURA_SPRZEDAZOWA = "FS";
	private static final String FAKTURA_ZAKUPOWA = "FZ";

	public EppReader() {
	}

	@SuppressWarnings("resource")
	public List<Faktura> readFile(String fileName) throws ConvertException {
		if (fileName.isEmpty()) {
			throw new ConvertException("Nie podano nazwy pliku");
		}
		List<Faktura> faktury = new ArrayList<>();

		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "Cp1250"));
		} catch (FileNotFoundException e) {
			throw new ConvertException("Nie znaleziono pliku o nazwie " + fileName + ". Wyjątek: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new ConvertException("Problem z enkodowaniem pliku " + fileName + ". Wyjątek: " + e.getMessage());
		}
		boolean nextNaglowek = false;
		boolean nextZawartosc = false;
		boolean nextInfo = false;
		Faktura faktura = null;
		try {
			String line = null;
			int counter = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				counter++;
				if (line.isEmpty()) {
					if (nextZawartosc) {
						nextZawartosc = false;
						if (faktura != null) {
							faktury.add(faktura);
						}
					}
					continue;
				}
				if (line.equals(NAGLOWEK)) {
					nextNaglowek = true;
				} else if (line.equals(ZAWARTOSC)) {
					nextZawartosc = true;
				} else if (line.equals(INFO)) {
					nextInfo = true;
				} else {
					if (nextNaglowek) {
						// faktura = new Faktura();
						faktura = handleNaglowek(line);
						nextNaglowek = false;
					} else if (nextZawartosc) {
						if (faktura != null) {
							handleZawartosc(line, faktura);
						}
					} else if (nextInfo) {
						// w info nic nie ma, na razie olewam
						// handleInfo(line, faktura);
						nextInfo = false;
					}
				}
			}
		} catch (IOException e) {
			throw new ConvertException("Wystąpił błąd podczas czytania pliku: " + e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return faktury;
	}

	private Faktura handleNaglowek(String line) throws ConvertException {
		String dateFormat = "yyyyMMdd";
		int RODZAJ_FAKTURY_INDEX = 0;
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		Faktura f = null;
		String[] tokens = line.split(TOKEN);
		switch (trimSemicolons(tokens[RODZAJ_FAKTURY_INDEX])) {
		/*case FAKTURA_SPRZEDAZOWA:
			f = new Faktura();
			f.typ = TypFaktury.Sprzedaz;
			break;*/
		case FAKTURA_ZAKUPOWA:
			f = new Faktura();
			f.typ = TypFaktury.Zakup;
			break;
		default:
			System.out.println("Omijam fakturę typu: " + trimSemicolons(tokens[RODZAJ_FAKTURY_INDEX]));
			return f;
		}
		try {
			for (int index = 0; index < tokens.length; index++) {
				String token = trimSemicolons(tokens[index]);
				switch (index) {
				case 4: // numer faktury kosztowej
					f.numer2 = token;
					break;
				case 6: // numer faktury sprzedazowej
					f.numer = token;
					break;
				case 12: // skrocona nazwa
					f.klient.skroconaNazwa = token;
					break;
				case 13: // nazwa odbiorcy
					f.klient.nazwa = token;
					break;
				case 14: // miasto
					f.klient.miasto = token;
					break;
				case 15: // kod pocztowy
					f.klient.kodPocztowy = token;
					break;
				case 16: // ulica
					f.klient.ulica = token;
					break;
				case 17: // nip
					f.klient.nip = token;
					break;
				case 18: // rejestr
					f.rejestr = token;
					break;
				case 19:
					f.opis = token;
					break;
				case 21: // data wystawienia2
					f.dataWystawienia2 = formatter.parse(token.substring(0, dateFormat.length()));
					break;
				case 22: // data sprzedazy
					f.dataSprzedazy = formatter.parse(token.substring(0, dateFormat.length()));
					break;
				case 27: // wartosc netto
					f.wartoscNetto = Float.valueOf(token);
					break;
				case 28: // kwota vat
					f.wartoscVAT = Float.valueOf(token);
					break;
				case 29: // kwota brutton
					f.wartoscBrutto = Float.valueOf(token);
					break;
				case 33: // forma platnosci
					f.formaPlatnosci = token;
					break;
				case 34: // data sprzedazy
					f.terminPlatnosci = formatter.parse(token.substring(0, dateFormat.length()));
					break;
				case 59:
					f.klient.kraj = token;
				default:
					break;
				}
			}
		} catch (Exception e) {
			throw new ConvertException(e.getMessage());
		}
		return f;
	}

	private void handleZawartosc(String line, Faktura f) throws ConvertException {
		String[] tokens = line.split(TOKEN);
		try {
			Pozycja pozycja = new Pozycja();
			for (int index = 0; index < tokens.length; index++) {
				String token = trimSemicolons(tokens[index]);
				switch (index) {
				case 0: // stawka vat str
					pozycja.stawkaVatStr = token;
					break;
				case 1: // stawka vat
					pozycja.stawkaVat = Float.valueOf(token);
					break;
				case 2: // kwota netto
					pozycja.kwotaNetto = Float.valueOf(token);
					break;
				case 3: // kwota vat
					pozycja.kwotaVat = Float.valueOf(token);
					break;
				case 4: // stawka vat str
					pozycja.kwotaBrutto = Float.valueOf(token);
					break;
				default:
					break;
				}
			}
			f.pozycje.add(pozycja);
		} catch (Exception e) {
			throw new ConvertException(e.getMessage());
		}
	}

	private void handleInfo(String line, Faktura f) throws ConvertException {
		String dateFormat = "yyyyMMddhhmmss";
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String[] tokens = line.split(TOKEN);
		try {
			for (int index = 0; index < tokens.length; index++) {
				String token = trimSemicolons(tokens[index]);
				switch (index) {
				case 19: // data wystawienia
					f.dataWystawienia = formatter.parse(token.substring(0, dateFormat.length()));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			throw new ConvertException(e.getMessage());
		}
	}

	private String trimSemicolons(String s) {
		if (s != null) {
			s = s.trim();
			if (s.length() > 1 && s.startsWith("\"") && s.endsWith("\"")) {
				return s.substring(1, s.length() - 1);
			}
			return s;
		} else {
			return "";
		}
	}
}
