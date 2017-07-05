package com.luxoft.rest.hateoas.domain.entities;

import java.net.URI;

/**
 * @author Anton German
 * @since 09 September 2016
 */
public class ActionLink {
    private String name;
    private URI link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getLink() {
        return link;
    }

    public void setLink(URI link) {
        this.link = link;
    }

    public static ActionLink create(String name, URI link) {
        ActionLink result = new ActionLink();
        result.setName(name);
        result.setLink(link);
        return result;
    }
}
