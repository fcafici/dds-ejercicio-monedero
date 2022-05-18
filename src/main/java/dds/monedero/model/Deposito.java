package dds.monedero.model;

import java.time.LocalDate;

public class Deposito extends Movimiento {

  public Deposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }

  public double calcularNuevoSaldo (double monto, double saldo) {
      return saldo + monto;
  }
}
