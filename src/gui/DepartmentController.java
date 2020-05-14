package gui;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Entities.Department;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class DepartmentController implements Initializable {
    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private Button newButton;

    public void setDepartmentTableView() {

    }

    public void setTableColumnId() {

    }

    public void setTableColumnName() {

    }

    @FXML
    public void onNewButtonAction() {
        System.out.println("Click");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
      tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
      tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));

      Stage stage = (Stage) Main.getMainScene().getWindow();
      departmentTableView.prefHeightProperty().bind(stage.heightProperty());

    }
}
