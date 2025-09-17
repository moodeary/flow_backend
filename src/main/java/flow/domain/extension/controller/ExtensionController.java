package flow.domain.extension.controller;

import flow.common.dto.ResponseApi;
import flow.domain.extension.entity.CustomExtension;
import flow.domain.extension.service.ExtensionService;
import flow.domain.extension.entity.FixedExtension;
import flow.domain.extension.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/extensions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ExtensionController {

    private final ExtensionService extensionService;

    @GetMapping("/fixed")
    public ResponseEntity<ResponseApi<Map<String, Object>>> getFixedExtensions() {
        List<FixedExtension> fixedExtensions = extensionService.getAllFixedExtensions();
        List<FixedExtensionResponse> responses = fixedExtensions.stream()
                .map(FixedExtensionResponse::from)
                .toList();
        long count = extensionService.getFixedExtensionCount();

        Map<String, Object> result = Map.of(
                "extensions", responses,
                "count", count,
                "maxCount", 10
        );
        return ResponseEntity.ok(ResponseApi.success(result));
    }

    @GetMapping("/custom")
    public ResponseEntity<ResponseApi<Map<String, Object>>> getCustomExtensions() {
        List<CustomExtension> customExtensions = extensionService.getAllCustomExtensions();
        List<CustomExtensionResponse> responses = customExtensions.stream()
                .map(CustomExtensionResponse::from)
                .toList();
        long count = extensionService.getCustomExtensionCount();

        Map<String, Object> result = Map.of(
                "extensions", responses,
                "count", count,
                "maxCount", 200
        );
        return ResponseEntity.ok(ResponseApi.success(result));
    }

    @PostMapping("/fixed")
    public ResponseEntity<ResponseApi<FixedExtensionResponse>> addFixedExtension(
             @RequestBody FixedExtensionRequest request) {

        FixedExtension fixedExtension = extensionService.addFixedExtension(
                request.getExtension(), request.getDescription());
        FixedExtensionResponse response = FixedExtensionResponse.from(fixedExtension);
        return ResponseEntity.ok(ResponseApi.success(response));
    }

    @PutMapping("/fixed/{extension}")
    public ResponseEntity<ResponseApi<FixedExtensionResponse>> updateFixedExtensionStatus(
            @PathVariable String extension,
            @Valid @RequestBody ExtensionStatusRequest request) {

        FixedExtension updatedExtension = extensionService.updateFixedExtensionStatus(
                extension, request.getIsBlocked());
        FixedExtensionResponse response = FixedExtensionResponse.from(updatedExtension);
        return ResponseEntity.ok(ResponseApi.success(response));
    }

    @DeleteMapping("/fixed/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteFixedExtension(@PathVariable Long id) {
        extensionService.deleteFixedExtension(id);
        return ResponseEntity.ok(ResponseApi.success(null));
    }

    @PostMapping("/custom")
    public ResponseEntity<ResponseApi<CustomExtensionResponse>> addCustomExtension(
             @RequestBody ExtensionRequest request) {

        CustomExtension customExtension = extensionService.addCustomExtension(request.getExtension());
        CustomExtensionResponse response = CustomExtensionResponse.from(customExtension);
        return ResponseEntity.ok(ResponseApi.success(response));
    }

    @DeleteMapping("/custom/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteCustomExtension(@PathVariable Long id) {
        extensionService.deleteCustomExtension(id);
        return ResponseEntity.ok(ResponseApi.success(null));
    }

    @GetMapping("/check/{extension}")
    public ResponseEntity<ResponseApi<ExtensionCheckResponse>> checkExtension(@PathVariable String extension) {
        boolean isBlocked = extensionService.isExtensionBlocked(extension);
        String blockType = extensionService.getExtensionBlockType(extension);
        ExtensionCheckResponse response = ExtensionCheckResponse.of(extension, isBlocked, blockType);
        return ResponseEntity.ok(ResponseApi.success(response));
    }

    @PostMapping("/initialize")
    public ResponseEntity<ResponseApi<String>> initializeFixedExtensions() {
        extensionService.initializeFixedExtensions();
        return ResponseEntity.ok(ResponseApi.success("고정 확장자가 초기화되었습니다."));
    }
}