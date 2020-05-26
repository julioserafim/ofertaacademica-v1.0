$(".sa-btn-excluir").click(function(event){
	event.preventDefault();
	
	var urlExcluir = $(this).attr("href");
	
	swal({
		title: "Tem certeza?",
		text: "Você não poderá desfazer essa operação posteriormente!",
		type: "warning",
		showCancelButton: true,
		cancelButtonText: "Cancelar",
		confirmButtonColor: "#DD6B55",   
        confirmButtonText: "Sim, desejo excluir",
        closeOnConfirm: true
		
	}, function(isConfirm){
		if(isConfirm){
			window.location.href = urlExcluir;
		}
	});
	
});