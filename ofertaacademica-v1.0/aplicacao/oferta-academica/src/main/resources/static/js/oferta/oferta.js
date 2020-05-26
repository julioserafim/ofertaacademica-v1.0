
var _context = $("meta[name='_context']").attr("content");
	
if(_context == null){
    _context = "";
}


var siglaCursoCoordenador = $('input[name=cursoAtual]').val();
var idCursoCoordenador = $('input[name=idCursoAtual]').val();
var idCursoSelecionado = idCursoCoordenador;
var direcao = false;

$('#btn-modal-importar-ofertas').on('click', function (event) {
	$('#resultado-ofertas-1').empty();
	$('#resultado-ofertas-2').empty();
	$('#sem-resultado-ofertas').empty();
	$('#modal-importar-ofertas').modal('show');
	
});

$('#visulizar-outras-ofertas').on('change', function (event) {
	$("#ofertas").empty();
	idCursoSelecionado = $('#visulizar-outras-ofertas').val();
	$.get(_context + '/ofertas/curso/' + idCursoSelecionado, function() {
	})
	.done(function(results) {
		organizarOfertas(results);
	});
});

$('#btn-importar-ofertas').on('click', function (event) {
	importarOfertas();
});

var periodos = document.getElementById("periodo");
var periodo = periodos.options[periodos.selectedIndex].text;

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function importarOfertas(){
	var disciplinas = $("input[name=ofertas]:checked").map(function() {
		return this.value;
	}).get().join(",");
	
	if(disciplinas.length > 0){
		alert(disciplinas)
		$.get(_context + "/ofertas/importar", {disciplinas : disciplinas}, function() {
		})
		.done(function(retorno) {
			if(retorno === true){
				$('#modal-importar-ofertas').modal('toggle');
				importacaoRealizada();
			}
		});
	}
}

$('#btn-exibir-ofertas').click(function() {
	var periodo = document.getElementById("periodo").selectedIndex;
	var periodos = document.getElementById("periodo").options;
	var id = periodos[periodo].value;
	
	if(id > 0){
		$.get(_context + "/ofertas/buscar-ofertas/" + id, function() {
			
		})
		.done(function(ofertas) {
			$('#resultado-ofertas-1').empty();
			$('#resultado-ofertas-2').empty();
			$('#sem-resultado-ofertas').empty();
			var index = 2;
			if(ofertas.length > 0){
				$.each(ofertas, function(key, value) {
					adicionarResultado(value.disciplina.id, "ofertas", '#resultado-ofertas-1', '#resultado-ofertas-2', value.disciplina.nome, "ofertas", index);	
					index++;
				});
			}else{
				adicionarMensagemSemResultado('#sem-resultado-ofertas');
			}
		});
	}
	
});

function adicionarMensagemSemResultado(coluna){
	var label = document.createElement('p');
    label.setAttribute('class','text-center');
    label.appendChild(document.createTextNode("N??o foi encontrado resultado para a sua busca."));
    $(coluna).append(label);
}

function adicionarResultado(id, name, coluna1, coluna2, nome, classe, index){
	
	var checkbox = document.createElement('input');
	checkbox.className = classe;
	checkbox.type = "checkbox";
	checkbox.name = name;
	checkbox.value = id;
	checkbox.id = id;
	checkbox.setAttribute("nome", nome);
	
	var label = document.createElement('label')
	label.htmlFor = id;
	label.appendChild(document.createTextNode(nome));
	
	var lista1 = document.createElement('ul');
	lista1.id = 'resultado-disciplinas-1';
	lista1.setAttribute('class','list-unstyled');
	
	var lista2 = document.createElement('ul');
	lista2.id = 'resultado-disciplinas-2';
	lista2.setAttribute('class','list-unstyled');
	
	
	$(coluna1).append(lista1);
	$(coluna2).append(lista2);
	
	
	var ul1 = document.getElementById('resultado-disciplinas-1');
	var ul2 = document.getElementById('resultado-disciplinas-2');
	
	var li = document.createElement('li');
    li.appendChild(checkbox);
    li.appendChild(label);
    li.setAttribute('class', 'checkbox checkbox-success');
    
	
	if(index % 2 == 0){
		ul1.appendChild(li); 
	}else{
		ul2.appendChild(li);
	}
	
}

$(".sa-btn-excluir").on("click", function(event){
	event.preventDefault();

	var botaoExcluir = $(event.currentTarget);
	var urlExcluir = botaoExcluir.attr("href");
	console.log(urlExcluir);
	swal({
		title: "Tem certeza?",
		text: "Voc?? n??o poder?? desfazer essa opera????o posteriormente!",
		type: "warning",   
		showCancelButton: true,
		cancelButtonText: "Cancelar",
		confirmButtonColor: "#DD6B55",
		confirmButtonText: "Sim, desejo excluir!",
		closeOnConfirm: false
	}, function(isConfirm){
		if(isConfirm){
			var response = $.ajax({
				url: urlExcluir,
				type: 'GET',
				success: function(result){
					if (result === true){
						successSwal();
					}
					else{
						errorSwal();
					}
					
				},
				error: function(status, error){
					errorSwal();
				}
			});
		}
	});
});

function importacaoRealizada(){
	swal({
		title: "Oferta(as) importadas!",
		text: "As oferta(as) selecionadas foram importadas.", 
		type: "success",
		showcancelButton: false,
		confirmButtonText: "Ok!",
		closeOnConfirm: true
	}, function(isConfirm){
		location.reload();
	});
}

function successSwal(){
	swal({
		title: "Oferta exclu??da!",
		text: "A oferta foi exclu??da.", 
		type: "success",
		showcancelButton: false,
		confirmButtonText: "Ok!",
		closeOnConfirm: true
	}, function(isConfirm){
		location.reload();
	});
}

function errorSwal(){
	swal({
		title: "Erro ao excluir",
		text: "A oferta n??o foi exclu??da.", 
		type: "error",
		showcancelButton: false,
		confirmButtonText: "Ok",
		closeOnConfirm: true
	});	
}

//Fun????o que faz a requisi????o da lista de ofertas e de compartilhamentos quando a p??gina ?? carregada
//result = model(ofertas e compartilhamentos)
/*$(window).load(function() {
<<<<<<< HEAD*/
	/*$.get(baseUrl + "/ofertas/listar", function() {
=======
	$.get(_context + "/ofertas/listar", function() {
>>>>>>> refs/remotes/origin/master
	})
	.done(function(result) {
		organizarOfertas(result);
	});*/
/*});*/

//Fun????o que organiza a lista de ofertas por semestre
function organizarOfertas(result) {
	semestres = ['PRIMEIRO', 'SEGUNDO', 'TERCEIRO', 'QUARTO', 'QUINTO', 'SEXTO', 'SETIMO', 'OITAVO', 'NONO', 'DECIMO'];
	direcao = result.papelDirecao;
	for(var i = 0; i <= 9; i++) {
		var semestre = semestres[i];
		var numberSemestre = i+1;
		criarEstrutura(semestre, numberSemestre, result.curso.sigla);
		var existe = false;
		var newRow = 0;
		var idNewRow = '';
		
		$.each(result.ofertas, function(key, value) {
			var professores = listarProfessoresOferta(value.professores);

			if(value.turma.semestre == semestre) {
				if(newRow%4 === 0) {
					idNewRow = 'rowPanel'+newRow+semestre;
				}
				
				criarPanelsOferta(value.turma.curso.id, value.turma.curso.sigla, value.disciplina.codigo, value.disciplina.nome, value.vagas, value.turno, professores, semestre, numberSemestre, value.id, newRow, idNewRow, -1);
				existe = true;
				newRow++;
				
			}
		});

		$.each(result.compartilhamentos, function(key, value) {
			var professores = listarProfessoresOferta(value.oferta.professores);

			if(value.turma.semestre == semestre) {
				if(newRow%4 === 0) {
					idNewRow = 'rowPanel'+newRow+semestre;
				}
				numeroDoSemestreOferta = semestres.indexOf(value.oferta.turma.semestre) + 1;
				
				criarPanelsOferta(value.oferta.turma.curso.id, value.oferta.turma.curso.sigla, value.oferta.id, value.oferta.disciplina.nome, value.vagas, value.oferta.turno, professores, semestre, numeroDoSemestreOferta, value.oferta.id, newRow, idNewRow, value.id);
				existe = true;
				newRow++;
				
			}
		});
		
		criarInforme(semestre, existe);
		
		$(".sa-btn-excluir-oferta").on("click", function(event){
			event.preventDefault();

			var botaoExcluir = $(event.currentTarget);
			var urlExcluir = botaoExcluir.attr("href");
			
			var ofertaId = urlExcluir.split('/')[3];
			var urlVerificarRelacionamento = _context + '/ofertas/' + ofertaId + '/relacionamentos';
			var textoConfirmacao = '';
			
			$.ajax({
				url: urlVerificarRelacionamento,
				type: 'GET',
				success: function(result){
					if (result === true){
						textoConfirmacao = 'Esta oferta possui compartilhamentos e/ou restrições de horarário associados a ela. Deseja continuar a exclusão?';
					}
					else{
						textoConfirmacao = 'Você não poderá desfazer essa operação posteriormente!';
					}
					swal({
						title: "Tem certeza?",
						text: textoConfirmacao,
						type: "warning",   
						showCancelButton: true,
						cancelButtonText: "Cancelar",
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "Sim, desejo excluir!",
						closeOnConfirm: false
						},
						function(isConfirm) {
							if(isConfirm) {
								var response = $.ajax({
									url: urlExcluir,
									type: 'GET',
									success: function(result){
										if (result === true){
											successSwal();
										}
										else{
											errorSwal();
										}
										
									},
									error: function(status, error){
										errorSwal();
									}
								});
							}
					});
				}
			});
			
		});
		//deve existir uma maneira melhor para n??o fazer este ctrl+c ctrl+v mas ainda encontrei
		$(".sa-btn-excluir-compartilhamento").on("click", function(event){
			event.preventDefault();

			var botaoExcluir = $(event.currentTarget);
			var urlExcluir = botaoExcluir.attr("href");
			
			swal({
				title: "Tem certeza?",
				text: "Você não poderá desfazer essa operação posteriormente!",
				type: "warning",   
				showCancelButton: true,
				cancelButtonText: "Cancelar",
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "Sim, desejo excluir!",
				closeOnConfirm: false
			}, function(isConfirm){
				if(isConfirm){
					var response = $.ajax({
						url: urlExcluir,
						type: 'GET',
						success: function(result){
							if (result === true){
								successSwalC();
							}
							else{
								errorSwalC();
							}
							
						},
						error: function(status, error){
							errorSwal();
						}
					});
				}
			});
		});
	}
}


//Fun????o que cria a estrtura por semestre
function criarEstrutura(semestre, numberSemestre, sigla) {
	//var divContainer = document.createElement('div');
	//divContainer.setAttribute('class', 'container');
	
	var divRow = document.createElement('div');
	divRow.setAttribute('class', 'row');
	
	divRow.id = semestre;

	var div = document.createElement('div');
	div.setAttribute('class', 'col-md-12');
	var h5 = document.createElement('h5');
	h5.setAttribute('style', 'text-align: center');
	var b = document.createElement('b');
	b.appendChild(document.createTextNode(sigla+' - '+numberSemestre));
	var hr = document.createElement('hr');

	h5.appendChild(b);
	div.appendChild(h5);
	div.appendChild(hr);
	
	divRow.appendChild(div);
	
	$('#ofertas').append(divRow);
	
	if(sigla !== 'EC') {
		$('#NONO').hide();
		$('#DECIMO').hide();
	}
}

//Fun????o que divide as ofertas em linhas com quatro ofertas
function criarRowsPanel(panel, semestre, newRow, idNewRow) {
	if(newRow%4 === 0) {
		var divRow = document.createElement('div');
		divRow.id = idNewRow;	
		divRow.setAttribute('class', 'row');
		
		divRow.appendChild(panel);
		$('#'+semestre).append(divRow);
	} else {
		$('#'+idNewRow).append(panel);
		$('#'+semestre).append($('#'+idNewRow));
	}
}


//Fun????o que cria o panel para cada oferta
function criarPanelsOferta(idCurso, sigla, codigoDisciplina, nomeDisciplina, vagas, turno, professores, semestre, numberSemestre, idOferta, newRow, idNewRow, idCompartilhamento){
	//Elementos html criados via Javascript
	var divCol = document.createElement('div');
	divCol.setAttribute('class', 'row');
	
	var divPanel = document.createElement('div');
	
	var divRibbon = document.createElement('div');
	divRibbon.setAttribute('class', 'col-md-12');
	var iconeRibbon = document.createElement('i');
	iconeRibbon .setAttribute('class', 'fa fa-share-alt');
	divRibbon.appendChild(iconeRibbon);

	var divPanelHeading = document.createElement('div');
	divPanelHeading.setAttribute('class', 'table-responsive');
	
	var label = document.createElement('label');
	var bold = document.createElement('b');
	
	bold.appendChild(document.createTextNode(sigla+numberSemestre + ' - ' + codigoDisciplina + ' - ' + nomeDisciplina));
	label.appendChild(bold);
	
	var divPanelAction = document.createElement('div');
	divPanelAction.setAttribute('class', 'panel-action');
	
	var divPanelWrapper = document.createElement('div');
	divPanelWrapper.setAttribute('class', 'panel-wrapper collapse in');
	
	var divPanelBody = document.createElement('div');
	divPanelBody.setAttribute('class', 'panel-body');
	
	var pVagas = document.createElement('p');
	var pTurno = document.createElement('p');
	var pProfessores = document.createElement('p');
	
	if(idCompartilhamento != -1){
		divPanel.setAttribute('class', 'panel panel-inverse');
		pVagas.appendChild(document.createTextNode("Vagas Solicitadas: " + vagas));

	} else {
		divPanel.setAttribute('class', 'panel panel-default');
		pVagas.appendChild(document.createTextNode("Vagas: " + vagas));
	}
	
	
	var divPanelFooter = document.createElement('div');
	divPanelFooter.setAttribute('class', 'panel-footer');
	
	var divRowButton = document.createElement('div');
	divRowButton.setAttribute('class', 'row');
	
	var divColButton = document.createElement('div');
	divColButton.setAttribute('class', 'col-sm-12');
	
	var divButton = document.createElement('div');
	divButton.setAttribute('class', 'pull-right');
	
	if(!direcao) {
		if(idCursoSelecionado == idCursoCoordenador) {
			if(siglaCursoCoordenador == sigla) {
				var iconeEditar = document.createElement('i');
				iconeEditar.setAttribute('class', 'fa fa-pencil');
				var buttonEditar = document.createElement('a');
				buttonEditar.href = _context + '/ofertas/'+ idOferta + '/editar';
				buttonEditar.setAttribute('class', 'btn btn-info btn-acoes');
				buttonEditar.appendChild(iconeEditar);
				divButton.appendChild(buttonEditar);
				
				var iconeExcluir = document.createElement('i');
				iconeExcluir.setAttribute('class', 'fa fa-close');
				var buttonExcluir = document.createElement('a');
				buttonExcluir.href = _context + '/ofertas/'+ idOferta + '/excluir';
				buttonExcluir.setAttribute('class', 'btn btn-danger btn-acoes sa-btn-excluir-oferta');
				buttonExcluir.appendChild(iconeExcluir);
				divButton.appendChild(buttonExcluir);
				
			} else {
	//			divPanel.appendChild(divRibbon);
				
				
				
				var iconeEditar = document.createElement('i');
				iconeEditar.setAttribute('class', 'fa fa-pencil');
				var buttonEditar = document.createElement('a');
				buttonEditar.href = _context + '/compartilhamentos/' + idCompartilhamento + '/editar';
				buttonEditar.setAttribute('class', 'btn btn-info btn-acoes');
				buttonEditar.appendChild(iconeEditar);
				divButton.appendChild(buttonEditar);
	
				var iconeExcluir = document.createElement('i');
				iconeExcluir.setAttribute('class', 'fa fa-close');
				var buttonExcluir = document.createElement('a');
				buttonExcluir.href = _context + '/compartilhamentos/' + idCompartilhamento + '/excluir';
				buttonExcluir.setAttribute('class', 'btn btn-danger btn-acoes sa-btn-excluir-compartilhamento');
				buttonExcluir.appendChild(iconeExcluir);
				divButton.appendChild(buttonExcluir);
			
			}
		} 
		else if(siglaCursoCoordenador != sigla) {
			var iconeShare = document.createElement('i');
			iconeShare.setAttribute('class', 'fa fa-share-alt');
			var buttonSolicitarCompartilhamento = document.createElement('a');
			buttonSolicitarCompartilhamento.href = _context + '/ofertas/'+ idOferta + '/solicitar-compartilhamento';
			buttonSolicitarCompartilhamento.setAttribute('class', 'btn btn-inverse btn-acoes');
			buttonSolicitarCompartilhamento.appendChild(iconeShare);
			divButton.appendChild(buttonSolicitarCompartilhamento);
		}
	} else {
		//EDITAR
		var iconeEditar = document.createElement('i');
		iconeEditar.setAttribute('class', 'fa fa-pencil');
		var buttonEditar = document.createElement('a');
		
		var id = idOferta;
		var textUrl = '/ofertas/'; 
		if(idCompartilhamento != -1){
			id = idCompartilhamento;
			textUrl = '/compartilhamentos/';
		}else{
			buttonEditar.setAttribute('data-toggle', 'tooltip');
			buttonEditar.setAttribute('title', 'Editar Oferta');
		}
		
		buttonEditar.href = _context + textUrl + id + '/editar';
		buttonEditar.setAttribute('class', 'btn btn-info btn-acoes');
		
		buttonEditar.appendChild(iconeEditar);
		divButton.appendChild(buttonEditar);
		
		//EXCLUIR
		var iconeExcluir = document.createElement('i');
		iconeExcluir.setAttribute('class', 'fa fa-close');
		var buttonExcluir = document.createElement('a');
		var id = idOferta;
		var textUrl = '/ofertas/'; 
		if(idCompartilhamento != -1){
			id = idCompartilhamento;
			textUrl = '/compartilhamentos/';
			buttonExcluir.setAttribute('class', 'btn btn-danger btn-acoes sa-btn-excluir-compartilhamento');
		}else{
			buttonExcluir.setAttribute('data-toggle', 'tooltip');
			buttonExcluir.setAttribute('title', 'Excluir Oferta');
			buttonExcluir.setAttribute('class', 'btn btn-danger btn-acoes sa-btn-excluir-oferta');
		}
		buttonExcluir.href = _context + textUrl + id + '/excluir';
		buttonExcluir.appendChild(iconeExcluir);
		divButton.appendChild(buttonExcluir);
		//COMPARTILHAR
		var iconeShare = document.createElement('i');
		iconeShare.setAttribute('class', 'fa fa-share-alt');
		var buttonSolicitarCompartilhamento = document.createElement('a');
		buttonSolicitarCompartilhamento.href = _context + '/ofertas/'+ idOferta + '/solicitar-compartilhamento';
		buttonSolicitarCompartilhamento.setAttribute('class', 'btn btn-inverse btn-acoes');
		buttonSolicitarCompartilhamento.appendChild(iconeShare);
		divButton.appendChild(buttonSolicitarCompartilhamento);
	}
	

	//Inserindo elementos filhos nos elementos pai
	//pVagas.appendChild(document.createTextNode("Vagas: " + vagas));
	pTurno.appendChild(document.createTextNode("Turno: " + turno));
	pProfessores.appendChild(document.createTextNode("Professores: " + professores));

	divPanelBody.appendChild(pVagas);
	divPanelBody.appendChild(pTurno);
	divPanelBody.appendChild(pProfessores);
	
	divColButton.appendChild(divButton);
	
	divRowButton.appendChild(divColButton)
	
	divPanelFooter.appendChild(divRowButton);
	
	divPanelWrapper.appendChild(divPanelBody);
	divPanelWrapper.appendChild(divPanelFooter);
	
	divPanelHeading.appendChild(label);
	divPanelHeading.appendChild(divPanelAction);
	
	divPanel.appendChild(divPanelHeading);
	divPanel.appendChild(divPanelWrapper);
	
	
	divCol.appendChild(divPanel);
	criarRowsPanel(divCol, semestre, newRow, idNewRow);	
}

//Fun????o que cria o panel de informe quando n??o h?? nenhuma oferta para determinado semestre
function criarInforme(semestre, existe) {
	if(existe === false) { 
		var divCol = document.createElement('div');
		divCol.setAttribute('class', 'col-md-12 panel-informe');
		
		var divPanel = document.createElement('div');
		divPanel.setAttribute('class', 'panel panel-default');
		
		var divPanelHeading = document.createElement('div');
		divPanelHeading.setAttribute('class', 'panel-heading');
		
		var label = document.createElement('p');
		var bold = document.createElement('b');
		label.setAttribute('class', 'text-center');
		
		bold.appendChild(document.createTextNode("As ofertas serão exibidas aqui"));
		label.appendChild(bold);
		
		divPanelHeading.appendChild(label);
		divPanel.appendChild(divPanelHeading);
		divCol.appendChild(divPanel);
		
		$('#'+semestre).append(divCol);
	}
}

//Fun????o que transforma em string a lista de professores de uma oferta
function listarProfessoresOferta(professores) {
	var professorList = '';
	
	if(professores.length > 0) {
		$.each(professores, function(key, value) {
			professorList += ' : ' + value.pessoa.nome;	
		});
	} else {
		professorList = 'Não há professores para essa oferta.';
	}

	return professorList;
}

//$('#btn-modal-importar-ofertas').on('click', function (event) {
//	limparResultadosImportacao();
//	$('#modal-importar-ofertas').modal('show');
//	
//});

$('#btn-importar-ofertas').on('click', function (event) {
	importarOfertas();
});


$('#btn-substituir-ofertas').on('click', function (event) {
	substituirOfertas();
});

var periodos = document.getElementById("periodo");
var periodo = periodos.options[periodos.selectedIndex].text;

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function limparResultadosImportacao(){
	$('#resultado-ofertas-1').empty();
	$('#resultado-ofertas-2').empty();
	$('#sem-resultado-ofertas').empty();
	$('#resultado-substituicao-ofertas-1').empty();
	$('#resultado-substituicao-ofertas-2').empty();
}

function substituirOfertas(){
	var ofertas = $("input[name=ofertas]:checked").map(function() {
		return this.value;
	}).get().join(",");
	if(ofertas.length > 0){
		$.get(_context + "/ofertas/substituicao-ofertas", {ofertas : ofertas}, function() {
		})
		.done(function(resultado) {
			if(resultado === true){
				$('#modal-substituir-ofertas').modal('toggle');
				importacaoRealizada(true, false);
			}else{
				errorSubstituirOferta();
			}
		});
	}
}

function importarOfertas(){
	var ofertas = $("input[name=ofertas]:checked").map(function() {
		return this.value;
	}).get().join(",");
	
	if(ofertas.length > 0){
		$.get(_context + "/ofertas/importar", {ofertas : ofertas}, function() {
		})
		.done(function(ofertas) {
				importacaoRealizada(ofertas.importada, ofertas.substituir);
				$('#modal-importar-ofertas').modal('toggle');
				limparResultadosImportacao();
				var index = 2;
				$.each(ofertas.contidas, function(key, value) {
					adicionarResultado(value.id, "ofertas", '#resultado-substituicao-ofertas-1', '#resultado-substituicao-ofertas-2', value.disciplina.nome, "ofertas", index);	
					index++;
				});
			
		});
	}
}



$('#btn-exibir-ofertas').click(function() {
	var periodo = document.getElementById("periodo").edIndex;
	var periodos = document.getElementById("periodo").options;
	var id = periodos[periodo].value;
	
	if(id > 0){
		$.get(_context + "/ofertas/buscar-ofertas/" + id, function() {
			
		})
		.done(function(ofertas) {
			limparResultadosImportacao();
			var index = 2;
			if(ofertas.length > 0){
				$.each(ofertas, function(key, value) {
					adicionarResultado(value.id, "ofertas", '#resultado-ofertas-1', '#resultado-ofertas-2', value.disciplina.nome, "ofertas", index);	
					index++;
				});
			}else{
				adicionarMensagemSemResultado('#sem-resultado-ofertas');
			}
		});
	}
	
});

function adicionarMensagemSemResultado(coluna){
	var label = document.createElement('p');
    label.setAttribute('class','text-center');
    label.appendChild(document.createTextNode("Não foi encontrado resultado para a sua busca."));
    $(coluna).append(label);
}

function adicionarResultado(id, name, coluna1, coluna2, nome, classe, index){
	
	var checkbox = document.createElement('input');
	checkbox.className = classe;
	checkbox.type = "checkbox";
	checkbox.name = name;
	checkbox.value = id;
	checkbox.id = id;
	checkbox.setAttribute("nome", nome);
	
	
	var label = document.createElement('label')
	label.htmlFor = id;
	label.appendChild(document.createTextNode(nome));
	
	if(index === 2){
	
		var lista1 = document.createElement('ul');
		lista1.id = 'resultado-disciplinas-1';
		lista1.setAttribute('class','list-unstyled');
	
		var lista2 = document.createElement('ul');
		lista2.id = 'resultado-disciplinas-2';
		lista2.setAttribute('class','list-unstyled');
	
	
		$(coluna1).append(lista1);
		$(coluna2).append(lista2);
	}
	
	var ul1 = document.getElementById('resultado-disciplinas-1');
	var ul2 = document.getElementById('resultado-disciplinas-2');
	
	var li = document.createElement('li');
    li.appendChild(checkbox);
    li.appendChild(label);
    li.setAttribute('class', 'checkbox checkbox-success');
    
	
	if(index % 2 == 0){
		ul1.appendChild(li); 
	}else{
		ul2.appendChild(li);
	}
	
}


function importacaoRealizada(importada, substituir) {
//	if (importada && !substituir) {
//		swal({
//			title : "Oferta(as) importadas!",
//			text : "As oferta(as) selecionadas foram importadas.",
//			type : "success",
//			showcancelButton : false,
//			confirmButtonText : "Ok!",
//			closeOnConfirm : true
//		}, function(isConfirm) {
//			$('#modal-substituir-ofertas').modal('show');
//		});
//	}else 
	if(!importada && substituir){
		$('#modal-substituir-ofertas').modal('show');
	}else if(importada && !substituir){
		swal({
			title : "Oferta(as) importadas!",
			text : "As oferta(as) selecionadas foram importadas.",
			type : "success",
			showcancelButton : false,
			confirmButtonText : "Ok!",
			closeOnConfirm : true
		}, function(isConfirm) {
			location.reload();
		});
	}
}

function successSwalC(){
	swal({
		title: "Compartilhamento excluído!",
		text: "O compartilhamento foi excluído.", 
		type: "success",
		showcancelButton: false,
		confirmButtonText: "Ok!",
		closeOnConfirm: true
	}, function(isConfirm){
		location.reload();
	});
}

function errorSubstituirOferta(){
	swal({
		title: "Erro ao substituir",
		text: "A(s) oferta(s) não foi(ram) substituída(s).", 
		type: "error",
		showcancelButton: false,
		confirmButtonText: "Ok",
		closeOnConfirm: true
	});	
}

function errorSwalC(){
	swal({
		title: "Erro ao excluir",
		text: "O compartilhamento não foi excluído.", 
		type: "error",
		showcancelButton: false,
		confirmButtonText: "Ok",
		closeOnConfirm: true
	});	
}
