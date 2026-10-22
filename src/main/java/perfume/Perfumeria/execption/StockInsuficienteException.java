package perfume.Perfumeria.execption;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String perfume, int disponible, int solicitado) {
        super("Stock insuficiente para" + perfume + "(disponibles:" + disponible + ", solicitados:" + solicitado + ")");
    }
}
