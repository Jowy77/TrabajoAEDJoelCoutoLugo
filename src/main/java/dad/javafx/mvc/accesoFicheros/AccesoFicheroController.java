package dad.javafx.mvc.accesoFicheros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;

public class AccesoFicheroController {

	private AccesoFicheroModel model = new AccesoFicheroModel();
	private AccesoFicheroView view = new AccesoFicheroView();

	public AccesoFicheroController() {

		model.rutaProperty().bindBidirectional(view.getRutaTextoLabel().textProperty());

		view.getFicheroList().itemsProperty().bind(model.ficheroListProperty());

		model.ficheroProperty().bind(view.getFicheroList().getSelectionModel().selectedItemProperty());

		view.getFicheroList().getSelectionModel().selectedItemProperty()
				.addListener((o, lv, nv) -> onSeleccionDeFichero(nv));
		view.getCopiarButon().setOnAction(e -> onCopiarAction(e));
		view.getBotonBorrar().setOnAction(e -> onBorrarAction(e));
		view.getBotonContenido().setOnAction(e -> onVerContenidoAction(e));
		view.getBotonModificar().setOnAction(e -> onModificarAction(e));
		view.getBotonView().setOnAction(e -> onVerFicheroAction(e));
		view.getBotonCrear().setOnAction(e -> onCrearAction(e));
		view.getBotonMover().setOnAction(e -> onMoverAction(e));

		view.getNombreFichTexto().textProperty().bind(Bindings.when(model.ficheroProperty().isNull())
				.then(new SimpleStringProperty("")).otherwise(model.ficheroProperty().asString()));

		view.getBotonModificar().disableProperty().bind(view.getFicheroList().getSelectionModel().selectedItemProperty()
				.isNull().or(model.esCarpetaProperty()));
		view.getBotonContenido().disableProperty().bind(view.getFicheroList().getSelectionModel().selectedItemProperty()
				.isNull().or(model.esCarpetaProperty()));

		view.getBotonBorrar().disableProperty()
				.bind(view.getFicheroList().getSelectionModel().selectedItemProperty().isNull());
		view.getBotonMover().disableProperty()
				.bind(view.getFicheroList().getSelectionModel().selectedItemProperty().isNull());
		view.getCopiarButon().disableProperty()
				.bind(view.getFicheroList().getSelectionModel().selectedItemProperty().isNull());

		view.getAreaContent().textProperty().bindBidirectional(model.contenidoProperty());

		view.getCarpetaRadio().selectedProperty().bindBidirectional(model.esCarpetaProperty());
		view.getFicheroRadio().selectedProperty().bindBidirectional(model.esFicheroProperty());
	}

	private void onSeleccionDeFichero(File nv) {

		if (nv != null) {
			model.setEsFichero(nv.isFile());
			model.setEsCarpeta(nv.isDirectory());
		} else {
			model.setEsFichero(false);
			model.setEsCarpeta(false);
		}
	}

	private void onMoverAction(ActionEvent e) {

		File fichero = model.getFichero();

		if (fichero != null) {

			String dialogo;

			if (fichero.isDirectory()) {
				dialogo = MensajeDelSistema.sendDialog("Mover", "Mover directorio",
						"Introduzca Nueva Ruta\n " + "y el nombre de la carpeta" + fichero.getName());
			} else {
				dialogo = MensajeDelSistema.sendDialog("Mover", "Mover fichero",
						"Establezca la nueva ubicación para el fichero\n junto con" + "el nombre del fichero"
								+ fichero.getName());
			}

			if (dialogo.equals("")) {
				MensajeDelSistema.sendError("Mover fichero/directorio", "Ruta incorrecta, no puede estar vacio");

			} else {

				File destino = new File(dialogo);
				fichero.renameTo(destino);
			}

			actualizarLista();

		} else {
			MensajeDelSistema.sendError("Mover, cuidado", "No has seleccionado nada");
		}
	}

	// modificada
	private void onModificarAction(ActionEvent e) {

		File fichero = model.getFichero();

		if (fichero == null || fichero.isDirectory()) {
			MensajeDelSistema.sendError("Fichero",
					"O esta vacio o ha seleccionado un directorio(solo se pueden modificar ficheros)");
		}

		FileOutputStream fileStream = null;
		OutputStreamWriter out = null;
		BufferedWriter bufferW = null;

		try {

			fileStream = new FileOutputStream(fichero);
			out = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
			bufferW = new BufferedWriter(out);
			bufferW.write(model.getContenido());

		} catch (IOException e1) {
			MensajeDelSistema.sendError("Escritura fichero", "Error al escribir en el fichero");

		} finally {

			try {
				if (bufferW != null) {
					bufferW.close();
				}
			} catch (IOException e1) {
				MensajeDelSistema.sendError("Escritura fichero", "Error modificar el fichero");
				e1.printStackTrace();
			}
		}
	}

	private void onCopiarAction(ActionEvent e) {

		File fichero = model.getFichero();

		if (fichero != null) {

			String nuevaRuta = MensajeDelSistema.sendDialog("Copiar", "Introducir Ruta",
					"Ruta para copiar" + fichero.getName());

			if (nuevaRuta.equals("")) {
				MensajeDelSistema.sendError("Error", "Ruta Invalida");
			}

			File destino = new File(nuevaRuta);

			if (destino.exists()) {
				boolean confirmacionSobre = MensajeDelSistema.confirmDialog("Copiar", "Ya existe",
						"Este fichero ya existe, \n¿Está seguro que desea sobreescribirlo?");

				if (confirmacionSobre) {

					try {
						Files.copy(fichero.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
						actualizarLista();
					} catch (IOException e1) {
						MensajeDelSistema.sendError("Copiar", "Error al copiar el fichero " + fichero.getName());
					}
				}
			}

			else {
				try {
					Files.copy(fichero.toPath(), destino.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
					actualizarLista();
				} catch (IOException e1) {
					MensajeDelSistema.sendError("Copiar", "Error al copiar el fichero " + fichero.getName());
				}
			}

		}
	}

	private void onVerContenidoAction(ActionEvent e) {

		File file = model.getFichero();
		if (file == null || file.isDirectory()) {
			MensajeDelSistema.sendError("Fichero",
					"No tiene ningún fichero seleccionado o ha seleccionado un directorio");
		}

		FileInputStream f = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		StringBuilder fileText = new StringBuilder();

		try {

			f = new FileInputStream(file);
			in = new InputStreamReader(f, java.nio.charset.StandardCharsets.UTF_8);
			reader = new BufferedReader(in);

			String line;
			while ((line = reader.readLine()) != null) {
				fileText.append(line + "\n");
			}

			model.setContenido(fileText.toString());

		} catch (IOException e1) {
			MensajeDelSistema.sendError("Leer fichero", "Error al leer en el fichero");
			e1.printStackTrace();

		} finally {

			try {
				if (reader != null) {
					reader.close();
				}

				if (in != null) {
					in.close();
				}
				if (f != null) {
					f.close();
				}
			} catch (IOException e1) {
				MensajeDelSistema.sendError("Leer fichero", "Error al leer en el fichero");
				e1.printStackTrace();
			}
		}
	}

	private void onBorrarAction(ActionEvent e) {

		File file = model.getFichero();

		if (file.isDirectory()) {
			boolean q = MensajeDelSistema.confirmDialog("Eliminar", "Eliminar directorio",
					"¿Está seguro de eliminar el directorio y su contenido?");
			if (q) {

				removeDir(file);

				if (!file.delete()) {
					MensajeDelSistema.sendError("Eliminar fichero",
							"Se ha producido un error al borrar el directorio " + file.getName());
				}

				actualizarLista();
			}
		}

		else {

			boolean q = MensajeDelSistema.confirmDialog("Eliminar", "Eliminar fichero",
					"¿Está seguro de eliminar este fichero?");
			if (q) {
				if (file.delete())
					actualizarLista();
				else
					MensajeDelSistema.sendError("Eliminar fichero",
							"Ha habido un error al eliminar el fichero seleccionado");
			}
		}
	}

	private void removeDir(File file) {

		for (File f : file.listFiles()) {

			if (f.isDirectory()) {
				removeDir(f);
			}
			if (!f.delete()) {
				MensajeDelSistema.sendError("Eliminar fichero",
						"Se ha producido un error al borrar el fichero/directorio " + file.getName());
			}
		}
	}

	private void onCrearAction(ActionEvent e) {

		try {

			if (model.esCarpeta()) {

				String fileName = MensajeDelSistema.sendDialog("Crear directorio", "Nombre Directorio",
						"Introduzca el nombre del directorio");

				if (fileName.equals("")) {
					MensajeDelSistema.sendError("Crear fichero",
							"No ha introducido un nombre correcto para el directorio");
					return;
				}

				File file = new File(model.getRuta() + "/" + fileName);
				file.mkdir();
			}

			else {
				String fileName = MensajeDelSistema.sendDialog("Crear fichero", "Nombre fichero",
						"Introduzca el nombre del fichero");

				if (fileName.equals("")) {
					MensajeDelSistema.sendError("Crear fichero", "Ese nombre no es valido");
					return;
				}

				File file = new File(model.getRuta() + "/" + fileName);

				file.createNewFile();
			}

			actualizarLista();

		} catch (IOException e1) {

			MensajeDelSistema.sendError("Crear fichero/directorio",
					"No se pudo crear el fichero/directorio, asegúrese de que la ruta es correcta");
		}

	}

	private void onVerFicheroAction(ActionEvent e) {

		File rutaActual = new File(model.getRuta());

		if (rutaActual == null || !rutaActual.isDirectory()) {

			MensajeDelSistema.sendError("Ruta", "La ruta introducida no es válida");
		} else {
			actualizarLista();
		}
	}

	private void actualizarLista() {

		File rutaActual = new File(model.getRuta());

		model.getFicheroList().clear();

		for (File fichero : rutaActual.listFiles()) {

			model.getFicheroList().add(fichero);

		}

	}

	public AccesoFicheroView getRootView() {
		return view;
	}
}
