package org.nfa.lucia.controller;

import org.apache.lucene.search.TopDocs;
import org.nfa.lucia.service.LuceneIndexService;
import org.nfa.lucia.service.LuceneQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuceneController {

	@Autowired
	private LuceneIndexService luceneIndexService;
	@Autowired
	private LuceneQueryService luceneQueryService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void buildIndex() {
		luceneIndexService.build();
	}

	@RequestMapping(value = "/index", method = RequestMethod.DELETE)
	public void deleteIndex() {
		luceneIndexService.delete();
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public TopDocs searchAll() {
		return luceneQueryService.searchAll();
	}

}
