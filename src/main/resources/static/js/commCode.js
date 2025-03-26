	 function comboList(combo,cmpyCd,comboType,commCd,comdCd,useYn,top)	//콤보박스,회사코드,공통콤보유형,공통코드,사용여부
	 {
		 var comboForm = new FormData();
		 comboForm.append("cmpyCd",cmpyCd);
		 comboForm.append("comboType",comboType);
		 comboForm.append("commCd",commCd);
		 comboForm.append("comdCd",comdCd);
		 comboForm.append("useYn",useYn);

		$.ajax({
	        url: "/comComboList",
	        processData : false,
			contentType : false,
	        data: comboForm,
	        type:"POST",
	        datatype:"json",
	        async:false,		//동기식
	        success : function(res){
	        	if(res)	
	        	{	
	        		if(top == 'all')
	        		{
	        			combo.addOption([{value:"", text:"전체"}]);
	        		}
	        		if(top == 'req')
	        		{
	        			combo.addOption([{value:"", text:"선택하세요"}]);
	        		}
	        		if(top == 'chk')
	        		{
	        			combo.addOption([{value:"", text:"체크하세요"}]);
	        		}
	        		for(var i=0;i<res.length;i++)
	        		{
	        			combo.addOption([{value:res[i].comboCd, text:res[i].comboNm}]);
	        		}
	        	}
	        	else	
	        	{
	        		alert("콤보조회 로직 조회 실패");
	        	}
	        },
	        error : function(res) {
	        	alert("콤보조회 로직 수행 실패");
			}
		});
	 }
	 
	 

	 