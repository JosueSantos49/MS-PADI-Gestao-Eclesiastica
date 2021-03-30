package br.pro.sispro.migreja.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.pro.sispro.migreja.domain.EventoCalendario;
import br.sispro.migreja.dao.EventoCalendarioDAO;

@ManagedBean
@ViewScoped
public class EventoCalendarioBean implements Serializable {

	private static final long serialVersionUID = 25241208988300281L;

	private EventoCalendario eventoCalendario;
	private ScheduleModel eventoModel;
	private List<EventoCalendario> listaEvento;
	private EventoCalendarioDAO eventoCalendarioDAO;

	public EventoCalendario getEventoCalendario() {
		return eventoCalendario;
	}

	public void setEventoCalendario(EventoCalendario eventoCalendario) {
		this.eventoCalendario = eventoCalendario;
	}

	public ScheduleModel getEventoModel() {
		return eventoModel;
	}

	public void setEventoModel(ScheduleModel eventoModel) {
		this.eventoModel = eventoModel;
	}

	public List<EventoCalendario> getListaEvento() {
		return listaEvento;
	}

	public void setListaEvento(List<EventoCalendario> listaEvento) {
		this.listaEvento = listaEvento;
	}

	public EventoCalendarioDAO getEventoCalendarioDAO() {
		return eventoCalendarioDAO;
	}

	public void setEventoCalendarioDAO(EventoCalendarioDAO eventoCalendarioDAO) {
		this.eventoCalendarioDAO = eventoCalendarioDAO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@PostConstruct
	public void listar() {
		eventoModel = new DefaultScheduleModel();
		eventoCalendarioDAO = new EventoCalendarioDAO();
		eventoCalendario = new EventoCalendario();
		try {
			listaEvento = eventoCalendarioDAO.listar();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro ao listar o evento", "Erro: " + e.getMessage()));
			e.printStackTrace();
		}

		for (EventoCalendario eventoCalendario : listaEvento) {

			DefaultScheduleEvent evt = new DefaultScheduleEvent();

			evt.setEndDate(eventoCalendario.getFim());
			evt.setStartDate(eventoCalendario.getInicio());
			evt.setTitle(eventoCalendario.getTitulo());
			evt.setData(eventoCalendario.getCodigo());
			evt.setDescription(eventoCalendario.getDescricao());
			evt.setAllDay(true);
			evt.setEditable(true);

			// cor do evento
			if (eventoCalendario.getStatus() == true) {
				evt.setStyleClass("public-event1");				
			} else if (eventoCalendario.getStatus() == false) {
				evt.setStyleClass("public-event2");
			}

			eventoModel.addEvent(evt);
		}
	}

	public void quandoSelecionado(SelectEvent selectEvent) {

		ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();

		for (EventoCalendario ev : listaEvento) {
			if (ev.getCodigo() == (Long) event.getData()) {
				eventoCalendario = ev;
				break;
			}
		}
	}

	public void novo(SelectEvent selectEvent) {
		ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(),
				(Date) selectEvent.getObject());

		eventoCalendario = new EventoCalendario();
		eventoCalendario.setInicio(new java.sql.Date(event.getStartDate().getTime()));
		eventoCalendario.setFim(new java.sql.Date(event.getEndDate().getTime()));
	}

	public void salvar() {
		if (eventoCalendario.getCodigo() == null) {
			if (eventoCalendario.getInicio().getTime() <= eventoCalendario.getFim().getTime()) {
				eventoCalendarioDAO = new EventoCalendarioDAO();
				try {
					eventoCalendarioDAO.salvar(eventoCalendario);
					listar();
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao salvar o evento", "Erro: " + e.getMessage()));
					e.printStackTrace();
				}
				eventoCalendario = new EventoCalendario();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data inicial não pode ser maior que a inicial",
								"Data inicial não pode ser maior que a inicial"));
			}
		} else {
			try {
				eventoCalendarioDAO.editar(eventoCalendario);
				listar();
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao salvar o evento", "Erro: " + e.getMessage()));
			}
		}
	}

	public void quandoMover(ScheduleEntryMoveEvent eventoCalen) {
		for (EventoCalendario ev : listaEvento) {
			if (ev.getCodigo() == (Long) eventoCalen.getScheduleEvent().getData()) {
				eventoCalendario = ev;
				eventoCalendarioDAO = new EventoCalendarioDAO();
				try {
					eventoCalendarioDAO.editar(eventoCalendario);
					listar();
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao salvar o evento", "Erro: " + e.getMessage()));
				}
				break;
			}
		}
	}

	public void quandoRedimensionar(ScheduleEntryResizeEvent eventoCalen) {
		for (EventoCalendario ev : listaEvento) {
			if (ev.getCodigo() == (Long) eventoCalen.getScheduleEvent().getData()) {
				eventoCalendario = ev;
				eventoCalendarioDAO = new EventoCalendarioDAO();
				try {
					eventoCalendarioDAO.editar(eventoCalendario);
					listar();
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao salvar o evento", "Erro: " + e.getMessage()));
				}
				break;
			}
		}
	}
	
	
}