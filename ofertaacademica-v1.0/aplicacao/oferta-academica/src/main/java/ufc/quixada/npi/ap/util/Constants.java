package ufc.quixada.npi.ap.util;

public class Constants {

	public static final Integer MAX_CREDITOS_TURMA = 20;

	// LDAP

	public static final String LDAP_URL = "ldap.url";

	public static final String LDAP_BASE = "ldap.base";

	public static final String LDAP_USER = "ldap.user";

	public static final String LDAP_PASSWORD = "ldap.password";

	public static final String LDAP_OU = "ldap.ou";

	// Afiliações

	public static final String AFFILIATION_DOCENTE = "DOCENTE";

	// Permissões

	public static final String PERMISSAO_COORDENACAO = "hasAuthority('COORDENACAO')";

	public static final String PERMISSAO_DIRECAO = "hasAuthority('DIRECAO')";

	public static final String PERMISSAO_DIRECAO_COORDENACAO = "hasAnyAuthority('DIRECAO,COORDENACAO')";

	// Mensagens

	public static final String MSG_PERMISSAO_NEGADA = "Permissão negada";

	public static final String MSG_LOGIN_INVALIDO = "Usuário e/ou senha inválidos";

	// Paginas

	public static final String PAGINA_ERRO_404 = "/error/404";

	public static final String PAGINA_ERRO_403 = "/error/403";

	public static final String PAGINA_ERRO_500 = "/error/500";

	// Ofertas

	public static final String OFERTA_CADASTRAR = "/ofertas/cadastrar-oferta";

	public static final String OFERTA_IMPORTAR = "/ofertas/importar-oferta";

	public static final String OFERTA_EDITAR = "/ofertas/editar-oferta";

	public static final String OFERTA_LISTAR = "/ofertas/listar-oferta";

	public static final String OFERTA_VISUALIZAR_OFERTA = "/ofertas/visualizar-oferta";

	public static final String OFERTA_DETALHAR = "/ofertas/detalhar-oferta";

	public static final String OFERTA_EXCLUIR = "/ofertas/excluir-oferta";

	public static final String OFERTA_REDIRECT_LISTAR = "redirect:/ofertas";

	public static final String OFERTA_REDIRECT_LISTAR_T = "redirect:/ofertas/1";

	public static final String OFERTA_CAMPUS_REDIRECT_LISTAR = "redirect:/oferta-campus";
	public static final String OFERTA_REDIRECT_IMPORTAR= "redirect:/ofertas/importar";
	public static final String OFERTA_REDIRECT_CADASTRO = "redirect:/ofertas/cadastrar";

	public static final String OFERTA_CADASTRADA = "Oferta cadastrada com sucesso!";

	public static final String EXPORTAR = "/ofertas/exportar";

	public static final String OFERTA_REDIRECT_EDITAR_DIRECAO = "redirect:/editar-oferta/";

	// Compartilhamento

	public static final String COMPARTILHAMENTO_CADASTRAR = "compartilhamento/cadastrar-compartilhamento";

	public static final String COMPARTILHAMENTO_CADASTRAR_DIRECAO = "compartilhamento/cadastrar-compartilhamento-direcao";

	public static final String COMPARTILHAMENTO_EDITAR = "compartilhamento/editar-compartilhamento";

	public static final String COMPARTILHAMENTO_LISTAR = "compartilhamento/listar-compartilhamento";

	public static final String COMPARTILHAMENTO_DETALHAR = "compartilhamento/detalhar-compartilhamento";

	public static final String COMPARTILHAMENTO_REDIRECT_LISTAR = "redirect:/compartilhamentos";

	// Periodo

	public static final String PERIODO_CADASTRAR = "periodo/cadastrar-periodo";

	public static final String PERIODO_DETALHAR = "periodo/detalhar-periodo";

	public static final String PERIODO_EDITAR = "periodo/editar-periodo";

	public static final String PERIODO_EXCLUIR = "periodo/excluir-periodo";

	public static final String PERIODO_LISTAR = "periodo/listar-periodo";

	public static final String PERIODO_REDIRECT_EDITAR = "redirect:/editar-periodo";

	public static final String PERIODO_REDIRECT_LISTAR = "redirect:/periodos";

	// CheckList

	public static final String CHECKLIST_DIRECAO = "checklist/checklist-direcao";

	public static final String CHECKLIST_COORDENACAO = "checklist/checklist-coordenacao";

	public static final String CHECKLIST_REDIRECT_DIRECAO = "redirect:/checklist/checklist-direcao";

	public static final String CHECKLIST_REDIRECT_COORDENACAO = "redirect:/checklist/checklist-coordenacao";

	// Disciplina

	public static final String DISCIPLINA_CADASTRAR = "disciplina/cadastrar-disciplina";

	public static final String DISCIPLINA_EDITAR = "disciplina/editar-disciplina";

	public static final String DISCIPLINA_LISTAR = "disciplina/listar-disciplinas";

	public static final String DISCIPLINA_REDIRECT_LISTAR = "redirect:/disciplinas";

	public static final String DISCIPLINA_REDIRECT_CADASTRAR = "redirect:/disciplinas/cadastrar";

	public static final String DISCIPLINA_REDIRECT_EDITAR = "redirect:/disciplinas/editar/";

	public static final String DISCIPLINA_DETALHES = "disciplina/detalhes-disciplina";

	// Empilhamento

	public static final String EMPILHAMENTO_CADASTRAR = "empilhamento/cadastrar-empilhamento";

	public static final String EMPILHAMENTO_EDITAR = "empilhamento/editar-empilhamento";

	public static final String EMPILHAMENTO_LISTAR = "empilhamento/listar-empilhamento";

	public static final String EMPILHAMENTO_DETALHAR = "empilhamento/detalhar-empilhamento";

	public static final String EMPILHAMENTO_REDIRECT_LISTAR = "redirect:/empilhamentos";

	public static final String EMPILHAMENTO_LISTAR_DIRECAO = "empilhamento/listar-empilhamento-direcao";

	// Professor

	public static final String PROFESSOR_LISTAR = "professor/listar-professores";

	public static final String PROFESSOR_REDIRECT_LISTAR = "redirect:/professores";

	public static final String PROFESSOR_CADASTRAR = "/professor/cadastrar-professor";

	public static final String PROFESSOR_REDIRECT_CADASTRAR = "redirect:/professores/cadastrar-professor";

	public static final String PROFESSOR_REDIRECT_EDITAR = "redirect:/professores/editar-professor";

	public static final String PROFESSOR_EDITAR = "professor/editar-professor";

	public static final String PROFESSOR_RELATORIO_CARGA_HORARIA = "professor/gerar-relatorio-carga-horaria-professor";

	public static final String PROFESSOR_DETALHES = "professor/detalhes-professor";

	public static final String PROFESSOR_REDIRECT_DETALHES = "redirect:/professores/detalhes-professor";

	// Curso

	public static final String CURSO_CADASTRAR = "curso/cadastrar-curso";

	public static final String CURSO_EDITAR = "curso/editar-cursos";

	public static final String CURSO_LISTAR = "curso/listar-cursos";

	public static final String CURSO_DETALHAR = "curso/detalhar-cursos";

	public static final String CURSO_REDIRECT_LISTAR = "redirect:/cursos";

	public static final String CURSO_REDIRECT_EDITAR = "redirect:/cursos/editar";

	public static final String CURSO_REDIRECT_CADASTRAR = "redirect:/cursos/cadastrar";

	public static final String CONFIGURACAO_EDITAR = "configuracao";

	public static final String CONFIGURACAO_REDIRECT_EDITAR = "redirect:/configuracao";

	// Exceptions

	public static final String EXCEPTION_PERIODO_INVALIDO = "Não há um período válido disponível";

	// Mensagens Sucesso

	public static final String SWAL_STATUS_SUCCESS = "success";

	public static final String MSG_DISCIPLINA_CADASTRAR_SUCCESS = "Disciplina cadastrada com sucesso!";

	public static final String MSG_DISCIPLINA_EDITAR_SUCCESS = "Disciplina editada com sucesso!";

	public static final String MSG_PERIODO_CADASTRAR_SUCCESS = "Período cadastrado com sucesso!";

	public static final String MSG_PERIODO_EDITAR_SUCCESS = "Período editado com sucesso!";

	public static final String MSG_OFERTA_CADASTRADA = "Oferta cadastrada com sucesso!";

	public static final String MSG_CONFIGURACOES_ATUALIZADAS = "As configurações foram atualizadas com sucesso!";

	public static final String MSG_OFERTA_EDITADA = "Oferta editada com sucesso!";

	public static final String MSG_OFERTA_EXCLUIR = "Oferta excluída com sucesso!";

	public static final String MSG_COMPARTILHAMENTO_EXCLUIR = "Solicitação de compartilhamento excluída com sucesso!";

	public static final String MSG_PROFESSOR_EDITADO = "Professor editado com sucesso!";

	public static final String MSG_PROFESSOR_CADASTRADO = "Professor cadastrado com sucesso!";

	public static final String MSG_COMPARTILHAMENTO_EDITADO = "Solicitação de compartilhamento editada com sucesso!";

	public static final String MSG_COMPARTILHAMENTO_SOLICITADO = "Solicitação de compartilhamento cadastrada com sucesso!";

	public static final String MSG_IMPORTACAO_REALIZADA = "Importação realizada com sucesso";

	public static final String MSG_ITEM_CHECKLIST_CADASTRADO = "Item adicionado!";

	public static final String MSG_ITEM_CHECKLIST_ATUALIZADO = "Item atualizado!";

	public static final String MSG_ITEM_CHECKLIST_EXCLUÌDO = "Item excluído!";

	public static final String MSG_RESTRICAO_HORARIO_EXCLUIDO = "Restrição de horário excluída com sucesso";

	public static final String MSG_RESTRICAO_HORARIO_HABILITADO = "Restrição de horário habilitada com sucesso";

	public static final String MSG_RESTRICAO_HORARIO_DESABILITADO = "Restrição de horário desabilitada com sucesso";

	public static final String MSG_HORARIO_BLOQUEADO_CADASTRADO = "Horário bloqueado cadastrado com sucesso.";

	public static final String MSG_HORARIO_BLOQUEADO_EXCLUIDO = "Horário bloqueado excluído com sucesso.";

	// Mensagens de Sucesso - Curso

	public static final String MSG_CURSO_CADASTRO_TITULO_SUCCESS = "Curso cadastrado!";

	public static final String MSG_CURSO_CADASTRADO = "O curso foi cadastrado com sucesso!";

	public static final String MSG_CURSO_EDITADO = "Curso foi editado com sucesso!";

	// Mensagens Informação

	public static final String SWAL_STATUS_INFO = "info";

	public static final String MSG_PREFERENCIA_CADASTRADA = "Preferência cadastrada com sucesso.";

	public static final String MSG_PREFERENCIA_EXCLUIDA = "Preferência excluida com sucesso.";

	// Mensagens Erros

	public static final String MESMA_PESSOA = "Você não pode se relacionar com você mesmo.";

	public static final String CPF_INVALIDO = "CPF inválido.";

	public static final String CPF_JA_CADASTRADO = "CPF já cadastrado.";

	public static final String CARGA_MAX_MENOR_CARGA_MIN = "Carga horária mínima maior que carga horária máxima.";

	public static final String NUM_CARACTERES_DO_APELIDO_VAZIO_OU_MAIOR20 = "Apelido do professor vazio ou maior de 20 caracteres.";

	public static final String STATUS_ERROR = "error";

	public static final String DISCIPLINA_CADASTRAR_TITULO_ERROR = "Erro ao cadastrar a disciplina";

	public static final String MSG_EMPILHAMENTO_CADASTRAR = "Restrição de horário cadastrada com sucesso";

	public static final String MSG_EMPILHAMENTO_CADASTRAR_EXISTENTE = "Restrição de horário já existente!";

	public static final String DISTINTO_CADASTRAR_EXISTENTE = "Horário Distinto já existente!";

	public static final String DISCIPLINA_CADASTRAR_EXISTENTE = "Já existe uma disciplina com esse código!";

	public static final String RESTRICAO_PERIODO = "Esta operação é restrita por períodos. Por favor verifique as datas do período.";

	public static final String RESTRICAO_PERIODO_ABERTO = "Já existe um período aberto!";

	public static final String SWAL_STATUS_ERROR = "error";

	public static final String MSG_OFERTA_CADASTRADA_ERROR = "Erro ao cadastrar Oferta";

	public static final String MSG_DISCIPLINA_CADASTRAR_TITULO_ERROR = "Erro ao cadastrar a disciplina";

	public static final String MSG_DISCIPLINA_EDITAR_TITULO_ERROR = "Erro ao editar a disciplina";

	public static final String MSG_DISCIPLINA_CADASTRAR_EXISTENTE = "Já existe uma disciplina com esse código.";

	public static final String MSG_HORARIO_BLOQUEADO_CADASTRAR_EXISTENTE = "Este horário bloqueado já existe.";

	public static final String MSG_HORARIO_BLOQUEADO_SQL_CADASTRAR = "Não foi possível adicionar o horário bloqueado. Tente novamente.";

	public static final String MSG_HORARIO_BLOQUEADO_SQL_EXCLUIR = "Não foi possível remover o horário bloqueado. Tente novamente.";

	// Mensagens Erros - Curso

	public static final String CURSO_CADASTRO_TITULO_ERROR = "Erro ao cadastrar o curso";

	public static final String MSG_CURSO_CADASTRO_SIGLA_EXISTENTE = "Já existe um Curso com essa sigla!";

	public static final String MSG_CURSO_CADASTRO_COORDENADOR_EXISTENTE = "Já é Coordenador!";

	public static final String MSG_CURSO_CADASTRO_VICECOORDENADOR_EXISTENTE = "Já é Vice-Coordenador!";

	public static final String MSG_CURSO_CADASTRO_TITULO_ERROR = "Erro ao cadastrar o curso";

	public static final String MSG_CURSO_CADASTRO_EXISTENTE = "Já existe um curso com esse código!";

	public static final String MSG_CURSO_CADASTRO_TITULO_EXISTENTE = "Já existe um Curso com esse nome!";

	// Prefixos das mensagens de erro de validação

	public static final String VALIDACAO_ERRO_INVALID = "invalid";

	public static final String VALIDACAO_ERRO_NULL = "null";

}