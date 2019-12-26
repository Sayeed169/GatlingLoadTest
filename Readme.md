##Running Gatling project from command line

The project needs to be able to run from terminal with some build tool, which in this case is Maven. The Gatling Maven plugin is a suitable tool for this. To import it, include the following snippet in your pom.xml:

<plugins>
  <plugin>
    <groupId>io.gatling</groupId>
    <artifactId>gatling-maven-plugin</artifactId>
    <version>MANUALLY_REPLACE_WITH_LATEST_VERSION</version>
    <configuration>
      <simulationClass>MANUALLY_REPLACE_WITH_YOUR_SIMULATION_CLASS</simulationClass>
    </configuration>
  </plugin>
</plugins>

Note 1: The versions of the gatling-charts-highcharts dependency and the gatling-maven-plugin plugin do not have to match.

Note 2: Replace the value in the simulationClass tag with your simulation class as it is in the package statement (e.g. com.example.my.SimulationClass).

After doing this, you should be able to run the project from the command line with the following commands:

``mvn clean package``
``mvn gatling:test``

Alternatively, you can use the Scala Maven plugin. In this case you need to run your main class (where you build your Gatling Properties).
Creating a Jenkins pipeline for your Gatling-Maven project

I assume you have a Jenkins environment set up with the Jenkins Maven plugin installed. See this page to learn more about the plugin part.
Pipeline script

There are three ways to define what a Jenkins pipeline does:

    Through Blue Ocean UI
    Through classic Jenkins UI
    By creating a Jenkinsfile in your project

I think the 3rd option is the most practical one because this way the pipeline script is committed to source control. This makes it more transparent and easier to maintain.
See this page to learn more about the other options.

So create a file in your project root directory with the name Jenkinsfile and no extension. Paste this script into it:

pipeline {
    agent any
    stages {
        stage("Maven build") {
            steps {
                sh 'mvn -B clean package'
            }
        }
        stage("Gatling run") {
            steps {
                sh 'mvn gatling:test'
            }
            post {
                always {
                    gatlingArchive()
                }
            }
        }
    }
}

Commit this file and push the commit into a remote repository like Github or Gitlab. This is how Jenkins is going to be able to read the Jenkinsfile.
Creating pipeline

To create a pipeline job in Jenkins, go to your Jenkins dashboard and click on "New Item" on the top of the left menu bar. Name the job and choose Pipeline as a type. Click OK.

On the pipeline config page, scroll down to the Pipeline header and select "Pipeline script from SCM" in the Definition drop-down. A dropdown appears to choose a version control system. Choose Git. Paste your repository URL into the related field (the one you use to clone your repo). You can use either HTTP or SSH protocol but pay attention to set the credentials accordingly. A red error message will show up if Jenkins cannot access the repository. Optionally, you can set the branch that you want to build (make sure you have the Jenkinsfile on that branch).

Once all set, click Save.
Gatling Jenkins plugin

At this point, Jenkins should be able to build and run the project, but to see the reports, the Gatling Jenkins plugin needs to be installed (this is what executes the gatlingArchive() line in the pipeline script. It also provides ways to compare results from the last 15 builds, which is very useful.

To install this plugin, go back to your Jenkins dashboard and click on "Manage Jenkins" on the left menu bar. Search for Gatling and install the plugin. Restart Jenkins when asked about it.
Using the Jenkins pipeline job

Go to the dashboard page of your new pipeline. (The left menu should contain a "Gatling" option by now.) Click on "Build". When the build is ready, click on "Gatling" on the left and scroll down to the bottom of the page. You can open the Gatling report by clicking on the link. As more and more builds are run and added to your history, all the reports will be available here and the graphs are going to contain data from the last 15 builds.

Links:
- https://stackoverflow.com/questions/38695062/how-to-integrate-gatling-with-jenkins
- http://gatling.io/docs/2.2.2/extensions/maven_plugin.html
- https://github.com/gatling/gatling-maven-plugin-demo
