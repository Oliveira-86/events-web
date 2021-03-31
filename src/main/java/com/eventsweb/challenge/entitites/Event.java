package com.eventsweb.challenge.entitites;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Event {

	private Long id;
	private String name;
	private Integer vacancies;
	private Instant startDate;
	private Instant finishDate;
	
	private List<User> users = new ArrayList<>();
	
	public Event() {
	}

	public Event(Long id, String name, Integer vacancies, Instant startDate, Instant finishDate, List<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.vacancies = vacancies;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVacancies() {
		return vacancies;
	}

	public void setVacancies(Integer vacancies) {
		this.vacancies = vacancies;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Instant finishDate) {
		this.finishDate = finishDate;
	}

	public List<User> getUsers() {
		return users;
	}
	
	
}
