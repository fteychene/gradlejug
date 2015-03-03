package fr.fteychene.gradlejug.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by fteychene on 06/02/2015.
 */
@Configuration
@EnableJpaRepositories(basePackages = "fr.fteychene.gradlejug.repository.dao")
public class RepositoryConfiguration {
}
