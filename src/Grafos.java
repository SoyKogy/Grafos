import javax.swing.JOptionPane;

public class Grafos {

    private String[] nodos;
    private int[][] matrizAdyacencia;
    private int[][] matrizIncidencia;
    private Nodo puntaListaAdyacencia;

    // constructor

    public Grafos(String[] nodos) {
        this.nodos = nodos;
        this.matrizAdyacencia = new int[nodos.length][nodos.length];
        // this.matrizIncidencia = 
        this.puntaListaAdyacencia = null;
    }

    // getters y setters

    public String[] getNodos() {
        return nodos;
    }

    public void setNodos(String[] nodos) {
        this.nodos = nodos;
    }

    public int[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    public void setMatrizAdyacencia(int[][] matrizAdyacencia) {
        this.matrizAdyacencia = matrizAdyacencia;
    }

    public void setMatrizAdyacencia(int i, int j, int valor) {
        this.matrizAdyacencia[i][j] = valor;
    }

    // métodos

    public static Grafos crearGrafo(String[] nodos, int tipo) {

        Grafos grafo = new Grafos(nodos);
        /* tipo 1 = grafo simple no dirigido */

        String[] conexiones;

        if (tipo == 1) {

            
            for (int i = 0; i < nodos.length; i++) {

                boolean entradaValida = false;
                String conexionesString;

                do {
                    conexionesString = JOptionPane.showInputDialog(grafo.mostrarConexionesDeNodo(i, 1) + "\n\nIngrese con qué nodos estará conectado el nodo \""
                                                                    + nodos[i] + "\", separados por comas.\n\n");
                    
                    conexiones = conexionesString.split(",");
                    entradaValida = true;

                    if (conexionesString.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Error: Debe ingresar al menos un nodo.");
                        entradaValida = false;
                    }

                    for (int j = 0; j < conexiones.length && entradaValida; j++) {
                        if (conexiones[j].isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: Hay comas sin nodo entre ellas. Ingrese solo nodos válidos.");
                            entradaValida = false;
                        }
                    }

                    for (int j = 0; j < conexiones.length && entradaValida; j++) {
                        if (conexiones[j].equals(nodos[i])) {
                            JOptionPane.showMessageDialog(null, "Error: El nodo \"" + nodos[i] + "\" no puede conectarse consigo mismo.");
                            entradaValida = false;
                        }
                    }

                    if (entradaValida && MenuGrafos.verificarNodosRepetidos(conexiones)) {
                        entradaValida = false;
                    }

                    if (entradaValida) {
                        for (int j = 0; j < conexiones.length && entradaValida; j++) {
                            for (int k = 0; k < nodos.length && entradaValida; k++) {
                                if (conexiones[j].equals(nodos[k]) && grafo.getMatrizAdyacencia()[i][k] == 1) {
                                    JOptionPane.showMessageDialog(null, "Error: Ya existe una arista entre \"" + nodos[i] + "\" y \"" + nodos[k] + "\".");
                                    entradaValida = false;
                                }
                            }
                        }
                    }

                    if (entradaValida) {
                        entradaValida = verificarSiTodosLosNodosExisten(conexiones, nodos);
                    }

                } while (entradaValida == false);

                for (int j = 0; j < conexiones.length; j++) {
                    for (int k = 0; k < nodos.length; k++) {
                        if (conexiones[j].equals(nodos[k])) {
                            grafo.setMatrizAdyacencia(i, k, 1);
                            grafo.setMatrizAdyacencia(k, i, 1);
                        }
                    }
                }
                
            }
            
        
        /* tipo 2 = multigrafo dirigido */
        } else if (tipo == 2) {
            System.out.println("Has seleccionado la opción 2: Crear un grafo dirigido");
            // Aquí puedes agregar el código para crear un grafo dirigido
        }

        return grafo;
    }

    public String mostrarConexionesDeNodo(int indiceNodo, int tipo) {

        StringBuilder conexiones = new StringBuilder();
        boolean huboConexiones = false;

        /* tipo 1 = grafo simple no dirigido */
        if (tipo == 1) {
            conexiones.append("El nodo " + nodos[indiceNodo] + " tiene aristas con: ");
            int[][] ady = this.getMatrizAdyacencia(); // Obtener la matriz de adyacencia del grafo

            for (int i = 0; i < nodos.length; i++) {

                if (ady[indiceNodo][i] == 1) { // Si hay una arista entre el nodo actual y el nodo i
                        conexiones.append(nodos[i]).append(", ");
                        huboConexiones = true;
                }
            }

        /* tipo 2 = multigrafo dirigido */
        } else if (tipo == 2) {
            int[][] ady = this.getMatrizAdyacencia(); // Obtener la matriz de adyacencia del grafo
            conexiones.append("El nodo " + nodos[indiceNodo] + " tiene estas aristas:");

            StringBuilder entradas = new StringBuilder("\nEntrantes: con los nodos ");
            StringBuilder salidas = new StringBuilder("\nSalientes: con los nodos ");

            boolean huboEntradas = false;
            boolean huboSalidas = false;

            for (int i = 0; i < nodos.length; i++) {

                if (ady[indiceNodo][i] == 1) { // Si hay una arista dirigida desde el nodo actual hacia el nodo i
                        salidas.append(nodos[i]).append(", ");
                        huboConexiones = true;
                        huboSalidas = true;
                }

                if (ady[i][indiceNodo] == 1) { // Si hay una arista dirigida desde el nodo i hacia el nodo actual
                        entradas.append(nodos[i]).append(", ");
                        huboConexiones = true;
                        huboEntradas = true;
                }
            }

            if (!huboEntradas) {
                entradas.replace(entradas.indexOf(":"), entradas.length(), "Ninguno");
            }
            if (!huboSalidas) {
                salidas.replace(salidas.indexOf(":"), salidas.length(), "Ninguno");
            }

            conexiones.append(entradas).append(salidas);

        }

        if (!huboConexiones) {
            conexiones.setLength(0);
            conexiones.append("El nodo ").append(nodos[indiceNodo]).append(" no tiene conexiones.");
        }

        return conexiones.toString();
    }

    public boolean verificarNodosRepetidos() {

        boolean repetidos = false;

        for (int i = 0; i < nodos.length && !repetidos; i++) {
            for (int j = 0; j < nodos.length && !repetidos; j++) {

                if (i == j) {
                    j++;
                }
                if (j < nodos.length && nodos[i].equals(nodos[j])) {
                    JOptionPane.showMessageDialog(null, "Error: No pueden haber nodos repetidos.");
                    
                    repetidos = true;
                    j = nodos.length;
                }
            }
        }

        return repetidos;
    }

    public static boolean verificarSiTodosLosNodosExisten(String[] conexiones, String[] nodos) {

        boolean todosExisten = true;

        // verificacion de que los nodos existan
        for (int j = 0; j < conexiones.length && todosExisten; j++) {
            
            boolean nodoExiste = false;

            // este ciclo se rompe cuando se encuentra el nodo
            for (int k = 0; k < nodos.length && !nodoExiste; k++) {

                if (conexiones[j].equals(nodos[k])) {                    
                    nodoExiste = true;
                }
            }

            // si no se encontró el nodo entre los nodos del grafo
            if (!nodoExiste) {
                JOptionPane.showMessageDialog(null, "Error: El nodo \"" + conexiones[j] + "\" no existe. Por favor, ingrese solo nodos válidos.");
                todosExisten = false;
            }

        }

        return todosExisten;
    }
}
