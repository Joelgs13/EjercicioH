package com.example.ejercicioh;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Persona;

/**
 * Controlador para la ventana modal que permite agregar o editar una persona.
 */
public class ejercicioHModalController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidosField;

    @FXML
    private TextField edadField;

    private ObservableList<Persona> personasList;
    private Persona personaAEditar = null; // Referencia a la persona a editar, si existe
    private boolean esModificacion = false;

    /**
     * Establece la lista de personas a la que se agregará o modificará la persona.
     *
     * @param personasList Lista de personas.
     */
    public void setPersonasList(ObservableList<Persona> personasList) {
        this.personasList = personasList;
    }

    /**
     * Establece la persona que se va a editar.
     * Si se llama a este método, se sobreescribe la persona seleccionada y el modal se comportará como un editor.
     *
     * @param persona Persona cuyos datos se van a editar.
     */
    public void setPersonaAEditar(Persona persona) {
        this.personaAEditar = persona;
        this.esModificacion = true;
        rellenarCampos(persona); // Rellenar los campos con la persona a editar
    }

    /**
     * Rellena los campos de texto con los datos de una persona existente para editarla.
     *
     * @param persona Persona cuyos datos se van a editar.
     */
    public void rellenarCampos(Persona persona) {
        nombreField.setText(persona.getNombre());
        apellidosField.setText(persona.getApellido());
        edadField.setText(String.valueOf(persona.getEdad()));
    }

    /**
     * Método que maneja el evento de agregar o editar una persona. Este método es llamado cuando
     * se pulsa el botón "Guardar" en la ventana modal.
     */
    @FXML
    private void aniadirPersona() {
        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String edadText = edadField.getText().trim();
        StringBuilder errores = new StringBuilder();

        if (nombre.isEmpty()) {
            errores.append("El campo 'Nombre' no puede estar vacío.\n");
        }
        if (apellidos.isEmpty()) {
            errores.append("El campo 'Apellidos' no puede estar vacío.\n");
        }

        int edad = -1;
        try {
            edad = Integer.parseInt(edadText);
            if (edad < 0) {
                errores.append("La edad debe ser un número positivo.\n");
            }
        } catch (NumberFormatException e) {
            errores.append("El campo 'Edad' debe ser un número entero válido.\n");
        }

        if (errores.length() > 0) {
            mostrarError(errores.toString());
            return;
        }

        if (esModificacion && personaAEditar != null) {
            // Si estamos modificando, actualizamos los valores de la persona existente
            personaAEditar.setNombre(nombre);
            personaAEditar.setApellido(apellidos);
            personaAEditar.setEdad(edad);

            // Mostrar el mensaje de éxito
            mostrarInformacion("Persona modificada con éxito.");
        } else {
            // Verificar que la persona no sea duplicada antes de agregarla
            Persona nuevaPersona = new Persona(nombre, apellidos, edad);
            for (Persona persona : personasList) {
                if (persona.equals(nuevaPersona)) {
                    mostrarError("Persona duplicada: Ya existe una persona con los mismos datos.");
                    return;
                }
            }
            // Agregar la nueva persona si no es duplicada
            personasList.add(nuevaPersona);
            mostrarInformacion("Persona agregada con éxito.");
        }

        // Cerrar la ventana modal
        cerrarVentana();
    }

    /**
     * Muestra un mensaje de éxito en una alerta emergente.
     *
     * @param mensaje Mensaje de éxito a mostrar.
     */
    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de error en una alerta emergente.
     *
     * @param mensaje Mensaje de error a mostrar.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Cierra la ventana modal.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
