import javax.swing.JOptionPane;

public class MenuGrafos {

    public static void main(String[] args) throws Exception {

        int opcion = 0;
        String nodosString = new String();

        do {
            menu();
            switch(opcion) {
                
                case 1:
                    nodosString = JOptionPane.showInputDialog("Ingrese los nodos del grafo, separados por comas: ");
                    // Aquí puedes agregar el código para crear un grafo no dirigido
                    break;
                case 2:
                    System.out.println("Has seleccionado la opción 2: Crear un grafo dirigido");
                    // Aquí puedes agregar el código para crear un grafo dirigido
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (opcion != 0);
    }

    public static void menu() {
        System.out.println("======= Grafos =======" +
                            "1. Crear un grafo no dirigido\n" +
                            "2. Crear un grafo dirigido\n\n" +
                            "0. Salir"
        );

    }
}