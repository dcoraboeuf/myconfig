var Application = function () {

	function editKeyDescription (key) {
		var id = 'key_' + key;
		var value = document.getElementById(id).getAttribute('description');
		document.getElementById('dialog-key-name').value = key;
		document.getElementById('dialog-key-description').value = value;
		$('#dialog-key-update').dialog({
			title: loc('application.key.update.title', key),
			width: 'auto'
		});
	}

	return {
		editKeyDescription: editKeyDescription
	};

} ();