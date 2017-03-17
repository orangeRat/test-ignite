# Test Apache Ignite app

#### Build
`mvn clean package`

#### Start App
The app uses Ignite server. Please, download Apache Ignite
and unzip it on your local machine. After that run it with command
`bin/ignite.sh examples/config/example-ignite.xml` and start the app with
command `java -jar test-ignite-0.0.1.jar`

#### Prefill cache
In `initialData` folder stored some random data.
You can just run the `load-initial.sh` script and test the app

#### API
* `GET /cell/{id}` - get cell info
* `GET /cell/{cellId}/numbers` - list of numbers for cell
* `POST /cell/add-number` - put one number+cell tuple
* `POST /cell/add-number-batch` - put batch of number+cell tuples

* `GET /profile/{cnt}` - get client's profile from cache
* `GET /profile/by-cell/{cellId}` - get profile list for target cell
* `POST /profile` - put one client's profile to cache
* `POST /profile/batch `- put batch of profiles to cache

#### Docker
[Docker image](https://hub.docker.com/r/mrdru/bl-ignite/)

For start type `sudo docker run -it bl-ignite run.sh`
