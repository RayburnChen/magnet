package org.nfa.base.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "configurations")
public class ConfigurationEntity<T> extends BaseEntity {

	private static final long serialVersionUID = -7066207437109854176L;

	@Field
	@Indexed
	private ConfigurationName name;
	@Field
	private T content;

	public ConfigurationName getName() {
		return name;
	}

	public void setName(ConfigurationName name) {
		this.name = name;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

}
