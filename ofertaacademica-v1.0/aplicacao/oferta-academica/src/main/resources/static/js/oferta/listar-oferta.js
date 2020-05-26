$(document).ready(function() {
	var _context = $("meta[name='_context']").attr("content");
	
	if(_context == null){
	    _context = "";
	}
	
	$("#tabOfertas li:first-child a").addClass("active");
	$("#tabOfertas li:first-child a").addClass("show");
	$("#tabOfertas div.tab-pane:first-child").addClass("active");

	$("#visulizar-outras-ofertas").change(function() {
		var id = $(this).val();

		window.location.href = _context + "/ofertas/" + id;

	});

	$(".table-ofertas").DataTable({
		"language" : {
			url : _context + '/js/Portuguese-Brasil.json'
		},
		"info": false,
		"order" : [ 0, 'asc' ],
		"columnDefs" : [ {
			"orderable" : false,
			"targets" : 4
		} ],
		paging : false,
		searching : false
	});
});