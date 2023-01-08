package kte.labs.task.endpoints;

import kte.labs.task.dto.ClientDto;
import kte.labs.task.services.ClientService;
import kte.labs.task.soap.clients.Client;
import kte.labs.task.soap.clients.GetAllClientsRequest;
import kte.labs.task.soap.clients.GetAllClientsResponse;
import kte.labs.task.soap.clients.GetClientByIdRequest;
import kte.labs.task.soap.clients.GetClientByIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class ClientEndpoint {
    private static final String NAMESPACE_URI = "http://www.user.com/spring/ws/clients";
    private final ClientService clientService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientByIdRequest")
    @ResponsePayload
    public GetClientByIdResponse getClientById(@RequestPayload GetClientByIdRequest request) {
        GetClientByIdResponse response = new GetClientByIdResponse();
        long idClient = request.getId();
        ClientDto client = clientService.getClientById(idClient);
        Client clientDto = new Client();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setIndividualDiscount1((int)client.getIndividualDiscount1());
        clientDto.setIndividualDiscount2((int)client.getIndividualDiscount2());
        response.setClient(clientDto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllClientsRequest")
    @ResponsePayload
    public GetAllClientsResponse getAllClients(@RequestPayload GetAllClientsRequest request) {
        GetAllClientsResponse response = new GetAllClientsResponse();
        clientService.getAllClients().stream().map(C -> {
            Client clientDto = new Client();
            clientDto.setId(C.getId());
            clientDto.setName(C.getName());
            clientDto.setIndividualDiscount1((int)C.getIndividualDiscount1());
            clientDto.setIndividualDiscount2((int)C.getIndividualDiscount2());
            return clientDto;
        }).forEach(response.getClients()::add);
        return response;
    }
}
