import java.util.*;
import java.util.concurrent.*;


/**********************************************************************************************************************************************
 *   APLICACIÓN: "MultiplicaLista"                                                                                                            *
 **********************************************************************************************************************************************
 *   PROGRAMACIÓN DE SERVICIOS Y PROCESOS 2DAM  -  Eclipse IDE for Java Developers v2021-09 (4.21.0)                                          *
 **********************************************************************************************************************************************
 *   @author S.García                                                                                                                        *
 *   @version 3.0 - Eliminado estatismo.                                                                                                      *
 *            2.0 - Mejoras en la codificación y comentarios.                                                                                 *
 *            1.0 - Versión inicial del algoritmo.                                                                                            *
 *   @since 07OCT2024                                                                                                                       *
 *            27OCT2023                                                                                                                       *
 *            06OCT2021                                                                                                                       *
 **********************************************************************************************************************************************
 *   COMENTARIOS:                                                                                                                             *
 *        - Ejemplo básico de ThreadPoolExecutor, Executors y Callable().                                                                     *
 *        - Contraviniendo las buenas prácticas de codificación, se emplea una clase anidada por ser mejor así el orden del código            *
 *          al explicar el ejemplo en case. En programación "real" es preferible declararla en fichero aparte o tras la clase principal.      *
 **********************************************************************************************************************************************/
class MultiplicaLista {

    public static final int NUMERO_HILOS = 3;
    public static final int NUMERO_MULTIPLICACIONES = 10;

    private class Multiplicacion implements Callable<Integer> {

        private int a_Operando1;
        private int a_Operando2;

        // Constructor que asigna los valores pasados por parámetro a los atributos de la clase.
        public Multiplicacion(int p_Operando1, int p_Operando2) {
            this.a_Operando1 = p_Operando1;
            this.a_Operando2 = p_Operando2;
        }   // Multiplicacion()

        // Sobrecarga del método call() por usar la interfaz Callable. Es aquí donde se realiza el trabajo de la tarea.
        @Override
        public Integer call() throws Exception {
            return (a_Operando1 * a_Operando2);
        }   // call()

    }   // Multiplicacion.


    public static void main(String[] args) {

        MultiplicaLista l_Aplicacion = new MultiplicaLista();
        ThreadPoolExecutor l_Executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMERO_HILOS);
        Multiplicacion l_Tarea = null;
        List<Multiplicacion> l_ListaTareas = new ArrayList<Multiplicacion>();
        List<Future<Integer>> l_ListaResultados = null;
        Future<Integer> l_Resultado = null;
        int l_Contador = 0;

        // Creamos las tareas (objetos de clase Multiplicación) y las añadimos a la colección.
        for (l_Contador = 0; l_Contador < NUMERO_MULTIPLICACIONES; l_Contador++) {
            l_Tarea = l_Aplicacion.new Multiplicacion((int) (Math.random() * 10), (int) (Math.random() * 10));
            l_ListaTareas.add(l_Tarea);
        }

        try {
            // Le decimos al gestor de tareas que las tareas de la colección ya son elegibles, recogemos los resultados en otra colección, y apagamos el ejecutor.
            l_ListaResultados = l_Executor.invokeAll(l_ListaTareas);
            l_Executor.shutdown();

            // Recuperamos uno a uno los resultados de cada tarea acabada correctamente y los mostramos.
            for (l_Contador = 0; l_Contador < l_ListaResultados.size(); l_Contador++) {
                l_Resultado = l_ListaResultados.get(l_Contador);
                try {
                    // Debido a que l_Resultado es un Future, recuperamos su valor con get() para asegurarnos de que éste ya existe.
                    System.out.println("El resultado de la tarea " + (l_Contador + 1) + " es: " + l_Resultado.get());
                } catch (InterruptedException | ExecutionException l_Excepcion) {
                    System.out.println("ERROR: Ha fallado un get() con error: " + l_Excepcion);
                }
            }

        } catch (InterruptedException l_Excepcion) {
            System.out.println("ERROR: Ha fallado el invoke() con error: " + l_Excepcion);
        }

    }   // main()

}   // MultiplicaLista