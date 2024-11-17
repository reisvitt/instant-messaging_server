# TCP/UDP Instant Messaging SERVER

## Descrição

Este projeto faz parte da disciplina de Redes 2 e tem como objetivo a criação de uma aplicação de mensagens instantâneas utilizando os protocolos TCP e UDP. O foco é aplicar os conhecimentos adquiridos sobre as camadas de transporte e aplicação. Este repositório contém a implementação do servidor.

## Funcionalidades

- **Conexão TCP**: Gerencia conexões persistentes e confiáveis entre clientes e o servidor.
- **Conexão UDP**: Suporta comunicação não confiável e sem conexão, útil para transmissões rápidas.
- **Mensagens em tempo real**: Permite a troca de mensagens instantâneas entre clientes conectados.
- **Comunicação em grupos**: Os usuários podem se comunicar em grupos.
- **Gerenciamento de grupos via TCP**: Os usuários podem entrar ou sair de grupos utilizando o protocolo TCP. Se um grupo solicitado não existir, ele é criado automaticamente.
- **Envio de mensagens via UDP**: Os usuários enviam mensagens para os grupos utilizando o protocolo UDP.
- **Gerenciamento de clientes**: Lida com múltiplos clientes simultaneamente, mantendo a integridade das sessões de comunicação.

## Requisitos

- Java 8+

## Instalação

1. Clone o repositório:

   ```bash
   git clone https://github.com/reisvitt/instant-messaging_server.git
   cd instant-messaging_server
   ```

2. Der permissão de execução para o start.sh:

```bash
   chmod +x start.sh
```

## Uso

1. Inicie o servidor:

```bash
  ./start.sh
```

Os servidores estarão agora escutando por conexões de clientes. O cliente correspondente deve ser implementado e conectado aos servidores para testar a funcionalidade completa.

## Contribuição

1. Fork este repositório.
2. Crie uma nova branch: `git checkout -b minha-feature`.
3. Faça suas alterações e commit: `git commit -m 'Minha nova feature'`.
4. Envie para a branch original: `git push origin minha-feature`.
5. Crie um pull request.

## Licença

Este projeto está licenciado sob os termos da licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## Contato

Para dúvidas ou sugestões, abra uma issue ou entre em contato com o mantenedor do projeto.
