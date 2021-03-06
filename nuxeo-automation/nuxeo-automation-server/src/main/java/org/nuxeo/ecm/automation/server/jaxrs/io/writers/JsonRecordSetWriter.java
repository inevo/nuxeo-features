/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nuxeo
 */

package org.nuxeo.ecm.automation.server.jaxrs.io.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerator;
import org.nuxeo.ecm.automation.core.util.PaginableRecordSet;
import org.nuxeo.ecm.automation.core.util.RecordSet;
import org.nuxeo.ecm.automation.server.jaxrs.io.JsonWriter;

/**
 * Manage JSON Marshalling for {@link RecordSet} objects
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since 5.7
 */
@Provider
@Produces({ "application/json+nxentity", "application/json" })
public class JsonRecordSetWriter implements MessageBodyWriter<RecordSet> {

    protected static Log log = LogFactory.getLog(JsonRecordSetWriter.class);

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        boolean canUse = RecordSet.class.isAssignableFrom(type);
        return canUse;
    }

    @Override
    public long getSize(RecordSet t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1L;
    }

    @Override
    public void writeTo(RecordSet records, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream out)
            throws IOException, WebApplicationException {
        try {
            writeRecords(out, records);
        } catch (Exception e) {
            log.error("Failed to serialize recordset", e);
            throw new WebApplicationException(500);
        }

    }

    protected void writeRecords(OutputStream out, RecordSet records)
            throws Exception {

        JsonGenerator jg = JsonWriter.createGenerator(out);

        jg.writeStartObject();
        jg.writeStringField("entity-type", "recordset");

        if (records instanceof PaginableRecordSet) {
            PaginableRecordSet pRecord = (PaginableRecordSet) records;
            jg.writeBooleanField("isPaginable", true);
            jg.writeNumberField("pageIndex", pRecord.getCurrentPageIndex());
            jg.writeNumberField("pageSize", pRecord.getPageSize());
            jg.writeNumberField("pageCount", pRecord.getNumberOfPages());
        }

        jg.writeArrayFieldStart("entries");
        for (Map<String, Serializable> entry : records) {
            jg.writeObject(entry);
        }
        jg.writeEndArray();
        jg.writeEndObject();
        jg.flush();
    }

}
