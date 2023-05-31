/*
 * https://github.com/dkastner/jquery.iecors/blob/master/jquery.iecors.js
 */
(function( jQuery ) {
  // Create the request object
  // (This is still attached to ajaxSettings for backward compatibility)
  jQuery.ajaxSettings.xdr = function() {
    return (window.XDomainRequest ? new window.XDomainRequest() : null);
  };

  // Determine support properties
  (function( xdr ) {
    jQuery.extend( jQuery.support, { iecors: !!xdr });
  })( jQuery.ajaxSettings.xdr() );

  // Create transport if the browser can provide an xdr
  if ( jQuery.support.iecors ) {

    jQuery.ajaxTransport(function( s ) {
      var callback,
        xdr = s.xdr();

      return {
        send: function( headers, complete ) {
          xdr.onload = function() {
            var headers = { 'Content-Type': xdr.contentType };
            complete(200, 'OK', { text: xdr.responseText }, headers);
          };
          
          // Apply custom fields if provided
                                        if ( s.xhrFields ) {
            xhr.onerror = s.xhrFields.error;
            xhr.ontimeout = s.xhrFields.timeout;
                                        }

          xdr.open( s.type, s.url );

          // XDR has no method for setting headers O_o

          xdr.send( ( s.hasContent && s.data ) || null );
        },

        abort: function() {
          xdr.abort();
        }
      };
    });
  }
})( jQuery );

/*! jQuery KONAN
 * Includes: jquery.konan.autocomplete.js, jquery.konan.rankings.js, jquery.konan.suggest.js
 */
(function( $, undefined ) {

$.konan = $.konan || {};

// prevent duplicate loading
$.extend( $.konan, {
	version: "1.0.0"
});

})( jQuery );

// support
(function( $, undefined) {

	$(document).on("click", ".box .box-collapse", function(e) {
	    e.preventDefault();
	    $(this).parents(".box:first").toggleClass("box-collapsed");
		$(this).toggleClass("ui-icon-carat-1-n ui-icon-carat-1-s");
	    return false;
	});

})( jQuery );

/* jQuery KONAN Autocomplete
 * Depends: jquery.ui.core.js, jquery.ui.widget.js, jquery.ui.position.js, jquery.ui.menu.js
 */
(function( $, undefined ) {

// used to prevent race conditions with remote data sources
var requestIndex = 0;

$.widget( "konan.autocomplete", {

	options: {
		appendTo: null,
		delay: 150,
		min_length: 1,
		mode: 's',
		domain_no: 0,
		max_count: 10,
		placement: "top",
		flhtml: null,
		source: null,
		dataType: "json",
		footer: {}
	},

	_create: function() {
		this.valueMethod = this.element[ "val" ];
		this.element.addClass( "konan-autocomplete-input" ).attr( "autocomplete", "off" );
		this.element.parents("form:first").attr( "autocomplete", "off" );
		$("<span class=\"ui-icon ui-icon-triangle-1-s\"></span>").insertAfter(this.element).position({my:"right-10 center", at:"right center", of:this.element});

		this._on( this.element, {
			keydown: function( event ) {
				var keyCode = $.ui.keyCode;
				switch ( event.keyCode ) {
				case keyCode.UP:
					this._keyEvent("previous", event );
					break;
				case keyCode.DOWN:
					this._keyEvent("next", event );
					break;
				case keyCode.ENTER:
				case keyCode.NUMPAD_ENTER:
					// when menu is open and has focus
					if ( this.menu.active ) {
						event.preventDefault();
						this.menu.select( event );
					}
					break;
				case keyCode.ESCAPE:
					if ( this.menu_wrap.is( ":visible" ) ) {
						this._value( this.term );
						this.close( event );
						// Different browsers have different default behavior for escape
						// Single press can mean undo or clear
						// Double press in IE means clear the whole form
						event.preventDefault();
					}
					break;
				default:
					// search timeout should be triggered before the input value is changed
					this._searchTimeout( event );
					break;
				}
			},
			keypress: function( event ) {
				// replicate some key handlers to allow them to repeat in Firefox and Opera
				var keyCode = $.ui.keyCode;
				switch ( event.keyCode ) {
				case keyCode.UP:
					this._keyEvent( "previous", event );
					break;
				case keyCode.DOWN:
					this._keyEvent( "next", event );
					break;
				}
			},
			input: function( event ) {
				this._searchTimeout( event );
			},
			focus: function() {
				this.selectedItem = null;
				this.previous = this._value();
			},
			blur: function( event ) {
				if ( this.cancelBlur ) {
					delete this.cancelBlur;
					return;
				}
				if ( !this.closeBlur ) {
					return;
				}

				clearTimeout( this.searching );
				this.close( event );
				this._change( event );
			}
		});

		this._initSource();
		
		this.menu_btn = this.element.next();
		this.menu = $( "<ul>" )
			.menu({
				input: $(), // custom key handling for now
				role: null  // disable ARIA support
			});
		this.menu = (parseInt($.ui.version.split('.')[1]) <= 9)
			? this.menu.data( "menu" )
			: this.menu.menu( "instance" );
	
		if ( !this.options.appendTo ) {
			var menu_wrap_stuffs = this.stuffs();
			this.menu_wrap = $( "<div>" )
				.addClass( "konan-autocomplete" )
				.css('position', 'absolute')
				.appendTo( this.document.find( "body" )[ 0 ] )
				.zIndex( this.element.zIndex() + 1 )
				.append( menu_wrap_stuffs[0] )
				.append( this.menu.element )
				.append( menu_wrap_stuffs[1] )
				.hide();
			this.closeBlur = true;
		} else {
			var element = $( this.options.appendTo );
			this.menu_wrap = element.parents( ".konan-autocomplete" )
				.hide();
			this.menu.element.appendTo( element );
		}

		this._on( this.menu.element, {
			mousedown: function( event ) {
				// prevent moving focus out of the text field
				this._cancelBlur( event );

				// clicking on the scrollbar causes focus to shift to the body
				// but we can't detect a mouseup or a click immediately afterward
				// so we have to track the next mousedown and close the menu if
				// the user clicks somewhere outside of the autocomplete
				var menuElement = this.menu.element[ 0 ];
				if ( !$( event.target ).closest( ".ui-menu-item" ).length ) {
					this._delay(function() {
						var that = this;
						this.document.one( "mousedown", function( event ) {
							if ( event.target !== that.element[ 0 ] &&
									event.target !== menuElement &&
									!$.contains( menuElement, event.target ) ) {
								that.close();
							}
						});
					});
				}
			},
			menufocus: function( event, ui ) {
				var item = ui.item.data( "ui-autocomplete-item" );
				if ( false !== this._trigger( "focus", event, { item: item } ) ) {
					// use value to match what will end up in the input, if it was a key event
					if ( event.originalEvent && /^key/.test( event.originalEvent.type ) ) {
						this._value( item[0] );
					}
				}
			},
			menuselect: function( event, ui ) {
				var item = ui.item.data( "ui-autocomplete-item" ),
					previous = this.previous;

				// only trigger when focus was lost (click on menu)
				if ( this.element[0] !== this.document[0].activeElement ) {
					this.element.focus();
					this.previous = previous;
					// IE triggers two focus events and the second
					// is asynchronous, so we need to reset the previous
					// term synchronously and asynchronously :-(
					this._delay(function() {
						this.previous = previous;
						this.selectedItem = item;
					});
				}

				if ( false !== this._trigger( "select", event, { item: item } ) ) {
					this._value( item[0] );
				}
				// reset the term after the select event
				// this allows custom select handling to work properly
				this.term = this._value();

				if ( this.closeBlur ) {
					this.close( event );
				}
				this.selectedItem = item;
			}
		});

		// bind mousedown event instead of click event, since the click event is
		// fired after blur event on this.element.
		// mousedown event is fired before blur event.
		this._on( this.menu_wrap, { 
			"mousedown [data-toggle=disable]": function( event ) {
				this._cancelBlur( event );
				this._setOption( "disabled", true);
			},
			"mousedown [data-toggle=close]": function( event ) {
				this._close();
			},
			"mousedown [data-mode]": function( event ) {
				this._cancelBlur( event );
				this._setOption( "mode", $( event.target ).attr( "data-mode" ) );
				this._searchTimeout( event );
			}
		});

		// do not use this._on() since this._on() does not delegate events 
		// when the widget is disabled.
		this.__on( this.menu_btn, "mousedown", function( event ) {
			if ( !this.element.is(":focus") ) {
				// focus the input and move cursor to end of input
				this.element.focus().val( this.element.val() );
			}
			if ( this.menu_wrap.is( ":visible" ) ) {
				// use ._close() instead of .close() so we don't cancel future searches
				this._close();
			} else {
				this._setOption( "disabled", false );
				this._searchTimeout( event );
			}
			return false;
		});

		// turning off autocomplete prevents the browser from remembering the
		// value when navigating through history, so we re-enable autocomplete
		// if the page is unloaded before the widget is destroyed. #7790
		this._on( this.window, {
			beforeunload: function() {
				this.element.removeAttr( "autocomplete" );
			}
		});
	},

	stuffs: function() {
		var regional = $.konan.messages.autocomplete;
		var tools = "";
		$.each( [ ( this.options.footer.left  || [] ), 
				  ( this.options.footer.right || [ "<a data-toggle=\"disable\">" + regional.autocompleteOff + "</a>" ] ) 
				], function( index ) {
		    tools += ( index == 0 ) ? "<span>" : "<span class=\"pull-right\">";
		    tools += this.join( " | ");
			tools += "</span>";
		});
		var	menu_wrap_stuffs = [
			"<div class=\"konan-autocomplete-header\">" + regional.autocompleteOn + "</div>",
			"<div class=\"konan-autocomplete-footer\">" + tools + "</div>"
		];

		return ( this.options.placement === "top" ) 
			? menu_wrap_stuffs : menu_wrap_stuffs.reverse();
	},

	_cancelBlur: function( event ) {
		// prevent moving focus out of the text field
		event.preventDefault();

		// IE doesn't prevent moving focus even with event.preventDefault()
		// so we set a flag to know when we should ignore the blur event
		this.cancelBlur = true;
		this._delay(function() {
			delete this.cancelBlur;
		});
	},

	__on: function( element, eventName, handler ) {
		var instance = this;
		element.on( eventName, function() {
			return handler.apply( instance, arguments );
		} );
	},

	_destroy: function() {
		clearTimeout( this.searching );
		this.element.removeAttr( "autocomplete" );
		this.menu_wrap.element.remove();
	},

	_setOption: function( key, value ) {
		this._super( key, value );
		if ( key === "mode" && !this.options.disabled ) {
			this.forceSearch = true;
		}
		if ( key == "appendTo" ) {
			this.menu.appendTo( $( value ) );
		}
		if ( key === "disabled" ) {
			if ( value ) {
				this.close();
				if ( this.xhr ) this.xhr.abort();
			} else {
				this.forceSearch = true;
			}
		}
	},

	_initSource: function() {
		if ( typeof this.options.source === "string" ) {
			var url, dataType, that = this;
			url = this.options.source;
			dataType = this.options.dataType;
			this.source = function( request, response ) {
				//console.log( JSON.stringify(request) );
				if ( that.xhr ) {
					that.xhr.abort();
				}
				that.xhr = $.ajax({
					url: url,
					data: request,
					dataType: dataType,
					success: function( data ) {
						response( data );
					},
					error: function() {
						response( {} );
					}
				});
			};
		} else {
			this.source = this.options.source;
		}
	},

	_searchTimeout: function( event ) {
		clearTimeout( this.searching );
		this.searching = this._delay(function() {
			// only search if the value has changed
			this.searching = null;
			if ( this.term != this._value() || this.forceSearch ) {
				this.selectedItem = null;
				this.search( null, event );
			}
		}, this.options.delay );
	},

	search: function( value, event ) {
		value = value != null ? value : this._value();

		// always save the actual value, not the one passed as an argument
		this.term = this._value();

		if ( value.length < this.options.min_length && !this.forceSearch ) {
			return this.close( event );
		} else if ( value.length == 0 ) {
			return this.__response( [] );
		}

		if ( this._trigger( "search", event ) === false ) {
			return;
		}

		return this._search( value );
	},

	_search: function( value ) {
		this.cancelSearch = false;
		this.source( { 
			term: value, 
			mode: this.options.mode,
			domain_no: this.options.domain_no,
			max_count: this.options.max_count
		}, this._response() );
	},

	_response: function() {
		var that = this,
			index = ++requestIndex;

		return function( content ) {
			if ( index == requestIndex ) {
				that.__response( content );
			}
		};
	},

	__response: function( content ) {
		if ( content ) {
			content = this._normalize( content );
		}
		this._trigger( "response", null, { seed: this.seed, content: content } );
		if ( (!this.options.disabled && content && content.length && !this.cancelSearch) || this.forceSearch || !this.closeBlur ) {
			this._suggest( content );
			this._trigger( "open" );
		} else {
			// use ._close() instead of .close() so we don't cancel future searches
			if ( this.closeBlur ) {
				this._close();
			}
		}
	},

	close: function( event ) {
		this.cancelSearch = true;
		this._close( event );
	},

	_close: function( event ) {
		if ( this.menu_wrap.is( ":visible" ) ) {
			this.menu_wrap.hide();
			this.menu.blur();
			this.menu_btn.toggleClass( "ui-icon-triangle-1-n ui-icon-triangle-1-s" );
			this._trigger( "close", event );
		}
	},

	_change: function( event ) {
		if ( this.previous != this._value() ) {
			this._trigger( "change", event, { item: this.selectedItem } );
		}
	},

	_normalize: function( items ) {
		if ( items.seed && items.suggestions ) {
			this.seed = items.seed;
			return items.suggestions[0];
		}
		this.seed = this.term;
		return items;
	},

	_suggest: function( items ) {
		this.forceSearch = false;

		var ul = this.menu.element.empty();
		
		this.menu_wrap.zIndex( this.element.zIndex() + 1 );
		this._renderMenu( ul, ( this.options.placement === "top" ) ? items : items.reverse() );
		this.menu.refresh();

		this.menu_btn.removeClass( "ui-icon-triangle-1-s" ).addClass( "ui-icon-triangle-1-n" );
		this.menu_wrap.children( ".konan-autocomplete-header" ).css( "display",  ( items && items.length ) ? "none" : "block" );

		// size and position menu
		this.menu_wrap.show();
		if ( !this.options.appendTo ) {
			this._resizeMenu();
		}
		this.menu_wrap.position( $.extend({
			of: this.element
		}, $.konan.autocomplete.positions[ this.options.placement ] ));
	},

	_resizeMenu: function() {
		this.menu_wrap.outerWidth( this.element.outerWidth() );
	},

	_renderMenu: function( ul, items ) {
		var that = this;
		$.each( items, function( index, item ) {
			that._renderItemData( ul, item );
		});
	},

	_renderItemData: function( ul, item ) {
		return this._renderItem( ul, item ).data( "ui-autocomplete-item", item );
	},

	_renderItem: function( ul, item ) {
		return $( "<li>" )
			.append( (item.length > 1) 
				? $( "<a>" ).attr( "href", item[1] ).html( this.highlight( item[0], this.seed ) )
				: $( "<a>" ).html( this.highlight( item[0], this.seed ) ) )
			.appendTo( ul );
	},

	escapeRegex: function( value ) {
		// escape regular expression's reserved character, remove white spaces and insert ignore white spaces pattern for each characters
		return value.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&").replace(/\s/g, "").replace(/[^\\]{1}/g, "$&\\s*");
	},

	highlight: function( value, seed ) {
		var that = this;
		var re;
		if ( this.options.mode === 's' ) re = new RegExp( "^" + this.escapeRegex( seed ), "i" ); // starts with
		else if ( this.options.mode === 'c' ) re = new RegExp( "\\s+" + this.escapeRegex( seed ), "i" ); // contains
		else if ( this.options.mode === 'e' ) re = new RegExp( this.escapeRegex( seed ) + "$", "i" ); // ends wuth
		else if ( this.options.mode === 't' ) re = new RegExp($.map(seed.match(/\S+/g), function( n, i ) { return that.escapeRegex( n ); }).join("|"), "ig");
		else re = new RegExp( this.escapeRegex( seed ), "ig" );
		return value.replace( re, "<strong>$&</strong>" );
	},

	_move: function( direction, event ) {
		if ( !this.menu_wrap.is( ":visible" ) ) {
			return;
		}
		if ( this.menu.isFirstItem() && /^previous/.test( direction ) ||
				this.menu.isLastItem() && /^next/.test(direction) ) {
			this._value( this.term );
			this.menu.blur();
			return;
		}
		this.menu[ direction ]( event );
	},

	widget: function() {
		return this.menu.element;
	},

	_value: function() {
		return this.valueMethod.apply( this.element, arguments );
	},

	_keyEvent: function( keyEvent, event ) {
		if ( this.menu_wrap.is( ":visible" ) ) {
			this._move( keyEvent, event );

			if ( this.searching ) {
				clearTimeout( this.searching );
				this.searching = null;
			}

			// prevents moving cursor to beginning/end of the text field in some browsers
			event.preventDefault();
		}
	}

});

$.extend($.konan.autocomplete, {
	positions: {
		top: {
			my: "left top", at: "left bottom", collision: "none"
		},
		bottom: {
			my: "left bottom", at: "left top", collision: "none"
		},
		right: {
			my: "right top", at: "right bottom", collision: "none"
		}
	},
	merge: function( first, second, n, m ) {
		var i = n, l = m, j = 0;
		if ( second.length < m ) { l = second.length; i = Math.min(first.length, (n + m) - second.length); } 
	    else if ( first.length < n ) { i = first.length; l = Math.min(second.length, (n + m) - first.length); }
	    while ( j < l ) { first[ i++ ] = second[ j++ ]; }
		first.length = i;
		return first;
	}
});

}(jQuery));

/* jQuery KONAN rankings
 * Depends: jquery.ui.core.js, jquery.ui.widget.js
 */
(function ($, undefined) {
	
$.widget( "konan.rankings", {
	options: {
		domain_no: 0,
		max_count: 10,
		url: null,
		dataType: "json"
	},
	
	_create: function() {
		this._delay(this.refresh,  this.options.delay);
	},

	refresh: function() {
		var self = this;
		$.ajax({
			url: this.options.url,
			data: { domain_no: this.options.domain_no, max_count: this.options.max_count },
			dataType: this.options.dataType,
			success: function(data) {
				self._renderItems(data);
			}
		});
	},
	
	destroy: function() {
		this.element.empty();
		$.Widget.prototype.destroy.call(this);
	},

	_setOption: function( key, value ) {
		this._super( key, value );
	},

	_renderItems: function(items) {
		var ol = $("<ol>").addClass("konan-rankings").appendTo(this.element.empty());
		var self = this;
		$.each(items, function(index, item) {
			self._renderItem(ol, index, item);
		});
	},
	
	_getClassName: function(tag) {
		if (tag === "new") return "new"; else if (tag === "0") return "equal"; else if (tag.charAt(0) === '-') return "arrow-down"; else return "arrow-up";
	},
	
	_renderItem: function(ol, idx, item) {
		var li = $( "<li>" ).append( "<span class='rank'><em>"+(idx+1)+"</em></span>" ).append( $( "<a>" ).text(item[0]) );
		if (item.length > 1) li.append( "<span class='"+this._getClassName(item[1])+"'>"+item[1]+"</span>" );
		return li.appendTo(ol);
	}
	
});
	
}(jQuery));

/* jQuery KONAN recent
 * Depends: jquery.ui.core.js, jquery.ui.widget.js, jquery.cookie.js
 */
(function ($, undefined) {

$.widget( "konan.recent", {
	options: {
		cookie: { // e.g. { expires: 7, path: '/', domain: 'konantech.com', secure: true }
			name: "konan-recent",
			separator: "__",
			expires: 365
		},
		max_count: 10
	},

	_create: function() {
		this.ol = $("<ol>").addClass("konan-recent").appendTo(this.element.empty());
		this.boxMsg = $("<div>").addClass("konan-box-msg").text($.konan.messages.recent.none).appendTo(this.element);

		var that = this, 
			items = this._read();
		if (items) {
			$.each( items, function( index, item ) {
				that._renderItem(that.ol, item, true);
			});
			if (items.length > 0) {
				this.boxMsg.hide();
			}
		}

		this._on({
			"click .ui-icon-close": function( event ) {
				$( event.target ).parents( "li:first" ).remove();
				that._write();
				if (that.ol.children().size() == 0) {
					this.boxMsg.show();
				}
			}
		});
	},

	_read: function() {
		var cookie = $.cookie(this.options.cookie.name);
		return (cookie) ? unescape(cookie).split(this.options.cookie.separator) : null;
	},

	_write: function() {
		$.cookie(this.options.cookie.name, escape(this.ol.find( "a" ).map(function() {
			return $(this).text();
		}).get().join(this.options.cookie.separator)), this.options.cookie);
	},

	add: function( item ) {
		var term = $.trim(item);
		if ( term ) {
			this.boxMsg.hide();
			this.ol.find( "a" ).filter(function() { return $(this).text() === term; }).parent().remove();
			this._renderItem(this.ol, term, false);
			if (this.ol.children().size() > this.options.max_count) {
				this.ol.find( "li:last ").remove();
			}
			this._write();
		}
	},

	_renderItem: function(ol, item, append) {
		var li = $( "<li>" ).append( $( "<a>" ).text(item) ).append( "<span class='ui-icon ui-icon-close'>close</span>" );
		return append ? li.appendTo(ol) : li.prependTo(ol);
	}

});

}(jQuery));