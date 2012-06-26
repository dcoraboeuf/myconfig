var KEY = function () {
	
	return {
		validateCreate: function () {
			return myconfig.validateTextAsName ('#key-name')
				&& myconfig.validateTextAsTrimmed ('#key-description');
		}
	};
	
} ();