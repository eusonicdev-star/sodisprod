
// Magnific Popup 


$('.open-popup-link').magnificPopup({
	  type:'inline',
	  closeOnContentClick : false,	//팝업 안쪽을 클릭할때 닫기 
	  closeOnBgClick: false,			//팝업 바깥영역 백그라운드를 클릭할때 닫기
	  fixedContentPos:false,
	  midClick: true // Allow opening popup on middle mouse click. Always set it to true if you don't provide alternative source in href.
	});
	
	
	
$('.ajax-popup-link').magnificPopup({
	  type: 'ajax',
		 closeOnContentClick : false,
		  closeOnBgClick: false,
		  midClick: true ,
		  closeBtnInside : false
	});
	

$('.iframe-popup-link').magnificPopup({
	  type: 'iframe',
		 closeOnContentClick : false,
		  closeOnBgClick: false,
		  midClick: true ,
		  closeBtnInside : false
	});
	


	

// Magnific Popup end	

