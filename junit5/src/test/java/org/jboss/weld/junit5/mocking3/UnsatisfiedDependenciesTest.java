package org.jboss.weld.junit5.mocking3;

import org.easymock.EasyMock;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * implemented similar to with custom WeldInitiator the documentation for @ExcludeBean
 * produces Unsatisfied dependencies for type TableController exception
 */
@ExtendWith(WeldJunit5Extension.class)
class UnsatisfiedDependenciesTest {

    /** fails with "Unsatisfied dependencies for type TableController" */
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(new Weld("unmanaged").addPackages(UnsatisfiedDependenciesTest.class))
            .addBeans(createCodeResourceBean()) // cannot be removed, otherwise fails because of missing beans.xml
            .build();

    Bean<?> createCodeResourceBean() {
        return MockBean.builder()
                .types(DefaultCodeResource.class)
                // Changed because if you use @ApplicationScoped, Weld will add a proxy, that's not wrong but interferes
                // with your test assumptions below as proxy injects a subclass of the mock
                // other than [not] having a proxy, app scoped and javax.inject.Singleton are the same
                .scope(Singleton.class) // jaxax.inject.Singleton, NOT the EJB one!
                .creating(EasyMock.createMock(DefaultCodeResource.class))
                .globallySelectedAlternative(1)
                .build();
    }

    @Inject
    private TableController tableController;

    @Inject
    DefaultCodeResource codeResource;

    @Test
    public void testInjectionMocked() {
        expect(codeResource.getHiddenTypeCodeNames()).andReturn(Collections.singletonList("MOCK!")).anyTimes();
        replay(codeResource);
        assertEquals("MOCK!", tableController.getTypeCode());
    }
}