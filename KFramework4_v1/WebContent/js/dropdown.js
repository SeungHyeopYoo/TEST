$(function() {
	var openDropdown = null;

	$('.dropdown-toggle').click(function() {
	    // 클릭한 메뉴의 하위 ul 요소를 가져옵니다.
	    var dropdown = $(this).children('ul');
	
	    // 열린 드롭다운 메뉴가 있으면 닫습니다.
	    if (openDropdown && openDropdown[0] !== dropdown[0]) {
	    	openDropdown.hide();
			$(openDropdown).parent().removeClass('active');
	    }
	
	    // 현재 클릭한 메뉴의 드롭다운 메뉴를 토글합니다.
	    dropdown.toggle();
	
	    // 열린 드롭다운 메뉴를 업데이트합니다.
	    openDropdown = dropdown.is(':visible') ? dropdown : null;
  		$(this).toggleClass('active');
	});
});


