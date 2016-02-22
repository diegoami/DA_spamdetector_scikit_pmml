JPMML-Evaluator-Bootstrap
=========================

The simplest way to get started with a [JPMML-Evaluator] (https://github.com/jpmml/jpmml-evaluator) powered software project.

# Prerequisites #

* Java 1.7 or newer.
* [Apache Maven] (https://maven.apache.org/) 3.2 or newer.

# Installation and Usage #

Verify that all the requirements are met:
```
mvn -v
```

Check out the JPMML-Evaluator-Bootstrap project and enter its directory:
```
git clone https://github.com/jpmml/jpmml-evaluator-bootstrap.git
cd jpmml-evaluator-bootstrap
```

Initialize [Eclipse IDE] (https://eclipse.org/ide/) support files `.project` and `.classpath`:
```
mvn eclipse:eclipse
```

Launch the Eclipse IDE, and open the project import wizard via `File` > `Import...` > `General / Existing Projects into Workspace`. In the project wizard window, activate the radio button `Select root directory` and specify the location of the JPMML-Evaluator-Bootstrap directory. Click `Finish` to close the project wizard window.

The Eclipse IDE will show the imported JPMML-Evaluator-Bootstrap project in the package explorer view as `jpmml-evaluator-bootstrap`. The project consists of a single Java source code file `org/jpmml/evaluator/bootstrap/Main.java` (located under Apache Maven's standard source directory `src/main/java`).

Build the project:
```
mvn clean package
```

The build produces an executable uber-JAR file `bootstrap-executable-1.0-SNAPSHOT.jar` (located under Apache Maven's standard target directory `target`).

Display the list of transitive dependency JAR files that were included into this uber-JAR file:
```
mvn dependency:tree
```

Execute the example Java command-line application:
```
java -cp target/bootstrap-executable-1.0-SNAPSHOT.jar org.jpmml.evaluator.bootstrap.Main <path to PMML file>
```

# License #

JPMML-Evaluator-Bootstrap is dual-licensed under the [GNU Affero General Public License (AGPL) version 3.0] (http://www.gnu.org/licenses/agpl-3.0.html) and a commercial license.

# Additional information #

Please contact [info@openscoring.io] (mailto:info@openscoring.io)