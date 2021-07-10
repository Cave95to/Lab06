/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Integer a = this.boxMese.getValue();
    	if(a == null) {
    		txtResult.setText("Seleziona un mese");
    		return;
    	}
    	
    	List<Citta> elenco = this.model.trovaSequenza(a);
    	
    	for(Citta c : elenco) {
    		this.txtResult.appendText(c.getNome()+"\n");
    	}
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Integer a = this.boxMese.getValue();
    	if(a == null) {
    		txtResult.setText("Seleziona un mese");
    		return;
    	}
    	
    	Map<String, Double> mappa = this.model.getUmiditaMedia(a);
    	
    	for(String s : mappa.keySet()) {
    		this.txtResult.appendText(s + " " + mappa.get(s)+ "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Integer> mesi = this.model.getMesi();
    	Collections.sort(mesi);
    	this.boxMese.getItems().addAll(mesi);
    }
}

