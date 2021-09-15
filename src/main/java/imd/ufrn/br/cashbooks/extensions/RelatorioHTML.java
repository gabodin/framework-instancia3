package imd.ufrn.br.cashbooks.extensions;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import imd.ufrn.br.cashbooks.interfaces.IGerarRelatorio;
import imd.ufrn.br.cashbooks.model.Movimentacao;

public class RelatorioHTML implements IGerarRelatorio {
	
	@Override
	public String gerarRelatorio(List<Movimentacao> movimentacoes) {
		String htmlText = "<!DOCTYPE html>\n<html>"; // Cabeçalho

		htmlText += "<head>\n" + 
				"		<meta charset=\"UTF-8\">\n" + 
				"	    <style>\n" + 
				"	    	:root {\n" + 
				"                --accent-color: [[${color}]];\n" + 
				"            }\n" + 
				"	    \n" + 
				"	        @media print {\n" + 
				"	            @page   { margin: 0;     }\n" + 
				"	            body    { margin: 1.6cm; }\n" + 
				"	            .noprint{ display:none !important;  }\n" + 
				"	        }\n" + 
				"	        a.noprint {\n" + 
				"	        	width:100%;\n" + 
				"	        	text-align:center;\n" + 
				"	        	display: block;\n" + 
				"	        	margin-bottom:30px;\n" + 
				"	        	padding: 15px;\n" + 
				"	        	background-color: var(--accent-color);\n" + 
				"	        	color: white;\n" + 
				"	        	text-decoration: none;\n" + 
				"	        	border-radius: 10px;\n" + 
				"	        }\n" + 
				"	        a.noprint:hover {\n" + 
				"	        	opacity: 0.8;\n" + 
				"	        }\n" + 
				"	        .left {\n" + 
				"	            text-align: left !important;\n" + 
				"	        }\n" + 
				"	        .center {\n" + 
				"	            text-align: center !important;\n" + 
				"	        }\n" + 
				"	        .right {\n" + 
				"	            text-align: right !important;\n" + 
				"	        }\n" + 
				"	        .invoice-box {\n" + 
				"	            max-width: 800px;\n" + 
				"	            margin: auto;\n" + 
				"	            font-size: 16px;\n" + 
				"	            line-height: 24px;\n" + 
				"	            font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" + 
				"	            color: #555;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table {\n" + 
				"	            width: 100%;\n" + 
				"	            line-height: inherit;\n" + 
				"	            text-align: left;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table td {\n" + 
				"	            padding: 5px;\n" + 
				"	            vertical-align: top;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr td:nth-child(n+2) {\n" + 
				"	            text-align: right;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.top table td {\n" + 
				"	            padding-bottom: 20px;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.top table td.title {\n" + 
				"	            font-size: 45px;\n" + 
				"	            line-height: 45px;\n" + 
				"	            color: #333;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.information table td {\n" + 
				"	            padding-bottom: 40px;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.heading td {\n" + 
				"	            background: #eee;\n" + 
				"	            border-bottom: 1px solid #ddd;\n" + 
				"	            font-weight: bold;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.details td {\n" + 
				"	            padding-bottom: 20px;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.item td{\n" + 
				"	            border-bottom: 1px solid #eee;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.item.last td {\n" + 
				"	            border-bottom: none;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.item input {\n" + 
				"	            padding-left: 5px;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.item td:first-child input {\n" + 
				"	            margin-left: -5px;\n" + 
				"	            width: 100%;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box table tr.total td:nth-child(2) {\n" + 
				"	            border-top: 2px solid #eee;\n" + 
				"	            font-weight: bold;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .invoice-box input[type=number] {\n" + 
				"	            width: 60px;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .surplus {\n" + 
				"	            color: #06822d;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .debit {\n" + 
				"	            color: #c82f2f;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .neutral {\n" + 
				"	            font-weight: bold;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        @media only screen and (max-width: 600px) {\n" + 
				"	            .invoice-box table tr.top table td {\n" + 
				"	                width: 100%;\n" + 
				"	                display: block;\n" + 
				"	                text-align: center;\n" + 
				"	            }\n" + 
				"	            \n" + 
				"	            .invoice-box table tr.information table td {\n" + 
				"	                width: 100%;\n" + 
				"	                display: block;\n" + 
				"	                text-align: center;\n" + 
				"	            }\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        /** RTL **/\n" + 
				"	        .rtl {\n" + 
				"	            direction: rtl;\n" + 
				"	            font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .rtl table {\n" + 
				"	            text-align: right;\n" + 
				"	        }\n" + 
				"	\n" + 
				"	        .rtl table tr td:nth-child(2) {\n" + 
				"	            text-align: left;\n" + 
				"	        }\n" + 
				"	    </style>\n" + 
				"</head>";
		
		htmlText += "<body>\n" + 
				"		<div class=\"invoice-box\">\n" + 
				"	        <a href=\"#\" onclick=\"window.history.back();\" class=\"noprint\">VOLTAR</a>\n" + 
				"	        <table cellpadding=\"0\" cellspacing=\"0\">\n" + 
				"	            <tr class=\"top\">\n" + 
				"	            <td colspan=\"7\">\n" + 
				"	                <table>\n" + 
				"	                <tr>\n" + 
				"	                    <td class=\"title\">\n" + 
				"	                    <h4>E-Finanças</h4>\n" + 
				"	                    </td>\n" + 
				"	\n" + 
				"	                    <td>\n" + 
				"	                    <b>Relatório de movimentações</b><br> Emitido: <span>" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "</span>\n" + 
				"	                    </td>\n" + 
				"	                </tr>\n" + 
				"	                </table>\n" + 
				"	            </td>\n" + 
				"	            </tr>\n" + 
				"	\n" + 
				"	            <tr class=\"heading\">\n" + 
				"		            <td>\n" + 
				"		                Data\n" + 
				"		            </td>\n" + 
				"		            \n" + 
				"		            <td class=\"center\">\n" + 
				"		                Valor\n" + 
				"		            </td>\n" + 
				"		            \n" + 
				"		            <td class=\"center\">\n" + 
				"		                Cobrança\n" + 
				"		            </td>\n" + 
				"		\n" + 
				"					<td class=\"center\">\n" + 
				"		                Status\n" + 
				"		            </td>\n" + 
				"		            \n" + 
				"		            <td class=\"center\">\n" + 
				"		                Categoria\n" + 
				"		            </td>\n" + 
				"		\n" + 
				"		            <td class=\"right\" colspan=\"2\">\n" + 
				"		                Descrição\n" + 
				"		            </td>\n" + 
				"	            </tr>";
		
		for(int i = 0; i < movimentacoes.size(); i++) {
			htmlText += "<tr class=\"item\">" + 
					"<td>" + movimentacoes.get(i).getDataMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")) + 
					"</td><td class=\"center\">" + String.format("%.2f", movimentacoes.get(i).getValor()) + 
					"</td><td class=\"center\">" + movimentacoes.get(i).getDataCobranca().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")) + 
					"</td><td class=\"center\">" + movimentacoes.get(i).getStatus().name() + 
					"</td><td class=\"center\">" + movimentacoes.get(i).getCategoria().name() + 
					"</td><td class=\"right\" colspan=\"2\">" + movimentacoes.get(i).getDescricao() + "</td>";
		}
		
		htmlText += "</tr></table></div></body></html>";
		
		return htmlText;
	}
	
}
