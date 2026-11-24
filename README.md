# Sistema de Controle Financeiro

API REST desenvolvida em Java com Spring Boot para o trabalho bimestral. O projeto foi evoluído durante o bimestre: começou com o CRUD de transações, recebeu um logger Singleton, depois Adapter e Proxy para consulta de CEP e, por fim, uma análise de orçamento com Strategy e Observer.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2

## Estrutura principal

- `Transacao`: entidade do sistema financeiro.
- `TransacaoRepository`: acesso ao banco com JPA.
- `TransacaoService`: regras do CRUD.
- `TransacaoController`: rotas do CRUD.
- `AppLogger`: Singleton clássico usado pelo sistema.
- `cep`: implementação de Adapter e Proxy.
- `orcamento`: implementação de Strategy e Observer.

## CRUD de transações

| Método | Rota | Retorno |
|---|---|---|
| POST | `/transacoes` | 201 Created |
| GET | `/transacoes` | 200 OK |
| GET | `/transacoes/{id}` | 200 OK ou 404 Not Found |
| PUT | `/transacoes/{id}` | 200 OK ou 404 Not Found |
| DELETE | `/transacoes/{id}` | 204 No Content ou 404 Not Found |

Exemplo de cadastro:

```json
{
  "descricao": "Mercado",
  "valor": 450.00,
  "data": "2026-11-24",
  "tipo": "DESPESA"
}
```

## Singleton - AppLogger

O `AppLogger` possui construtor privado e uma única instância acessada por `getInstance()`. A inicialização é lazy e thread-safe usando `volatile` e dupla checagem dentro de `synchronized`.

O logger registra níveis `DEBUG`, `INFO` e `ERRO` no console e no arquivo `app.log`.

Também existe uma versão `SpringAppLogger` com `@Component` para a comparação pedida na atividade. Na versão clássica, a própria classe garante a instância única. Na versão Spring, o container do Spring gerencia o objeto, que por padrão possui escopo singleton.

A versão escolhida para o projeto é o `AppLogger` clássico porque o objetivo da atividade era implementar o padrão manualmente.

## Adapter - consulta de CEP

A interface do sistema é:

```java
ConsultaCep
```

Ela possui o método `buscar(String cep)` e sempre devolve um objeto `Endereco` do nosso sistema.

Foram criados dois adapters:

- `ViaCepAdapter`
- `BrasilApiAdapter`

Os dois provedores foram simulados com JSON fixo em formatos diferentes, opção permitida no enunciado. Cada adapter traduz o formato externo para o mesmo objeto `Endereco`.

A rota é:

```text
GET /cep/{cep}
```

Exemplo:

```text
GET /cep/85816350
```

Para trocar o provedor, basta mudar uma linha em `CepConfig`.

## Proxy - cache e validação do CEP

`ConsultaCepProxy` implementa a mesma interface `ConsultaCep`.

Antes de chamar o adapter ele:

1. verifica se o CEP possui exatamente 8 dígitos;
2. verifica se o CEP já está no cache (`Map` em memória);
3. só chama o provedor quando o CEP ainda não foi consultado.

O Controller continua conhecendo apenas `ConsultaCep`. Ele não precisa saber se recebeu um Adapter ou um Proxy.

O `AppLogger` informa se a resposta veio do cache ou do provedor.

## Funcionalidade final - análise de orçamento

A nova funcionalidade soma as transações do tipo `DESPESA`, compara o valor gasto com um limite informado e informa se o orçamento está em situação normal ou de alerta.

Rota:

```text
GET /orcamento/analise?limite=1000&estrategia=padrao
```

Também é possível usar:

```text
GET /orcamento/analise?limite=1000&estrategia=conservadora
```

### Strategy

A interface `EstrategiaAlerta` representa a regra usada para decidir quando emitir um alerta.

Existem duas estratégias:

- `padrao`: alerta quando 80% ou mais do orçamento já foi usado;
- `conservadora`: alerta quando 60% ou mais foi usado.

O `OrcamentoService` trabalha com a interface, então uma nova estratégia pode ser criada sem alterar a lógica principal de cálculo.

### Observer

Quando a estratégia determina que existe um alerta, o `NotificadorOrcamento` avisa todos os objetos que implementam `ObservadorOrcamento`.

Foram usados dois observadores:

- `LogObservador`: registra o alerta com o `AppLogger`;
- `HistoricoObservador`: guarda os alertas em memória.

O histórico pode ser consultado em:

```text
GET /orcamento/alertas
```

## Princípios SOLID aplicados

- **Responsabilidade Única:** Controller recebe requisições, Service concentra regras e Repository acessa os dados.
- **Aberto/Fechado:** novos adapters, estratégias e observadores podem ser adicionados sem reescrever as classes principais.
- **Inversão de Dependência:** `CepController` depende da interface `ConsultaCep` e `OrcamentoService` trabalha com a interface `EstrategiaAlerta`.

## Banco de dados

Foi utilizado H2 em memória para facilitar a execução e a apresentação. Os dados são apagados quando a aplicação é encerrada.

H2 Console:

```text
http://localhost:8080/h2-console
```

Configuração:

```text
JDBC URL: jdbc:h2:mem:financeiro
User Name: sa
Password: vazio
```

## Como executar

É necessário Java 21 e Maven.

Na pasta do projeto:

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Sequência simples para demonstrar

1. Cadastre duas despesas usando `POST /transacoes`.
2. Liste com `GET /transacoes` para mostrar que o CRUD continua funcionando.
3. Consulte `GET /cep/85816350` duas vezes. Na segunda consulta o log mostrará que veio do cache.
4. Teste `GET /cep/123` para mostrar o bloqueio do Proxy.
5. Consulte `/orcamento/analise?limite=1000&estrategia=padrao`.
6. Troque apenas `padrao` por `conservadora` e mostre a diferença da Strategy.
7. Consulte `/orcamento/alertas` para mostrar o Observer guardando os alertas.
