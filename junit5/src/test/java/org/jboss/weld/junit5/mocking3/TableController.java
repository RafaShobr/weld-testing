package org.jboss.weld.junit5.mocking3;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TableController {

    @Inject
    private InternalController internalController;

    public String getTypeCode() {
        return internalController.getInternalTypes();
    }
}
