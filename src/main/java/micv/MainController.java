package micv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.ContactoController;
import controller.ExperienciaController;
import controller.FormacionController;
import controller.PersonalController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.CV;
import utils.JSONUtils;

public class MainController implements Initializable {
	
	// controllers
	
	private PersonalController personalController = new PersonalController();
	private ContactoController contactoController = new ContactoController();
	private FormacionController formacionController = new FormacionController();
	private ExperienciaController experienciaController = new ExperienciaController();
	
	// model
	
	private ObjectProperty<CV> cv = new SimpleObjectProperty<>();
	
	// view

    @FXML
    private BorderPane view;

    @FXML
    private Tab personalTab;

    @FXML
    private Tab contactoTab;

    @FXML
    private Tab formacionTab;

    @FXML
    private Tab experienciaTab;

    @FXML
    private Tab conocimientosTab;
	
	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	public BorderPane getView() {
		return view;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		personalTab.setContent(personalController.getView());
		contactoTab.setContent(contactoController.getView());
		formacionTab.setContent(formacionController.getView());
		experienciaTab.setContent(experienciaController.getView());
		
		cv.addListener((o, ov, nv) -> onCVChanged(o, ov, nv));
		
		cv.set(new CV());
		
	}

    private void onCVChanged(ObservableValue<? extends CV> o, CV ov, CV nv) {
    	
    	if (ov != null) {
    		
    		personalController.personalProperty().unbind(); // desbindeo personalProperty del CV anterior
    		contactoController.contactoProperty().unbind();
    		formacionController.formacionProperty().unbind();
    		experienciaController.experienciaProperty().unbind();
    		// desbindear resto de controllers
    		
    	}

    	if (nv != null) {
    		
    		personalController.personalProperty().bind(nv.personalProperty()); // bindeo personalProperty del nuevo CV
    		// bindear resto de controllers
    		contactoController.contactoProperty().bind(nv.contactoProperty());
    		formacionController.formacionProperty().bind(nv.formacionProperty());
    		experienciaController.experienciaProperty().bind(nv.experienciasProperty());
    	}
    	
	}

	@FXML
    void onAbrirAction(ActionEvent event) {

    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Abrir un currículum");
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Currículum Vitae (*.cv)", "*.cv"));
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos", "*.*"));
    	File cvFile = fileChooser.showOpenDialog(App.getPrimaryStage());
    	if (cvFile != null) {
    		try {
				cv.set(JSONUtils.fromJson(cvFile, CV.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		
    }

    @FXML
    void onAcercaDeAction(ActionEvent event) {

    }

    @FXML
    void onGuardarAction(ActionEvent event) {

    }

    @FXML
    void onGuardarComoAction(ActionEvent event) {

    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Guardar un currículum");
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Currículum Vitae (*.cv)", "*.cv"));
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos", "*.*"));
    	File cvFile = fileChooser.showSaveDialog(App.getPrimaryStage());
    	if (cvFile != null) {
    		
    		try {
				JSONUtils.toJson(cvFile, cv.get());
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	}
    	
    }

    @FXML
    void onNuevoAction(ActionEvent event) {
    	System.out.println("Has pulsado nuevo");
    	cv.set(new CV());
    }

    @FXML
    void onSalirAction(ActionEvent event) {

    }
	
}
