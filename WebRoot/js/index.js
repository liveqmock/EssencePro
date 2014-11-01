var setting = {
	check : { // **复选框**/
		enable : false,
		chkboxType : {
			"Y" : "ps",
			"N" : "ps"
		}
	},
	view : {
		// dblClickExpand: false,
		expandSpeed : 300
	// 设置树展开的动画速度，IE6下面没效果，
	},
	data : {
		simpleData : { // 简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的
			enable : true,
			idKey : "id", // id和pid，这里不用多说了吧，树的目录级别
			pIdKey : "pId",
			rootPId : 0
		// 根节点
		}
	},
	callback : { // **回调函数的设置，随便写了两个**/
		beforeClick : beforeClick,
		onCheck : onCheck
	}
};
function beforeClick(treeId, treeNode) {
	alert("beforeClick");
}
function onCheck(e, treeId, treeNode) {
	alert("onCheck");
}

var citynodes = [ // **自定义的数据源，ztree支持json,数组，xml等格式的**/
{
	id : 0,
	pId : -1,
	name : "中国"
}, {
	id : 1,
	pId : 0,
	name : "北京"
}, {
	id : 2,
	pId : 0,
	name : "天津"
}, {
	id : 3,
	pId : 0,
	name : "上海"
}, {
	id : 6,
	pId : 0,
	name : "重庆"
}, {
	id : 4,
	pId : 0,
	name : "河北省",
	open : false,
	nocheck : true
}, {
	id : 41,
	pId : 4,
	name : "石家庄"
}, {
	id : 42,
	pId : 4,
	name : "保定"
}, {
	id : 43,
	pId : 4,
	name : "邯郸"
}, {
	id : 44,
	pId : 4,
	name : "承德"
}, {
	id : 5,
	pId : 0,
	name : "广东省",
	open : false,
	nocheck : true
}, {
	id : 51,
	pId : 5,
	name : "广州"
}, {
	id : 52,
	pId : 5,
	name : "深圳"
}, {
	id : 53,
	pId : 5,
	name : "东莞"
}, {
	id : 54,
	pId : 5,
	name : "佛山"
}, {
	id : 6,
	pId : 0,
	name : "福建省",
	open : false,
	nocheck : true
}, {
	id : 61,
	pId : 6,
	name : "福州"
}, {
	id : 62,
	pId : 6,
	name : "厦门"
}, {
	id : 63,
	pId : 6,
	name : "泉州"
}, {
	id : 64,
	pId : 6,
	name : "三明"
}, {
	id : 7,
	pId : 0,
	name : "四川省",
	open : true,
	nocheck : true
}, {
	id : 71,
	pId : 7,
	name : "成都"
}, {
	id : 72,
	pId : 7,
	name : "绵阳"
}, {
	id : 73,
	pId : 7,
	name : "自贡"
}, {
	id : 711,
	pId : 71,
	name : "金牛区"
}, {
	id : 712,
	pId : 71,
	name : "锦江区"
}, {
	id : 7111,
	pId : 711,
	name : "九里堤"
}, {
	id : 7112,
	pId : 711,
	name : "火车北站"
} ];

$(function() {
	// 编辑器
	//KindEditor.create($("#editor"), editorOption);
	// 树形结构
	//$.fn.zTree.init($("#tree"), setting, citynodes);
	//
	var refreshAuthCode = function() {
		var src = context+'/authCode.do?' + Math.random();
		$('img#authCode').attr('src', src);
	};
	$("#authCode").click(refreshAuthCode);
});



function zTreeOnClick(event, treeId, treeNode) {
	alert(treeNode.tId + "," + treeNode.name);
}

function abc() {

	var a = document.getElementById("project_id");
	getPinyin(a, 'pym');

}