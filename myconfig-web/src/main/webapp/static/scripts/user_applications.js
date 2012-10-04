var UserApplications = function () {
	
	return {
		assignAppFunction: function (img) {
			var set = img.getAttribute("set");
			var application = img.getAttribute("application");
			var user = img.getAttribute("user");
			var fn = img.getAttribute("function");
			var method = (set == "no") ? 'add' : 'remove';
			var url = 'ui/user/' + user + '/application/' + application + '/ ' + fn + '/' + method; 
			$.post (url, function (data) {
					if (data.success) {
						img.setAttribute('set', (set == "yes" ? "no" : "yes"));
					} else {
						myconfig.displayError (loc('user_applications.appfunction.error'));
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					myconfig.displayAjaxError (loc('user_applications.appfunction.error'), jqXHR, textStatus, errorThrown);
				});
		}
	};
	
} ();