package kakeibo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import szk.OraDbConnect;

/**
 * Servlet implementation class Keikaku
 */
//@WebServlet("/Keikaku")
public class Keikaku extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Keikaku() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    static String userid = "000000";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		this.setRespHeader(response);
		PrintWriter out = null;
		String setSql = "";
		ResultSet resultSet = null;
		
		out = this.getRespWriter(response);
		
		OraDbConnect oraConnect = new OraDbConnect("823surface", "XEPDB1", "kaihatu", "tsbtzkstshkr");
        if (oraConnect.OraConnect()) {
            System.out.println("接続失敗");
        }
        //ResultSet resultSet = oraConnect.ExcecuteQuery("SELECT * FROM TEST");
        //Vector<Vector<String>> vec = oraConnect.getSqlResult(resultSet);
        
        resultSet = null;
        setSql = "";
        setSql = " select アイテムID, i.カテゴリID, カテゴリ, アイテム, 金額 ";
        setSql = setSql + " from K_アイテムマスタ  i,K_カテゴリマスタ c ";
        setSql = setSql + " where i.ユーザID = c.ユーザID ";
        setSql = setSql + " and i.カテゴリID = c.カテゴリID ";
        setSql = setSql + " and i.ユーザID = '" + userid + "' ";
        setSql = setSql + " and i.カテゴリID = 'A0001'";
        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> income = oraConnect.getSqlResult(resultSet);
        
        resultSet = null;
        setSql="";
        setSql  = setSql + "select アイテムID, i.カテゴリID as カテゴリID, カテゴリ, アイテム, 金額";
        setSql  = setSql + " from K_アイテムマスタ  i,K_カテゴリマスタ c";
        setSql  = setSql + " where i.ユーザID = c.ユーザID ";
        setSql  = setSql + " and i.カテゴリID = c.カテゴリID ";
        setSql  = setSql + " and i.ユーザID = '" + userid + "' ";
        setSql  = setSql + " and i.カテゴリID <> 'A0001'";
        setSql  = setSql + " and i.カテゴリID <> 'C0001'";
        setSql  = setSql + "union ";
        setSql  = setSql + "select 'C0001' as アイテムID, i.カテゴリID as カテゴリID, カテゴリ, '年額支払い', AVG(金額)";
        setSql  = setSql + " from K_アイテムマスタ  i,K_カテゴリマスタ c";
        setSql  = setSql + " where i.ユーザID = c.ユーザID ";
        setSql  = setSql + " and i.カテゴリID = c.カテゴリID ";
        setSql  = setSql + " and i.ユーザID = '" + userid + "' ";
        setSql  = setSql + " and i.カテゴリID = 'C0001'";
        setSql  = setSql + " group by i.カテゴリID, カテゴリ";
        setSql  = setSql + " order by カテゴリID, アイテムID";

        //memo
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> item = oraConnect.getSqlResult(resultSet);

        
		out.println("<html>");
		out.println("<head>");
		out.println("<title>main</title>");
		out.println("</head>");
		//out.println("<body>");
		out.println("<body onload='load()'>");
		out.println("<h1>HelloWorld</h1>");
		out.println("<form name='formmain' method='post' target='_self'>");
		out.println("<p id='test'>aiueo</p>");
		
		//out.println(item);
		

		for (int i = 0; i < income.size(); i++) {
			
			Vector<String> incomeRow = (Vector<String>)income.get(i);
			
			if (i==0) {
				out.println("<table class='itemRowSum' id='itemRowSum_" + incomeRow.get(1).toString().trim() + "'><tr><td class='itemSumName'>" + incomeRow.get(2).toString().trim() + "合計</td colspan='4'><td id='itemSumPrice_" + incomeRow.get(1).toString().trim() + "'>0</td><td class='itemSumBtn' id='itemSumBtn_" + incomeRow.get(1).toString().trim() + "' onclick=\"itemHidden('" + incomeRow.get(1).toString().trim() + "')\">-</td></tr></table>");
				out.println("<div class='itemTablediv' id='itemTablediv_" + incomeRow.get(1).toString().trim() + "' style='display:block'>");
				out.println("<table class='itemTable' id='itemTable_" + incomeRow.get(1).toString().trim() + "'>");
				out.println("<tr id='itemRowTitle''><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>");
			}
			
			
			out.println("<tr class='itemRowDetail'>");
			/*for (int j = 0; j < itemRow.size(); j++) {
				out.println("<td>" + incomeRow.get(j).toString().trim() + "</td>");
			}*/
			out.println("<input type='hidden' class='itemNo itemNoCategory_" + incomeRow.get(1).toString().trim() + "' id='itemNo_" + incomeRow.get(0).toString().trim() + "' name='itemNo'>");
			out.println("<td class='itemCheck itemCheckCategory_" + incomeRow.get(1).toString().trim() + "' id='itemCheck_" + incomeRow.get(0).toString().trim() + "'><input type='checkbox' id='itemChk_" + incomeRow.get(0).toString().trim() + "' name='itemCheck' value='" + incomeRow.get(3).toString().trim() + "' checked></td>");
			out.println("<td class='itemName' id='item_" + incomeRow.get(0).toString().trim() + "'>" + incomeRow.get(3).toString().trim() + "</td>");
			out.println("<td class='itemPrice itemPrice_c" + incomeRow.get(0).toString().trim() + "' id='itemPr_" + incomeRow.get(0).toString().trim() + "'><input id='itemPrice_" + incomeRow.get(0).toString().trim() + "' name='itemPrice' type='number' value="+ incomeRow.get(4).toString().trim() + "><input type ='hidden' name='itemPrice_default' id='itemPriceDefault_" + incomeRow.get(0).toString().trim() + "' value="+ incomeRow.get(4).toString().trim() + "></td>");
			out.println("<td><div class='defaultBtn' onclick=\"defaultPrice('" + incomeRow.get(0).toString().trim() + "')\">Default</div></td>");
			out.println("</tr>");
		}
		
		out.println("</table>");
		out.println("</div>");
		
		out.println("<table class='itemRowSum' id='itemRowSum_kotei'><tr><td class='itemSumName'>固定費合計</td colspan='4'><td id='itemSumPrice_kotei'>0</td></tr></table>");
		
		//out.println(item);
		
		String shocate ="";
		for (int i = 0; i < item.size(); i++) {
			Vector<String> itemRow = (Vector<String>)item.get(i);
			if (!shocate.equals(itemRow.get(2).toString().trim())) {
				if (!shocate.equals("")) {
					out.println("</table>");
					out.println("</div>");
				}
				out.println("<table class='itemRowSum' id='itemRowSum_" + itemRow.get(1).toString().trim() + "'><tr><td class='itemSumName'>" + itemRow.get(2).toString().trim() + "合計</td colspan='4'><td id='itemSumPrice_" + itemRow.get(1).toString().trim() + "'>0</td><td class='itemSumBtn' id='itemSumBtn_" + itemRow.get(1).toString().trim() + "' onclick=\"itemHidden('" + itemRow.get(1).toString().trim() + "')\">-</td></tr></table>");
				out.println("<div class='itemTablediv' id='itemTablediv_" + itemRow.get(1).toString().trim() + "' style='display:block'>");
				out.println("<table class='itemTable' id='itemTable_" + itemRow.get(1).toString().trim() + "'>");
				out.println("<tr id='itemRowTitle''><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>");
			} 
			out.println("<tr class='itemRowDetail'>");
			/*for (int j = 0; j < itemRow.size(); j++) {
				out.println("<td>" + itemRow.get(j).toString().trim() + "</td>");
			}*/
			out.println("<input type='hidden' class='itemNo itemNoCategory_" + itemRow.get(1).toString().trim() + "' id='itemNo_" + itemRow.get(0).toString().trim() + "' name='itemNo'>");
			out.println("<td class='itemCheck itemCheckCategory_" + itemRow.get(1).toString().trim() + "' id='itemCheck_" + itemRow.get(0).toString().trim() + "'><input type='checkbox' id='itemChk_" + itemRow.get(0).toString().trim() + "' name='itemCheck' value='" + itemRow.get(3).toString().trim() + "' checked></td>");
			out.println("<td class='itemName' id='item_" + itemRow.get(0).toString().trim() + "'>" + itemRow.get(3).toString().trim() + "</td>");
			out.println("<td class='itemPrice itemPrice_c" + itemRow.get(0).toString().trim() + "' id='itemPr_" + itemRow.get(0).toString().trim() + "'><input id='itemPrice_" + itemRow.get(0).toString().trim() + "' name='itemPrice' type='number' value="+ itemRow.get(4).toString().trim() + "><input type ='hidden' name='itemPrice_default' id='itemPriceDefault_" + itemRow.get(0).toString().trim() + "' value="+ itemRow.get(4).toString().trim() + "></td>");
			out.println("<td><div class='defaultBtn' onclick=\"defaultPrice('" + itemRow.get(0).toString().trim() + "')\">Default</div></td>");
			out.println("</tr>");
			shocate = itemRow.get(2).toString().trim();
		}
		
		out.println("</form>");
		out.println("<script type='text/javascript' src='js/keikaku.js'></script>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void setRespHeader(HttpServletResponse response) {
		// TODO 自動生成されたメソッド・スタブ
		//文字コードの設定
		response.setContentType("text/html; charset=UTF-8");
		//キャッシュの設定
		response.setHeader("Cache-Control", "no-cache,no-store");
		response.setHeader("Pragma", "no-cache");
	}
	
	PrintWriter getRespWriter(HttpServletResponse response) {
		//変数定義
		String encoding ="";
		OutputStreamWriter osw = null;
		
		//HTMLライタの設定
		encoding = response.getCharacterEncoding();
		try {
			osw = new OutputStreamWriter(response.getOutputStream(), encoding);
		} catch (Exception ex) {
			return null;
		}
		
		return new PrintWriter(osw, true);
	}


}
