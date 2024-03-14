package kakeibo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    
	//-------------------------------------
	// 変数定義
	//-------------------------------------
    
    static String userid = "000000";
    static OraDbConnect oraConnect =null;
    static String[][] categorys = null;
    static Vector<Vector<String>> items = new Vector<>();
    static String[] incomeItems = null;

    
  //--------------------------------------------------------------------------------
  	//
  	// データ引渡用構造体
  	//
  	//--------------------------------------------------------------------------------
  	class DataHolder {
  		
  		//-------------------------------------
  		// 変数定義
  		//-------------------------------------
  		public Hashtable<String, String> htParam    = null;   // リクエストバッファ
  		public String stSysDate     = "";     // システム日付
  		public String stSysTime     = "";     // システム時間
  		
  		
  		//-------------------------------------
  		// コンストラクタ
  		//-------------------------------------
  		public DataHolder() {
  			
  			htParam   = new Hashtable<>();
  			stSysDate = "" ;
  			stSysTime = "" ;
  			
  		}
  		
  		//-------------------------------------
  		// システム日付をセット
  		//-------------------------------------
  		public void setSysDateTime(String stSysDateTime) {
  			
  			stSysDate = stSysDateTime.substring(0,8);
  			stSysTime = stSysDateTime.substring(8,14);
  			
  		}
  		
  		public String getSysDate() {
  			return stSysDate;
  		}
  		
  		public String getSysTime()  {
  			return stSysTime;
  		}

  		//-------------------------------------
  		// リクエストから文字列を取得
  		//-------------------------------------
  		public String getString(String stKey) {

  			if (htParam.get(stKey.toUpperCase()) == null) {
  				return "";
  			}

  			if (htParam.get(stKey.toUpperCase()) instanceof String == false) {
  				return "";
  			}

  			return ((String)htParam.get(stKey.toUpperCase())).trim();
  		}

  		//-------------------------------------
  		// リクエストに文字列をセット
  		//-------------------------------------
  		public void setString(String stKey, String stData) {

  			htParam.put(stKey.toUpperCase(), stData);

  		}

  	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
  	
	 //--------------------------------------------------------------------------------
	 //
	 // GETによるリクエスト受付処理
	 //
	 //--------------------------------------------------------------------------------
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		System.out.println("doGet");
		
		//-------------------------------------
		// DB接続
		//-------------------------------------
		oraConnect = new OraDbConnect("823surface", "XEPDB1", "kaihatu", "tsbtzkstshkr");
		if (oraConnect.OraConnect()) {
			System.out.println("接続失敗");
		}
		 //ResultSet resultSet = oraConnect.ExcecuteQuery("SELECT * FROM TEST");
		 //Vector<Vector<String>> vec = oraConnect.getSqlResult(resultSet);

		//-------------------------------------
		// セッション情報の設定
		//-------------------------------------
		HttpSession htpsTrans = this.setHttpSession(request);

		//-------------------------------------
		// 変数定義
		//-------------------------------------
		PrintWriter out = null;
		DataHolder dhHtml = new DataHolder();

		//-------------------------------------
		// 初期処理
		//-------------------------------------
		this.setRespHeader(response);
		out = this.getRespWriter(response);
		dhHtml.htParam = this.getReqParam(request);
				
		//-------------------------------------
		// ユーザ権限のチェック
		//-------------------------------------
		userid = "000000";
				
		//-------------------------------------
		// セッション情報の取得
		//-------------------------------------
		this.getSessionParam(htpsTrans, dhHtml.htParam);
		
		//-------------------------------------
		// ユーザ定義
		//-------------------------------------
		        
		//-------------------------------------
		// HTMLの編集・出力
		//-------------------------------------
		
		//日付取得
		Date nowDate = new Date();
        SimpleDateFormat sdf1
        = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatNowDate = sdf1.format(nowDate);
        dhHtml.setSysDateTime(formatNowDate);
		
		//登録FLG
		dhHtml.setString("TOROKUFLG", "1");
		
		getItamTable(dhHtml);
		setHtmlDefault(out, dhHtml);
		//setHtmlDefault(out);
		out.close();
				
		//-------------------------------------
		// DB切断
		//-------------------------------------
		if (oraConnect != null) {
			oraConnect.CloseDB();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	 //--------------------------------------------------------------------------------
	 //
	 // POSTによるリクエスト受付処理
	 //
	 //--------------------------------------------------------------------------------
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		System.out.println("doPost");		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//-------------------------------------
		// DB接続
		//-------------------------------------
		oraConnect = new OraDbConnect("823surface", "XEPDB1", "kaihatu", "tsbtzkstshkr");
		if (oraConnect.OraConnect()) {
			System.out.println("接続失敗");
		}
		 //ResultSet resultSet = oraConnect.ExcecuteQuery("SELECT * FROM TEST");
		 //Vector<Vector<String>> vec = oraConnect.getSqlResult(resultSet);

		//-------------------------------------
		// セッション情報の設定
		//-------------------------------------
		HttpSession htpsTrans = this.setHttpSession(request);

		//-------------------------------------
		// 変数定義
		//-------------------------------------
		PrintWriter out = null;
		DataHolder dhHtml = new DataHolder();

		//-------------------------------------
		// 初期処理
		//-------------------------------------
		this.setRespHeader(response);
		out = this.getRespWriter(response);
		dhHtml.htParam = this.getReqParam(request);
				
		//-------------------------------------
		// ユーザ権限のチェック
		//-------------------------------------
		userid = "000000";
				
		//-------------------------------------
		// セッション情報の取得
		//-------------------------------------
		this.getSessionParam(htpsTrans, dhHtml.htParam);
		        
		//-------------------------------------
		// HTMLの編集・出力
		//-------------------------------------
		setHtmlDefault(out, dhHtml);
		//setHtmlDefault(out);
		out.close();
				
		//-------------------------------------
		// DB切断
		//-------------------------------------
		if (oraConnect != null) {
			oraConnect.CloseDB();
		}
	}
	
	 //--------------------------------------------------------------------------------
	 //
	 // セッションの設定
	 //
	 //--------------------------------------------------------------------------------
	HttpSession setHttpSession (HttpServletRequest req) {
		
		//-------------------------------------
		// 変数定義
		//-------------------------------------
		HttpSession httpTrans = null;
		
		//-------------------------------------
		// セッション情報尾設定
		//-------------------------------------
		httpTrans = req.getSession(true);
		try {
			httpTrans.setMaxInactiveInterval(3000);
		} catch (NoSuchMethodError ex) {
			;
		}
		return httpTrans;
	}
	
	
	 //--------------------------------------------------------------------------------
	 //
	 // レスポンスヘッダの設定
	 //
	 //--------------------------------------------------------------------------------
	private void setRespHeader(HttpServletResponse response) {
		// TODO 自動生成されたメソッド・スタブ
		//文字コードの設定
		response.setContentType("text/html; charset=UTF-8");
		//キャッシュの設定
		response.setHeader("Cache-Control", "no-cache,no-store");
		response.setHeader("Pragma", "no-cache");
	}
	
	 //--------------------------------------------------------------------------------
	 //
	 // HTMLライタの取得
	 //
	 //--------------------------------------------------------------------------------
	PrintWriter getRespWriter(HttpServletResponse response) {
		
		//-------------------------------------
		// 変数定義
		//-------------------------------------
		String encoding ="";
		OutputStreamWriter osw = null;
		
		//-------------------------------------
		// HTMLライタの設定
		//-------------------------------------
		encoding = response.getCharacterEncoding();
		try {
			osw = new OutputStreamWriter(response.getOutputStream(), encoding);
		} catch (Exception ex) {
			return null;
		}
		
		return new PrintWriter(osw, true);
	}
	
	
	 //--------------------------------------------------------------------------------
	 //
	 // HTMLからのリクエスト抽出
	 //
	 //--------------------------------------------------------------------------------
	Hashtable<String, String> getReqParam(HttpServletRequest req) throws IOException {
		
		//-------------------------------------
		// 変数定義
		//-------------------------------------
		Hashtable<String, String> htParam = new Hashtable<>();
		Enumeration<?> enmKeys = req.getParameterNames();
		
		//-------------------------------------
		// リクエストから名前と値を抽出
		//-------------------------------------
		while (enmKeys.hasMoreElements()) {
			String stKeys = (String)enmKeys.nextElement();
			String stCnvWord = req.getParameterValues(stKeys)[0];
			String stCnvHalf = stCnvWord;
			htParam.put(stKeys, stCnvHalf.toUpperCase());
		}
		
		
		return htParam;
	}
	
	//--------------------------------------------------------------------------------
	//
	// セッション情報の取得
	//
	//--------------------------------------------------------------------------------
	void getSessionParam(HttpSession htpsTrans, Hashtable<String, String> htParam) {
		
		//-------------------------------------
		// セッション情報が空ならば抜ける
		//-------------------------------------
		if (htpsTrans.getAttributeNames() == null) {
			return;
		}
		
		//-------------------------------------
		// 変数定義
		//-------------------------------------
		String stKey = "";
		String stVal = "";
		Enumeration<?> ensSnKeys = htpsTrans.getAttributeNames();
		
		//-------------------------------------
		// セッション情報を抽出
		//-------------------------------------
		while (ensSnKeys.hasMoreElements()) {
			stKey = (String)ensSnKeys.nextElement();
			stVal = (String)htpsTrans.getAttribute(stKey);
			
			htParam.put(stKey.toUpperCase(), stVal);
			htpsTrans.removeAttribute(stKey);
		}
	}
	
	 //--------------------------------------------------------------------------------
	 //
	 // HTML編集
	 //
	 //--------------------------------------------------------------------------------
	void setHtmlDefault(PrintWriter out, DataHolder dhHtml) {
    //void setHtmlDefault(PrintWriter out) {
		
		String setSql = "";
		ResultSet resultSet = null;
		
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

        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> item = oraConnect.getSqlResult(resultSet);

        
		out.println("<html>");
		out.println("<head>");
		out.println("<title>main</title>");
		out.println("</head>");
		//out.println("<body>");
		out.println("<body onload='load()'>");
		out.println("<h1>HelloWorld</h1>");
		out.println("<form name='FORMMAIN' method='post' target='_self'>");
		out.println("<p id='test'>aiueo" + dhHtml.getString("FORMTEST") + "</p>");
		out.println("<p>" + dhHtml.getString("TOROKUFLG") + "</p>");
		
		try {
			
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMM");
            Date date = sdFormat.parse(dhHtml.getSysDate().substring(0, 6));
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            
            
            out.println("<select name='KEIKAKUDATE'>");
            
            calendar.add(Calendar.MONTH, 4);
            out.println("<option value='" + sdFormat.format(calendar.getTime()) + "'>" + sdFormat.format(calendar.getTime()) + "</option>");
            calendar.add(Calendar.MONTH, -1);
            out.println("<option value='" + sdFormat.format(calendar.getTime()) + "'>" + sdFormat.format(calendar.getTime()) + "</option>");
            calendar.add(Calendar.MONTH, -1);
            out.println("<option value='" + sdFormat.format(calendar.getTime()) + "'>" + sdFormat.format(calendar.getTime()) + "</option>");
            calendar.add(Calendar.MONTH, -1);
            out.println("<option value='" + sdFormat.format(calendar.getTime()) + "' selected>" + sdFormat.format(calendar.getTime()) + "</option>");
            calendar.add(Calendar.MONTH, -1);
            out.println("<option value='" + sdFormat.format(calendar.getTime()) + "'>" + sdFormat.format(calendar.getTime()) + "</option>");
            
    		out.println("</select>");
    		
		} catch(Exception e) {
			System.out.println("日付変換エラー");
		}
		
		out.println("<input type='hidden'name='TOROKUFLG' value='" + dhHtml.getString("TOROKUFLG") + "'>");
		
		//out.println(item);
		out.println("<input type='hidden' id='formtest' name='FORMTEST' VALUE='TEST'>");
		

		for (int i = 0; i < income.size(); i++) {
			
			Vector<String> incomeRow = (Vector<String>)income.get(i);
			
			if (i==0) {
				out.println("<table class='itemRowSum' id='itemRowSum_" + incomeRow.get(1).toString().trim() + "'><tr><td class='itemSumName'>" + incomeRow.get(2).toString().trim() + "合計</td colspan='4'><td id='itemSumPrice_" + incomeRow.get(1).toString().trim() + "'>0</td><td class='itemSumBtn' id='itemSumBtn_" + incomeRow.get(1).toString().trim() + "' onclick=\"itemHidden('" + incomeRow.get(1).toString().trim() + "')\">-</td></tr></table>");
				out.println("<div class='itemTablediv' id='itemTablediv_" + incomeRow.get(1).toString().trim() + "' style='display:block'>");
				out.println("<table class='itemTable' id='itemTable_" + incomeRow.get(1).toString().trim() + "'>");
				out.println("<tr class='itemRowTitle'><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>");
			}
			
			
			out.println("<tr class='itemRowDetail' id='itemRowTr_" + incomeRow.get(0).toString().trim() + "'>");
			/*for (int j = 0; j < itemRow.size(); j++) {
				out.println("<td>" + incomeRow.get(j).toString().trim() + "</td>");
			}*/
			out.println("<input type='hidden' class='itemNo itemNoCategory_" + incomeRow.get(1).toString().trim() + "' id='itemNo_" + incomeRow.get(0).toString().trim() + "' name='ITEM_NO'>");
			out.println("<td class='itemCheck itemCheckCategory_" + incomeRow.get(1).toString().trim() + "' id='itemCheck_" + incomeRow.get(0).toString().trim() + "'><input type='checkbox' id='itemChk_" + incomeRow.get(0).toString().trim() + "' name='ITEM_CHECK' value='" + incomeRow.get(3).toString().trim() + "' checked></td>");
			out.println("<td class='itemName' id='item_" + incomeRow.get(0).toString().trim() + "'>" + incomeRow.get(3).toString().trim() + "</td>");
			out.println("<td class='itemPrice itemPrice_c" + incomeRow.get(0).toString().trim() + "' id='itemPr_" + incomeRow.get(0).toString().trim() + "'><input id='itemPrice_" + incomeRow.get(0).toString().trim() + "' name='ITEM_PRICE' type='number' value="+ incomeRow.get(4).toString().trim() + "><input type ='hidden' name='ITEM_PRICEDEFAULT' id='itemPriceDefault_" + incomeRow.get(0).toString().trim() + "' value="+ incomeRow.get(4).toString().trim() + "></td>");
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
				out.println("<tr class='itemRowTitle'><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>");
			} 
			out.println("<tr class='itemRowDetail' id='itemRowTr_" + itemRow.get(0).toString().trim() + "'>");
			/*for (int j = 0; j < itemRow.size(); j++) {
				out.println("<td>" + itemRow.get(j).toString().trim() + "</td>");
			}*/
			out.println("<input type='hidden' class='itemNo itemNoCategory_" + itemRow.get(1).toString().trim() + "' id='itemNo_" + itemRow.get(0).toString().trim() + "' name='ITEM_NO'>");
			out.println("<td class='itemCheck itemCheckCategory_" + itemRow.get(1).toString().trim() + "' id='itemCheck_" + itemRow.get(0).toString().trim() + "'><input type='checkbox' id='itemChk_" + itemRow.get(0).toString().trim() + "' name='ITEM_CHECK' value='" + itemRow.get(3).toString().trim() + "' checked></td>");
			out.println("<td class='itemName' id='item_" + itemRow.get(0).toString().trim() + "'>" + itemRow.get(3).toString().trim() + "</td>");
			out.println("<td class='itemPrice itemPrice_c" + itemRow.get(0).toString().trim() + "' id='itemPr_" + itemRow.get(0).toString().trim() + "'><input id='itemPrice_" + itemRow.get(0).toString().trim() + "' name='ITEM_PRICE' type='number' value="+ itemRow.get(4).toString().trim() + "><input type ='hidden' name='ITEM_PRICEDEFAULT' id='itemPriceDefault_" + itemRow.get(0).toString().trim() + "' value="+ itemRow.get(4).toString().trim() + "></td>");
			out.println("<td><div class='defaultBtn' onclick=\"defaultPrice('" + itemRow.get(0).toString().trim() + "')\">Default</div></td>");
			out.println("</tr>");
			shocate = itemRow.get(2).toString().trim();
		}
		out.println("</table>");
		out.println("</div>");
		
		out.println("<a href='#' onclick='document.FORMMAIN.submit();'>登録</a>");
		
		out.println("</form>");
		out.println("<script type='text/javascript' src='js/keikaku.js'></script>");
		out.println("</body>");
		out.println("</html>");
		
		return;
	}
	
	
	@SuppressWarnings("null")
	//--------------------------------------------------------------------------------
	//
	// アイテムテーブル取得
	//
	//--------------------------------------------------------------------------------
	boolean getItamTable(DataHolder dhHtml) {
		
		String setSql = "";
		ResultSet resultSet = null;
		String incomeID = "A0001";
		
		resultSet = null;
        setSql = "";
        setSql = " select アイテムID, i.カテゴリID, カテゴリ, アイテム, 金額 ";
        setSql = setSql + " from K_アイテムマスタ  i,K_カテゴリマスタ c ";
        setSql = setSql + " where i.ユーザID = c.ユーザID ";
        setSql = setSql + " and i.カテゴリID = c.カテゴリID ";
        setSql = setSql + " and i.ユーザID = '" + userid + "' ";
        setSql = setSql + " and i.カテゴリID = '" + incomeID + "'";
        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> income = oraConnect.getSqlResult(resultSet);
        
        
        dhHtml.setString("ITEMCATEID", incomeID);
		dhHtml.setString("INCOME", ((Vector<String>)income.get(0)).toString());
		
		incomeItems = new String[income.size()];


		for (int i = 0; i < income.size(); i++) {
			Vector<String> incomeRow = (Vector<String>)income.get(i);

			incomeItems[i]=incomeRow.get(0).toString().trim();
			dhHtml.setString("INCOMEITEMID_" + (i+1),incomeRow.get(0).toString().trim());
			dhHtml.setString("INCOMEITEMNAME_" + (i+1), incomeRow.get(1).toString().trim());
			dhHtml.setString("INCOMEPRICE_" + (i+1), incomeRow.get(2).toString().trim());
		}
		
		System.out.println(Arrays.toString(incomeItems));
        
        resultSet = null;
        setSql  = "";
        setSql  = setSql + "";
        setSql  = setSql + "select i.カテゴリID, カテゴリ ";
        setSql  = setSql + " from K_アイテムマスタ i, K_カテゴリマスタ c ";
        setSql  = setSql + "where i.カテゴリID = c.カテゴリID";
        setSql  = setSql + " and i.カテゴリID <> 'A0001' ";
        setSql  = setSql + " and i.カテゴリID <> 'C0001' ";
        setSql  = setSql + "group by i.カテゴリID, カテゴリ ";
        setSql  = setSql + "order by i.カテゴリID ";

        
        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> category = oraConnect.getSqlResult(resultSet);
        
        categorys = new String[category.size()][2];


		for (int i = 0; i < category.size(); i++) {
			Vector<String> categoryRow = (Vector<String>)category.get(i);

			categorys[i][0]=categoryRow.get(0).toString().trim();
			categorys[i][1]=categoryRow.get(1).toString().trim();

		}

		for (int i=0;i < categorys.length; i++) {
			resultSet = null;
	        setSql  = "";
	        setSql  = setSql + "select アイテムID, カテゴリID, アイテム, 金額 ";
	        setSql  = setSql + " from K_アイテムマスタ ";
	        setSql  = setSql + "where ユーザID = '" + userid + "' ";
	        setSql  = setSql + " and カテゴリID = '" + categorys[i][0] + "' ";
	        setSql  = setSql + "order by カテゴリID, アイテムID";
	        
	        resultSet = oraConnect.ExcecuteQuery(setSql);
	        Vector<Vector<String>> item = oraConnect.getSqlResult(resultSet);
	        Vector<String> itemID = null;
	        
	        for (int k=0;k<item.size();k++) {
	        	Vector<String> itemRow = (Vector<String>)item.get(k);
	        	itemID = new Vector<>();
	        	itemID.add(itemRow.get(0).toString().trim());
				dhHtml.setString("ITEMID_" + (k+1),itemRow.get(0).toString().trim());
				dhHtml.setString("ITEMCATEGORY_" + (k+1), itemRow.get(1).toString().trim());
				dhHtml.setString("ITEMNAME_" + (k+1), itemRow.get(1).toString().trim());
				dhHtml.setString("PRICE_" + (k+1), itemRow.get(2).toString().trim());
	        }
	        
	        items.add(itemID);

			
		}
		
		System.out.println(items);
		
		
		setSql  = "";
        setSql  = setSql + "select 'C0001' as アイテムID, 'C0001' as カテゴリID, '年額' as カテゴリ, '年額支払い' as アイテム, AVG(金額)";
        setSql  = setSql + " from K_アイテムマスタ";
        setSql  = setSql + " where ユーザID = '" + userid + "' ";
        setSql  = setSql + " group by カテゴリID";

        
        System.out.println(setSql);
        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> yearpay = oraConnect.getSqlResult(resultSet);
        Vector<String> yearpRow = (Vector<String>)yearpay.get(0);
        
        dhHtml.setString("NENITEMID",yearpRow.get(0).toString().trim());
        dhHtml.setString("NENITEMCATEID", yearpRow.get(1).toString().trim());
		dhHtml.setString("NENITEMCATEGORY", yearpRow.get(2).toString().trim());
		dhHtml.setString("NENITEMNAME", yearpRow.get(3).toString().trim());
		dhHtml.setString("NENPRICE", yearpRow.get(4).toString().trim());
        
        

		return false;
	}


}