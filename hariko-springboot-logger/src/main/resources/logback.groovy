import ch.qos.logback.classic.AsyncAppender
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import com.frame.hariko.springboot.logger.filter.DynamicThresholdLoggerFilter

def DEFAULT_META_PATH = "META-INF/app.properties"
def PROJECT_ARTIFACT_ID = "app.id"
def SYSTEM_OS_NAME = "os.name"

//define const system var . -DlogPath=d:/logs/
def ENV_LOG_PATH = "logPath"
def ENV_LOG_LAYOUT = "logLayout"
def ENV_LOG_LEVEL = "logLevel"

def DEFAULT_ROOT_LOGGER_LEVEL = DEBUG
//scan("30 seconds")
// load external file source properties

def sysProperties = System.properties
def classLoader = getClass().getClassLoader()

def logBase = ""
if(sysProperties.containsKey(ENV_LOG_PATH)){
	logBase = String.valueOf(sysProperties.get(ENV_LOG_PATH))
}else{
	def osName = String.valueOf(sysProperties.get(SYSTEM_OS_NAME)).toLowerCase()
	if (osName.contains("windows")){
		logBase = "d:/temp/applogs/"
	} else {
		logBase = "/temp/applogs/"
	}
}

def patternTemplate
if(sysProperties.containsKey(ENV_LOG_LAYOUT)){
	patternTemplate = String.valueOf(sysProperties.get(ENV_LOG_LAYOUT))
}else{
	patternTemplate = "[%date{ISO8601}] [%level] %logger{80} %thread [%X{traceId}] dashuf [%contextName] - %msg%n"
}

if(sysProperties.containsKey(ENV_LOG_LEVEL)){
	DEFAULT_ROOT_LOGGER_LEVEL = Level.toLevel(String.valueOf(sysProperties.get(ENV_LOG_LEVEL)))
}

def sourceUri = classLoader.getResource(DEFAULT_META_PATH)
def appProperties = new Properties()
appProperties.load(sourceUri.openStream())
println("@@app.properties:" + appProperties)
context.name = appProperties[PROJECT_ARTIFACT_ID]
logBase = "${logBase}/${appProperties[PROJECT_ARTIFACT_ID]}".replaceAll("//", "/")
def dir = new File(logBase)
if(!dir.exists()){
	dir.mkdirs()
}

turboFilter(DynamicThresholdLoggerFilter)

appender("STDOUT", ConsoleAppender) {
	encoder(PatternLayoutEncoder) { pattern = "${patternTemplate}" }
}
appender("FILE", RollingFileAppender) {
	file = "${logBase}/spring.log"
	rollingPolicy(TimeBasedRollingPolicy) {
		timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) { maxFileSize = "20MB" }
		maxHistory = 30
		fileNamePattern = "${logBase}/spring.%d{yyyy-MM-dd}.log.%i"
	}
	encoder(PatternLayoutEncoder) { pattern = "${patternTemplate}" }
}
appender("ASYNC", AsyncAppender) {
	discardingThreshold = 20
	queueSize = 2048
	includeCallerData = true
	neverBlock = true
	maxFlushTime = 10000
	appenderRef("FILE")
}
root(DEFAULT_ROOT_LOGGER_LEVEL, ["ASYNC", "STDOUT"])
logger("org.springframework", WARN, [], true)
logger("org.spring", WARN, [], true)
logger("org.hibernate", WARN, [], true)
logger("io.grpc.netty", OFF, [], true)
logger("org.eclipse.jetty", WARN, [], true)
logger("jndi", WARN, [], true)
logger("redis.clients", WARN, [], true)
logger("application", WARN, [], true)
logger("springfox.documentation", WARN, [], true)
logger("com.netflix", WARN, [], true)
logger("com.dashuf", DEBUG, [], true)
logger("io.grpc.internal.SerializingExecutor", OFF, [], true)
logger("org.apache", WARN, [], true)
logger("io.grpc.internal.ClientCallImpl", OFF, [], true)
logger("org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration", INFO, [], true)
logger("com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter", DEBUG, [], true)
logger("de.codecentric.boot.admin.client.registration.ApplicationRegistrator", INFO, [], true)
logger("org.quartz.core", WARN, [], true)