
# Projekt: Darwin World

Celem projektu było stworzenie symulacji składającej się ze świata, po którym chodzą zwierzaki, rośnie trawa oraz w dodatkowym wariancie rozchodzi się woda.


## Wylosowane dodatkowe konfiguracje

- [C] Mapa - Przypływy i odpływy
- [3] Wariant zachowania - Nieco szaleństwa


## Menu

Menu początkowe składa się z szeregu możliwych konfiguracji do wyboru. Od szerokości i wysokości mapy po liczbę mutacji, czy ilość generowanej trawy każdego dnia. W zasadzie wszystkie ważne aspekty symulacji można ustawić samemu. (Poniżej znajduje sie lista wszystkich parametrów.)

Przygotowane są cztery presety, po dwa na każdy rodzaj mapy. [NormalMap1, NormalMap2, WaterMap1, WaterMap2]

Oprócz wyboru jednej z przygotowanych konfiguracji, użytkownik może ustawić dowolne dla siebie parametry, uruchomić dla nich symulację przyciskiem "Commit", zapisać do pliku json wybierając nazwę i naciskając przycisk "Save New Preset", albo edytować lub usunąć jeden z utworzonych presetów. (Usuwanie i edytowanie wbudowanych konfiguracji jest niemożliwe.)

Dodatkowo użytkownik może wybrać czy chce aby dane z kolejnych dni symulacji zapisywały się do pliku CSV zaznaczając checkbox.
## Symulacja

Po naciśnięciu przycisku "Commit" w menu początkowym naszym oczom ukaże się mapa świata wraz z wygenerowanymi zwierzakami, trawą oraz ewentualną wodą w zależności od rodzaju wybranej konfiguracji. Po lewej stronie okna znajdują się ogólne informacje o symulowanym świecie a pod nimi miejsce ma wykres tych danych. Po prawej stronie znajduje się legenda do mapy oraz miejsce na dane o konkretnym zwierzaku. Na górze miejsce ma numer symulacji oraz dzień tej aktualnej, zaś na spodzie znajduje się przycisk "Start Simulation" oraz "Most Popular Genom".

Po naciśnięciu tegoż guzika do startu, symulacja zacznie się ruszać, oraz sam przycisk zmieni nazwę na "Stop Simulation". W każdym momencie możemy przełączać sie pomiędzy stanami symulacji. Kiedy symulacja jest wyłączona możemy nacisnąć na dowolnego zwierzaka i wybrać go do śledzenia. Od tego momentu informacje o nim bedą na bieżąco pojawiać sie po prawej stronie okna. Dodatkowo kiedy symulacja jest zatrzymana możemy sprawdzić, które zwierzaki mają najpopularniejszy genom. Kiedy częstotliwość występowania danego genomu jest taka sama jak innego, wybierany do pokazywania jest jeden z nich.


Do przedstawienia kierunku poruszania się zwierzaka skorzystaliśmy z ułożenia jego ikonki (łapki). W zależności w jakim kierunku wskazują palce, w takim kierunku stworzenie w danym momencie patrzy.
## Wodna mapa

Do ustawienia tego rodzaju mapy służą trzy parametry: 
- Water field number - określa ilość początkowych pól wody,
- Water days to change - określa co ile dni poziom wody ma się zmienić,
- Water percentage expansion - określa o ile procent woda ma się rozrastać.

W naszym programie problem wodnej mapy został rozwiązany następująco:\
Na początku program stara się wylosować w miarę spójny zbiornik wodny dla podanej na wejściu ilości pól wody. Zdarza się jednak, że liczba ta może być mniejsza od zadanej. Wynika to z faktu, iż kiedy trafi na miejsce, w którym stoi zwierzak i dookoła jest już woda, odpuszcza dalsze poszukiwania.\
Następnie co x zadanych dni dany zbiornik wodny się zwiększa (tutaj do liczby pól wody działa ta sama zasada co na początku) i co kolejnych x dni liczba wody się zmniejsza, ale w tym momencie już do losowych miejsc.\
Woda podczas generowania, czy ekspansji może zalewać trawę i przez to usuwa ją z danego pola. Zwierzaka natomiast zalać nie może, a samo stworzenie chodzić po wodzie nie może. Dlatego może nastąpić sytuacja, w której zwierzak umrze z głodu przez niemożliwość opuszczenia swojego miejsca.
## Zapisywanie danych
Kiedy użytkownik zdecyduje się na zapisywanie danych do pliku CSV, stworzonie zostany folder "simulationRaports" w głównym folderze projektu. Pliki są zapisywane do pliku o nazwie: "simulationreport-dd-MM-yyyy-HH-mm-ss.csv"

## Parametry wejściowe
![alt text](https://i.imgur.com/kmmmstu.png)
## Okno symulacji
![alt text](https://i.imgur.com/Rkkkj0k.png)
## Wykorzystane biblioteki

- JavaFX
- Gson
- OpenCSV


## Autorzy

- [Piotr Andres](https://github.com/Apiotr16st)
- [Paweł Podedworny](https://github.com/pabliqto)
