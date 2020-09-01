package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	private synchronized void loadView(String absoluteName) { // synchronized garante que processamento entre o try não vai ser interrompido durante o multthread
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load(); 
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox =  (VBox)((ScrollPane) mainScene.getRoot()).getContent(); // pega uma referencia para o vbox que esta na tela principal <tag vBox>
			
			Node mainMenu = mainVBox.getChildren().get(0); // Guarda uma ref para o menu. Pega o primeiro filho do vbox da janela principal
			mainVBox.getChildren().clear(); // limpa tdos os filhos do main vBox
			mainVBox.getChildren().add(mainMenu); // add o mainMenu no mainVBox
			mainVBox.getChildren().addAll(newVBox.getChildren()); // add os filhos do newVBox
			// *com isso é possivel manipular a cena principal incluindo nela alem do mainMenu os filhos da janela que estiver sendo aberta no caso a (newVBox)
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load(); 
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox =  (VBox)((ScrollPane) mainScene.getRoot()).getContent(); 
			Node mainMenu = mainVBox.getChildren().get(0); 
			mainVBox.getChildren().clear(); 
			mainVBox.getChildren().add(mainMenu); 
			mainVBox.getChildren().addAll(newVBox.getChildren()); 
		
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
