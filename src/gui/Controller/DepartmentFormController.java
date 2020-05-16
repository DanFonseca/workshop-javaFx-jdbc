package gui.Controller;

import Model.DAO.DAOFactory;
import Model.DB.DBException;
import Model.Entities.Department;
import Model.Services.DepartmentService;
import gui.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {
    private Department entity;
    private DepartmentService service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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

    public void setService(DepartmentService service) {
        this.service = service;
    }

    @FXML
    public void onBtnSaveAction (ActionEvent event){
        if(entity == null){
            throw  new IllegalStateException("Entity was null");
        }
        if (service == null){
            throw  new IllegalStateException("Service was null");
        }

        try{
            entity = getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(event).close();
            changeListener();
        }catch (DBException e ){
            Alerts.showAlerts("Error save DB",null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void changeListener() {
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    public void subscribeListener (DataChangeListener dataChangeListener){
        dataChangeListeners.add(dataChangeListener);
    }

    private Department getFormData() {
        Department department = new Department();
        department.setId(Utils.tryParseToInt(id.getText()));
        department.setName(departmentName.getText());

        return department;
    }

    @FXML
    public void onBtnClose (ActionEvent event){
        Utils.currentStage(event).close();
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
