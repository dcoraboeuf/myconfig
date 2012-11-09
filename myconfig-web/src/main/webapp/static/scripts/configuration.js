// Activation of tooltips
$(function() {
	$( document ).tooltip();
});

var configuration = function () {
	
	var configurationChanges = {};
	
	function submitConfigurationChanges () {
		// URL
		var application = $('#application').val();
		var url = 'ui/configuration/{0}'.format(application);
		// Data		
		var data = {updates: []};
		for (var i in configurationChanges) {
			var id = configurationChanges[i];
			var input = document.getElementById(id);
			var key = input.getAttribute('key');
			var environment = input.getAttribute('environment');
			var version = input.getAttribute('version');
			var value = input.value;
			data.updates.push({
				environment: environment,
				version: version,
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
					myconfig.displayError (loc('configuration.changes.submit.error'));
				}
			  },
			  error: function (jqXHR, textStatus, errorThrown) {
				  	if (jqXHR.responseText && jqXHR.responseText != '') {
				  		$('#configuration-error').html(jqXHR.responseText.htmlWithLines());
				  		$('#configuration-error').show();
				  	} else {
				  		myconfig.displayAjaxError (loc('configuration.changes.submit.error'), jqXHR, textStatus, errorThrown);
				  	}
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
			var version = input.getAttribute('version');
			var oldValue = input.getAttribute('oldvalue');
			var value = input.value;
			html += '<tr id="{0}">'.format(id);
				html += '<td class="change-environment">{0}</td>'.format(environment.html());
				html += '<td class="change-version">{0}</td>'.format(version.html());
				html += '<td class="change-key">{0}</td>'.format(key.html());
				html += '<td class="change-old">{0}</td>'.format(oldValue.html());
				html += '<td class="change-new">{0}</td>'.format(value.html());
			html += '</tr>';
		}
		// Check
		if (count > 0) {
	  		$('#configuration-error').hide();
			// Generates the content of the dialog
			$('#configuration-changes').empty();
			$('#configuration-changes').append(html);
			// Shows the dialog
			$('#dialog-changes').dialog({
				title: loc('configuration.changes.confirm'),
				width: 'auto'
			});
		}
	}
	
	function updateConfigurationValue (input) {
		var id = input.getAttribute('id');
		var value = input.value;
		var oldvalue = input.getAttribute('oldvalue');
		if (value != oldvalue) {
			$(input).addClass('changed');
			configurationChanges[id] = id;
			$('#configuration-changes-submit').removeAttr('disabled');
			$('#configuration-changes-reset').removeClass('form-command-disabled');
		} else {
			$(input).removeClass('changed');
			delete configurationChanges[id];
			$('#configuration-changes-submit').attr('disabled', 'disabled');
			$('#configuration-changes-reset').addClass('form-command-disabled');
		}
	}
	
	function resetConfigurationChanges () {
		if (myconfig.displayConfirmation(loc('configuration.changes.reset.prompt'))) {
			location.reload();
		}
	}
	
	return {
		updateConfigurationValue: updateConfigurationValue,
		controlConfigurationChanges: controlConfigurationChanges,
		submitConfigurationChanges: submitConfigurationChanges,
		resetConfigurationChanges: resetConfigurationChanges
	};
	
} ();