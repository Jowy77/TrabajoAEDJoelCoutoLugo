package dad.javafx.mvc.accesoFicheros;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class MensajeDelSistema {

	public static void sendError(String header, String msg) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	public static String sendDialog(String title, String header, String msg) {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(msg);

		Optional<String> str = dialog.showAndWait();
		return (str.isPresent()) ? str.get() : "";
	}

	public static boolean confirmDialog(String title, String header, String msg) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(msg);

		Optional<ButtonType> btSelected = alert.showAndWait();
		if (!btSelected.isPresent()) {
			return false;
		}

		return (btSelected.get() == ButtonType.OK) ? true : false;
	}
}
