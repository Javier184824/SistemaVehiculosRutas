/*
 * Nombre del Archivo: ControladorUsuarios.java
 * 
 * Descripcion: Controlador que gestiona la lista de usuarios del sistema.
 *              Proporciona funcionalidades para agregar, buscar, eliminar
 *              y persistir usuarios en un archivo binario. Utiliza una
 *              LinkedList para almacenar los usuarios y maneja operaciones
 *              de serialización/deserialización para persistencia de datos.
 *              Incluye validaciones para evitar duplicados y búsquedas
 *              por diferentes criterios.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import modelo.Usuario;

/**
 * Controlador que gestiona la lista de usuarios del sistema
 * 
 * Esta clase proporciona funcionalidades completas para la gestión de usuarios:
 * - Almacenamiento de usuarios en una LinkedList
 * - Operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * - Búsqueda de usuarios por diferentes criterios
 * - Persistencia de datos en archivo binario
 * - Validaciones para evitar duplicados
 * 
 * Utiliza serialización de objetos para guardar y recuperar la lista de usuarios.
 */
public class ControladorUsuarios {
    private LinkedList<Usuario> lista;
    
    /**
     * Constructor por defecto del controlador de usuarios
     * 
     * Notas:
     * - Inicializa una LinkedList vacía para almacenar usuarios
     * - Lista lista para operaciones de gestión de usuarios
     */
    public ControladorUsuarios() {
        lista = new LinkedList<>();
    }
    
    /**
     * Agrega un nuevo usuario a la lista
     * 
     * @param nuevo Usuario a agregar a la lista
     * 
     * Proceso:
     * - Verifica que el usuario no exista ya en la lista
     * - Utiliza el método equals para comparar usuarios
     * - Agrega el usuario solo si no es duplicado
     * 
     * Notas:
     * - Evita duplicados automáticamente
     * - No retorna confirmación de la operación
     * - Utiliza LinkedList para inserción eficiente
     */
    public void agregar(Usuario nuevo) {
        for (Usuario otro : lista) {
            if (nuevo.equals(otro)) {
                return;
            }
        }
        lista.add(nuevo);
    }
    
    /**
     * Busca un usuario en la lista tomando como referencia otra instancia de usuario
     * 
     * @param referencia Usuario de referencia para buscar la instancia que se desea encontrar
     * @return Usuario encontrado en la lista, o null si no se encuentra
     * 
     * Proceso:
     * - Recorre toda la lista de usuarios
     * - Compara cada usuario con la referencia usando equals
     * - Retorna el primer usuario que coincida
     * 
     * Notas:
     * - Utiliza el método equals del usuario para comparación
     * - Retorna null si no encuentra coincidencias
     * - Útil para verificar existencia de usuarios
     */
    public Usuario buscar(Usuario referencia) {
        for (Usuario otro : lista) {
            if (referencia.equals(otro)) {
                return otro;
            }
        }
        return null;
    }
    
    /**
     * Busca un usuario en la lista por nombre
     * 
     * @param nombre String con el nombre del usuario que se quiere buscar
     * @return Usuario encontrado con el nombre especificado, o null si no se encuentra
     * 
     * Proceso:
     * - Recorre toda la lista de usuarios
     * - Compara el nombre de cada usuario con el nombre buscado
     * - Retorna el primer usuario que coincida
     * 
     * Notas:
     * - Búsqueda exacta por nombre
     * - Retorna null si no encuentra coincidencias
     * - Útil para autenticación por nombre de usuario
     */
    public Usuario buscar(String nombre) {
        for (Usuario otro : lista) {
            if (otro.getNombre().equals(nombre)) {
                return otro;
            }
        }
        return null;
    }
    
    /**
     * Elimina un usuario de la lista tomando como referencia otra instancia de usuario
     * 
     * @param referencia Usuario de referencia para buscar la instancia que se desea eliminar
     * 
     * Proceso:
     * - Utiliza el método remove de LinkedList
     * - Elimina el primer usuario que coincida con la referencia
     * 
     * Notas:
     * - Utiliza el método equals del usuario para comparación
     * - No retorna confirmación de la operación
     * - Si no encuentra el usuario, no hace nada
     */
    public void eliminar(Usuario referencia) {
        lista.remove(referencia);
    }
    
    /**
     * Elimina un usuario de la lista por nombre
     * 
     * @param nombre String con el nombre del usuario que se quiere eliminar
     * 
     * Proceso:
     * - Recorre la lista por índice
     * - Compara el nombre de cada usuario con el nombre especificado
     * - Elimina el primer usuario que coincida
     * 
     * Notas:
     * - Búsqueda exacta por nombre
     * - Elimina solo el primer usuario encontrado con ese nombre
     * - Si no encuentra el usuario, no hace nada
     */
    public void eliminar(String nombre) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre().equals(nombre)) {
                lista.remove(i);
            }
        }
    }
    
    /**
     * Guarda la lista de usuarios en un archivo binario
     * 
     * Proceso:
     * - Crea un FileOutputStream para el archivo "cuentasUsarios.dat"
     * - Crea un ObjectOutputStream para serializar objetos
     * - Escribe la lista completa de usuarios en el archivo
     * - Cierra los streams de salida
     * 
     * Manejo de errores:
     * - Captura IOException y muestra mensaje de advertencia
     * - No interrumpe la ejecución del programa
     * 
     * Notas:
     * - Utiliza serialización de objetos Java
     * - Sobrescribe el archivo si ya existe
     * - Archivo: "cuentasUsarios.dat"
     */
    public void guardarLista() {
        try {
            FileOutputStream archivo = new FileOutputStream("cuentasUsarios.dat");
            ObjectOutputStream objectStream = new ObjectOutputStream(archivo);
            objectStream.writeObject(lista);
            objectStream.close();
            archivo.close();
        }
        catch (IOException ex) {
            System.out.println("WARNING: IOException was caught");
        }
    }
    
    /**
     * Recupera la lista de usuarios desde un archivo binario
     * 
     * Proceso:
     * - Crea un FileInputStream para leer el archivo "cuentasUsarios.dat"
     * - Crea un ObjectInputStream para deserializar objetos
     * - Lee la lista completa de usuarios del archivo
     * - Cierra los streams de entrada
     * 
     * Manejo de errores:
     * - Captura IOException y muestra mensaje de advertencia
     * - Captura ClassNotFoundException y muestra mensaje de advertencia
     * - No interrumpe la ejecución del programa
     * 
     * Notas:
     * - Utiliza deserialización de objetos Java
     * - Reemplaza la lista actual con los datos del archivo
     * - Archivo: "cuentasUsarios.dat"
     * - Si el archivo no existe, mantiene la lista vacía
     */
    public void recuperarLista() {
        try {
            FileInputStream archivo = new FileInputStream("cuentasUsarios.dat");
            ObjectInputStream objectStream = new ObjectInputStream(archivo);
            lista = (LinkedList<Usuario>) objectStream.readObject();
            objectStream.close();
            archivo.close();
        }
        catch (IOException ex) {
            System.out.println("WARNING: IOException was caught");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("WARNING: ClassNotFoundException was caught");
        }
    }

    /**
     * Genera una representación en cadena del controlador de usuarios
     * 
     * @return Cadena con información del controlador y la lista de usuarios
     * 
     * Formato:
     * - Nombre de la clase
     * - Contenido de la lista de usuarios
     * 
     * Notas:
     * - Utiliza el método toString de LinkedList
     * - Útil para debugging y logging
     */
    @Override
    public String toString() {
        return "ControladorUsuariosRegulares{" + "lista=" + lista + '}';
    }
}
