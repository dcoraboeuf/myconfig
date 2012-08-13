var Users = function () {
	
	function assignUserFunction (img) {
		var set = img.getAttribute("set");
		var user = img.getAttribute("user");
		var fn = img.getAttribute("function");
		var method = (set == "no") ? 'add' : 'remove';
		var url = 'ui/user/' + user + '/function/' + fn + '/' + method; 
		$.post (url, function (data) {
				if (data.success) {
					img.setAttribute('set', (set == "yes" ? "no" : "yes"));
				} else {
					myconfig.displayError (loc('users.userfunction.error'));
				}
			})
			.error(function (jqXHR, textStatus, errorThrown) {
				myconfig.displayAjaxError (loc('users.userfunction.error'), jqXHR, textStatus, errorThrown);
			});
	}
	
	return {
		validateCreate: function () {
			return myconfig.validateTextAsName ('#user-name')
				&& myconfig.validateTextAsName ('#user-email');
		},
		assignUserFunction: assignUserFunction
	};
	
} ();