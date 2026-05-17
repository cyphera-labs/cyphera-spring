# cyphera-spring

[![CI](https://github.com/cyphera-labs/cyphera-spring/actions/workflows/ci.yml/badge.svg)](https://github.com/cyphera-labs/cyphera-spring/actions/workflows/ci.yml)
[![Security](https://github.com/cyphera-labs/cyphera-spring/actions/workflows/codeql.yml/badge.svg)](https://github.com/cyphera-labs/cyphera-spring/actions/workflows/codeql.yml)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](LICENSE)

Format-preserving encryption for [Spring Boot](https://spring.io/projects/spring-boot) — auto-configured starter powered by Cyphera.

Built on [`io.cyphera:cyphera`](https://central.sonatype.com/artifact/io.cyphera/cyphera) from Maven Central.

## Quick Start

Add the dependency:

```xml
<dependency>
    <groupId>io.cyphera</groupId>
    <artifactId>cyphera-spring-boot-starter</artifactId>
    <version>VERSION</version>
</dependency>
```

Add `cyphera.json` to your classpath, inject `CypheraClient`, and protect data:

```java
@Autowired
private CypheraClient cyphera;

String protectedSsn = cyphera.protect("ssn", "123-45-6789");
// → "T01i6J-xF-07pX" (tagged, dashes preserved)

String accessed = cyphera.access(protectedSsn);
// → "123-45-6789"
```

## Build

### From source

```bash
mvn package -DskipTests
```

### Via Docker

```bash
docker build -t cyphera-spring .
```

## Install / Deploy

1. Add the Maven dependency to your Spring Boot project
2. Place `cyphera.json` on the classpath (e.g. `src/main/resources/cyphera.json`)
3. Spring auto-configures `CypheraClient` — inject it anywhere

### Configuration

```yaml
# application.yml
cyphera:
  configuration-file: classpath:cyphera.json
  # configuration-file: file:/etc/cyphera/cyphera.json  # external file
```

## Usage

```java
@Autowired
private CypheraClient cyphera;

// Protect — policy determines engine, alphabet, key
String protectedValue = cyphera.protect("ssn", "123-45-6789");
// → "T01i6J-xF-07pX"

// Access — tag-based, no policy name needed
String original = cyphera.access(protectedValue);
// → "123-45-6789"

// Access with explicit policy (for untagged values)
String original = cyphera.access("ssn", protectedValue);

// Direct SDK access for advanced use
Cyphera sdk = cyphera.sdk();
```

### API

| Method | Description |
|--------|-------------|
| `protect(policy, value)` | Protect using a named policy |
| `access(protectedValue)` | Access using tag-based policy lookup |
| `access(policy, protectedValue)` | Access with explicit policy name |
| `sdk()` | Access underlying Cyphera SDK instance |

## Operations

### Policy Configuration

- Default location: `classpath:cyphera.json`
- Override with `cyphera.configuration-file` in `application.yml`
- Supports `classpath:`, `file:`, and absolute paths
- Policy changes require application restart

### Monitoring

- Bean creation logged at startup — check for `CypheraAutoConfiguration` in logs
- Errors throw `RuntimeException` — handle in your application error handling

### Upgrading

1. Bump the `cyphera-spring-boot-starter` version in `pom.xml`
2. Rebuild and redeploy your application

### Troubleshooting

- **Bean not created** — check that `cyphera.json` exists on the classpath
- **"Unknown policy"** — policy name doesn't match cyphera.json
- **"No matching tag"** — the protected value doesn't start with a known tag

## Policy File

```json
{
  "policies": {
    "ssn": { "engine": "ff1", "key_ref": "my-key", "tag": "T01" },
    "credit_card": { "engine": "ff1", "key_ref": "my-key", "tag": "T02" }
  },
  "keys": {
    "my-key": { "material": "2B7E151628AED2A6ABF7158809CF4F3C" }
  }
}
```

## Future

- JPA `AttributeConverter` for transparent field-level encryption on entities
- Dynamic policy reload without restart
- Actuator health indicator for Cyphera status
- Spring Security integration for role-based access policies

## License

Apache 2.0 — Copyright 2026 Horizon Digital Engineering LLC
