Using Apache Commons Javaflow with OSGi
=======================================


Run-time code instrumentation required by Javaflow can be employed in OSGi environment
by means of so-called Adaptor Hooks. Please see [1] for more information on them.


Enabling Javaflow instrumentation
---------------------------------

To instrument plugin classes with Javaflow, add the following header into the 
bundle manifest file (META-INF/MANIFEST.MF): “JavaFlow-Enabled: true”.


Running with Javaflow
---------------------

Please refer to [1]. In short, for *development* mode:

1) Open “Plug-ins” view, find org.eclipse.osgi and choose “Import As > Source Project”.

2) Make sure org.eclipse.osgi (from your workspace) and com.yoursway.osgi.javaflow
are enabled in the launch configuration.

3) Make extra sure org.eclipse.osgi and com.yoursway.osgi.javaflow are located
in the same file system directory (e.g. both in the workspace folder).

4) Add “-Dosgi.framework.extensions=com.yoursway.osgi.javaflow” to VM arguments of
your launch configuration (found on Arguments tab).


For *production* (compiled & jared plugins) mode:

1) Compile com.yoursway.osgi.javaflow into a jar, and make sure the jar includes
a version number (e.g. com.yoursway.osgi.javaflow_1.0.0.jar).

2) Put com.yoursway.osgi.javaflow_*.jar into eclipse/plugins folder (i.e. into
exactly the same folder where org.eclipse.osgi_*.jar is located).

3) Add “osgi.framework.extensions=com.yoursway.osgi.javaflow” to config.ini.


Failure to do any of these steps will cause Javaflow extension to be silently
ignored. In case you find yourself debugging it, two places of interest are:

- org.eclipse.equinox.launcher/org.eclipse.equinox.launcher.Main.readFrameworkExtensions
(Reads extensions list from “osgi.framework.extensions” property and sets up
the class path of the system bundle. The same property is read from numerous other
location, but those locations do not affect anything loaded from the extension.)

- org.eclipse.osgi/org.eclipse.osgi.baseadaptor.HookRegistry.mergeFileHookConfigurators
(Actually reads the list of hook configurators from hookconfigurators.properties.)


A few facts
-----------

1) To provide run-time instrumentation, you need adaptor hooks.

2) Adaptor hooks can only be registered in OSGi extension bundles [2]

3) OSGi extension bundles cannot require any other bundles

4) So you need to put Javaflow libraries into the extension bundle itself.

5) To make Javaflow available to other plugins, they must import the packages
rather than requiring the bundle (something you cannot do with fragments).

6) Unfortunately, Eclipse PDE has some kind of problem importing the packages
from com.yoursway.osgi.javaflow fragment — I could not get it to compile.

7) So you have to put another copy of Javaflow into a regular plugin, and
require it in a regular way. You end up with two copies of Javaflow, one of
them is only used for instrumentation so it's not a problem.

8) Equinox launcher reads eclipse.properties to determine the extension classpath
(in case you wonder why the file is needed).


References
----------

[1] http://wiki.eclipse.org/index.php/Adaptor_Hooks
[2] OSGi R4 Core Reference, Section 3.15 “Extension Bundles”
