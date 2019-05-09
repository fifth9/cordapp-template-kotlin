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

## Next steps
Now I need to embelish the REST API and look at adding a React frontend.