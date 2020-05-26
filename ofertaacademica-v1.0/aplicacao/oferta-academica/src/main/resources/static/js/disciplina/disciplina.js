$(".sa-btn-arquivar").on("click", function(event) {
	event.preventDefault();

	var botaoArquivada = $(event.currentTarget); 
	var urlArquivar = botaoArquivada.attr("href");
	
	
	swal({   
        title: "Tem Certeza?",   
        text: "Você poderá desfazer essa operação posteriormente, se desejar!",   
        type: "warning",   
        showCancelButton: true,   
        cancelButtonText: "Cancelar",
        confirmButtonColor: "#DD6B55",   
        confirmButtonText: "Continuar",   
        closeOnConfirm: false
	}, function(isConfirm){
		if(isConfirm){
			var response = $.ajax({
			    url: urlArquivar,
			    type: 'GET'
			});
			
			swal({				 
				title: "Operação realizada com sucesso",   
		        type: "success",   
		        showCancelButton: false,   
		        cancelButtonText: "",
		        confirmButtonColor: "#32CD32",   
		        confirmButtonText: "Ok",   
		        closeOnConfirm: false
			}, function(isConfirm){
				if(isConfirm){
					location.reload();
				}
				
			});
			
			
			
		}
	}
	);

    response.done(function(resultadoArquivar){               		        	
    });
    

});
