package Utilitarios;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // 1. Carrega o XML (procura por hibernate.cfg.xml no src/main/resources)
            Configuration configuration = new Configuration().configure();

            // 2. Carrega as variáveis (Lógica Híbrida: .env ou System)
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            
            String dbHost = getVar(dotenv, "DB_HOST");
            String dbPort = getVar(dotenv, "DB_PORT");
            String dbName = getVar(dotenv, "DB_NAME");
            String dbUser = getVar(dotenv, "DB_USER");
            String dbPass = getVar(dotenv, "DB_PASSWORD");

            // 3. Monta a URL e injeta no Hibernate sobrescrevendo o XML
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s?ssl-mode=REQUIRED", dbHost, dbPort, dbName);

            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbUser);
            configuration.setProperty("hibernate.connection.password", dbPass);

            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Falha na criação da SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Método auxiliar para buscar no Dotenv ou no Sistema (GitHub)
    private static String getVar(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        return (value != null) ? value : System.getenv(key);
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}