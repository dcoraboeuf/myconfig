var UserForgotten = function () {
	
	return {
		validate : function () {
			return myconfig.validateTextAsName ('#email');
		}
	};
	
} ();