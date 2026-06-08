package lt.techin.shiftpilot.feature.swaprequest.service;

import lt.techin.shiftpilot.feature.swaprequest.dto.CreateSwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.SwapRequestResponse;

import java.util.List;

public interface SwapRequestService {
    SwapRequestResponse createRequest(
            CreateSwapRequest request,
            String requesterUsername
    );

    void managerRespondFromNotification(Long swapRequestId, boolean decision, String username);

//    List<SwapRequestResponse> getAllRequests();
//    List<SwapRequestResponse> getMyRequests(String username);
}
