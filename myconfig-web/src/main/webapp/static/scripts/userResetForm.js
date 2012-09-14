var UserResetForm = function () {
	
	return {
		validate : function () {
			return myconfig.validateTextAsName ('#password')
				&& myconfig.validateConfirmation ('#password', '#confirmPassword');
		}
	};
	
} ();