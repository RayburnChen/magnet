package org.nfa.lucia.controller;

import org.nfa.lucia.service.LuceneIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuceneController {

	@Autowired
	private LuceneIndexService luceneIndexService;

	@RequestMapping(value = "/index/build", method = RequestMethod.GET)
	public void buildIndex() {
		luceneIndexService.build();
	}

}
