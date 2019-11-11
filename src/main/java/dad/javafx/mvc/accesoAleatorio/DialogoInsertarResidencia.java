package dad.javafx.mvc.accesoAleatorio;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DialogoInsertarResidencia extends Dialog<Residencia> {

	private static class DialogoBinding extends BooleanBinding {

		private StringExpression id, nombre, codUni, precio;

		public DialogoBinding(StringExpression id, StringExpression nombre, StringExpression codUni,
				StringExpression precio) {

			this.id = id;
			this.nombre = nombre;
			this.codUni = codUni;
			this.precio = precio;
			bind(this.id, this.nombre, this.codUni, this.precio);

		}

		@SuppressWarnings("unused")
		private boolean esNumerico(String id, String precio) {

			try {

				int idVal = Integer.parseInt(id);
				float precioVal = Float.parseFloat(precio);

				return true;

			} catch (NumberFormatException e) {
			}

			return false;
		}

		private boolean validarCampos() {

			boolean check;

			check = id.get().isEmpty();
			check |= nombre.get().isEmpty();
			check |= codUni.get().isEmpty();
			check |= precio.get().isEmpty();

			return !check && esNumerico(id.get(), precio.get());
		}

		@Override
		protected boolean computeValue() {
			return validarCampos();
		}

	}

	private ButtonType copfirmarButon, cancelButton;
	private CheckBox comedor;
	private Node insertarButton;

	private TextField id, nombre, codUni, precio;

	public DialogoInsertarResidencia(int idResidencia) {

		setTitle("Nueva Residencia");
		setHeaderText("Introduce los datos de la Resindecia\nEl codUniversidad es OBLIGATORIO");

		copfirmarButon = new ButtonType("Insertar", ButtonData.OK_DONE);
		cancelButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

		getDialogPane().getButtonTypes().addAll(copfirmarButon, cancelButton);

		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);

		Label infoLabel = new Label(getContentText());
		infoLabel.setWrapText(true);
		grid.addRow(0, infoLabel);
		GridPane.setColumnSpan(infoLabel, 2);

		Label idLbl = new Label("ID:");
		id = new TextField();
		id.setEditable(false);
		id.setDisable(true);
		id.setText(String.valueOf(idResidencia));
		id.setPrefColumnCount(2);
		grid.addRow(1, idLbl, id);

		Label nombreLbl = new Label("Nombre:");
		nombre = new TextField();
		nombre.setPromptText("Nombre");
		grid.addRow(2, nombreLbl, nombre);

		Label codUniLbl = new Label("Universidad:");
		codUni = new TextField();
		codUni.setPromptText("Código");
		grid.addRow(3, codUniLbl, codUni);

		Label precioLbl = new Label("Precio:");
		precio = new TextField();
		precio.setPromptText("Precio");
		precio.setPrefColumnCount(3);
		grid.addRow(4, precioLbl, precio);

		Label comedorLbl = new Label("Comedor");
		comedor = new CheckBox();
		grid.addRow(5, comedorLbl, comedor);

		insertarButton = getDialogPane().lookupButton(copfirmarButon);
		insertarButton.disableProperty().bind(new DialogoBinding(id.textProperty(), nombre.textProperty(),
				codUni.textProperty(), precio.textProperty()).not());

		getDialogPane().setContent(grid);
		setResultConverter(bt -> onAccionEjecutar(bt));
	}

	private Residencia onAccionEjecutar(ButtonType bt) {

		if (bt == copfirmarButon) {
			float precioVal = Float.parseFloat(precio.getText());
			int idVal = Integer.parseInt(id.getText());

			return new Residencia(idVal, nombre.getText(), codUni.getText(), precioVal, comedor.isSelected());

		}

		return null;
	}

}
