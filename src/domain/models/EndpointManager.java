/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: EndpointManager
* Funcao...........: Gerenciado de endpoints - APDU's
*************************************************************** */
package src.domain.models;

import java.util.ArrayList;
import java.util.List;

import src.domain.models.abstracts.EndpointListener;

public class EndpointManager {
  private List<EndpointListener> endpoints = new ArrayList<>();

  /*
   * ***************************************************************
   * Metodo: subscribe
   * Funcao: Inserir um novo endpoint na lista de endpoints
   * Parametros: Endpoint a ser inserido
   * Retorno: void
   */
  public void subscribe(EndpointListener endpoint) {
    this.endpoints.add(endpoint);
  }

  /*
   * ***************************************************************
   * Metodo: unsubscribe
   * Funcao: Remove um endpoint na lista de endpoints
   * Parametros: Endpoint a ser removido
   * Retorno: void
   */
  public void unsubscribe(EndpointListener endpoint) {
    this.endpoints.remove(endpoint);
  }

  /*
   * ***************************************************************
   * Metodo: execute
   * Funcao: Com a conexao e a mensagem, verifica qual endpoint dá match
   * Parametros: Servidor, Client e mensagem
   * Retorno: void
   */
  public void execute(Server server, Client client, String data) {
    boolean matched = false;
    for (EndpointListener endpoint : this.endpoints) {
      if (endpoint.isMatch(data)) {
        endpoint.execute(server, client, data);
        matched = true;
        break;
      }
    }
    if (!matched) {
      System.out.println("404 - Endpoit not found: " + data + " for client: " + client.getIp());
    }
  }

  /*
   * ***************************************************************
   * Metodo: getEndpoints
   * Funcao: Retorna todos os endpoints
   * Parametros: void
   * Retorno: Lista de endpoints
   */
  public List<EndpointListener> getEndpoints() {
    return this.endpoints;
  }
}
