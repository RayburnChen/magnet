package org.nfa.atropos.model;

import org.nfa.base.model.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "articles")
public class Article extends BaseEntity {

	private static final long serialVersionUID = -6488924319840520191L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
