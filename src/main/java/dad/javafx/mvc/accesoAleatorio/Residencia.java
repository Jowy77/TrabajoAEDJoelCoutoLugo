package dad.javafx.mvc.accesoAleatorio;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Residencia {

	private IntegerProperty idUniversidad = new SimpleIntegerProperty();
	private StringProperty nombre = new SimpleStringProperty();
	private StringProperty codUniversidad = new SimpleStringProperty();
	private FloatProperty precio = new SimpleFloatProperty();
	private StringProperty comedorString = new SimpleStringProperty();
	private BooleanProperty comedorBoolean = new SimpleBooleanProperty();

	public Residencia(int id, String name, String codUniversidad, float precio, boolean comedor) {

		setId(id);
		setName(name);
		setCodUniversidad(codUniversidad);
		setPrecio(precio);
		setComedor(comedor);

		comedorString.bind(Bindings.when(this.comedorBoolean).then(new SimpleStringProperty("Si"))
				.otherwise(new SimpleStringProperty("No")));
	}

	public final float getPrecio() {
		return this.precioProperty().get();
	}

	public final void setPrecio(final float precio) {
		this.precioProperty().set(precio);
	}

	public final BooleanProperty comedorProperty() {
		return this.comedorBoolean;
	}

	public final boolean isComedor() {
		return this.comedorProperty().get();
	}

	public final void setComedor(final boolean comedor) {
		this.comedorProperty().set(comedor);
	}

	public final StringProperty comedorStrProperty() {
		return this.comedorString;
	}

	public final String getComedorString() {
		return this.comedorStrProperty().get();
	}

	public final void setComedorString(final String comedorString) {
		this.comedorStrProperty().set(comedorString);
	}

	public final IntegerProperty idProperty() {
		return this.idUniversidad;
	}

	public final int getId() {
		return this.idProperty().get();
	}

	public final void setId(final int id) {
		this.idProperty().set(id);
	}

	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	public final String getName() {
		return this.nombreProperty().get();
	}

	public final void setName(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final StringProperty codUniversidadProperty() {
		return this.codUniversidad;
	}

	public final String getCodUniversidad() {
		return this.codUniversidadProperty().get();
	}

	public final void setCodUniversidad(final String codUniversidad) {
		this.codUniversidadProperty().set(codUniversidad);
	}

	public final FloatProperty precioProperty() {
		return this.precio;
	}
}
