package eu.ksiegowasowa.convert;

import java.util.List;

import eu.ksiegowasowa.Config;
import eu.ksiegowasowa.convert.pojo.Faktura;

public class FileConverter {

	public FileConverter(){};
	
	public void convert(String fileName) throws ConvertException {
		List<Faktura> faktury = new EppReader().readFile(fileName);
		String newFileName = getNewFileName(fileName);
		if (Config.getConfig().isGenerujSprzedaz() || Config.getConfig().isGenerujZakup()) {
			new XMLWriter().writeFile(faktury, newFileName + "xml");
		}
		if (Config.getConfig().isGenerujKontrahentow()) {
			new XLSWriter().writeFile(faktury, newFileName + "xls");
		}
	}
	
	private String getNewFileName(String fn) {
		String newFileName = new String(fn);
		String[] tokens = newFileName.split("\\.");
		newFileName = fn.substring(0, fn.length() - tokens[tokens.length-1].length());
		return newFileName;
	}
}
