package eu.ksiegowasowa;

public class Config {

	private static final Config CONFIG = new Config();

	private boolean generujKontrahentow = true;
	private boolean generujZakup = true;
	private boolean generujSprzedaz = true;
	private String nazwaBazy = "MSTRA";

	public static Config getConfig() {
		return CONFIG;
	}

	public boolean isGenerujKontrahentow() {
		return generujKontrahentow;
	}

	public void setGenerujKontrahentow(boolean generujKontrahentow) {
		this.generujKontrahentow = generujKontrahentow;
	}

	public boolean isGenerujZakup() {
		return generujZakup;
	}

	public void setGenerujZakup(boolean generujZakup) {
		this.generujZakup = generujZakup;
	}

	public boolean isGenerujSprzedaz() {
		return generujSprzedaz;
	}

	public void setGenerujSprzedaz(boolean genereuSprzedaz) {
		this.generujSprzedaz = genereuSprzedaz;
	}

	public String getNazwaBazy() {
		return nazwaBazy;
	}

	public void setNazwaBazy(String nazwaBazy) {
		this.nazwaBazy = nazwaBazy;
	}

}
