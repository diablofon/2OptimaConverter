package eu.ksiegowasowa.convert.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Faktura {

	public enum TypFaktury {Zakup, Sprzedaz};
	
	public TypFaktury typ;
	public Date dataWystawienia;
	public Date dataWystawienia2;
	public Date dataSprzedazy;
	public Date terminPlatnosci;
	public String opis;
	public String numer;
	public Klient klient;
	public List<Pozycja> pozycje;
	public Float wartoscNetto;
	public Float wartoscVAT;
	public Float wartoscBrutto;
	public String formaPlatnosci;
	public String rejestr;
	
	public Faktura() {
		klient = new Klient();
		pozycje = new ArrayList<>();
	}
}
