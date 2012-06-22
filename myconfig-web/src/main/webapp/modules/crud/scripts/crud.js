var CRUD = function () {
	
	return {
		confirmDelete: function (id) {
			var value = document.getElementById ('crud-delete-prompt-' + id).value;
			return confirm (value);
		},
		validateCreate: function (id) {
			var htmlID = "#crud-create-text-" + id;
			var value = $(htmlID).val();
			if (value.trim() != "") {
				return true;
			} else {
				$(htmlID).addClass("invalid");
				return false;
			}
		}
	};
	
} ();