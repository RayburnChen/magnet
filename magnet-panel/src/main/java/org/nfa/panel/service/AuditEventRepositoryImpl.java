package org.nfa.panel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Service;

@Service
public class AuditEventRepositoryImpl extends InMemoryAuditEventRepository {

	@Autowired
	protected MongoOperations mongoOperations;

	@Override
	public void add(AuditEvent event) {
		super.add(event);
		MongoAuditEvent mongoAuditEvent = new MongoAuditEvent();
		mongoAuditEvent.setEvent(event);
		mongoOperations.insert(mongoAuditEvent);
	}

	@Document(collection = "audit_events")
	public static class MongoAuditEvent {

		@Id
		private String id;
		@Field
		private AuditEvent event;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public AuditEvent getEvent() {
			return event;
		}

		public void setEvent(AuditEvent event) {
			this.event = event;
		}

	}

}
