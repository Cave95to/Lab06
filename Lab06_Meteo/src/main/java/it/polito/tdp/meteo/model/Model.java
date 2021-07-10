package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private List<Citta> tutteCitta = new ArrayList<>();
	private List<Citta> migliore = new ArrayList<>();
	private double costoMigliore = 0.0;
	
	private MeteoDAO dao;
	
	public Model() {
		dao = new MeteoDAO();
		this.tutteCitta = dao.getCitta();
	}
	
	// of course you can change the String output with what you think works best
	public Map<String, Double> getUmiditaMedia(int mese) {
		return this.dao.getUmiditaMedia(mese);
	}
	
	public List<Integer> getMesi() {
		
		return dao.getMesi();
	}

	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<>();
		
		for(Citta c : this.tutteCitta) {
			
			c.setRilevamenti(this.dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		this.cerca(parziale, 0);
		
		return this.migliore;
	}

	private void cerca(List<Citta> parziale, int livello) {
		
		if(livello == NUMERO_GIORNI_TOTALI) {
			
			for(Citta c : this.tutteCitta) {
				if(!parziale.contains(c))
					return;
			}
			
			// calcolo costo di questo percorso
			double costo = calcolaCosto(parziale);
			
			if(this.migliore.size()==0 || costo < this.costoMigliore) {
				this.costoMigliore = costo;
				this.migliore = new ArrayList<>(parziale);
			}
			
			return;
		}
		
		for(Citta prova : this.tutteCitta) {
			if(aggiuntaValida(parziale, prova)) {
				parziale.add(prova);
				cerca(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private boolean aggiuntaValida(List<Citta> parziale, Citta prova) {
		
		// non piu di 6 giorni nella stessa citta
		int cont = 0;
		for (Citta c : parziale) {
			if(c.equals(prova))
				cont++;
		}
		
		// se siamo stati meno di 6 giorni in quella città allora controllo il resto
		if(cont<NUMERO_GIORNI_CITTA_MAX) {

			// ALMENO 3 GIORNI STESSA CITTA
			
			// se parziale è vuoto possiamo aggiungere
			if(parziale.size()==0)
				return true;
			
			// se la città aggiunta è uguale all'ultima di parziale possiamo aggiungere sempre
			if(parziale.get(parziale.size()-1).equals(prova))
				return true;
			
			// se parziale ha 1 citta, prova deve essere quella
			if(parziale.size()==1)
				return (parziale.get(0).equals(prova));
			
			// se parziale ha due citta, SICURAMENTE CON IF DI PRIMA la 1a e la 2a citta sono uguali
			//  ma anche questa nuova che sara la terza giornata deve essere uguale
			if(parziale.size()==2)
				return (parziale.get(1).equals(prova));
			
			// se arriviamo qui allora prova è diversa dall'ultima citta di parziale
			// possiamo aggiungere prova solo se le ultime 3 citta di parziale sono già uguali tra loro
			if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && 
				parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-3)))
				return true;
			
		}
		
		return false;
	}

	private double calcolaCosto(List<Citta> parziale) {
		
		double costo = 0.0;
		
		for(int i = 0; i<NUMERO_GIORNI_TOTALI; i++) {
			
			Citta c = parziale.get(i);
			List<Rilevamento> rilevamenti = c.getRilevamenti();
			Rilevamento r = rilevamenti.get(i);
			
			costo = costo + r.getUmidita();
		}
		
		for(int i = 3; i<NUMERO_GIORNI_TOTALI; i++) {
			if(!parziale.get(i-1).equals(parziale.get(i)))
				costo = costo + COST;
		}
		return costo;
	}


	

}
