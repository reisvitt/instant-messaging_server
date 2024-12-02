
/* ***************************************************************
* Autor............: Vitor Reis
* Matricula........: 201710793
* Inicio...........: 16/08/2024
* Ultima alteracao.: 01/12/2024
* Nome.............: Principal
* Funcao...........: Servidor do MSN - Aplicação de mensagens usando os protocolos TCP e UDP
*************************************************************** */

import src.presentation.Manager;
import src.data.repository.GroupsStore;
import src.data.service.GroupService;

public class Principal {
  public static void main(String[] args) {
    GroupsStore gs = GroupsStore.getInstance();
    GroupService groupService = new GroupService(gs);

    Manager endpointManager = Manager.getInstance();
    endpointManager.build(groupService);
    endpointManager.printEndpoints();

    Servidor servidor = Servidor.getInstance();
    servidor.setManager(endpointManager);
    servidor.init();
  }
}