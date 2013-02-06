package wiki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nuxeo.ecm.wiki.rendering.WikiConstants.HAS_LINK_TO;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.relations.api.util.RelationHelper;
import org.nuxeo.ecm.webengine.model.WebContext;

public class WikiHelper {
    public static List<Map<String, String>> getLinks(WebContext ctx) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        DocumentModel doc = null;
        org.nuxeo.ecm.webengine.model.Resource resource = ctx.getTargetObject();
        if (resource instanceof DocumentObject) {
            DocumentObject docObj = (DocumentObject) resource;
            doc = docObj.getDocument();
        }
        if (doc == null) {
            return list;
        }

        DocumentModelList l = RelationHelper.getSubjectDocuments(
                HAS_LINK_TO, getPageAbsoluteLink(doc), doc.getSessionId());

        String prefix = ctx.getModulePath();

        for (DocumentModel d : l) {
            Map<String, String> map = new HashMap<String, String>();
            try {
                map.put("title", d.getTitle());
                map.put("href", prefix + "/" + d.getPath().removeFirstSegments(3).toString());
                list.add(map);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String getPageAbsoluteLink(DocumentModel doc) {
        String s = doc.getPath().removeFirstSegments(3).toString();
        return "." + s.replace("/", ".");
    }
}
