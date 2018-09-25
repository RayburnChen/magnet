package org.nfa.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Service;

@Service
public class TraceRepositoryImpl extends InMemoryHttpTraceRepository {

	@Autowired
	protected MongoOperations mongoOperations;

	@Override
	public void add(HttpTrace trace) {
		super.add(trace);
		MongoTrace mongoTrace = new MongoTrace();
		mongoTrace.setHttpTrace(trace);
		mongoOperations.insert(mongoTrace);
	}

	@Document(collection = "http_traces")
	public static class MongoTrace {

		@Id
		private String id;
		@Field
		private HttpTrace httpTrace;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public HttpTrace getHttpTrace() {
			return httpTrace;
		}

		public void setHttpTrace(HttpTrace httpTrace) {
			this.httpTrace = httpTrace;
		}

	}

}
