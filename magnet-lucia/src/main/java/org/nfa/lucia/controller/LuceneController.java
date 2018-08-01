package org.nfa.lucia.controller;

import java.io.IOException;

import org.apache.lucene.search.TopDocs;
import org.nfa.lucia.service.LuceneIndexService;
import org.nfa.lucia.service.LuceneQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuceneController {

	@Autowired
	private LuceneIndexService luceneIndexService;
	@Autowired
	private LuceneQueryService luceneQueryService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void buildIndex() throws IOException {
		luceneIndexService.build();
	}

	@RequestMapping(value = "/index", method = RequestMethod.DELETE)
	public void deleteIndex() throws IOException {
		luceneIndexService.delete();
	}

	@RequestMapping(value = "/query/all", method = RequestMethod.GET)
	public TopDocs searchAll() throws IOException {
		return luceneQueryService.searchAll();
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public TopDocs search(@RequestParam String param) throws IOException {
		// David
		// fileContent:David
		// fileName:04
		return luceneQueryService.search(param);
	}

}
