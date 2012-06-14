var myconfig = function () {
	
	return {
		changeLanguage: function (lang) {
			if (location.search.indexOf("language") > -1) {
		      location.search = location.search.replace(/language=[a-z][a-z]/, "language=" + lang);
		   } else if (location.search == "") {
		      location.href += "?language=" + lang;
		   } else {
		      location.href += "&language=" + lang;
		   }
		}
	};
	
} ();