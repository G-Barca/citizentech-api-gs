API de Pontos de Ônibus – Global Solution
Integrantes
Andre Sousa Matuda – RM 566733
Guilherme Barca Nascimento Silva – RM 568517
Guilherme Oliveira Feitosa – RM 566842
Kaliel Conceição de Aquino – RM 567587
Paulo Henrique Muniz – RM 567618
Link do vídeo

https://youtu.be/IbLQZXpUxhg

Sobre o projeto

A proposta do INTransporte é tornar o uso do transporte público mais acessível. O ponto inteligente poderá oferecer avisos sonoros, seleção de ônibus por totem e um botão de emergência.

Público beneficiado

A solução é pensada para cidadãos que utilizam o transporte público, com atenção especial a pessoas cegas ou com baixa visão e a pessoas com mobilidade reduzida.

O projeto completo prevê recursos de acessibilidade no ponto inteligente, como avisos sonoros e seleção de ônibus.

Problema e solução
Uma API que informa, para cada ponto, as linhas que o atendem e as últimas chegadas registradas.
ODS:
ODS 11 – Cidades e Comunidades Sustentáveis;
ODS 10 – Redução das Desigualdades;
ODS 9 – Indústria, Inovação e Infraestrutura.
Tecnologias utilizadas
Java 21
Spring Boot 4.1
Spring Web
Spring Data JPA
Bean Validation
Spring Security com JWT
Spring Cache
Spring HATEOAS
Actuator
Swagger/OpenAPI (springdoc)
Oracle
Maven
Como rodar o projeto

Pré-requisito: JDK 21.

1. Criar as tabelas no Oracle

Conecte-se ao Oracle utilizando os seguintes dados:

Host: oracle.fiap.com.br
Porta: 1521
SID: ORCL
Usuário e senha: credenciais do Oracle.

Na pasta sql/, abra e execute os dois scripts:

bancoAPI.sql
complemento_java.sql
2. Abrir o projeto

Abra o projeto no IntelliJ e aguarde o Maven baixar todas as dependências necessárias.

3. Informar o usuário e a senha do Oracle

A senha não fica armazenada no código. Informe as credenciais por meio de variáveis de ambiente:

Abra a classe CitizentechApiApplication e clique no triângulo verde ao lado do método main.
Vá em Run > Edit Configurations, escolha CitizentechApiApplication e clique em Modify options.
Marque Environment variables e preencha o campo que aparecerá com:
env
DB_USER=seu_usuario;DB_PASSWORD=sua_senha

4. Executar o projeto

Execute o programa pelo IntelliJ.

5. Testar a API

Acesse a documentação Swagger pelo endereço:

http://localhost:8080/swagger-ui/index.html

Siga os passos para autenticação:

Em POST /auth/login, clique em Try it out, utilize as credenciais abaixo e clique em Execute:
json
{
"login": "admin",
"senha": "admin123"
}

Copie o valor de token retornado na resposta.
Clique em Authorize (ícone de cadeado, no canto superior direito), cole o token e confirme.
Teste qualquer endpoint disponível, por exemplo, GET /api/pontos.

Também é possível executar pelo terminal!

Com o JAVA_HOME apontando para o JDK 21, defina DB_USER e DB_PASSWORD como variáveis de ambiente e execute:

bash
./mvnw spring-boot:run


No Windows:

cmd
.\mvnw.cmd spring-boot:run

Usuários de teste
Papel	Login	Senha	Permissão
ADMIN	admin	admin123	Consultar, cadastrar, alterar e excluir
USER	usuario	usuario123	Consultar
Diagrama das entidades
mermaid
erDiagram
LINHA ||--o{ LINHA_PONTO : "passa por"
PONTO_ONIBUS ||--o{ LINHA_PONTO : "pertence a"
LINHA ||--o{ OPERACAO : "tem"
ONIBUS ||--o{ OPERACAO : "realiza"
OPERACAO ||--o{ REGISTRO : "gera"
PONTO_ONIBUS ||--o{ REGISTRO : "recebe"

Justificativa das consultas
Tipo	Onde	Por quê
Derivada	findByEnderecoContainingIgnoreCase, findByStatus	Buscas simples
JPQL (@Query)	buscarPorPonto, contarConflitos	Consultas com junção e contagem
Specification	OperacaoSpecs.comFiltros	Filtros opcionais (data, linha, ônibus)
Justificativa do cache

Cacheamos a lista de pontos (pontos) e as linhas de cada ponto (linhasDoPonto), pois são as consultas mais frequentes do aplicativo e os dados mudam pouco.

Toda escrita que afeta esses dados limpa o cache (@CacheEvict).

O cache é armazenado em memória e é zerado ao reiniciar a aplicação.

Uso de Inteligência Artificial Generativa

Foi utilizada Inteligência Artificial Generativa para a criação de funções que fogem do conhecimento do grupo, tratamento de erros durante o desenvolvimento e auxílio na organização.

O uso foi feito em benefício do próprio aplicativo, com o objetivo de proporcionar uma melhor experiência ao usuário final.