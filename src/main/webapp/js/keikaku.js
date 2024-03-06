/**
 * 
 */

//document.getElementById("test").innerText="testtest";

var itemCategory;
var itemCategoryID=[];
var itemID=[];
var sumPrice=[];
var itemhidden= [] //itemCategoryごとの表示非表示　0:表示 1:非表示

const load =()=> {
	//document.getElementById("test").innerText="test";
	itemCategory = document.getElementsByClassName("itemTable");
	for (var i=0;i < itemCategory.length;i++) {
		//alert(itemCategory[i].id);
		itemCategoryID[i]=itemCategory[i].id.replace("itemTable_","");
		itemhidden[i]=0;
		var categoryItem = document.getElementsByClassName("itemNoCategory_" + itemCategoryID[i]);
		var koID=[]
		//var priceSum=0;
		for (var j=0;j < categoryItem.length;j++){
			koID[j]=categoryItem[j].id.replace("itemNo_","");
			//console.log("name:" + "itemPrice_"+koID[j]);
			//console.log("pr:" + eval(document.getElementById("itemPrice_"+koID[j]).value));
			//priceSum+=eval(document.getElementById("itemPrice_"+koID[j]).value);
		}
		itemID[i]=koID;
		//console.log("sumpr:" + priceSum);
		//console.log("cID:" + "itemSumPrice_"+itemCategoryID[i]);
		//document.getElementById("itemSumPrice_"+itemCategoryID[i]).innerText=priceSum;
		//sumPrice[i]=priceSum;
	}
	
	getSumPrice();
	
	//console.log(itemCategoryID);
	//console.log(itemID);
	//console.log(sumPrice);
	
	itemHidden('A0001');
}

window.addEventListener('DOMContentLoaded', function(){
  
  let input_itemPrice = document.querySelectorAll("input[name=itemPrice]");

		if( 0 < input_itemPrice.length ) {

			for(let itemPrice of input_itemPrice) {

				itemPrice.addEventListener("change",function(){
					console.log("Change action num");
					getSumPrice();			
				});

				itemPrice.addEventListener("input",function(){
					console.log("Input action num");
					getSumPrice();		
				});
			}
		}
		
  let input_itemCheck = document.querySelectorAll("input[name=itemCheck]");

		if( 0 < input_itemCheck.length ) {

			for(let itemCheck of input_itemCheck) {

				itemCheck.addEventListener("change",function(){
					console.log("Change action check");
					getSumPrice();			
				});

				itemCheck.addEventListener("input",function(){
					console.log("Input action check");
					getSumPrice();		
				});
			}
		}
});

const itemHidden =(CId)=> {
	//console.log(CId);
	var cindex=getCategoryIndex(CId);
	//console.log(cindex);
	if (itemhidden[cindex]==0) {
		document.getElementById('itemTablediv_' + itemCategoryID[cindex]).style.display='none';
		document.getElementById("itemSumPrice_"+itemCategoryID[cindex]).innerText="";
		itemhidden[cindex]=1;
		document.getElementById('itemSumBtn_' + itemCategoryID[cindex]).innerText='+';
	} else {
		document.getElementById('itemTablediv_' + itemCategoryID[cindex]).style.display='block';
		document.getElementById("itemSumPrice_"+itemCategoryID[cindex]).innerText=sumPrice[cindex];
		itemhidden[cindex]=0;
		document.getElementById('itemSumBtn_' + itemCategoryID[cindex]).innerText='-';
	}
}

const itemHiddenUpd =()=> {
	for(var i=0;i < itemCategoryID.length;i++){
		if (itemhidden[i]==0) {
			document.getElementById('itemTablediv_' + itemCategoryID[i]).style.display='block';
			document.getElementById("itemSumPrice_"+itemCategoryID[i]).innerText=sumPrice[i];
			document.getElementById('itemSumBtn_' + itemCategoryID[i]).innerText='-';
		} else {
			document.getElementById('itemTablediv_' + itemCategoryID[i]).style.display='none';
			document.getElementById("itemSumPrice_"+itemCategoryID[i]).innerText="";
			document.getElementById('itemSumBtn_' + itemCategoryID[i]).innerText='+';
		}
	}
}

const getCategoryIndex =(CId)=> {
	for (var i=0;i < itemCategoryID.length;i++){
		if(itemCategoryID[i]==CId){
			return i;
		}
	}
	//見つからなかったとき
	return -1;
}

const getSumPrice =()=> {
	
	for (var i=0;i < itemCategory.length;i++) {
		var categoryItem = document.getElementsByClassName("itemNoCategory_" + itemCategoryID[i]);
		var priceSum=0;
		for (var j=0;j < categoryItem.length;j++){
			//console.log("name:" + "itemPrice_"+ itemID[i][j]);
			//console.log("pr:" + eval(document.getElementById("itemPrice_"+itemID[i][j]).value));
			if (document.getElementById("itemChk_" + itemID[i][j]).checked) {
				priceSum+=eval(document.getElementById("itemPrice_"+itemID[i][j]).value);
			}
		}
		//console.log("sumpr:" + priceSum);
		//console.log("cID:" + "itemSumPrice_"+itemCategoryID[i]);
		document.getElementById("itemSumPrice_"+itemCategoryID[i]).innerText=priceSum;
		sumPrice[i]=priceSum;
	}
	//console.log(itemCategoryID);
	//console.log(itemID);
	//console.log(sumPrice);
	var koteiSum = 0;
	for (var i=1;i<sumPrice.length;i++){
		koteiSum+=sumPrice[i];
	}
	document.getElementById("itemSumPrice_kotei").innerText=koteiSum;
	itemHiddenUpd();
}

const defaultPrice =(id)=> {
	document.getElementById('itemPrice_' + id).value = document.getElementById('itemPriceDefault_' + id).value;
} 