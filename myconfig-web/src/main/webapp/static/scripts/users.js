var Users = function () {
	
	return {
		validateCreate: function () {
			return myconfig.validateTextAsName ('#user-name');
		}
	};
	
} ();