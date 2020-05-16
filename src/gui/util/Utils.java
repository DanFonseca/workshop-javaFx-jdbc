package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
    //retorna o palco atual
    public static Stage currentStage (ActionEvent actionEvent){
        return (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt (String s){
        try{
            return Integer.parseInt(s);
        }catch (NumberFormatException e){
            return null;
        }
    }
}
