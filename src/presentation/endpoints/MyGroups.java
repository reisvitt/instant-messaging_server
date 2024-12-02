package src.presentation.endpoints;

import src.domain.enums.Protocol;
import src.domain.models.Client;
import src.domain.models.Server;
import src.domain.models.abstracts.EndpointListener;
import src.domain.models.abstracts.methods.List;
import src.data.service.GroupService;

public class MyGroups extends EndpointListener {
  private GroupService groupService;

  public MyGroups(GroupService gs) {
    super(new List("LIST GROUPS", "GROUPS$"));
    this.groupService = gs;

  }

  @Override
  public void execute(Server server, Client client, String data) {
    // group names separated by commas
    String groups = this.groupService.listMyGroup(client);

    String dataToSend = data + ":" + groups;
    server.send(Protocol.UDP, client, dataToSend);
  }
}
