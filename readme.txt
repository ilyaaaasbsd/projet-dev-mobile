Caftan Rental Mobile App
Une application Android complète de location et vente de caftans marocains avec authentification utilisateur, gestion de produits, panier d'achat et paiement.

Table des Contenus
Fonctionnalités

Architecture

Structure du Projet

Prérequis

Installation

Configuration API

Gestion des Rôles

Captures d'Écran

Technologies Utilisées

Contribution

Licence

Fonctionnalités
Pour les Clients
Authentification : Connexion et inscription sécurisée

Catalogue : Parcourir les caftans par catégories (Nouveautés, Robes, Soirée, Mariage)

Recherche : Trouver rapidement des produits

Favoris : Sauvegarder les articles préférés

Détails Produit : Voir les détails, sélectionner taille et quantité

Panier : Ajouter/modifier/supprimer des articles

Checkout : Processus de commande en plusieurs étapes

Paiement : Saisie des informations de paiement

Historique : Visualiser les commandes passées

Pour les Admins
Dashboard : Interface d'administration

Gestion Produits : CRUD complet (Créer, Lire, Mettre à jour, Supprimer)

Upload d'Images : Sélection depuis la galerie

Catégorisation : Assigner des catégories aux produits

Modération : Gérer le contenu de l'application

Architecture
text
Client Android (Kotlin/Java)
        |
    Retrofit2
        |
    API REST (Node.js/Express)
        |
    MongoDB Atlas
Structure du Projet
text
app/
├── java/com.example.caftanrental/
│   ├── Activities/
│   │   ├── MainActivity.java          # Écran principal avec filtres
│   │   ├── LoginActivity.java         # Connexion utilisateur
│   │   ├── SignupActivity.java        # Inscription
│   │   ├── ProductDetailsActivity.java # Détails produit
│   │   ├── CartActivity.java          # Panier
│   │   ├── CheckoutActivity.java      # Information client
│   │   ├── PaymentActivity.java       # Paiement
│   │   ├── OrderReviewActivity.java   # Revue commande
│   │   ├── AddCaftanActivity.java     # Ajout produit (admin)
│   │   ├── EditCaftanActivity.java    # Édition produit (admin)
│   │   ├── FavoritesActivity.java     # Favoris
│   │   └── SearchActivity.java        # Recherche
│   │
│   ├── Adapters/
│   │   ├── CaftanAdapter.java         # Adapteur catalogue
│   │   └── CartAdapter.java           # Adapteur panier
│   │
│   ├── Models/
│   │   ├── Caftan.java                # Modèle produit
│   │   ├── LoginRequest.java          # Requête connexion
│   │   ├── LoginResponse.java         # Réponse connexion
│   │   └── OrderRequest.java          # Requête commande
│   │
│   ├── API/
│   │   ├── ApiService.java            # Interface endpoints
│   │   └── RetrofitClient.java        # Client HTTP
│   │
│   └── Utils/
│       └── CartManager.java           # Gestion panier local
│
├── res/
│   ├── layout/                        # Layouts XML
│   ├── drawable/                      # Images et icônes
│   └── values/                        # Resources strings/couleurs
│
└── build.gradle.kts                   # Configuration Gradle
Prérequis
Android Studio (version Arctic Fox ou supérieure)

JDK 11 ou supérieur

Android SDK API 24 minimum

Gradle 7.0+

Compte MongoDB Atlas (pour la base de données)

Node.js (pour le backend)

Installation
1. Cloner le projet
bash
git clone [URL_DU_PROJET]
cd caftanrental
2. Ouvrir dans Android Studio
Ouvrir Android Studio

Sélectionner "Open an existing project"

Choisir le dossier du projet

3. Synchroniser Gradle
Attendre la synchronisation automatique

Ou cliquer sur "Sync Now" si demandé

4. Configurer les dépendances
Le fichier build.gradle.kts contient déjà toutes les dépendances nécessaires :

kotlin
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
Configuration API
Backend nécessaire
Cette application nécessite un backend avec les endpoints suivants :

text
GET    /api/caftans         # Liste produits
POST   /api/caftans         # Ajouter produit
PUT    /api/caftans/{id}    # Modifier produit
DELETE /api/caftans/{id}    # Supprimer produit

GET    /api/cart            # Panier
POST   /api/cart            # Ajouter au panier
DELETE /api/cart/{id}       # Retirer du panier

POST   /api/orders          # Créer commande
POST   /api/login           # Connexion
POST   /api/signup          # Inscription
Configuration Retrofit
Modifier RetrofitClient.java pour pointer vers votre serveur :

java
public class RetrofitClient {
    private static final String BASE_URL = "https://votre-api.com/";
    // ...
}
Gestion des Rôles
L'application supporte deux rôles :

Utilisateur Standard
Parcourir le catalogue

Ajouter aux favoris

Gérer le panier

Passer commande

Administrateur
Toutes les fonctionnalités utilisateur

CRUD des produits

Upload d'images

Gestion des catégories

Les rôles sont gérés via le champ role dans la réponse de connexion.

Captures d'Écran
(Ajoutez vos captures d'écran ici)

Écran d'accueil - Catalogue avec filtres

Détails produit - Taille, quantité, ajout panier

Panier - Liste articles et total

Checkout - Processus de commande

Admin - Interface gestion produits

Technologies Utilisées
Frontend
Kotlin/Java - Langage principal

Android Jetpack - Composants modernes

Material Design - Interface utilisateur

Retrofit2 - Client HTTP

Glide - Chargement images

GSON - Sérialisation JSON

Backend (à implémenter)
Node.js/Express - Serveur API

MongoDB - Base de données

JWT - Authentification

Multer - Upload fichiers

Contribution
Les contributions sont les bienvenues ! Suivez ces étapes :

Fork le projet

Créer une branche (git checkout -b feature/AmazingFeature)

Commit vos changements (git commit -m 'Add AmazingFeature')

Push vers la branche (git push origin feature/AmazingFeature)

Ouvrir une Pull Request

Licence
Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

Remerciements
Design inspiré par les applications e-commerce modernes

Icônes de Material Design Icons

Images d'exemple de Unsplash