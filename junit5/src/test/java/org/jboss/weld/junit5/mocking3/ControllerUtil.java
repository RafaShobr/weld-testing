package org.jboss.weld.junit5.mocking3;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ControllerUtil {

    @Inject
    private DefaultCodeResource codeResource;
    public String getTypes() {
        return codeResource.getHiddenTypeCodeNames().get(0);
    }
}
