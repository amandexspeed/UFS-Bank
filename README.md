# 🏦 UFS-BANK - Sistema de Gestão Bancária
 Fork do repositório Gerenciamento Banco
 
 Projeto para matérias SD (Sistemas distribuídos) e SAD (Sistemas de apoio a decisão) focado na gestão de atendimentos e operações bancárias, com persistência híbrida (Arquivos para ETL e Banco de Dados para Operações).

 Simula diferentes diferentes setores de um banco, como RH, caixa e recepção.
 Usamos o conceito de multilista e fila para a consistência interna dos dados e arquivos para salvar os dados dos funcionários para uso posterior.

 # ⌛ Em desenvolvimento

 - Processo de ETL para analisar saúde do banco e pontos de melhoria
 - Processo para atender cliente realmente implementado (Não apenas chamar o cliente)
 - BI

# ✨ Coisas que você pode testar!
 - Comunicação em rede local entre diferentes instâncias
  
# 🛠️ Tecnologias Utilizadas

- **Java 18** com Maven.
- **Hibernate ORM** para persistência no MySQL (Aiven Cloud).
- **Sockets (UDP)** para comunicação entre instâncias.
- **GitHub Actions** para Integração Contínua (CI).
- **Dotenv** para gestão segura de credenciais.


 Você pode baixar o zip da release a vontade para aproveitar nossa ferramenta!</br>
 Para o cliente visual funcionar corretamente, você precisa ter uma instância na sua rede do servidor(programa de console).</br>
 Mas caso você não se sinta confortável em usar prompt de comandado para rodar o jar do servidor de nosso projeto, não tem problema! o .bat foi criado justamente para você!</br>
 Ele inicia uma instância do servidor e outra do cliente apenas clicando! (Não tem pegadinhas nem capturamos nada kkkkkkkkkkkkkkkk apenas te ajudamos!)</br>
