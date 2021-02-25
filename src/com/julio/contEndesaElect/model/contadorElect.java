package com.julio.contEndesaElect.model;

/**
 * User.java This is a model class represents a User entity
 * 
 * @author Ramesh Fadatare
 * 
 */
public class contadorElect {

	protected int id;
	protected String fatura;
	protected String parcela;
	protected String data1;
	protected String data2;
	protected String leituradata;
	protected String est;
	protected int vazio;
	protected int ponta;
	protected int cheia;
	protected int foravazio;
	protected double valor;
	protected String companhia;
	protected int tipo;

	public contadorElect(int id, String fatura, String parcela, String data1,
			String data2, String leituradata, String est, int vazio, int ponta,
			int cheia, int foravazio, double valor, String companhia, int Tipo) {
		super();
		this.id = id;
		this.fatura = fatura;
		this.parcela = parcela;
		this.data1 = data1;
		this.data2 = data2;
		this.leituradata = leituradata;
		this.est = est;
		this.vazio = vazio;
		this.ponta = ponta;
		this.cheia = cheia;
		this.foravazio = foravazio;
		this.valor = valor;
		this.companhia = companhia;
		this.tipo = Tipo;
	}

	public contadorElect(String fatura, String parcela, String data1,
			String data2, String leituradata, String est, int vazio, int ponta,
			int cheia, int foravazio, double valor, String companhia, int Tipo) {
		super();
		this.fatura = fatura;
		this.parcela = parcela;
		this.data1 = data1;
		this.data2 = data2;
		this.leituradata = leituradata;
		this.est = est;
		this.vazio = vazio;
		this.ponta = ponta;
		this.cheia = cheia;
		this.foravazio = foravazio;
		this.valor = valor;
		this.companhia = companhia;
		this.tipo = Tipo;
	}

	public contadorElect() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFatura() {
		return fatura;
	}

	public void setFatura(String fatura) {
		this.fatura = fatura;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getLeituradata() {
		return leituradata;
	}

	public void setLeituradata(String leituradata) {
		this.leituradata = leituradata;
	}

	public String getEst() {
		return est;
	}

	public void setEst(String est) {
		this.est = est;
	}

	public int getVazio() {
		return vazio;
	}

	public void setVazio(int vazio) {
		this.vazio = vazio;
	}

	public int getPonta() {
		return ponta;
	}

	public void setPonta(int ponta) {
		this.ponta = ponta;
	}

	public int getCheia() {
		return cheia;
	}

	public void setCheia(int cheia) {
		this.cheia = cheia;
	}

	public int getForavazio() {
		return foravazio;
	}

	public void setForavazio(int foravazio) {
		this.foravazio = foravazio;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getCompanhia() {
		return companhia;
	}

	public void setCompanhia(String companhia) {
		this.companhia = companhia;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}