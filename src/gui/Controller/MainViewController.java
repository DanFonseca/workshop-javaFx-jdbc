package gui.Controller;

import Model.Services.DepartmentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController  implements Initializable {
    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem about;


    @FXML
    public void onMenuItemSellerAction (){
        System.out.println("onMenuItemSellerAction");
    }

    @FXML
    public void onMenuItemDepartmentAction (){
        loadView("/gui/Views/DepartmentListView.fxml",(DepartmentListController controller)->{
            controller.setDepartmentService(new DepartmentService());
            controller.updateDepartmentList();
        } );
    }

    @FXML
    public void onMenuItemAboutAction (){
        loadView("/gui/Views/AboutView.fxml", x->{});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private synchronized <T> void  loadView (String absoluteName, Consumer<T> consumer){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
        try {
            VBox vBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();


            Node mainMenu = mainVbox.getChildren().get(0);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(vBox.getChildren());
            T controller = loader.getController();
            consumer.accept(controller);

        } catch (IOException e) {
            e.printStackTrace();
            gui.util.Alerts.showAlerts("Error",null,e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
