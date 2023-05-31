<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form id="detailed-search-form" class="form-horizontal" role="form">
	<div class="form-group form-group-sm">
		<label class="col-md-2 control-label">키워드</label>
		<div class="col-md-10">
			<input class="form-control" type="text" name="kwd" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">메뉴</label>
		<div class="col-md-10">
			<label class="radio-inline"><input type="radio" name="category" value="TOTAL"/> 통합검색</label>
			<label class="radio-inline"><input type="radio" name="category" value="PERSON"/> 인물</label>
			<label class="radio-inline"><input type="radio" name="category" value="BOARD"/> 게시판</label>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">정렬</label>
		<div class="col-md-10">
			<label class="radio-inline"><input type="radio" name="sort" value="r" checked/> 정확도</label>
			<label class="radio-inline"><input type="radio" name="sort" value="d"/> 최신순</label>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">영역</label>
		<div class="col-md-10">
			<label class="radio-inline"><input type="radio" name="fields" value="" checked/> 전체</label>
			<label class="radio-inline"><input type="radio" name="fields" value="title"/> 제목</label>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">기간</label>
		<div class="col-md-10">
			<label class="radio-inline"><input type="radio" name="date" value=""/> 전체</label>
			<label class="radio-inline"><input type="radio" name="date" value="d"/> 1일</label>
			<label class="radio-inline"><input type="radio" name="date" value="w"/> 1주</label>
			<label class="radio-inline"><input type="radio" name="date" value="m"/> 1달</label>
			<label class="radio-inline"><input type="radio" name="date" value="y"/> 1년</label>
			<span  class="span-inline">
				<label class="radio-inline"><input type="radio" name="date" value="r"/> 직접입력 </label>
				<input type="text" name="startDate" data-toggle="date"/> - <input type="text" name="endDate" data-toggle="date"/>
			</span>
		</div>
	</div>
	<!-- 문서유형 -->
	<!-- <div class="form-group">
		<label class="col-md-2 control-label">문서유형</label>
		<div class="col-md-10">
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value=""/> 전체</label>
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value="hwp"/> HWP</label>
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value="doc"/> DOC</label>
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value="ppt"/> PPT</label>
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value="xls"/> XLS</label>
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value="pdf"/> PDF</label>
			<label class="checkbox-inline"><input type="checkbox" name="fileext" value="etc"/> 기타</label>
		</div>
	</div>-->
	<div class="form-group">
		<label class="col-md-2 control-label">제외어</label>
		<div class="col-md-10">
			<input class="form-control" type="text" name="notWord"/>
			<span class="help-block">여러 단어를 입력하실 때는 쉼표(,)로 구분</span>
		</div>
	</div> 
	<div class="dialog-buttonpane">
		<div class="dialog-buttonset">
			<button type="submit" class="btn btn-default">검색</button>
			<button type="reset" class="btn btn-default">초기화</button>
		</div>
	</div>
</form>
<script>
$(function() {
	var $form = $('#detailed-search-form');
	$form.deserialize( $('#search-form').serializeArray() )
		.find('input[name=category][value="'+ $('#category').val() + '"]').prop('checked', true).end()
		.find('[data-toggle=date]').datepicker({
			changeYear: true, changeMonth: true, showOn: "both", buttonImage: 'images/btn_calendar.gif'
		}).inputmask('yyyy.mm.dd');
	if ($('#fileext').val()) { // 전체 선택이 아닐 경우
		var initValues = $('#fileext').val().split(',');
		$form.find('input[name=fileext]').each(function() {
			$(this).prop('checked', ($.inArray($(this).val(), initValues) != -1));
		});
	}
	
	$form.on('submit', function(e) {
		e.preventDefault();
		
		if ($.trim($(this).find('input[name=kwd]').val())) {
			$('#historyForm').deserialize( $(this).serializeArray() ).submit();
		} else {
			alert('검색어를 입력해주세요.');
		}
	}).on('reset', function(e) {
		e.preventDefault();
		$(this).find('input[type=text]').val('');
		$(this).find('input:radio,input:checkbox').val(['']);
	}).on('change', 'input[name=fileext]', function() {
		var $sw = $(this).parents('.form-group');
		// 개별 확장자 선택 시 전체 선택 해제.
		if ($(this).val()) $sw.find(':checkbox[value=""]').prop('checked', false);
		// 전체 선택 시 개별 확장자 선택 해제
		else if ($(this).is(':checked')) $sw.find(':checkbox').not(this).prop('checked', false);
	});
	
	$form.on('change', 'input:radio[name=date]', function(e) {
		var selValue = $(this).val();
		if(selValue !="" &&  selValue !="r"  ){
			alert("A");
			$form.find('input:text[name=startDate]').val(getAddDate(selValue));
			$form.find('input:text[name=endDate]').val(getTodate() );
		}	
	});
});
</script>