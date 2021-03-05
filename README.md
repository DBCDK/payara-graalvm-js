Payara with graal.js javascript running in graalvm
--------------------------------------------------

**Requirements**

To build this project JDK 11 and Apache Maven is required.

**Build**
```bash
mvn package
```
**Run**
```bash
/path/to/graalvm/graalvm-ce-java11-21.0.0.2/bin/java -cp target/graalvm/graal-sdk-21.0.0.2.jar:/target/graalvm/icu4j-67.1.jar:target/graalvm/js-21.0.0.2.jar:target/graalvm/js-scriptengine-21.0.0.2.jar:target/graalvm/regex-21.0.0.2.jar:target/graalvm/truffle-api-21.0.0.2.jar -jar /path/to/payara-micro-5.2021.1.jar target/payara-graalvm-js-1.0-SNAPSHOT.war
```
**Verification**

```bash
curl -v http://localhost:8080/resources?engine=graal.js
# Actual output: engine for graal.js is: null
# Expected output: engine for graal.js is: com.oracle.truffle.js.scriptengine.GraalJSScriptEngine@...
```

```bash
curl -v http://localhost:8080/resources?engine=JavaScript
# Actual output: engine for JavaScript is: jdk.nashorn.api.scripting.NashornScriptEngine@...
# Expected output: engine for graal.js is: com.oracle.truffle.js.scriptengine.GraalJSScriptEngine@...
```

```bash
curl -v http://localhost:8080/resources?engine=nashorn
# Actual output: engine for nashorn is: jdk.nashorn.api.scripting.NashornScriptEngine@...
# Expected output: engine for nashorn is: jdk.nashorn.api.scripting.NashornScriptEngine@...
```

```bash
curl -v http://localhost:8080/resources/polyglot
# Actual output: Internal Server Error - No language and polyglot implementation was found on the classpath. Make sure the truffle-api.jar is on the classpath.
# Expected output: hello world from javascript
```