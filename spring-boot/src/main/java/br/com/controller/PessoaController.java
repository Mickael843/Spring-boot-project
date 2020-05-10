package br.com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.model.Pessoa;
import br.com.model.Telefone;
import br.com.repository.PessoaRepository;
import br.com.repository.ProfissaoRepository;
import br.com.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaReposytory;
	
	@Autowired
	private TelefoneRepository telefoneReposytoty;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastroPessoa")
	public ModelAndView inicio() {

		Iterable<Pessoa> pessoasIterator = pessoaReposytory.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		
		ModelAndView view = new ModelAndView("cadastro/cadastropessoa");
		
		view.addObject("pessoas", pessoasIterator);
		view.addObject("pessoaObj", new Pessoa());
		view.addObject("profissoes", profissaoRepository.findAll());
		
		return view;
	}
	
	@GetMapping("/pessoaspag")
	public ModelAndView carregaPessoasPorPaginacao(@PageableDefault(size = 5) Pageable numeroDaPagina,
			ModelAndView view, @RequestParam("nomePesquisa") String nomePesquisa) {

		Page<Pessoa> pagePessoa = pessoaReposytory.findPessoaByNamePage(nomePesquisa, numeroDaPagina);
		view.addObject("pessoas", pagePessoa);
		view.addObject("pessoaObj", new Pessoa());
		view.addObject("nomePesquisa", nomePesquisa);
		view.setViewName("cadastro/cadastropessoa");
		
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarPessoa",
			consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult result,
			final MultipartFile file) throws IOException {
		
		if(pessoa.getId() != null) {
			pessoa.setTelefones(telefoneReposytoty.getTelefones(pessoa.getId()));
		}
		
		/*Validação*/
		if(result.hasErrors()) {
			ModelAndView view = new ModelAndView("cadastro/cadastropessoa");
			
			view.addObject("pessoas", pessoaReposytory.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			view.addObject("pessoaObj", pessoa);
			view.addObject("profissoes", profissaoRepository.findAll());
			
			List<String> msg = new ArrayList<>();
			
			for(ObjectError objectError : result.getAllErrors()) {
				//Pega as mensagens de erros que estão nas anotações na entidade
				msg.add(objectError.getDefaultMessage());
			}
			view.addObject("msg", msg);
			
			return view;
		} else {
			
			if(file.getSize() > 0) {
				pessoa.setCurriculo(file.getBytes());
				pessoa.setTipoFileCurriculo(file.getContentType());
				pessoa.setNomeFileCurriculo(file.getOriginalFilename());
			} else {
				
				if(pessoa.getId() != null && pessoa.getId() > 0) {
					Pessoa pessoaTmp = pessoaReposytory.findById(pessoa.getId()).get();
					
					pessoa.setCurriculo(pessoaTmp.getCurriculo());
					pessoa.setTipoFileCurriculo(pessoaTmp.getTipoFileCurriculo());
					pessoa.setNomeFileCurriculo(pessoaTmp.getNomeFileCurriculo());
				}
				
			}
			
			pessoaReposytory.save(pessoa);
			
			ModelAndView view = new ModelAndView("cadastro/cadastropessoa");
			
			view.addObject("pessoas", pessoaReposytory.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			view.addObject("pessoaObj", new Pessoa());
			view.addObject("profissoes", profissaoRepository.findAll());
			
			return view;
		}
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editarPessoas(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = pessoaReposytory.findById(idpessoa);
		
		ModelAndView view = new ModelAndView("cadastro/cadastropessoa");
		
		view.addObject("pessoas", pessoaReposytory.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		view.addObject("pessoaObj", pessoa.get());
		view.addObject("profissoes", profissaoRepository.findAll());
		
		return view;
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView removerPessoa(@PathVariable("idpessoa") Long idpessoa) {
		
		ModelAndView view = new ModelAndView("cadastro/cadastropessoa");
		
		pessoaReposytory.deleteById(idpessoa);
		
		view.addObject("pessoas", pessoaReposytory.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		view.addObject("pessoaObj", new Pessoa());
		view.addObject("profissoes", profissaoRepository.findAll());
		
		return view;
	}
	
	@PostMapping("**/pesquisarPessoa")
	public ModelAndView pesquisar(@RequestParam("nomePesquisa") String nomePesquisa,
			@RequestParam("pesquisaSexo") String pesquisaSexo, 
			@PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
		
		Page<Pessoa> pessoas = null;
		
		if(pesquisaSexo != null && !pesquisaSexo.isEmpty()) {
			pessoas = pessoaReposytory.findPessoaByNameAndSexoPage(nomePesquisa, pesquisaSexo, pageable);
		} else {
			pessoas = pessoaReposytory.findPessoaByNamePage(nomePesquisa, pageable);
		}
		
		ModelAndView view = new ModelAndView("cadastro/cadastropessoa");
		
		view.addObject("pessoas", pessoas);
		view.addObject("pessoaObj", new Pessoa());
		view.addObject("profissoes", profissaoRepository.findAll());
		view.addObject("nomePesquisa", nomePesquisa);
		
		return view;
	}
	
	@GetMapping("**/pesquisarPessoa")
	public void imprimirPdf(@RequestParam("nomePesquisa") String nomePesquisa,
			@RequestParam("pesquisaSexo") String pesquisaSexo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Pessoa> pessoas = new ArrayList<>();
		
		if(pesquisaSexo != null && !pesquisaSexo.isEmpty()
				&& nomePesquisa != null && !nomePesquisa.isEmpty()) { // Busca por nome e sexo
		
			pessoas = pessoaReposytory.findPersonByNameSexo(nomePesquisa, pesquisaSexo);
			
		} else if(nomePesquisa != null && !nomePesquisa.isEmpty()) { // Busca por nome
			
			pessoas = pessoaReposytory.findPersonByName(nomePesquisa);
			
		} else if(pesquisaSexo != null && !pesquisaSexo.isEmpty()) { // Busca por sexo
		
			pessoas = pessoaReposytory.findPersonBySexo(pesquisaSexo);
			
		} else { // Busca todos
			
			Iterable<Pessoa> iterator = pessoaReposytory.findAll(PageRequest.of(0, 5, Sort.by("nome")));
			
			for (Pessoa pessoa : iterator) {
				pessoas.add(pessoa);
			}
		}
		
		/* Chama o serviço que faz a geração do relatorio */
		byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
		
		/* Tamanho da resposta para o navegador */
		response.setContentLength(pdf.length);
		
		/* Definir na resposta o tipo de arquivo */
		response.setContentType("application/octet-stream");
		
		/* Definir o cabeçalho da resposta */
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		
		response.setHeader(headerKey, headerValue);
		
		/* Finaliza a resposta para o navegador */
		response.getOutputStream().write(pdf);
		
	}
	
	@GetMapping("**/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = pessoaReposytory.findById(idpessoa);
		
		List<Telefone> telefones = telefoneReposytoty.getTelefones(idpessoa);
		
		ModelAndView view = new ModelAndView("cadastro/telefones");
		
		view.addObject("pessoaObj", pessoa.get());
		view.addObject("telefones", telefones);
		
		return view;
	}
	
	@PostMapping("**/addFonePessoa/{pessoaId}")
	public ModelAndView addFonePessoa(@Valid Telefone telefone, 
			BindingResult result, @PathVariable("pessoaId") Long pessoaId) {
		
		Optional<Pessoa> pessoa = pessoaReposytory.findById(pessoaId);
		
		if(telefone != null && telefone.getNumero().isEmpty()
				|| telefone.getTipo().isEmpty()) {
			
			ModelAndView view = new ModelAndView("cadastro/telefones");
			
			view.addObject("pessoaObj", pessoa.get());
			view.addObject("telefones", telefoneReposytoty.getTelefones(pessoaId));
			
			List<String> msg = new ArrayList<>();
			if(telefone.getNumero().isEmpty()) {
				msg.add("Número deve ser Informado");
			}

			if(telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser Informado");
			}
			
			view.addObject("msg", msg);
			
			return view;
		} 
		
		telefone.setPessoa(pessoa.get());
		telefoneReposytoty.save(telefone);
		
		ModelAndView view = new ModelAndView("cadastro/telefones");
		
		view.addObject("pessoaObj", pessoa.get());
		view.addObject("telefones", telefoneReposytoty.getTelefones(pessoaId));
		
		return view;
	}
	
	@GetMapping("**/removerTelefone/{idtelefone}")
	public ModelAndView removerTelefone(@PathVariable("idtelefone") Long idtelefone) {
		
		Pessoa pessoa = telefoneReposytoty.findById(idtelefone).get().getPessoa();
		
		telefoneReposytoty.deleteById(idtelefone);
		
		ModelAndView view = new ModelAndView("cadastro/telefones");
		
		view.addObject("pessoaObj", pessoa);
		view.addObject("telefones", telefoneReposytoty.getTelefones(pessoa.getId()));
		
		return view;
	}
	
	@GetMapping("**/baixarCurriculo/{idpessoa}")
	public void baixarCuriiculo(@PathVariable("idpessoa") Long idpessoa,
			HttpServletResponse response) throws IOException {
		
		
		/* Consultar o objeto pessoa no banco de dados */
		Pessoa pessoa = pessoaReposytory.findById(idpessoa).get();
		
		if(pessoa.getCurriculo() != null) {
			
			/* Setar o tamanho da resposta */
			response.setContentLength(pessoa.getCurriculo().length);
			
			/* Setar o tipo do arquivo para download ou pode ser generica usando application/octet-stream */
			response.setContentType(pessoa.getTipoFileCurriculo());
			
			/* Definir o cabeçalho da resposta */
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
			
			response.setHeader(headerKey, headerValue);
			
			/* Finaliza a resposta passando o arquivo para download */
			response.getOutputStream().write(pessoa.getCurriculo());
		}
	}
}
