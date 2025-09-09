<h1 align="center">RECEITRON</h1>


<br>

## Problema de negócio resolvido
É comum termos um leque de receitas bem enxuto, 
eu mesmo moro sozinho e não devo cozinhar mais do que 10 combinaçoes diferentes de proteínas e vegetais em um mês.

O Receitron leva em conta as preferências dos usuários pra sugerir
novas receitas e montar cardápios semanais estratégicos, assim o usuário
pode explorar novas receitas, culturas e combinações, avaliar e redefinir
suas preferências de acordo com os pratos experimentados.

<br>
<br>

## Arquitetura escolhida e justificativa
Recentemente um programador experiente me perguntou se eu conhecia ou já tinha trabalhado com o padrão arquitetural hexagonal (ports and adapters). O fato de eu não conhecer e de esse padrão ser tão relevante, junto com todas as suas valências me motivaram. Parece fácil seguir os princípios SOLID (principalmente no spring que abstrai muito da configuração e te prender em um padrão mais acoplado) com uma arquitetura bem definida e robusta. Foi um desafio legal, gostei bastante.

Referências para tomadas de decisões acerca da arquitetura:
- Arquitetura Hexagonal na Prática - Fernanda Kipper: https://www.youtube.com/watch?v=UKSj5VJEzps
- Hexagonal Architecture in Spring Boot: A Practical Guide: https://dev.to/jhonifaber/hexagonal-architecture-or-port-adapters-23ed
- Um repositório aleatório que eu encontrei com a implementação: https://github.com/aleccanto/hexagonal-exemple-springboot

<br>
<br>

## Algoritmos e lógicas de negócio explicados
A aplicação tem 2 pontos centrais bem definidos: cadastro de preferências e receitas sugeridas.
A parte lógica relacionada a sugestão de receitas, considera as preferências e restrições do usuário e abusa de loops pra garantir a integridade mesmo com dados externos.

- POST /user/add/cultures -> o usuário logado pode cadastrar culturas (desde que a api externa suporte-as)
```json
[
    "Chinese", "Italian"
]
```

- POST /user/add/preferences -> o usuário logado pode cadastrar suas preferências (mesma premissa anterior)
```json
[
    "Pork", "Lamb"
]
```
- POST /user/add/restrictions -> o usuário logado pode cadastrar suas restrições
```json
[ 
    "Vegan", "Vegetarian"
]
```
- GET /recipe/suggest -> o usuário recebe uma sugestão aleatória (restrições são aplicadas)
```json
{        
  "id": 52774,
  "title": "Pad See Ew",
  "category": "Chicken",
  "culture": "Thai",
  "instructions": "blablabla meia chicara de farinha e come com água"
}
```

- GET /recipe/weekly-menu -> o usuário logado recebe um menu de até 8 pratos, baseado nas suas preferências e culturas preferidas. Além de considerado também suas restrições
```json
[
    {
        "id": 52791,
        "title": "Eton Mess",
        "category": "Dessert",
        "culture": "British",
        "instructions": "balbalbalblbalbalbalbalbalba."
    },
    {
        "id": 52827,
        "title": "Massaman Beef curry",
        "category": "Beef",
        "culture": "Thai",
        "instructions": "s, akdnakçdaçlmdçlmdlamdçlamd."
    }
]
```

### Observações
- as restrições são 100% respeitadas e filtradas em todo caso de uso.
- culturas não podem ser restringidas, só categorias de comida.
- preferencias do usuário e suas culturas favoritas são prioridades no menu mas outros pratos não restringidos também podem aparecer.
- a endpoint de sugestão não considera preferências, só restrições.

<br>
<br>

## Decisões técnicas e tradeoffs

- Autenticação: embora não fosse demandado, é um conceito que gosto de explorar e eu quis aplica-lo dentro dessa arquitetura, ainda que tenha custado tempo valeu a pena por como o usuário interage com o negócio.
- Api: a escolha da Api foi tecnica, elaborei o projeto baseado em outra api de nutrição sugerida que por sua vez tinha uma grande limitação no uso gratuito.

ps: Cache cai como uma luva nas requisições pra api externa no caso dessa aplicação, uma das decisões técnicas foi a necessidade de abrir mão do cache, que havia aprimorado mutio a performance da aplicação, no entando, ao subir a aplicação e o redis em container, por algum motivo ainda não descoberto a aplicação quebrava, em nome da entrega o cache foi desabilitado mas deve ser convertido em um feature futuramente.

<br>
<br>

## Como executar localmente

Eu recomendo rodar em container Docker, pra isso, deixei tudo configurado. Basta ter o Docker instalado e rodar o seguinte comando:

```bash
docker compose up --build
```
A api está configurada pra rodar na porta 8080 do container, podendo ser possível ser referenciada pelo localhost.

url da documentação swagger: http://localhost:8080/swagger-ui/index.html
- Em authorize, basta adicionar o token oriundo do login, o prefixo "Bearer " já está setado.

O projeto está rodando na versão 21 do java caso queira testar localmente e utilizei o maven como gerenciador de dependências,

Também disponibilizei uma collection do postman com tudo configurado para executar o fluxo lógico da aplicação "./receitron.postman_collection.json".

Para rodar os testes:
```bash
mvn test
```

<br>
<br>

## Exemplos de uso com resultados reais

- POST /auth/signup
req:
```json
{
  "email": "tester@gmail.com",
  "password": "abcd1234"
}
```
Response, se válido: Status 201 Created.

- POST /auth/login
req:
```json
{
    "email": "tester@gmail.com",
    "password": "abcd1234"
}
```
res:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXJAZ21haWwuY29tIiwicm9sZXMiOlsiVVNFUiJdLCJleHAiOjE3NTczNTM0NTksImlhdCI6MTc1NzM0OTg1OX0.4nw6pxMjqUBrsrTCP2zlgK1PSpB02nCsSV49h84UW6w",
    "roles": [
        "USER"
    ]
}
```

Outros casos de uso já foram retratados anteriormente. A api tem documentação swagger para testes mas recomendo experimentar a collection pré configurada do postman, lá eu automatizei o preenchimento do cabeçalho com o token ao fazer login e já montei o corpo das requisições.