# Spring-boot-project
### Projeto criado como material de estudos, sendo utilizado para testar novas tecnologias e conhecimentos adiquiridos ao longo do tempo 

### Sobre o projeto 
Ao entrar no sistema o usuário deve ser autenticado utilizando um login e senha já cadastrados no banco de dados, para está autenticação é utilizado o spring security no back end. A primeira tela depois do login do usuário é composta por 3 elementos principais. O primeiro é onde pode ser efetuado o cadastro e a atualização dos usuários. O segundo elemento é composto por uma barra de pesquisa que trás o usuário de acordo com parte do nome escrito e/ou com o sexo informado no combo-box, também é possivel imprimir um relátorio com os elementos filtrados no campo de busca. É no terceiro elemento que é mostrado os usuários cadastrados no sistema, também é possivel fazer download do curriculo informado ao cadastrar um novo usuário. Ao clicar sobre o nome do usuário é redirecionado para uma tela onde mostra todos os dados desse usuário e onde é possivel cadastrar novos telefones. No final da listagem de usuários está contido a parte de paginação do sistema.

### Tecnologias utilizadas 
##### back-end
- Spring boot
- Spring mvc
- Spring data
- Spring security

##### front-end
- HTML5 
- CSS3
- Thymeliaf
- Materialize
- JQuery

### Imagens do sistema
<table>
	<thead>
    	<tr>
      		<th>Login</th>
      		<th>cadastro de usuários</th>
   	 	</tr>
  	</thead>
	<tbody>
		<tr>
			<td>
				<img src="https://github.com/Mickael843/Spring-boot-project/blob/master/login.jpeg" width="500">
			</td>
			<td>
				<img src="https://github.com/Mickael843/Spring-boot-project/blob/master/tela1_form1_parte1.jpeg" width="500">
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<img src="https://github.com/Mickael843/Spring-boot-project/blob/master/tela1_form1_parte2.jpeg" width="500">
			</td>
		</tr>
	</tbody>
</table>
<br />
<table>
	<thead>
    	<tr>
      		<th>Busca por usuários</th>
      		<th>Usuários cadastrados</th>
   	 	</tr>
  	</thead>
	<tbody>
		<tr>
			<td>
				<img src="https://github.com/Mickael843/Spring-boot-project/blob/master/tela1_buscaUsuario.jpeg" width="500">
			</td>
			<td>
				<img src="https://github.com/Mickael843/Spring-boot-project/blob/master/tela1_listaDeUsuarios.jpeg" width="500">
			</td>
		</tr>
	</tbody>
</table>



