package perfume.Perfumeria.execption;

public class StockInsuficienteException extends RuntimeException {
  public StockInsuficienteException(String message) {
    super(message);
  }
}
