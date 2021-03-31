package com.eventsweb.challenge.tests.service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eventsweb.challenge.entitites.Event;
import com.eventsweb.challenge.entitites.User;
import com.eventsweb.challenge.repository.EventRepository;
import com.eventsweb.challenge.repository.UserRepository;
import com.eventsweb.challenge.services.EventService;
import com.eventsweb.challenge.services.exceptions.ExpiredTimeException;
import com.eventsweb.challenge.services.exceptions.VacanciesUnavailableException;
import com.eventsweb.challenge.services.utils.EventMessage;

@ExtendWith(SpringExtension.class)
public class EventServiceTests {

	@InjectMocks
	private EventService service;
	
	@Mock
	private EventRepository eventRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private EventMessage message;
	
	@Test
	public void registerShouldEnrollmentUserAnyEventTheyWant() {
		
		
		User user1 = new User(1L, "Miguel");
		User user2 = new User(2L, "Miguel");
		User user3 = new User(3L, "Miguel");
		List<User> list = new ArrayList<>();
		list.add(user2);
		list.add(user3);
		
		Event event = new Event(1L, "Java/React", 10, Instant.parse("2021-10-19T05:00:00Z"), 
				Instant.parse("2021-10-21T15:00:00Z"), list);
		Long eventId = event.getId();
		
		Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
		
		Event result = service.register(eventId, user1);
		
		org.assertj.core.api.Assertions.assertThat(result.getUsers()).contains(user1);
	}
	
	@Test
	public void registerShouldThrowExceptionIfVacanciesIsEqualsUsersList() {
		
		Assertions.assertThrows(VacanciesUnavailableException.class, () -> {
			
			User user1 = new User(1L, "Miguel");
			User user2 = new User(2L, "Miguel");
			User user3 = new User(3L, "Miguel");
			List<User> list = new ArrayList<>();
			list.add(user2);
			list.add(user3);
			Event event = new Event(1L, "Java/React", 2, Instant.parse("2021-10-19T05:00:00Z"), 
					Instant.parse("2021-10-21T15:00:00Z"), list);
			Long eventId = event.getId();
			
			Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
			
			service.register(eventId, user1);
			
		});
	
	}
	
	
	@Test
	public void registerShouldThrowExpiredTimeExceptionIfEnrollmentDateIsLessThanAnHourDateStart() {
		
			Assertions.assertThrows(ExpiredTimeException.class, () -> {
			
			User user1 = new User(1L, "Miguel");
			Event event = new Event(1L, "Java/React", 10, Instant.now().plusSeconds(3599), 
					Instant.now().plusSeconds(7200), new ArrayList<>());
			Long eventId = event.getId();
			
			Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
			
			service.register(eventId, user1);
		});		
	}
	
	@Test
	public void userShouldEntryEventWhenDateIsBetweenAnHoursBeforeStartAndAnHourBeforeEnd() {
		
		User user1 = new User(1L, "Miguel");
		List<User> list = new ArrayList<>();
		list.add(user1);
		Event event = new Event(1L, "Java/React", 10, Instant.now().plusSeconds(-3599), 
				Instant.now().plusSeconds(3601), list);
		
		Mockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
		Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
		
		EventMessage result = service.entryEvent(event.getId(), user1.getId());
		
		Assertions.assertEquals("The client has access to the event!!", result.getMsg());
	}
	
	@Test
	public void userShouldNotHaveAccessEventWhenHisNotRegister() {
		
		User user1 = new User(1L, "Miguel");
		List<User> list = new ArrayList<>();
		Event event = new Event(1L, "Java/React", 10, Instant.now().plusSeconds(-3599), 
				Instant.now().plusSeconds(3601), list);
		
		Mockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
		Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
		
		EventMessage result = service.entryEvent(event.getId(), user1.getId());
		
		Assertions.assertEquals("The client doest not have access to the event!!", result.getMsg());
	}
	
	
	@Test
	public void userShouldNotHaveAccessEventWhenTimeIsNotValid() {
		
		User user1 = new User(1L, "Miguel");
		List<User> list = new ArrayList<>();
		list.add(user1);
		Event event = new Event(1L, "Java/React", 10, Instant.now().plusSeconds(3601), 
				Instant.now().plusSeconds(-3599), list);
		
		Mockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
		Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
		
		EventMessage result = service.entryEvent(event.getId(), user1.getId());
		
		Assertions.assertEquals("The client doest not have access to the event!!", result.getMsg());
	}

	@Test
	public void userShouldCancelEnrollment() {
		
		User user1 = new User(1L, "Miguel");
		List<User> list = new ArrayList<>();
		list.add(user1);
		Event event = new Event(1L, "Java/React", 10, Instant.parse("2021-10-19T05:00:00Z"), 
				Instant.parse("2021-10-21T15:00:00Z"), list);
		
		Mockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
		Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
		
		EventMessage result = service.deregister(event.getId(), user1.getId());
		
		Assertions.assertEquals("User removed in the event!", result.getMsg());
		
	}
}
	
	

