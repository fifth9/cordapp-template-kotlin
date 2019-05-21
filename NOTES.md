# Corda Dev Notes

## Running the code
To run the code you will need to do the following

In the root directory
```text
$ ./gradlew clean deployNodes
```
The above command will build the node code.

Then run the nodes like this:
```text
$ build/nodes/runnodes
```

Then we need to start the webserver that will serve the frontend and the REST API.
```text
$ ./gradlew runTemplateServer
```

There is also a client commandline example that can be run as follows:
```text
$ ./gradlew runTemplateClient
```

## Add the Token SDK dependencies
See [github reference](https://github.com/corda/cordapp-template-kotlin/blob/token-template/build.gradle)
and [token sdk](https://github.com/corda/token-sdk)

These token libraries allow you to make use of the token support. 

## Testing
For testing and running individual tests you will need to install Quasar

```text
$ ./gradlew installQuasar
```


Before creating the IntelliJ run configurations for these unit tests go 
to Run -> Edit Configurations -> Defaults -> JUnit, add `-javaagent:lib/quasar.jar` to 
the VM options, and set Working directory to `$PROJECT_DIR$` so that the Quasar 
instrumentation is correctly configured.


## Next steps
Now I need to embelish the REST API and look at adding a React frontend.

