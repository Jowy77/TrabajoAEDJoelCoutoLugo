package dad.javafx.mvc.accesoFicheros;

import java.io.File;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccesoFicheroModel {

	private StringProperty ruta = new SimpleStringProperty();
	private ObjectProperty<File> fichero = new SimpleObjectProperty<>();

	private StringProperty contenido = new SimpleStringProperty();

	private ObservableList<File> listaObserver = FXCollections.observableArrayList(new ArrayList<File>());
	private ListProperty<File> listaFicheros = new SimpleListProperty<>(listaObserver);

	private BooleanProperty esCarpeta = new SimpleBooleanProperty();

	private BooleanProperty esFichero = new SimpleBooleanProperty();

	public final StringProperty rutaProperty() {
		return this.ruta;
	}

	public final String getRuta() {
		return this.rutaProperty().get();
	}

	public final void setRuta(final String ruta) {
		this.rutaProperty().set(ruta);
	}

	public final StringProperty contenidoProperty() {
		return this.contenido;
	}

	public final String getContenido() {
		return this.contenidoProperty().get();
	}

	public final void setContenido(final String contenido) {
		this.contenidoProperty().set(contenido);
	}

	public final BooleanProperty esCarpetaProperty() {
		return this.esCarpeta;
	}

	public final boolean esCarpeta() {
		return this.esCarpetaProperty().get();
	}

	public final void setEsCarpeta(final boolean esCarpeta) {
		this.esCarpetaProperty().set(esCarpeta);
	}

	public final BooleanProperty esFicheroProperty() {
		return this.esFichero;
	}

	public final boolean esFichero() {
		return this.esFicheroProperty().get();
	}

	public final void setEsFichero(final boolean esFile) {
		this.esFicheroProperty().set(esFile);
	}

	public final ObjectProperty<File> ficheroProperty() {
		return this.fichero;
	}

	public final File getFichero() {
		return this.ficheroProperty().get();
	}

	public final void setFichero(final File fichero) {
		this.ficheroProperty().set(fichero);
	}

	public final ListProperty<File> ficheroListProperty() {
		return this.listaFicheros;
	}

	public final ObservableList<File> getFicheroList() {
		return this.ficheroListProperty().get();
	}

	public final void setFicheroList(final ObservableList<File> ficheroList) {
		this.ficheroListProperty().set(ficheroList);
	}

}
