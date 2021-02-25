package com.julio.contEndesa.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.julio.contEndesaElect.model.electricidadePrecos;
import com.julio.contEndesaElect.model.contadorElect;
import com.julio.contEndesaElect.model.TableRows;
import com.julio.contEndesaElect.model.contFactura;
import com.julio.contEndesaElect.model.potenciaPrecos;

/**
 * AbstractDAO.java This DAO class provides CRUD database operations for the
 * table facturaelects in the database.
 * 
 * @author Ramesh Fadatare
 * 
 */
public class contEndesaDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/agro?useSSL=false";
	// private String jdbcURL =
	// "jdbc:mysql://sanfona.myvnc.com:3306/agro?useSSL=false";
	// private String jdbcURL ="jdbc:mysql://sanfona.myvnc.com:3306/" + schema +
	// "?autoReconnect=true&useSSL=false";
	private String jdbcUsername = "root";
	// private String jdbcUsername = "julio";

	private String jdbcPassword = "j301052";
	private static String env = "XXXX";
	private static String envirnmt = "agro";

	private static String INSERT_CONTADORELECT_SQL = "INSERT INTO "
			+ "contadorelect"
			+ " (Fatura, Nome, Data1, Data2, LeituraData, Est, Vazio, Ponta, Cheia, foraVazio, Valor, Companhia) VALUES "
			+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private static final String SELECT_CONTADORELECT_BY_ID = "select a.id, Fatura, a.Nome, Data1, Data2, LeituraData, Est,"
			+ " Vazio, Ponta, Cheia, foraVazio, Valor, a.Companhia, Tipo"
			+ "  from contadorelect a, parcela b"
			+ " where a.id =? and a.Nome = b.Nome";
	private static String SELECT_ALL_CONTADORELECT = "select a.id, Fatura, a.Nome, Data1, Data2, LeituraData, "
			+ "Est, Vazio, Ponta, Cheia, foraVazio, Valor, a.Companhia, Tipo "
			+ "from contadorelect a, parcela b "
			+ " where Data1 between ? and ? and a.Nome like ? and a.Nome = b.Nome";
	private static String DELETE_CONTADORELECT_SQL = "delete from "
			+ "contadorelect where id = ?;";
	private static String UPDATE_CONTADORELECT_SQL = "update "
			+ "contadorelect set Fatura = ?, Nome= ?, Data1 =?,"
			+ " Data2 = ?, LeituraData= ?, Est =?, Vazio =?, Ponta = ?, Cheia=?, foraVazio=?, Valor=?  where id = ?;";
	private static final String SELECT_MIN_DATE = "SELECT MIN(Data1) FROM "
			+ "contadorelect";
	private static final String SELECT_MAX_DATE = "SELECT MAX(Data1) FROM "
			+ "contadorelect";
	private static String SELECT_CONTADORELECT = "select id, Fatura, Nome, Data1, Data2, LeituraData,"
			+ " Est, Vazio, Ponta, Cheia, foraVazio, Valor, Companhia"
			+ " from contadorelect where id = ?";
	private static String SELECT_LATEST_PRECOS = "select  a.Companhia, a.Data, Simples, DescSimples, "
			+ "	VazioBi, ForaVazioBi, DescBi, VazioTri, DescVazioTri, PontaTri, DescPontaTri, CheiasTri,  DescCheiasTri, a.Iva, b.Tipo, "
			+ " c.potencia, c.preco, c.Desc, c.Iva, a.AccessoRede, a.DescBoasVindas"
			+ "	 from   "
			+ "electricidadeprecos a "
			+ "        ,"
			+ "parcela b "
			+ "        ,"
			+ "potenciatbl c"
			+ "      where a.Companhia = b.Companhia "
			+ "        and a.Companhia = c.Companhia "
			+ "        and b.Nome = ? "
			+ "        and a.Data <= ? "
			+ "        and c.Data <= ? "
			+ "        and c.potencia = b.potencia "
			+ "        and c.potencia = a.potencia "
			+ "	 order by a.Data Desc Limit 1";
	private static String GET_INVNO = "select Fatura, Data1, Data2, b.Companhia, b.Potencia, b.Tipo, a.Est"
			+ " FROM  contadorelect  a, parcela b"
			+ " where  a.Nome = ? and a.Nome = b.Nome"
			+ " and b.Companhia != '' order by a.Data1 desc limit 1";
	private static String GET_PRICES = "Select Companhia, Data, Simples, DescSimples, VazioBi, ForaVazioBi, DescBi, "
			+ "VazioTri, DescVazioTri, PontaTri, DescPontaTri, CheiasTri, DescCheiasTri, Iva, Potencia, AccessoRede, DescBoasVindas "
			+ "FROM electricidadeprecos where Companhia = ? and Potencia = ? "
			+ "and Data >= (SELECT max(data) FROM electricidadeprecos "
			+ "             where Companhia = ? and Potencia = ? and Data <= ?) "
			+ "   order by Data limit 2";
	private static String GET_POTENCIA = "SELECT Preco, `Desc`, Iva from potenciatbl "
			+ "where Companhia = ? and Potencia = ? "
			+ "and Data <= ? order by Data desc limit 1";
	private static String GET_COUNT = "SELECT count(*) as rowcount FROM contadorelect "
			+ "where Fatura = ?";
	private static String GET_COMPANHIA = "SELECT Companhia FROM parcela where Nome = ? and Companhia != '' ";

	public contEndesaDAO() {
		String currentDirectory = System.getProperty("user.dir");
		if (currentDirectory.indexOf("Apache") > 0
				|| currentDirectory.indexOf("apache") > 0
				|| currentDirectory.indexOf("pi") > 0)
			env = "agro";
		else
			env = "test";
		// env = "agro";

		System.out.println("user.dir: " + currentDirectory + "\nenvironment: "
				+ env);
	}

	protected Connection getConnection() {
		Connection connection = null;
		String url = jdbcURL.replaceAll("agro", env);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, jdbcUsername,
					jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public void insertContadorElect(contadorElect contadorelect)
			throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(INSERT_CONTADORELECT_SQL)) {
			// preparedStatement.setInt(1, contadorelect.getId());
			preparedStatement.setString(1, contadorelect.getFatura());
			preparedStatement.setString(2, contadorelect.getParcela());
			preparedStatement.setString(3, contadorelect.getData1());
			preparedStatement.setString(4, contadorelect.getData2());
			preparedStatement.setString(5, contadorelect.getLeituradata());
			preparedStatement.setString(6, contadorelect.getEst());
			preparedStatement.setInt(7, contadorelect.getVazio());
			preparedStatement.setInt(8, contadorelect.getPonta());
			preparedStatement.setInt(9, contadorelect.getCheia());
			preparedStatement.setInt(10, contadorelect.getForavazio());
			preparedStatement.setDouble(11, contadorelect.getValor());
			preparedStatement.setString(12, contadorelect.getCompanhia());

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public boolean faturaExiste(String fatura) throws SQLException {
		boolean faturaExists = false;
		int count = 0;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(GET_COUNT)) {
			preparedStatement.setString(1, fatura);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("rowcount");
			}
			if (count > 0)
				faturaExists = true;
		} catch (SQLException e) {
			printSQLException(e);
		}
		return faturaExists;
	}

	public List<contFactura> getLastInv(String parcela, String date) {
		List<contFactura> factura = new ArrayList<>();
		String nome = parcela;
		try (Connection connection = getConnection();
		// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection
						.prepareStatement(GET_INVNO);) {
			preparedStatement.setString(1, parcela);
			// preparedStatement.setString(2, date);
			System.out.println("date: " + date + "\n" + preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String fatura = rs.getString("Fatura");

				String data1 = rs.getString("Data1");
				String data2 = rs.getString("Data2");
				String companhia = rs.getString("Companhia");
				double potencia = rs.getDouble("Potencia");
				int tipo = rs.getInt("Tipo");
				String est = rs.getString("Est");
				factura.add(new contFactura(fatura, data1, data2, companhia,
						potencia, tipo, est));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return factura;
	}

	public String getCompanhia(String parcela) {
		String companhia = "";
		try (Connection connection = getConnection();
		// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection
						.prepareStatement(GET_COMPANHIA);) {
			preparedStatement.setString(1, parcela);
			System.out.println("parcela: " + parcela + preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				companhia = rs.getString("Companhia");
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return companhia;
	}

	public List<electricidadePrecos> getElectPrecos(String companhia,
			double potencia, String d1) {
		List<electricidadePrecos> electricidadeprecos = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(GET_PRICES);) {
			preparedStatement.setString(1, companhia);
			preparedStatement.setString(3, companhia);
			preparedStatement.setDouble(2, potencia);
			preparedStatement.setDouble(4, potencia);
			preparedStatement.setString(5, d1);
			ResultSet rs = preparedStatement.executeQuery();
			System.out.println(preparedStatement);
			while (rs.next()) {
				String comp = rs.getString("Companhia");
				String data = rs.getString("Data");
				double simples = rs.getDouble("Simples");
				double descsimples = rs.getDouble("DescSimples");
				double vaziobi = rs.getDouble("VazioBi");
				double foravaziobi = rs.getDouble("ForaVazioBi");
				double descbi = rs.getDouble("DescBi");
				double vaziotri = rs.getDouble("VazioTri");
				double descvaziotri = rs.getDouble("DescVazioTri");
				double pontatri = rs.getDouble("PontaTri");
				double descpontatri = rs.getDouble("DescPontaTri");
				double cheiastri = rs.getDouble("CheiasTri");
				double desccheiastri = rs.getDouble("DescCheiasTri");
				double iva = rs.getDouble("Iva");
				double pot = rs.getDouble("Potencia");
				double accessoRede = rs.getDouble("AccessoRede");
				double descBoasVindas = rs.getDouble("DescBoasVindas");
				List<potenciaPrecos> potenciaprecos = getPotenciaPrecos(
						companhia, potencia, d1);
				potenciaPrecos potprecos = potenciaprecos.get(0);
				electricidadeprecos.add(new electricidadePrecos(companhia,
						data, simples, descsimples, vaziobi, foravaziobi,
						descbi, vaziotri, descvaziotri, pontatri, descpontatri,
						cheiastri, desccheiastri, iva, pot, potprecos
								.getPreco(), potprecos.getDescr(), potprecos
								.getIva(), accessoRede, descBoasVindas));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		// System.out.println("facturaelect; " + facturaelects);
		return electricidadeprecos;
	}

	public List<potenciaPrecos> getPotenciaPrecos(String companhia,
			double potencia, String d1) {
		List<potenciaPrecos> potenciaprecos = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(GET_POTENCIA);) {
			preparedStatement.setString(1, companhia);
			preparedStatement.setDouble(2, potencia);
			preparedStatement.setString(3, d1);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				double preco = rs.getDouble("Preco");
				double desc = rs.getDouble("Desc");
				double iva = rs.getDouble("Iva");

				potenciaprecos.add(new potenciaPrecos(companhia, potencia, d1,
						preco, desc, iva));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		// System.out.println("facturaelect; " + facturaelects);
		return potenciaprecos;
	}

	public List<contadorElect> selectAllContadorElect(String data1,
			String data2, String nome1) {

		List<contadorElect> contadorelect = new ArrayList<>();
		try (Connection connection = getConnection();

				PreparedStatement preparedStatement = connection
						.prepareStatement(SELECT_ALL_CONTADORELECT);) {
			String nome;
			if (nome1.isEmpty() || nome1.length() == 0)
				nome = "%";
			else
				nome = "%" + nome1 + "%";
			preparedStatement.setString(1, data1);
			preparedStatement.setString(2, data2);
			preparedStatement.setString(3, nome);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String fatura = rs.getString("Fatura");
				String nome02 = rs.getString("Nome");
				String data01 = rs.getString("Data1");
				String data02 = rs.getString("Data2");
				String leituradata = rs.getString("LeituraData");
				String est = rs.getString("Est");
				int vazio = rs.getInt("Vazio");
				int ponta = rs.getInt("Ponta");
				int cheia = rs.getInt("Cheia");
				int foravazio = rs.getInt("foraVazio");
				double valor = rs.getDouble("Valor");
				String companhia = rs.getString("Companhia");
				int tipo = rs.getInt("tipo");
				contadorelect.add(new contadorElect(id, fatura, nome02, data01,
						data02, leituradata, est, vazio, ponta, cheia,
						foravazio, valor, companhia, tipo));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return contadorelect;
	}

	public List<contadorElect> selectContadorElect(int id) {

		List<contadorElect> contadorelect = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(SELECT_CONTADORELECT_BY_ID);) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String fatura = rs.getString("Fatura");
				String nome02 = rs.getString("Nome");
				String data01 = rs.getString("Data1");
				String data02 = rs.getString("Data2");
				String leituradata = rs.getString("LeituraData");
				String est = rs.getString("Est");
				int vazio = rs.getInt("Vazio");
				int ponta = rs.getInt("Ponta");
				int cheia = rs.getInt("Cheia");
				int foravazio = rs.getInt("foraVazio");
				double valor = rs.getDouble("Valor");
				String companhia = rs.getString("Companhia");
				int tipo = rs.getInt("Tipo");
				contadorelect.add(new contadorElect(id, fatura, nome02, data01,
						data02, leituradata, est, vazio, ponta, cheia,
						foravazio, valor, companhia, tipo));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return contadorelect;
	}

	public List<electricidadePrecos> getElectPrecos(String parcela,
			String leituradata) {

		List<electricidadePrecos> contEndesaPrecos = new ArrayList<>();
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();

		// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection
						.prepareStatement(SELECT_LATEST_PRECOS);) {
			String nome;

			preparedStatement.setString(1, parcela);
			preparedStatement.setString(2, leituradata);
			preparedStatement.setString(3, leituradata);

			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String companhia = rs.getString("Companhia");
				String data = rs.getString("Data");
				double simples = rs.getDouble("Simples");
				double descsimples = rs.getDouble("DescSimples");
				double vaziobi = rs.getDouble("VazioBi");
				double foravaziobi = rs.getDouble("ForaVazioBi");
				double descbi = rs.getDouble("DescBi");
				double vaziotri = rs.getDouble("VazioTri");
				double descvaziotri = rs.getDouble("DescVazioTri");
				double pontatri = rs.getDouble("PontaTri");
				double descpontatri = rs.getDouble("DescPontaTri");
				double cheiastri = rs.getDouble("CheiasTri");
				double desccheiastri = rs.getDouble("DescCheiasTri");
				double iva = rs.getDouble("Iva");
				int tipo = rs.getInt("Tipo");
				double potencia = rs.getDouble("Potencia");
				double preco = rs.getDouble("Preco");
				double potenciaDesc = rs.getDouble("Desc");
				double potenciaIva = rs.getDouble("Iva");
				double accessoRede = rs.getDouble("AccessoRede");
				double descBoasVindas = rs.getDouble("DescBoasVindas");
				contEndesaPrecos
						.add(new electricidadePrecos(companhia, data, simples,
								descsimples, vaziobi, foravaziobi, descbi,
								vaziotri, descvaziotri, pontatri, descpontatri,
								cheiastri, desccheiastri, iva, potencia, preco,
								potenciaDesc, potenciaIva, accessoRede,
								descBoasVindas));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return contEndesaPrecos;
	}

	public List<TableRows> getTableRows(int tipo, String companhia,
			List<electricidadePrecos> contEndesaPrecos, int vazio,
			int foraVazio, int ponta, int cheias, String date1, String date2,
			boolean estimado) {
		electricidadePrecos ep = contEndesaPrecos.get(0);
		int len = contEndesaPrecos.size();
		List<TableRows> tablerows = new ArrayList<>();
		String descr = "";
		double preco = 0;
		double desc = 0;
		double iva = 0;
		double potencia = 0;
		double potenciaPreco = 0;
		double potenciaDesc = 0;
		double potenciaIva = 0;
		int quant = 0;
		if (companhia.equalsIgnoreCase("Nabalia")) {
			tablerows.add(new TableRows("Juros de Mora", 0, 3.70, 0, 0, tipo));
		}
		potencia = ep.getPotencia();
		potenciaPreco = ep.getPotPreco();
		potenciaDesc = ep.getPotDesc();
		potenciaIva = ep.getPotIva();

		String electDate;
		if (len > 1)
			electDate = contEndesaPrecos.get(1).getData();
		else
			electDate = contEndesaPrecos.get(0).getData();
		double months = getMonths(date1, date2, electDate);
		String[] str = getDateLiteral(date1, date2, electDate);
		Integer[] days = getDays(date1, date2, electDate);

		for (int x = 0; x < len && days[x] > 0; x++) {
			ep = contEndesaPrecos.get(x);
			for (int i = 0; i < tipo; i++) {
				descr = "unknown";
				preco = 0;
				desc = 0;
				iva = 0;
				quant = 0;
				if (tipo == 1) {
					descr = "Simples";
					preco = ep.getSimples();
					desc = ep.getDescSimples();
					iva = ep.getIva();
					quant = ponta;
				} else if (tipo == 2) {
					if (i == 0) {
						descr = "Fora Vazio\t\t\t\t";
						preco = ep.getForaVazioBi();
						quant = foraVazio;
					} else {
						descr = "Vazio\t\t\t\t\t";
						preco = ep.getVazioBi();
						quant = vazio;
					}
					desc = ep.getDescBi();
					iva = ep.getIva();
				} else if (tipo == 3) {
					if (i == 0) {
						descr = "Ponta";
						preco = ep.getPontaTri();
						quant = ponta;
						desc = ep.getDescPontaTri();
					} else if (i == 1) {
						descr = "Cheias";
						preco = ep.getCheiasTri();
						quant = cheias;
						desc = ep.getDescCheiasTri();
					} else {
						descr = "Vazio";
						preco = ep.getVazioTri();
						quant = vazio;
						desc = ep.getDescVazioTri();
					}
					iva = ep.getIva();
				}
				String description = descr;
				if (tipo == 1)
					description = descr;
				if (companhia.equalsIgnoreCase("Nabalia")
						|| companhia.equalsIgnoreCase("EDP")) {
					description = descr + " medido";
				}
				description += str[x];
				tablerows.add(new TableRows(description, quant, preco, desc,
						iva, tipo));
			}
		}
		System.out.println("$$ getTableRows, len: " + len + ", potencia: "
				+ potencia);
		if (tipo == 3)
			desc = 0;
		for (int x = 0; x < len; x++) {
			if (days[x] > 0) {
				ep = contEndesaPrecos.get(x);
				String f = "Termo de Potencia (" + potencia + " kva) \t"
						+ str[x];
				tablerows.add(new TableRows(f, days[x], ep.getPotPreco(), desc,
						ep.getPotIva(), 0));
			}
		}
		for (int x = 0; x < len; x++) {
			if (days[x] > 0) {
				ep = contEndesaPrecos.get(x);
				String f = "Accesso as Redes\t" + str[x];
				tablerows.add(new TableRows(f, days[x], ep.getAccessoRede(),
						desc, ep.getPotIva(), 0));
			}
		}
		if (tipo != 3) {
			tablerows.add(new TableRows("Desconto de Boas Vindas", 1, ep
					.getDescBoasVindas() * -1, 0, ep.getPotIva(), tipo));
		}

		tablerows.add(new TableRows("Contribuicao Audiovisual", months, 2.85,
				0, 6, tipo));
		double precu = .35;
		if (tipo == 1)
			precu = .07;
		tablerows.add(new TableRows("Taxa Exploracao DGEG", months, precu, 0,
				23, tipo));
		if (estimado)
			tablerows.add(new TableRows("IEC Estimado", 0, .001, 0, 23, tipo));
		else
			tablerows.add(new TableRows("IEC Medido", 0, .001, 0, 23, tipo));

		return tablerows;
	}

	public boolean deleteContadorElect(int id) throws SQLException {
		DELETE_CONTADORELECT_SQL = DELETE_CONTADORELECT_SQL.replaceAll("XXXX",
				envirnmt);
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection
						.prepareStatement(DELETE_CONTADORELECT_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateContadorElect(contadorElect facturaelect)
			throws SQLException {
		UPDATE_CONTADORELECT_SQL = UPDATE_CONTADORELECT_SQL.replaceAll("XXXX",
				envirnmt);
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement ps = connection
						.prepareStatement(UPDATE_CONTADORELECT_SQL);) {
			ps.setString(1, facturaelect.getFatura());
			ps.setString(2, facturaelect.getParcela());
			ps.setString(3, facturaelect.getData1());
			ps.setString(4, facturaelect.getData2());
			ps.setString(5, facturaelect.getLeituradata());
			ps.setString(6, facturaelect.getEst());
			ps.setInt(7, facturaelect.getVazio());
			ps.setInt(8, facturaelect.getPonta());
			ps.setInt(9, facturaelect.getCheia());
			ps.setInt(10, facturaelect.getForavazio());
			ps.setDouble(11, facturaelect.getValor());
			ps.setInt(12, facturaelect.getId());
			System.out.println("----> updateFacturaElect sql: " + ps);
			rowUpdated = ps.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	private String[] getDateLiteral(String startDate, String endDate,
			String electDate) {
		String str[] = new String[2];
		str[0] = "";
		str[1] = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM", new Locale(
				"pt", "PT"));
		try {
			Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			Date elect = new SimpleDateFormat("yyyy-MM-dd").parse(electDate);

			if ((elect.compareTo(start) > 0) && (end.compareTo(elect) > 0)) {
				Calendar c = Calendar.getInstance();
				c.setTime(elect);
				c.add(Calendar.DATE, -1);
				Date electMinus1 = c.getTime();
				str[0] = "(" + formatter.format(start) + " a "
						+ formatter.format(electMinus1) + ")";
				str[1] = "(" + formatter.format(elect) + " a "
						+ formatter.format(end) + ")";
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	private double getMonths(String startDate, String endDate, String electDate) {
		long noOfDaysBetween = 0;
		Date start = null, end = null, elect = null;
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			elect = new SimpleDateFormat("yyyy-MM-dd").parse(electDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if ((elect.compareTo(start) > 0) && (end.compareTo(elect) > 0)) {
			long noOfDaysBetween1 = (elect.getTime() - start.getTime())
					/ (60 * 60 * 1000 * 24);
			System.out.println("noOfDaysBetween elect & start: "
					+ noOfDaysBetween1);
			long noOfDaysBetween2 = (end.getTime() - elect.getTime())
					/ (60 * 60 * 1000 * 24) + 1;
			System.out.println("noOfDaysBetween start & end: "
					+ noOfDaysBetween2);
			noOfDaysBetween = noOfDaysBetween1 + noOfDaysBetween2;
		} else {
			noOfDaysBetween = (end.getTime() - start.getTime())
					/ (60 * 60 * 1000 * 24) + 1;
			System.out.println("noOfDaysBetween start & end: "
					+ noOfDaysBetween);
		}
		float factor = (float) noOfDaysBetween / ((float) 365 / (float) 12);
		factor += .00005;
		System.out.println("noOfDaysBetween: " + noOfDaysBetween + ", factor: "
				+ factor);
		String res = String.format("%3.4f", factor);
		double result = Double.parseDouble(res);
		System.out.printf("double result: " + result);
		return result;
	}

	private Integer[] getDays(String startDate, String endDate, String electDate) {
		Integer[] days = new Integer[] { 0, 0 };
		long noOfDaysBetween = 0;
		Date start = null, end = null, elect = null;
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			elect = new SimpleDateFormat("yyyy-MM-dd").parse(electDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if ((elect.compareTo(start) > 0) && (end.compareTo(elect) > 0)) {
			long noOfDaysBetween1 = (elect.getTime() - start.getTime())
					/ (60 * 60 * 1000 * 24);
			days[0] = (int) noOfDaysBetween1;
			long noOfDaysBetween2 = (end.getTime() - elect.getTime())
					/ (60 * 60 * 1000 * 24) + 1;
			days[1] = (int) noOfDaysBetween2;
		} else {
			noOfDaysBetween = (end.getTime() - start.getTime())
					/ (60 * 60 * 1000 * 24) + 1;
			days[0] = (int) noOfDaysBetween;
			days[1] = 0;
		}
		return days;
	}

	public String minDate() throws SQLException {
		SELECT_ALL_CONTADORELECT = SELECT_ALL_CONTADORELECT.replaceAll("XXXX",
				envirnmt);
		String date = null;
		try (Connection connection = getConnection();

		// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection
						.prepareStatement(SELECT_ALL_CONTADORELECT);) {
			// System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				date = rs.getDate(1).toString();
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return date;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: "
						+ ((SQLException) e).getSQLState());
				System.err.println("Error Code: "
						+ ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}