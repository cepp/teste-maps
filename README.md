# API Finanças Pessoais
Sistema criado para participar do processo seletivo da MAPS

### Java
A versão Java utilizada para criar o projeto foi a 14

### Maven
Projeto foi criado usando o controle de dependências [Apache Maven](https://maven.apache.org/), para contruir o projeto 
é necessário executar no terminal o seguinte comando:
> mvn package

Ao terminar a construção do projeto, será criada a pastar __target__ com o código compilado do projeto e na pasta __site__ 
pode-se encontrar o relatório de execução dos testes unitários surefire-report.html e o relatório de cobertura de testes 
__jacoco/indext.html__

O artefato compilado depois da instalação é o __financas-1.0.0.jar__ que será levantado no servidor para apresentação.

### Banco de dados
Foi escolhido o banco de dados [H2](https://www.h2database.com/html/main.html) para poupar tempo de configuração e 
facilitar na implantação do projeto na nuvem. 

### Http Status
A definição do Http Status pode ser visualizada na [documentação da API](docs/swagger.html) ou nos itens abaixo:
* `200`: será retornado como caso de sucesso da API
* `204`: será retornado quando não for encontrado algum dado do banco de dados
* `400`: retornará erros de validação e negócio
* `401`: quando o usuário não estiver autenticado
* `403`: quando o usuário não for autorizado a acessar o recurso
* `404`: quando um recurso não for encontrado
* `409`: quando tentar fazer uma operação que não é a responsabilidade do recurso, exemplo de alterar no recurso de inclusão 
* `500`: quando houver um erro não esperado na aplicação

### Tecnologias escolhidas
......

### Deployment Aplicação
.....

### Thread-safe
Uma das alternativas escolhidas para atender a solicitação do Thread-safe, foi de tornar as classes de modelo e DTO 
imutáveis, pra isso não foi criado nenhum SETTER, o construtor sem argumentos está marcado com o modificador de acesso 
PRIVATE, para fazer alguma mudança no estado do objeto, é preciso criar uma nova instância dele, evitando que uma Thread 
possa substituir o valor da outra. Como as classes de serviço e Controller Rest não são thread-safe, para minimizar a 
concorrência todas as variáveis foram declaradas como __final__. 

Em todos os métodos que fazem escrita no banco de dados são anotados com @Transactional do Spring com o intuito de evitar
que o problema da concorrência de escrita no banco de dados.


### Desempenho
.....

### Teste desempenho
.....

### Segurança
Para fazer a segurança da API foi utilizado o Spring Security, armazenando os usuários e senha no banco de dados. 
Como todo o projeto foi criado usando Spring, o módulo de segurança seria mais facilmente acoplado ao projeto e também 
atende à demanda de fazer a autorização dos usuários via banco de dados.

Não foi criada uma estrtura de dados nova, porque o Spring Sercurity já possui essa estrutura, e atende ao solicitado.

Foram criados dois perfis, como solicitado, um de administrado (ADMIN) e outro para os usuários de 0-9 do sistema (USER).
