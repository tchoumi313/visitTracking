# Vérifier si le port 8080 est occupé
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "Le port 8080 est déjà occupé."
    # Demander à l'utilisateur s'il veut tuer le processus en cours
    read -p "Voulez-vous tuer le processus en cours ? (oui/non) " response
    if [[ "$response" =~ ^([oO]|[oO][uU][iI])$ ]]; then
        # Tuer le processus en cours
        fuser -k 8080/tcp
        echo "Le processus a été tué."
    else
        # Quitter le script
        echo "Le processus n'a pas été tué. Le script s'arrête."
        exit
    fi
fi

# Lancer le projet Spring Boot
cd src/backend/spring-boot
# mvn clean 
# mvn install
mvn spring-boot:run
