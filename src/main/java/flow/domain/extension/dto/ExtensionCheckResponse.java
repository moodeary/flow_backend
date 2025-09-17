package flow.domain.extension.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionCheckResponse {
    private String extension;
    private boolean isBlocked;
    private String message;
    private String blockType; // "fixed", "custom", "none"

    public static ExtensionCheckResponse of(String extension, boolean isBlocked, String blockType) {
        String message;
        if (isBlocked) {
            message = switch (blockType) {
                case "fixed" -> "고정 확장자에 있습니다.";
                case "custom" -> "커스텀 확장자에 있습니다.";
                default -> "차단된 확장자입니다.";
            };
        } else {
            message = "허용된 확장자입니다.";
        }

        return ExtensionCheckResponse.builder()
                .extension(extension.toLowerCase())
                .isBlocked(isBlocked)
                .message(message)
                .blockType(blockType)
                .build();
    }

    public static ExtensionCheckResponse of(String extension, boolean isBlocked) {
        return of(extension, isBlocked, "none");
    }
}