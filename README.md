# API Finanças Pessoais
Sistema criado para participar do processo seletivo da MAPS

### Java
A versão Java utilizada para criar o projeto foi a 14

### Maven
Projeto foi criado usando o controle de dependências [Maven](), para contruir o projeto é necessário executar no terminal o seguinte comando:
> mvn install

Ao terminar a construção do projeto, será criada a pastar __target__ com o código compilado do projeto e na pasta __site__ 
pode-se encontrar o relatório de execução dos testes unitários surefire-report.html e o relatório de cobertura de testes 
__jacoco/indext.html__

O artefato compilado depois da instalação é o __financas-1.0.0.jar__ que será levantado no servidor para apresentação.

### Banco de dados
.....

### Http Status
A definição do Http Status pode ser visualizada na [documentação da API](https://htmlpreview.github.io/?https://github.com/cepp/teste-maps/blob/master/docs/swagger.html) ou nos itens abaixo:
* `200`: será retornado como caso de sucesso da API
* `204`: será retornado quando não for encontrado algum dado do banco de dados
* `400`: retornará erros de validação e negócio
* `401`: quando o usuário não for autorizado a acessar o recurso
* `404`: quando um recurso não for encontrado
* `409`: quando tentar fazer uma operação que não é a responsabilidade do recurso, exemplo de alterar no recurso de inclusão 
* `500`: quando houver um erro não esperado na aplicação

### Tecnologias escolhidas
......

### Deployment Aplicação
.....

### Thread-safe
Uma das alternativas escolhidas para atender a solicitação do Thread-safe, foi de tornar as classes de modelo e DTO 
imutáveis, evitando que uma Thread possa substituir o valor da outra.

....

### Desempenho
.....

### Teste desempenho
.....

### Segurança
