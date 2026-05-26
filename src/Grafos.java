import javax.swing.JOptionPane;

public class Grafos {

    private String[] nodos;
    private int[][] matrizAdyacencia;
    private int[][] matrizIncidencia;
    private Nodo[] puntaListaAdy;
    private int tipo; // 1 = grafo simple no dirigido, 2 = multigrafo dirigido
    

    // constructor

    public Grafos(String[] nodos, int tipo) {
        this.nodos = nodos;
        this.matrizAdyacencia = new int[nodos.length][nodos.length];
        this.matrizIncidencia = null;
        this.puntaListaAdy = null;
        this.tipo = tipo;
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

    // métodos

    public static Grafos crearGrafo(String[] nodos, int tipo) {

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

                for (int j = 0; j < nodos.length; j++) {
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
                        
                        conexiones = conexionesString.split(",");
                        entradaValida = true;

                        /*
                        
                        Validaciones
                        */

                        // si no se ingresa nada
                        if (conexionesString.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: Debe ingresar al menos un nodo.");
                            entradaValida = false;
                        }

                        // si se ingresan comas sin nodos entre ellas
                        for (int j = 0; j < conexiones.length && entradaValida; j++) {
                            if (conexiones[j].isEmpty()) {
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

                    } while (entradaValida == false);

                    // si pasaron todas las validaciones, se llena la matriz de adyacencia
                    for (int j = 0; j < conexiones.length; j++) {
                        for (int k = 0; k < nodos.length; k++) {
                            if (conexiones[j].equals(nodos[k])) { // si se encuentra el nodo ingresado entre los nodos del grafo
                                grafo.setMatrizAdyacencia(i, k, 1);
                                grafo.setMatrizAdyacencia(k, i, 1);
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

        /* tipo 2 = multigrafo dirigido */
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

                for (int j = 0; j < nodos.length; j++) {
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
                        if (conexionesString.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: Debe ingresar al menos un nodo.");
                            entradaValida = false;
                        }

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

                    } while (entradaValida == false);

                    // si pasaron todas las validaciones, se llena la matriz de adyacencia
                    for (int j = 0; j < conexiones.length; j++) {
                        for (int k = 0; k < nodos.length; k++) {
                            if (conexiones[j].equals(nodos[k])) { // si se encuentra el nodo ingresado entre los nodos del grafo
                                grafo.setMatrizAdyacencia(i, k, 1);
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

    public int[][] updateMatrizInc() {

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

        return nuevaMatrizInc;
    }

    public void llenarListaAdy() {
        
        // solo se pidió lista de adyacencia para grafo no dirigido
        // entonces no se pide tipo 
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
    }

    // mostrados

    public void mostrarGrafo() {
        if (tipo == 1) {

        }

        if (tipo == 2) {

        }
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

        // solo esta diseñada para grafo tipo 1 (simple no dirigido)

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

    public void mostrarMatInc() {

        this.setMatrizIncidencia(updateMatrizInc()); //actualizar matriz

        StringBuilder matrizString = new StringBuilder("Matriz de incidencia:\n\n   ");

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

        /* tipo 2 = multigrafo dirigido */
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
            // vacio ya que no se pidio
        }
        return aristas;
    }


}
