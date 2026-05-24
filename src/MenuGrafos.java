import javax.swing.JOptionPane;

public class MenuGrafos {

    public static void main(String[] args) throws Exception {

        int opcion = 0;
        String nodosString = new String();

        do {
            opcion = menu(0);            
            switch(opcion) {
                
                case 1:
                    nodosString = JOptionPane.showInputDialog("Ingrese los nodos del grafo, separados por comas: ");
                    Grafos.crearGrafo(nodosString, 1);
                    menuGrafos();
                    break;
                case 2:
                    nodosString = JOptionPane.showInputDialog("Ingrese los nodos del grafo, separados por comas: ");
                    Grafos.crearGrafo(nodosString, 2);
                    menuGrafos();
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
            opcionUsuario = Integer.parseInt(JOptionPane.showInputDialog(null, "======= Grafos =======" +
                                "1. Crear un grafo no dirigido\n" +
                                "2. Crear un grafo dirigido\n\n" +
                                "0. Salir"
            ));
        } else if (opcion == 1) {
            opcionUsuario = Integer.parseInt(JOptionPane.showInputDialog(null, "======= Grafos =======" +
                            "1. Mostrar grafo\n" +
                            "2. Mostrar matriz de adyacencia\n" +
                            "3. Mostrar lista de adyacencia\n" +
                            "4. Mostrar matriz de incidencia\n\n" +
                            "0. Volver a la creación de grafos"
            ));
        }

        return opcionUsuario;
    }

    public static void menuGrafos() {
        
        int opcion = 0;
        do {
            opcion = menu(1);
                switch(opcion) {
                
                case 1:
                    // mostrar grafo
                    break;
                case 2:
                    // mostrar matriz de adyacencia
                    break;
                case 3:
                    // mostrar lista de adyacencia
                    break;
                case 4:
                    // mostrar matriz de incidencia
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (opcion != 0);
        
    }
}
