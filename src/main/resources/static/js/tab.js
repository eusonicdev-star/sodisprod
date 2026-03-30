var button='<button class="close" type="button" title="Remove this page">×</button>';
var tabID = 1;

var closeTabSpanID = ""; //닫은 span 탭ID
var closeTabAID = "";  //닫은 탭의 a href 경로 
function resetTab(){
	var tabs=$("#tab-list li:not(:first)");		//eq  레벨단계
	var tabContents=$("#tab-content div:not(:first)");	//eq  레벨단계

	var len=1;
	var lenContents=1;

	$(tabContents).each(function(k,v){	//x탭하단 콘텐츠 부분
		var contentTabId = $(this).attr('id'); //아래나오는 내용부분
		if ( contentTabId != undefined && contentTabId != null && contentTabId !='' && contentTabId.substring(0,3) == 'tab' && $(this).hasClass("sys_tab_content"))
		{
			lenContents++;
			$(this).attr('id','tab'+lenContents);
		}
	});
	
	$(tabs).each(function(k,v){
		len++;
		$(this).find('a').attr('href','#tab'+len);	//상단 tab_list 안에 있는 탭버튼들의 순번을 변경
	});

	tabID--;
}

$(document).ready(function() {
	$('#tab-list').on('click','.tabBtnClick', function(e) {

		var tabBtnClick=$("#tab-list li");		//eq  레벨단계
		$(tabBtnClick).each(function(k,v){
			$(this).find('a').find('span').css({'font-weight':'normal','color':'black','border':'none'});
		});
	
		e.target.style.fontWeight = 'bold';

	});
    
	//탭에서 x 버튼 눌러서 닫을때
    $('#tab-list').on('click', '.close', function() {
		var tabAID = $(this).parents('a').attr('href');
		var tabSpanID = $(this).parents('a').find('span').attr("id");
		closeTabSpanID = tabSpanID;
		closeTabAID = tabAID;
		$(this).parents('li').remove();
        $(tabAID).remove();	//탭버튼 누르면 보여지는 내용이 나오는 div를 제거

        resetTab();

		var lastTabIdNo=0
		var lastTabId="";
		var lastTabSpanId="";
		var tabListOnClose=$("#tab-list li:not(:first)");	//첫번째 탭은 빼고 갯수를 구한다
		
		$(tabListOnClose).each(function(k,v){

			lastTabIdNo = (k+2); //첫번째 탭은 빼고 갯수를 구해서 1을 더하고, k는 0부터 시작해서 1을 더함
			lastTabId = $(this).find('a').attr('href');	//탭의 아이디
			lastTabSpanId= $(this).find('a').find('span').attr('id');

			if($(this).find('a').attr('href') == closeTabAID)
			{
				$("#" + $(this).find('a').find('span').attr('id')).click();
				return;
			}
		});
		
		//닫은 탭 아이디하고 남은 탭의 마지막 탭의 탭아이디에 1을 더한 탭 아이디 가 같으면 (= 삭제한탭이 마지막 탭 이라면)
		if(closeTabAID == "#tab"+(lastTabIdNo+1))
		{
			console.log("삭제한 탭이 마지막탭 - " + "a href #tab : " +lastTabIdNo +", span id : " + lastTabSpanId);
			$("#"+lastTabSpanId).click();
			return;
		}
		
		//첫번째 탭은 빼고 구한 탭의 갯수가 0 이면
		if(tabListOnClose.length == 0)
		{
			console.log('안지워지는 첫번째 탭만 남음 - #tab1 - mainPage');
			$("#mainPage").click();
			return;
		}
    });


	$("#mainPage").click();	//시작 하자마자 메인페이지 클릭한걸로 해서 메인페이지 오픈함
});

function btnAdd(idForTab,nameForTab,url) {
	var tabs2=$("#tab-list li:not(:first) a span:nth-child(1)");
	var thisVal=idForTab;
	var cnt=0;
	$(tabs2).each(function(k,v){

		if(thisVal == v.id)
		{
			cnt++;
		}
	});

	if(cnt != 0)
	{
		$('#'+thisVal).click();
		return;
	}

    if(tabID >= 5)
    {
        alert("화면을 더이상 열 수 없습니다. 최대 5 페이지입니다.");
        return;
    }
    tabID++;

	$('#tab-list').append($('<li><a href="#tab' + tabID + '" role="tab" data-toggle="tab"><span  id='+ idForTab +' class="tabBtnClick">&nbsp;&nbsp;' + nameForTab + '</span><button class="close" type="button" title="창닫기">×</button></a></li>'));
	$('#tab-content').append($('<div class="sys_tab_content tab-pane fade" id="tab' + tabID + '">Tab ' + idForTab + ' content</div>'));
	$('#tab'+tabID).load(url);

	$("#" + idForTab).click();
};


function btnLogOut() {
	var resultConfirm = confirm("로그아웃 하시겠습니까?");
	if (resultConfirm){
		window.location.href = './logout';
	}
};
