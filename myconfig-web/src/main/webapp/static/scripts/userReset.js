var UserReset = function () {
	
	return {
		validate : function () {
			return myconfig.validateTextAsName ('#oldPassword')
				&& myconfig.validateTextAsName ('#newPassword')
				&& myconfig.validateConfirmation ('#newPassword', '#confirmPassword');
		}
	};
	
} ();