package com.bolsadeideas.springboot.app.controllers;


import java.io.IOException;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteSerivce;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	@Autowired
	//@Qualifier("clienteDaoJPA")
	private IClienteSerivce clienteService;
	@Autowired
	private IUploadFileService uploadFileService;
	@RequestMapping(value = "/listar",method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page",defaultValue = "0") int page,Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente>pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("clientes",clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	@RequestMapping(value="/form")
	public String crear(Map<String,Object>model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario del cliente");
		
		return "form";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id,Map<String,Object>model,RedirectAttributes flash) {
		Cliente cliente = null;
		if(id>0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				
				flash.addFlashAttribute("danger","El id del cliente  no existe en la base de datos" );
			}

		}else {
			flash.addFlashAttribute("danger","El id del cliente  no puede ser 0" );
			return "redirect:/listar";
		}
		model.put("cliente",cliente);
		model.put("titulo","Ediar cliente" );
		return "form";
	}
	
	
	@GetMapping(value ="/ver/{id}")
	public String ver(@PathVariable(value="id")Long id,Map<String,Object>model,RedirectAttributes flash) {	
		Cliente cliente = clienteService.findOne(id);
		if(cliente == null) {
			flash.addFlashAttribute("error","El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente",cliente);
		model.put("titulo","Detalle cliente: "+cliente.getNombre());
		return "ver";
	}
//	@GetMapping(value = "/uploads/{filename:.+}")
//	public ResponseEntity<Resource>verFoto(@PathVariable String filename){//core.io
//		Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
//		Resource recurso = null;
//		try {
//			 recurso = new UrlResource(pathFoto.toUri());
//			 if(!recurso.exists()||! recurso.isReadable()) {
//				 throw new RuntimeException("No se puede cargar la image");
//			 }
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attatchment; filename=\""+recurso.getFilename()+"\"")
//				.body(recurso);
//		
//		
//	}
	
	
	@RequestMapping(value="/form",method=RequestMethod.POST)
	public String guardar(@Valid Cliente client, BindingResult result,Model model,@RequestParam("file")MultipartFile foto,RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo","formulario de cliente");
			return "form";
		}
		if(!foto.isEmpty()) {
			if(client.getId()!=null &&client.getId()>0 && client.getFoto()!= null && client.getFoto().length()>0) {
					uploadFileService.delete(client.getFoto());
			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info","Archivo subido correctamente '"+uniqueFileName+"'");
			client.setFoto(uniqueFileName);
		
		}
		String mensajeFlash = (client.getId()!=null)?"Cliente editado con exito":"Cliente creado con exito";
		clienteService.save(client);
		
		status.setComplete();
		flash.addFlashAttribute("success",mensajeFlash);
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,Map<String,Object>model,RedirectAttributes flash) {
		if(id>0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success","Cliente eliminado con exito");
			if(uploadFileService.delete(cliente.getFoto())){
				flash.addAttribute("info","foto"+cliente.getFoto()+" eliminada con exito!");
			}
		}

		return "redirect:/listar";
	}
}
