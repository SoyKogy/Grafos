import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Queue;
import java.util.LinkedList;

import org.graphper.api.Graphviz;
import org.graphper.api.Node;
import org.graphper.api.attributes.NodeShapeEnum;
import org.graphper.api.attributes.Color;
import org.graphper.api.FileType;

public class Grafos {

    private String[] nodos;
    private int[][] pesos;
    private int[][] matrizAdyacencia;
    private int[][] matrizIncidencia;
    private Nodo[] puntaListaAdy;
    private int tipo; // 1 = grafo simple no dirigido, 2 = grafo simple dirigido
    
    private JFrame ventana;
    private JLabel etiqueta;
    private Graphviz visualGrafo;

    // constructor

    public Grafos(String[] nodos, int tipo) {
        this.nodos = nodos;
        this.pesos = new int[nodos.length][nodos.length];
        this.matrizAdyacencia = new int[nodos.length][nodos.length];
        this.matrizIncidencia = null;
        this.puntaListaAdy = null;
        this.tipo = tipo;
        this.etiqueta = new JLabel();
        this.visualGrafo = null;
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

    public int[][] getMatrizIncidencia() {
        return matrizIncidencia;
    }

    public void setMatrizIncidencia(int[][] matrizIncidencia) {
        this.matrizIncidencia = matrizIncidencia;
    }

    public void setPuntaListAdy(Nodo[] puntaListaAdy) {
        this.puntaListaAdy = puntaListaAdy;
    }

    public Nodo[] getPuntaListaAdy() {
        return puntaListaAdy;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public JFrame getVentana() {
        return ventana;
    }
    

    // métodos

    public static Grafos crearGrafo(String[] nodos, int tipo) throws Exception {

        Grafos grafo = new Grafos(nodos, tipo);
        /* tipo 1 = grafo simple no dirigido */

        String[] conexiones;

        if (tipo == 1) {

            // pedir datos y llenar adyacencia en cada iteración             
            for (int i = 0; i < nodos.length; i++) {

                boolean entradaValida = false; 
                /*
                entradaValida verifica, en este orden:

                    validaciones de formato:
                    1. si no se ingresa nada -> false
                    2. si se ingresan comas sin nodos entre ellas -> false

                    validaciones de logica (segun teoría de grafos simples no dirigidos):
                    3. si el nodo se conecta consigo mismo -> false
                    4. si se ingresan nodos repetidos -> false
                    5. si ya existe una arista entre el nodo actual y alguno de los nodos ingresados -> false
                    
                    adicional:
                    6. si se ingresan nodos que no existen en el grafo -> false
                    */
                
                boolean tieneTodas = true;  
                String conexionesString;

                for (int j = 0; j < nodos.length && tieneTodas == true; j++) {
                    if (i != j) {
                        if (grafo.getMatrizAdyacencia()[i][j] != 1) {
                            tieneTodas = false;
                        }
                    }
                }

                if (!tieneTodas) {
                    do {
                        conexionesString = JOptionPane.showInputDialog(grafo.mostrarConexionesDeNodo(i) + "\n\nIngrese con qué nodos estará conectado el nodo \""
                                                                        + nodos[i] + "\", separados por comas.\n\n");
                        /* no se maneja NullPointerException por mero capricho mio
                         */
                        conexiones = conexionesString.split(",");
                        entradaValida = true;

                        /*
                        
                        Validaciones
                        */

                        // si no se ingresa nada
                        if (!conexionesString.isEmpty()) {
                            // si se ingresan comas sin nodos entre ellas
                            for (int j = 0; j < conexiones.length && entradaValida; j++) {
                                if (conexiones[j].isEmpty() || conexiones[j].isBlank()) {
                                    JOptionPane.showMessageDialog(null, "Error: Hay comas sin nodo entre ellas. Ingrese solo nodos válidos.");
                                    entradaValida = false;
                                }
                            }

                            // si el nodo se conecta consigo mismo
                            for (int j = 0; j < conexiones.length && entradaValida; j++) {
                                if (conexiones[j].equals(nodos[i])) {
                                    JOptionPane.showMessageDialog(null, "Error: El nodo \"" + nodos[i] + "\" no puede conectarse consigo mismo.");
                                    entradaValida = false;
                                }
                            }

                            // si se ingresan nodos repetidos
                            if (entradaValida && MenuGrafos.verificarNodosRepetidos(conexiones)) {
                                entradaValida = false;
                            }

                            if (entradaValida) {

                                // si ya existe una arista entre el nodo actual y alguno de los nodos ingresados
                                for (int j = 0; j < conexiones.length && entradaValida; j++) {
                                    for (int k = 0; k < nodos.length && entradaValida; k++) {
                                        if (conexiones[j].equals(nodos[k]) && grafo.getMatrizAdyacencia()[i][k] == 1) {
                                            JOptionPane.showMessageDialog(null, "Error: Ya existe una arista entre \"" + nodos[i] + "\" y \"" + nodos[k] + "\".");
                                            entradaValida = false;
                                        }
                                    }
                                }
                            }

                            // si se ingresan nodos que no existen en el grafo
                            if (entradaValida) {
                                entradaValida = verificarSiTodosLosNodosExisten(conexiones, nodos);
                            }
                        }

                    } while (entradaValida == false);

                    // si pasaron todas las validaciones, se llena la matriz de adyacencia

                    if (!conexionesString.isEmpty()) {
                        for (int j = 0; j < conexiones.length; j++) {
                            for (int k = 0; k < nodos.length; k++) {
                                if (conexiones[j].equals(nodos[k])) { // si se encuentra el nodo ingresado entre los nodos del grafo
                                    grafo.setMatrizAdyacencia(i, k, 1);
                                    grafo.setMatrizAdyacencia(k, i, 1);
                                }
                            }
                        }
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "El nodo \"" + nodos[i] + "\" ya tiene conexiones con todos los demás nodos.\nSe procederá al siguiente nodo.");
                }
                
            }
            
            // crear la matriz de incidencia
            grafo.setMatrizIncidencia(grafo.updateMatrizInc());
            
            // llenar lista de adyacencia
            grafo.llenarListaAdy();

        /* tipo 2 = grafo simple dirigido */
        }
        
        if (tipo == 2) {
            // pedir datos y llenar adyacencia en cada iteración             
            for (int i = 0; i < nodos.length; i++) {

                boolean entradaValida = false; 
                /*
                entradaValida verifica, en este orden:

                    validaciones de formato:
                    1. si no se ingresa nada -> false
                    2. si se ingresan comas sin nodos entre ellas -> false

                    validaciones de logica (segun teoría de grafos simples no dirigidos):
                    3. si el nodo se conecta consigo mismo (no es necesario)
                    4. si se ingresan nodos repetidos -> false
                    5. si ya existe una arista SALIENTE entre el nodo actual y alguno de los nodos ingresados -> false
                    
                    adicional:
                    6. si se ingresan nodos que no existen en el grafo -> false
                    */
                
                boolean tieneTodas = true;  
                String conexionesString;

                for (int j = 0; j < nodos.length && tieneTodas == true; j++) {
                    if (grafo.getMatrizAdyacencia()[i][j] != 1) {
                            tieneTodas = false;
                    }
                }

                if (!tieneTodas) {
                    do {
                        conexionesString = JOptionPane.showInputDialog(grafo.mostrarConexionesDeNodo(i) + "\n\nIngrese con qué nodos estará conectado el nodo \""
                                                                        + nodos[i] + "\", separados por comas.\n\n");
                        
                        conexiones = conexionesString.split(",");
                        entradaValida = true;

                        /*
                        
                        Validaciones
                        */

                        // si no se ingresa nada
                        if (!conexionesString.isEmpty()) {
                            // si se ingresan comas sin nodos entre ellas
                            for (int j = 0; j < conexiones.length && entradaValida; j++) {
                                if (conexiones[j].isEmpty() || conexiones[j].isBlank()) {
                                    JOptionPane.showMessageDialog(null, "Error: Hay comas sin nodo entre ellas. Ingrese solo nodos válidos.");
                                    entradaValida = false;
                                }
                            }

                            // si el nodo se conecta consigo mismo
                            /*
                            for (int j = 0; j < conexiones.length && entradaValida; j++) {
                                if (conexiones[j].equals(nodos[i])) {
                                    JOptionPane.showMessageDialog(null, "Error: El nodo \"" + nodos[i] + "\" no puede conectarse consigo mismo.");
                                    entradaValida = false;
                                }
                            }
                            */

                            // si se ingresan nodos repetidos
                            if (entradaValida && MenuGrafos.verificarNodosRepetidos(conexiones)) {
                                entradaValida = false;
                            }

                            if (entradaValida) {

                                // si ya existe una arista SALIENTE entre el nodo actual y alguno de los nodos ingresados
                                for (int j = 0; j < conexiones.length && entradaValida; j++) {
                                    for (int k = 0; k < nodos.length && entradaValida; k++) {
                                        if (conexiones[j].equals(nodos[k]) && grafo.getMatrizAdyacencia()[i][k] == 1) {
                                            JOptionPane.showMessageDialog(null, "Error: Ya existe una arista desde \"" + nodos[i] + "\" hacia \"" + nodos[k] + "\".");
                                            entradaValida = false;
                                        }
                                    }
                                }
                            }

                            // si se ingresan nodos que no existen en el grafo
                            if (entradaValida) {
                                entradaValida = verificarSiTodosLosNodosExisten(conexiones, nodos);
                            }

                        }

                        
                    } while (entradaValida == false);

                    // si pasaron todas las validaciones, se llena la matriz de adyacencia
                    
                    if (!conexionesString.isEmpty()) {
                        
                        for (int j = 0; j < conexiones.length; j++) {
                            for (int k = 0; k < nodos.length; k++) {
                                if (conexiones[j].equals(nodos[k])) { // si se encuentra el nodo ingresado entre los nodos del grafo
                                    grafo.setMatrizAdyacencia(i, k, 1);
                                }
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El nodo \"" + nodos[i] + "\" ya tiene salientes a cada nodo posible.\nSe procederá al siguiente nodo.");
                }
                
            }
            
            // crear la matriz de incidencia
            grafo.setMatrizIncidencia(grafo.updateMatrizInc());
            
            // llenar lista de adyacencia
            grafo.llenarListaAdy();
        }

        return grafo;
    }

    public int[][] updateMatrizInc() throws Exception {

        int aristas = this.contarAristas();

        int[][] nuevaMatrizInc = new int[nodos.length][aristas];

        if (this.tipo == 1) {
            for (int i = 0, k = 0; i < nodos.length; i++) {    // filas de mat adyacencia
                for (int j = i + 1; j < nodos.length; j++) {// colms de mat adyacencia & colms de mat incidencia
                    
                    if (k < nuevaMatrizInc[0].length && this.getMatrizAdyacencia()[i][j] == 1) {
                            
                            nuevaMatrizInc[i][k] = 1;
                            nuevaMatrizInc[j][k] = 1;
                            k++; // si se encontró una conexion, se le asigna una columna en la matriz
                                // y aumenta k 
                    } 
                }
            }
        }

        if (this.tipo == 2) {
            for (int i = 0, k = 0; i < nodos.length; i++) {    // filas de mat adyacencia
                for (int j = 0; j < nodos.length; j++) {// colms de mat adyacencia & colms de mat incidencia
                    
                    if (k < nuevaMatrizInc[0].length && this.getMatrizAdyacencia()[i][j] == 1) {
                        
                        if (i == j) {
                            nuevaMatrizInc[i][k] = 3;
                            k++;
                        } else {
                            nuevaMatrizInc[i][k] = 1;
                            nuevaMatrizInc[j][k] = 2;
                            k++; // si se encontró una conexion, se le asigna una columna en la matriz
                                // y aumenta k 
                        }
                            
                    } 
                }
            }
        }

        this.mostrarGrafo(false); // refresca solo si ya existe una versión visual del grafo

        return nuevaMatrizInc;
    }

    public void llenarListaAdy() throws Exception {
        
        // solo se pidió lista de adyacencia para grafo no dirigido
        // entonces no se pide tipo 
        // NO se implementará para tipo = 2.

        Nodo[] listaAdy = new Nodo[nodos.length];

        for (int i = 0; i < nodos.length; i++) { // itera sobre los nodos del grafo

            Nodo nodoActual = null; // se crea un nodo para el nodo actual,
                                    // pero no se asigna a la lista de adyacencia
                                    // hasta que se encuentre el primer nodo adyacente
            int aristasNodo = contarAristasDeNodo(i);

            if (aristasNodo > 0) {

                for (int j = 0; j < nodos.length; j++) {
                
                    if (this.getMatrizAdyacencia()[i][j] == 1) {

                        // si el nodo actual es el primer nodo adyacente encontrado
                        if (nodoActual == null) {
                            nodoActual = new Nodo(nodos[j]); // se asigna el primer nodo adyacente al nodo actual
                            listaAdy[i] = nodoActual; // se asigna el nodo adyacente a la posición del nodo actual en la lista de adyacencia
                        
                        
                        // sino, si ya había un nodo antes del actual
                        } else {
                            Nodo nodoAdy = new Nodo(nodos[j]);
                            nodoActual.setLiga(nodoAdy);

                            nodoActual = nodoAdy;
                        }
                        
                    }
                }
                
            }
        }
        
        this.setPuntaListAdy(listaAdy);
        
        this.mostrarGrafo(false); // refresca solo si ya existe una versión visual del grafo
    }

    public void DFS() {  

        boolean[] visitados = new boolean[nodos.length]; // arreglo para marcar los nodos ya visitados
        StringBuilder resultado = new StringBuilder(); // guarda el recorrido DFS en orden de visita
        
        // recorrer todos los nodos, por si el grafo no es conexo
        for (int i = 0; i < nodos.length; i++) {
            if (!visitados[i]) { // si el nodo actual aún no ha sido visitado
                DFSrecursivo(visitados, i, resultado); // iniciar DFS desde ese nodo
            }
        }
        
        JOptionPane.showMessageDialog(null, "Resultado: " + resultado);

    }

    public void DFSrecursivo(boolean[] visitado, int indiceNodoActual, StringBuilder res) {
        visitado[indiceNodoActual] = true; // marcar como visitado el nodo actual

        if (!res.isEmpty()) { // si ya hay nodos en el recorrido
            res.append(", "); // separar con coma el siguiente nodo
        }
        res.append(nodos[indiceNodoActual]); // agregar el nodo actual al recorrido

        // visitar recursivamente los nodos que aún no han sido visitados

        // para cada columna de la fila del nodo actual
        for (int j = 0; j < this.matrizAdyacencia[0].length; j++) {
            if (this.matrizAdyacencia[indiceNodoActual][j] == 1 && !visitado[j]) { // si hay arista y el nodo no ha sido visitado
                DFSrecursivo(visitado, j, res); // visitar recursivamente el nodo adyacente
            }
        }
    }

    public void BFS() {

        boolean[] visitados = new boolean[nodos.length]; // arreglo para marcar los nodos ya visitados
        StringBuilder resultado = new StringBuilder(); // guarda el recorrido BFS en orden de visita

        // recorrer todos los nodos, por si el grafo no es conexo
        for (int j = 0; j < nodos.length; j++) {
            if (!visitados[j]) { // si el nodo actual aún no ha sido visitado
                // entonces se inicia BFS desde ese nodo.

                // j es el indice del nodo inicial en esta iteración.

                Queue<Integer> cola = new LinkedList<>(); // cola para recorrer el grafo por niveles
                visitados[j] = true; // marcar como visitado el nodo inicial
                cola.add(j); // agregar el nodo inicial a la cola

                while (!cola.isEmpty()) {
                    int actual = cola.poll();

                    if (!resultado.isEmpty()) { // si ya hay nodos en el recorrido
                        resultado.append(", "); // separar con coma el siguiente nodo
                    }
                    resultado.append(nodos[actual]); // agregar el nodo actual al recorrido

                    // visitar los nodos adyacentes no visitados del nodo actual
                    for (int i = 0; i < this.matrizAdyacencia[0].length; i++) {
                        if (this.matrizAdyacencia[actual][i] == 1 && !visitados[i]) { // si hay arista y el nodo no ha sido visitado
                            visitados[i] = true; // marcar como visitado el nodo adyacente
                            cola.add(i); // agregar el nodo adyacente a la cola
                        }
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Resultado: " + resultado); // mostrar el recorrido final
    }

    public void insertarNodo() throws Exception {

        boolean nodoValido = true;
        String nuevoNodo = "";
        do {
            nodoValido = true;
            nuevoNodo = JOptionPane.showInputDialog("Ingrese el nombre del nuevo nodo:\n\nIngrese 0 para regresar.");
            
            // si no se ingresó nada
            if (nuevoNodo.isEmpty() || nuevoNodo.isBlank()) {
                JOptionPane.showMessageDialog(null, "Error: debe ingresar un nodo.");
                nodoValido = false;
            }

            // si el nodo ingresado ya existe
            if (nodoYaExiste(nuevoNodo) == true && nodoValido == true) {
                JOptionPane.showMessageDialog(null, "Error: "+nuevoNodo+" ya existe en el grafo.");
                nodoValido = false;
            }


        } while (nodoValido == false);

        if (!nuevoNodo.equals("0")) {


            boolean salientesValidos = true;
            String salientes;
            String[] nuevosSalientes = new String[0]; 

            do {
                salientesValidos = true;
                salientes = JOptionPane.showInputDialog("Ingrese a qué otros nodos conectar aristas, separados por comas.\n\nPuede dejar en blanco para dejar el nodo aislado.");
                
                if (!salientes.isEmpty() && !salientes.isBlank()) {
                    nuevosSalientes = salientes.split(",");

                    // si se ingresan comas sin nodos entre ellas
                    for (int j = 0; j < nuevosSalientes.length && salientesValidos; j++) {
                        if (nuevosSalientes[j].isEmpty() || nuevosSalientes[j].isBlank()) {
                            JOptionPane.showMessageDialog(null, "Error: Hay comas sin nodo entre ellas. Ingrese solo nodos válidos.");
                            salientesValidos = false;
                        }
                    }

                    // si se ingresan nodos repetidos
                    if (salientesValidos && MenuGrafos.verificarNodosRepetidos(nuevosSalientes)) {
                        salientesValidos = false;
                    }

                    // si es un grafo no dirigido, y se ingresó a sí mismo como arista saliente
                    for (int j = 0; j < nuevosSalientes.length && salientesValidos && tipo == 1; j++) {
                        if (nuevosSalientes[j].equals(nuevoNodo)) {
                            JOptionPane.showMessageDialog(null, "Error: No puede ingresar al mismo nodo para un grafo no dirigido.");
                            salientesValidos = false;
                        }
                    }

                    // si se ingresan nodos que no existen en el grafo
                    if (salientesValidos) {
                        salientesValidos = verificarSiTodosLosNodosExisten(nuevosSalientes, nodos);
                    }
                }
            } while (salientesValidos == false);
            
            
            
            int[][] nuevaMatAdy = new int[matrizAdyacencia.length + 1][matrizAdyacencia.length + 1];
            int[][] nuevosPesos = new int[pesos.length + 1][pesos.length + 1];
            String[] nuevoNodos = new String[nodos.length + 1];

            nuevoNodos[nuevoNodos.length - 1] = nuevoNodo;
            // llenar nueva matriz con datos viejos
            for (int i = 0; i < matrizAdyacencia.length; i++) {
                for (int j = 0; j < matrizAdyacencia[0].length; j++) {
                    if (matrizAdyacencia[i][j] != 0) {
                        nuevaMatAdy[i][j] = matrizAdyacencia[i][j];
                    }
                    if (pesos[i][j] != 0) {
                        nuevosPesos[i][j] = pesos[i][j];
                    }
                }
            }

            
            // añadirle a la mat ady los nuevos valores
            if (!salientes.isEmpty() && !salientes.isBlank()) {
                for (int i = 0; i < nuevosSalientes.length; i++) {
                    for (int j = 0; j < nodos.length; j++) {
                        if (nuevosSalientes[i].equals(nodos[j])) { // si se encuentra el nodo ingresado entre los nodos del grafo
                            nuevaMatAdy[nuevaMatAdy.length - 1][j] = 1;

                            if (tipo == 1) {
                                nuevaMatAdy[j][nuevaMatAdy.length - 1] = 1;
                            }
                            
                        }
                    }
                }
            }

            // llenar el nuevo arreglo de índice de nodos
            for (int i = 0; i < nodos.length; i++) {
                nuevoNodos[i] = nodos[i];
            }
            nodos = nuevoNodos;
            pesos = nuevosPesos;
            setMatrizAdyacencia(nuevaMatAdy);
            setMatrizIncidencia(updateMatrizInc());

            if (tipo != 2) {
                llenarListaAdy();
            }
            
            mostrarGrafo(true);

        }
    }

    public void eliminarNodo() throws Exception {

        // no se permite dejar el grafo sin nodos
        if (nodos.length == 1) {
            JOptionPane.showMessageDialog(null, "Error: No se puede eliminar el último nodo del grafo.");
            return;
        }

        boolean nodoValido = true;
        String nodoAEliminar = "";
        StringBuilder nodosDisponibles = new StringBuilder("Nodos disponibles:\n");

        for (int i = 0; i < nodos.length; i++) {
            nodosDisponibles.append(nodos[i]).append("\n");
        }

        do {
            nodoValido = true;
            nodoAEliminar = JOptionPane.showInputDialog(nodosDisponibles + "\nIngrese el nombre del nodo a eliminar:\n\nIngrese 0 para regresar.");

            // si no se ingresó nada
            if (nodoAEliminar.isEmpty() || nodoAEliminar.isBlank()) {
                JOptionPane.showMessageDialog(null, "Error: debe ingresar un nodo.");
                nodoValido = false;
            }

            // si el nodo ingresado no existe
            if (nodoValido && !nodoAEliminar.equals("0") && buscarIndiceNodo(nodoAEliminar) == -1) {
                JOptionPane.showMessageDialog(null, "Error: " + nodoAEliminar + " no existe en el grafo.");
                nodoValido = false;
            }

        } while (nodoValido == false);

        if (!nodoAEliminar.equals("0")) {

            int indiceEliminado = buscarIndiceNodo(nodoAEliminar);
            String[] nuevosNodos = new String[nodos.length - 1];
            int[][] nuevaMatAdy = new int[matrizAdyacencia.length - 1][matrizAdyacencia.length - 1];
            int[][] nuevosPesos = new int[pesos.length - 1][pesos.length - 1];

            // llenar el nuevo arreglo de índice de nodos
            for (int i = 0, k = 0; i < nodos.length; i++) {
                if (i != indiceEliminado) {
                    nuevosNodos[k] = nodos[i];
                    k++;
                }
            }

            // llenar la nueva matriz de adyacencia, omitiendo fila y columna del nodo eliminado
            for (int i = 0, filaNueva = 0; i < matrizAdyacencia.length; i++) {
                if (i != indiceEliminado) {
                    for (int j = 0, colNueva = 0; j < matrizAdyacencia[0].length; j++) {
                        if (j != indiceEliminado) {
                            nuevaMatAdy[filaNueva][colNueva] = matrizAdyacencia[i][j];
                            nuevosPesos[filaNueva][colNueva] = pesos[i][j];
                            colNueva++;
                        }
                    }
                    filaNueva++;
                }
            }

            nodos = nuevosNodos;
            pesos = nuevosPesos;
            setMatrizAdyacencia(nuevaMatAdy);
            setMatrizIncidencia(updateMatrizInc());

            if (tipo != 2) {
                llenarListaAdy();
            }

            mostrarGrafo(true);
        }
    }

    public void Djikstra() {

        if (nodos.length < 2) { // si el grafo tiene menos de 2 nodos
            JOptionPane.showMessageDialog(null, "Error: Deben haber al menos 2 nodos para ejecutar Dijkstra."); // avisar del error
            return; // salir del método sin ejecutar Dijkstra
        }

        StringBuilder nodosDisponibles = new StringBuilder("Nodos disponibles:\n"); // lista de nodos para mostrar al usuario
        for (int i = 0; i < nodos.length; i++) {
            nodosDisponibles.append(nodos[i]).append("\n"); // agregar cada nodo a la lista mostrada
        }

        String nodoOrigen;
        boolean origenValido;
        do {
            origenValido = true; // asumir entrada válida al inicio de cada intento
            nodoOrigen = JOptionPane.showInputDialog(nodosDisponibles + "\nIngrese el nodo origen:\n\nIngrese 0 para regresar."); // pedir el nodo origen

            if (nodoOrigen.isEmpty() || nodoOrigen.isBlank()) { // si no se ingresó nada
                JOptionPane.showMessageDialog(null, "Error: debe ingresar un nodo origen."); // avisar del error
                origenValido = false; // marcar la entrada como inválida
            }

            if (origenValido && !nodoOrigen.equals("0") && buscarIndiceNodo(nodoOrigen) == -1) { // si el nodo no existe en el grafo
                JOptionPane.showMessageDialog(null, "Error: " + nodoOrigen + " no existe en el grafo."); // avisar del error
                origenValido = false; // marcar la entrada como inválida
            }
        } while (origenValido == false); // repetir hasta obtener un origen válido

        if (nodoOrigen.equals("0")) { // si el usuario pidió regresar
            return; // salir del método sin hacer cambios
        }

        String nodoDestino;
        boolean destinoValido;
        do {
            destinoValido = true; // asumir entrada válida al inicio de cada intento
            nodoDestino = JOptionPane.showInputDialog(nodosDisponibles + "\nIngrese el nodo final:\n\nIngrese 0 para regresar."); // pedir el nodo final

            if (nodoDestino.isEmpty() || nodoDestino.isBlank()) { // si no se ingresó nada
                JOptionPane.showMessageDialog(null, "Error: debe ingresar un nodo final."); // avisar del error
                destinoValido = false; // marcar la entrada como inválida
            }

            if (destinoValido && !nodoDestino.equals("0") && buscarIndiceNodo(nodoDestino) == -1) { // si el nodo no existe en el grafo
                JOptionPane.showMessageDialog(null, "Error: " + nodoDestino + " no existe en el grafo."); // avisar del error
                destinoValido = false; // marcar la entrada como inválida
            }
        } while (destinoValido == false); // repetir hasta obtener un destino válido

        if (nodoDestino.equals("0")) { // si el usuario pidió regresar
            return; // salir del método sin hacer cambios
        }

        pedirPesosFaltantes(); // pedir pesos para las aristas que aún no lo tengan definido

        int origen = buscarIndiceNodo(nodoOrigen); // obtener el índice del nodo origen
        int destino = buscarIndiceNodo(nodoDestino); // obtener el índice del nodo destino
        int[] dist = new int[nodos.length]; // guarda la menor distancia conocida desde el origen
        int[] previo = new int[nodos.length]; // guarda el nodo anterior en la mejor ruta encontrada
        boolean[] visitado = new boolean[nodos.length]; // marca los nodos cuya distancia ya quedó fija

        for (int i = 0; i < nodos.length; i++) {
            dist[i] = Integer.MAX_VALUE; // inicialmente todas las distancias son infinito
            previo[i] = -1; // inicialmente ningún nodo tiene predecesor
        }

        dist[origen] = 0; // la distancia del origen hacia sí mismo es 0

        for (int i = 0; i < nodos.length; i++) {
            int actual = buscarNodoConMenorDistancia(dist, visitado); // buscar el nodo no visitado con menor distancia

            if (actual == -1) { // si no se encontró ningún nodo válido
                break; // terminar el algoritmo
            }

            visitado[actual] = true; // fijar definitivamente la distancia del nodo actual

            for (int j = 0; j < nodos.length; j++) {
                if (matrizAdyacencia[actual][j] == 1
                    && !visitado[j]
                    && pesos[actual][j] > 0
                    && dist[actual] != Integer.MAX_VALUE
                    && dist[actual] + pesos[actual][j] < dist[j]) { // si pasar por actual mejora la distancia hacia j

                    dist[j] = dist[actual] + pesos[actual][j]; // actualizar la mejor distancia conocida
                    previo[j] = actual; // guardar el nodo actual como predecesor de j
                }
            }
        }

        if (dist[destino] == Integer.MAX_VALUE) { // si el destino sigue con distancia infinita
            JOptionPane.showMessageDialog(null, "No existe ruta entre " + nodoOrigen + " y " + nodoDestino + "."); // avisar que no existe ruta
            return; // salir del método
        }

        JOptionPane.showMessageDialog(null, // mostrar la ruta y el costo final calculados por Dijkstra
            "Ruta más corta de " + nodoOrigen + " a " + nodoDestino + ": " + construirRuta(previo, destino)
            + "\nCosto total: " + dist[destino]);
    }
    // mostrados

    public void mostrarGrafo(Boolean crearNuevoGrafo) throws Exception {

        // visualizador de grafos basado en la librería graphviz-java

        // la jerarquia es:
        // 1. etiqueta
        // 2. scroll
        // 3. ventana

        if (crearNuevoGrafo) { // solo reconstruye visualGrafo cuando cambió la estructura del grafo
            // Crear nodos para graphviz
            Node[] visualNodes = new Node[nodos.length];
            
            if (tipo == 1) {

                for (int i = 0; i < nodos.length; i++) {
                    visualNodes[i] = Node.builder()
                                        .label(nodos[i])
                                        .shape(NodeShapeEnum.CIRCLE)
                                        .fillColor(Color.ofRGB("#87fae3"))
                                        .build();
                }
                
                // crear grafp de clase graphviz (la que lo muestra) y
                // subclase builder (la que lo construye de forma dinámica)
                Graphviz.GraphvizBuilder graph = Graphviz.graph().addNode(visualNodes);
                
                // añadir aristas al grafo
                for (int i = 0; i < nodos.length; i++) {
                    
                    for (int j = i + 1; j < nodos.length; j++) {
                        if (matrizAdyacencia[i][j] == 1) {
                            graph.addLine(visualNodes[i], visualNodes[j]);
                        }
                    }                
                }
                
                visualGrafo = graph.build();

            }

            if (tipo == 2) {
                for (int i = 0; i < nodos.length; i++) {
                    visualNodes[i] = Node.builder()
                                        .label(nodos[i])
                                        .shape(NodeShapeEnum.CIRCLE)
                                        .fillColor(Color.ofRGB("#87fae3")).build();
                                        
                }
                
                // crear grafp de clase graphviz (la que lo muestra) y
                // subclase builder (la que lo construye de forma dinámica)
                Graphviz.GraphvizBuilder digraph = Graphviz.digraph().addNode(visualNodes);
                
                // añadir aristas al grafo
                for (int i = 0; i < nodos.length; i++) {
                    
                    for (int j = 0; j < nodos.length; j++) {
                        if (matrizAdyacencia[i][j] == 1) {
                            digraph.addLine(visualNodes[i], visualNodes[j]);
                        }
                    }                
                }
                
                visualGrafo = digraph.build();
            }
        }

        if (visualGrafo == null) { // evita refrescar o guardar antes de haber construido el grafo visual
            return;
        }

        if (!crearNuevoGrafo) { // si no se pidió reconstrucción, solo actualiza la imagen actual
            actualizarGrafo();
            return;
        }

        JScrollPane scroll = new JScrollPane(etiqueta);

        if (ventana != null) {
            ventana.dispose(); // cierra la ventana que haya abierta
        }
        
        ventana = new JFrame("Visualizador");

        
        ventana.add(scroll); // se agrega el scroll (que contiene la label dela imagen) a la ventana
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // se establece que se cierre por completo al cerrar.
                                                                // * por defecto esta en HIDE_ON_CLOSE
        actualizarGrafo(); // se asigna la imagen del grafo a la etiqueta, y se muestra en la ventana
        ventana.pack(); // toma el tamaño preferido real de la imagen en la etiqueta
        ventana.setVisible(true); // se hace visible (por defecto, las ventanas en Swing nacen ocultas)
    }

    public void mostrarMatAdy() {
        
        StringBuilder matrizString = new StringBuilder("Matriz de adyacencia:\n\n   ");

        matrizString.append("  "); 
        for (int i = 0; i < nodos.length; i++) {
            matrizString.append(nodos[i]).append(" "); // primera fila
        }
        matrizString.append("\n");

        for (int i = 0; i < nodos.length; i++) {

            matrizString.append(nodos[i]).append(" ");

            for (int j = 0; j < matrizAdyacencia[0].length; j++) {
                matrizString.append(" ").append(matrizAdyacencia[i][j]);
            }

            matrizString.append("\n");
        }

        JOptionPane.showMessageDialog(null, matrizString);
    }

    public void mostrarListAdy() {
        
        StringBuilder listaString = new StringBuilder("Lista de adyacencia:\n\n");

        // solo esta diseñada para grafo tipo 1 (simple no dirigido). NO se piensa implementar para tipo 2.

        Nodo nodoActual = null;
        for (int i = 0; i < nodos.length; i++) {

            nodoActual = this.getPuntaListaAdy()[i];

            listaString.append(nodos[i] + ":  ");

            while (nodoActual != null) {
                listaString.append(nodoActual.getDatoNodoAdy() + " -> ");

                nodoActual = nodoActual.getLiga();
            }

            listaString.append("\n");
        }

        JOptionPane.showMessageDialog(null, listaString);
    }

    public void mostrarMatInc() throws Exception {

        this.setMatrizIncidencia(updateMatrizInc()); //actualizar matriz

        StringBuilder matrizString = new StringBuilder("Matriz de incidencia:\n\n   ");

        if (this.tipo == 2) {
            matrizString.append("Convencion para grafo dirigido: 1 = salida, 2 = entrada, 3 = bucle\n\n");
        }

        matrizString.append("  "); 
        for (int i = 0; i < matrizIncidencia[0].length; i++) {
            matrizString.append(i).append(" "); // primera fila
        }
        matrizString.append("\n");

        for (int i = 0; i < nodos.length; i++) {

            matrizString.append(nodos[i]).append(" ");

            for (int j = 0; j < matrizIncidencia[0].length; j++) {
                matrizString.append(" ").append(matrizIncidencia[i][j]);
            }

            matrizString.append("\n");
        }

        JOptionPane.showMessageDialog(null, matrizString);
    }

    // metodos helpers
 
    public void actualizarGrafo() throws Exception {
        if (tipo == 1) {
            // Save as PNG
            visualGrafo.toFile(FileType.PNG).save("./", "no-dirigido");
            etiqueta.setIcon(new ImageIcon("./no-dirigido.png"));
        }

        if (tipo == 2) {
            // Save as PNG
            visualGrafo.toFile(FileType.PNG).save("./", "dirigido");
            etiqueta.setIcon(new ImageIcon("./dirigido.png"));
        }

        etiqueta.revalidate();
        if (ventana != null) {
            ventana.pack();
        }
    }
     
    public String mostrarConexionesDeNodo(int indiceNodo) {

        StringBuilder conexiones = new StringBuilder();
        boolean huboConexiones = false;

        /* tipo 1 = grafo simple no dirigido */
        if (this.tipo == 1) {
            conexiones.append("El nodo " + nodos[indiceNodo] + " tiene aristas con: ");
            int[][] ady = this.getMatrizAdyacencia(); // Obtener la matriz de adyacencia del grafo

            for (int i = 0; i < nodos.length; i++) {

                if (ady[indiceNodo][i] == 1) { // Si hay una arista entre el nodo actual y el nodo i
                        conexiones.append(nodos[i]).append(", ");
                        huboConexiones = true;
                }
            }

        /* tipo 2 = grafo simple dirigido */
        } else if (this.tipo == 2) {
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

    public int contarAristas() {
        int aristas = 0;

        if (this.tipo == 1) {
            for (int i = 0; i < nodos.length; i++) {
                for (int j = i + 1; j < nodos.length; j++) {
                    if (getMatrizAdyacencia()[i][j] == 1) {
                        aristas++;
                    }
                }
            }
        }

        if (this.tipo == 2) {
            for (int i = 0; i < nodos.length; i++) {
                for (int j = 0; j < nodos.length; j++) {
                    if (getMatrizAdyacencia()[i][j] == 1) {
                        aristas++;
                    }
                }
            }
        }
        
        return aristas;        
    }

    public int contarAristasDeNodo(int indiceNodo) {
        int aristas = 0;

        if (this.tipo == 1) {
            for (int i = 0; i < nodos.length; i++) {
                if (this.getMatrizAdyacencia()[indiceNodo][i] == 1) {
                    aristas++;
                }
            }
        }
        
        if (tipo == 2) {
            // vacio ya que habria que hacer 2 metoidos separados:
            /* - contarEntrantesDeNodo()
               - contarSalientesDeNodo()
            */
        }
        return aristas;
    }

    public Boolean nodoYaExiste (String nuevoNodo) {

        boolean repetido = false;

        for (int i = 0; i < nodos.length && repetido == false; i++) {
            if (nodos[i].equals(nuevoNodo)) {
                repetido = true;
            }
        }


        return repetido;
    }

    public int buscarIndiceNodo(String nombreNodo) {

        int indice = -1;

        for (int i = 0; i < nodos.length && indice == -1; i++) {
            if (nodos[i].equals(nombreNodo)) {
                indice = i;
            }
        }

        return indice;
    }

    public void pedirPesosFaltantes() {

        if (tipo == 1) {
            for (int i = 0; i < nodos.length; i++) {
                for (int j = i + 1; j < nodos.length; j++) {
                    if (matrizAdyacencia[i][j] == 1) { // si existe arista entre ambos nodos
                        if (pesos[i][j] == 0 && pesos[j][i] != 0) { // si falta un peso pero el del sentido contrario ya existe
                            pesos[i][j] = pesos[j][i]; // copiar el mismo peso en este sentido
                        } else if (pesos[j][i] == 0 && pesos[i][j] != 0) { // si falta el peso contrario pero este ya existe
                            pesos[j][i] = pesos[i][j]; // copiar el mismo peso en el sentido contrario
                        } else if (pesos[i][j] == 0 && pesos[j][i] == 0) { // si ambos sentidos siguen sin peso definido
                            int peso = pedirPesoArista(nodos[i], nodos[j]); // pedir el peso de la arista
                            pesos[i][j] = peso; // guardar el peso en un sentido
                            pesos[j][i] = peso; // guardar el mismo peso en el otro sentido
                        }
                    }
                }
            }
        }

        if (tipo == 2) {
            for (int i = 0; i < nodos.length; i++) {
                for (int j = 0; j < nodos.length; j++) {
                    if (matrizAdyacencia[i][j] == 1 && pesos[i][j] == 0) { // si existe arista y su peso aún no fue definido
                        pesos[i][j] = pedirPesoArista(nodos[i], nodos[j]); // pedir el peso de esa arista dirigida
                    }
                }
            }
        }
    }

    public int pedirPesoArista(String nodoOrigen, String nodoDestino) {

        boolean pesoValido;
        String pesoString;
        int peso = 0;

        do {
            pesoValido = true; // asumir entrada válida al inicio de cada intento
            pesoString = JOptionPane.showInputDialog("Ingrese el peso de la arista entre \"" + nodoOrigen + "\" y \"" + nodoDestino + "\"."); // pedir el peso al usuario

            if (pesoString.isEmpty() || pesoString.isBlank()) { // si no se ingresó nada
                JOptionPane.showMessageDialog(null, "Error: debe ingresar un peso."); // avisar del error
                pesoValido = false; // marcar la entrada como inválida
            } else if (!pesoString.matches("\\d+")) { // si el valor no es un entero positivo
                JOptionPane.showMessageDialog(null, "Error: debe ingresar un número entero positivo."); // avisar del error
                pesoValido = false; // marcar la entrada como inválida
            } else {
                peso = Integer.parseInt(pesoString); // convertir el peso ingresado a entero
                if (peso <= 0) { // si el peso no es mayor que 0
                    JOptionPane.showMessageDialog(null, "Error: el peso debe ser mayor que 0."); // avisar del error
                    pesoValido = false; // marcar la entrada como inválida
                }
            }
        } while (pesoValido == false); // repetir hasta obtener un peso válido

        return peso; // devolver el peso validado
    }

    public int buscarNodoConMenorDistancia(int[] dist, boolean[] visitado) {

        int menorDistancia = Integer.MAX_VALUE;
        int indice = -1;

        for (int i = 0; i < nodos.length; i++) {
            if (!visitado[i] && dist[i] < menorDistancia) { // si el nodo no fue visitado y tiene menor distancia
                menorDistancia = dist[i]; // actualizar la menor distancia encontrada
                indice = i; // guardar el índice de ese nodo
            }
        }

        return indice; // devolver el nodo no visitado con menor distancia
    }

    public String construirRuta(int[] previo, int destino) {

        int[] ruta = new int[nodos.length];
        int cantidad = 0;
        StringBuilder recorrido = new StringBuilder();

        for (int actual = destino; actual != -1; actual = previo[actual]) { // recorrer la ruta desde el destino hacia atrás
            ruta[cantidad] = actual; // guardar el nodo actual en la ruta
            cantidad++; // aumentar la cantidad de nodos guardados
        }

        for (int i = cantidad - 1; i >= 0; i--) {
            recorrido.append(nodos[ruta[i]]); // agregar el nodo al recorrido final
            if (i > 0) { // si aún faltan nodos por agregar
                recorrido.append(" -> "); // separar con flecha el siguiente nodo de la ruta
            }
        }

        return recorrido.toString(); // devolver la ruta reconstruida
    }
}
