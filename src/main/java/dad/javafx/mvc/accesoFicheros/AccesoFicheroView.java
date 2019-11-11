package dad.javafx.mvc.accesoFicheros;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AccesoFicheroView extends BorderPane {

	private Label miNombreLabel, rutaLabel;
	private TextField rutaTextoLabel, nombreFicheroLabel;
	private RadioButton esCarpetaRadio, esFicheroRadio;
	private Button botonCrear, borrarBoton, moverBoton, verBoton, botonCopiar, botonVerContenido, modificarBoton;
	private TextArea resultadosArea;
	private ListView<File> listaResultadosLView;

	public AccesoFicheroView() {

		rutaLabel = new Label("Ruta actual:");
		rutaTextoLabel = new TextField();

		miNombreLabel = new Label("Joel Couto Lugo AED");
		miNombreLabel.setPrefHeight(50);

		nombreFicheroLabel = new TextField();
		nombreFicheroLabel.setPromptText("Carpeta o fichero seleccionado");

		resultadosArea = new TextArea();
		resultadosArea.setPromptText("Contenido del fichero");

		listaResultadosLView = new ListView<>();

		botonCrear = new Button("Crear");
		borrarBoton = new Button("Eliminar");
		moverBoton = new Button("Mover");
		botonCopiar = new Button("Copiar");
		verBoton = new Button("Ver ficheros y carpetas");
		botonVerContenido = new Button("Ver contenido fichero");
		modificarBoton = new Button("Modificar fichero");

		esCarpetaRadio = new RadioButton("Es carpeta");
		esFicheroRadio = new RadioButton("Es fichero");
		ToggleGroup grp = new ToggleGroup();
		grp.getToggles().addAll(esCarpetaRadio, esFicheroRadio);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(5, 30, 5, 5));

		grid.addRow(0, rutaLabel, rutaTextoLabel);

		HBox btBox = new HBox(80, botonCrear, borrarBoton, moverBoton, botonCopiar, esCarpetaRadio, esFicheroRadio);

		btBox.setAlignment(Pos.BASELINE_CENTER);

		grid.addRow(1, btBox);
		GridPane.setColumnSpan(btBox, 2);

		grid.addRow(2, nombreFicheroLabel);
		GridPane.setColumnSpan(nombreFicheroLabel, 2);

		grid.addRow(3, verBoton);

		grid.addRow(4, listaResultadosLView);
		listaResultadosLView.setPrefHeight(100);
		GridPane.setColumnSpan(listaResultadosLView, 2);

		VBox contentBtBox = new VBox(10, botonVerContenido, modificarBoton);
		grid.add(contentBtBox, 0, 5);

		grid.add(resultadosArea, 1, 5);

		ColumnConstraints[] cols = {

				new ColumnConstraints(), new ColumnConstraints() };

		cols[1].setHgrow(Priority.ALWAYS);
		cols[1].setFillWidth(true);
		grid.getColumnConstraints().addAll(cols);

		GridPane.setVgrow(listaResultadosLView, Priority.ALWAYS);
		GridPane.setVgrow(resultadosArea, Priority.ALWAYS);

		setAlignment(miNombreLabel, Pos.CENTER);
		setAlignment(grid, Pos.CENTER);

		setTop(miNombreLabel);
		setCenter(grid);
		setPadding(new Insets(5));

	}

	public TextArea getAreaContent() {
		return resultadosArea;
	}

	public ListView<File> getFicheroList() {
		return listaResultadosLView;
	}

	public Button getBotonCrear() {
		return botonCrear;
	}

	public Button getBotonBorrar() {
		return borrarBoton;
	}

	public Button getBotonMover() {
		return moverBoton;
	}

	public Button getBotonView() {
		return verBoton;
	}

	public Button getBotonContenido() {
		return botonVerContenido;
	}

	public Button getBotonModificar() {
		return modificarBoton;
	}

	public RadioButton getCarpetaRadio() {
		return esCarpetaRadio;
	}

	public RadioButton getFicheroRadio() {
		return esFicheroRadio;
	}

	public Button getCopiarButon() {
		return botonCopiar;
	}

	public void setCarpetaRadio(RadioButton carpetaRadio) {
		this.esCarpetaRadio = carpetaRadio;
	}

	public Label getMiNombreLabel() {
		return miNombreLabel;
	}

	public Label getRutaLabel() {
		return rutaLabel;
	}

	public TextField getRutaTextoLabel() {
		return rutaTextoLabel;
	}

	public TextField getNombreFichTexto() {
		return nombreFicheroLabel;
	}
}
