package br.com.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query("select p from Pessoa p where p.nome like %?1%")
	List<Pessoa> findPersonByName(String name);

	@Query("select p from Pessoa p where p.sexoPessoa = ?1")
	List<Pessoa> findPersonBySexo(String sexo);

	@Query("select p from Pessoa p where p.nome like %?1% and p.sexoPessoa = ?2")
	List<Pessoa> findPersonByNameSexo(String name, String sexopessoa);

	default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {

		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);

		/*
		 * Configurando a pesquisa para consultar com partes do nome no banco de dados
		 */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		/* Une o objeto com o valor e a coniguração para consulta */
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

		Page<Pessoa> pessoas = findAll(example, pageable);

		return pessoas;
	}

	default Page<Pessoa> findPessoaByNameAndSexoPage(String nome, String sexo, Pageable pageable) {

		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexoPessoa(sexo);

		/*
		 * Configurando a pesquisa para consultar com partes do nome no banco de dados
		 */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexoPessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		/* Une o objeto com o valor e a coniguração para consulta */
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

		Page<Pessoa> pessoas = findAll(example, pageable);

		return pessoas;
	}
}
