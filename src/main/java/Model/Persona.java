package Model;

import java.util.Objects;

/**
 * Clase Persona que representa una persona con un nombre, apellido y edad.
 */
public class Persona {
    private String nombre;
    private String apellido;
    private int edad;

    /**
     * Constructor que inicializa una persona con nombre, apellido y edad.
     *
     * @param nombre   Nombre de la persona.
     * @param apellido Apellido de la persona.
     * @param edad     Edad de la persona.
     */
    public Persona(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    /**
     * Obtiene el nombre de la persona.
     *
     * @return Nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la persona.
     *
     * @param nombre Nombre de la persona.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido de la persona.
     *
     * @return Apellido de la persona.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido de la persona.
     *
     * @param apellido Apellido de la persona.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene la edad de la persona.
     *
     * @return Edad de la persona.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad de la persona.
     *
     * @param edad Edad de la persona.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Sobrescribe el método equals para comparar si dos personas son iguales
     * basándose en su nombre, apellido y edad.
     *
     * @param o Objeto a comparar.
     * @return true si los objetos son iguales, false de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return edad == persona.edad &&
                Objects.equals(nombre, persona.nombre) &&
                Objects.equals(apellido, persona.apellido);
    }

    /**
     *
     * Sobrescribe el metodo HashCode para convertir un hash
     * en el nombre, apellido y edad de la persona.
     *
     * @return Código hash de la persona.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellido, edad);
    }
}
