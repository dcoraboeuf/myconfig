var myconfig = function () {
	
	function confirmAndCall (text, callback) {
		$('<div>{0}</div>'.format(text)).dialog({
			title: loc('general.confirm.title'),
			dialogClass: 'confirm-dialog',
			modal: true,
			buttons: {
				Ok: function () {
					$( this ).dialog( "close" );
					callback();
				},
				Cancel: function () {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	
	function confirmIDAndCall (id, callback) {
		var text = document.getElementById(id).value;
		confirmAndCall(text, callback);
	}
	
	function onConfirmedAction (form, confirmID) {
		// Asks for confirmation
		myconfig.confirmIDAndCall (confirmID, function () {
			form.submit();
		});
		// Does not submit the form
		return false;
	}
	
	function displayError (text) {
		$('<div>{0}</div>'.format(text)).dialog({
			title: loc('general.error.title'),
			dialogClass: 'error-dialog',
			modal: true,
            buttons: {
                Ok: function() {
                    $( this ).dialog( "close" );
                }
            }
		});
	}
	
	function displayAjaxError (message, jqXHR, textStatus, errorThrown) {
		var text = '{0}\n[{1}] {2}'.format(message, jqXHR.status, jqXHR.statusText);
		displayError(text);
	}
	

	function validate (selector, test) {
		if (test) {
			$(selector).removeClass("invalid");
			return true;
		} else {
			$(selector).addClass("invalid");
			return false;
		}
	}
	
	return {
		confirmAndCall: confirmAndCall,
		confirmIDAndCall: confirmIDAndCall,
		displayError: displayError,
		displayAjaxError: displayAjaxError,
		onConfirmedAction: onConfirmedAction,
		changeLanguage: function (lang) {
			if (location.search.indexOf("language") > -1) {
		      location.search = location.search.replace(/language=[a-z][a-z]/, "language=" + lang);
		   } else if (location.search == "") {
		      location.href += "?language=" + lang;
		   } else {
		      location.href += "&language=" + lang;
		   }
		},
		keyVersion: function (img) {
			var application = img.getAttribute('application');
			var version = img.getAttribute('version');
			var key = img.getAttribute('key');
			var oldSet = img.getAttribute('set');
			var set = oldSet == 'yes' ? 'no' : 'yes';
			var mode = set == 'yes' ? 'add' : 'remove';
			var url = 'ui/version/' + application + '/' + version + '/' + mode + '/' + key;
			$.post (url, function (data) {
					if (data.success) {
						img.setAttribute('set', set);
					} else {
						displayError (loc('matrix.submit.error'));
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					displayAjaxError (loc('matrix.submit.error'), jqXHR, textStatus, errorThrown);
				});
		},
		validateTextAsName: function (selector) {
			var value = $(selector).val();
			var trimmedValue = value.trim();
			return validate (selector, trimmedValue == value && trimmedValue != "");
		},
		validateTextAsTrimmed: function (selector) {
			var value = $(selector).val();
			var trimmedValue = value.trim();
			return validate (selector, trimmedValue == value);
		},
		validateConfirmation: function (source, confirmation) {
			var value = $(source).val();
			var confirmValue = $(confirmation).val();
			return validate (confirmation, confirmValue == value);
		}
	};
	
} ();