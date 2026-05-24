public class Grafos {

    public static void crearGrafo(String nodosString, int tipo) {
        String[] nodos = nodosString.split(",");

        /* tipo 1 = no dirigido */
        if (tipo == 1) {
            for (int i = 0; i < nodos.length; i++) {
                for (int j = 1; j < nodos.length; j++) {
                    
                }
            }
            
        
        /* tipo 2 = dirigido */
        } else if (tipo == 2) {
            System.out.println("Has seleccionado la opción 2: Crear un grafo dirigido");
            // Aquí puedes agregar el código para crear un grafo dirigido
        }
    }
}
