package com.julio.contEndesa.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.julio.contEndesa.dao.contEndesaDAO;
import com.julio.contEndesaElect.model.TableRows;
import com.julio.contEndesaElect.model.contFactura;
import com.julio.contEndesaElect.model.contadorElect;
import com.julio.contEndesaElect.model.electricidadePrecos;

/**
 * ControllerServlet.java This servlet acts as a page controller for the
 * application, handling all requests from the contadorelect.
 * 
 * @email Ramesh Fadatare
 */

@WebServlet("/")
public class contEndesa extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private contEndesaDAO contEndesaDAO;
	String data1 = "1900-01-01";
	String data2 = "2900-01-01";
	String leituradata = "";
	String parcela = "";
	String companhia = "";
	String fatura = "";
	String est = "Phy";
	String valor = "0.00";
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date threeMonthsAgo = null;
	java.util.Date today = null;

	public void init() {
		contEndesaDAO = new contEndesaDAO();
		System.out.println("====================> init()");
		Calendar aCalendar = Calendar.getInstance();
		today = aCalendar.getTime();
		aCalendar.add(Calendar.MONTH, -3);
		aCalendar.set(Calendar.DATE, 1);
		threeMonthsAgo = aCalendar.getTime();
		data1 = dateFormat.format(threeMonthsAgo);
		data2 = dateFormat.format(today);
		leituradata = dateFormat.format(today);
		valor = "0.00";
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("====================> doPost  ");
		request.getSession().setAttribute("errMessage", "");

		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getServletPath();

		System.out.println("====================> doGet, action: " + action);
		request.getSession().setAttribute("errMessage", "");

		//
		response.setContentType("text/html");

		PrintWriter pwriter = response.getWriter();
		String userName = "No User", role = "No Role";
		// Reading cookies
		Cookie cookie = null;
		Cookie[] cookies = null;
		cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase("userName"))
					userName = cookie.getValue();
				if (cookie.getName().equalsIgnoreCase("role"))
					role = cookie.getValue();
			}
		} else {
			pwriter.println("<h2>No cookies founds</h2>");
		}
		if (!role.equals("Admin") && (!action.equalsIgnoreCase("/list"))
				&& (!action.equalsIgnoreCase("/ContadorElectServlet"))
				&& (!action.equalsIgnoreCase("/ElectPrecos"))
				&& (!action.equalsIgnoreCase("/getPrices"))
				&& (!action.equalsIgnoreCase("/storeAttributes"))
				&& (!action.equalsIgnoreCase("/"))) {
			pwriter.println("<h2>Only Adminstrator allowed to <mark>" + action
					+ "</mark> Contador de Electricidade</h2>" + userName + "/"
					+ role);
			//
			pwriter.close();
		}
		//
		else {

			try {
				switch (action) {
				case "/new":
					showNewForm(request, response);
					break;
				case "/insert":
					insertcontadorelect(request, response);
					break;
				case "/delete":
					deletecontadorelect(request, response);
					break;
				case "/edit":
					showEditForm(request, response);
					break;
				case "/update":
					updatecontadorelect(request, response);
					break;
				case "/list":
					listSomecontadorelect(request, response);
					break;
				case "/ContadorElectServlet":
					listSomecontadorelect(request, response);
					break;
				case "/buildRows":
					buildRows(request, response);
					break;
				case "/getPrices":
					getElectPrecos(request, response);
					break;
				case "/storeAttributes":
					storeAttributes(request, response);
					break;
				case "/checkInvNo":
					checkInvNo(request, response);
					break;

				default:
					listSomecontadorelect(request, response);
					break;
				}
			} catch (SQLException ex) {
				throw new ServletException(ex);
			}
		}
	}

	@SuppressWarnings("unused")
	private void listcontadorelect(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ServletException {
		System.out.println("====================> Listcontadorelect: ");

		List<contadorElect> listcontadorelect = contEndesaDAO
				.selectAllContadorElect(data1, data2, parcela);
		request.setAttribute("listcontadorelect", listcontadorelect);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contadorelect-list.jsp");
		dispatcher.forward(request, response);
	}

	private void listSomecontadorelect(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ServletException {
		String d1 = data1, d2 = data2, p = "";
		System.out.println("====================> ListSomecontadorelect: ");
		if (request.getParameter("Data1") != null)
			d1 = request.getParameter("Data1");
		if (request.getParameter("Data2") != null)
			d2 = request.getParameter("Data2");
		if (request.getParameter("Parcela") != null)
			p = request.getParameter("Parcela");

		request.getSession().setAttribute("D1Value", d1);
		request.getSession().setAttribute("D2Value", d2);
		request.getSession().setAttribute("PValue", p);

		List<contadorElect> listcontadorelect = contEndesaDAO
				.selectAllContadorElect(d1, d2, p);
		request.setAttribute("listcontadorelect", listcontadorelect);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contador-list.jsp");
		dispatcher.forward(request, response);
	}

	private void checkInvNo(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ServletException {
		String redirect = "new";
		int id = 0;
		String fatura = request.getParameter("fatura1");
		String msg = "Fatura: <strong>" + fatura + "</strong> já existe!";
		request.getSession().setAttribute("errMessage", "");
		if (contEndesaDAO.faturaExiste(fatura)) {
			System.out.println("...msg: " + msg);
			request.getSession().setAttribute("errMessage", msg);
		} else {
			request.getSession().setAttribute("FaturaValue", fatura);
		}
		if (request.getParameter("id") != null) {
			id = Integer.parseInt(request.getParameter("id"));
			redirect = "edit?id=" + id;
			response.sendRedirect(redirect);
		} else {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("contador-form.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void getElectPrecos(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ServletException {
		List<contFactura> listcontFactura = new ArrayList<>();
		String parcela = request.getParameter("parcela");
		String data1 = request.getParameter("data1");

		String fatura = request.getParameter("fatura1");
		String companhia = request.getParameter("companhia");
		String data2 = request.getParameter("data21");
		String leituradata = request.getParameter("leituradata1");
		String est = request.getParameter("est1");
		String valor = request.getParameter("totvalor1");

		Date date1 = new Date();
		listcontFactura = contEndesaDAO.getLastInv(parcela, data1);

		// contFactura contFactura = listcontFactura.get(0);
		String d1 = listcontFactura.get(0).getData2();
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(d1);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.add(Calendar.MONTH, 1);
		Date date2 = cal.getTime();
		String d2 = dateFormat.format(date2);
		cal.setTime(date1);
		// cal.add(Calendar.DATE, 1);
		date1 = cal.getTime();
		d1 = dateFormat.format(date1);
		int tipo = listcontFactura.get(0).getTipo();
		fatura = listcontFactura.get(0).getFatura();
		double potencia = listcontFactura.get(0).getPotencia();
		est = listcontFactura.get(0).getEst();
		System.out.println("## getElectPrecos, companhia: " + companhia
				+ ", potencia: " + potencia + ", d1: " + d1 + ", Est: " + est);
		if (companhia.isEmpty() || companhia.equalsIgnoreCase("")) {
			companhia = contEndesaDAO.getCompanhia(parcela);
			d1 = data1;
			d2 = data2;
		}
		List<electricidadePrecos> listelectprecos = contEndesaDAO
				.getElectPrecos(companhia, potencia, d1);

		request.getSession().setAttribute("FaturaValue", fatura);
		request.getSession().setAttribute("ParcelaValue", parcela);
		request.getSession().setAttribute("CompanhiaValue", companhia);
		request.getSession().setAttribute("Data1Value", d1);
		request.getSession().setAttribute("Data2Value", d2);
		request.getSession().setAttribute("LeituradataValue", d2);
		request.getSession().setAttribute("EstValue", est);
		request.getSession().setAttribute("ValorValue", valor);

		request.setAttribute("listelectprecos", listelectprecos);
		boolean estimado = est.equalsIgnoreCase("Est");

		List<TableRows> tablerows = contEndesaDAO.getTableRows(tipo, companhia,
				listelectprecos, 0, 0, 0, 0, d1, d2, estimado);
		for (int i = 0; i < tablerows.size(); i++) {
			TableRows tr = tablerows.get(i);
		}
		request.setAttribute("tablerows", tablerows);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contador-form.jsp");
		dispatcher.forward(request, response);
	}

	private void buildRows(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ServletException {
		int id = 0;
		if (request.getSession().getAttribute("id") != null) {
			String idStr = request.getSession().getAttribute("id").toString();
			id = Integer.parseInt(idStr);
			List<contadorElect> existingcontadorelect = contEndesaDAO
					.selectContadorElect(id);
			request.setAttribute("existingcontadorelect", existingcontadorelect);
		}

		List<contFactura> listcontFactura = new ArrayList<>();
		String parcela = request.getSession().getAttribute("ParcelaValue")
				.toString();
		String data1 = request.getParameter("data1");
		listcontFactura = contEndesaDAO.getLastInv(parcela, data1);
		String companhia = request.getParameter("companhia");
		String data2 = request.getParameter("data21");
		String leituradata = request.getParameter("leituradata1");
		String valor = request.getParameter("totvalor1");
		String est = request.getParameter("est1");
		boolean estimado = est.equalsIgnoreCase("Est");
		request.getSession().setAttribute("EstValue", est);
		double potencia = listcontFactura.get(0).getPotencia();
		int tipo = listcontFactura.get(0).getTipo();

		List<electricidadePrecos> listelectprecos = contEndesaDAO
				.getElectPrecos(companhia, potencia, data1);

		request.setAttribute("listelectprecos", listelectprecos);

		List<TableRows> tablerows = contEndesaDAO.getTableRows(tipo, companhia,
				listelectprecos, 0, 0, 0, 0, data1, data2, estimado);
		request.setAttribute("tablerows", tablerows);
		request.getSession().setAttribute("Data1Value", data1);
		request.getSession().setAttribute("Data2Value", data2);
		String fatura = request.getParameter("fatura1");
		request.getSession().setAttribute("FaturaValue", fatura);

		request.getSession().setAttribute("LeituradataValue", leituradata);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contador-form.jsp");
		dispatcher.forward(request, response);
	}

	private void storeAttributes(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException,
			ServletException {

		if (request.getParameter("fatura") != null)
			fatura = request.getParameter("fatura");
		if (request.getParameter("parcela") != null)
			parcela = request.getParameter("parcela");
		if (request.getParameter("companhia") != null)
			companhia = request.getParameter("companhia");
		if (request.getParameter("data1") != null)
			data1 = request.getParameter("data1");
		if (request.getParameter("data2") != null)
			data2 = request.getParameter("data2");
		if (request.getParameter("leituradata") != null)
			leituradata = request.getParameter("leituradata");
		if (request.getParameter("est") != null)
			est = request.getParameter("est");
		if (request.getParameter("valor") != null)
			valor = request.getParameter("valor");
		request.getSession().setAttribute("FaturaValue", fatura);
		request.getSession().setAttribute("ParcelaValue", parcela);
		request.getSession().setAttribute("CompanhiaValue", companhia);
		request.getSession().setAttribute("Data1Value", data1);
		request.getSession().setAttribute("Data2Value", data2);
		request.getSession().setAttribute("LeituradataValue", leituradata);
		request.getSession().setAttribute("EstValue", est);
		request.getSession().setAttribute("ValorValue", valor);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contador-form.jsp");
		dispatcher.forward(request, response);

	}

	private void showNewForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Calendar aCalendar = Calendar.getInstance();
		today = aCalendar.getTime();
		aCalendar.add(Calendar.MONTH, -3);
		aCalendar.set(Calendar.DATE, 1);
		threeMonthsAgo = aCalendar.getTime();
		data1 = dateFormat.format(threeMonthsAgo);
		data2 = dateFormat.format(today);
		leituradata = dateFormat.format(today);

		if (request.getSession().getAttribute("D1Value") != null) {
			data1 = request.getSession().getAttribute("D1Value").toString();
			data2 = request.getSession().getAttribute("D2Value").toString();
			// parcela = request.getSession().getAttribute("PValue").toString();
		}

		if (request.getSession().getAttribute("fatura") != null)
			fatura = request.getSession().getAttribute("FaturaValue")
					.toString();
		if (request.getSession().getAttribute("parcela") != null)
			parcela = request.getSession().getAttribute("ParcelaValue")
					.toString();
		if (request.getSession().getAttribute("companhia") != null)
			companhia = request.getSession().getAttribute("CompanhiaValue")
					.toString();
		if (request.getSession().getAttribute("data1") != null)
			data1 = request.getSession().getAttribute("Data1Value").toString();
		if (request.getSession().getAttribute("data2") != null)
			data2 = request.getSession().getAttribute("Data2Value").toString();
		if (request.getSession().getAttribute("leituradata") != null)
			leituradata = request.getSession().getAttribute("LeituradataValue")
					.toString();
		if (request.getSession().getAttribute("est") != null)
			est = request.getSession().getAttribute("EstValue").toString();
		if (request.getSession().getAttribute("valor") != null)
			valor = request.getSession().getAttribute("ValorValue").toString();
		request.getSession().setAttribute("errMessage", "");
		request.getSession().setAttribute("FaturaValue", fatura);
		request.getSession().setAttribute("ParcelaValue", parcela);
		request.getSession().setAttribute("CompanhiaValue", companhia);
		request.getSession().setAttribute("Data1Value", data1);
		request.getSession().setAttribute("Data2Value", data2);
		request.getSession().setAttribute("LeituradataValue", leituradata);
		request.getSession().setAttribute("EstValue", est);
		request.getSession().setAttribute("ValorValue", valor);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contador-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ServletException, IOException {
		// request.getSession().setAttribute("errMessage", "");
		int id = Integer.parseInt(request.getParameter("id"));
		List<contadorElect> existingcontadorelect = contEndesaDAO
				.selectContadorElect(id);
		request.getSession().setAttribute("id", id);
		int vazio = existingcontadorelect.get(0).getVazio();
		int foraVazio = existingcontadorelect.get(0).getForavazio();
		int cheia = existingcontadorelect.get(0).getCheia();
		int ponta = existingcontadorelect.get(0).getPonta();
		int tipo = existingcontadorelect.get(0).getTipo();
		boolean estimado = existingcontadorelect.get(0).getEst()
				.equalsIgnoreCase("Est");

		request.getSession().setAttribute("FaturaValue",
				existingcontadorelect.get(0).getFatura());
		parcela = existingcontadorelect.get(0).getParcela();
		request.getSession().setAttribute("ParcelaValue",
				existingcontadorelect.get(0).getParcela());

		request.getSession().setAttribute("CompanhiaValue",
				existingcontadorelect.get(0).getCompanhia());

		request.getSession().setAttribute("Data1Value",
				existingcontadorelect.get(0).getData1());
		request.getSession().setAttribute("Data2Value",
				existingcontadorelect.get(0).getData2());
		String data1 = existingcontadorelect.get(0).getData1();
		String data2 = existingcontadorelect.get(0).getData2();
		String leituradata = existingcontadorelect.get(0).getLeituradata();
		request.getSession().setAttribute("LeituradataValue",
				existingcontadorelect.get(0).getLeituradata());
		request.getSession().setAttribute("EstValue",
				existingcontadorelect.get(0).getEst());
		request.getSession().setAttribute("ValorValue",
				existingcontadorelect.get(0).getValor());
		request.setAttribute("existingcontadorelect", existingcontadorelect);

		List<electricidadePrecos> listelectprecos = contEndesaDAO
				.getElectPrecos(parcela, data1);

		request.setAttribute("listelectprecos", listelectprecos);

		electricidadePrecos ep = listelectprecos.get(0);
		List<TableRows> tablerows = contEndesaDAO.getTableRows(tipo, companhia,
				listelectprecos, vazio, foraVazio, ponta, cheia,
				existingcontadorelect.get(0).getData1(), existingcontadorelect
						.get(0).getData2(), estimado);
		for (int i = 0; i < tablerows.size(); i++) {
			TableRows tr = tablerows.get(i);
		}
		request.setAttribute("tablerows", tablerows);
		request.setAttribute("id", id);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("contador-form.jsp");
		dispatcher.forward(request, response);

	}

	private void insertcontadorelect(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException {
		String fatura = request.getParameter("fatura");
		String msg = "Fatura: " + fatura + " já existe!";
		request.getSession().setAttribute("errMessage", "");
		if (contEndesaDAO.faturaExiste(fatura)) {
			System.out.println("...msg: " + msg);
			request.getSession().setAttribute("errMessage", msg);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("contador-form.jsp");
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			try {
				String parcela = request.getSession()
						.getAttribute("ParcelaValue").toString();
				String companhia = request.getSession()
						.getAttribute("CompanhiaValue").toString();
				String data1 = request.getSession().getAttribute("Data1Value")
						.toString();
				String data2 = request.getSession().getAttribute("Data2Value")
						.toString();
				String leituradata = request.getSession()
						.getAttribute("LeituradataValue").toString();
				String est = request.getSession().getAttribute("EstValue")
						.toString();
				String valorValue = request.getSession()
						.getAttribute("ValorValue").toString();
				System.out.println("|||----> insertcontadorelect  fatura: "
						+ fatura + ", parcela: " + parcela + ", data1: "
						+ data1 + ", data2: " + data2 + ", leituradata: "
						+ leituradata + ", est: " + est + ", valorValue: "
						+ valorValue);
				String value, s;
				String[] quant = request.getParameterValues("quant");
				int vazio = 0;
				int ponta = 0;
				int cheia = 0;
				int foraVazio = 0;
				double valor = 0.0;
				value = request.getParameter("vazio");
				s = value.replaceAll("[^0-9.]", "");
				vazio = Integer.parseInt(s);

				value = request.getParameter("ponta");
				s = value.replaceAll("[^0-9.]", "");
				ponta = Integer.parseInt(s);

				value = request.getParameter("cheias");
				s = value.replaceAll("[^0-9.]", "");
				cheia = Integer.parseInt(s);

				value = request.getParameter("foravazio");
				s = value.replaceAll("[^0-9.]", "");
				foraVazio = Integer.parseInt(s);

				System.out.println("%&%& vazio: " + vazio + ", foravazio: "
						+ foraVazio + ", ponta: " + ponta + ", cheias: "
						+ cheia + ", valor: " + valor);

				String grandtot = request.getParameter("grandtot");
				String totiva = request.getParameter("totiva");

				// s = grandtot.replaceAll("[^0-9.]", "");
				double gtot = Double.parseDouble(grandtot);
				// s = totiva.replaceAll("[^0-9.]", "");
				double tiva = Double.parseDouble(totiva);

				valor = gtot + tiva;

				contadorElect newcontadorelect = new contadorElect(fatura,
						parcela, data1, data2, leituradata, est, vazio, ponta,
						cheia, foraVazio, valor, companhia, 0);
				contEndesaDAO.insertContadorElect(newcontadorelect);
			} catch (Exception ex) {
				request.setAttribute("errMessage", ex.getMessage());
				System.out.println("Error occured when parsing data.");
			}
			response.sendRedirect("list");
		}
	}

	private void updatecontadorelect(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException {
		request.getSession().setAttribute("errMessage", "");
		System.out.println("====================> updatecontadorelect");
		String idString = null;
		String redirect = "list";
		int id = 0;
		try {
			idString = request.getParameter("id");
			String parcela;
			id = Integer.parseInt(idString);
			String fatura = request.getParameter("fatura");
			if ((request.getParameter("parcela") != null)
					&& (request.getParameter("parcela") != ""))
				parcela = request.getParameter("parcela");
			else
				parcela = request.getSession().getAttribute("ParcelaValue")
						.toString();

			String data1 = request.getParameter("data1");
			String data2 = request.getParameter("data2");
			String leituradata = request.getParameter("leituradata");
			String est = request.getParameter("est");

			String value = request.getParameter("vazio");
			String s = value.replaceAll("[^0-9.]", "");
			int vazio = Integer.parseInt(s);

			value = request.getParameter("ponta");
			s = value.replaceAll("[^0-9.]", "");
			int ponta = Integer.parseInt(s);

			value = request.getParameter("cheias");
			s = value.replaceAll("[^0-9.]", "");
			int cheia = Integer.parseInt(s);

			value = request.getParameter("foravazio");
			s = value.replaceAll("[^0-9.]", "");
			int foraVazio = Integer.parseInt(s);

			value = request.getParameter("totvalor");
			s = value.replaceAll("[^0-9.]", "");
			double valor = Double.parseDouble(s);
			System.out.println("%&%& vazio: " + vazio + ", foravazio: "
					+ foraVazio + ", ponta: " + ponta + ", cheias: " + cheia
					+ ", valor: " + valor);
			String companhia = request.getParameter("companhia");
			contadorElect book = new contadorElect(id, fatura, parcela, data1,
					data2, leituradata, est, vazio, ponta, cheia, foraVazio,
					valor, companhia, 0);
			contEndesaDAO.updateContadorElect(book);

		} catch (Exception ex) {
			System.out.println("Error occured when parsing data, Message: "
					+ ex.getMessage());
			request.getSession().setAttribute("errMessage",
					ex.getMessage() + ", Please re-enter quantities");
			redirect = "edit?id=" + id;
		}
		response.sendRedirect(redirect);
	}

	private void deletecontadorelect(HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException {
		System.out.println("====================> deletecontadorelect");
		int id = Integer.parseInt(request.getParameter("id"));
		contEndesaDAO.deleteContadorElect(id);
		// System.out.println("-----> Deleted no: " + id);
		response.sendRedirect("list");

	}

	String removeLeadingZeroes(String s) {
		int index;
		for (index = 0; index < s.length(); index++) {
			if (s.charAt(index) >= '0' && s.charAt(index) <= '9') {
				break;
			}
		}
		return s.substring(index);
	}

	String removeTrailingZeroes(String s) {
		int index;
		for (index = s.length() - 1; index >= 0; index--) {
			if (s.charAt(index) >= '0' && s.charAt(index) <= '9') {
				break;
			}
		}
		return s.substring(index);
	}
}