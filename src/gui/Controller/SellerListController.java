package gui.Controller;

import Model.DB.DBException;
import Model.Entities.Seller;
import Model.Services.SellerService;
import gui.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Main;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;


    @FXML
    private TableView<Seller> SellerTableView;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnName;

    @FXML
    private Button newButton;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;
    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;

    private ObservableList<Seller> SellerObservableList;

    public void setSellerTableView() {

    }

    public void setTableColumnId() {

    }

    public void setTableColumnName() {

    }

    @FXML
    public void onNewButtonAction(ActionEvent event) {
        Stage currentStage = Utils.currentStage(event);
        Seller seller = new Seller();
        createDialogForm(seller, currentStage, "/gui/Controller/SellerForm.fxml");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    private void initializeNodes() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        SellerTableView.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateSellerList() {
        if (service == null) {
            throw new IllegalMonitorStateException("SellerList variable is null");
        }
        initEditButtons();
        initRemoveButtons();
        List<Seller> findAll = service.findAll();
        SellerObservableList = FXCollections.observableList(findAll);
        SellerTableView.setItems(SellerObservableList);
    }

    private void createDialogForm(Seller seller, Stage parentStage, String absoluteName) {
       /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setSeller(seller);
            controller.updateFormData();
            controller.setService(new SellerService());
            controller.subscribeListener(this);

            Stage dialogueStage = new Stage();
            dialogueStage.setTitle("Enter Seller Data");
            dialogueStage.setScene(new Scene(pane));
            dialogueStage.setResizable(false);
            dialogueStage.initOwner(parentStage);
            dialogueStage.initModality(Modality.WINDOW_MODAL);
            dialogueStage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlerts("Io Exception", "Error loading form view", e.getMessage(), Alert.AlertType.ERROR);
      }*/
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, Utils.currentStage(event), "/gui/Controller/SellerForm.fxml"));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> reusult = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        if (reusult.get() == ButtonType.OK) {
            if (obj == null) {
                throw new IllegalStateException("Obj debarment is null");
            }
            try {
                service.remove(obj);
                updateSellerList();
            } catch (DBException e) {
                Alerts.showAlerts("Error", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }


    @Override
    public void onDataChanged() {
        updateSellerList();
    }
}
