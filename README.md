# API Finanças Pessoais
Sistema criado para participar do processo seletivo da MAPS

### Java
A versão Java utilizada para criar o projeto foi a 1.8 que é a versão pré-configurada no servidor que será implantado 
[Heroku](https://www.heroku.com)

### Maven
Projeto foi criado usando o controle de dependências [Apache Maven](https://maven.apache.org/), para contruir o projeto 
é necessário executar no terminal o seguinte comando:
> mvn install

Por padrão os testes integrados estão desabilitados para não gerar overhead na implantação do projeto no [Heroku](https://www.heroku.com), 
para habilitar, basta executar o comando no terminal:
> mvn install -Dskip.it.test=false


Após a instalação do projeto serão criados os seguintes artefatos na pasta __target__:
* __financas-1.0.0.jar__ é o artefato principal criado pelo projeto, que será implantado no [Heroku](https://www.heroku.com)
* __site/surefire-report.html__ relatório dos testes unitários
* __site/jacoco/index.html__ relatório da cobertura de código
* __site/swagger.html__ documentação da API utilizando a especificação OpenAPI 3.0.0 no formato Html e utilizando o 
padrão do [redoc](https://github.com/Redocly/redoc), que é o mesmo utilizado pelo [DICT API](https://www.bcb.gov.br/content/estabilidadefinanceira/forumpireunioes/api-dict.html)

### Banco de dados
Inicialmente foi escolhido o banco de dados [H2](https://www.h2database.com/html/main.html) para poupar tempo de 
configuração e facilitar na implantação do projeto na nuvem, mas o servidor escolhido não possui suporte ao banco de 
dados H2, então foi feita a configuração do [PostgreSQL](https://www.postgresql.org/), que o Heroku possui suporte.

Foi adicionada uma pasta na raiz do projeto chamada [Docker](https://www.docker.com) com o __docker-compose.yaml__ criando 
a instância do banco de dados necessários para utilizar a aplicação standalone.

Se possuir o docker instalado na máquina que utilizará a aplicação standalone, então abra o terminal na pasta [docker](/docker) 
e execute o comando abaixo:
> docker-compose up -d

Será criado um container PostgreSQL com o banco de dados __financas__, usuário __financas__ e senha __f1n4nc4s@123__, 
que conferem com as credenciais informadas no [application.yaml](src/main/resources/application.yaml) do projeto. 

### Http Status
A definição do Http Status pode ser visualizada na [documentação da API](docs/swagger.html) ou nos itens abaixo:
* `200`: será retornado como caso de sucesso da API
* `400`: retornará erros de validação e negócio
* `401`: quando o usuário não estiver autenticado
* `403`: quando o usuário não for autorizado a acessar o recurso
* `404`: será retornado quando não for encontrado algum dado do banco de dados
* `409`: quando tentar fazer uma operação que não é a responsabilidade do recurso, exemplo de alterar no recurso de inclusão 
* `500`: quando houver um erro não esperado na aplicação


### Deployment Aplicação
O deploy da aplicação foi realizado no [Heroku](https://www.heroku.com) e pode ser acessada a partir do [link](https://financas-maps.herokuapp.com/financas)
Não foi implementada a parte Web, mas como o projeto foi criado desde o início com a documentação do swagger, estou deixando
o [swagger-ui](https://financas-maps.herokuapp.com/financas/swagger-ui.html) habilitado para que possam utilizar, ao acessar
abrirá um prompt no navegador para colocar as credenciais, então colocar o usuário root ou os usuários usuario0 - usuario17.

Antes de implantar a aplicação foi necessário criar adicionar o add-on do PostgreSQL à minha conta, 

No processo de implantação, tive que criar um App no Heroku, que chamei de __financas-maps__, então fui na área de __Deploy__
e escolhi o método de deploy do [Github](), fazendo a vinculação e dando privilégios da minha conta à conta do Heroku, 
então fui na opção __Manual deploy__ e pressionei o botão __Deploy Branch__, então o Heroku faz o clone do projeto, 
construi o projeto usando o Maven, depois executa o artefato, disponibilizando a aplicação no [link](https://financas-maps.herokuapp.com/financas).


### Thread-safe
Uma das alternativas escolhidas para atender a solicitação do Thread-safe, foi de tornar as classes de modelo e DTO 
imutáveis, pra isso não foi criado nenhum SETTER, o construtor sem argumentos está marcado com o modificador de acesso 
PRIVATE, para fazer alguma mudança no estado do objeto, é preciso criar uma nova instância dele, evitando que uma Thread 
possa substituir o valor da outra. Como as classes de serviço e Controller Rest não são thread-safe, para minimizar a 
concorrência todas as variáveis foram declaradas como __final__. 

Em todos os métodos que fazem escrita no banco de dados são anotados com @Transactional do Spring com o intuito de evitar
que o problema da concorrência de escrita no banco de dados.


### Segurança
Para fazer a segurança da API foi utilizado o Spring Security, armazenando os usuários e senha no banco de dados. 
Como todo o projeto foi criado usando Spring, o módulo de segurança seria mais facilmente acoplado ao projeto e também 
atende à demanda de fazer a autorização dos usuários via banco de dados.

Não foi criada uma estrtura de dados nova, porque o Spring Sercurity já possui essa estrutura, e atende ao solicitado.

Foram criados dois perfis, como solicitado, um de administrado (ADMIN) e outro para os usuários de 0-9 do sistema (USER).
