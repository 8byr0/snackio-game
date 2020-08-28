# About
> Snackio est un RPG multijoueur développé dans le cadre d'un projet de fin d'études

> Snackio is a multiplayer RPG developped as end of studies project

# Les différentes instances du joueur

## Couche de données (Player)
Player est une classe locale qui contient toutes les méta-données du joueur.
Lors d'une partie en multi, il y a autant d'instances de Player que de joueurs connectés à la partie.

Création d'un nouveau joueur
```java
Player thePlayer = new Player("John DOE", CharacterFactory.CharacterType.GOLDEN_KNIGHT)
```
Il faut donner un nom au joueur et son character. Les charactères disponibles sont définis dans la classe `CharacterFactory` via l'enum `CharacterType`.

## Couche graphique (Character)
Character est une classe locale qui gère le rendu du joueur:
- Interactions avec la map
- Position
Lors d'une partie en multi, il y a autant d'instances de Character que de joueurs connectés à la partie.

### Implémentation d'un nouveau Character
Il faut dans un premier temps un sprite avec les frames d'animations et les frames statiques du personnage. Ces frames doivent être de 64px de côté.
Ensuite, modifier la CharacterFactory pour déclarer le nouveau personnage :
```java
public enum CharacterType{
    // ...
    MY_NEW_CHARACTER
    // ...
}
```
```java
public static Character getCharacter(CharacterType type) throws UnhandledCharacterTypeException {
    // ...
    case MY_NEW_CHARACTER:
        // 9 et 4 correspondent respectivement aux colonnes et lignes présentes dans le sprite (utilisé pour le rendu des frames)
        skin = new AnimatedCharacterSkin("sprites/my_character_sprite.png", 9, 4);
        break;
    // ...
}
```
Une implémentation de TextureFactory a été ajoutée sur une branche non mergée (`feature/animatedSkin`) et permet de passer au constructeur un objet contenant les informations de Mapping au lieu de se contenter des lignes et colonnes. Cela permet des textures plus variées.

## Couche réseau (NetPlayer)
La classe NetPlayer est une classe dont les instances ne se trouvent que sur le serveur. Elle est disponible en RMI (Remote Method Invocation) depuis tous les clients. Chaque instance de NetPlayer dispose d'un accès aux autres instances de cette classe (uniquement sur le serveur) et peut ainsi communiquer avec les autres clients via ce biais.

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
### Coin quest en solo (SoloGameEngine)
Il est constitué d'un Thread qui gère les triggers de pièces et les ajouts de points d'interêt sur la map.

### Multi
En multi, le GameState est envoyé par le serveur au moment de la connexion du client.

## Coin quest en multi (NetworkGameEngine)
Mode à développer (la classe existe mais n'est pas implémentée)


# Les points d’intérêt
Les points d'intérêt sont des éléments graphiques qui sont ajoutés sur la map par le moteur de jeu.
On distingue deux types:
- Les pois statiques (bonus / malus par exemple)
- Les pois dynamiques (la pièce)

## Modèle abstrait statique (AbstractPointOfInterest)
Cette classe permet de fournir un modèle commun à tous les pois qui en héritent.
Elle définit une méthode `render()` qui gère l'affichage à l'écran sur la map et une méthode abstraite `execute(Character character)` qui doit être écrite pour chaque point d'intérêt. Cette méthode est appellée lorsqu'une collision se produit entre un joueur et le point d'intérêt en question. Le character passé en paramètre est le joueur en question sur lequel doit s'appliquer l'effet.

## Modèle abstrait dynamique (AnimatedPointOfInterest)
Le principe est rigoureusement le même que pour la version statique, la seule différence est au niveau du rendu qui anime un ensemble de frames donné.

## Implémenter un nouveau point d'intérêt
### Ajout à la POIFactory
La classe POIFactory est en charge de fournir les pois.
```java
public enum POIList {
    // ...
    MY_NEW_POI
    // ...
}
```
Ajouter également la gestion de l'instantiation dans la méthode `getRandom()`
```java
public static iPoi getRandom() {
    // ...
    case MY_NEW_POI:
        poi = new MyNewPOI();
        break;
    // ...
}
```
### Implémentation du POI
```java
public class MyNewPOI extends PointOfInterest{
    public MyNewPOI() {
        super("poi/my_new_poi.png");
    }

    @Override
    public void execute(Character character) {
        // Do whatever you want in this method
        character.dance();
    }
}

```


# Tiled map
Use `Tiled` editor to configure your map and add all tiles you want.
When the design part's gone, you need to create 3 objects layers :
- obstacles
- characters
- triggers
- spawns

## spawns layer
This layer contains all possible positions for pois / coins.

## obstacles layer
This layer contains all obstacles (i.e. zones the user cannot go through) of the map.

## triggers layer
This layer contains zones with custom behavior (such as doors).

### Create a door to another door
In order to create a door to another room's door, you need to set:
- its Name
- its type (`door`)
And add a few custom properties :
- destination_direction (NORTH, SOUTH, EAST, WEST), the direction of the character when going out of the destination door
- destination_name, the name of the destination door
- room_name, the name of the destination room

NB: if you declare a door to go from main map to sub room, room_name is the name of the destination room. If your door makes the character go back to the main map, set room_name to `BACK_TO_MAIN`.

# Create a map room
The process to create a sub room is the same as for creating a map.

# Register all in the code
## create a new MapType
First, you need to add a new enum value to MapType(in MapFactory).

## add new case in MapFactory
```java
switch(type){
// ...
case(MapType.MY_NEW_TYPE):
// We'll write this case body
break;
```

## initialize map and rooms
Once you've declared your case statement, you can initialize the map.
```java
// ...
    // Load the map
    Map desertCastleMap = new Map("maps/snackio.tmx", "DESERT_CASTLE");
    // Add map rooms
    MapRoom cave = new MapRoom("maps/snackio_cave.tmx", "CAVE", desertCastleMap);
    desertCastleMap.addRoom(cave);
    MapRoom castle = new MapRoom("maps/snackio_castle.tmx", "CASTLE", desertCastleMap);
    desertCastleMap.addRoom(castle);
    return desertCastleMap;
// ...
```
