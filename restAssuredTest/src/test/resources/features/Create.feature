#language: pt

Funcionalidade: Criação de Usuário

  Cenário: Criar um novo usuário com sucesso
    Dado que o usuário possui dados de criação
    Quando o usuário faz uma requisição POST para "/users" com os seguintes dados:
      | name   | job |
      | Marcus | QA    |
    Então a resposta deve ter o código de status 201
    E a resposta deve conter os campos obrigatórios
    E a resposta deve seguir o contrato
