import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
}

group = "com.personio.hierarchy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

sourceSets {
	create("integrationTest") {
		kotlin {
			compileClasspath += main.get().output + configurations.testRuntimeClasspath
			runtimeClasspath += output + compileClasspath
		}
	}
}

val integrationTest = task<Test>("integrationTest") {
	description = "Runs the integration tests"
	group = "verification"
	testClassesDirs = sourceSets["integrationTest"].output.classesDirs
	classpath = sourceSets["integrationTest"].runtimeClasspath
	mustRunAfter(tasks["test"])
}

tasks.check {
	dependsOn(integrationTest)
}
