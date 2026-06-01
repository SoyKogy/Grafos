import javax.swing.JOptionPane;

public class MenuGrafos {

    public static void main(String[] args) throws Exception {

        int opcion = 0;
        String nodosString = new String();

        do {
            boolean repetidos = false;

            opcion = menu(0);            
            switch(opcion) {
                
                case 1:

                    String[] nodosNoDirigidos;
                    
                    do {
                        nodosString = JOptionPane.showInputDialog("Ingrese los nodos del grafo, separados por comas.\n\n" +
                        "Ejemplo: A,B,FC,D,12,2"
                        );
                        nodosNoDirigidos = nodosString.split(",");

                        repetidos = verificarNodosRepetidos(nodosNoDirigidos);
                    } while (repetidos == true);

                    Grafos grafoNoDirigido = Grafos.crearGrafo(nodosNoDirigidos, 1);

                    menuGrafos(grafoNoDirigido);
                    break;
                case 2:
                    
                    String[] nodosDirigidos = nodosString.split(",");

                    do {
                        nodosString = JOptionPane.showInputDialog("Ingrese los nodos del grafo, separados por comas.\n\n" +
                        "Ejemplo: A,B,FC,D,12,2"
                        );
                        nodosDirigidos = nodosString.split(",");

                        repetidos = verificarNodosRepetidos(nodosDirigidos);
                    } while (repetidos == true);

                    Grafos grafoDirigido = Grafos.crearGrafo(nodosDirigidos, 2);

                    menuGrafos(grafoDirigido);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (opcion != 0);
    }

    public static int menu(int opcion) {

        int opcionUsuario = 0;
        if (opcion == 0) {
            opcionUsuario = Integer.parseInt(JOptionPane.showInputDialog(null, "======= Grafos =======\n" +
                                "1. Crear un grafo no dirigido\n" +
                                "2. Crear un grafo dirigido\n\n" +
                                "0. Salir"
            ));
        } else if (opcion == 1) {
            opcionUsuario = Integer.parseInt(JOptionPane.showInputDialog(null, "======= Grafos =======\n" +
                            "1. Mostrar grafo\n" +
                            "2. Mostrar matriz de adyacencia\n" +
                            "3. Mostrar lista de adyacencia\n" +
                            "4. Mostrar matriz de incidencia\n\n" +
                            "5. Recorrido DFS\n" +
                            "6. Recorrido BFS\n\n" +
                            "7. Insertar nodo\n" +
                            "8. Eliminar nodo\n" +
                            "9. Algoritmo de Dijkstra\n\n" +
                            "-1. Cerrar grafo (si está abierto)\n" +
                            "0. Volver a la creación de grafos"
            ));
        }

        return opcionUsuario;
    }

    public static void menuGrafos(Grafos grafo) throws Exception {
        
        int opcion = 0;
        do {
            opcion = menu(1);
                switch(opcion) {
                
                case 1:
                    grafo.mostrarGrafo(true);
                    break;
                case 2:
                    grafo.mostrarMatAdy();
                    break;
                case 3:
                    grafo.mostrarListAdy();
                    break;
                case 4:
                    grafo.mostrarMatInc();
                    break;
                case 5:
                    grafo.DFS();
                    break;
                case 6:
                    grafo.BFS();
                    break;
                case 7:
                    grafo.insertarNodo();
                    break;
                case 8:
                    grafo.eliminarNodo();
                    break;
                case 9:
                    grafo.Djikstra();
                    break;
                case -1:
                    grafo.getVentana().dispose(); // cierra el grafo que estuviese abierto
                    break;
                case 0:
                    grafo.getVentana().dispose(); // cierra el grafo que estuviese abierto
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (opcion != 0);
        
    }

    public static boolean verificarNodosRepetidos(String[] nodos) {

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
}
