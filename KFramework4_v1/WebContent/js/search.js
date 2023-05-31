// 페이지 선택
function gotopage(p) {
	$('#categorize').val(false); // 페이지 이동의 경우 새로 카테고리 그루핑할 필요 없음
	$('#page').val(p);
	$('#pageNum').val((p+1));
	$('#contents').load($('#historyForm').attr('action'), $('#historyForm').serializeArray());
	$('#historyForm').submit();
}
//20160706, 현재날짜 조회
function getTodate(){
	var rightNow = new Date();
	var res = rightNow.toISOString().slice(0,10).replace(/-/g,"");
	
	return ($('#nowDate').val()).length > 7 ? ('#nowDate').val(): rightNow.toISOString().slice(0,10).replace(/-/g,""); 	
}

//20160706, 현재 날짜계산기준으로 날짜 계산
function getAddDate(dtFg) {
	if(dtFg==null || dtFg =="") {
		return "";
	}
	var nowDate = getTodate();
	
	var yy = parseInt(nowDate.substr(0, 4));
	var mm = parseInt(nowDate.substr(4, 2));
	//var dd =  parseInt(nowDate.substr(7));	;20180502 수정
	var dd =  parseInt(nowDate.substr(6));
    
    if("d" == dtFg){
    	dd = dd-1;
    }else  if("w" == dtFg){
    	dd = dd-7;
    }else  if("m" == dtFg){
    	mm = mm-1
    }else  if("y" == dtFg){
    	yy = yy-1
    }
    
    date = new Date(yy, mm - 1, dd );
    yy = date.getFullYear();
    mm = date.getMonth() + 1; mm = (mm < 10) ? '0' + mm : mm;
    dd = date.getDate(); dd = (dd < 10) ? '0' + dd : dd;
    
    return yy+''+mm+''+dd;
}

// 카테고리 선택
function goCategory( val ) {
	$('#category').val(val);
	$('#historyForm').submit();
}

// 조건 선택
function goCondition( val ) {
	$('#condition').val(val);
	$('#historyForm').submit();
}


$(function() {
	
	// 첨부파일 미리보기
	$('#contents').on('click', 'span.preview', function() {
		// 미리보기
		var $base = $(this).parents(".media:first"),
			$that = $base.find(".preview-dialog");
		
		//alert('index of file: ' + $base.find('.preview.label.label-default').index($(this)));
		var indexOfFile = $base.find('.preview.label.label-default').index($(this));

		/*
		if ($that.length == 0) {
			$.ajax({
				type: 'GET',
				url: 'dispatch.jsp',
				data: { view: 'preview', query: $('#query').val(), rowid: $base.find('input[name=rowid]').val() },
				dataType: 'html',
				success: function(data) {
					$('#preview-dialog').clone()
						.attr('id', '')
						.find('.modal-body p').html(data).end()
						.appendTo($base).collapse('show');
				}
			});
		} else {
			$that.collapse('toggle');
		}
		*/
		$that.eq(indexOfFile).collapse('toggle');
	}).on('click', '.preview-dialog .close', function() {
		// 미리보기 대화상자 닫기
		$(this).parents('.preview-dialog').collapse('hide');
	});
	
	// 검색 타깃 초기화 및 사이드바 위젯 초기화
	$('#sidebar')
		.find('[data-target=' + $('#category').val() + ']').addClass('active').end()
		.find('form').deserialize($('#historyForm').serializeArray());

	// 사이드바 위젯 - 첨부 초기화
	if ($('#fileext').val()) { // 전체 선택이 아닐 경우
		var initValues = $('#fileext').val().split(',');
		$('#sidebar').find('input[name=fileext]').each(function() {
			$(this).prop('checked', ($.inArray($(this).val(), initValues) != -1));
		});
	}

	// 결과 내 재검색 플래그 설정
	if ($('#resrch').val() == 'true') {
		$('header form').find('input[name=resrch]').prop('checked', true);
	}

	// 기간 직접 입력 폼 보이기/숨기기
	if ($('#date').val() == 'r') {
		$('#date-radio').hide(), $('#date-input').show();
	}

	/* Event Handlers */

	/* 검색 폼 제출 시 */
	$('#historyForm').submit(function(e) {

		$('#categorize').val($('#category').val() != 'TOTAL');
		$('#page').val(0);
		//$('#category').val('');
		$('#resrch').val($('header form input[name=resrch]').is(':checked'));
		$('#recent').recent('add', $('#kwd').val()); // 최근 검색어 추가
	});

	/* 상/하단 키워드 입력 폼을 통한 검색 시 */
	$('header form, footer form').submit(function(e) {
		e.preventDefault();
		e.stopPropagation();
		if ($.trim($(this).find('input[name=kwd]').val())) {
			$('#historyForm').deserialize($(this).serializeArray()).submit();
		} else {
			alert('검색어를 입력해주세요.');
		}
	});
	/* 검색어 추천, 인기검색어, 최근검색어 클릭 시 */
	$('#rankings, #recent').on('click', 'a', function() {
		$('#kwd').val($(this).text());
		$('#historyForm').submit();
	});
	
	/* 사이드바 영역 이벤트 */
	$('#sidebar').on('click', '.nav > li > a', function() {
		// 검색 타깃 선택 시
		$('#fileext').val('');
		$('#target').val( $(this).parent().attr('data-target') );
		$('#historyForm').submit();
	}).on('change', 'input:radio[name=sort]', function(e) {
		// 사이드바 위젯 - 정렬
		$('#sort').val($(this).val());
		$('#categorize').val(false);
		$('#contents').load($('#historyForm').attr('action'), $('#historyForm').serializeArray());
		$('#historyForm').submit();
	}).on('change', 'input:radio[name=fields]', function(e) {
		// 사이드바 위젯 - 영역 : 20160706 수정
		$('#historyForm').deserialize(
				$(e.delegateTarget).find('form').serializeArray()
			).submit();		
	}).on('change', 'input:radio[name=date]', function(e) {
		//날짜 설정 20160706 추가
		$('#startDate').val(getAddDate($(this).val()) );
		$('#endDate').val(getTodate());
		// 사이드바 위젯 -  기간
		$('#historyForm').deserialize(
				$(e.delegateTarget).find('form').serializeArray()
			).submit();
	}).on('click', '#open-date-input', function(e) {
		// 사이드바 위젯 - 기간 > 직접입력
		$('#date-radio').hide();
		$('#date-input').show();
		return false;
	}).on('click', '#close-date-input', function(e) {
		// 사이드바 위젯 - 기간 > 직접입력 > 닫기
		$('#date').val($('#date-radio').find("input:radio:checked").val());
		$('#date-input').hide();
		$('#date-radio').show();
		return false;
	}).on('click', '#apply-date-input', function(e) {
		// 사이드바 위젯 - 기간 > 직접입력 > 적용
		$('#date').val('r');
		//날짜 설정 20160706 추가
		$('#startDate').val( $('#date-input').find("input:text[name=date-from]").val() );
		$('#endDate').val( $('#date-input').find("input:text[name=date-to]").val() );		
		$('#historyForm').deserialize(
				$('#date-input').find('input').serializeArray()
			).submit();
	}).on('change', 'input[name=fileext]', function() {
		// 사이드바 위젯 - 확장자 선택
		var $sw = $(this).parents(".sidebar-widget");
		// 개별 확장자 선택 시 전체 선택 해제.
		if ($(this).val()) $sw.find(':checkbox[value=""]').prop('checked', false);
		// 전체 선택 시 개별 확장자 선택 해제
		else if ($(this).is(':checked')) $sw.find(':checkbox').not(this).prop('checked', false);
	}).on('click', '#apply-fileext', function() {
		// 사이드바 위젯 - 확장자 검색
		var $sw = $(this).parents(".sidebar-widget");
		$('#fileext').val(
			$sw.find('input[name=fileext]:checked').map(function() { return $(this).val(); }).get().join()
		);
		$('#historyForm').submit();
	}).on('click', '#toggle-widget', function() {
		// 반응형 - 사이드바 위젯 모두 펼침/닫기
		var $sw = $(this).parents(".sidebar-widget");
		$('#sidebar').find('.sidebar-widget').not($sw).toggle();
		$(this).toggleClass('fa-chevron-up fa-chevron-down');
	});

	/* 검색 결과 영역 이벤트 */
	$('#contents').on('click', '.more > a', function() {
		// 결과 더보기 - 통합검색
		$('#category').val($(this).attr('data-target'));
		$('#historyForm').submit();
	});

	/* 상세 검색 */
	$('#detailed-search').click(function() {
		$('<div>').attr('id', 'detailed-search-dialog').dialog({
			title: '상세검색',
			// 검색 결과 영역의 상단에 맞춰 대화상자 위치
    		position: { my: 'left top', at: 'left+10 top+2', of: '.main-contents' },
    		width: $('.main-contents').width(),

			open: function() { $(this).load('dispatch.jsp?view=detailed-search'); },
			close: function() { $(this).remove(); }
		});
	});

	/* 카테고리 필터링 - 트리 */
	$('.category-tree').fancytree({ 
		icons: false, 
		activate: function(event, data) {
			$('#categorize').val(false); // 카테고리 필터링의 경우 새로 카테고리 그루핑할 필요 없음
			$('#page').val(0);
			$('#category').val(data.node.key);
			$('#contents').load($('#historyForm').attr('action'), $('#historyForm').serializeArray());
		}
	});

	/* 카테고리 필터링 - 리스트 */
	$('.category-list').on('click', 'li', function() {
		$('#categorize').val(false); // 카테고리 필터링의 경우 새로 카테고리 그루핑할 필요 없음
		$('#page').val(0);
		$('#category').val($(this).attr('data-group'));
		$('#contents').load($('#historyForm').attr('action'), $('#historyForm').serializeArray());
	});
});

