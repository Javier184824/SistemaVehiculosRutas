/*
 * Nombre del Archivo: BinaryDataManager.java
 * 
 * Descripcion: Gestor de datos binarios que implementa la interfaz DataManager.
 *              Proporciona funcionalidades completas para la persistencia de
 *              objetos serializables en archivos binarios. Maneja operaciones
 *              de guardado, carga, eliminación y respaldo de archivos de datos.
 *              Incluye gestión de directorios, validaciones de archivos y
 *              manejo robusto de errores de entrada/salida.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Binary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Interfaces.DataManager;
import Interfaces.Serializable;
import Interfaces.SerializableFactory;
import Interfaces.SerializationException;

/**
 * Gestor de datos binarios para persistencia de objetos
 * 
 * Esta clase implementa la interfaz DataManager y proporciona funcionalidades
 * completas para la gestión de datos binarios en el sistema. Maneja la
 * serialización y deserialización de objetos, gestión de archivos y
 * operaciones de respaldo.
 * 
 * Características principales:
 * - Gestión de directorio de datos centralizado
 * - Serialización/deserialización de objetos individuales y listas
 * - Operaciones de respaldo y restauración de archivos
 * - Validaciones de archivos y manejo de errores
 * - Información detallada del directorio de datos
 */
public class BinaryDataManager implements DataManager {
    private final String dataDirectory;
    private static final String DATA_DIR_DEFAULT = "data";
    
    /**
     * Constructor por defecto del gestor de datos binarios
     * 
     * Utiliza el directorio de datos por defecto "data" y crea
     * el directorio si no existe.
     */
    public BinaryDataManager() {
        this(DATA_DIR_DEFAULT);
    }
    
    /**
     * Constructor con directorio de datos personalizado
     * 
     * @param dataDirectory Ruta del directorio donde se almacenarán los archivos de datos
     * 
     * Proceso:
     * - Asigna el directorio de datos especificado
     * - Crea el directorio si no existe
     * 
     * Notas:
     * - Lanza RuntimeException si no puede crear el directorio
     * - El directorio se crea automáticamente si no existe
     */
    public BinaryDataManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        createDataDirectoryIfNotExists();
    }
    
    /**
     * Crea el directorio de datos si no existe
     * 
     * Proceso:
     * - Verifica si el directorio existe
     * - Crea el directorio y subdirectorios si es necesario
     * 
     * Manejo de errores:
     * - Lanza RuntimeException si no puede crear el directorio
     * - Incluye información del directorio en el mensaje de error
     */
    private void createDataDirectoryIfNotExists() {
        try {
            Path path = Paths.get(dataDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de datos: " + dataDirectory, e);
        }
    }
    
    /**
     * Obtiene la ruta completa de un archivo
     * 
     * @param filename Nombre del archivo
     * @return Ruta completa del archivo en el directorio de datos
     * 
     * Notas:
     * - Utiliza el separador de archivos del sistema operativo
     * - Combina el directorio de datos con el nombre del archivo
     */
    private String getFilePath(String filename) {
        return dataDirectory + File.separator + filename;
    }
    
    /**
     * Guarda un objeto serializable en un archivo
     * 
     * @param object Objeto a guardar (debe implementar Serializable)
     * @param filename Nombre del archivo donde guardar el objeto
     * @throws SerializationException Si hay error en la serialización o guardado
     * 
     * Proceso:
     * - Valida que el objeto no sea null
     * - Crea streams de salida para el archivo
     * - Serializa el objeto usando su método serialize()
     * - Cierra los streams automáticamente
     * 
     * Manejo de errores:
     * - Valida objeto null antes de procesar
     * - Captura IOException y la convierte en SerializationException
     * - Incluye información del archivo en el mensaje de error
     */
    @Override
    public void save(Serializable object, String filename) throws SerializationException {
        if (object == null) {
            throw new SerializationException("No se puede guardar un objeto null");
        }
        
        String filePath = getFilePath(filename);
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            
            object.serialize(dos);
            dos.flush();
            
        } catch (IOException e) {
            throw new SerializationException("Error al guardar objeto en archivo: " + filename, e);
        }
    }
    
    /**
     * Guarda una lista de objetos serializables en un archivo
     * 
     * @param objects Lista de objetos a guardar
     * @param filename Nombre del archivo donde guardar la lista
     * @throws SerializationException Si hay error en la serialización o guardado
     * 
     * Proceso:
     * - Valida que la lista no sea null
     * - Escribe el número de objetos al inicio del archivo
     * - Serializa cada objeto individualmente
     * - Valida que ningún objeto en la lista sea null
     * 
     * Formato del archivo:
     * - Entero: número de objetos en la lista
     * - Objeto 1 serializado
     * - Objeto 2 serializado
     * - ... (continúa para todos los objetos)
     * 
     * Manejo de errores:
     * - Valida lista null antes de procesar
     * - Valida cada objeto individual en la lista
     * - Captura IOException y la convierte en SerializationException
     */
    @Override
    public void saveList(List<? extends Serializable> objects, String filename) throws SerializationException {
        if (objects == null) {
            throw new SerializationException("No se puede guardar una lista null");
        }
        
        String filePath = getFilePath(filename);
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             DataOutputStream dos = new DataOutputStream(fos)) {
            
            // Escribe el número de objetos primero
            dos.writeInt(objects.size());
            
            // Escribe cada objeto
            for (Serializable object : objects) {
                if (object == null) {
                    throw new SerializationException("No se puede guardar un objeto null en la lista");
                }
                object.serialize(dos);
            }
            
            dos.flush();
            
        } catch (IOException e) {
            throw new SerializationException("Error al guardar lista en archivo: " + filename, e);
        }
    }
    
    /**
     * Carga un objeto serializable desde un archivo
     * 
     * @param filename Nombre del archivo a cargar
     * @param factory Factory para crear instancias del tipo de objeto
     * @return Objeto cargado desde el archivo
     * @throws SerializationException Si hay error en la deserialización o carga
     * 
     * Proceso:
     * - Valida que la factory no sea null
     * - Verifica que el archivo existe
     * - Crea streams de entrada para el archivo
     * - Crea una nueva instancia usando la factory
     * - Deserializa el objeto usando su método deserialize()
     * 
     * Manejo de errores:
     * - Valida factory null antes de procesar
     * - Verifica existencia del archivo
     * - Captura IOException y la convierte en SerializationException
     */
    @Override
    public <T extends Serializable> T load(String filename, SerializableFactory<T> factory) throws SerializationException {
        if (factory == null) {
            throw new SerializationException("La factory no puede ser null");
        }
        
        String filePath = getFilePath(filename);
        
        if (!fileExists(filename)) {
            throw new SerializationException("El archivo no existe: " + filename);
        }
        
        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {
            
            T object = factory.create();
            object.deserialize(dis);
            return object;
            
        } catch (IOException e) {
            throw new SerializationException("Error al cargar objeto desde archivo: " + filename, e);
        }
    }
    
    /**
     * Carga una lista de objetos serializables desde un archivo
     * 
     * @param filename Nombre del archivo a cargar
     * @param factory Factory para crear instancias del tipo de objeto
     * @return Lista de objetos cargados desde el archivo
     * @throws SerializationException Si hay error en la deserialización o carga
     * 
     * Proceso:
     * - Valida que la factory no sea null
     * - Si el archivo no existe, retorna lista vacía
     * - Lee el número de objetos del archivo
     * - Crea y deserializa cada objeto individualmente
     * 
     * Formato esperado del archivo:
     * - Entero: número de objetos en la lista
     * - Objeto 1 deserializado
     * - Objeto 2 deserializado
     * - ... (continúa para todos los objetos)
     * 
     * Manejo de errores:
     * - Valida factory null antes de procesar
     * - Retorna lista vacía si el archivo no existe
     * - Captura IOException y la convierte en SerializationException
     */
    @Override
    public <T extends Serializable> List<T> loadList(String filename, SerializableFactory<T> factory) throws SerializationException {
        if (factory == null) {
            throw new SerializationException("La factory no puede ser null");
        }
        
        String filePath = getFilePath(filename);
        
        if (!fileExists(filename)) {
            return new ArrayList<>(); // Retorna lista vacía si el archivo no existe
        }
        
        try (FileInputStream fis = new FileInputStream(filePath);
             DataInputStream dis = new DataInputStream(fis)) {
            
            int count = dis.readInt();
            List<T> objects = new ArrayList<>(count);
            
            for (int i = 0; i < count; i++) {
                T object = factory.create();
                object.deserialize(dis);
                objects.add(object);
            }
            
            return objects;
            
        } catch (IOException e) {
            throw new SerializationException("Error al cargar lista desde archivo: " + filename, e);
        }
    }
    
    /**
     * Verifica si un archivo existe en el directorio de datos
     * 
     * @param filename Nombre del archivo a verificar
     * @return true si el archivo existe, false en caso contrario
     * 
     * Notas:
     * - Utiliza la ruta completa del archivo
     * - Verifica existencia física del archivo en el sistema
     */
    @Override
    public boolean fileExists(String filename) {
        String filePath = getFilePath(filename);
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Elimina un archivo del directorio de datos
     * 
     * @param filename Nombre del archivo a eliminar
     * @return true si el archivo fue eliminado, false si no existía o hubo error
     * 
     * Proceso:
     * - Obtiene la ruta completa del archivo
     * - Elimina el archivo si existe
     * - Retorna true si la eliminación fue exitosa
     * 
     * Manejo de errores:
     * - Captura IOException y retorna false
     * - No lanza excepciones, solo retorna false en caso de error
     */
    @Override
    public boolean deleteFile(String filename) {
        try {
            String filePath = getFilePath(filename);
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Obtiene el tamaño de un archivo en bytes
     * 
     * @param filename Nombre del archivo
     * @return Tamaño del archivo en bytes, o -1 si hay error
     * 
     * Manejo de errores:
     * - Retorna -1 si no puede obtener el tamaño del archivo
     * - Útil para verificar si un archivo está vacío o corrupto
     */
    public long getFileSize(String filename) {
        try {
            String filePath = getFilePath(filename);
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return -1;
        }
    }
    
    /**
     * Lista todos los archivos en el directorio de datos
     * 
     * @return Lista de nombres de archivos en el directorio
     * 
     * Proceso:
     * - Lista todos los archivos regulares en el directorio
     * - Filtra solo archivos (no directorios)
     * - Retorna solo los nombres de archivos
     * 
     * Manejo de errores:
     * - Retorna lista vacía si hay error al listar archivos
     * - No lanza excepciones, solo retorna lista vacía
     */
    public List<String> listFiles() {
        List<String> files = new ArrayList<>();
        try {
            Files.list(Paths.get(dataDirectory))
                 .filter(Files::isRegularFile)
                 .forEach(path -> files.add(path.getFileName().toString()));
        } catch (IOException e) {
            // Retorna lista vacía en caso de error
        }
        return files;
    }
    
    /**
     * Crea una copia de respaldo de un archivo
     * 
     * @param filename Nombre del archivo a respaldar
     * @return true si el respaldo fue exitoso, false en caso contrario
     * 
     * Proceso:
     * - Verifica que el archivo original existe
     * - Crea una copia con sufijo .backup
     * - Utiliza Files.copy para la operación
     * 
     * Notas:
     * - El archivo de respaldo tendrá el nombre: filename.backup
     * - Si el archivo original no existe, retorna false
     * - No sobrescribe respaldos existentes
     */
    public boolean backupFile(String filename) {
        if (!fileExists(filename)) {
            return false;
        }
        
        try {
            String originalPath = getFilePath(filename);
            String backupPath = getFilePath(filename + ".backup");
            Files.copy(Paths.get(originalPath), Paths.get(backupPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Restaura un archivo desde su respaldo
     * 
     * @param filename Nombre del archivo a restaurar
     * @return true si la restauración fue exitosa, false en caso contrario
     * 
     * Proceso:
     * - Verifica que existe el archivo de respaldo
     * - Copia el archivo de respaldo sobre el original
     * - Utiliza Files.copy para la operación
     * 
     * Notas:
     * - Busca el archivo de respaldo con nombre: filename.backup
     * - Si el archivo de respaldo no existe, retorna false
     * - Sobrescribe el archivo original si existe
     */
    public boolean restoreFromBackup(String filename) {
        String backupFilename = filename + ".backup";
        if (!fileExists(backupFilename)) {
            return false;
        }
        
        try {
            String backupPath = getFilePath(backupFilename);
            String originalPath = getFilePath(filename);
            Files.copy(Paths.get(backupPath), Paths.get(originalPath));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Obtiene información detallada del directorio de datos
     * 
     * @return Cadena con información del directorio, número de archivos y tamaño total
     * 
     * Información incluida:
     * - Ruta del directorio de datos
     * - Número total de archivos
     * - Tamaño total en bytes de todos los archivos
     * 
     * Proceso:
     * - Lista todos los archivos en el directorio
     * - Calcula el tamaño de cada archivo
     * - Suma todos los tamaños para obtener el total
     * 
     * Notas:
     * - Excluye archivos con tamaño negativo (error al obtener tamaño)
     * - Formato de salida: "Data Directory: ruta\nFiles: número\nTotal Size: bytes bytes"
     */
    public String getDirectoryInfo() {
        List<String> files = listFiles();
        long totalSize = files.stream()
                             .mapToLong(this::getFileSize)
                             .filter(size -> size >= 0)
                             .sum();
        
        return String.format("Data Directory: %s%nFiles: %d%nTotal Size: %d bytes", 
                           dataDirectory, files.size(), totalSize);
    }
    
    /**
     * Genera una representación en cadena del gestor de datos
     * 
     * @return Cadena con información del directorio de datos
     * 
     * Formato:
     * - Nombre de la clase
     * - Directorio de datos configurado
     * 
     * Notas:
     * - Útil para debugging y logging
     * - Incluye el directorio de datos actual
     */
    @Override
    public String toString() {
        return "BinaryDataManager{dataDirectory='" + dataDirectory + "'}";
    }
}