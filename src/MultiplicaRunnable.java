import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiplicaRunnable {

    public static final int NUMERO_HILOS = 3;
    public static final int NUMERO_MULTIPLICACIONES = 10;
    public static List<Integer> a_ListaResultados = new ArrayList<Integer>();

    class Multiplicacion2 implements Runnable {
        private int a_Operando1;
        private int a_Operando2;

        public Multiplicacion2(int p_Operando1, int p_Operando2) {
            this.a_Operando1 = p_Operando1;
            this.a_Operando2 = p_Operando2;
        }

        @Override
        public void run() {
            a_ListaResultados.add(a_Operando1 * a_Operando2);
        }
    }

    public static void main(String[] args) {

        MultiplicaRunnable l_Aplicacion = new MultiplicaRunnable();
        ThreadPoolExecutor l_Executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMERO_HILOS);
        Multiplicacion2 l_Tarea = null;
        List<Callable<Object>> l_ListaTareas = new ArrayList<Callable<Object>>();
        Integer l_Resultado = null;
        int l_Contador = 0;

        // Creamos las tareas (objetos de clase Multiplicación) y las añadimos a la colección.
        for (l_Contador = 0; l_Contador < NUMERO_MULTIPLICACIONES; l_Contador++) {
            l_Tarea = l_Aplicacion.new Multiplicacion2((int) (Math.random() * 10), (int) (Math.random() * 10));
            l_ListaTareas.add(Executors.callable(l_Tarea));
        }

        try {
            // Le decimos al gestor de tareas que las tareas de la colección ya son elegibles, recogemos los resultados en otra colección, y apagamos el ejecutor.
            l_Executor.invokeAll(l_ListaTareas);
            l_Executor.shutdown();

            // Recuperamos uno a uno los resultados de cada tarea acabada correctamente y los mostramos.
            for (l_Contador = 0; l_Contador < a_ListaResultados.size(); l_Contador++) {
                l_Resultado = a_ListaResultados.get(l_Contador);

                // Debido a que l_Resultado es un Future, recuperamos su valor con get() para asegurarnos de que éste ya existe.
                System.out.println("El resultado de la tarea " + (l_Contador + 1) + " es: " + l_Resultado);

            }

        } catch (InterruptedException l_Excepcion) {
            System.out.println("ERROR: Ha fallado el invoke() con error: " + l_Excepcion);
        }

    }
}
