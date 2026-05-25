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

    public static Grafos crearGrafo(String nodosString, int tipo) {
        String[] nodos = nodosString.split(",");

        Grafos grafo = new Grafos(nodos);
        /* tipo 1 = grafo simple no dirigido */

        String[] conexiones;

        if (tipo == 1) {

            
            for (int i = 0; i < nodos.length; i++) {

                boolean nodoExiste = false;
                String conexionesString;

                do {
                    conexionesString = JOptionPane.showInputDialog(grafo.mostrarConexionesDeNodo(i, 1) + "\nIngrese con qué nodos estará conectado el nodo \""
                                                                    + nodos[i] + "\", separados por comas.\n\n");
                    
                    conexiones = conexionesString.split(",");

                    // verificacion de que los nodos existan
                    for (int j = 0; j < conexiones.length || !nodoExiste; j++) {
                        
                        for (int k = 0; k < nodos.length || nodoExiste; k++) {
                            if (conexiones[j].equals(nodos[k])) {
                                nodoExiste = true;
                            }
                        }

                        if (!nodoExiste) {
                            JOptionPane.showMessageDialog(null, "El nodo \"" + conexiones[j] + "\" no existe. Ingrese un nodo válido.");
                        }
                    }
                } while (nodoExiste == false);
                

                
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
}
