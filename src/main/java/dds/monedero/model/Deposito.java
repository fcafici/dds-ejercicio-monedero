package dds.monedero.model;

public class Deposito extends Movimiento {
  public double calcularValor(Cuenta cuenta) {
      return cuenta.getSaldo() + getMonto();
      // return cuenta.montoIncrementado(getMonto());
  }
}
