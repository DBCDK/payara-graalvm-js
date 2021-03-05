package dk.dbc.incubator;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import javax.ejb.Stateless;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("resources")
public class Resources {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getEngine(@QueryParam("engine") String engineAlias) {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName(engineAlias);
        return "engine for " + engineAlias + " is: " + engine;
    }

    @GET
    @Path("polyglot")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPolyglot(@QueryParam("engine") String engineAlias) {
        try (Context context = Context.newBuilder().allowExperimentalOptions(true).build()) {
            final Value jsValue = context.eval("js", "'hello world from javascript'");
            return jsValue.asString();
        }
    }
}
