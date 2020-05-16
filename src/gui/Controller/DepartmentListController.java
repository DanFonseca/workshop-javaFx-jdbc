package gui.Controller;

import Model.Entities.Department;
import Model.Services.DepartmentService;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class DepartmentListController implements Initializable {

    private DepartmentService service;


    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private Button newButton;


    private ObservableList<Department> departmentObservableList;

    public void setDepartmentTableView() {

    }

    public void setTableColumnId() {

    }

    public void setTableColumnName() {

    }

    @FXML
    public void onNewButtonAction(ActionEvent event) {
        Stage currentStage =  Utils.currentStage(event);
        Department department = new Department();
        createDialogForm(department, currentStage, "/gui/Controller/DepartmentForm.fxml");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    private void initializeNodes() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        departmentTableView.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateDepartmentList() {
        if (service == null) {
            throw new IllegalMonitorStateException("DepartmentList variable is null");
        }
        List<Department> findAll = service.findAll();
        departmentObservableList = FXCollections.observableList(findAll);
        departmentTableView.setItems(departmentObservableList);
    }

    private void createDialogForm(Department department, Stage parentStage, String absoluteName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DepartmentFormController  controller = loader.getController();
            controller.setDepartment(department);
            controller.updateFormData();

            Stage dialogueStage = new Stage();
            dialogueStage.setTitle("Enter Department Data");
            dialogueStage.setScene(new Scene(pane));
            dialogueStage.setResizable(false);
            dialogueStage.initOwner(parentStage);
            dialogueStage.initModality(Modality.WINDOW_MODAL);
            dialogueStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlerts("Io Exception", "Error loading form view", e.getMessage(), Alert.AlertType.ERROR );
        }
    }
}
