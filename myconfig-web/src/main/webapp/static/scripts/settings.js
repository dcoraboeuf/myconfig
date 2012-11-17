var Settings = function () {
	
	return {
		validateAppSettings: function () {
			return myconfig.validateTextAsName ('#app-name')
				&& myconfig.validateTextAsName ('#app-replyto-address')
				&& myconfig.validateTextAsName ('#app-replyto-name');
		},
		validateUserData: function () {
			return myconfig.validateTextAsName ('#userDisplayName')
				&& myconfig.validateTextAsName ('#userEmail')
				&& myconfig.validateTextAsName ('#userPassword');
		},
		confirmClearAll: function() {
			return myconfig.displayConfirmation(loc('settings.audit.clearAll.prompt'));
		}
	};
	
} ();