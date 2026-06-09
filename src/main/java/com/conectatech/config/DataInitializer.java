package com.conectatech.config;

import com.conectatech.entity.Skill;
import com.conectatech.entity.User;
import com.conectatech.entity.enums.UserRole;
import com.conectatech.repository.SkillRepository;
import com.conectatech.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Profile("!test") // Não executa durante testes
    public CommandLineRunner initData(SkillRepository skillRepository,
                                      UserRepository userRepository) {
        return args -> {
            if (skillRepository.count() > 0) {
                log.info("Banco já populado. Pulando inicialização.");
                return;
            }

            log.info("Populando banco de dados com dados iniciais...");

            // ── Skills ──────────────────────────────────────────────────────
            List<String> skillNames = List.of(
                "Java", "Python", "JavaScript", "TypeScript", "React",
                "Spring Boot", "Node.js", "SQL", "PostgreSQL", "MongoDB",
                "Docker", "Kubernetes", "AWS", "DevOps", "UX Design",
                "Product Management", "Agile/Scrum", "Machine Learning",
                "Data Science", "Cybersecurity"
            );

            List<Skill> skills = skillNames.stream()
                    .map(Skill::new)
                    .toList();

            List<Skill> savedSkills = skillRepository.saveAll(skills);
            log.info("{} skills cadastradas.", savedSkills.size());

            // ── Usuários de exemplo ──────────────────────────────────────────
            Skill java  = savedSkills.stream().filter(s -> s.getName().equals("Java")).findFirst().orElseThrow();
            Skill react = savedSkills.stream().filter(s -> s.getName().equals("React")).findFirst().orElseThrow();
            Skill devops = savedSkills.stream().filter(s -> s.getName().equals("DevOps")).findFirst().orElseThrow();

            User mentor1 = new User(
                "Ana Lima",
                "ana.lima@mentor.com",
                "senha123",
                UserRole.MENTOR,
                "Desenvolvedora Java Sênior com 10 anos de experiência em sistemas distribuídos.",
                "https://meet.google.com/abc-defg-hij"
            );
            mentor1.setSkills(Set.of(java));

            User mentor2 = new User(
                "Carlos Souza",
                "carlos.souza@mentor.com",
                "senha123",
                UserRole.MENTOR,
                "Especialista em Frontend e DevOps apaixonado por ensinar.",
                "https://teams.microsoft.com/l/meetup-join/xyz"
            );
            mentor2.setSkills(Set.of(react, devops));

            User mentee1 = new User(
                "João Silva",
                "joao.silva@mentee.com",
                "senha123",
                UserRole.MENTEE,
                "Estudante de ADS buscando meu primeiro emprego na área de TI.",
                null
            );

            userRepository.saveAll(List.of(mentor1, mentor2, mentee1));
            log.info("Usuários de exemplo criados.");
            log.info("==============================================");
            log.info("Dados iniciais carregados com sucesso!");
            log.info("Swagger UI: http://localhost:8080/swagger-ui.html");
            log.info("==============================================");
        };
    }
}
