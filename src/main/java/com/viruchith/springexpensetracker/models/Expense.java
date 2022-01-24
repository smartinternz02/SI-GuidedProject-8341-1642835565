package com.viruchith.springexpensetracker.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "expense_tracker_expenses")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank(message = "Title is required !")
	@Size(min = 3 , max = 60 , message = "Title must be between 3 and 8 characters !")
	private String title;
	
	@Size(max = 256, message = "Note must be less than 256 characters !")
	private String note;
	
	@DecimalMin(value = "0.0",message = "Amount cannot be less than 0.0 !")
	private double amount;
	
	@NotBlank(message = "Category is required !")
	@Pattern(regexp = "Clothing|Entertainment|Food|Fuel|Health|Salary|Misc", message = "Invalid category !")
	private String category;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public Expense() {
		super();
	}
	
	

	public Expense(
			@NotBlank(message = "Title is required !") @Size(min = 3, max = 60, message = "Title must be between 3 and 8 characters !") String title,
			@Size(max = 256, message = "Note must be less than 256 characters !") String note,
			@NotBlank(message = "Amount is required") @DecimalMin(value = "0.0", message = "Amount cannot be less than 0.0 !") double amount,
			@NotBlank(message = "Category is required !") @Pattern(regexp = "Clothing|Entertainment|Food|Fuel|Health|Salary|Misc", message = "Invalid category !") String category,
			Date createdAt) {
		super();
		this.title = title;
		this.note = note;
		this.amount = amount;
		this.category = category;
		this.createdAt = createdAt;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", title=" + title + ", note=" + note + ", amount=" + amount + ", category="
				+ category + ", createdAt=" + createdAt + "]";
	}
	
	
	
}