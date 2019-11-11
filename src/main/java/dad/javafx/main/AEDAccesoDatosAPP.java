package dad.javafx.main;

import dad.javafx.mvc.accesoAleatorio.AccesoAleatorioController;
import dad.javafx.mvc.accesoFicheros.AccesoFicheroController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.stage.Stage;

public class AEDAccesoDatosAPP extends Application {

	private AccesoFicheroController tabAccesoFichero;
	private AccesoAleatorioController tabAccesoAleatorio;

	@Override
	public void start(Stage primaryStage) throws Exception {

		TabPane tab = new TabPane();

		tabAccesoFichero = new AccesoFicheroController();
		Tab tabAccesoFicheros = new Tab("Acceso a ficheros Joel Couto Lugo");
		tabAccesoFicheros.setContent(tabAccesoFichero.getRootView());

		tabAccesoAleatorio = new AccesoAleatorioController();
		Tab tabRandomAccess = new Tab("Acceso aleatorio Joel Couto Lugo");
		tabRandomAccess.setContent(tabAccesoAleatorio.getRootView());

		tab.getTabs().addAll(tabAccesoFicheros, tabRandomAccess);

		tab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		Scene scene = new Scene(tab, 900, 700);

		primaryStage.setTitle("Acceso a datos Joel Couto Lugo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}

}
