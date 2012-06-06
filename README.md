_**myconfig**_ is an application that is use to access and to manage configurations for applications.

Applications can access their configuration for a given version and environment. Granted users can manage the configuration values.

It remains up to the application about the way they _use_ the configuration (build time, packaging time, deployment time, runtime...), _myconfig_ just serves the correct configuration at the correct moment.

_myconfig_ provides a REST API to access the configuration:
* _/get/key/**key**/**app**/**version**/**env**_ for a single key
* _/get/env/**app**/**version**/**env**/(json|xml|properties)_ for the full configuration

Client libraries are available for an easy access: Maven plug-in, ANT task, Java API, etc. They are all built on the same REST API.


