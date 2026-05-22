/*package perfume.Perfumeria.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perfume.Perfumeria.model.perfume;
import perfume.Perfumeria.repository.perfumeRepository;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDb(perfumeRepository repository) {
        return args -> {
            repository.save(new perfume("Sauvage", "Dior", 1300.000));
            repository.save(new perfume("Bleu", "Chanel", 150.0));
            repository.save(new perfume("Oriental · Gourmand", "Dior", 150.0));
            System.out.println("¡Base de datos inicializada!");
        };
    }
} */
