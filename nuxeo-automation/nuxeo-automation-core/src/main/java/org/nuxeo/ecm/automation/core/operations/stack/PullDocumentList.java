/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.ecm.automation.core.operations.stack;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.DocumentRefList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;

/**
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@Operation(id=PullDocumentList.ID, category=Constants.CAT_EXECUTION_STACK, label="Pull Document List",
    description="Restore the first saved input document list in the context input stack")
public class PullDocumentList {

    public static final String ID = "Document.PullList";

    protected @Context OperationContext ctx;

    @OperationMethod
    public DocumentModelList run() throws Exception {
        Object obj = ctx.pull(Constants.O_DOCUMENTS);
        if (obj instanceof DocumentModelList) {
            return (DocumentModelList)obj;
        } else if (obj instanceof DocumentRefList) {
            CoreSession session = ctx.getCoreSession();
            DocumentRefList refs = (DocumentRefList)obj;
            DocumentModelListImpl list = new DocumentModelListImpl((int)refs.totalSize());
            for (DocumentRef ref : refs) {
                list.add(session.getDocument(ref));
            }
        }
        throw new OperationException("Illegal state error for pull document operation. The context stack doesn't contains a document list on its bottom");
    }

}