package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();
  int maximoDepositos = 3;
  double limiteExtraccionDiario = 1000;

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setDepositos(List<Deposito> depositos) {
    this.depositos = depositos;
  }
  public void setExtracciones(List<Extraccion> extracciones) {
    this.extracciones = extracciones;
  }

  // no nos gusta que depositar y extraer tengan la misma lógica, pero tampoco queremos delegarlo a las respectivas clases
  // porque usan atributos de la clase Cuenta.
  public void depositar(double monto) {
    validarDeposito(monto);
    Deposito deposito = new Deposito(LocalDate.now(), monto);
    depositos.add(deposito);
    saldo = deposito.calcularNuevoSaldo(monto, saldo); // podriamos haber hecho la suma aca y eliminar las clases deposito y extraccion pero lo dejamos asi porque no quisimos cambiar tanto
  }

  public void extraer(double monto) {
    validarExtraccion(monto);
    Extraccion extraccion = new Extraccion(LocalDate.now(), monto);
    extracciones.add(extraccion);
    saldo = extraccion.calcularNuevoSaldo(monto, saldo); // idem deposito
  }

  private void validarNegativo(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException("el monto debe ser un valor positivo (" + monto + ")");
    }
  }

  private void validarMaximoDepositos(){
    if (depositos.size() >= maximoDepositos) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + maximoDepositos + " depositos diarios");
    }
  }

  private void validarSaldo(double monto) {
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de $" + getSaldo());
    }
  }

  private void validarMaximoExtraccionDiaria(double monto){
    double extraccionDiariaRestante = limiteExtraccionDiario - getMontoExtraidoA(LocalDate.now());
    if (monto > extraccionDiariaRestante) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $" + limiteExtraccionDiario
          + " diarios, te quedan: $" + extraccionDiariaRestante);
    }
  }

  private void validarDeposito(double monto) {
    validarNegativo(monto);
    validarMaximoDepositos();
  }

  private void validarExtraccion(double monto) {
    validarNegativo(monto);
    validarSaldo(monto);
    validarMaximoExtraccionDiaria(monto);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return extracciones.stream()
        .filter(extracciones -> extracciones.esDeLaFecha(fecha))
        .mapToDouble(Extraccion::getMonto)
        .sum();
  }

  public List<Deposito> getDepositos() {
    return depositos;
  }

  public List<Extraccion> getExtracciones() {
    return extracciones;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }
}
