$(document).ready(function() 
    { 
        $("#applications").addClass('tablesorter');
        $("#applications").tablesorter({
            headers: {
                6: {
                   sorter: false
                }
            }
        }); 
    } 
);

var Applications = function () {
	
	return {
		validateCreate: function () {
			return myconfig.validateTextAsName ('#application-create-id')
				&& myconfig.validateTextAsName ('#application-create-name');
		}
	};
	
} ();