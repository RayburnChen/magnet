package org.nfa.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Holder<T> extends PageImpl<T> {

	private static final long serialVersionUID = 8958474351635914957L;

	public Holder() {
		super(new ArrayList<>());
	}

	public Holder(List<T> content) {
		super(content);
	}

	public Holder(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public Holder(Page<T> page) {
		super(page.getContent(), new PageRequest(page.getNumber(), page.getSize(), page.getSort()), page.getTotalElements());
	}

}
