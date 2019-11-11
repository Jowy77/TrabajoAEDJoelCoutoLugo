package dad.javafx.mvc.accesoAleatorio;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DialogoDeInfoResidencias extends Alert {

	public DialogoDeInfoResidencias(Residencia residencia) {

		super(AlertType.INFORMATION);

		setTitle("Residencia");
		setHeaderText("Información de la residencia");

		GridPane root = new GridPane();

		Label idLabel = new Label("ID:");
		Label idTLabel = new Label(String.valueOf(residencia.getId()));
		root.addRow(0, idLabel, idTLabel);

		Label nombreLabel = new Label("Nombre Residencia:");
		Label nombreTLabel = new Label(residencia.getName());
		root.addRow(1, nombreLabel, nombreTLabel);

		Label uniLabel = new Label("Codigo universidad:");
		Label uniTLabael = new Label(residencia.getCodUniversidad());
		root.addRow(2, uniLabel, uniTLabael);

		Label precioLabel = new Label("Precio:");
		Label precioTLabel = new Label(String.valueOf(residencia.getPrecio()));
		root.addRow(3, precioLabel, precioTLabel);

		Label comedorLabel = new Label("Comedor:");
		Label comedorTLabel = new Label(residencia.getComedorString());
		root.addRow(4, comedorLabel, comedorTLabel);

		root.setHgap(5);
		root.setVgap(5);

		getDialogPane().setContent(root);
	}
}
