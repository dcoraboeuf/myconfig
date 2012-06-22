var KEY = function () {
	
	return {
		validateCreate: function () {
			var id = '#key-name';
			var value = $(id).val();
			if (value.trim() != "") {
				return true;
			} else {
				$(id).addClass("invalid");
				return false;
			}
		}
	};
	
} ();