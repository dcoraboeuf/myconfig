var UserConfirm = function () {
	
	return {
		validate : function () {
			return myconfig.validateTextAsName ('#password')
				&& myconfig.validateConfirmation ('#password', '#confirm-password');
		}
	};
	
} ();