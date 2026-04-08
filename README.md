# cyphera-spring-boot-starter

Spring Boot auto-configuration for Cyphera format-preserving encryption.

> **Note**: Currently uses a dummy (reversible shift) cipher as a placeholder.
> Real FF1 FPE will be wired in when `cyphera-java` is published to Maven.

## Usage

Add the dependency (once published):

```xml
<dependency>
    <groupId>io.cyphera</groupId>
    <artifactId>cyphera-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

Add a `cyphera.yaml` to your classpath:

```yaml
policies:
  ssn:
    engine: ff1
    alphabet: digits
    key_ref: demo-key

keys:
  demo-key:
    material: "2B7E151628AED2A6ABF7158809CF4F3C"
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
  policy-file: classpath:cyphera.yaml       # local file
  # server-url: https://cyphera.company.com  # future: remote policy server
```

## API

### CypheraClient

| Method | Description |
|--------|-------------|
| `protect(policy, value)` | Encrypt using a named policy |
| `access(policy, value)` | Access (decrypt) using a named policy |
| `ff1Encrypt(value, keyHex, alphabet)` | Direct FF1 encrypt |
| `ff1Decrypt(value, keyHex, alphabet)` | Direct FF1 decrypt |
