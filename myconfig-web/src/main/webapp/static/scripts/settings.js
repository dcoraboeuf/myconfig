var Settings = function () {
	
	return {
		validateAppSettings: function () {
			return myconfig.validateTextAsName ('#app-name')
				&& myconfig.validateTextAsName ('#app-replyto-address')
				&& myconfig.validateTextAsName ('#app-replyto-name');
		}
	};
	
} ();