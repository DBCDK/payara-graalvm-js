package dk.dbc.incubator;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class defines the other classes that make up this JAX-RS application by
 * having the getClasses method return a specific set of resources.
 */
@ApplicationPath("/")
public class JsApplication extends Application {

    private static final Set<Class<?>> CLASSES = new HashSet<>(Collections.singletonList(
            Resources.class
    ));

    @Override
    public Set<Class<?>> getClasses() {
        return CLASSES;
    }
}
