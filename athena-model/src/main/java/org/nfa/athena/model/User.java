package org.nfa.athena.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@CompoundIndexes({
		@CompoundIndex(background = true, name = "user_name_amount_idx", def = "{ 'name':1, 'amount':-1 }") })
public class User implements Serializable, Measurable<User> {

	private static final long serialVersionUID = 2157443368101050157L;

	public User() {
		super();
	}

	public User(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public User(BigDecimal amount, String tag) {
		super();
		this.amount = amount;
		this.tag = tag;
	}

	@Id
	private String id;

	@Field
	@NotNull(message = "name is missing")
	private String name;

	@Field
	private int age;

	@Field
	// @JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal amount;

	@Field
	private String password;

	@Field
	private List<String> values;

	@Field
	private UserType userType;

	@Field
	private String tag;

	@CreatedDate
	private Date createdDate;

	private LocalDateTime localDateTime = LocalDateTime.now();

	private ZonedDateTime zonedDateTime = ZonedDateTime.now();

	@Version
	private Long version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}

	public void setZonedDateTime(ZonedDateTime zonedDateTime) {
		this.zonedDateTime = zonedDateTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", amount=" + amount + ", password=" + password
				+ ", values=" + values + ", userType=" + userType + ", createdDate=" + createdDate + ", version="
				+ version + "]";
	}

	@Override
	public double distance(User target) {
		return this.amount.subtract(target.amount).abs().doubleValue();
	}

}
