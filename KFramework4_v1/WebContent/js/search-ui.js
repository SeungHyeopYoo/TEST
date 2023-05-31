function getdevice() {
	if ($(window).width() < 768) return 'xs';
	else if ($(window).width() >= 768 && $(window).width() < 992) return 'sm';
	else if ($(window).width() >= 992 && $(window).width() < 1200) return 'md';
	else return 'lg';
}
(function( $, undefined) {

	$(window).scroll(function() {
		if (!$('.right-sidebar').length) return;
		var h = $('.right-sidebar').height() + $('.right-sidebar').offset().top - $('.scrolltop').height() - 30;
		var t = $(document).scrollTop();
		if (t > h) setTimeout(function() { $('.scrolltop').stop().animate({ top: t - h }, 1000/15, 'swing'); }, 000);
		else $('.scrolltop').stop().animate({ top: '0' }, 1000/15, 'swing');
	});
	$(document).ready(function() {
		$('[data-toggle=date]').datepicker({
			changeYear: true, changeMonth: true, showOn: "both", buttonImage: 'images/btn_calendar.gif'
		}).inputmask('yyyy.mm.dd');
		
		$('.sidebar').on('click', '.nav li', function(e) {
			var $this = $(this);
			if (!$this.hasClass('active')) {
				var $active = $this.siblings('.active');
				if ($active) {
					$active.removeClass('active');
				}
				$this.addClass('active');
			}
		});
	});

})( jQuery );

(function ($, undefined) {

$.widget( "konan.icon", {
	options: {
		filter: '*',
		icons: {
			'doc': [ 'doc', 'docx' ],
			'gul': [ 'gul' ],
			'hwp': [ 'hwp' ],
			'pdf': [ 'pdf' ],
			'ppt': [ 'ppt', 'pptx' ],
			'txt': [ 'txt' ],
			'xls': [ 'xls', 'xlsx' ]
		},
		defaultIcon: 'etc',
		imagePrefix: 'images/ico_',
		imageExt: 'gif'
	},

	_getIcon: function(s) {
		var i = this.options.defaultIcon;
		var p = s.lastIndexOf('.');
		if (p >= 0) {
			var e = s.substring(p + 1);
			
			$.each(this.options.icons, function(k, v) {
				if ($.inArray(e, v) >= 0) {
					i = k; return false; // break;
				}
			});
		}
		return i;
	},

	_create: function() {
		var self = this,
			o = self.options;
		self.element.each(function() {
			var $this = $(this);
			$this.before($('<img src="' 
				+ o.imagePrefix 
				+ self._getIcon($this.text()) 
				+ '.' 
				+ o.imageExt + '"/>'));
		});
	}

});

}(jQuery));