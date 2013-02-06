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
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package org.nuxeo.ecm.wiki.listener;

import static org.nuxeo.ecm.wiki.rendering.WikiConstants.PAGE_LINK_PATTERN;
import static org.nuxeo.ecm.wiki.rendering.WikiConstants.HAS_LINK_TO;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyNotFoundException;
import org.nuxeo.ecm.platform.relations.api.Graph;
import org.nuxeo.ecm.platform.relations.api.QNameResource;
import org.nuxeo.ecm.platform.relations.api.RelationManager;
import org.nuxeo.ecm.platform.relations.api.Statement;
import org.nuxeo.ecm.platform.relations.api.impl.LiteralImpl;
import org.nuxeo.ecm.platform.relations.api.impl.StatementImpl;
import org.nuxeo.ecm.platform.relations.api.util.RelationConstants;
import org.nuxeo.ecm.platform.relations.api.util.RelationHelper;
import org.nuxeo.ecm.wiki.WikiTypes;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.common.CommonWikiParser;

public class WikiHelper {

    // Utility class.
    private WikiHelper() {
    }

    public static List<String> getWordLinks(DocumentModel doc) {
        try {
            String content = (String) doc.getPropertyValue(WikiTypes.PROP_WIKI_CONTENT);
            StringBuffer collector = new StringBuffer();
            WordExtractor extractor = new WordExtractor(collector);
            CommonWikiParser parser = new CommonWikiParser();
            try {
                parser.parse(new StringReader(content), extractor);
            } catch (WikiParserException e) {
                e.printStackTrace();
            }
            return getWordLinks(collector.toString());
        } catch (PropertyNotFoundException e) {
            // ignore
        } catch (ClientException e) {
            // ignore
        }
        return null;
    }

    // this will update links graph
    // TODO optimize this!
    // keep old statements
    public static void updateRelations(DocumentModel doc) {
        List<String> list = getWordLinks(doc);
        List<Statement> stmts = RelationHelper.getStatements(doc, HAS_LINK_TO);
        try {
            // remove old links
            RelationManager rm = RelationHelper.getRelationManager();
            Graph graph = rm.getGraphByName(RelationConstants.GRAPH_NAME);
            if (stmts != null) {
                graph.remove(stmts);
                stmts.clear();
            } else {
                stmts = new ArrayList<Statement>();
            }

            // add new links
            if (list != null) {
                QNameResource docResource = RelationHelper.getDocumentResource(doc);
                for (String word : list) {
                    if (!word.startsWith(".")) {
                        word = getAbsolutePageLink(doc, word);
                    }
                    Statement stmt = new StatementImpl(
                            docResource, HAS_LINK_TO, new LiteralImpl(word));
                    stmts.add(stmt);
                }
                graph.add(stmts);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getWordLinks(String text) {
        List<String> wordLinks = new ArrayList<String>();
        Matcher matcher = PAGE_LINK_PATTERN.matcher(text);
        while (matcher.find()) {
            String s = matcher.group(0);
            if (!wordLinks.contains(s)) {
                wordLinks.add(s);
            }
        }
        return wordLinks;
    }

    public static String getAbsolutePageLink(DocumentModel doc, String relativeLink) {
        if (relativeLink.startsWith(".")) {
            return relativeLink;
        }
        Path path = doc.getPath().removeFirstSegments(3);
        path = path.removeLastSegments(1);
        return "." + path.toString().replace("/", ".") + "." + relativeLink;
    }

}
