public class Nodo {
    
    // atributos 

    private String datoNodoAdy;
    private Nodo liga;

    // constructor

    public Nodo(String datoNodoAdy) {
        this.datoNodoAdy = datoNodoAdy;
        this.liga = null;
    }

    // getters y setters

    public String getDatoNodoAdy() {
        return datoNodoAdy;
    }

    public void setDatoNodoAdy(String datoNodoAdy) {
        this.datoNodoAdy = datoNodoAdy;
    }

    public Nodo getLiga() {
        return liga;
    }

    public void setLiga(Nodo liga) {
        this.liga = liga;
    }

}
