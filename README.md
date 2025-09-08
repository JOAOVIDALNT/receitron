# RECEITRON

## Problema de negócio resolvido
É comum termos um leque de receitas bem enxuto, 
eu mesmo moro sozinho e não devo cozinhar mais do que 10 combinaçoes diferentes de proteínas e vegetais em um mês.

O Receitron leva em conta as preferências dos usuários pra sugerir
novas receitas e montar cardápios semanais estratégicos, assim o usuário
pode explorar novas receitas, culturas e combinações, avaliar e redefinir
suas preferências de acordo com os pratos experimentados.

## Arquitetura escolhida e justificativa
Recentemente um programador experiente me perguntou se eu conhecia ou já tinha trabalhado com o padrão arquitetural hexagonal (ports and adapters). O fato de eu não conhecer e de esse padrão ser tão relevante, junto com todas as suas valências me motivaram. Parece fácil seguir os princípios SOLID (principalmente no spring que abstrai muito da configuração e te prender em um padrão mais acoplado) com uma arquitetura bem definida e robusta. Foi um desafio legal, gostei bastante.

Referências para tomadas de decisões acerca da arquitetura:
- Arquitetura Hexagonal na Prática - Fernanda Kipper: https://www.youtube.com/watch?v=UKSj5VJEzps
- Hexagonal Architecture in Spring Boot: A Practical Guide: https://dev.to/jhonifaber/hexagonal-architecture-or-port-adapters-23ed
- Um repositório aleatório que eu encontrei com a implementação: https://github.com/aleccanto/hexagonal-exemple-springboot

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

## Decisões técnicas e tradeoffs

- Autenticação: embora não fosse demandado, é um conceito que gosto de explorar e eu quis aplica-lo dentro dessa arquitetura, ainda que tenha custado tempo valeu a pena por como o usuário interage com o negócio.
- Redis: algumas operações na api externa gastariam demasiado tempo caso fossem chamadas todas as vezes, utilizei redis e algumas requisiçoes estão 500% mais rápidas, no entanto, diquei das 6h ao 12h tentando entende o comportamento estranho do cache ao subir me containers Docker e desisti em nome da entrega.
- Api: a escolha da Api foi tecnica, elaborei o projeto baseado em outra api de nutrição sugerida que por sua vez tinha uma grande limitação no uso gratuito.





