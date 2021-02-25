package com.julio.contEndesaElect.model;

public class potenciaPrecos {
	protected String Companhia;
	protected double Potencia;
	protected String Data;
	protected double Preco;
	protected double Descr;
	protected double Iva;

	public potenciaPrecos(String companhia, double potencia,
			String data, double preco, double descr, double iva) {
		super();
		Companhia = companhia;
		Potencia = potencia;
		Data = data;
		Preco = preco;
		Descr = descr;
		Iva = iva;
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
	 * @return the data
	 */
	public String getData() {
		return Data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		Data = data;
	}

	/**
	 * @return the preco
	 */
	public double getPreco() {
		return Preco;
	}

	/**
	 * @param preco
	 *            the preco to set
	 */
	public void setPreco(double preco) {
		Preco = preco;
	}

	/**
	 * @return the descr
	 */
	public double getDescr() {
		return Descr;
	}

	/**
	 * @param descr
	 *            the descr to set
	 */
	public void setDescr(double descr) {
		Descr = descr;
	}

	/**
	 * @return the iva
	 */
	public double getIva() {
		return Iva;
	}

	/**
	 * @param iva
	 *            the iva to set
	 */
	public void setIva(double iva) {
		Iva = iva;
	}

}
