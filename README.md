# spamc
## Introduction
spamc is a simple, solid library in Java to communicate with Apache SpamAssassin.
You can send different messages (tipically email) for SpamAssassin to check it is a spam or not.

## Maven dependency
```xml
<dependency>
    <groupId>hu.alphabox</groupId>
    <artifactId>spamc</artifactId>
    <version>0.1.2</version>
</dependency>
```

## Building
To make the latest SNAPSHOT jar file, you will need the Apache Maven installed.
Run the following command:
```
mvn clean package
```

## Usage
A simple usage example to send GTUBE message with all available command.
```java
public static void main(String[] args) {
    SARequest request = new SARequest();
    //GTUBE message string
    request.setMessage("XJS*C4JDBQADN1.NSBN3*2IDNEN*GTUBE-STANDARD-ANTI-UBE-TEST-EMAIL*C.34X");
    request.useCompression(false);
    SAClient client = new SAClient(InetAddress.getByName("localhost"), 783);
    for (SACommand command : SACommand.values()) {
        LOGGER.info("Send command: " + command.name());
        if (command == SACommand.TELL) {
            request.addHeader("Message-class", "spam");
            request.addHeader("Set", "local,remote");
        }
        request.setCommand(command);
        client.sendRequest(request);
    }
}
```

## License
This project licensed under Apache 2.0 License - see the [LICENSE](LICENSE) file for details