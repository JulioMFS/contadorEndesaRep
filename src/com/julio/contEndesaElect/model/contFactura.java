package com.julio.contEndesaElect.model;

public class contFactura {
	public contFactura(String fatura, String data1, String data2,
			String companhia, double potencia, int tipo, String est) {
		super();
		Fatura = fatura;
		Data1 = data1;
		Data2 = data2;
		Companhia = companhia;
		Potencia = potencia;
		Tipo = tipo;
		Est = est;
	}

	protected String Fatura;
	protected String Data1;
	protected String Data2;
	protected String Companhia;
	protected double Potencia;
	protected int Tipo;
	protected String Est;

	/**
	 * @return the fatura
	 */
	public String getFatura() {
		return Fatura;
	}

	/**
	 * @param fatura
	 *            the fatura to set
	 */
	public void setFatura(String fatura) {
		Fatura = fatura;
	}

	/**
	 * @return the data1
	 */
	public String getData1() {
		return Data1;
	}

	/**
	 * @param data1
	 *            the data1 to set
	 */
	public void setData1(String data1) {
		Data1 = data1;
	}

	/**
	 * @return the data2
	 */
	public String getData2() {
		return Data2;
	}

	/**
	 * @param data2
	 *            the data2 to set
	 */
	public void setData2(String data2) {
		Data2 = data2;
	}

	/**
	 * @return the companhia
	 */
	public String getCompanhia() {
		return Companhia;
	}

	/**
	 * @param companhia
	 *            the companhia to set
	 */
	public void setCompanhia(String companhia) {
		Companhia = companhia;
	}

	/**
	 * @return the potencia
	 */
	public double getPotencia() {
		return Potencia;
	}

	/**
	 * @param potencia
	 *            the potencia to set
	 */
	public void setPotencia(double potencia) {
		Potencia = potencia;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return Tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(int tipo) {
		Tipo = tipo;
	}

	public void setEst(String est) {
		Est = est;
	}

	public String getEst() {
		return Est;
	}
}
