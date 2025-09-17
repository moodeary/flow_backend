package flow.domain.extension.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionStatusRequest {

    @NotNull(message = "차단 상태를 설정해주세요.")
    private Boolean isBlocked;
}