package com.example.ejercicioh;

import Dao.DaoPersona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Persona;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que maneja la ventana principal con la lista de personas.
 * Permite agregar, modificar, eliminar y filtrar personas, cargando los datos desde una base de datos.
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
    private DaoPersona daoPersona = new DaoPersona();

    /**
     * Inicializa la tabla y carga los datos desde la base de datos.
     */
    @FXML
    public void initialize() {
        nombreColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        apellidosColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));
        edadColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        // Cargar datos desde la base de datos
        cargarPersonasDesdeBD();
    }

    /**
     * Carga las personas desde la base de datos y las añade a la lista.
     */
    private void cargarPersonasDesdeBD() {
        try {
            List<Persona> personas = daoPersona.obtenerTodas();
            personasList.setAll(personas); // Actualiza la lista con las personas obtenidas de la base de datos
            personTable.setItems(personasList);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos desde la base de datos: " + e.getMessage());
        }
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
            modalController.setPersonasList(personasList);
            modalController.setDaoPersona(daoPersona);  // Pasamos el DAO al modal

            if (event.getSource() == agregarButton) {
                modalStage.setTitle("Agregar Persona");
            } else if (event.getSource() == modificarButton) {
                Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();
                if (personaSeleccionada == null) {
                    mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para editar.");
                    return;
                }
                modalStage.setTitle("Editar Persona");
                modalController.setPersonaAEditar(personaSeleccionada);
            }

            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

            // Recargar la tabla después de agregar o modificar
            cargarPersonasDesdeBD();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que elimina una persona seleccionada de la tabla.
     * Si no hay una persona seleccionada, muestra un mensaje de alerta.
     */
    @FXML
    private void eliminarPersona(ActionEvent event) {
        Persona personaSeleccionada = personTable.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para eliminar.");
        } else {
            try {
                daoPersona.eliminar(personaSeleccionada.getId());
                personasList.remove(personaSeleccionada);
                mostrarAlerta("Persona eliminada", "La persona ha sido eliminada con éxito.");
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar la persona: " + e.getMessage());
            }
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
}
