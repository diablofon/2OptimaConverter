package eu.ksiegowasowa.convert;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.ksiegowasowa.convert.pojo.Faktura;
import eu.ksiegowasowa.convert.pojo.Pozycja;

public class XMLWriter {
	
	public static final String EMPTY_CDATA = "__^__";
	
	Document doc;

	public XMLWriter() throws ConvertException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// root elements
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new ConvertException(e.getMessage());
		}
	};

	public void writeFile(List<Faktura> faktury, String fileName) throws ConvertException {
		
		try {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			Element rootElement = doc.createElement("ROOT");
			rootElement.setAttribute("xmlns", "http://www.comarch.pl/cdn/optima/offline");
			doc.appendChild(rootElement);

			Element rejestrySprzedazyVat = createElementAndAppend("REJESTRY_SPRZEDAZY_VAT", rootElement, null, false);
			createElementAndAppend("WERSJA", rejestrySprzedazyVat, "2.00", false);
			createElementAndAppend("BAZA_ZRD_ID", rejestrySprzedazyVat, "MSTRA", false);
			createElementAndAppend("BAZA_DOC_ID", rejestrySprzedazyVat, "MSTRA", false);
			
			for (Faktura faktura : faktury) {
				Element rejestrVat = createElementAndAppend("REJESTR_SPRZEDAZY_VAT", rejestrySprzedazyVat, null, false);
				createElementAndAppend("MODUL", rejestrVat, "Handel", false);
				createElementAndAppend("TYP", rejestrVat, "Faktura sprzedaży", false);
				createElementAndAppend("REJESTR", rejestrVat, "SPRZEDAŻ"/*faktura.rejestr*/, true);
				createElementAndAppend("DATA_WYSTAWIENIA", rejestrVat, df.format(faktura.dataWystawienia2), true);
				createElementAndAppend("DATA_SPRZEDAZY", rejestrVat, df.format(faktura.dataSprzedazy), true);
				createElementAndAppend("TERMIN", rejestrVat, df.format(faktura.terminPlatnosci), true); // termin platnosci 
				createElementAndAppend("NUMER", rejestrVat, faktura.numer, true);
				createElementAndAppend("KOREKTA", rejestrVat, "Nie", false);
				createElementAndAppend("KOREKTA_NUMER", rejestrVat, "", true);
				createElementAndAppend("METODA_KASOWA", rejestrVat, "Nie", false);
				createElementAndAppend("FISKALNA", rejestrVat, "Nie", false);
				createElementAndAppend("DETALICZNA", rejestrVat, "Nie", false);
				createElementAndAppend("WEWNETRZNA", rejestrVat, "Nie", false);
				createElementAndAppend("EKSPORT", rejestrVat, "Nie", false);
				createElementAndAppend("FINALNY", rejestrVat, "Nie", false);
				createElementAndAppend("PODATNIK_CZYNNY", rejestrVat, "Nie", false);
				createElementAndAppend("IDENTYFIKATOR_KSIEGOWY", rejestrVat, faktura.numer, true);
				
				//podmiot
				createElementAndAppend("TYP_PODMIOTU", rejestrVat, "kontrahent", true);
				createElementAndAppend("PODMIOT", rejestrVat, faktura.klient.skroconaNazwa.substring(0,  20), true);
				// podmio id createElementAndAppend("PODMIOT_ID", rejestrVat, "", true);
				createElementAndAppend("PODMIOT_NIP", rejestrVat, faktura.klient.nip, true);
				createElementAndAppend("NAZWA1", rejestrVat, faktura.klient.nazwa, true);
				createElementAndAppend("NAZWA2", rejestrVat, "", true);
				createElementAndAppend("NAZWA3", rejestrVat, "", true);
				createElementAndAppend("NIP_KRAJ", rejestrVat, "", true);
				createElementAndAppend("NIP", rejestrVat, faktura.klient.nip, true);
				createElementAndAppend("KRAJ", rejestrVat, faktura.klient.kraj, true);
				createElementAndAppend("WOJEWODZTWO", rejestrVat, "", true);// wojewodztwo 
				createElementAndAppend("POWIAT", rejestrVat, "", true); // powiat
				createElementAndAppend("GMINA", rejestrVat, "", true); // gmina 
				createElementAndAppend("ULICA", rejestrVat, faktura.klient.ulica, true);
				createElementAndAppend("NR_DOMU", rejestrVat, "", true); // nr domu
				createElementAndAppend("NR_LOKALU", rejestrVat, "", true);
				createElementAndAppend("MIASTO", rejestrVat, faktura.klient.miasto, true);
				createElementAndAppend("KOD_POCZTOWY", rejestrVat, faktura.klient.kodPocztowy, true);
				createElementAndAppend("POCZTA", rejestrVat, faktura.klient.miasto, true); // poczta
				createElementAndAppend("DODATKOWE", rejestrVat, "", true);
				
				//platnik
				createElementAndAppend("TYP_PLATNIKA", rejestrVat, "kontrachent", true);
				createElementAndAppend("PLATNIK", rejestrVat, faktura.klient.skroconaNazwa, true);
				createElementAndAppend("PLATNIK_ID", rejestrVat, "", true);
				createElementAndAppend("PLATNIK_NIP", rejestrVat, faktura.klient.nip, true);
				createElementAndAppend("PESEL", rejestrVat, "", true);
				createElementAndAppend("ROLNIK", rejestrVat, "Nie", false);
				createElementAndAppend("KATEGORIA", rejestrVat, "TOWARY", true);  // kategoria
				createElementAndAppend("KATEGORIA_ID", rejestrVat, "", true);
				createElementAndAppend("OPIS", rejestrVat, faktura.opis, true);
				createElementAndAppend("WALUTA", rejestrVat, "PLN", true);
				createElementAndAppend("FORMA_PLATNOSCI", rejestrVat, "przelew", true);  //forma platnosci
				createElementAndAppend("FORMA_PLATNOSCI_ID", rejestrVat, "", true);
				createElementAndAppend("DEKLARACJA_VAT7", rejestrVat, "", true);  //deklaracja vat
				createElementAndAppend("DEKLARACJA_VATUE", rejestrVat, "Nie", true);
				createElementAndAppend("DEKLARACJA_VAT27", rejestrVat, "Nie", true);
				createElementAndAppend("KURS_WALUTY", rejestrVat, "NBP", true);
				createElementAndAppend("NOTOWANIE_WALUTY_ILE", rejestrVat, "1", false);
				createElementAndAppend("NOTOWANIE_WALUTY_ZA_ILE", rejestrVat, "1", false);
				createElementAndAppend("DATA_KURSU", rejestrVat, df.format(faktura.dataWystawienia2), true);
				createElementAndAppend("KURS_DO_KSIEGOWANIA", rejestrVat, "Nie", false);
				createElementAndAppend("KURS_WALUTY_2", rejestrVat, "NBP", true);
				createElementAndAppend("NOTOWANIE_WALUTY_ILE_2", rejestrVat, "1", false);
				createElementAndAppend("NOTOWANIE_WALUTY_ZA_ILE_2", rejestrVat, "1", false);
				createElementAndAppend("DATA_KURSU_2", rejestrVat, df.format(faktura.dataWystawienia2), true);
				createElementAndAppend("PLATNOSC_VAT_W_PLN", rejestrVat, "Nie", false);
				createElementAndAppend("AKCYZA_NA_WEGIEL", rejestrVat, "0", false);
				createElementAndAppend("FA_Z_PA", rejestrVat, "Nie", false);
				createElementAndAppend("VAN_FA_Z_PA", rejestrVat, "Nie", false);
				createElementAndAppend("VAN_RODZAJ", rejestrVat, "0", false);

				//pozycje
				Element pozycjeEl = createElementAndAppend("POZYCJE", rejestrVat, null, false);
				for (Pozycja pozycja : faktura.pozycje) {
					Element pozycjaEl = createElementAndAppend("POZYCJA", pozycjeEl, null, false);
					createElementAndAppend("KATEGORIA_POS", pozycjaEl, "TOWARY", true);  // kategoria 2gi raz
					createElementAndAppend("KATEGORIA_ID_POS", pozycjaEl, "", true); 
					createElementAndAppend("STAWKA_VAT", pozycjaEl, formatNumber(pozycja.stawkaVat), false);
					createElementAndAppend("STATUS_VAT", pozycjaEl, pozycja.stawkaVat == 0 ? "zwolniona" : "opodatkowana", false); // jak 0 to zwolniona
					createElementAndAppend("NETTO", pozycjaEl, formatNumber(pozycja.kwotaNetto), false);
					createElementAndAppend("VAT", pozycjaEl, formatNumber(pozycja.kwotaVat), false);
					createElementAndAppend("NETTO_SYS", pozycjaEl, formatNumber(pozycja.kwotaNetto), false);
					createElementAndAppend("VAT_SYS", pozycjaEl, formatNumber(pozycja.kwotaVat), false);
					createElementAndAppend("NETTO_SYS2", pozycjaEl, formatNumber(pozycja.kwotaNetto), false);
					createElementAndAppend("VAT_SYS2", pozycjaEl, formatNumber(pozycja.kwotaVat), false);
					createElementAndAppend("RODZAJ_SPRZEDAZY", pozycjaEl, "", false); // rodzaj sprzedaży
					createElementAndAppend("UWZ_W_PROPORCJI", pozycjaEl, "warunkowo", true);			
				}
				
				//płatności
				Element platnosciEl = createElementAndAppend("PLATNOSCI", rejestrVat, null, false);
				Element platnoscEl = createElementAndAppend("PLATNOSC", platnosciEl, null, false);
				createElementAndAppend("ID_ZRODLA_PLAT", platnoscEl, "", true);
				createElementAndAppend("TERMIN_PLAT", platnoscEl, df.format(faktura.terminPlatnosci), true); 
				createElementAndAppend("FORMA_PLATNOSCI_PLAT", platnoscEl, /*faktura.formaPlatnosci.toLowerCase()*/"przelew", true);
				createElementAndAppend("FORMA_PLATNOSCI_ID_PLAT", platnoscEl, "", true);
				createElementAndAppend("KWOTA_PLAT", platnoscEl, formatNumber(faktura.wartoscBrutto), false); //tu jest string !!!
				createElementAndAppend("WALUTA_PLAT", platnoscEl, "PLN", true);
				createElementAndAppend("KURS_WALUTY_PLAT", platnoscEl, "NBP", true);
				createElementAndAppend("NOTOWANIE_WALUTY_ILE_PLAT", platnoscEl, "1", false);
				createElementAndAppend("NOTOWANIE_WALUTY_ZA_ILE_PLAT", platnoscEl, "1", false);
				createElementAndAppend("KWOTA_PLN_PLAT", platnoscEl, formatNumber(faktura.wartoscBrutto), false); //tu jest string !!!
				createElementAndAppend("KIERUNEK", platnoscEl, "przychód", false);
				createElementAndAppend("PODLEGA_ROZLICZENIU", platnoscEl, "tak", false);
				createElementAndAppend("KONTO", platnoscEl, "", true); // konto
				createElementAndAppend("NIE_NALICZAJ_ODSETEK", platnoscEl, "Nie", false);
				createElementAndAppend("PRZELEW_SEPA", platnoscEl, "Nie", false);
				createElementAndAppend("DATA_KURSU_PLAT", rejestrVat, df.format(faktura.dataWystawienia2), true);
				createElementAndAppend("WALUTA_DOK", platnoscEl, "PLN", true);
				createElementAndAppend("PLATNOSC_TYP_PODMIOTU", platnoscEl, "kontrachent", true); // typ podmiotu
				createElementAndAppend("PLATNOSC_PODMIOT", platnoscEl, faktura.klient.skroconaNazwa, true);
				createElementAndAppend("PLATNOSC_PODMIOT_ID", platnoscEl, "", true);
				createElementAndAppend("PLATNOSC_PODMIOT_NIP", platnoscEl, faktura.klient.nip, false);
				createElementAndAppend("PLAT_KATEGORIA", platnoscEl, "", true); // platnosc kategoria
				createElementAndAppend("PLAT_KATEGORIA_ID", platnoscEl, "", true);
				createElementAndAppend("PLAT_ELIXIR_O1", platnoscEl, "", true); // opis za fakture
				createElementAndAppend("PLAT_ELIXIR_O2", platnoscEl, "", true);
				createElementAndAppend("PLAT_ELIXIR_O3", platnoscEl, "", true);
				createElementAndAppend("PLAT_ELIXIR_O4", platnoscEl, "", true);
				createElementAndAppend("PLAT_FA_Z_PA", platnoscEl, "Nie", false);
				createElementAndAppend("PLAT_VAN_FA_Z_PA", platnoscEl, "Nie", false);
				createElementAndAppend("PLAT_SPLIT_PAYMENT", platnoscEl, "Nie", false);
				createElementAndAppend("PLAT_SPLIT_NIP", platnoscEl, faktura.klient.nip, true);
				createElementAndAppend("PLAT_SPLIT_NR_DOKUMENTU", platnoscEl, faktura.numer, true);
					
				//atrybuty
				Element atrybutyEl = createElementAndAppend("ATRYBUTY", rejestrVat, null, false);
			
				break;
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (TransformerException tfe) {
			throw new ConvertException(tfe.getMessage());
		}
	}
	
	Element createElementAndAppend(String elementName, Element parent, String elementText, boolean cData){
		Element el = doc.createElement(elementName);
		if (elementText != null){
			if (cData) {
				if (elementText.isEmpty()) {
					//elementText = EMPTY_CDATA;
				}
				el.appendChild(doc.createCDATASection(elementText));
			} else {
				el.appendChild(doc.createTextNode(elementText));
			}
		}
		parent.appendChild(el);
		return el;
	}
	
	String formatNumber(Float n) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("####,####.##", otherSymbols);
		return df.format(n.doubleValue());
		//return String.format("%.2f", n);
	}
}
