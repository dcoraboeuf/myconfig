var CRUD = function () {
	
	return {
		confirmDelete: function (id) {
			var value = document.getElementById ('crud-delete-prompt-' + id).value;
			return myconfig.displayConfirmation (value);
		},
		validateCreate: function (id) {
			return myconfig.validateTextAsName ("#crud-create-text-" + id);
		}
	};
	
} ();