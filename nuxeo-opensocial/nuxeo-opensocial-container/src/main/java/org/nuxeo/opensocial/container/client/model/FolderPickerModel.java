package org.nuxeo.opensocial.container.client.model;

import java.util.ArrayList;
import java.util.List;

import org.restlet.gwt.Callback;
import org.restlet.gwt.Client;
import org.restlet.gwt.data.Method;
import org.restlet.gwt.data.Protocol;
import org.restlet.gwt.data.Request;

/**
 * @author Stéphane Fourrier
 */
public class FolderPickerModel {
    private List<Folder> folders;

    private Client client;

    private String gadgetId;

    private final String NXPICS_PATH = "nxpicsfile/default/";

    private final String MEDIUM_VIEW = "/Medium:content/";

    private String initSelectedFolder;

    public FolderPickerModel(String gadgetId, String initSelectedFolder) {
        folders = new ArrayList<Folder>();
        client = new Client(Protocol.HTTP);
        this.gadgetId = gadgetId;
        this.initSelectedFolder = initSelectedFolder;
    }

    public void getFolderListRequest(Callback callback) {
        String uri = new String(getBaseUrl() + "site/browser/" + gadgetId
                + "/gadgetChildren?type=PictureBook");
        Request request = new Request(Method.GET, uri);
        client.handle(request, callback);
    }

    public String getFolderPreview(String pictureId) {
        StringBuilder sb = new StringBuilder("");
        if (pictureId != null && !("").equals(pictureId)) {
            sb.append(getBaseUrl());
            sb.append(NXPICS_PATH);
            sb.append(pictureId);
            sb.append(MEDIUM_VIEW);
        }
        return sb.toString();
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public Folder getFolder(String id) {
        for (Folder f : folders) {
            if (id.equals(f.getId())) {
                return f;
            }
        }
        return null;
    }

    private native String getBaseUrl() /*-{
                                       return $wnd.nuxeo.baseURL;
                                       }-*/;

    public String getInitialSelectedFolder() {
        return initSelectedFolder;
    }
}