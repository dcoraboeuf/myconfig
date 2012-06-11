var CRUD = function () {
	
	return {
		confirmDelete: function (id) {
			var value = document.getElementById ('crud-delete-prompt-' + id).value;
			return confirm (value);
		}
	};
	
} ();