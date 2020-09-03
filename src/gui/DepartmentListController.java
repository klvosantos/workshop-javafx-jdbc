package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
	
	private DepartmentService service; // Declara��o de depend�ncia
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
		
	}
	
	public void setDepartmentService(DepartmentService service) { // Inje��o de depend�ncia na classe
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Faz com que o tableViewDepartment acompanhe a altura da janela 
	}
	
	 public void updateTableView() {
		 if (service == null) {
			 throw new IllegalStateException("Service was null");
		 }
		 List<Department> list = service.findAll();
		 obsList = FXCollections.observableArrayList(list);
		 tableViewDepartment.setItems(obsList);
	 }
	 
	 private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscriberDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage(); // Quando carregar uma janela de dialogo modal na frente da janela existente � necessario instanciar um novo Stage(Um palco na frente do outro)
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane)); // Como � um novo stage, tamb�m sera uma nova cena. pane nesse caso sera o elemento raiz da cena
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage); // parentStage � o Stage pai desse modal 
			dialogStage.initModality(Modality.WINDOW_MODAL); // initModality � o metodo que informa qual comportamento a janela tera, nesse caso como modal. A janela fica travada, enquando n�o for fechada n�o � possivel acessar a anterior.
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading View", e.getMessage(), AlertType.ERROR);
		}
	 }

	@Override
	public void onDataChangeg() {
		updateTableView();
	}
}
