package com.galbern.req.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Profile("firebase")
public class FirebaseConfig {

    @Bean
    public FirebaseOptions firebaseOptions(
            @Value("${app.firebase.credentials-file:}") String credentialsFile,
            @Value("${app.firebase.database-url:https://pp-dev-49657-default-rtdb.europe-west1.firebasedatabase.app}") String databaseUrl
    ) throws IOException {
        GoogleCredentials credentials;
        String credentialsEnv = System.getenv("FIREBASE_CREDENTIALS");
        if (credentialsEnv != null && !credentialsEnv.isBlank()) {
            credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsEnv.getBytes(StandardCharsets.UTF_8)));
        } else if (credentialsFile != null && !credentialsFile.isBlank()) {
            credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFile));
        } else {
            credentials = GoogleCredentials.getApplicationDefault();
        }

        return FirebaseOptions.builder()
                .setCredentials(credentials)
                .setDatabaseUrl(databaseUrl)
                .build();
    }

    @Bean
    public FirebaseApp firebaseApp(FirebaseOptions options) {
        List<FirebaseApp> apps = FirebaseApp.getApps();
        if (apps == null || apps.isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return apps.get(0);
    }

    @Bean
    public FirebaseDatabase firebaseDatabase(FirebaseApp firebaseApp) {
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}
