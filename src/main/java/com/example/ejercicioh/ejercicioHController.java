package com.example.ejercicioh;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Persona;

import java.io.*;

/**
 * Controlador que maneja la ventana principal con la lista de personas.
 * Permite agregar, modificar, eliminar y filtrar personas, así como
 * importar y exportar datos en formato CSV.
 */
public class ejercicioHController {

    @FXML
    private TableView<Persona> personTable;

    @FXML
    private TextField filtrarField;

    @FXML
    private TableColumn<Persona, String> nombreColumn;

    @FXML
    private TableColumn<Persona, String> apellidosColumn;

    @FXML
    private TableColumn<Persona, Integer> edadColumn;

    @FXML
    private Button agregarButton;

    @FXML
    private Button modificarButton;

    @FXML
    private Button eliminarButton;

    private ObservableList<Persona> personasList = FXCollections.observableArrayList();

    /**
     * Inicializa la tabla y vincula las columnas a los datos de las personas.
     * Este método se llama automáticamente al inicializar la escena.
     */
    @FXML
    public void initialize() {
        nombreColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        apellidosColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));
        edadColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        personTable.setItems(personasList);
    }

    /**
     * Método que abre una ventana modal para agregar o modificar una persona
     * dependiendo del botón que se haya pulsado.
     *
     * @param event Evento disparado por los botones "Agregar Persona" o "Modificar Persona".
     */
    @FXML
    private void abrirVentanaAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ejercicioh/ejercicioHmodal.fxml"));
            Parent modalRoot = loader.load();
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(agregarButton.getScene().getWindow());

            ejercicioHModalController modalController = loader.getController();

            if (event.getSource() == agregarButton) {
                modalStage.setTitle("Agregar Persona");
                modalController.setPersonasList(personasList);
            } else if (event.getSource() == modificarButton) {
                Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();
                if (personaSeleccionada == null) {
                    mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para editar.");
                    return; // No continuar si no hay selección
                }
                modalStage.setTitle("Editar Persona");
                modalController.setPersonasList(personasList);
                modalController.setPersonaAEditar(personaSeleccionada);
            }

            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

            // Refrescar la tabla después de modificar
            personTable.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que elimina una persona seleccionada de la tabla.
     * Si no hay una persona seleccionada, muestra un mensaje de alerta.
     *
     * @param event Evento disparado al hacer clic en el botón de eliminar.
     */
    @FXML
    private void eliminarPersona(ActionEvent event) {
        // Obtener la persona seleccionada
        Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();

        if (personaSeleccionada == null) {
            // Si no hay ninguna persona seleccionada, mostrar alerta
            mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para eliminar.");
        } else {
            // Eliminar la persona seleccionada de la lista
            personasList.remove(personaSeleccionada);
            // Mostrar un mensaje de confirmación
            mostrarAlerta("Persona eliminada", "La persona ha sido eliminada con éxito.");
        }
    }

    /**
     * Muestra una alerta con el título y el mensaje especificado.
     *
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Filtra las personas en la tabla según el texto ingresado en el campo de filtro.
     */
    public void filtrar() {
        String textoFiltro = filtrarField.getText().toLowerCase();

        ObservableList<Persona> personasFiltradas = FXCollections.observableArrayList();

        for (Persona persona : personasList) {
            if (persona.getNombre().toLowerCase().contains(textoFiltro)) {
                personasFiltradas.add(persona);
            }
        }

        personTable.setItems(personasFiltradas);
    }

    /**
     * Método que exporta la información de la tabla a un archivo CSV.
     *
     * @param actionEvent Evento disparado al hacer clic en el botón de exportar.
     */
    public void exportar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showSaveDialog(agregarButton.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Escribir cabecera
                writer.write("Nombre,Apellidos,Edad");
                writer.newLine();

                // Escribir datos
                for (Persona persona : personasList) {
                    writer.write(persona.getNombre() + "," + persona.getApellido() + "," + persona.getEdad());
                    writer.newLine();
                }

                mostrarAlerta("Exportación Exitosa", "Los datos han sido exportados a " + file.getAbsolutePath());
            } catch (IOException e) {
                mostrarAlerta("Error de Exportación", "No se pudo exportar el archivo: " + e.getMessage());
            }
        }
    }

    /**
     * Método que importa datos de un archivo CSV a la tabla.
     *
     * @param actionEvent Evento disparado al hacer clic en el botón de importar.
     */
    public void importar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar desde CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showOpenDialog(agregarButton.getScene().getWindow());

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // Saltar la primera línea (cabecera)
                        continue;
                    }

                    String[] datos = line.split(",");

                    // Validar que haya suficientes datos y que no estén vacíos
                    if (datos.length == 3) {
                        String nombre = datos[0].trim();
                        String apellidos = datos[1].trim();
                        Integer edad;

                        // Validar que los campos no estén vacíos
                        if (nombre.isEmpty() || apellidos.isEmpty()) {
                            mostrarAlerta("Error de Importación", "Los campos Nombre y Apellidos no pueden estar vacíos.");
                            continue; // Saltar este registro
                        }

                        // Validar edad
                        try {
                            edad = Integer.parseInt(datos[2].trim());
                        } catch (NumberFormatException e) {
                            mostrarAlerta("Error de Importación", "La edad debe ser un número: " + datos[2]);
                            continue; // Saltar este registro
                        }

                        // Comprobar si la persona ya existe
                        boolean existe = personasList.stream()
                                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre) && p.getApellido().equalsIgnoreCase(apellidos) && p.getEdad() == edad);

                        if (!existe) {
                            personasList.add(new Persona(nombre, apellidos, edad));
                        } else {
                            mostrarAlerta("Registro Duplicado", "La persona " + nombre + " " + apellidos + " ya existe.");
                        }
                    } else {
                        mostrarAlerta("Error de Importación", "Línea inválida: " + line);
                    }
                }

                mostrarAlerta("Importación Exitosa", "Los datos han sido importados desde " + file.getAbsolutePath());
            } catch (IOException e) {
                mostrarAlerta("Error de Importación", "No se pudo importar el archivo: " + e.getMessage());
            }
        }
    }
}
