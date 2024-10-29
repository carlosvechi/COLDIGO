COLDIGO.marca = new Object();

$(document).ready(function() {
    COLDIGO.marca.cadastroMarca = function() {
        var marca = new Object();
        marca.nome = document.frmAddMarca.marca.value;

        if (marca.nome == "") {
            COLDIGO.exibirAviso("Preencha todos os campos!");
        } else {
            $.ajax({
                type: "POST",
                url: COLDIGO.PATH + "marca/inserir",
                data: JSON.stringify(marca),
                success: function(msg) {
                    COLDIGO.exibirAviso(msg);
                    $("#addMarca").trigger("reset");
                },
                error: function(info) {""
                    COLDIGO.exibirAviso("Erro ao cadastrar um novo produto: " + info.status + " - " + info.statusText);
                }
            });
        }
    }
        
    COLDIGO.marca.buscar = function(){
		
		//Aqui pegamos o 'valor' digitado no campo de 'campoBuscaProduto' e inserimos no 'valorBusca'
		var valorBusca = $("#campoBuscaMarca").val();
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarMarca",
			data: "valorBusca=" + valorBusca, //Nome do campo: "valorBusca" e nome da variavél: valorBusca. CAMPO/VALOR.
			success: function(dados){
				dados = JSON.parse(dados); //dados é a variavél que recebe os objetos da pesquisa em um formato JSON.
				//JSON.parse converterá o objeto em um formato JSON para poder mostrar na tabela HTML.
				$("#listaMarcas").html(COLDIGO.marca.exibir(dados)); //o campo 'listaMarcas' receberá num formato HTML os dados vindo do BD pelo parametro 'dados'.
			},
			error: function(info){
				COLDIGO.exibirAviso("Erro ao consultar os contatos: " + info.status + " - " + info.statusText);
			}
		});
	};
    
    COLDIGO.marca.exibir = function(listaDeMarcas) {
		var tabela = "<table>" +
		"<tr>" +
		"<tr>" +
		"<th>Marca</th>" +
		"<th class='acoes'>Ações</th" +
		"</tr>";
		
		if(listaDeMarcas != undefined && listaDeMarcas.length > 0) {
			for(var i = 0; i < listaDeMarcas.length; i++) {
				tabela+= "<tr>" +
						"<td>"+listaDeMarcas[i].marcaNome+"</td>" +
						"<td>" +
							"<a onclick=\"COLDIGO.marca.exibirEdicao('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/Edit.png' alt='Editar registro'></a> " +
							"<a onclick=\"COLDIGO.marca.excluir('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/delete.png' alt='Excluir registro'></a>" +
							"</td>" +
							"</tr>"
			}
		} else if(listaDeMarcas == "") {
			tabela+= "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>"
		}
		tabela += "<table>";
		
		return tabela;
	};
	
	COLDIGO.marca.buscar();
    
    
    COLDIGO.marca.excluir = function(id){
		alert(id);
		$.ajax({
			type: "DELETE",
			url: COLDIGO.PATH + `marca/excluir/${id}`,
			success: function(msg){
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar(); //irá atualizar a lista dos produtos para não aparecer o item excluído. 
			},
			error: function(info){
				COLDIGO.exibirAviso("Erro ao excluir produto: " + info.status + " - " + info.statusText);
			}
		});
	};
	
	COLDIGO.marca.exibirEdicao = function(id) {
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarPorId",
			data: "id="+id,
			success: function(marca) {
				
				//Vai jogar o que está digitado no formulário HTML para as variavéis.
				document.frmEditaMarca.idMarca.value = marca.id; 
				document.frmEditaMarca.marcaId.value = marca.nome;
				
				var modalEditaMarca = {
					title: "Edita marca",
					height: 400,
					width: 550,
					modal: true,
					buttons: {
						"Salvar": function() {
							COLDIGO.marca.editar();
						},
						"Cancelar": function() {
							$(this).dialog("close");
						}
					},
					close: function() {
						
					}
				};
				$("#modalEditaMarca").dialog(modalEditaMarca);
			},
			error: function(info){
				COLDIGO.exibirAviso("Erro ao buscar marca para edição: " + info.status + " - " + info.statusText);
			}
			
		});
	}
	
	COLDIGO.marca.editar = function(id) {
		
		var marca = new Object();
		marca.id = document.frmEditaMarca.idMarca.value;
		marca.nome = document.frmEditaMarca.marcaId.value;
		$.ajax({
			type: "PUT",
			url: COLDIGO.PATH + "marca/alterar",
			data: JSON.stringify(marca),
			success: function(msg) {
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
				$("#modalEditaMarca").dialog("close");
			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao editar marca: " + info.status + " - " + info.statusText);
			}
		})
	}
 });
