/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: Group
* Funcao...........: Representação do Grupo
*************************************************************** */
package src.domain.models;

import java.util.ArrayList;
import java.util.Objects;

public class Group {
  private String name;
  private ArrayList<Client> clients;

  public Group(String name) {
    this.name = name;
    this.clients = new ArrayList<Client>();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void addClient(Client client) {
    this.clients.add(client);
  }

  public void removeClient(Client client) {
    this.clients.removeIf(clt -> clt.getIp().equals(client.getIp()));
  }

  public ArrayList<Client> getClients() {
    return this.clients;
  }

  public boolean hasClient(Client clientCompare) {
    for (Client client : this.clients) {
      if (client.getIp().equals(clientCompare.getIp())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Group group = (Group) o;
    return name.equals(group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "Grupo: " + this.name + " clients: " + this.clients;
  }
}
