var Application = function () {

	function editKeyDescription (key) {
		var id = 'key_' + key;
		var value = document.getElementById(id).getAttribute('description');
		document.getElementById('dialog-key-name').innerHTML = key;
		document.getElementById('dialog-key-name-value').value = key;
		document.getElementById('dialog-key-description').value = value;
		$('#dialog-key-update').dialog({
			width: '30%'
		});
	}

	return {
		editKeyDescription: editKeyDescription
	};

} ();