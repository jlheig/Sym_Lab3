# Sym_Lab3
Par Jean-Luc Blanc, Kylian Bourcoud & Paul Reeve

## Questions

### 2.4.1
L'API Android ne propose aucun mécanisme de base pour sécuriser un NFC et
compliquer son clonage car NDEF n'est de base pas un format sécurisé.


### 2.4.2
Elle serait moins préférable qu'un NFC et celà pour plusieurs raisons:
1. L'autonomie. Une IBeacon fonctionne avec une batterie alors que le NFC fonctionne avec l'électricité que lui donne le téléphone. On a alors un risque que le IBEacon tombe à cours de batterie
2. Le Broadcast. Une IBeacon émet ses datas en broadcast ce qui fait 
que n'importe quel appareil peut récupérer ses datas. Un attaquant se trouvant simplement à proximité physique du IBeacon peut du coup récupérer l'UUID
et mettre cet UUID sur une autre IBeacon (du spoofing en somme)
3. La Passivité. Une IBeacon émet ses données en boucle contrairement
à une puce NFC qui exige une action de l'utilisateur. Pour un processus d'authentification il faut mieux utiliser des facteurs d'authentification qui requiert une utilisation active (où l'utilisateur doit faire une action)