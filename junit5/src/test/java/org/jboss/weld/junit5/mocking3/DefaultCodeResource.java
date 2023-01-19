package org.jboss.weld.junit5.mocking3;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Resource
@Default
@ApplicationScoped
public class DefaultCodeResource {
    public List<String> getHiddenTypeCodeNames() {
        List<String> result = new ArrayList<>();
        result.add("notMocked");
        return result;
    }
}
