
var _context = $("meta[name='_context']").attr("content");
	
if(_context == null){
    _context = "";
}

$("#btn-editar-oferta").click(function() {
	editarOferta();
})

function getInputsByName(name) {
	return $("input[name='" + name + "']").map(function() {
		return this.value;
	}).get();
}

function getSelectsByName(name){
	return $("select[name='" + name + "']").map(function() {
		return this.options[this.selectedIndex].value;
	}).get();
}

function editarOferta() {
	var idsCompartilhamentos = getInputsByName('compartilhamento.id');
	var idsTurmas = getSelectsByName('compartilhamento.turma');
	var vagas = getInputsByName('compartilhamento.vagas');
	
	idsCompartilhamentos = idsCompartilhamentos.join(",");
	idsTurmas = idsTurmas.join(",");
	vagas = vagas.join(",");
		
	$.get(_context + '/editar-compartilhamentos-oferta/', {idsCompartilhamentos : idsCompartilhamentos, idsTurmas : idsTurmas, vagas : vagas}, function(){
	}).done(resultadoEdicaoOferta);
}

function resultadoEdicaoOferta(retorno) {
	var title, text, type, callbackFunction;
	
	if (retorno){
		title = "Compartilhamentos editados!";
		text = "Os compartilhamentos foram editados.";
		type = "success";
		callbackFunction = function(isConfirm) {
			window.location.href = _context + '/oferta-campus/';
		};
	}else{
		title = "Erro ao editar os compartilhamentos!";
		text = "Os compartilhamentos não foram editados.";
		type = "error";
		callbackFunction = function(isConfirm){};
	}
	
	swalMessage(title, text, type, callbackFunction);
}

function swalMessage(swalTitle, swalText, swalType, confirmCallbackFunction) {
	swal({
		title : swalTitle,
		text : swalText,
		type : swalType,
		showcancelButton : false,
		confirmButtonText : "Ok!",
		closeOnConfirm : true
	}, confirmCallbackFunction);
}