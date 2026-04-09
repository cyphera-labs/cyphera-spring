# cyphera-spring-boot-starter

Spring Boot auto-configuration for Cyphera format-preserving encryption.

Built on [`io.cyphera:cyphera`](https://central.sonatype.com/artifact/io.cyphera/cyphera) from Maven Central.

## Usage

Add the dependency (once published):

```xml
<dependency>
    <groupId>io.cyphera</groupId>
    <artifactId>cyphera-spring-boot-starter</artifactId>
    <version>VERSION</version>
</dependency>
```

Add a `cyphera.json` to your classpath:

```json
{
  "policies": {
    "ssn": { "engine": "ff1", "alphabet": "digits", "key_ref": "demo-key", "tag": "T01" }
  },
  "keys": {
    "demo-key": { "material": "2B7E151628AED2A6ABF7158809CF4F3C" }
  }
}
```

Inject and use:

```java
@Autowired
private CypheraClient cyphera;

public void example() {
    String encrypted = cyphera.protect("ssn", "123-45-6789");
    // → "456-78-9012" (format preserved)

    String decrypted = cyphera.access("ssn", encrypted);
    // → "123-45-6789"
}
```

## Configuration

```yaml
# application.yml
cyphera:
  policy-file: classpath:cyphera.json       # local file
  # server-url: https://cyphera.company.com  # future: remote policy server
```

## API

### CypheraClient

| Method | Description |
|--------|-------------|
| `protect(policy, value)` | Protect using a named policy |
| `access(policy, value)` | Access (decrypt) using a named policy |
| `access(value)` | Access using tag-based policy lookup |
| `sdk()` | Access underlying Cyphera SDK instance |
