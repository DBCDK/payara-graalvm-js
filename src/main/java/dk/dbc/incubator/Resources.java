package dk.dbc.incubator;

import org.graalvm.home.HomeFinder;
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
import java.net.URL;
import java.net.URLClassLoader;
import java.text.Collator;
import java.util.Collection;
import java.util.TreeSet;

@Stateless
@Path("resources")
public class Resources {
    final private String polyglotPrefix    = "\n/polygot     : ";
    final private String polyglotSysPrefix = "\n/polygot-sys : ";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getEngine(@QueryParam("engine") String engineAlias) {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName(engineAlias);
        return "engine for " + engineAlias + " is: " + engine;
    }

    @GET
    @Path("sys")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEngineSystemCL(@QueryParam("engine") String engineAlias) {

        final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());

            final ScriptEngine engine = new ScriptEngineManager().getEngineByName(engineAlias);
            return "engine for " + engineAlias + " is: " + engine + "\n";
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    @GET
    @Path("polyglot")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPolyglot(@QueryParam("engine") String engineAlias) {
        try {
            try (Context context = Context.newBuilder().allowExperimentalOptions(true).build()) {
                final Value jsValue = context.eval("js", "'hello world from javascript'");
                return polyglotPrefix +jsValue.asString() + "\n";
            }
        } catch( Throwable e ) {
            e.printStackTrace();
            return polyglotPrefix+"Unable to Load Polyglot Exception : " + e.getMessage() + "\n";
        }
    }

    @GET
    @Path("polyglot-sys")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPolyglotSystemCl(@QueryParam("engine") String engineAlias) {

        try {
            final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());

            try (Context context = Context.newBuilder().allowExperimentalOptions(true).build()) {
                final Value jsValue = context.eval("js", "'hello world from javascript'");
                return polyglotSysPrefix + jsValue.asString() + "\n";
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }

        } catch( Throwable e ) {
            return polyglotSysPrefix+"Unable to Load Polyglot Exception : " + e.getMessage() + "\n";
        }
    }

    @GET
    @Path("ja")
    @Produces(MediaType.TEXT_PLAIN)
    public String dd () {
        org.graalvm.home.HomeFinder hf= HomeFinder.getInstance();
        return hf.getVersion() + hf.getToolHomes();
    }

    @GET
    @Path("this")
    @Produces(MediaType.TEXT_PLAIN)
    public String dumpThisCl( ) {
        return dumpClassLoader( this.getClass().getClassLoader() );
    }

    @GET
    @Path("this-sys")
    @Produces(MediaType.TEXT_PLAIN)
    public String dumpSysCl () {
        return dumpClassLoader( ClassLoader.getSystemClassLoader());
    }

    public String dumpClassLoader( ClassLoader cl ) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append(cl.toString()).append('\n');

        try {
            URL[] urls = ((URLClassLoader) cl).getURLs();
            Collection<String> sort_urls = new TreeSet<String>(Collator.getInstance());
            for (URL url : urls) {
                sort_urls.add(url.getFile());
            }


            sb.append("size : ").append(sort_urls.size()).append('\n');
            for (String s : sort_urls) {
                sb.append(s).append('\n');
            }
        } catch( Exception e) {
            sb.append("Only able to list URLClassLoader path\n");
        }
        return sb.toString();
    }




}
