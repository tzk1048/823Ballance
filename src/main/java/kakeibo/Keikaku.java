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
		
		//String setSql = "";
		//ResultSet resultSet = null;
		
		/*resultSet = null;
        setSql = "";
        setSql = " select アイテムID, i.カテゴリID, カテゴリ, アイテム, 金額 ";
        setSql = setSql + " from K_アイテムマスタ  i,K_カテゴリマスタ c ";
        setSql = setSql + " where i.ユーザID = c.ユーザID ";
        setSql = setSql + " and i.カテゴリID = c.カテゴリID ";
        setSql = setSql + " and i.ユーザID = '" + userid + "' ";
        setSql = setSql + " and i.カテゴリID = 'A0001'";
        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> income = oraConnect.getSqlResult(resultSet);*/
        
        /*resultSet = null;
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
        Vector<Vector<String>> item = oraConnect.getSqlResult(resultSet);*/

        
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
		out.println("<p>GET:" + dhHtml.getString("KEIKAKUDATE") + "</p>");
		
		int inrow = StrToInt(dhHtml.getString("INROW"));
		
		for (int i=1; i <= inrow; i++) {
			out.println(dhHtml.getString("INCATEID_" + i) + "," + dhHtml.getString("INCATENAME_" + i) + "," + dhHtml.getString("INTYPE_" + i) + "," + dhHtml.getString("INITEMID_" + i) + "," + dhHtml.getString("INITEMNAME_" + i) + "," + dhHtml.getString("INITEMPRICE_" + i));
		}
		
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
		
		out.println("<input type='hidden' name='TOROKUFLG' value='" + dhHtml.getString("TOROKUFLG") + "'>");
		
		//out.println(item);
		out.println("<input type='hidden' id='formtest' name='FORMTEST' VALUE='TEST'>");
		
		int row = 1;
		
		//収入
		
		
		//収入合計
		//POST
		out.println("<input type='hidden' name='INCATEID_" + row + "' value='" + dhHtml.getString("INCOMECATEID") + "'>");
		out.println("<input type='hidden' name='INCATENAME_" + row + "' value='" + dhHtml.getString("INCOMECATENAME") + "'>");
		out.println("<input type='hidden' name='INTYPE_" + row + "' value='S'>");
		out.println("<input type='hidden' name='INITEMID_" + row + "' value=' '>");
		out.println("<input type='hidden' name='INITEMNAME_" + row + "' value=' '>");
		out.println("<input type='hidden' name='INITEMPRICE_" + row + "' value='0' id='itemSumPriceName_" + dhHtml.getString("INCOMECATEID") + "'>");
		
		//表示
		out.println("<table class='itemRowSum' id='itemRowSum_" + dhHtml.getString("INCOMECATEID") + "'><tr>");
		out.println("<td class='itemSumName'>" + dhHtml.getString("INCOMECATENAME") + "合計</td colspan='4'>");
		out.println("<td id='itemSumPrice_" + dhHtml.getString("INCOMECATEID") + "'>0</td>");
		out.println("<td class='itemSumBtn' id='itemSumBtn_" + dhHtml.getString("INCOMECATEID") + "' onclick=\"itemHidden('" + dhHtml.getString("INCOMECATEID") + "')\">-</td>");
		out.println("</tr></table>");
		
		
		//固定費
		int incomeCnt = StrToInt(dhHtml.getString("INCOMECOUNT"));
		
		out.println("<div class='itemTablediv' id='itemTablediv_" + dhHtml.getString("INCOMECATEID") + "' style='display:block'>");
		out.println("<table class='itemTable' id='itemTable_" + dhHtml.getString("INCOMECATEID") + "'>");
		out.println("<tr class='itemRowTitle'><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>");

		for (int i = 0; i < incomeCnt; i++) {
			
			row++;
			
			//INPUT
			out.println("<input type='hidden' name='INCATEID_" + row + "' value='" + dhHtml.getString("INCOMECATEID") + "'>");
			out.println("<input type='hidden' name='INCATENAME_" + row + "' value='" + dhHtml.getString("INCOMECATENAME") + "'>");
			out.println("<input type='hidden' name='INTYPE_" + row + "' value='I'>");
			out.println("<input type='hidden' name='INITEMID_" + row + "' value='" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "'>");
			out.println("<input type='hidden' name='INITEMNAME_" + row + "' value='" + dhHtml.getString("INCOMEITEMNAME_" + (i+1)) + "'>");
			
			//表示
			out.println("<tr class='itemRowDetail' id='itemRowTr_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "'>");
			out.println("<td class='itemCheck itemCheckCategory_" + dhHtml.getString("INCOMECATEID") + "' id='itemCheck_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "'>");
			out.println("<input type='checkbox' id='itemChk_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "' name='INITEMCHECK_" + row + "' value='1' checked>");
			out.println("</td>");
			out.println("<td class='itemName' id='item_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "'>" + dhHtml.getString("INCOMEITEMNAME_" + (i+1)) + "</td>");
			out.println("<td class='itemPrice itemPrice_c" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "' id='itemPr_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "'><input id='itemPrice_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "' name='INITEMPRICE_" + row + "' type='number' value="+ dhHtml.getString("INCOMEITEMPRICE_" + (i+1)) + "><input type ='hidden' name='ITEM_PRICEDEFAULT_" + row + "' id='itemPriceDefault_" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "' value="+ dhHtml.getString("INCOMEITEMPRICE_" + (i+1)) + "></td>");
			out.println("<td><div class='defaultBtn' onclick=\"defaultPrice('" + dhHtml.getString("INCOMEITEMID_" + (i+1)) + "')\">Default</div></td>");
			out.println("</tr>");
		}
		
		out.println("</table>");
		out.println("</div>");
		
		//固定費
		
		row++;
		
		//固定費合計
		//INPUT
		out.println("<input type='hidden' name='INCATEID_" + row + "' value='B0000'>");
		out.println("<input type='hidden' name='INCATENAME_" + row + "' value='固定費'>");
		out.println("<input type='hidden' name='INTYPE_" + row + "' value='S'>");
		out.println("<input type='hidden' name='INITEMID_" + row + "' value=' '>");
		out.println("<input type='hidden' name='INITEMNAME_" + row + "' value=' '>");
		out.println("<input type='hidden' name='INITEMPRICE_" + row + "' value='0' id='itemSumPriceName_kotei'>");
		
		//表示
		out.println("<table class='itemRowSum' id='itemRowSum_kotei'><tr>");
		out.println("<td class='itemSumName'>固定費合計</td colspan='4'>");
		out.println("<td id='itemSumPrice_kotei'>0</td>");
		out.println("</tr></table>");
		
		//out.println(item);
		
		int itemCnt = StrToInt(dhHtml.getString("ITEMCOUNT"));
		String shocate ="";
		for (int i = 0; i < itemCnt; i++) {
			//Vector<String> itemRow = (Vector<String>)item.get(i);
			if (!shocate.equals(dhHtml.getString("ITEMCATEGORY_" + (i+1)))) {
				if (!shocate.equals("")) {
					out.println("</table>");
					out.println("</div>");
				}
				
				//固定費カテゴリ合計
				
				row++;
				
				//INPUT
				out.println("<input type='hidden' name='INCATEID_" + row + "' value='" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "'>");
				out.println("<input type='hidden' name='INCATENAME_" + row + "' value='" + dhHtml.getString("ITEMCATEGORY_" + (i+1)) + "'>");
				out.println("<input type='hidden' name='INTYPE_" + row + "' value='S'>");
				out.println("<input type='hidden' name='INITEMID_" + row + "' value=' '>");
				out.println("<input type='hidden' name='INITEMNAME_" + row + "' value=' '>");
				out.println("<input type='hidden' name='INITEMPRICE_" + row + "' value='0' id='itemSumPriceName_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "'>");
				
				//表示
				out.println("<table class='itemRowSum' id='itemRowSum_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "'><tr>");
				out.println("<td class='itemSumName'>" + dhHtml.getString("ITEMCATEGORY_" + (i+1)) + "合計</td colspan='4'>");
				out.println("<td id='itemSumPrice_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "'>0</td>");
				out.println("<td class='itemSumBtn' id='itemSumBtn_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "' onclick=\"itemHidden('" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "')\">-</td></tr></table>");
				out.println("<div class='itemTablediv' id='itemTablediv_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "' style='display:block'>");
				out.println("<table class='itemTable' id='itemTable_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "'>");
				out.println("<tr class='itemRowTitle'><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>");
			} 
			
			
			row++;
			
			//INPUT
			out.println("<input type='hidden' name='INCATEID_" + row + "' value='" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "'>");
			out.println("<input type='hidden' name='INCATENAME_" + row + "' value='" + dhHtml.getString("ITEMCATEGORY_" + (i+1)) + "'>");
			out.println("<input type='hidden' name='INTYPE_" + row + "' value='I'>");
			out.println("<input type='hidden' name='INITEMID_" + row + "' value='" + dhHtml.getString("ITEMID_" + (i+1)) + "'>");
			out.println("<input type='hidden' name='INITEMNAME_" + row + "' value='" + dhHtml.getString("ITEMNAME_" + (i+1)) + "'>");
			
			//表示
			out.println("<tr class='itemRowDetail' id='itemRowTr_" + dhHtml.getString("ITEMID_" + (i+1)) + "'>");
			out.println("<td class='itemCheck itemCheckCategory_" + dhHtml.getString("ITEMCATEGORYID_" + (i+1)) + "' id='itemCheck_" + dhHtml.getString("ITEMID_" + (i+1)) + "'>");
			out.println("<input type='checkbox' id='itemChk_" + dhHtml.getString("ITEMID_" + (i+1)) + "' name='INITEMCHECK_" + row + "' value='1' checked>");
			out.println("</td>");
			out.println("<td class='itemName' id='item_" + dhHtml.getString("ITEMID_" + (i+1)) + "'>" + dhHtml.getString("ITEMNAME_" + (i+1)) + "</td>");
			out.println("<td class='itemPrice itemPrice_c" + dhHtml.getString("ITEMID_" + (i+1)) + "' id='itemPr_" + dhHtml.getString("ITEMID_" + (i+1)) + "'>");
			out.println("<input id='itemPrice_" + dhHtml.getString("ITEMID_" + (i+1)) + "' name='INITEMPRICE_" + row + "' type='number' value="+ dhHtml.getString("ITEMPRICE_" + (i+1)) + ">");
			out.println("<input type ='hidden' name='ITEM_PRICEDEFAULT' id='itemPriceDefault_" + dhHtml.getString("ITEMID_" + (i+1)) + "' value="+ dhHtml.getString("ITEMPRICE_" + (i+1)) + ">");
			out.println("</td>");
			out.println("<td><div class='defaultBtn' onclick=\"defaultPrice('" + dhHtml.getString("ITEMID_" + (i+1)) + "')\">Default</div></td>");
			out.println("</tr>");
			shocate = dhHtml.getString("ITEMCATEGORY_" + (i+1));
		}
		out.println("</table>");
		out.println("</div>");
		
		
	//年払い
		
	row++;
		
	//INPUT
	out.println("<input type='hidden' name='INCATEID_" + row + "' value='" + dhHtml.getString("NENITEMCATEID") + "'>");
	out.println("<input type='hidden' name='INCATENAME_" + row + "' value='" + dhHtml.getString("NENITEMCATEGORY") + "'>");
	out.println("<input type='hidden' name='INTYPE_" + row + "' value='I'>");
	out.println("<input type='hidden' name='INITEMID_" + row + "' value='" + dhHtml.getString("NENITEMCATEID") + "'>");
	out.println("<input type='hidden' name='INITEMNAME_" + row + "' value='" + dhHtml.getString("NENITEMCATEGORY") + "'>");
	
	
	//表示		
	out.println("<table class='itemRowSum' id='itemRowSum_" + dhHtml.getString("NENITEMCATEID") + "'><tr>");
	out.println("<td class='itemSumName'>" + dhHtml.getString("NENITEMCATEGORY") + "合計</td colspan='4'>");
	out.println("<td id='itemSumPrice_" + dhHtml.getString("NENITEMCATEID") + "'>0</td>");
	out.println("<td class='itemSumBtn' id='itemSumBtn_" + dhHtml.getString("NENITEMCATEID") + "' onclick=\"itemHidden('" + dhHtml.getString("NENITEMCATEID") + "')\">-</td>");
	out.println("</tr></table>");
	out.println("<div class='itemTablediv' id='itemTablediv_" + dhHtml.getString("NENITEMCATEID") + "' style='display:block'>");
	
	out.println("<table class='itemTable' id='itemTable_" + dhHtml.getString("NENITEMCATEID") + "'>");
	out.println("<tr class='itemRowTitle'><td></td><td>アイテムID</td><td>アイテム</td><td>金額</td></tr>"); 
	out.println("<tr class='itemRowDetail' id='itemRowTr_" + dhHtml.getString("NENITEMID") + "'>");
	out.println("<td class='itemCheck itemCheckCategory_" + dhHtml.getString("NENITEMCATEID") + "' id='itemCheck_" + dhHtml.getString("NENITEMID") + "'>");
	out.println("<input type='checkbox' id='itemChk_" + dhHtml.getString("NENITEMID") + "' name='INITEMCHECK_" + row + "' value='1' checked>");
	out.println("</td>");
	out.println("<td class='itemName' id='item_" + dhHtml.getString("NENITEMID") + "'>" + dhHtml.getString("NENITEMNAME") + "</td>");
	out.println("<td class='itemPrice itemPrice_c" + dhHtml.getString("NENITEMID") + "' id='itemPr_" + dhHtml.getString("NENITEMID") + "'>");
	out.println("<input id='itemPrice_" + dhHtml.getString("NENITEMID") + "' name='INITEMPRICE_" + row + "' type='number' value="+ dhHtml.getString("NENPRICE") + ">");
	out.println("<input type ='hidden' name='ITEM_PRICEDEFAULT' id='itemPriceDefault_" + dhHtml.getString("NENITEMID") + "' value="+ dhHtml.getString("NENPRICE") + ">");
	out.println("</td>");
	out.println("<td><div class='defaultBtn' onclick=\"defaultPrice('" + dhHtml.getString("NENITEMID") + "')\">Default</div></td>");
	out.println("</tr>");

	out.println("</table>");
	out.println("</div>");
	
		out.println("<input type='hidden' name='INROW' value='" + row + "'>");
		
		out.println("<a href='#' onclick='document.FORMMAIN.submit();'>登録</a>");
		
		out.println("</form>");
		out.println("<script type='text/javascript' src='js/keikaku.js'></script>");
		out.println("</body>");
		out.println("</html>");
		
		return;
	}
	
	
	//@SuppressWarnings("null")
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
        
        
        dhHtml.setString("INCOMECATEID", incomeID);
		dhHtml.setString("INCOMECATENAME", ((Vector<String>)income.get(0)).get(2).toString().toString());
		
		//incomeItems = new String[income.size()];
		
		dhHtml.setString("INCOMECOUNT", String.valueOf(income.size()));


		for (int i = 0; i < income.size(); i++) {
			Vector<String> incomeRow = (Vector<String>)income.get(i);

			//incomeItems[i]=incomeRow.get(0).toString().trim();
			dhHtml.setString("INCOMEITEMID_" + (i+1),incomeRow.get(0).toString().trim());
			dhHtml.setString("INCOMEITEMNAME_" + (i+1), incomeRow.get(3).toString().trim());
			dhHtml.setString("INCOMEITEMPRICE_" + (i+1), incomeRow.get(4).toString().trim());
		}
		
		System.out.println(Arrays.toString(incomeItems));
		
		
		resultSet = null;
		setSql  = "";
		setSql = setSql + "select アイテムID, i.カテゴリID as カテゴリID, カテゴリ, アイテム, 金額 ";
		setSql = setSql + "from K_アイテムマスタ  i,K_カテゴリマスタ c ";
		setSql = setSql + "where i.ユーザID = c.ユーザID  ";
		setSql = setSql + " and i.カテゴリID = c.カテゴリID  ";
		setSql = setSql + " and i.ユーザID = '000000'  ";
		setSql = setSql + " and i.カテゴリID <> 'A0001' ";
		setSql = setSql + " and i.カテゴリID <> 'C0001' ";
		setSql = setSql + "order by カテゴリID, アイテムID ";
		
		resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> item = oraConnect.getSqlResult(resultSet);
        
        dhHtml.setString("ITEMCOUNT", String.valueOf(item.size()));

		
        for (int i=0; i<item.size();i++) {
        	Vector<String> itemRow = (Vector<String>)item.get(i);
			dhHtml.setString("ITEMID_" + (i+1),itemRow.get(0).toString().trim());
			dhHtml.setString("ITEMCATEGORYID_" + (i+1), itemRow.get(1).toString().trim());
			dhHtml.setString("ITEMCATEGORY_" + (i+1), itemRow.get(2).toString().trim());
			dhHtml.setString("ITEMNAME_" + (i+1), itemRow.get(3).toString().trim());
			dhHtml.setString("ITEMPRICE_" + (i+1), itemRow.get(4).toString().trim());
        }
        
        System.out.println(item);
        
		
		resultSet = null;
		setSql  = "";
        setSql  = setSql + "select 'C0001' as アイテムID, カテゴリID, '年額' as カテゴリ, '年額支払い' as アイテム, AVG(金額)";
        setSql  = setSql + " from K_アイテムマスタ";
        setSql  = setSql + " where ユーザID = '" + userid + "' ";
        setSql  = setSql + " and カテゴリID = 'C0001' ";
        setSql  = setSql + " group by カテゴリID";

        
        System.out.println(setSql);
        
        resultSet = oraConnect.ExcecuteQuery(setSql);
        Vector<Vector<String>> yearpay = oraConnect.getSqlResult(resultSet);
        Vector<String> yearpRow = (Vector<String>)yearpay.get(0);
        
        System.out.println(yearpRow);
        
        dhHtml.setString("NENITEMID",yearpRow.get(0).toString().trim());
        dhHtml.setString("NENITEMCATEID", yearpRow.get(1).toString().trim());
		dhHtml.setString("NENITEMCATEGORY", yearpRow.get(2).toString().trim());
		dhHtml.setString("NENITEMNAME", yearpRow.get(3).toString().trim());
		dhHtml.setString("NENPRICE", yearpRow.get(4).toString().trim());
        
        

		return false;
	}
	
	
	//--------------------------------------------------------------------------------
	//
	// 文字列から数値(０以上)
	//
	//--------------------------------------------------------------------------------
	int StrToInt(String str) {
		
		try {
			return Integer.parseInt(str);
			
		} catch(Exception e) {
			return 0;
		}
	}


}