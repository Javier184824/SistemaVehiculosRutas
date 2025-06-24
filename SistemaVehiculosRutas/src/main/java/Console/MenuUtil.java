/*
 * Nombre del Archivo: MenuUtil.java
 * 
 * Descripcion: Clase de utilidades para interfaces de consola. Proporciona métodos
 *              estáticos para la interacción con el usuario a través de consola,
 *              incluyendo menús, entrada de datos con validación, mensajes de
 *              estado y control de la interfaz de usuario. Facilita la creación
 *              de interfaces de consola consistentes y amigables.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Console;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utilidades para interfaces de consola
 * 
 * Esta clase proporciona métodos estáticos para facilitar la creación de
 * interfaces de consola interactivas y amigables al usuario:
 * - Generación de menús con formato consistente
 * - Entrada de datos con validación robusta
 * - Mensajes de estado con iconos visuales
 * - Control de la interfaz de usuario
 * 
 * Todos los métodos son estáticos para facilitar su uso sin instanciación.
 */
public class MenuUtil {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Muestra un menú y obtiene la selección del usuario
     * 
     * @param title El título del menú
     * @param options Las opciones del menú (varargs)
     * @return El índice de la opción seleccionada (base 0), o -1 para entrada inválida
     * 
     * Características:
     * - Formato visual consistente con separadores
     * - Opción "0" para salir/volver automáticamente
     * - Validación de entrada numérica
     * - Conversión automática de índice (base 1 a base 0)
     */
    public static int displayMenu(String title, String... options) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(title);
        System.out.println("=".repeat(50));
        
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.println("0. Exit/Back");
        System.out.println("-".repeat(50));
        
        return getIntInput("Select an option: ") - 1;
    }
    
    /**
     * Obtiene entrada de tipo entero del usuario con validación
     * 
     * @param prompt El mensaje de solicitud de entrada
     * @return El valor entero ingresado por el usuario
     * 
     * Validaciones:
     * - Maneja excepciones de tipo de entrada incorrecto
     * - Bucle infinito hasta obtener entrada válida
     * - Consume caracteres inválidos del buffer
     * - Mensaje de error informativo
     */
    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }
    
    /**
     * Obtiene entrada de tipo double del usuario con validación
     * 
     * @param prompt El mensaje de solicitud de entrada
     * @return El valor double ingresado por el usuario
     * 
     * Validaciones:
     * - Maneja excepciones de tipo de entrada incorrecto
     * - Bucle infinito hasta obtener entrada válida
     * - Consume caracteres inválidos del buffer
     * - Mensaje de error informativo
     */
    public static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }
    
    /**
     * Obtiene entrada de tipo String del usuario
     * 
     * @param prompt El mensaje de solicitud de entrada
     * @return La cadena ingresada por el usuario (sin espacios en blanco)
     * 
     * Características:
     * - Elimina espacios en blanco al inicio y final
     * - No valida si la entrada está vacía
     * - Consume la línea completa del buffer
     */
    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Obtiene entrada de tipo String no vacía del usuario
     * 
     * @param prompt El mensaje de solicitud de entrada
     * @return La cadena no vacía ingresada por el usuario
     * 
     * Validaciones:
     * - Bucle hasta obtener entrada no vacía
     * - Mensaje de error si la entrada está vacía
     * - Elimina espacios en blanco automáticamente
     */
    public static String getNonEmptyStringInput(String prompt) {
        String input;
        do {
            input = getStringInput(prompt);
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }
    
    /**
     * Obtiene confirmación sí/no del usuario
     * 
     * @param prompt El mensaje de confirmación
     * @return true para sí, false para no
     * 
     * Entradas válidas:
     * - Sí: "y", "yes" (case insensitive)
     * - No: "n", "no" (case insensitive)
     * - Bucle hasta obtener entrada válida
     */
    public static boolean getConfirmation(String prompt) {
        while (true) {
            String input = getStringInput(prompt + " (y/n): ").toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
    
    /**
     * Muestra un mensaje de error con formato visual
     * 
     * @param message El mensaje de error a mostrar
     * 
     * Características:
     * - Icono visual de error (❌)
     * - Formato consistente con otros mensajes
     * - Salto de línea para separación visual
     */
    public static void showError(String message) {
        System.out.println("\n❌ ERROR: " + message);
    }
    
    /**
     * Muestra un mensaje de éxito con formato visual
     * 
     * @param message El mensaje de éxito a mostrar
     * 
     * Características:
     * - Icono visual de éxito (✅)
     * - Formato consistente con otros mensajes
     * - Salto de línea para separación visual
     */
    public static void showSuccess(String message) {
        System.out.println("\n✅ SUCCESS: " + message);
    }
    
    /**
     * Muestra un mensaje informativo con formato visual
     * 
     * @param message El mensaje informativo a mostrar
     * 
     * Características:
     * - Icono visual de información (📋)
     * - Formato consistente con otros mensajes
     * - Salto de línea para separación visual
     */
    public static void showInfo(String message) {
        System.out.println("\n📋 INFO: " + message);
    }
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     * 
     * @param message El mensaje de pausa a mostrar
     * 
     * Características:
     * - Permite al usuario leer información antes de continuar
     * - No requiere entrada específica, solo Enter
     * - Útil para controlar el flujo de la aplicación
     */
    public static void pause(String message) {
        System.out.print("\n" + message + " (Press Enter to continue...)");
        scanner.nextLine();
    }
    
    /**
     * Limpia la consola para mejorar la legibilidad
     * 
     * Características:
     * - Detecta automáticamente el sistema operativo
     * - Usa comandos específicos para Windows y Unix/Linux
     * - Fallback a impresión de líneas vacías si falla
     * - Maneja excepciones de forma silenciosa
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // Fallback: print empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
