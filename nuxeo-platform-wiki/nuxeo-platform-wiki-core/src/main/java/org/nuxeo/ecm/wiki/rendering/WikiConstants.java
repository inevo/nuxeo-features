package org.nuxeo.ecm.wiki.rendering;

import java.util.regex.Pattern;

import org.nuxeo.ecm.platform.relations.api.Resource;
import org.nuxeo.ecm.platform.relations.api.impl.ResourceImpl;

public class WikiConstants {

    public static final Resource HAS_LINK_TO = new ResourceImpl("http://www.nuxeo.org/wiki/hasLinkTo");

    // TODO get this from config files
    public static final String PATTERN = "(\\.)?([A-Z]+[a-z]+[A-Z][A-Za-z]*\\.)*([A-Z]+[a-z]+[A-Z][A-Za-z]*)";
    public static final Pattern PAGE_LINK_PATTERN = Pattern.compile(PATTERN);
}
