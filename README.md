# LaMortEtSes7CCWeb

## Présentation

Application pour accompagner le JDR nommé "Les Feus Peregrins" , anciennement nommé "La Mort et ses 7 couvres-chefs".
Chaque joueur dispose de son application sur son portable/PC/navigateur préféré, et peut voir en temps réel les statistiques de ses équipements, les monstres qu'il rencontre et pleins d'autres surprises.

De son côté le MJ dispose d'une interface spécifique qui lui permet de créer monstres, objets, armes... et de les donner aux joueurs en temps réel. Bref il fait le MJ numérique : l'administrateur.
Toutes les données de l'application sont contenues dans une base de données, que je remplis moi-même depuis plusieurs années au cours des parties. Si vous souhaitez utiliser l'application il faudra vous conformer au format des objets que j'ai établi.

## Technique
Application développée en kotlin avec les frameworks compose multiplatform et kotlin mulitplatform. Ainsi pour un code unique j'obtiens une application serveur java, une application cliente desktop, android et web.

Le serveur est déployé sur aws, avec les sécurités et la base de données en ligne. Le site web est aussi déployé sur github. Les applications clients sont configurées pour utiliser le serveur distant. Le tout est fonctionnel, avec des releases directement attachées au projet github.

