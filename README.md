# Gutendex Author Fetcher

Ce projet est une application Java en ligne de commande qui interroge l'API publique [Gutendex](https://gutendex.com/) pour récupérer des informations sur des livres, extraire une liste unique de noms d'auteurs, et sauvegarder cette liste dans un fichier texte.

Ce projet a été développé en mettant l'accent sur les bonnes pratiques de conception logicielle, la testabilité et l'utilisation de fonctionnalités Java modernes.

## Table des matières
- [Fonctionnalités](#fonctionnalités)
- [Choix d'architecture et de conception](#choix-darchitecture-et-de-conception)
- [Prérequis](#prérequis)
- [Installation et Exécution](#installation-et-exécution)
- [Lancer les tests](#lancer-les-tests)

---

### Fonctionnalités

- Interroge l'API Gutendex de manière paginée pour collecter des données sur les livres.
- Extrait et dédoublonne les noms d'auteurs à partir des données collectées.
- Écrit la liste unique des auteurs dans un fichier `authors.txt` à la racine du projet.
- Utilise un logging structuré (via SLF4J et Logback) pour suivre l'exécution du processus.

---

### Choix d'architecture et de conception

L'architecture de ce projet a été pensée pour être modulaire, testable et maintenable.

1.  **Programmation orientée interface** : Le cœur de l'application est découpé en services (`GutendexService`, `WriterService`, `AuthorService`) définis par des interfaces. Cela permet de découpler les implémentations concrètes du reste de l'application.
    -   `GutendexService` : Définit un contrat pour la récupération de données, sans imposer une technologie (actuellement implémenté avec le client HTTP de Java).
    -   `WriterService` : Définit un contrat pour l'écriture des données, permettant de changer facilement de stratégie de sortie (fichier local, base de données, etc.).

2.  **Injection de Dépendances (DI) Manuelle** : Pour garder le projet léger et sans framework, l'injection des dépendances est réalisée manuellement dans la classe `Main`. C'est le seul endroit de l'application qui connaît les implémentations concrètes, agissant comme la "Racine de Composition" (Composition Root). Le reste de l'application ne dépend que des abstractions (interfaces).

3.  **Testabilité** : Grâce à l'utilisation d'interfaces, les tests unitaires (`GutendexClientTest`) sont ciblés et efficaces. Ils utilisent Mockito pour mocker les dépendances (comme `HttpClient` et `ObjectMapper`) et se concentrent uniquement sur la logique métier de la classe testée, sans dépendre d'une véritable connexion réseau.

4.  **Fonctionnalités Java Modernes** :
    -   **Java 25** : Le projet est configuré pour utiliser une version récente de Java.
    -   **Records** : Les DTOs (`GutendexResponse`, `Book`) sont implémentés avec des `records` pour un code plus concis et immuable.
    -   **Stream API** : La logique de pagination et de traitement des données dans la classe `Main` est gérée de manière fonctionnelle et déclarative grâce à l'API Stream.
    -   **Interfaces Fonctionnelles** : Les interfaces de service avec une seule méthode sont annotées avec `@FunctionalInterface`, indiquant leur compatibilité avec les expressions lambda.

---

### Prérequis

-   [JDK 25](https://jdk.java.net/25/) ou une version compatible.
-   [Apache Maven](https://maven.apache.org/download.cgi) (version 3.6+).

---

### Installation et Exécution

1.  **Cloner le dépôt**
    ```bash
    git clone https://github.com/amirzacker/test-ati/
    cd test-ati
    ```

2.  **Compiler le projet avec Maven**
    Cette commande va télécharger les dépendances, compiler le code et lancer les tests.
    ```bash
    mvn clean install
    ```

3.  **Exécuter l'application**
    Une fois le projet compilé, vous pouvez l'exécuter avec la commande suivante. L'application créera un fichier `authors.txt` à la racine du projet.
    ```bash
    mvn exec:java -Dexec.mainClass="org.atineos.Main"
    ```
    *(Note: Un plugin `exec-maven-plugin` pourrait être ajouté au `pom.xml` pour simplifier cette commande).*

---

### Lancer les tests

Pour lancer la suite de tests unitaires et vérifier l'intégrité du code, utilisez la commande :
```bash
mvn test
```
Un rapport de tests sera généré dans le répertoire `target/surefire-reports`.
