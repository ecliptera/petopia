package net.gb.knox.petopia.converter;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtConverterUnitTest {

    private final JwtConverter jwtConverter = new JwtConverter();


    @Test
    public void testConvert() {
        var jwt = new Jwt("A", Instant.now(), Instant.MAX, Map.of("A", ""), Map.of("A", ""));
        var convertedJwt = jwtConverter.convert(jwt);
        assertNotNull(convertedJwt);
    }

    @Test
    public void testConvertAuthorities() {
        var expectedRoles = List.of("ROLE_realm_access", "ROLE_resource_access", "ROLE_groups");
        var claims = Map.of("realm_access", Map.of("roles", List.of("realm_access")), "resource_access",
                            Map.of("client", Map.of("roles", List.of("resource_access"))), "groups", List.of("groups")
        );
        var jwt = new Jwt("a", Instant.now(), Instant.MAX, Map.of("Test", ""), claims);

        var convertedJwt = jwtConverter.convert(jwt);

        assertNotNull(convertedJwt);
        var actualRoles = convertedJwt.getAuthorities()
                                      .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .toList();
        assertTrue(expectedRoles.containsAll(actualRoles),
                   String.format("Expected list %s to have the same elements as %s", expectedRoles, actualRoles)
        );
    }
}
