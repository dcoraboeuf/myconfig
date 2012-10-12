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
