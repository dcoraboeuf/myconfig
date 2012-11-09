$(document).ready(function() {
	$.get('ui/types', function (data) {
		$.each (data.descriptions, function (index, description) {
			$('#key-type').append('<option parameterized="{1}" value="{0}">{2}</option>'.format(
					description.id,
					description.parameterized,
					loc(description.id)
					));
		});
	});
});

var KEY = function () {
	
	function validateCreate () {
		return myconfig.validateTextAsName ('#key-name')
			&& myconfig.validateTextAsTrimmed ('#key-description');
	}
	
	return {
		displayKeyForm: function () {
			var name = document.getElementById('key-create-name').value;
			// Updates the fields
			$('#key-name').val(name);
			$('#key-description').val('');
			// Shows the dialog
			$('#key-dialog').dialog({
				title: loc('application.key.new'),
				width: 'auto'
			});
		},
		submitKeyForm: function () {
			if (validateCreate()) {
				var application = $('#key-application').val();
				var name = $('#key-name').val().trim();
				var url = 'ui/key/{0}/{1}/create'.format(application, name);
				// TODO Waiting mask
				$.ajax({
					  type: 'POST',
					  url: url,
					  data: {
						  description: $('#key-description').val(),
						  typeId: 'plain',
						  typeParam: ''
					  },
					  success: function (data) {
						if (data.success) {
							location.reload();
						} else {
							myconfig.displayError (loc('key.error'));
						}
					  },
					  error: function (jqXHR, textStatus, errorThrown) {
						  	if (jqXHR.responseText && jqXHR.responseText != '') {
						  		$('#key-error').html(jqXHR.responseText.htmlWithLines());
						  		$('#key-error').show();
						  	} else {
						  		myconfig.displayAjaxError (loc('key.error'), jqXHR, textStatus, errorThrown);
						  	}
					  }
					});
			}
			// Does not submit the form
			return false;
		}
	};
	
} ();