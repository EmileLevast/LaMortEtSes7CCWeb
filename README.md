# LaMortEtSes7CCWeb

<img width="1274" height="800" alt="Capture d'écran 2025-09-23 170551" src="https://github.com/user-attachments/assets/474833ea-66ab-4dbd-89c0-ce4d0633b657" />
<img width="1276" height="800" alt="Capture d'écran 2025-09-23 170636" src="https://github.com/user-attachments/assets/b5c9b9e5-ef81-4935-a5dd-507d2dd9839d" />
<img width="1280" height="800" alt="Capture d'écran 2025-09-23 170744" src="https://github.com/user-attachments/assets/566004d3-6808-4b57-a436-22183ad4117e" />
<img width="1280" height="800" alt="Capture d'écran 2025-09-23 170806" src="https://github.com/user-attachments/assets/aae21317-920e-4db8-8033-5d007cc005d3" />


## Présentation

Application pour accompagner le JDR nommé "Les Feus Peregrins" , anciennement nommé "La Mort et ses 7 couvres-chefs".
Chaque joueur dispose de son application sur son portable/PC/navigateur préféré, et peut voir en temps réel les statistiques de ses équipements, les monstres qu'il rencontre et pleins d'autres surprises.

De son côté le MJ dispose d'une interface spécifique qui lui permet de créer monstres, objets, armes... et de les donner aux joueurs en temps réel. Bref il fait le MJ numérique : l'administrateur.
Toutes les données de l'application sont contenues dans une base de données, que je remplis moi-même depuis plusieurs années au cours des parties. Si vous souhaitez utiliser l'application il faudra vous conformer au format des objets que j'ai établi.

## Technique
Application développée en kotlin avec les frameworks compose multiplatform et kotlin mulitplatform. Ainsi pour un code unique j'obtiens une application serveur java, une application cliente desktop, android et web.

Le serveur est déployé sur aws, avec les sécurités et la base de données en ligne. Le site web est aussi déployé sur github. Les applications clientes sont configurées pour utiliser le serveur distant. Le tout est fonctionnel, avec des releases directement attachées au projet github.

