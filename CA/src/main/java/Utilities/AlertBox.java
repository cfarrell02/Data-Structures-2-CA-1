package Utilities;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AlertBox {
    // https://code.makery.ch/blog/javafx-dialogs-official/
    public static void display(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
    public static boolean displayConfirmation(String title, String header,String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    public static String displayTextBox(String title, String content){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        //dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }


}
