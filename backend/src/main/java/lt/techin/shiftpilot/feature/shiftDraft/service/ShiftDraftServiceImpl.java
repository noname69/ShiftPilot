package lt.techin.shiftpilot.feature.shiftDraft.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.ResourceNotFoundException;
import lt.techin.shiftpilot.exception.core.BusinessException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.shiftDraft.dto.CreateDraftRequest;
import lt.techin.shiftpilot.feature.shiftDraft.dto.ShiftDraftResponse;
import lt.techin.shiftpilot.feature.shiftDraft.mapper.ShiftDraftMapper;
import lt.techin.shiftpilot.feature.shiftDraft.model.DraftEmployee;
import lt.techin.shiftpilot.feature.shiftDraft.model.ShiftDraft;
import lt.techin.shiftpilot.feature.shiftDraft.repository.DraftEmployeeRepository;
import lt.techin.shiftpilot.feature.shiftDraft.repository.ShiftDraftRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftDraftServiceImpl implements ShiftDraftService{

    private final ShiftDraftRepository shiftDraftRepository;
    private final DraftEmployeeRepository draftEmployeeRepository;
    private final ShiftDraftMapper shiftDraftMapper;
    private final UserRepository userRepository;

    @Override
    public List<ShiftDraftResponse> getAllDrafts() {

        return shiftDraftRepository.findAll().stream()
                .map(shiftDraftMapper::toResponse)
                .toList();

    }

    @Override
    @Transactional
    public ShiftDraftResponse createDraft(CreateDraftRequest request, String username) {

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        ShiftDraft draft = shiftDraftMapper.toEntity(request);
        draft.setCreatedBy(manager);

        List<User> users = userRepository.findAllByIdIn(request.getUserIds().stream().toList());
        List<DraftEmployee> draftEmployees = users.stream()
                .map(user -> {
                    DraftEmployee draftEmployee = new DraftEmployee();
                    draftEmployee.setDraftEmployee(user);
                    draftEmployee.setShiftDraft(draft);
                    return draftEmployeeRepository.save(draftEmployee);
                }).toList();

        draft.setDraftEmployees(draftEmployees);

        ShiftDraft savedDraft;
        try {
            savedDraft = shiftDraftRepository.save(draft);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Draft name already exists.");
        }

        return shiftDraftMapper.toResponse(savedDraft);
    }

    @Override
    public void deleteDraft(Long draftId, String username) {

        ShiftDraft draft = shiftDraftRepository.findById(draftId)
                .orElseThrow(() -> new ResourceNotFoundException("Draft", draftId));

        shiftDraftRepository.delete(draft);

    }
}
