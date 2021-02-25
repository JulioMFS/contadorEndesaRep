package com.julio.contEndesaElect.model;

public class electricidadePrecos {

	protected String Companhia;
	protected String Data;
	protected double Simples;
	protected double DescSimples;
	protected double VazioBi;
	protected double ForaVazioBi;
	protected double DescBi;
	protected double VazioTri;
	protected double DescVazioTri;
	protected double PontaTri;
	protected double DescPontaTri;
	protected double CheiasTri;
	protected double DescCheiasTri;
	protected double Iva;
	protected double Potencia;
	protected double PotPreco;
	protected double PotDesc;
	protected double PotIva;
	protected double AccessoRede;
	protected double DescBoasVindas;

	public electricidadePrecos(String companhia, String data, double simples,
			double descsimples, double vazioBi, double foraVazioBi,
			double descBi, double vazioTri, double descVazioTri,
			double pontaTri, double descPontaTri, double cheiasTri,
			double descCheiasTri, double iva, double potencia, double potPreco,
			double potDesc, double potIva, double accessoRede,
			double descBoasVindas) {
		super();
		Companhia = companhia;
		Data = data;
		Simples = simples;
		DescSimples = descsimples;
		VazioBi = vazioBi;
		ForaVazioBi = foraVazioBi;
		DescBi = descBi;
		VazioTri = vazioTri;
		DescVazioTri = descVazioTri;
		PontaTri = pontaTri;
		DescPontaTri = descPontaTri;
		CheiasTri = cheiasTri;
		DescCheiasTri = descCheiasTri;
		Iva = iva;
		Potencia = potencia;
		PotPreco = potPreco;
		PotDesc = potDesc;
		PotIva = potIva;
		AccessoRede = accessoRede;
		DescBoasVindas = descBoasVindas;
	}

	/**
	 * @return the companhia
	 */
	public String getCompanhia() {
		return Companhia;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return Data;
	}

	/**
	 * @return the simples
	 */
	public double getSimples() {
		return Simples;
	}

	public double getDescSimples() {
		return DescSimples;
	}

	/**
	 * @return the vazioBi
	 */
	public double getVazioBi() {
		return VazioBi;
	}

	/**
	 * @return the foraVazioBi
	 */
	public double getForaVazioBi() {
		return ForaVazioBi;
	}

	/**
	 * @return the descBi
	 */
	public double getDescBi() {
		return DescBi;
	}

	/**
	 * @return the vazioTri
	 */
	public double getVazioTri() {
		return VazioTri;
	}

	/**
	 * @return the pontaTri
	 */
	public double getPontaTri() {
		return PontaTri;
	}

	/**
	 * @return the cheiasTri
	 */
	public double getCheiasTri() {
		return CheiasTri;
	}

	/**
	 * @return the descTri
	 */
	public double getDescCheiasTri() {
		return DescCheiasTri;
	}

	public double getDescPontaTri() {
		return DescPontaTri;
	}

	public double getDescVazioTri() {
		return DescVazioTri;
	}

	/**
	 * @return the iva
	 */
	public double getIva() {
		return Iva;
	}

	/**
	 * @return the potencia
	 */
	public double getPotencia() {
		return Potencia;
	}

	/**
	 * @param companhia
	 *            the companhia to set
	 */
	public void setCompanhia(String companhia) {
		Companhia = companhia;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		Data = data;
	}

	/**
	 * @param simples
	 *            the simples to set
	 */
	public void setSimples(double simples) {
		Simples = simples;
	}

	public void setDescSimples(double descsimples) {
		DescSimples = descsimples;
	}

	/**
	 * @param vazioBi
	 *            the vazioBi to set
	 */
	public void setVazioBi(double vazioBi) {
		VazioBi = vazioBi;
	}

	/**
	 * @param foraVazioBi
	 *            the foraVazioBi to set
	 */
	public void setForaVazioBi(double foraVazioBi) {
		ForaVazioBi = foraVazioBi;
	}

	/**
	 * @param descBi
	 *            the descBi to set
	 */
	public void setDescBi(double descBi) {
		DescBi = descBi;
	}

	/**
	 * @param vazioTri
	 *            the vazioTri to set
	 */
	public void setVazioTri(double vazioTri) {
		VazioTri = vazioTri;
	}

	public void setDescVazioTri(double descVazioTri) {
		DescVazioTri = descVazioTri;
	}

	/**
	 * @param pontaTri
	 *            the pontaTri to set
	 */
	public void setPontaTri(double pontaTri) {
		PontaTri = pontaTri;
	}

	public void setDescPontaTri(double descPontaTri) {
		DescPontaTri = descPontaTri;
	}

	/**
	 * @param cheiasTri
	 *            the cheiasTri to set
	 */
	public void setCheiasTri(double cheiasTri) {
		CheiasTri = cheiasTri;
	}

	/**
	 * @param descTri
	 *            the descTri to set
	 */
	public void setDescCheiasTri(double descCheiasTri) {
		DescCheiasTri = descCheiasTri;
	}

	/**
	 * @param iva
	 *            the iva to set
	 */
	public void setIva(double iva) {
		Iva = iva;
	}

	/**
	 * @param potencia
	 *            the potencia to set
	 */
	public void setPotencia(double potencia) {
		Potencia = potencia;
	}

	/**
	 * @return the potPreco
	 */
	public double getPotPreco() {
		return PotPreco;
	}

	/**
	 * @param potPreco
	 *            the potPreco to set
	 */
	public void setPotPreco(double potPreco) {
		PotPreco = potPreco;
	}

	/**
	 * @return the potDesc
	 */
	public double getPotDesc() {
		return PotDesc;
	}

	/**
	 * @param potDesc
	 *            the potDesc to set
	 */
	public void setPotDesc(double potDesc) {
		PotDesc = potDesc;
	}

	/**
	 * @return the potIva
	 */
	public double getPotIva() {
		return PotIva;
	}

	/**
	 * @param potIva
	 *            the potIva to set
	 */
	public void setPotIva(double potIva) {
		PotIva = potIva;
	}

	/**
	 * @return the accessoRede
	 */
	public double getAccessoRede() {
		return AccessoRede;
	}

	/**
	 * @param accessoRede
	 *            the accessoRede to set
	 */
	public void setAccessoRede(double accessoRede) {
		AccessoRede = accessoRede;
	}

	/**
	 * @return the descBoasVindas
	 */
	public double getDescBoasVindas() {
		return DescBoasVindas;
	}

	/**
	 * @param descBoasVindas
	 *            the descBoasVindas to set
	 */
	public void setDescBoasVindas(double descBoasVindas) {
		DescBoasVindas = descBoasVindas;
	}
}
