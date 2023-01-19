package org.jboss.weld.junit5.mocking3;

import org.easymock.EasyMock;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jboss.weld.junit5.auto.ExcludeBean;
import org.jboss.weld.junit5.auto.WeldJunit5AutoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import java.util.Collections;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * implemented similar to with custom WeldInitiator the documentation for @ExcludeBean
 * produces Unsatisfied dependencies for type TableController exception
 */
@ExtendWith(WeldJunit5AutoExtension.class)
class UnsatisfiedDependenciesTest {

    /** fails with "Unsatisfied dependencies for type TableController" */
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(new Weld("unmanaged").enableDiscovery().disableIsolation())
            .addBeans(createCodeResourceBean()) // cannot be removed, otherwise fails because of missing beans.xml
            .build();
    @Produces
    @ExcludeBean
    private DefaultCodeResource codeResource;

    Bean<?> createCodeResourceBean() {
        codeResource = EasyMock.createMock(DefaultCodeResource.class);
        return MockBean.builder()
                .types(DefaultCodeResource.class)
                .scope(ApplicationScoped.class)
                .selectedAlternative(DefaultCodeResource.class)
                .creating(codeResource)
                .globallySelectedAlternative(1)
                .priority(1)
                .build();
    }

    @Inject
    private TableController tableController;

    @Test
    public void testInjectionMocked() {
        expect(codeResource.getHiddenTypeCodeNames()).andReturn(Collections.singletonList("MOCK!")).anyTimes();
        replay(codeResource);
        assertEquals("MOCK!", tableController.getTypeCode());
    }
}