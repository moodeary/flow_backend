package flow.domain.extension.controller;

import flow.common.dto.ResponseApi;
import flow.domain.extension.entity.CustomExtension;
import flow.domain.extension.service.ExtensionService;
import flow.domain.extension.entity.FixedExtension;
import flow.domain.extension.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extensions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ExtensionController {

    private final ExtensionService extensionService;

    @GetMapping("/fixed")
    public ResponseEntity<ResponseApi<List<FixedExtension>>> getFixedExtensions() {
        List<FixedExtension> fixedExtensions = extensionService.getAllFixedExtensions();
        String message = String.format("고정 확장자 %d/%d", fixedExtensions.size(), 10);
        return ResponseEntity.ok(ResponseApi.success(fixedExtensions, message));
    }

    @GetMapping("/custom")
    public ResponseEntity<ResponseApi<List<CustomExtension>>> getCustomExtensions() {
        List<CustomExtension> customExtensions = extensionService.getAllCustomExtensions();
        String message = String.format("커스텀 확장자 %d/%d", customExtensions.size(), 200);
        return ResponseEntity.ok(ResponseApi.success(customExtensions, message));
    }

    @PostMapping("/fixed")
    public ResponseEntity<ResponseApi<FixedExtensionResponse>> addFixedExtension(
            @Valid @RequestBody FixedExtensionRequest request) {

        FixedExtension fixedExtension = extensionService.addFixedExtension(
                request.getExtension(), request.getDescription());
        FixedExtensionResponse response = FixedExtensionResponse.from(fixedExtension);
        return ResponseEntity.ok(ResponseApi.success(response));
    }

    @PutMapping("/fixed/{extension}")
    public ResponseEntity<ResponseApi<FixedExtensionResponse>> updateFixedExtensionStatus(
            @PathVariable String extension,
            @RequestParam Boolean isBlocked) {

        FixedExtension updatedExtension = extensionService.updateFixedExtensionStatus(extension, isBlocked);
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
            @Valid @RequestBody CustomExtensionRequest request) {

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
    public ResponseEntity<ResponseApi<Boolean>> checkExtension(@PathVariable String extension) {
        boolean isBlocked = extensionService.isExtensionBlocked(extension);
        String blockType = extensionService.getExtensionBlockType(extension);

        String message;
        if (isBlocked) {
            message = switch (blockType) {
                case "fixed" -> extension + "는 고정 확장자에 있습니다.";
                case "custom" -> extension + "는 커스텀 확장자에 있습니다.";
                default -> extension + "는 차단된 확장자입니다.";
            };
        } else {
            message = extension + "는 허용된 확장자입니다.";
        }

        return ResponseEntity.ok(ResponseApi.success(isBlocked, message));
    }

    @PostMapping("/initialize")
    public ResponseEntity<ResponseApi<String>> initializeFixedExtensions() {
        extensionService.initializeFixedExtensions();
        return ResponseEntity.ok(ResponseApi.success("고정 확장자가 초기화되었습니다."));
    }
}