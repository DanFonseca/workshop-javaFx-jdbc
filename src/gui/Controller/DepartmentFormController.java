package gui.Controller;

import Model.DAO.DAOFactory;
import Model.Entities.Department;
import gui.util.Constraints;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {
    private Department entity;

    @FXML
    private TextField id;
    @FXML
    private TextField departmentName;
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private Label error;

    public void setDepartment (Department entity){
        this.entity = entity;
    }

    @FXML
    public void onBtnSaveAction (){
        String name = departmentName.getText();
        DAOFactory.createDepartmentDAO().insert(new Department(1, name));
        departmentName.clear();
        gui.util.Alerts.showAlerts("Sucesso!", null, "Cadastro salco com sucesso", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void onBtnClose (ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(departmentName, 10);
        Constraints.setTextFieldInteger(id);
    }

    public void updateFormData (){
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        departmentName.setText(entity.getName());
        id.setText(String.valueOf(entity.getId()));
    }
}
