package kte.labs.task.services;

import kte.labs.task.dto.ClientDto;
import kte.labs.task.entities.Client;
import kte.labs.task.exceptions.ResourceNotFoundException;
import kte.labs.task.repositories.ClientRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Data
@Service
public class ClientService {

    private final ClientRepository repository;

    public List<ClientDto> getAllClients(){
        List<Client> list = repository.findAll();
        return Client.listDto(list);
    }

    @Transactional
    public boolean setDiscont(long id, long discont1, long discont2){
        Client client = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found, id: " + id));
        client.setIndividualDiscount1(discont1);
        client.setIndividualDiscount2(discont2);
        return true;
    }

    public ClientDto getClientById(long clientId){
        Client client = repository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found, id:" + clientId));
        return new ClientDto(client.getId(), client.getName(), client.getIndividualDiscount1(), client.getIndividualDiscount2());
    }

    public static long getIndividualDiscount(ClientDto clientDto, int products){
        long individualDiscount = clientDto.getIndividualDiscount1();
        if(products >= 5){
            individualDiscount = clientDto.getIndividualDiscount2();
        }
        return individualDiscount;
    }

}
