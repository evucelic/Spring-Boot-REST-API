# Spring Boot REST Middleware
KING ICT Akademija prijemni zadatak
> Middleware s 4 endpointa : za dohvaćanje svih proizvoda, proizvoda s specifičnim ID-om, proizvoda filtriranih po kategoriji i/ili cijeni, proizvoda filtriranih po imenu \
Podaci se dohvaćaju s [dummyjson.com](dummyjson.com)

## Pokretanje aplikacije

- Klonirajte repozitorij i pozicionirajte se unutar direktorija aplikacije
```sh
git clone https://github.com/evucelic/SpringBootREST.git
cd SpringBootREST
```
- Otvorite projekt u IDE po izboru i stavite SDK projekta na JDK 17, potrebno je imati instaliran gradle

### **Docker** 
- Potrebno je imati instaliran Docker i pokrenuti Docker engine prije poziva u terminalu

Izgradite Docker image slijedećom komandom:
```sh
docker build -t springbootrest:latest .
```
Nakon builda, Docker kontejner može se pokrenuti s:
```sh
docker run -p 8080:8080 springbootrest:latest
```
Nakon ovoga, aplikacija se vrti lokalno na portu 8080. Zaustavljanje kontejnera može se napraviti putem desktop aplikacije ili slijedećim komandama:
```sh
docker ps // nađi ID kontejnera
docker stop <ID_kontejnera>
```

### **Gradle** 
- Kako bi lokalno pokrenuli projekt koristeći gradle, treba izgraditi projekt naredbom:
```sh
./gradlew clean build
```
Time će se stvoriti .jar file u build/libs direktoriju. Aplikacija se pokreće s:
```sh
./gradlew bootRun
```
Server se može ugasiti putem terminala kombinacijom CTRL + C


## Endpointovi
Svi endpointovi nalaze se unutar `ProductController` klase
#### getAllProducts
- **URL**: `/products`
- **Metoda**: `GET`
- **Opis**: Dohvaća sve proizvode.
- **Odgovor**: `200 OK` sa listom svih proizvoda.

#### getProductById(@PathVariable Long id)
- **URL**: `/products/{id}`
- **Metoda**: `GET`
- **Opis**: Dohvaća proizvod prema danom ID-u.
- **Parametri**:
  - `id` (Long) - ID proizvoda koji se dohvaća.
- **Odgovor**: 
  - `200 OK` sa podacima o proizvodu ako je pronađen.
  - `404 Not Found` ako proizvod nije pronađen.

#### getProductsByFilter(@RequestParam List\<String> key, @RequestParam List\<String> operation, @RequestParam List\<String> value)
- **URL**: `/products/filter`
- **Metoda**: `GET`
- **Opis**: Filtrira proizvode prema zadanim kriterijima.
- **Parametri**:
  - `key` (List<String>) - Lista ključeva po kojima se filtrira.
  - `operation` (List<String>) - Lista operacija za filtriranje (`>`, `<`, `:`).
  - `value` (List<String>) - Lista vrijednosti po kojima se uspoređuju ključevi.
- **Odgovor**: `200 OK` sa listom filtriranih proizvoda.

#### getProductsBySearch(@RequestParam String name)
- **URL**: `/products/search`
- **Metoda**: `GET`
- **Opis**: Pretražuje proizvode prema danom imenu.
- **Parametri**:
  - `name` (String) - Ime (ili dio imena) proizvoda za pretraživanje.
- **Odgovor**: `200 OK` sa listom proizvoda koji zadovoljavaju kriterij pretraživanja.


## Testiranje
Da biste pristupili endpointovima, morate se prijaviti koristeći korisničko ime i lozinku. Korisnički podaci za prijavu mogu se pronaći na [dummyjson.com/users](https://dummyjson.com/users). Bilo koji korisnik je valjan za prijavu, na primjer:

- **username**: emilys
- **password**: emilyspass

### Primjeri zahtjeva

#### Primjer zahtjeva za dohvaćanje svih proizvoda
```http
GET localhost:8080/products
```

#### Primjer zahtjeva za dohvaćanje proizvoda po ID-u
```http
GET localhost:8080/products/5
```

#### Primjer zahtjeva za dohvaćanje proizvoda po filtru
```http
GET localhost:8080/products/filter?key=price&operation=>&value=500&key=price&operation=<&value=550
GET localhost:8080/products/filter?key=category&operation=:&value=fragrances
GET localhost:8080/products/filter?key=category&operation=:&value=fragrances&key=price&operation=>&value=50
```

#### Primjer zahtjeva za dohvaćanje proizvoda po imenu
```http
GET localhost:8080/products/search?name=red
GET localhost:8080/products/search?name=Red%20Lipstick


