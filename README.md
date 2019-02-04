# Les différentes instances du joueur 

## Couche de données (Player) 
Player est une classe locale qui contient toutes les méta-données du joueur.
Lors d'une partie en multi, il y a autant d'instances de Player que de joueurs connectés à la partie.

## Couche graphique (Character) 
Character est une classe locale qui gère le rendu du joueur:
- Interactions avec la map
- Position
Lors d'une partie en multi, il y a autant d'instances de Character que de joueurs connectés à la partie.

## Couche réseau (NetPlayer) 
La classe NetPlayer est une classe dont les instances ne se trouvent que sur le serveur. Elle est disponible en RMI (Remote Method Invocation) depuis tous les clients.

# Contrôle de position
## Contrôleur clavier (KeyboardController)
Le choix du contrôleur se fait en passant une des valeurs d'enum de `iMotionController`
```java
myPlayer.setMotionController(IMotionController.KEYBOARD);

```
La gestion du clavier est gérée dans la méthode `execute()`.
```java
@Override
public void execute(Character character) {
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        character.setDirection(Direction.WEST);
        // ...
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        character.setDirection(Direction.EAST);
        // ...
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        character.setDirection(Direction.SOUTH);
        // ...
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        character.setDirection(Direction.NORTH);
        // ...
    }
}
```

## Implémenter un nouveau contrôleur
Les contrôleurs doivent implémenter l'interface `iCharacterController` et donc définir une méthode `void execute(Character character)` qui sera appellée à chaque frame.
L'objet character correspond au joueur à rendre.

# Rendering 
## Gestion du rendu (GameRenderer) 
La classe GameRenderer va afficher à chaque frame tous les objets graphiques en appelant leurs méthodes `render()`. ATTENTION, il faut au préalable que les-dits objets aient été créés via un appel à leur méthode `create()`.
L'ordre dans lequel sotn rendus les objets est le suivant :
1. Déplacement de la caméra
2. Rendu des characters
3. Rendu de la map
4. Rendu des points d'intérêts
5. Rendu de l'overlay

 

# Le Gameplay 

## État de la partie (AbstractGameState) 
L'état de la partie (joueurs connectés, objectifs, temps...) est contenu dans une instance de classe héritée de AbstractGameState. 
### Solo
En solo ce GameState est local.
### Multi
En multi, le GameState en envoyé par le serveur au moment de la connexion du client.

## Coin quest en solo (SoloGameEngine) 

## Coin quest en multi (NetworkGameEngine) 

 

# Les points d’intérêt 

## Modèle abstrait statique (AbstractPointOfInterest)

## Modèle abstrait dynamique (AnimatedPointOfInterest) 

## Bonus de vitesse 
## Malus de vitesse

