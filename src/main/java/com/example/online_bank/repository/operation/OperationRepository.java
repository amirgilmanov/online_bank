package com.example.online_bank.repository.operation;

import com.example.online_bank.entity.Operation;
import com.example.online_bank.repository.CustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OperationRepository implements CustomRepository<Operation> {
    private final Map<UUID, Operation> operationMap = new HashMap<>();

    @Override
    public void save(Operation operation) {
        operationMap.put(operation.getId(), operation);
    }

    @Override
    public List<Operation> findAll() {
        return operationMap.values().stream().toList();
    }

    public List<Operation> findByAccountId(String id) {
        return operationMap.values().stream()
                .filter(operation -> operation.getAccountId().equals(id))
                .toList();
    }


}
