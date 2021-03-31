package com.eventsweb.challenge.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.eventsweb.challenge.entitites.Event;
import com.eventsweb.challenge.entitites.User;
import com.eventsweb.challenge.repository.EventRepository;
import com.eventsweb.challenge.repository.UserRepository;
import com.eventsweb.challenge.services.exceptions.EventNotFoundException;
import com.eventsweb.challenge.services.exceptions.ExpiredTimeException;
import com.eventsweb.challenge.services.exceptions.VacanciesUnavailableException;
import com.eventsweb.challenge.services.utils.EventMessage;

public class EventService {
	
	@Autowired
	private EventRepository repositoryEvent;
	
	@Autowired
	private UserRepository repositoryUser;

	public Event register(Long eventId, User user) {
		
			Optional<Event> optional = repositoryEvent.findById(eventId);
			Event event = optional.orElseThrow(() -> new EventNotFoundException(eventId));
			Integer vacancies = event.getVacancies();
			Instant now = Instant.now();
			List<User> list = event.getUsers();
				
			if (vacancies > list.size() && now.compareTo(event.getStartDate().plusSeconds(-3600)) < 0) {
				list.add(user);
			}
			if (vacancies <= list.size()) {
				throw new VacanciesUnavailableException("The event is packed");
			}
			if (now.compareTo(event.getStartDate().plusSeconds(-3600)) > 0) {
				throw new ExpiredTimeException("Expired event entry time");
			}
			 return event;
	}
	
	public EventMessage entryEvent(Long eventId, Long userId) {
		
		Optional<Event> optional = repositoryEvent.findById(eventId);
		Event event = optional.orElseThrow(() -> new EventNotFoundException(eventId));
		Optional<User> optionalUser = repositoryUser.findById(userId); 
		User user = optionalUser.orElseThrow(() -> new EventNotFoundException(userId));
		List<User> list = event.getUsers();
		Instant now = Instant.now();
		
		if (list.contains(user) && now.compareTo(event.getStartDate().plusSeconds(-3601)) > 0 && now.compareTo(event.getFinishDate().plusSeconds(-3600)) < 0) {
			return new EventMessage("The client has access to the event!!");
		}
		return new EventMessage("The client doest not have access to the event!!");
	}
	
	public EventMessage deregister(Long eventId, Long userId) {
		
		Optional<Event> optional = repositoryEvent.findById(eventId);
		Event event = optional.orElseThrow(() -> new EventNotFoundException(eventId));
		Optional<User> optionalUser = repositoryUser.findById(userId); 
		User user = optionalUser.orElseThrow(() -> new EventNotFoundException(userId));
		List<User> list = event.getUsers();
		EventService service = new EventService();
		EventMessage eventMessage = service.entryEvent(eventId, userId);
		String message = eventMessage.getMsg();
		String messageAccess = "The client has access to the event!!";
		
		if (list.contains(user) && message != messageAccess) {
			list.remove(user);
			return new EventMessage("User removed in the event!");
		}
		if (list.contains(user) && message == messageAccess) {
			throw new ExpiredTimeException("Can not cancel your subscription!");
		}
		return new EventMessage("Can not cancel your subscription!");
	}
}
