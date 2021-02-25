<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@page import="java.util.Calendar"%><%@page import="java.text.*"%>
<%@page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%
	ResultSet resultset = null;
%>

<html>

<head>
<body>
	<script type="text/javascript">
		function load() {
			alert("load event detected!");
			var date1;
			var date2;
			var parcela;
			var currentDate;
			var lastMonth;
			var fatura;
			var est;
			var companhia;
			var firstTime;
			alert("load event detected! 2");
			if (localStorage.getItem("firstTime") == null) {
				firstTime = "yes";
			} else {
				firstTime = localStorage.getItem("firstTime")
			}
			localStorage.setItem("firstTime", "no");
			alert("load event detected! 3 " + firstTime);
			if (firstTime != "no") {
				localStorage.setItem("first_time", "1");
				alert("First load event detected!");

				date1 = "";
				date2 = "";
				parcela = "<--Parcela-->";
				currentDate = "";
				lastMonth = "";
				fatura = "FT 0001/1";
				est = "Real";
				companhia = "";

				dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				aCalendar = Calendar.getInstance();
				java.util.Date
				today = aCalendar.getTime();
				currentDate = dateFormat.format(today);

				aCalendar.add(Calendar.MONTH, -1);
				java.util.Date
				oneMonthAgo = aCalendar.getTime();
				lastMonth = dateFormat.format(oneMonthAgo);

				request.setAttribute("fatura", fatura);
				request.setAttribute("companhia", companhia);
				request.setAttribute("data1", lastMonth);
				request.setAttribute("data2", currentDate);
				request.setAttribute("leituradata", currentDate);
				request.setAttribute("est", est);
				request.setAttribute("parcela", parcela);
			}
		}

		function ValidateNum(field) {
			//			var pattern = /^-?[0-9]+(.[0-9]{1,2})?$/;
			var pattern = /^-?\d{0,4}(?:[.,]\d{1,5})?$/
			var value = field.value;
			var name = field.name;
			if (value.match(pattern) == null) {
				window.alert(" !!!! " + name + ' : format is wrong');
				field.focus();
			}
			return false;
		}

		function calculate(field) {
			return false;
			var value = field.value;
			var name = field.name;
			var r = 0;
			if (name = "quant")
				r = 0;
			else if (name === "foraVazioBi")
				r = 1;
			else if (name === "vazioTri")
				r = 2;
			else if (name === "pontaTri")
				r = 3;
			else
				r = 4;
			var preco = document.getElementById("preco").value;
			var descPercent = document.getElementById("desc").value;
			var ivaPercent = document.getElementById("BiIva").value;

			window.alert(" ---- vazioBiPreco: " + preco);
			var valor = value * preco;
			window.alert(" ---- valor: " + valor);
			document.getElementById('vazioBiValor').value = valor.toFixed(2);
			var desc = valor * (descPercent / 100);
			document.getElementById('vazioBiDesc').value = desc.toFixed(2);
			var tot = valor - desc;
			document.getElementById('vazioBiTot').value = tot.toFixed(2);
			var iva = tot * (ivaPercent / 100);
			document.getElementById('vazioBiIva').value = iva.toFixed(2);
			return false;
		}

		function tableloop(field) {
			var row = field.closest('tr').rowIndex;
			var table = document.getElementById('myTable');

			var day_start = new Date(document.getElementById("data1").value);

			var day_end = new Date(document.getElementById("data2").value);
			var total_days = ((day_end - day_start) / (1000 * 60 * 60 * 24)) + 1;
			var months = Math.ceil(total_days / 30);

			var quant = document.getElementById("myTable").rows[row].cells[1]
					.querySelector('input').value;
			if (isNaN(quant) || quant == "")
				quant = 0;
			var preco = document.getElementById("myTable").rows[row].cells[2]
					.querySelector('input').value;
			if (isNaN(preco) || preco == "")
				preco = 0;
			var descP = document.getElementById("myTable").rows[row].cells[7]
					.querySelector('input').value;
			if (isNaN(descP) || descP == "")
				descP = 0;
			var ivaP = document.getElementById("myTable").rows[row].cells[8]
					.querySelector('input').value;
			if (isNaN(ivaP) || ivaP == "")
				ivaP = 0;
			var valor = quant * preco;
			var round = Math.round(valor * 100);
			valor = round / 100;
			var desc = (valor * descP / 100) * -1;
			var tot = valor + desc;
			var iva = tot * ivaP / 100;

			document.getElementById("myTable").rows[row].cells[3]
					.querySelector('input').value = valor.toFixed(2);
			document.getElementById("myTable").rows[row].cells[4]
					.querySelector('input').value = desc.toFixed(2);
			document.getElementById("myTable").rows[row].cells[5]
					.querySelector('input').value = tot.toFixed(2);
			document.getElementById("myTable").rows[row].cells[6]
					.querySelector('input').value = iva.toFixed(2);

			var total = 0;
			var totiva = 0;
			var t1 = 0;
			var t2 = 0;

			var qty = 0;
			var vazio = 0;
			var foravazio = 0;
			var ponta = 0;
			var cheias = 0;
			var simples = 0;
			for (var i = 1; i < table.rows.length - 1; i++) {
				var descr = document.getElementById("myTable").rows[i].cells[0]
						.querySelector('input').value;
				if (descr.indexOf("Fora Vazio") != -1) {
					qty = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;
					if (isNaN(qty) || qty == "")
						qty = 0;
					foravazio = foravazio + parseFloat(qty);
				} else if (descr.indexOf("Vazio") != -1
						&& descr.indexOf("Fora Vazio") == -1) {
					qty = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;
					if (isNaN(qty) || qty == "")
						qty = 0;
					vazio = vazio + parseFloat(qty);
				} else if (descr.indexOf("Ponta") != -1) {
					qty = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;
					if (isNaN(qty) || qty == "")
						qty = 0;
					ponta = ponta + parseFloat(qty);
				} else if (descr.indexOf("Cheias") != -1) {
					qty = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;
					if (isNaN(qty) || qty == "")
						qty = 0;
					cheias = cheias + parseFloat(qty);
				} else if (descr.indexOf("Simples") != -1) {
					qty = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;
					if (isNaN(qty) || qty == "")
						simples = 0;
					simples = simples + parseFloat(qty);
				}
				if (descr.indexOf("Gest") > -1
						|| descr.indexOf("Audiovisual") > -1
						|| descr.indexOf("DGEG") > -1
						|| descr.indexOf("Potencia") > -1
						|| descr.indexOf("Redes") > -1
						|| descr.indexOf("Boas Vindas") > -1
						|| descr.indexOf("IEC") > -1) {
					var quantt = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;
					if (quantt == null || quantt == "" || quantt == 0) {
						document.getElementById("myTable").rows[i].cells[1]
								.querySelector('input').value = months
								.toFixed(0);
					}
					var quant = 0;
					if (descr.indexOf("IEC") > -1) {
						quant = foravazio + vazio + ponta + cheias + simples;
						document.getElementById("myTable").rows[i].cells[1]
								.querySelector('input').value = quant
								.toFixed(4);
					}
					quant = document.getElementById("myTable").rows[i].cells[1]
							.querySelector('input').value;

					if (isNaN(quant))
						quant = 0;
					var preco = document.getElementById("myTable").rows[i].cells[2]
							.querySelector('input').value;
					if (isNaN(preco))
						preco = 0;
					var valor = parseFloat(quant) * parseFloat(preco);
					var round = Math.round(valor * 100);
					valor = round / 100;
					var descp = document.getElementById("myTable").rows[i].cells[7]
							.querySelector('input').value;
					if (isNaN(descp))
						descp = 0;
					var desc = (valor * parseFloat(descp) / 100) * -1;
					var tot = valor + desc;

					if (descr.indexOf("Audiovisual") != -1) {
						iva = tot * 6 / 100
					} else {
						iva = tot * 23 / 100;
					}

					document.getElementById("myTable").rows[i].cells[3]
							.querySelector('input').value = valor.toFixed(2);
					document.getElementById("myTable").rows[i].cells[4]
							.querySelector('input').value = desc.toFixed(2);
					document.getElementById("myTable").rows[i].cells[5]
							.querySelector('input').value = tot.toFixed(2);
					document.getElementById("myTable").rows[i].cells[6]
							.querySelector('input').value = iva.toFixed(2);
				}
				t1 = document.getElementById("myTable").rows[i].cells[5]
						.querySelector('input').value;
				if (isNaN(t1) || t1 == "")
					t1 = 0;
				t2 = document.getElementById("myTable").rows[i].cells[6]
						.querySelector('input').value;
				if (isNaN(t2) || t2 == "")
					t2 = 0;
				total = total + parseFloat(t1);
				totiva = totiva + parseFloat(t2);
			}

			var invtot = 0.0;

			invtot = total + totiva;
			document.getElementById("grandtot").value = total.toFixed(2);
			document.getElementById("totiva").value = totiva.toFixed(2);
			document.getElementById("totvalor").value = invtot.toFixed(2);

			document.getElementById("foravazio").value = foravazio.toFixed(0);
			document.getElementById("vazio").value = vazio.toFixed(0);
			document.getElementById("ponta").value = ponta.toFixed(0);
			document.getElementById("cheias").value = cheias.toFixed(0);

			return false;
		}

		function adjust(field) {
			var name = field.name;
			var value = field.value;

			var table = document.getElementById('myTable');
			var adjVal = document.getElementById("adjVal").value;
			if (isNaN(adjVal) || adjVal == "")
				adjVal = 0;

			var adjIva = document.getElementById("adjIva").value;
			if (isNaN(adjIva) || adjIva == "")
				adjIva = 0;

			var t1 = 0;
			var t2 = 0;
			var total = 0;
			var totiva = 0;
			var invtot = 0.0;

			for (var i = 1; i < table.rows.length - 1; i++) {

				t1 = document.getElementById("myTable").rows[i].cells[5]
						.querySelector('input').value;
				if (isNaN(t1) || t1 == "")
					t1 = 0;
				t2 = document.getElementById("myTable").rows[i].cells[6]
						.querySelector('input').value;
				if (isNaN(t2) || t2 == "")
					t2 = 0;
				total = total + parseFloat(t1);
				totiva = totiva + parseFloat(t2);
			}

			invtot = total + totiva + parseFloat(adjVal) + parseFloat(adjIva);
			document.getElementById("grandtot").value = total.toFixed(2);
			document.getElementById("totiva").value = totiva.toFixed(2);
			document.getElementById("totvalor").value = invtot.toFixed(2);
		}
		function validateCurrency(field) {
			var pattern = /^-?[0-9]+(.[0-9]{1,2})?$/;
			var text = field.value;
			var name = field.name;
			if (text.match(pattern) == null) {
				window.alert(name + ' : format is wrong');
				field.focus();
				//			return false;
			}
			//			return false;
		}

		function validateDouble(field) {
			var pattern = /^-?[0-9]+(.[0-9]{1,5})?$/;
			var text = field.value;
			var name = field.name;
			if (text.match(pattern) == null) {
				window.alert(name + ' : format is wrong');
				field.focus();
				//			return false;
			}
			//			return false;
		}
		function selFunction(sel) {
			var selText = sel.options[sel.selectedIndex].text;
			var selValue = sel.options[sel.selectedIndex].value;

			document.getElementById("companhia").value = selValue;
			document.getElementById("parcela").value = selText;
			document.getElementById("parcel").value = selText;

			document.forms["form1"].action = "getPrices";
			document.forms["form1"].submit();
		}
		function checkInvNo() {
			document.forms["form1"].action = "checkInvNo";
			document.forms["form1"].submit();
		}
		function buildRows() {
			document.forms["form1"].action = "buildRows";
			document.forms["form1"].submit();
		}
		function storeAttributes() {
			var selText = document.getElementById("parcelalist").value;
			document.getElementById("parcela").value = selText;
			//			document.forms["form1"].action = "storeAttributes";
			//			document.forms["form1"].submit();
			var inv = document.getElementById("factura").value;
			document.getElementById("fatur").value = inv;

		}

		function storeFatura() {
			var fatura = document.getElementById("fatura").value;
			document.getElementById("fatur").value = fatura;
		}

		function populateCustomerId() {
			var selectBox = document.getElementById('selectBox');

			/* selected value of dropdown */
			var selectedCustomerId = selectBox.options[selectBox.selectedIndex].value;

			/* selected value set to input field */
			document.getElementById('customerId').value = selectedCustomerId;
		}

		function setAction(form) {
			var id = document.getElementById("idd").value;
			var fatura = document.getElementById("fatura").value;
			var data1 = document.getElementById("data1").value;
			var data2 = document.getElementById("data2").value;
			var leituradata = document.getElementById("leituradata").value;
			var est = document.getElementById("est").value;
			var parcela = document.getElementById("parcela").value;
			var totvalor = document.getElementById("totvalor").value;
			var companhia = document.getElementById("companhia").value;
			if (isNaN(id) || id == "") {
				document.form2.action = "insert";
			} else {
				document.getElementById("fatur").value = fatura;
				document.getElementById("dat1").value = data1;
				document.getElementById("dat2").value = data2;
				document.getElementById("leituradat").value = leituradata;
				document.getElementById("es").value = est;
				document.getElementById("parcel").value = parcela;
				document.getElementById("totvalo").value = totvalor;
				document.getElementById("companhi").value = companhia;
				document.form2.action = "update?id=" + id;
			}
			//return true;
		}
		function addRow(selValue) {
			alert("In addRow, selected value: " + selValue);

			var empTab = document.getElementById('myTable');

			var rowCnt = empTab.rows.length; // get the number of rows.
			alert("row count: " + rowCnt);
			var tr = empTab.insertRow(rowCnt); // table row.
			tr = empTab.insertRow(rowCnt);

			for (var c = 0; c < 7; c++) {
				var td = document.createElement('td'); // TABLE DEFINITION.
				td = tr.insertCell(c);

				if (c == 0) { // if its the first column of the table.
					// add a button control.
					var button = document.createElement('input');

					// set the attributes.
					button.setAttribute('type', 'button');
					button.setAttribute('value', 'Remove');

					// add button's "onclick" event.
					button.setAttribute('onclick', 'removeRow(this)');

					td.appendChild(button);
				} else {
					// the 2nd, 3rd and 4th column, will have textbox.
					var ele = document.createElement('input');
					ele.setAttribute('type', 'text');
					ele.setAttribute('value', selValue);

					td.appendChild(ele);
				}
			}
		}

		function selectText() {
			var textbox = document.getElementById("fatura");
			textbox.focus();
			textbox.scrollIntoView();
		}
	</script>

	<title>Factura Management Application</title>


	<link rel="stylesheet"
		href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
		integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
		crossorigin="anonymous">
	<style>
table {
	border-collapse: collapse;
}

th,td {
	text-align: centre;
	padding: 5px;
}

tr:nth-child(even) {
	background-color: #f2f2f2;
}

.green {
	background-color: green
}

.red {
	background-color: red
}

.grey {
	background-color: grey
}

.LightGrey {
	background-color: LightGrey
}
</style>
</head>

<header>
	<nav class="navbar navbar-expand-md navbar-dark"
		style="background-color: tomato">
		<div>
			<a href="https://www.javaguides.net" class="navbar-brand">
				Factura Management App </a>
		</div>

		<ul class="navbar-nav">
			<li><a href="<%=request.getContextPath()%>/list"
				class="nav-link">Facturas</a></li>
		</ul>
	</nav>
</header>
<%
	String invoice = "";
%>
<br>
<!-- 	<c:if test="${contadorelect != null}">
		<form action="update" method="post">
	</c:if>
	<c:if test="${contadorelect == null}">  -->
<form name="form1" id="form1" action="getPrices" method="post">
	<!-- 	</c:if> -->
	<caption>
		<h2>
			<c:if test="${existingcontadorelect != null}">
                                    Editar Factura de Electricidade
                                </c:if>
			<c:if test="${existingcontadorelect == null}">
                                    Nova Factura de Electricidade
                                </c:if>
		</h2>
	</caption>
	<p>
	<div style="color: red">
		<%=request.getSession().getAttribute("errMessage")%>
	</div>
	</p>
	<c:if test="${existingcontadorelect != null}">
		<input type="hidden" name="id" id="id"
			value="<c:out value='<%=request.getAttribute("id")%>' />" />
	</c:if>
	<table class="table table-bordered">
		<%
			
		%>
		<thead>
			<tr>
				<th>Parcela</th>
				<th>Factura#</th>
				<th>Data Inicio</th>
				<th>Data final</th>
				<th>Leitura</th>
				<th>Est</th>
				<th style="text-align: center;">Valor</th>
				<th>Companhia</th>
			</tr>
		</thead>
		<tbody onload="tableloop(quant);">

			<tr>
				<input type="hidden" name="parcela" id="parcela">
				<%
					try {
						Class.forName("com.mysql.jdbc.Driver").newInstance();
						Connection connection = DriverManager
								.getConnection("jdbc:mysql://localhost:3306/agro?useSSL=false&user=root&password=j301052");
						Statement statement = connection.createStatement();

						resultset = statement
								.executeQuery("SELECT Nome, Companhia, tipo FROM agro.parcela where Companhia !='' ");
				%>

				<td><select id="parcelalist" name="parcelalist" size="1"
					style="width: 120px"
					<c:if test="${existingcontadorelect != null}">
                                   disabled="true"
                                </c:if>
					onChange="selFunction(this);storeFatura();selectText()">
						<option value="" selected disabled hidden><%=request.getSession().getAttribute("ParcelaValue")%></option>
						<%
							while (resultset.next()) {
						%>

						<option value="<%=resultset.getString(2)%>"><%=resultset.getString(1)%></option>
						<%
							}
						%>
				</select></td>
				<%
					} catch (Exception e) {
						out.println("wrong entry" + e);
					}
				%>
				<td><input type="text" style="width: 200px"
					value="<c:out value='<%=request.getSession().getAttribute("FaturaValue")%>' />"
					name="fatura1" id="fatura" size="18" required="required"
					onChange="storeFatura();checkInvNo()"></td>
				<td><input type="date"
					value="<c:out value='<%=request.getSession().getAttribute("Data1Value")%>' />"
					name="data1" id="data1" style="width: 144px" required="required"
					onfocusout=buildRows();></td>

				<td><input type="date"
					value="<c:out value='<%=request.getSession().getAttribute("Data2Value")%>' />"
					name="data21" id="data2" style="width: 144px" required="required"
					onfocusout=buildRows();></td>

				<td><input type="date"
					value="<c:out value='<%=request.getSession().getAttribute("LeituradataValue")%>' />"
					name="leituradata1" id="leituradata" style="width: 144px"
					required="required"></td>
				<td><input type="text"
					value="<c:out value='<%=request.getSession().getAttribute("EstValue")%>' />"
					name="est1" id="est" style="width: 40px" required="required"
					onfocusout=buildRows();></td>
				<td class="LightGrey"><input type="text" tabindex="-1"
					readonly="readonly"
					value="<fmt:formatNumber minFractionDigits="2" value = '<%=request.getSession().getAttribute("ValorValue")%>' type="number" />"
					style="text-align: right; width: 100px" name="totvalor1"
					id="totvalor" onChange="validateCurrency(this);"
					required="required" onfocusout=storeAttributes();></td>
				<td class="LightGrey"><input tabindex="-1" type="text"
					readonly="readonly"
					value="<%=request.getSession().getAttribute("CompanhiaValue")%>"
					name="companhia" id="companhia" style="width: 75px"
					required="required" onfocusout=storeAttributes();></td>
				<td></td>
			</tr>
		</tbody>
	</table>
</form>
<SCRIPT type="text/javascript">
    document.form1.fatura.focus();
</SCRIPT>
<form name="form2" id="form2" method="post" action="insert"
	onsubmit="return setAction(this)">
	<table style="width: 100%;" id="myTable">
		<tr>
			<th style="text-align: left; width: 10%;">Descrição</th>
			<th style="text-align: right; width: 10%;">Quant</th>
			<th style="text-align: right; width: 10%;">Preco</th>
			<th style="text-align: right; width: 10%;">Valor</th>
			<th style="text-align: right; width: 10%;">Desc</th>
			<th style="text-align: right; width: 10%;">Total</th>
			<th style="text-align: right; width: 10%;">iva</th>
		</tr>
		<c:forEach var="rows" items="${tablerows}">
			<tr>
				<td><input type="text" tabindex="-1" value="${rows.descr}"
					style="text-align: left; width: 270px" /></td>

				<td><input type="text"
					value="<fmt:formatNumber minFractionDigits="4" type='number' value="${rows.quant}"/>"
					name="quant" id="quant" oninput="tabloop(this);"
					onchange="ValidateNum(this);tableloop(this);" required="required"
					style="text-align: right; width: 120px"></td>
				<td><input type="text" tabindex="-1"
					value="<fmt:formatNumber minFractionDigits="6" value="${rows.preco}"  type="number"/>"
					name="preco" id="preco"
					onchange="validateDouble(this);tableloop(this);"
					style="text-align: right; width: 120px"></td>
				<td class="LightGrey"><input type="text" tabindex="-1"
					value="<fmt:formatNumber  value="" type="currency"/>" name="valor"
					id="valor" readonly style="text-align: right; width: 120px"></td>
				<td class="LightGrey"><input type="text" tabindex="-1"
					value="<fmt:formatNumber  value="" type="currency" />" name="descV"
					id="descV" readonly style="text-align: right; width: 120px"></td>
				<td class='LightGrey'><input type='text' tabindex='-1'
					value="<fmt:formatNumber  value='' type='currency' />" name="tot"
					id="tot" style="text-align: right; width: 120px"></td>
				<td class="LightGrey"><input type="text" tabindex="-1"
					value="<fmt:formatNumber type="currency" value="" />" name="ivaV"
					id="ivaV" readonly style="text-align: right; width: 120px"></td>
				<td><input type="hidden"
					value="<fmt:formatNumber  value="${rows.desc}" type="number"/>"
					id="desc" style="width: 120px"></td>
				<td><input type="hidden"
					value="<fmt:formatNumber  value="${rows.iva}" type="number"/>"
					id="iva" style="width: 120px"></td>
			</tr>
		</c:forEach>

		<tr>
			<td style="text-align: right; font-weight: bold;">Valor:</td>
			<td><input type="text"
				value="<fmt:formatNumber minFractionDigits="2" type='number' value="0.00"/>"
				name="adjVal" id="adjVal" onchange="ValidateNum(this);adjust(this);"
				required="required" style="text-align: right; width: 120px"></td>
			<td style="text-align: right; font-weight: bold;">Iva:</td>
			<td><input type="text"
				value='<fmt:formatNumber minFractionDigits="2" type='number' value="0.00"/>'
				name="adjIva" id="adjIva" onchange="ValidateNum(this);adjust(this);"
				required="required" style="text-align: right; width: 120px"></td>

			<td style="text-align: right; font-weight: bold;">Total</td>

			<td class="LightGrey"><input type="text" tabindex="-1"
				value="<fmt:formatNumber type="currency" value="" />"
				name="grandtot" id="grandtot" readonly
				style="text-align: right; width: 120px"></td>

			<td class="LightGrey"><input type="text" tabindex="-1"
				value="<fmt:formatNumber type="currency" value="" />" name="totiva"
				id="totiva" readonly style="text-align: right; width: 120px"></td>

			<td><input type="hidden" value="" name="vazio" id="vazio"></td>
			<td><input type="hidden" value="" name="foravazio"
				id="foravazio"></td>
			<td><input type="hidden" value="" name="ponta" id="ponta"></td>
			<td><input type="hidden" value="" name="cheias" id="cheias"></td>

			<td><input type="hidden"
				value="<c:out value='<%=request.getSession().getAttribute("FaturaValue")%>' />"
				name="fatura" id="fatur"></td>
			<td><input type="hidden" value="" name="data1" id="dat1"></td>
			<td><input type="hidden" value="" name="data2" id="dat2"></td>
			<td><input type="hidden" value="" name="leituradata"
				id="leituradat"></td>
			<td><input type="hidden" value="" name="est" id="es"></td>
			<td><input type="hidden" value="" name="parcela" id="parcel"></td>
			<td><input type="hidden" value="" name="totvalor" id="totvalo"></td>
			<td><input type="hidden" value="" name="companhia" id="companhi"></td>
			<td><input type="hidden"
				value="<c:out value='<%=request.getSession().getAttribute("id")%>' />"
				name="idd" id="idd"></td>

		</tr>
	</table>
	<p id="demo"></p>
	<tr>
		<td><button type="submit" tabindex="0" class="btn btn-success">Save</button></td>
		<td><a href="ContadorElectServlet" class="btn btn-success">Voltar</a></td>
	</tr>
</form>

</body>

</html>