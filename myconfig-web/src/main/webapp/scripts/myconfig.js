String.prototype.format = function() {
	var args = arguments;
	return this.replace(/\{\{|\}\}|\{(\d+)\}/g, function(m, n) {
		if (m == "{{") {
			return "{";
		}
		if (m == "}}") {
			return "}";
		}
		return args[n];
	});
};

var configurationChanges = {};

var myconfig = function () {
	
	function displayConfirmation (text) {
		return confirm (text);
	}
	
	function displayError (text) {
		alert(text);
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
	
	function submitConfigurationChanges () {
		// URL
		var application = $('#application').val();
		var version = $('#version').val();
		var url = 'ui/configuration/{0}/{1}'.format(application, version);
		// Data		
		var data = {updates: []};
		for (var i in configurationChanges) {
			var id = configurationChanges[i];
			var input = document.getElementById(id);
			var key = input.getAttribute('key');
			var environment = input.getAttribute('environment');
			var value = input.value;
			data.updates.push({
				environment: environment,
				key: key,
				value: value
			});
		}
		// TODO Waiting mask
		$.ajax({
			  type: 'POST',
			  url: url,
			  contentType: 'application/json',
			  data: JSON.stringify(data),
			  dataType: 'json',
			  success: function (data) {
				if (data.success) {
					location.reload();
				} else {
					displayError ('I18N Could not update the configuration');
					$('#dialog-changes').dialog('close');
				}
			  },
			  error: function (jqXHR, textStatus, errorThrown) {
					displayAjaxError ('I18N Could not update the configuration', jqXHR, textStatus, errorThrown);
					$('#dialog-changes').dialog('close');
			  }
			});
	}
	
	function controlConfigurationChanges () {
		// HTML for the changes
		var html = '';
		var count = 0;
		for (var i in configurationChanges) {
			count++;
			var id = configurationChanges[i];
			var input = document.getElementById(id);
			var key = input.getAttribute('key');
			var environment = input.getAttribute('environment');
			var oldValue = input.getAttribute('oldvalue');
			var value = input.value;
			html += '<tr id="{0}">'.format(id);
				html += '<td class="change-environment">{0}</td>'.format(environment);
				html += '<td class="change-key">{0}</td>'.format(key);
				html += '<td class="change-old">{0}</td>'.format(oldValue);
				html += '<td class="change-new">{0}</td>'.format(value);
			html += '</tr>';
		}
		// Check
		if (count > 0) {
			// Generates the content of the dialog
			$('#configuration-changes').empty();
			$('#configuration-changes').append(html);
			// Shows the dialog
			$('#dialog-changes').dialog({
				title: 'I18N Confirmation of changes',
				width: '50%'
			});
		}
	}
	
	return {
		displayConfirmation: displayConfirmation,
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
						displayError ('I18N Could not update the key x version');
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					displayAjaxError ('I18N Error', jqXHR, textStatus, errorThrown);
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
		updateConfigurationValue: function (input) {
			var id = input.getAttribute('id');
			var value = input.value;
			var oldvalue = input.getAttribute('oldvalue');
			if (value != oldvalue) {
				$(input).addClass('changed');
				configurationChanges[id] = id;
				$('#configuration-changes-submit').removeClass('configuration-command-disabled');
				$('#configuration-changes-reset').removeClass('configuration-command-disabled');
			} else {
				$(input).removeClass('changed');
				delete configurationChanges[id];
				$('#configuration-changes-submit').addClass('configuration-command-disabled');
				$('#configuration-changes-reset').addClass('configuration-command-disabled');
			}
		},
		controlConfigurationChanges: controlConfigurationChanges,
		submitConfigurationChanges: submitConfigurationChanges,
		resetConfigurationChanges: function () {
			if (displayConfirmation('I18N Do you want to reset all changes?')) {
				location.reload();
			}
		}
	};
	
} ();