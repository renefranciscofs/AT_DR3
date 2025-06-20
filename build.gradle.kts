plugins {
  java
  application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.javalin:javalin:5.6.1")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
  testImplementation("com.konghq:unirest-java:3.14.5")
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
  mainClass.set("org.example.Main")
}

tasks.test {
  useJUnitPlatform()
}
