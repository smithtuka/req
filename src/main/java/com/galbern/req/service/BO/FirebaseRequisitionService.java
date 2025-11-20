package com.galbern.req.service.BO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galbern.req.dto.RequisitionMetaData;
import com.galbern.req.jpa.entities.ApprovalStatus;
import com.galbern.req.jpa.entities.Requisition;
import com.galbern.req.service.RequisitionService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Primary
@Profile("firebase")
@Slf4j
public class FirebaseRequisitionService implements RequisitionService {

    private final FirebaseDatabase firebaseDatabase;
    private final ObjectMapper objectMapper;

    public FirebaseRequisitionService(FirebaseDatabase firebaseDatabase, ObjectMapper objectMapper) {
        this.firebaseDatabase = firebaseDatabase;
        this.objectMapper = objectMapper;
    }

    private DatabaseReference root() {
        return firebaseDatabase.getReference("requisitions");
    }

    @Override
    public List<Requisition> findAllRequisitions() {
        return new ArrayList<>(loadAll().values());
    }

    @Override
    public Requisition createRequisition(Requisition requisition) throws IOException, MessagingException {
        if (requisition.getId() == null) {
            requisition.setId(System.currentTimeMillis());
        }
        root().child(String.valueOf(requisition.getId()))
                .setValueAsync(toMap(requisition));
        return requisition;
    }

    @Override
    public Requisition findRequisitionById(Long requisitionId) {
        try {
            DataSnapshot snapshot = blockingGet(root().child(String.valueOf(requisitionId)));
            if (!snapshot.exists()) {
                return null;
            }
            return objectMapper.convertValue(snapshot.getValue(), Requisition.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while fetching requisition " + requisitionId, e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to fetch requisition " + requisitionId, e);
        }
    }

    @Override
    public List<Requisition> findRequisitions(List<Long> stageIds, List<Long> projectIds, List<Long> requesterIds, ApprovalStatus approvalStatus, Date submissionDate) {
        Collection<Requisition> all = loadAll().values();
        return all.stream()
                .filter(r -> stageIds == null || (r.getStage() != null && stageIds.contains(r.getStage().getId())))
                .filter(r -> projectIds == null || (r.getStage() != null && r.getStage().getProject() != null && projectIds.contains(r.getStage().getProject().getId())))
                .filter(r -> requesterIds == null || (r.getRequester() != null && requesterIds.contains(r.getRequester().getId())))
                .filter(r -> approvalStatus == null || approvalStatus.equals(r.getApprovalStatus()))
                .filter(r -> submissionDate == null || (r.getRequiredDate() != null && sameDay(r.getRequiredDate(), submissionDate)))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteRequisition(Long requisitionId) {
        root().child(String.valueOf(requisitionId)).removeValueAsync();
        return requisitionId + " successfully deleted";
    }

    @Override
    public Requisition updateRequisition(Requisition requisition) {
        if (requisition.getId() == null) {
            throw new IllegalArgumentException("Requisition id is required for update");
        }
        root().child(String.valueOf(requisition.getId())).setValueAsync(toMap(requisition));
        return requisition;
    }

    @Override
    public RequisitionMetaData getRequisitionMetaData(Long id) {
        // Minimal metadata for now; extend as needed.
        Requisition req = findRequisitionById(id);
        if (req == null || req.getStage() == null) {
            return RequisitionMetaData.builder().build();
        }
        return RequisitionMetaData.builder()
                .projectName(req.getStage().getProject() != null ? req.getStage().getProject().getName() : null)
                .stageName(req.getStage().getName())
                .build();
    }

    private Map<String, Requisition> loadAll() {
        try {
            DataSnapshot snapshot = blockingGet(root());
            if (snapshot == null || !snapshot.exists()) {
                return Collections.emptyMap();
            }
            Map<String, Requisition> mapped = new LinkedHashMap<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                mapped.put(child.getKey(), objectMapper.convertValue(child.getValue(), Requisition.class));
            }
            return mapped;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while fetching requisitions", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to fetch requisitions", e);
        }
    }

    private Map<String, Object> toMap(Requisition requisition) {
        return objectMapper.convertValue(requisition, new TypeReference<Map<String, Object>>() {});
    }

    private boolean sameDay(Date a, Date b) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(a);
        Calendar cb = Calendar.getInstance();
        cb.setTime(b);
        return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR)
                && ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR);
    }

    private DataSnapshot blockingGet(DatabaseReference ref) throws ExecutionException, InterruptedException {
        CompletableFuture<DataSnapshot> future = new CompletableFuture<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                future.complete(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future.get();
    }
}
