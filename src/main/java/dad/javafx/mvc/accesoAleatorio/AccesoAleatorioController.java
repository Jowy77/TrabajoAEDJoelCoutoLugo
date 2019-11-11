package dad.javafx.mvc.accesoAleatorio;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class AccesoAleatorioController implements Initializable {

	// ESTA VISTA LA HIZE CON EL SCENE BUILDER

	@FXML
	private GridPane view;

	@FXML
	private TableView<Residencia> tablaResidencias;

	@FXML
	private Button insertarResidenciaBoton, consultarBoton;

	@FXML
	private TextField rutaTxt, resiID;

	@FXML
	private Button consultaIDBt, modPrecioBt;

	private static int LongitudResidencias = 10;
	private static int LongitudUniversidad = 6;
	private static int LongitudBytesResidencia = 51;
	int listaResinciaId = 0;

	private StringProperty ruta = new SimpleStringProperty();

	private StringProperty id = new SimpleStringProperty();

	private ObservableList<Residencia> resiList = FXCollections.observableArrayList(new ArrayList<Residencia>());

	private ListProperty<Residencia> resiListProperty = new SimpleListProperty<Residencia>(resiList);

	private ObjectProperty<File> residenciasFile = new SimpleObjectProperty<File>();

	public AccesoAleatorioController() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VistaFXMLAccesoAleatorioJoel.fxml"));
		loader.setController(this);
		loader.load();
	}

	public GridPane getRootView() {
		return view;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		insertarResidenciaBoton.disableProperty().bind(residenciasFile.isNull());

		id.bindBidirectional(resiID.textProperty());

		tablaResidencias.getSelectionModel().selectedItemProperty()
				.addListener((o, ov, nv) -> onResidenciaIDCambiado(nv));
		resiID.disableProperty().bind(resiListProperty.emptyProperty());

		consultaIDBt.disableProperty().bind(resiListProperty.emptyProperty().or(resiID.textProperty().isEmpty()
				.and(tablaResidencias.getSelectionModel().selectedItemProperty().isNull())));

		modPrecioBt.disableProperty().bind(resiListProperty.emptyProperty().or(resiID.textProperty().isEmpty()
				.and(tablaResidencias.getSelectionModel().selectedItemProperty().isNull())));

		ruta.bind(Bindings.when(residenciasFile.isNotNull()).then(residenciasFile.asString())
				.otherwise(new SimpleStringProperty("")));

		rutaTxt.textProperty().bind(ruta);

		tablaResidencias.itemsProperty().bind(resiListProperty);
		insertarResidenciaBoton.setOnAction(evt -> insertarResidencia());
		consultarBoton.setOnAction(evt -> consultaResidencias());
		consultaIDBt.setOnAction(evt -> consultarResidenciaID());
		modPrecioBt.setOnAction(evt -> modificarPrecioID());
	}

	private void listarResidencias() {
		if (residenciasFile.get() == null) {
			return;
		}

		RandomAccessFile resiFile = null;

		try {

			resiFile = new RandomAccessFile(residenciasFile.get(), "r");
			int id;
			String nombre = "";
			String codUni = "";
			float precio;
			boolean comedor;

			while (true) {

				id = resiFile.readInt();

				resiFile.readChar();

				for (int i = 0; i < LongitudResidencias; i++)
					nombre += resiFile.readChar();

				resiFile.readChar();

				for (int i = 0; i < LongitudUniversidad; i++) {
					codUni += resiFile.readChar();
				}

				resiFile.readChar();

				precio = resiFile.readFloat();

				resiFile.readChar();

				comedor = resiFile.readBoolean();

				resiFile.readChar();

				listaResinciaId++;

				resiListProperty.add(new Residencia(id, nombre, codUni, precio, comedor));

				nombre = codUni = "";
			}

		} catch (EOFException eof) {

		}

		catch (IOException e) {
			sendFileError(residenciasFile.getName());
		} finally {

			try {

				if (resiFile != null) {
					resiFile.close();
				}

			} catch (IOException e) {
				sendFileError(residenciasFile.getName());
			}
		}
	}

	private void consultarResidenciaID() {
		if (!isIDValid(id.get())) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ID");
			alert.setHeaderText("ID no válido");
			alert.setContentText("Por favor, introduzca un ID válido");
			alert.showAndWait();
			return;
		}

		RandomAccessFile resiFile = null;

		try {
			resiFile = new RandomAccessFile(residenciasFile.get(), "r");

			int rId = Integer.parseInt(id.get());
			resiFile.seek(LongitudBytesResidencia * (rId - 1));

			int id;
			float precio;
			boolean comedor;
			String nombre, codUni;

			nombre = codUni = "";

			id = resiFile.readInt();
			resiFile.readChar();

			for (int i = 0; i < LongitudResidencias; i++)
				nombre += resiFile.readChar();

			resiFile.readChar();

			for (int i = 0; i < LongitudUniversidad; i++)
				codUni += resiFile.readChar();

			resiFile.readChar();

			precio = resiFile.readFloat();
			resiFile.readChar();

			comedor = resiFile.readBoolean();

			DialogoDeInfoResidencias dialog = new DialogoDeInfoResidencias(
					new Residencia(id, nombre, codUni, precio, comedor));
			dialog.showAndWait();

		} catch (EOFException eof) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ID");
			alert.setHeaderText("ID no válido");
			alert.setContentText("No se ha encontrado el ID seleccionado");
			alert.showAndWait();

		} catch (IOException e) {
			sendFileError(residenciasFile.getName());

		} finally {

			if (resiFile != null) {

				try {

					resiFile.close();

				} catch (IOException e) {
					sendFileError(residenciasFile.getName());
				}
			}
		}
	}

	private void modificarPrecioID() {

		if (!isIDValid(id.get())) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ID");
			alert.setHeaderText("ID no válido");
			alert.setContentText("Por favor, introduzca un ID válido");
			alert.showAndWait();
			return;
		}

		RandomAccessFile resiFile = null;

		try {

			resiFile = new RandomAccessFile(residenciasFile.get(), "rw");

			final int locPrecioBytes = 42;

			int rId = Integer.parseInt(id.get());

			resiFile.seek(LongitudBytesResidencia * (rId - 1));
			resiFile.skipBytes(locPrecioBytes);

			Residencia resi = findResidenciaById(rId);
			if (resi == null) {
				throw new EOFException();
			}

			TextInputDialog dialog = new TextInputDialog(String.valueOf(resi.getPrecio()));
			dialog.setTitle("Residencia " + resi.getName());
			dialog.setHeaderText("Modificar precio");

			Optional<String> precio = dialog.showAndWait();
			float precioFinal = 0;

			if (precio.isPresent() && !precio.get().isBlank() && !precio.get().isEmpty()) {

				try {
					precioFinal = Float.parseFloat(precio.get());

				} catch (NumberFormatException e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Precio");
					alert.setHeaderText("Precio no válido");
					alert.setContentText("El precio introducido no es válido");
					alert.showAndWait();
					throw new Exception();
				}
			} else {
				throw new Exception();
			}

			resiFile.writeFloat(precioFinal);

			resi.setPrecio(precioFinal);

		} catch (EOFException eof) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ID");
			alert.setHeaderText("ID no válido");
			alert.setContentText("No se ha encontrado el ID seleccionado");
			alert.showAndWait();

		} catch (IOException e) {
			sendFileError(residenciasFile.getName());
		} catch (Exception e) {

		} finally {

			if (resiFile != null) {

				try {
					resiFile.close();

				} catch (IOException e) {
					sendFileError(residenciasFile.getName());
				}
			}
		}
	}

	private Residencia findResidenciaById(int id) {

		ArrayList<Residencia> rList = new ArrayList<>(resiListProperty.get());

		try {
			return rList.stream().filter(r -> r.getId() == id).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	private void onResidenciaIDCambiado(Residencia nv) {

		if (nv != null) {
			id.set(String.valueOf(nv.getId()));
		}
	}

	private void insertarResidencia() {

		DialogoInsertarResidencia dialog = new DialogoInsertarResidencia(++listaResinciaId);
		Optional<Residencia> result = dialog.showAndWait();

		if (result.isPresent()) {
			insertResidenciaTable(result.get());
			tablaResidencias.getSelectionModel().clearSelection();
			tablaResidencias.getSelectionModel().select(result.get());
		}
	}

	private void insertResidenciaTable(Residencia myResi) {
		String nombre = myResi.getName();
		String codUni = myResi.getCodUniversidad();

		if (nombre.length() < LongitudResidencias) {

			for (int i = nombre.length(); i < LongitudResidencias; i++) {
				nombre += " ";
			}
		} else {
			nombre = nombre.substring(0, LongitudResidencias);
		}

		if (codUni.length() < LongitudUniversidad) {

			for (int i = codUni.length(); i < LongitudUniversidad; i++) {
				codUni += " ";
			}
		} else {
			codUni = codUni.substring(0, LongitudUniversidad);
		}

		resiListProperty.add(new Residencia(myResi.getId(), nombre, codUni, myResi.getPrecio(), myResi.isComedor()));

		RandomAccessFile resiFile = null;

		try {
			resiFile = new RandomAccessFile(residenciasFile.get(), "rw");
			resiFile.seek(resiFile.length());

			resiFile.writeInt(myResi.getId());
			resiFile.writeChar(',');
			resiFile.writeChars(nombre);
			resiFile.writeChar(',');
			resiFile.writeChars(codUni);
			resiFile.writeChar(',');
			resiFile.writeFloat(myResi.getPrecio());
			resiFile.writeChar(',');
			resiFile.writeBoolean(myResi.isComedor());
			resiFile.writeChar(',');

		} catch (EOFException eof) {

		} catch (IOException e) {
			sendFileError(residenciasFile.getName());

		} finally {

			if (resiFile != null) {

				try {
					resiFile.close();

				} catch (IOException e) {

					sendFileError(residenciasFile.getName());
				}
			}
		}

	}

	private void consultaResidencias() {
		FileChooser explorer = new FileChooser();
		explorer.setTitle("Explorador residencias");

		explorer.setInitialDirectory(new File(System.getProperty("user.dir")));
		explorer.getExtensionFilters().add(new FileChooser.ExtensionFilter("data", "*.dat"));
		File datFile = explorer.showOpenDialog(view.getScene().getWindow());
		if (datFile != null) {

			if (residenciasFile.get() != null) {
				resiListProperty.clear();
				listaResinciaId = 0;
			}

			residenciasFile.set(datFile);
			listarResidencias();
		}
	}

	private boolean isIDValid(String id) {

		try {

			@SuppressWarnings("unused")
			int idN = Integer.parseInt(id);
			return true;

		} catch (NumberFormatException e) {

		}

		return false;
	}

	private void sendFileError(String fileName) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error fichero");
		alert.setHeaderText("Error al cargar el fichero " + fileName);
		alert.showAndWait();
	}

	public final ListProperty<Residencia> resiListPropertyProperty() {
		return this.resiListProperty;
	}

	public final ObservableList<Residencia> getResiListProperty() {
		return this.resiListPropertyProperty().get();
	}

	public final void setResiListProperty(final ObservableList<Residencia> resiListProperty) {
		this.resiListPropertyProperty().set(resiListProperty);
	}

}
