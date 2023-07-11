package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int numeroElementosPorPaginas;
	private int paginaActual;
	
	private List<PageItem>paginas;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Page<T> getPage() {
		return page;
	}
	public void setPage(Page<T> page) {
		this.page = page;
	}
	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		numeroElementosPorPaginas =page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber()+1;
		int desde, hasta;
		if(totalPaginas<= numeroElementosPorPaginas) {
			desde = 1;
			hasta = totalPaginas;
		}else {
			if(paginaActual <= numeroElementosPorPaginas/2) {
				desde =1;
				hasta=numeroElementosPorPaginas;
			}else if(paginaActual>= totalPaginas- numeroElementosPorPaginas/2) {
				desde = totalPaginas-numeroElementosPorPaginas+1;
				hasta = numeroElementosPorPaginas;
			}else {
				desde = paginaActual- numeroElementosPorPaginas/2;
				hasta = numeroElementosPorPaginas;
			}
		}
		for(int i = 0; i<hasta;i++) {
			paginas.add(new PageItem(desde+i,paginaActual==desde+i));
		}
	}
	public int getTotalPaginas() {
		return totalPaginas;
	}
	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}
	public int getNumeroElementosPorPaginas() {
		return numeroElementosPorPaginas;
	}
	public void setNumeroElementosPorPaginas(int numeroElementosPorPaginas) {
		this.numeroElementosPorPaginas = numeroElementosPorPaginas;
	}
	public int getPaginaActual() {
		return paginaActual;
	}
	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}
	public List<PageItem> getPaginas() {
		return paginas;
	}
	public void setPaginas(List<PageItem> paginas) {
		this.paginas = paginas;
	}
	public boolean isFirst() {
		return page.isFirst();
	}
	public boolean isLast() {
		return page.isLast();
	}
	public boolean isHasNext() {
		return page.hasNext();
	}
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
	
}
