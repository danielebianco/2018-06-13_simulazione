package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Model;
import it.polito.tdp.flightdelays.model.Rotta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightDelaysController {
	
	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Airline> cmbBoxLineaAerea;

    @FXML
    private Button caricaVoliBtn;

    @FXML
    private TextField numeroPasseggeriTxtInput;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    void doCaricaVoli(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Airline airline = this.cmbBoxLineaAerea.getValue();
    	
    	if(airline==null) {
    		this.txtResult.setText("Selezionare una linea aerea!\n");
    		return;
    	}
    	
    	model.creaGrafo(airline);
    	
    	txtResult.appendText(String.format("Grafo creato: %d vertici e %d archi...\n\n",
    			model.getGrafo().vertexSet().size(), model.getGrafo().edgeSet().size()));
    	
    	txtResult.appendText(">> 10 peggiori rotte:\n");
    	
    	for(Rotta r : model.stampaRisultato(airline)){
    		txtResult.appendText("- " + r.toString()+"\n");
    	}    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    		System.out.println("Simula!");
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxLineaAerea != null : "fx:id=\"cmbBoxLineaAerea\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert caricaVoliBtn != null : "fx:id=\"caricaVoliBtn\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroPasseggeriTxtInput != null : "fx:id=\"numeroPasseggeriTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
	public void setModel(Model model) {
		this.model = model;
		this.cmbBoxLineaAerea.getItems().addAll(model.getAirlines());
	}
}
