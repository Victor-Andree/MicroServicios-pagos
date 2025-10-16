package com.paymentchain.customer.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "this model is used to return errors")
@NoArgsConstructor
@Data

public class StandarizedApiExceptionResponse {


    @Schema(description = "The unique identifier that categorizes the error", name = "type",
    requiredMode = Schema.RequiredMode.REQUIRED, example = "/errorAuthentication/not-authorized")
    private String type;

    @Schema(description = "A brief, human-redeable message about the errors", name = "title",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "The user does not have autorization")
    private String title;


    @Schema(description = "The unique error code", name = "code",
            required= false, example = "192")
    private String code;


    @Schema(description = "A brief, human-redeable message about the errors", name = "detail",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "The user does not have autorization")
    private String detail;


    @Schema(description = "A brief, human-redeable message about the errors", name = "instance",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "The user does not have autorization")
    private String instance;

    public StandarizedApiExceptionResponse(String type , String title, String code, String detail) {
        this.type = type;
        this.title = title;
        this.code = code;
        this.detail = detail;
    }
}
