String.prototype.format = function() {
	var args = arguments;
	return this.replace(/\{\{|\}\}|\{(\d+)\}/g, function(m, n) {
		if (m == "{{") {
			return "{";
		}
		if (m == "}}") {
			return "}";
		}
		return args[n];
	});
};

var myconfig = function () {
	
	function displayAjaxError (message, jqXHR, textStatus, errorThrown) {
		var text = '{0}\n[{1}] {2}'.format(message, jqXHR.status, jqXHR.statusText);
		alert(text);
	}
	
	function manageKeyVersion (application, version, key, set) {
		var mode = set ? 'add' : 'remove';
		var url = 'ui/version/' + application + '/' + version + '/' + mode + '/' + key;
		$.post (url)
			.success(function () {
				alert('Success!');
			})
			.error(function (jqXHR, textStatus, errorThrown) {
				displayAjaxError ('TODO Error', jqXHR, textStatus, errorThrown);
			});
	}
	
	return {
		changeLanguage: function (lang) {
			if (location.search.indexOf("language") > -1) {
		      location.search = location.search.replace(/language=[a-z][a-z]/, "language=" + lang);
		   } else if (location.search == "") {
		      location.href += "?language=" + lang;
		   } else {
		      location.href += "&language=" + lang;
		   }
		},
		addKeyVersion: function (application, version, key) {
			manageKeyVersion (application, version, key, true);
		},
		removeKeyVersion: function (application, version, key) {
			manageKeyVersion (application, version, key, false);
		}
	};
	
} ();