# Étape 1: Télécharger le JAR depuis Nexus
FROM openjdk:17-jdk-alpine AS downloader

# Installer curl pour télécharger l'artefact
RUN apk add --no-cache curl

# Définir l'URL correcte de l'artefact dans une variable d'environnement
ENV JAR_URL=http://192.168.33.10:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Télécharger l'artefact depuis Nexus et le renommer lors du téléchargement
RUN curl -u admin:root -o /5SE4G3gestionstationski-1.0.jar $JAR_URL

# Étape 2: Construire l'image finale
FROM openjdk:17-jdk-alpine
EXPOSE 8089

# Copier le JAR téléchargé depuis l'étape downloader avec le nom souhaité
COPY --from=downloader /5SE4G3gestionstationski-1.0.jar 5SE4G3gestionstationski-1.0.jar

# Définir l'entrée pour lancer l'application
ENTRYPOINT ["java", "-jar", "/5SE4G3gestionstationski-1.0.jar"]
