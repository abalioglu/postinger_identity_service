package com.kafein.intern.postinger_identity_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, //enables the use of @PreAuthorize and @PostAuthorize
        securedEnabled = true, //" " " @Secured --> methoda ulaşmasını istediğin spesifik roller için
        jsr250Enabled = true)  //" " " @RolesAllowed
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
